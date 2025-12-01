package com.cassinisys.is.service.store;

import com.cassinisys.is.filtering.MaterialCriteria;
import com.cassinisys.is.filtering.MaterialPredicateBuilder;
import com.cassinisys.is.filtering.TopInventoryCriteria;
import com.cassinisys.is.filtering.TopInventoryPredicateBuilder;
import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.is.model.pm.ISProject;
import com.cassinisys.is.model.pm.ISProjectResource;
import com.cassinisys.is.model.pm.ResourceType;
import com.cassinisys.is.model.procm.*;
import com.cassinisys.is.model.procm.dto.InventoryListDTO;
import com.cassinisys.is.model.procm.dto.ProjectInventoryDTO;
import com.cassinisys.is.model.procm.dto.StoreInventoryDTO;
import com.cassinisys.is.model.store.ISTopInventory;
import com.cassinisys.is.model.store.ISTopStockMovement;
import com.cassinisys.is.model.store.ISTopStore;
import com.cassinisys.is.model.tm.ISProjectTask;
import com.cassinisys.is.repo.pm.ProjectRepository;
import com.cassinisys.is.repo.pm.ResourceRepository;
import com.cassinisys.is.repo.procm.BoqItemRepository;
import com.cassinisys.is.repo.procm.MachineItemRepository;
import com.cassinisys.is.repo.procm.MaterialItemRepository;
import com.cassinisys.is.repo.store.ISTopInventoryRepository;
import com.cassinisys.is.repo.store.ISTopStockMovementRepository;
import com.cassinisys.is.repo.store.ISTopStoreRepository;
import com.cassinisys.is.repo.tm.ProjectTaskRepository;
import com.cassinisys.is.service.pm.MaterialShortageDTO;
import com.cassinisys.is.service.procm.ItemDTO;
import com.cassinisys.is.service.procm.ItemService;
import com.cassinisys.platform.service.core.CrudService;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by subramanyamreddy on 017 17-Nov -17.
 */
@Service
public class TopInventoryService implements CrudService<ISTopInventory, Integer> {

    @Autowired
    public BoqItemRepository boqItemRepository;
    @Autowired
    public ISTopStockMovementRepository topStockMovementRepository;
    @Autowired
    public TopStoreService topStoreService;
    @Autowired
    public ProjectTaskRepository taskRepository;
    @Autowired
    private ISTopInventoryRepository topInventoryRepository;
    @Autowired
    private ISTopStoreRepository topStoreRepository;
    @Autowired
    private ItemService itemService;
    @Autowired
    private TopInventoryPredicateBuilder topInventoryPredicateBuilder;
    @Autowired
    private MachineItemRepository machineItemRepository;
    @Autowired
    private MaterialItemRepository materialItemRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private MaterialPredicateBuilder materialPredicateBuilder;

    @Override
    @Transactional(readOnly = false)
    public ISTopInventory create(ISTopInventory inventory) {
        return topInventoryRepository.save(inventory);
    }

