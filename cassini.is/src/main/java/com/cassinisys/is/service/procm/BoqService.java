package com.cassinisys.is.service.procm;

import com.cassinisys.is.model.pm.ResourceType;
import com.cassinisys.is.model.procm.*;
import com.cassinisys.is.model.store.ISTopInventory;
import com.cassinisys.is.model.store.ISTopStockMovement;
import com.cassinisys.is.repo.procm.BoqItemRepository;
import com.cassinisys.is.repo.procm.BoqRepository;
import com.cassinisys.is.repo.procm.MachineItemRepository;
import com.cassinisys.is.repo.procm.MaterialItemRepository;
import com.cassinisys.is.repo.store.ISTopInventoryRepository;
import com.cassinisys.is.repo.store.ISTopStockMovementRepository;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The class is for BoqService
 */
@Service
@Transactional
public class BoqService {

    @Autowired
    public ISTopStockMovementRepository topStockMovementRepository;
    @Autowired
    private BoqRepository boqRepository;
    @Autowired
    private BoqItemRepository itemRepository;
    @Autowired
    private MachineItemRepository machineItemRepository;
    @Autowired
    private MaterialItemRepository materialItemRepository;
    @Autowired
    private ISTopInventoryRepository topInventoryRepository;

    /**
     * The method used to getItems for the list of ISBoqItem
     **/
    @Transactional(readOnly = true)
    public List<ISBoqItem> getItems(Integer boqId) {
        checkNotNull(boqId);
        ISBoq boq = boqRepository.findOne(boqId);
        if (boq == null) {
            throw new ResourceNotFoundException();
        }
        List<ISBoqItem> boqItems = itemRepository.findByBoqOrderById(boqId);
        for (ISBoqItem boqItem : boqItems) {
            if (boqItem.getItemType() == ResourceType.MACHINETYPE) {
                ISMachineItem machineItem = machineItemRepository.findByItemNumber(boqItem.getItemNumber());
                boqItem.setResourceTypeName(machineItem.getItemType().getName());
            } else if (boqItem.getItemType() == ResourceType.MATERIALTYPE) {
                ISMaterialItem materialItem = materialItemRepository.findByItemNumber(boqItem.getItemNumber());
                boqItem.setResourceTypeName(materialItem.getItemType().getName());
            }
        }
        return boqItems;
    }

    public List<ISBoqItem> getProjectItems(Integer prjectId, Integer boqId) {
        checkNotNull(boqId);
        ISBoq boq = boqRepository.findOne(boqId);
        Map<String, ISBoqItem> map = new HashMap<>();
        Map<Integer, ISMaterialItem> materialMap = new HashMap<>();
        ISMaterialItem materialItem = new ISMaterialItem();
        if (boq == null) {
            throw new ResourceNotFoundException();
        }
        List<ISBoqItem> boqItemList = itemRepository.findByBoqOrderById(boqId);
        for (ISBoqItem boqItem : boqItemList) {
            boqItem.setIssuedQty(0.0);
            boqItem.setReceivedQty(0.0);
            boqItem.setInventory(0.0);
            boqItem.setTotalBoqQuantity(0.0);
            boqItem.setReturnedQty(0.0);
            if (boqItem.getItemType() == ResourceType.MACHINETYPE) {
                ISMachineItem machineItem = machineItemRepository.findByItemNumber(boqItem.getItemNumber());
                boqItem.setResourceTypeName(machineItem.getItemType().getName());
                map.put(machineItem.getItemNumber(), boqItem);
            } else if (boqItem.getItemType() == ResourceType.MATERIALTYPE) {
                materialItem = materialItemRepository.findByItemNumber(boqItem.getItemNumber());
                boqItem.setResourceTypeName(materialItem.getItemType().getName());
                map.put(materialItem.getItemNumber(), boqItem);
                materialMap.put(materialItem.getId(), materialItem);
            }
            List<ISBoqItem> boqItemList1 = itemRepository.findByProjectAndItemNumber(prjectId, boqItem.getItemNumber());
            for (ISBoqItem boqItem1 : boqItemList1) {
                boqItem.setTotalBoqQuantity(boqItem.getTotalBoqQuantity() + boqItem1.getQuantity());
            }
        }
        List<ISTopInventory> topInventoryList = topInventoryRepository.findByProject(prjectId);
        for (ISTopInventory topInventory : topInventoryList) {
            materialItem = materialMap.get(topInventory.getItem());
            if (materialItem != null) {
                ISBoqItem boqItem = map.get(materialItem.getItemNumber());
                if (boqItem != null) {
                    boqItem.setInventory(boqItem.getInventory() + topInventory.getStoreOnHand());
                }
            }
        }
        List<ISTopStockMovement> issueStockMovementList = topStockMovementRepository.findByProject(prjectId);
        for (ISTopStockMovement stockMovement : issueStockMovementList) {
            materialItem = materialMap.get(stockMovement.getItem());
            if (materialItem != null) {
                ISBoqItem boqItem = map.get(materialItem.getItemNumber());
                if (boqItem != null) {
                    if (stockMovement.getMovementType().equals(MovementType.ISSUED)) {
                        boqItem.setIssuedQty(boqItem.getIssuedQty() + stockMovement.getQuantity());
                    } else if (stockMovement.getMovementType().equals(MovementType.RECEIVED) || stockMovement.getMovementType().equals(MovementType.ALLOCATED) || stockMovement.getMovementType().equals(MovementType.OPENINGBALANCE)) {
                        boqItem.setReceivedQty(boqItem.getReceivedQty() + stockMovement.getQuantity());
                    } else if (stockMovement.getMovementType().equals(MovementType.RETURNED)) {
                        boqItem.setReturnedQty(boqItem.getReturnedQty() + stockMovement.getQuantity());
                    }
                }
            }
        }
        List<ISBoqItem> boqItemList1 = new ArrayList<>();
        boqItemList1 = map.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
        boqItemList1.sort(Comparator.comparing(o -> o.getId()));
        return boqItemList1;
    }

    /**
     * The method used to addItem ISBoqItem
     **/
    @Transactional
    public ISBoqItem addItem(ISBoqItem item) {
        checkNotNull(item);
        item.setId(null);
        return itemRepository.save(item);
    }

    /**
     * The method used to addItems for the list of ISBoqItem
     **/
    @Transactional
    public List<ISBoqItem> addItems(List<ISBoqItem> items) {
        return itemRepository.save(items);
    }

    /**
     * The method used to updateItem of ISBoqItem
     **/
    @Transactional(readOnly = false)
    public ISBoqItem updateItem(ISBoqItem item) {
        checkNotNull(item);
        checkNotNull(item.getId());
        return itemRepository.save(item);
    }

    /**
     * The method used to deleteItem
     **/
    @Transactional(readOnly = false)
    public void deleteItem(Integer itemId) {
        checkNotNull(itemId);
        ISBoqItem item = itemRepository.findOne(itemId);
        if (item == null) {
            throw new ResourceNotFoundException();
        }
        itemRepository.delete(item);
    }

}
