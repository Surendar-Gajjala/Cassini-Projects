package com.cassinisys.drdo.service.failureList;

import com.cassinisys.drdo.model.bom.*;
import com.cassinisys.drdo.model.failureList.DRDOFailureValueList;
import com.cassinisys.drdo.model.failureList.FailureSteps;
import com.cassinisys.drdo.model.inventory.ItemAllocation;
import com.cassinisys.drdo.model.inventory.Storage;
import com.cassinisys.drdo.model.inventory.StorageItem;
import com.cassinisys.drdo.repo.bom.*;
import com.cassinisys.drdo.repo.failureList.FailureStepsRepository;
import com.cassinisys.drdo.repo.failureList.FailureValueListRepository;
import com.cassinisys.drdo.repo.inventory.ItemAllocationRepository;
import com.cassinisys.drdo.repo.inventory.StorageItemRepository;
import com.cassinisys.drdo.repo.inventory.StorageRepository;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.security.SessionWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Nageshreddy on 02-01-2019.
 */
@Service
public class FailureValueListService implements CrudService<DRDOFailureValueList, Integer> {

    @Autowired
    private FailureValueListRepository failureValueListRepository;

    @Autowired
    private FailureStepsRepository failureStepsRepository;

    @Autowired
    private SessionWrapper sessionWrapper;

    @Autowired
    private BomItemInstanceRepository bomItemInstanceRepository;

    @Autowired
    private ItemAllocationRepository itemAllocationRepository;

    @Autowired
    private BomInstanceItemRepository bomInstanceItemRepository;

    @Autowired
    private BomInstanceRepository bomInstanceRepository;

    @Autowired
    private StorageItemRepository storageItemRepository;

    @Autowired
    private ItemInstanceRepository itemInstanceRepository;

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private ItemInstanceStatusHistoryRepository instanceStatusHistoryRepository;

    @Autowired
    private LotInstanceHistoryRepository lotInstanceHistoryRepository;

    @Autowired
    private LotInstanceRepository lotInstanceRepository;

    @Override
    @Transactional(readOnly = true)
    public DRDOFailureValueList get(Integer id) {
        return failureValueListRepository.getOne(id);
    }

    @Override
    public DRDOFailureValueList create(DRDOFailureValueList failureValueList) {
        DRDOFailureValueList failureValueList1 = failureValueListRepository.findByInstanceAndFailureStep(failureValueList.getInstance(), failureValueList.getFailureStep());
        if (failureValueList1 != null) {
            failureValueList1.setCheckedDate(new Date());
            failureValueList1.setValue(failureValueList.getValue());
            failureValueList1.setCheckedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
            return failureValueListRepository.save(failureValueList1);
        } else {
            failureValueList.setValue(failureValueList.getValue());
            failureValueList.setCheckedDate(new Date());
            failureValueList.setCheckedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
            return failureValueListRepository.save(failureValueList);
        }
    }

    public DRDOFailureValueList create(Integer instanceId, DRDOFailureValueList failureValueList) {
        DRDOFailureValueList failureValueList1 = failureValueListRepository.findByInstanceAndFailureStep(failureValueList.getInstance(), failureValueList.getFailureStep());
        FailureSteps failureSteps = failureStepsRepository.findOne(failureValueList.getFailureStep());
        if (failureSteps.getStatus().equals("STORES")) {

            ItemInstance itemInstance = itemInstanceRepository.findOne(failureValueList.getInstance());
            List<StorageItem> returnStorages = new ArrayList<>();
            if (itemInstance.getSection() == null) {
                returnStorages = storageItemRepository.getItemReturnStoragesByBomAndSectionIsNull(itemInstance.getBom(), itemInstance.getUniqueCode());
            } else {
                returnStorages = storageItemRepository.getItemReturnStoragesByBomAndSection(itemInstance.getBom(), itemInstance.getUniqueCode(), itemInstance.getSection());
            }

            if (returnStorages.size() == 0) {
                throw new CassiniException("No Return Storage available for : " + itemInstance.getItem().getItemMaster().getItemName());
            }

            Storage matchingLocation = null;

            for (StorageItem storageItem : returnStorages) {
                if (itemInstance.getItem().getItemMaster().getItemType().getHasLots()) {
                    if (storageItem.getStorage().getRemainingCapacity() >= itemInstance.getLotSize()) {
                        matchingLocation = storageItem.getStorage();
                        break;
                    }
                } else {
                    if (storageItem.getStorage().getRemainingCapacity() >= 1.0) {
                        matchingLocation = storageItem.getStorage();
                        break;
                    }
                }
            }

            if (matchingLocation != null) {
                if (!itemInstance.getItem().getItemMaster().getItemType().getHasLots()) {
                    matchingLocation.setRemainingCapacity(matchingLocation.getRemainingCapacity() - 1);
                } else {
                    matchingLocation.setRemainingCapacity(matchingLocation.getRemainingCapacity() - itemInstance.getLotSize());
                }
            }

            matchingLocation = storageRepository.save(matchingLocation);

            itemInstance.setStorage(matchingLocation);

            itemInstance = itemInstanceRepository.save(itemInstance);


            BomItemInstance bomItemInstance = bomItemInstanceRepository.findByItemInstance(instanceId);
            if (bomItemInstance == null) {
                LotInstance lotInstance = lotInstanceRepository.findOne(instanceId);
                bomItemInstance = bomItemInstanceRepository.findByItemInstance(lotInstance.getInstance());
                lotInstance.setStatus(ItemInstanceStatus.FAILURE);
                lotInstance.setPresentStatus("FAILURE");
                lotInstance = lotInstanceRepository.save(lotInstance);
                LotInstanceHistory lotInstanceHistory = new LotInstanceHistory();
                lotInstanceHistory.setStatus(ItemInstanceStatus.FAILURE);
                lotInstanceHistory.setLotInstance(lotInstance.getId());
                lotInstanceHistory.setTimestamp(new Date());
                lotInstanceHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());
                lotInstanceHistoryRepository.save(lotInstanceHistory);
            } else {
                itemInstance.setStatus(ItemInstanceStatus.FAILURE);
                itemInstance.setPresentStatus("FAILURE");
                itemInstance = itemInstanceRepository.save(itemInstance);
                ItemInstanceStatusHistory statusHistory = new ItemInstanceStatusHistory();
                statusHistory.setItemInstance(itemInstance);
                statusHistory.setStatus(ItemInstanceStatus.FAILURE);
                statusHistory.setPresentStatus("FAILURE");
                statusHistory.setTimestamp(new Date());
                statusHistory.setComment(itemInstance.getReason());
                statusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());
                instanceStatusHistoryRepository.save(statusHistory);
            }