    @Override
    @Transactional(readOnly = false)
    public ISTopInventory update(ISTopInventory inventory) {
        return topInventoryRepository.save(inventory);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        topInventoryRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ISTopInventory get(Integer id) {
        return topInventoryRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ISTopInventory> getAll() {
        return topInventoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<ISTopInventory> getInventoryByItem(String itemNumber) {
        Integer id = 0;
        ISMachineItem machineItem = machineItemRepository.findByItemNumber(itemNumber);
        ISMaterialItem materialItem = materialItemRepository.findByItemNumber(itemNumber);
        if (machineItem != null) {
            id = machineItem.getId();
        } else {
            id = materialItem.getId();
        }
        List<ISTopInventory> topInventories1 = new ArrayList<>();
        List<ISTopStore> topStores = topStoreRepository.findAll();
        for (ISTopStore topStore : topStores) {
            List<ISTopInventory> topInventories = new ArrayList<>();
            Map<Integer, ISTopInventory> map = new HashMap<>();
            topInventories = topInventoryRepository.findByItemAndStore(id, topStore);
            if (topInventories.size() > 0) {
                for (ISTopInventory topInventory : topInventories) {
                    ISTopInventory store = map.get(topInventory.getStore().getId());
                    if (store != null) {
                        ISTopInventory topInventory1 = map.get(topInventory.getStore().getId());
                        topInventory1.setStoreOnHand(topInventory1.getStoreOnHand() + topInventory.getStoreOnHand());
                        map.put(topInventory.getStore().getId(), topInventory1);
                    } else {
                        topInventories1.add(topInventory);
                        map.put(topInventory.getStore().getId(), topInventory);
                    }
                }
            }
        }
        for (ISTopInventory topInventory : topInventories1) {
            topInventory.setStockMovementDTO(topStoreService.getStockIssuedByItem(topInventory.getItem(), topInventory.getStore()));
        }
        return topInventories1;
    }

    @Transactional(readOnly = true)
    public List<ISTopInventory> getInventoryByItemNumberAndProject(Integer projectId, String itemNumber) {
        List<ISBoqItem> boqItems = boqItemRepository.findByProjectAndItemNumber(projectId, itemNumber);
        List<String> materialNumberList = new ArrayList<>();
        List<String> machineNumberList = new ArrayList<>();
        List<Integer> itemIds = new ArrayList<>();
        for (ISBoqItem boqItem : boqItems) {
            if (boqItem.getItemType() == ResourceType.MACHINETYPE) {
                machineNumberList.add(boqItem.getItemNumber());
            } else {
                materialNumberList.add(boqItem.getItemNumber());
            }
        }
        List<ISMachineItem> machineItems = machineItemRepository.findByItemNumberIn(machineNumberList);
        List<ISMaterialItem> materialItems = materialItemRepository.findByItemNumberIn(materialNumberList);
        for (ISMachineItem machineItem : machineItems) {
            itemIds.add(machineItem.getId());
        }
        for (ISMaterialItem materialItem : materialItems) {
            itemIds.add(materialItem.getId());
        }
        List<ISTopInventory> topInventories1 = new ArrayList<>();
        Map<Integer, ISTopInventory> map = new HashMap<>();
        List<ISTopInventory> topInventories = topInventoryRepository.findByProjectAndItemIn(projectId, itemIds);
        for (ISTopInventory topInventory : topInventories) {
            ISTopInventory store = map.get(topInventory.getStore().getId());
            if (store != null) {
                ISTopInventory topInventory1 = map.get(topInventory.getStore().getId());
                topInventory1.setStoreOnHand(topInventory1.getStoreOnHand() + topInventory.getStoreOnHand());
                map.put(topInventory.getStore().getId(), topInventory1);
            } else {
                topInventories1.add(topInventory);
                map.put(topInventory.getStore().getId(), topInventory);
            }
        }
        for (ISTopInventory topInventory : topInventories1) {
            topInventory.setStockMovementDTO(topStoreService.getStockIssuedByItem(topInventory.getItem(), topInventory.getStore()));
        }
        return topInventories1;
    }

    @Transactional(readOnly = true)
    public List<ISTopInventory> getInventoryByStore(ISTopStore topStore) {
        return topInventoryRepository.findByStore(topStore);
    }

    @Transactional(readOnly = true)
    public List<ISTopInventory> getInventoryByBoqItemNumber(String itemNumber, Integer boqId, Integer projectId) {
        ISBoqItem boqItem = boqItemRepository.findByBoqAndItemNumber(boqId, itemNumber);
        ISMachineItem machineItem = machineItemRepository.findByItemNumber(boqItem.getItemNumber());
        ISMaterialItem materialItem = materialItemRepository.findByItemNumber(boqItem.getItemNumber());
        List<ISTopInventory> topInventories = new ArrayList<>();
        if (machineItem != null) {
            topInventories = topInventoryRepository.findByItemAndProject(machineItem.getId(), projectId);
        } else {
            topInventories = topInventoryRepository.findByItemAndProject(materialItem.getId(), projectId);
        }
        return topInventories;
    }

    //    public ISTopInventory getInventoryByBoq(Integer boqId) {
//        return topInventoryRepository.findByBoqItem(boqId);
//    }
    @Transactional(readOnly = true)
    public ISTopInventory getInventoryByItemAndStore(Integer projectId, Integer itemId, Integer storeId) {
        ISTopStore topStore = topStoreRepository.findOne(storeId);
        return topInventoryRepository.findByStoreAndItemAndProject(topStore, itemId, projectId);
    }

    @Transactional(readOnly = true)
    public Page<ISTopInventory> getStoreInventoryforLoan(Integer projectId, Integer storeId, Pageable pageable) {
        List<ISTopInventory> topInventories = new ArrayList<>();
        Map<Integer, ISTopInventory> map = new HashMap<>();
        Page<ISTopInventory> inventories = topInventoryRepository.findByStoreAndProjectOrderByItem(topStoreRepository.findOne(storeId), projectId, pageable);
        for (ISTopInventory inventory : inventories.getContent()) {
            ItemDTO itemDTO = new ItemDTO();
            ISMaterialItem materialItem = materialItemRepository.findOne(inventory.getItem());
            if (map.get(inventory.getItem()) != null) {
                ISTopInventory topInventory = map.get(inventory.getItem());
                topInventory.setStoreOnHand(topInventory.getStoreOnHand() + inventory.getStoreOnHand());
            } else {
                itemDTO.setDescription(materialItem.getDescription());
                itemDTO.setId(materialItem.getId());
                itemDTO.setItemNumber(materialItem.getItemNumber());
                itemDTO.setItemName(materialItem.getItemName());
                itemDTO.setItemType("MATERILATYPE");
                itemDTO.setResourceType(materialItem.getItemType().getName());
                itemDTO.setResourceType(itemService.getMaterialTypeByItemNumber(materialItem.getItemNumber()).getName());
            }
            inventory.setItemDTO(itemDTO);
            if (inventory.getStoreOnHand() > 0) {
                topInventories.add(inventory);
            }
        }
        return inventories;
    }

    public List<MaterialShortageDTO> getProjectMaterialShortageDTO(Integer projectId) {
        List<MaterialShortageDTO> materialShortageDTOs = new ArrayList<>();
        List<ItemDTO> itemDTOList = new ArrayList<>();
        List<Integer> boqIds = new ArrayList<>();
        List<ISProjectTask> taskList = taskRepository.findByProject(projectId);
        for (ISProjectTask projectTask : taskList) {
            List<ISProjectResource> projectResourceList = resourceRepository.findByProjectAndTask(projectId, projectTask.getId());
            for (ISProjectResource projectResource : projectResourceList) {
                boqIds.add(projectResource.getReferenceId());
            }
            itemDTOList = itemService.getItemsByBoqIds(projectTask.getId(), boqIds);
            List<ISTopInventory> topInventoryList = topInventoryRepository.findByProject(projectId);
            for (ItemDTO itemDTO : itemDTOList) {
                MaterialShortageDTO materialShortageDTO = new MaterialShortageDTO();
                for (ISTopInventory inventory : topInventoryList) {
                    if (itemDTO.getId().equals(inventory.getItem())) {
                        itemDTO.setStoreInventory(inventory.getStoreOnHand());
                        if (itemDTO.getProjectResource().getQuantity() > (itemDTO.getStoreInventory() + itemDTO.getItemIssueQuantity())) {
                            itemDTO.setShortage(itemDTO.getProjectResource().getQuantity() - itemDTO.getStoreInventory() - itemDTO.getItemIssueQuantity());
                        } else {
                            itemDTO.setShortage(0.0);
                        }
                        materialShortageDTO.setInventoryQty(itemDTO.getStoreInventory());
                        materialShortageDTO.setShortage(itemDTO.getShortage());
                        materialShortageDTO.setResourceQty(itemDTO.getResourceQuantity());
                        materialShortageDTO.setTaskName(projectTask.getName());
                        materialShortageDTO.setItemNumber(itemDTO.getItemNumber());
                        materialShortageDTOs.add(materialShortageDTO);
                    }
                }

            }
        }
        return materialShortageDTOs;
    }

    @Transactional(readOnly = true)
    public List<ISTopInventory> getInventoryByIds(List<Integer> ids) {
        return topInventoryRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    public Page<ISTopInventory> getInventoryByFilters(TopInventoryCriteria topInventoryCriteria, Pageable pageable) {
        Predicate predicate = topInventoryPredicateBuilder.getMaterialItemPredicate(topInventoryCriteria, QISMaterialItem.iSMaterialItem);
        Map<String, ISTopInventory> hashMap = new HashMap<>();
        List<ISTopInventory> topInventories = new ArrayList<>();
        Page<ISTopInventory> topInventories1 = topInventoryRepository.findAll(predicate, pageable);
        for (ISTopInventory inventory : topInventories1.getContent()) {
            ISMaterialItem materialItem = new ISMaterialItem();
            materialItem = materialItemRepository.findOne(inventory.getItem());
            if (materialItem != null) {
                ISTopInventory topInventory1 = hashMap.get(materialItem.getItemNumber());
                if (topInventory1 != null) {
                    if (!topInventory1.equals(inventory)) {
                        topInventory1.setStoreOnHand(topInventory1.getStoreOnHand() + inventory.getStoreOnHand());
                    }
                } else {
                    hashMap.put(materialItem.getItemNumber(), inventory);
                    topInventories.add(inventory);
                }
            }
        }
        for (ISTopInventory topInventory : topInventories) {
            topInventory.setStockMovementDTO(topStoreService.getStockIssuedByItem(topInventory.getItem(), topInventory.getStore()));
        }
        return new PageImpl<ISTopInventory>(topInventories, pageable, topInventories1.getTotalElements());
    }

    @Transactional(readOnly = true)
    public List<InventoryListDTO> getCustomInventories(Pageable pageable) {
        Boolean first = true;
        Boolean last = false;
        Long totalPages = 0l;
        List<Integer> itemIds = topInventoryRepository.findDisItems();
        totalPages = Long.valueOf(itemIds.size() / pageable.getPageSize());
        if (itemIds.size() % pageable.getPageSize() > 0) {
            totalPages = totalPages + 1;
        }
        List<InventoryListDTO> inventoryListDTOs = new ArrayList<InventoryListDTO>();
        List<ISProject> projects = projectRepository.findAll();
        int start = 0;
        Long end = 0l;
        if (pageable.getPageNumber() == 0) {
            start = 0;
            first = true;
        } else {
            start = (pageable.getPageNumber() * pageable.getPageSize()) + 1;
            first = false;
        }
        end = Long.valueOf(pageable.getPageSize());
        if (end == 0) {
            end = Long.valueOf(pageable.getPageSize());
        } else if (end > itemIds.size()) {
            end = Long.valueOf(itemIds.size());
            last = true;
        }
        if (start <= itemIds.size()) {
            for (int i = start; i < end; i++) {
                Integer itemId = itemIds.get(i);
                InventoryListDTO inventoryListDTO = new InventoryListDTO();
                inventoryListDTO.setFirst(first);
                inventoryListDTO.setLast(last);
                inventoryListDTO.setTotalPages(totalPages);
                inventoryListDTO.setTotalElements(Long.valueOf(itemIds.size()));
                inventoryListDTO.setNumberOfElements(end.intValue());
                inventoryListDTO.setProjectInvQtyTotal(0.0);
                inventoryListDTO.setTotalQuantity(0.0);
                List<StoreInventoryDTO> storeInventoryDTOs = new ArrayList<>();
                List<ProjectInventoryDTO> projectInventoryDTOs = new ArrayList<>();
                ISMaterialItem materialItem = materialItemRepository.findOne(itemId);
                if (materialItem != null) {
                    inventoryListDTO.setItemType(materialItem.getItemType().getName());
                    inventoryListDTO.setResourceType("MATERIALTYPE");
                    inventoryListDTO.setDescription(materialItem.getDescription());
                    inventoryListDTO.setItemName(materialItem.getItemName());
                    inventoryListDTO.setItemNumber(materialItem.getItemNumber());
                    inventoryListDTO.setUnits(materialItem.getUnits());
                }
                // total inventory
                List<ISTopInventory> topInventories = topInventoryRepository.findByItem(itemId);
                for (ISTopInventory topInventory : topInventories) {
                    inventoryListDTO.setTotalQuantity(inventoryListDTO.getTotalQuantity() + topInventory.getStoreOnHand());
                }
                for (ISProject project : projects) {
                    List<ISTopInventory> topInventoryList = topInventoryRepository.findByItemAndProject(itemId, project.getId());
                    ProjectInventoryDTO projectInventoryDTO = new ProjectInventoryDTO();
                    projectInventoryDTO.setQuantity(0.0);
                    projectInventoryDTO.setProjectId(project.getId());
                    projectInventoryDTO.setProjectName(project.getName());
                    projectInventoryDTO.setQuantity(0.0);
                    for (ISTopInventory topInventory : topInventoryList) {
                        projectInventoryDTO.setQuantity(topInventory.getStoreOnHand() + projectInventoryDTO.getQuantity());
                        inventoryListDTO.setProjectInvQtyTotal(topInventory.getStoreOnHand() + inventoryListDTO.getProjectInvQtyTotal());
                    }
                    projectInventoryDTOs.add(projectInventoryDTO);
                }
                inventoryListDTO.setProjectInventoryDetailsList(projectInventoryDTOs);
                //store inventory
                List<ISTopStore> topStores = topInventoryRepository.findDisStores();
                for (ISTopStore store : topStores) {
                    ISTopStore topStore = topStoreRepository.findOne(store.getId());
                    StoreInventoryDTO storeInventoryDTO = new StoreInventoryDTO();
                    storeInventoryDTO.setTotalQuantity(0.0);
                    storeInventoryDTO.setStoreId(store.getId());
                    storeInventoryDTO.setStoreName(topStore.getStoreName());
                    List<ProjectInventoryDTO> stoteProjectInventoryDTOs = new ArrayList<>();
                    List<ISTopInventory> topInventories1 = topInventoryRepository.findByItemAndStore(itemId, topStoreRepository.findOne(store.getId()));
                    for (ISTopInventory topInventory : topInventories1) {
                        storeInventoryDTO.setTotalQuantity(storeInventoryDTO.getTotalQuantity() + topInventory.getStoreOnHand());
                    }
                    for (ISProject project : projects) {
                        ISTopInventory topInventory = topInventoryRepository.findByStoreAndItemAndProject(topStore, itemId, project.getId());
                        ProjectInventoryDTO projectInventoryDTO = new ProjectInventoryDTO();
                        projectInventoryDTO.setProjectId(project.getId());
                        projectInventoryDTO.setProjectName(project.getName());
                        projectInventoryDTO.setQuantity(0.0);
                        if (topInventory != null) {
                            projectInventoryDTO.setQuantity(topInventory.getStoreOnHand());
                        }
                        stoteProjectInventoryDTOs.add(projectInventoryDTO);
                    }
                    storeInventoryDTO.setProjectStoreInvDetailsList(stoteProjectInventoryDTOs);
                    storeInventoryDTOs.add(storeInventoryDTO);
                }
                inventoryListDTO.setStoreDetailsList(storeInventoryDTOs);
                inventoryListDTOs.add(inventoryListDTO);
            }
        }
        return inventoryListDTOs;

    }

    @Transactional(readOnly = true)
    public List<ItemDTO> getUnallocatedProjectInventoryBoq(Integer projectId, Integer storeId) {
        List<ItemDTO> itemDTOs = new ArrayList<>();
        List<ISTopInventory> topInventories = topInventoryRepository.findByStoreAndProjectIsNull(topStoreRepository.findOne(storeId));
        List<ISBoqItem> boqItems = boqItemRepository.findByProject(projectId);
        for (ISTopInventory topInventory : topInventories) {
            ISMaterialItem materialItem = materialItemRepository.findOne(topInventory.getItem());
            HashMap<String, ISBoqItem> hashMap = new HashMap<>();
            for (ISBoqItem boqItem : boqItems) {
                if (hashMap.get(boqItem.getItemNumber()) == null) {
                    if (boqItem.getItemNumber().equals(materialItem.getItemNumber())) {
                        ItemDTO itemDTO = new ItemDTO();
                        itemDTO.setDescription(boqItem.getDescription());
                        itemDTO.setItemName(boqItem.getItemName());
                        itemDTO.setItemNumber(boqItem.getItemNumber());
                        itemDTO.setStoreInventory(topInventory.getStoreOnHand());
                        itemDTO.setId(materialItem.getId());
                        itemDTOs.add(itemDTO);
                    }
                    hashMap.put(boqItem.getItemNumber(), boqItem);
                }
            }
        }
        return itemDTOs;
    }

    @Transactional(readOnly = false)
    public void allocateItemsToProject(Integer projectId, Integer storeId, List<ItemDTO> itemDTOs) {
        for (ItemDTO itemDTO : itemDTOs) {
            ISTopInventory topInventory = topInventoryRepository.findByStoreAndItemAndProjectIsNull(topStoreRepository.findOne(storeId), itemDTO.getId());
            topInventory.setStoreOnHand(topInventory.getStoreOnHand() - itemDTO.getItemIssueQuantity());
            topInventoryRepository.save(topInventory);
            ISTopInventory inventory = topInventoryRepository.findByStoreAndItemAndProject(topStoreRepository.findOne(storeId), itemDTO.getId(), projectId);
            if (inventory == null) {
                ISTopInventory inventory1 = new ISTopInventory();
                inventory1.setStore(topStoreRepository.findOne(storeId));
                inventory1.setProject(projectId);
                inventory1.setStoreOnHand(itemDTO.getItemIssueQuantity());
                inventory1.setItem(itemDTO.getId());
                topInventoryRepository.save(inventory1);
            } else {
                inventory.setStoreOnHand(inventory.getStoreOnHand() + itemDTO.getItemIssueQuantity());
                topInventoryRepository.save(inventory);
            }
            ISTopStockMovement topStockMovement = new ISTopStockMovement(ISObjectType.RECEIVEITEM);
            topStockMovement.setItem(itemDTO.getId());
            topStockMovement.setQuantity(itemDTO.getItemIssueQuantity());
            topStockMovement.setStore(topStoreRepository.findOne(storeId));
            topStockMovement.setTimeStamp(new Date());
            topStockMovement.setProject(projectId);
            topStockMovement.setReference("");
            topStockMovement.setMovementType(MovementType.ALLOCATED);
            topStockMovement.setRecordedBy(itemDTO.getPerson());
            topStockMovementRepository.save(topStockMovement);

        }
    }

    public List<InventoryListDTO> searchCustomInventories(Pageable pageable, MaterialCriteria criteria) {
        long size = 0l;
        Predicate predicate = materialPredicateBuilder.build(criteria, QISMaterialItem.iSMaterialItem);
        Iterable<ISMaterialItem> isMaterialItemIterable = materialItemRepository.findAll(predicate);
        List<ISMaterialItem> materialItems = materialItemRepository.findAll(predicate, pageable).getContent();
        List<InventoryListDTO> inventoryListDTOs = new ArrayList<InventoryListDTO>();
        List<ISProject> projects = projectRepository.findAll();
        if (criteria.getSearchQuery().equals("null")) {
            List<Integer> itemIds = topInventoryRepository.findDisItems();
            materialItems = materialItemRepository.findByIdIn(itemIds, pageable).getContent();
            size = itemIds.size();
        } else {
            size = isMaterialItemIterable.spliterator().getExactSizeIfKnown();
        }
        int start = 0;
        Long end = 0l;
        Boolean first = true;
        Boolean last = false;
        Long totalPages = 0l;
        if (pageable.getPageNumber() == 0) {
            start = 0;
            first = true;
        } else {
            start = (pageable.getPageNumber() * pageable.getPageSize()) + 1;
            first = false;
        }
        end = Long.valueOf(pageable.getPageSize());
        if (end == 0) {
            end = Long.valueOf(pageable.getPageSize());
        }
        totalPages = size / pageable.getPageSize();
        if (size % pageable.getPageSize() > 0) {
            totalPages = totalPages + 1;
        }
        if ((pageable.getPageNumber() + 1) * pageable.getPageSize() >= size) {
            last = true;
        }
        for (ISMaterialItem materialItem : materialItems) {
            InventoryListDTO inventoryListDTO = new InventoryListDTO();
            inventoryListDTO.setFirst(first);
            inventoryListDTO.setLast(last);
            inventoryListDTO.setTotalPages(totalPages);
            inventoryListDTO.setTotalElements(size);
            inventoryListDTO.setNumberOfElements(materialItems.size());
            inventoryListDTO.setProjectInvQtyTotal(0.0);
            inventoryListDTO.setTotalQuantity(0.0);
            List<StoreInventoryDTO> storeInventoryDTOs = new ArrayList<>();
            List<ProjectInventoryDTO> projectInventoryDTOs = new ArrayList<>();
            if (materialItem != null) {
                inventoryListDTO.setItemType(materialItem.getItemType().getName());
                inventoryListDTO.setResourceType("MATERIALTYPE");
                inventoryListDTO.setDescription(materialItem.getDescription());
                inventoryListDTO.setItemName(materialItem.getItemName());
                inventoryListDTO.setItemNumber(materialItem.getItemNumber());
                inventoryListDTO.setUnits(materialItem.getUnits());
            }
            // total inventory
            List<ISTopInventory> topInventories = topInventoryRepository.findByItem(materialItem.getId());
            for (ISTopInventory topInventory : topInventories) {
                inventoryListDTO.setTotalQuantity(inventoryListDTO.getTotalQuantity() + topInventory.getStoreOnHand());
            }
            for (ISProject project : projects) {
                List<ISTopInventory> topInventoryList = topInventoryRepository.findByItemAndProject(materialItem.getId(), project.getId());
                ProjectInventoryDTO projectInventoryDTO = new ProjectInventoryDTO();
                projectInventoryDTO.setQuantity(0.0);
                projectInventoryDTO.setProjectId(project.getId());
                projectInventoryDTO.setProjectName(project.getName());
                projectInventoryDTO.setQuantity(0.0);
                for (ISTopInventory topInventory : topInventoryList) {
                    projectInventoryDTO.setQuantity(topInventory.getStoreOnHand() + projectInventoryDTO.getQuantity());
                    inventoryListDTO.setProjectInvQtyTotal(topInventory.getStoreOnHand() + inventoryListDTO.getProjectInvQtyTotal());
                }
                projectInventoryDTOs.add(projectInventoryDTO);
            }
            inventoryListDTO.setProjectInventoryDetailsList(projectInventoryDTOs);
            //store inventory
            List<ISTopStore> topStores = topInventoryRepository.findDisStores();
            for (ISTopStore store : topStores) {
                ISTopStore topStore = topStoreRepository.findOne(store.getId());
                StoreInventoryDTO storeInventoryDTO = new StoreInventoryDTO();
                storeInventoryDTO.setTotalQuantity(0.0);
                storeInventoryDTO.setStoreId(store.getId());
                storeInventoryDTO.setStoreName(topStore.getStoreName());
                List<ProjectInventoryDTO> stoteProjectInventoryDTOs = new ArrayList<>();
                List<ISTopInventory> topInventories1 = topInventoryRepository.findByItemAndStore(materialItem.getId(), topStoreRepository.findOne(store.getId()));
                for (ISTopInventory topInventory : topInventories1) {
                    storeInventoryDTO.setTotalQuantity(storeInventoryDTO.getTotalQuantity() + topInventory.getStoreOnHand());
                }
                for (ISProject project : projects) {
                    ISTopInventory topInventory = topInventoryRepository.findByStoreAndItemAndProject(topStore, materialItem.getId(), project.getId());
                    ProjectInventoryDTO projectInventoryDTO = new ProjectInventoryDTO();
                    projectInventoryDTO.setProjectId(project.getId());
                    projectInventoryDTO.setProjectName(project.getName());
                    projectInventoryDTO.setQuantity(0.0);
                    if (topInventory != null) {
                        projectInventoryDTO.setQuantity(topInventory.getStoreOnHand());
                    }
                    stoteProjectInventoryDTOs.add(projectInventoryDTO);
                }
                storeInventoryDTO.setProjectStoreInvDetailsList(stoteProjectInventoryDTOs);
                storeInventoryDTOs.add(storeInventoryDTO);
            }
            inventoryListDTO.setStoreDetailsList(storeInventoryDTOs);
            inventoryListDTOs.add(inventoryListDTO);
        }
        return inventoryListDTOs;
    }
}

