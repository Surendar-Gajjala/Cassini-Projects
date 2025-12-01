package com.cassinisys.drdo.service.failureList;

import com.cassinisys.drdo.model.bom.ItemInstance;
import com.cassinisys.drdo.model.bom.ItemType;
import com.cassinisys.drdo.model.bom.LotInstance;
import com.cassinisys.drdo.model.failureList.FailureList;
import com.cassinisys.drdo.model.failureList.FailureSteps;
import com.cassinisys.drdo.repo.bom.BomItemInstanceRepository;
import com.cassinisys.drdo.repo.bom.ItemInstanceRepository;
import com.cassinisys.drdo.repo.bom.ItemTypeRepository;
import com.cassinisys.drdo.repo.bom.LotInstanceRepository;
import com.cassinisys.drdo.repo.failureList.FailureListRepository;
import com.cassinisys.drdo.repo.failureList.FailureStepsRepository;
import com.cassinisys.drdo.repo.transactions.IssueItemRepository;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.platform.service.core.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by Nageshreddy on 02-01-2019.
 */
@Service
public class FailureListService implements CrudService<FailureList, Integer> {

    @Autowired
    private FailureListRepository failureListRepository;
    @Autowired
    private FailureStepsRepository failureStepsRepository;
    @Autowired
    private ItemInstanceRepository itemInstanceRepository;
    @Autowired
    private ItemTypeRepository itemTypeRepository;
    @Autowired
    private LotInstanceRepository lotInstanceRepository;
    @Autowired
    private BomItemInstanceRepository bomItemInstanceRepository;
    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;
    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;
    @Autowired
    private IssueItemRepository issueItemRepository;

    @Transactional(readOnly = true)
    public FailureList get(Integer id) {
        FailureList failureList = failureListRepository.getOne(id);
        failureList.setFailureSteps(failureStepsRepository.findByFailureListOrderBySerialNo(id));
        return failureList;
    }

    @Transactional(readOnly = true)
    public List<FailureList> getAll() {
        List<FailureList> failureLists = failureListRepository.findAll();
        for (FailureList failureList : failureLists) {
            failureList.setFailureSteps(failureStepsRepository.findByFailureListOrderBySerialNo(failureList.getId()));
        }
        return failureLists;
    }

    @Transactional(readOnly = false)
    public FailureList create(FailureList failureList) {
        List<FailureSteps> failureListStepses = failureList.getFailureSteps();
        failureListStepses = failureStepsRepository.save(failureListStepses);
        FailureList failureList1 = failureListRepository.save(failureList);
        failureList1.setFailureSteps(failureListStepses);
        return failureList1;
    }

    @Transactional(readOnly = false)
    public FailureList update(FailureList failureList) {
        List<FailureSteps> failureSteps1 = failureStepsRepository.findByFailureListOrderBySerialNo(failureList.getId());
        List<FailureSteps> failureSteps = failureList.getFailureSteps();
        if (failureSteps != null) {
            if (failureSteps1.size() > failureSteps.size()) {
                for (FailureSteps failureStep : failureSteps1) {
                    if (!failureSteps.contains(failureStep)) {
                        failureStepsRepository.delete(failureStep.getId());
                    }
                }
            }
        }
        failureSteps = failureStepsRepository.save(failureSteps);
        FailureList failureList1 = failureListRepository.save(failureList);
        failureList1.setFailureSteps(failureSteps);
        return failureList1;
    }

    @Transactional(readOnly = false)
    public void delete(Integer id) {
        if (id != null) {
            failureStepsRepository.deleteByFailureList(id);
            failureListRepository.delete(id);
        }
    }

    @Transactional(readOnly = true)
    public List<FailureSteps> getStepsMultiple(List<Integer> integers) {
        return failureStepsRepository.findByIdIn(integers);
    }

    @Transactional(readOnly = true)
    public Page<ItemInstance> getFailureProcessItems(Pageable pageable) {

        List<ItemInstance> instances = itemInstanceRepository.findFailureProcessItemInstance();
        List<LotInstance> lotInstances = lotInstanceRepository.findFailureProcessLotInstances();
        Map<Integer, List<LotInstance>> integerListMap = new HashMap();

        for (LotInstance lotInstance : lotInstances) {
            List<LotInstance> lotInstances1 = integerListMap.containsKey(lotInstance.getInstance()) ? integerListMap.get(lotInstance.getInstance()) : new ArrayList();

            lotInstances1.add(lotInstance);
            integerListMap.put(lotInstance.getInstance(), lotInstances1);
        }

        Iterator it = integerListMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            ItemInstance itemInstance = itemInstanceRepository.findOne((Integer) pair.getKey());
            itemInstance.setLotInstanceList((List) pair.getValue());
            instances.add(itemInstance);
        }

        Page<ItemInstance> itemInstancePage = new PageImpl<ItemInstance>(instances, pageable, instances.size());
        for (ItemInstance itemInstance : itemInstancePage.getContent()) {
            ItemType itemType = itemTypeRepository.findOne(itemInstance.getItem().getItemMaster().getItemType().getParentType());
            if (!itemType.getParentNode()) {
                itemInstance.getItem().getItemMaster().setParentType(itemType);
            } else {
                itemInstance.getItem().getItemMaster().setParentType(itemInstance.getItem().getItemMaster().getItemType());
            }

            ObjectTypeAttribute certificateNumberType = objectTypeAttributeRepository.findByNameAndObjectType("Certificate Number", ObjectType.valueOf("ITEMINSTANCE"));

            if (certificateNumberType != null) {
                ObjectAttribute certificateNumber = objectAttributeRepository.findByObjectIdAndAttributeDefId(itemInstance.getId(), certificateNumberType.getId());

                if (certificateNumber != null && certificateNumber.getStringValue() != null) {
                    itemInstance.setCertificateNumber(certificateNumber.getStringValue());
                }
            }
        }

        return itemInstancePage;
    }

}