            BomInstance bomInstance = getBomInstanceByInstanceItem(bomItemInstance);
            ItemAllocation itemAllocation = itemAllocationRepository.findByBomInstanceAndBomInstanceItem(bomInstance.getId(), bomItemInstance.getBomInstanceItem());
            itemAllocation.setFailedQty(itemAllocation.getFailedQty() + 1);
            itemAllocationRepository.save(itemAllocation);
        }
        if (failureValueList1 != null) {
            failureValueList1.setCheckedDate(new Date());
            failureValueList1.setValue(failureValueList.getValue());
            failureValueList1.setCheckedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
            return failureValueListRepository.save(failureValueList1);
        } else {
            failureValueList.setValue(failureValueList.getValue());
            failureValueList.setCheckedDate(new Date());
            failureValueList.setCheckedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
            return failureValueListRepository.save(failureValueList);
        }
    }

    private BomInstance getBomInstanceByInstanceItem(BomItemInstance bomItemInstance) {
        BomInstance bomInstance = null;
        BomInstanceItem bomInstanceItem = bomInstanceItemRepository.findOne(bomItemInstance.getBomInstanceItem());

        BomInstanceItem parent = bomInstanceItemRepository.findOne(bomInstanceItem.getParent());
        if (parent.getBomItemType().equals(BomItemType.UNIT)) {

            BomInstanceItem parent1 = bomInstanceItemRepository.findOne(parent.getParent());
            if (parent1.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                BomInstanceItem parent2 = bomInstanceItemRepository.findOne(parent1.getParent());

                if (parent2.getBomItemType().equals(BomItemType.SECTION) || parent2.getBomItemType().equals(BomItemType.COMMONPART)) {
                    bomInstance = bomInstanceRepository.findOne(parent2.getBom());
                }
            }
        } else if (parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
            BomInstanceItem parent2 = bomInstanceItemRepository.findOne(parent.getParent());

            if (parent2.getBomItemType().equals(BomItemType.SECTION) || parent2.getBomItemType().equals(BomItemType.COMMONPART)) {
                bomInstance = bomInstanceRepository.findOne(parent2.getBom());
            }
        } else if (parent.getBomItemType().equals(BomItemType.COMMONPART)) {
            bomInstance = bomInstanceRepository.findOne(parent.getBom());
        }
        return bomInstance;
    }

    @Override
    public DRDOFailureValueList update(DRDOFailureValueList failureValueList) {
        return null;
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public List<DRDOFailureValueList> getAll() {
        return null;
    }

    @Transactional(readOnly = false)
    public List<DRDOFailureValueList> saveAll(List<DRDOFailureValueList> failValueLists) {
        for (DRDOFailureValueList failureValueList : failValueLists) {
            failureValueList.setCheckedDate(new Date());
        }
        return failureValueListRepository.save(failValueLists);
    }

    @Transactional(readOnly = true)
    public List<DRDOFailureValueList> getByItem(Integer item) {
        return failureValueListRepository.findByItemOrderByIdAsc(item);
    }

    @Transactional(readOnly = true)
    public List<DRDOFailureValueList> findByItemAndInstanceOrderByIdAsc(Integer item, Integer upn) {
        return failureValueListRepository.findByItemAndInstanceOrderByIdAsc(item, upn);
    }

    @Transactional(readOnly = true)
    public List<DRDOFailureValueList> findByItemAndLotInstanceOrderByIdAsc(Integer item, Integer upn) {
        return failureValueListRepository.findByItemAndLotInstanceOrderByIdAsc(item, upn);
    }

}
