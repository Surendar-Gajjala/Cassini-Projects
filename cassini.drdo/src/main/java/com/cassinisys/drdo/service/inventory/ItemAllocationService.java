
package com.cassinisys.drdo.service.inventory;

import com.cassinisys.drdo.filtering.BomSearchCriteria;
import com.cassinisys.drdo.filtering.BomSearchPredicateBuilder;
import com.cassinisys.drdo.model.bom.*;
import com.cassinisys.drdo.model.dto.BomAllocationDto;
import com.cassinisys.drdo.model.dto.MissileAllocationDto;
import com.cassinisys.drdo.model.inventory.Inventory;
import com.cassinisys.drdo.model.inventory.ItemAllocation;
import com.cassinisys.drdo.repo.bom.*;
import com.cassinisys.drdo.repo.inventory.InventoryRepository;
import com.cassinisys.drdo.repo.inventory.ItemAllocationRepository;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ExecutorService;

/**
 * Created by Nageshreddy on 03-12-2018.
 */
@Service
public class ItemAllocationService implements CrudService<ItemAllocation, Integer> {

    Integer tempBomId = 0;
    private Boolean bomItemsloaded = false;
    @Autowired
    private ItemAllocationRepository allocationRepository;
    @Autowired
    private BomInstanceItemRepository bomInstanceItemRepository;
    @Autowired
    private BomRepository bomRepository;
    @Autowired
    private BomItemRepository bomItemRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private ItemInstanceRepository itemInstanceRepository;
    @Autowired
    private BomInstanceRepository bomInstanceRepository;
    @Autowired
    private BomGroupRepository bomGroupRepository;
    @Autowired
    private ItemTypeRepository itemTypeRepository;
    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;
    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;
    @Autowired
    private ExecutorService executor;
    @Autowired
    private BomSearchPredicateBuilder bomSearchPredicateBuilder;

    private List<BomAllocationDto> bomInventoryDtoList = new ArrayList<>();
    Map<Integer, BomInstance> bomInstanceMap = new HashMap();
    Map<Integer, ItemType> integerItemTypeMap = new HashMap();

    @Override
    public List<ItemAllocation> getAll() {
        return allocationRepository.findAll();
    }

    @Override
    public ItemAllocation create(ItemAllocation itemAllocation) {
        return allocationRepository.save(itemAllocation);
    }

    @Override
    public ItemAllocation update(ItemAllocation itemAllocation) {
        return allocationRepository.save(itemAllocation);
    }

    @Override
    public void delete(Integer integer) {
        allocationRepository.delete(integer);
    }

    @Override
    public ItemAllocation get(Integer integer) {
        return allocationRepository.findOne(integer);
    }

    public List<ItemAllocation> getAllocationByInstance(Integer instance) {
        return allocationRepository.findByBomInstance(instance);
    }

    @Transactional(readOnly = true)
    public List<BomAllocationDto> getBomAllocation(Integer bomId) {
        tempBomId = bomId;
        List<BomAllocationDto> tempBomAllocation = new ArrayList<>();
        Bom bom = bomRepository.findOne(bomId);

        if (bom != null) {
            List<BomItem> bomItems = bomItemRepository.findByBomOrderByCreatedDateAsc(bom.getId());
            List<BomGroup> commonPartSections = bomGroupRepository.findByNameAndType("Common Parts", BomItemType.COMMONPART);
            if (commonPartSections.size() > 0) {
                for (BomGroup bomGroup : commonPartSections) {
                    BomItem commonPart = bomItemRepository.findByBomAndTypeRef(bom.getId(), bomGroup);
                    if (commonPart != null) {
                        commonPart.getChildren().addAll(bomItemRepository.findByParentOrderByCreatedDateAsc(commonPart.getId()));
                        Integer index = bomItems.indexOf(commonPart);

                        if (index != -1) {
                            bomItems.remove(commonPart);
                            bomItems.add(commonPart);
                        }
                    }
                }
            }
            for (BomItem bomItem : bomItems) {

                bomItem.getChildren().addAll(bomItemRepository.findByParentOrderByCreatedDateAsc(bomItem.getId()));
                BomAllocationDto bomAllocationDto = new BomAllocationDto();
                bomAllocationDto.setItem(bomItem);
                Inventory inventory = inventoryRepository.findByBomAndItem(bom.getId(), bomItem.getItem());

                if (bomItem.getBomItemType().equals(BomItemType.PART)) {
                    BomItem bomItemSection = null;
                    BomItem parent = bomItemRepository.findOne(bomItem.getParent());
                    if (parent.getBomItemType().equals(BomItemType.UNIT)) {
                        BomItem parent1 = bomItemRepository.findOne(parent.getParent());
                        if (parent1.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                            BomItem parent2 = bomItemRepository.findOne(parent1.getParent());
                            if (parent2.getBomItemType().equals(BomItemType.SECTION) || parent2.getBomItemType().equals(BomItemType.COMMONPART)) {
                                bomItemSection = parent2;
                            }
                        } else {
                            bomItemSection = parent1;
                        }
                    } else if (parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                        BomItem parent1 = bomItemRepository.findOne(parent.getParent());
                        bomItemSection = parent1;
                    } else if (parent.getBomItemType().equals(BomItemType.COMMONPART)) {
                        bomItemSection = parent;
                    }
                    Inventory withSectionInventory = null;
                    Inventory withOutSectionInventory = null;

                    if (bomItemSection != null) {
                        withSectionInventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSection(bom.getId(), bomItem.getItem().getId(), bomItem.getUniqueCode(), bomItemSection.getTypeRef().getId());
                        if (withSectionInventory != null) {
                            if (bomItem.getItem().getItemMaster().getItemType().getHasLots()) {
                                bomAllocationDto.setFractionalStock(bomAllocationDto.getFractionalStock() + withSectionInventory.getFractionalQtyOnHand());
                            } else {
                                bomAllocationDto.setStock(bomAllocationDto.getStock() + withSectionInventory.getQuantity());
                            }
                        }

//                        bomAllocationDto.setItemInstances(itemInstanceRepository.findByItem(bomItem.getItem().getId()));
                    }

                    withOutSectionInventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSectionIsNull(bom.getId(), bomItem.getItem().getId(), bomItem.getUniqueCode());
                    if (withOutSectionInventory != null) {
                        if (bomItem.getItem().getItemMaster().getItemType().getHasLots()) {
                            bomAllocationDto.setFractionalStock(bomAllocationDto.getFractionalStock() + withOutSectionInventory.getFractionalQtyOnHand());
                        } else {
                            bomAllocationDto.setStock(bomAllocationDto.getStock() + withOutSectionInventory.getQuantity());
                        }
                    }
                }
                tempBomAllocation.add(bomAllocationDto);
            }
        }
        return tempBomAllocation;
    }

    @Transactional(readOnly = true)
    public List<MissileAllocationDto> getBomInstanceAllocation(Integer bomId) {
        List<MissileAllocationDto> allocationDtoList = new ArrayList<>();
        BomInstance bomInstance = bomInstanceRepository.findOne(bomId);
        if (bomInstance != null) {
            List<BomInstanceItem> bomInstanceItems = bomInstanceItemRepository.findByBomOrderByCreatedDateAsc(bomInstance.getId());
            for (BomInstanceItem bomInstanceItem : bomInstanceItems) {

                bomInstanceItem.getChildren().addAll(bomInstanceItemRepository.findByParentOrderByCreatedDateAsc(bomInstanceItem.getId()));
                MissileAllocationDto allocationDto = new MissileAllocationDto();
                allocationDto.setItem(bomInstanceItem);

                allocationDtoList.add(allocationDto);
            }
        }

        return allocationDtoList;
    }


    @Transactional(readOnly = false)
    public void loadBomChildrenWithoutSecAllocation(Integer bomId, Integer[] ids, Integer[] selectedIds) {
        tempBomId = bomId;
        getTotalBomChildren(bomId, ids, selectedIds);
        this.bomItemsloaded = true;
    }

    public void getTotalBomChildren(Integer bomId, Integer[] ids, Integer[] selectedIds) {
        List<BomAllocationDto> bomInventoryDtoList1 = new ArrayList<>();
//        BomItem selectedItem = bomItemRepository.findOne(bomItemId);
//        if (selectedItem.getBomItemType().equals(BomItemType.SECTION) || selectedItem.getBomItemType().equals(BomItemType.COMMONPART)) {
        bomInventoryDtoList = new ArrayList<>();
//        }
        List<BomItem> bomItems = new ArrayList<>();

        List<BomItem> bomItems1 = bomItemRepository.findByBomOrderByCreatedDateAsc(bomId);

        for (BomItem bomItem1 : bomItems1) {
            BomItem selectedItem = bomItemRepository.findOne(bomItem1.getId());
            bomItems = bomItemRepository.findByParentOrderByCreatedDateAsc(bomItem1.getId());

            bomItems.forEach(bomItem -> {
                bomItem.getChildren().addAll(bomItemRepository.findByParentOrderByCreatedDateAsc(bomItem.getId()));
                BomAllocationDto bomAllocationDto = new BomAllocationDto();

                if (bomItem.getBomItemType().equals(BomItemType.PART)) {
                    ItemType itemType = integerItemTypeMap.get(bomItem.getItem().getItemMaster().getItemType().getParentType());

                    if (!itemType.getParentNode()) {
                        bomItem.getItem().getItemMaster().setParentType(itemType);
                    } else {
                        bomItem.getItem().getItemMaster().setParentType(bomItem.getItem().getItemMaster().getItemType());
                    }
                }

                if (selectedItem.getBomItemType().equals(BomItemType.SECTION) || selectedItem.getBomItemType().equals(BomItemType.COMMONPART)) {
                    bomAllocationDto.setLevel(1);
                    bomAllocationDto.setExpanded(true);
                }
                bomAllocationDto.setItem(bomItem);
//            Inventory inventory = inventoryRepository.findByBomAndItem(bomId, bomItem.getItem());
                if (/*inventory != null && */bomItem.getBomItemType().equals(BomItemType.PART)) {
                    BomItem bomItemSection = null;
                    BomItem parent = bomItemRepository.findOne(bomItem.getParent());
                    if (parent.getBomItemType().equals(BomItemType.UNIT)) {
                        BomItem parent1 = bomItemRepository.findOne(parent.getParent());
                        if (parent1.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                            BomItem parent2 = bomItemRepository.findOne(parent1.getParent());
                            if (parent2.getBomItemType().equals(BomItemType.SECTION) || parent2.getBomItemType().equals(BomItemType.COMMONPART)) {
                                bomItemSection = parent2;
                            }
                        } else {
                            bomItemSection = parent1;
                        }
                    } else if (parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                        BomItem parent1 = bomItemRepository.findOne(parent.getParent());
                        bomItemSection = parent1;
                    } else if (parent.getBomItemType().equals(BomItemType.COMMONPART)) {
                        bomItemSection = parent;
                    }
                    Inventory withSectionInventory = null;
                    Inventory withOutSectionInventory = null;

                    if (bomItemSection != null) {
                        withSectionInventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSection(bomId, bomItem.getItem().getId(), bomItem.getUniqueCode(), bomItemSection.getTypeRef().getId());
                        bomAllocationDto.getItemInstances().addAll(itemInstanceRepository.getItemInstancesBySectionAndBomAndUniqueCode(bomItem.getItem().getId(), bomItemSection.getTypeRef().getId(), bomId, bomItem.getUniqueCode()));
                        if (withSectionInventory != null) {
                            if (bomItem.getItem().getItemMaster().getItemType().getHasLots()) {
                                bomAllocationDto.setFractionalStock(bomAllocationDto.getFractionalStock() + withSectionInventory.getFractionalQtyOnHand());
                                bomAllocationDto.setAllocated(bomAllocationDto.getAllocated() + withSectionInventory.getFractionalQtyAllocated());
                                bomAllocationDto.setIssuedQty(bomAllocationDto.getIssuedQty() + withSectionInventory.getFractionalQtyIssued());
                            } else {
                                bomAllocationDto.setStock(bomAllocationDto.getStock() + withSectionInventory.getQuantity());
                                bomAllocationDto.setAllocated(bomAllocationDto.getAllocated() + withSectionInventory.getQtyAllocated());
                                bomAllocationDto.setIssuedQty(bomAllocationDto.getIssuedQty() + withSectionInventory.getQtyIssued());
                            }
                        }
                    }
                    bomAllocationDto.getItemInstances().addAll(itemInstanceRepository.getItemInstancesByBomAndUniqueCodeAndSectionIsNull(bomItem.getItem().getId(), bomId, bomItem.getUniqueCode()));

                    bomAllocationDto.getItemInstances().forEach(itemInstance -> {
                        ObjectTypeAttribute certificateNumberType = objectTypeAttributeRepository.findByNameAndObjectType("Certificate Number", ObjectType.valueOf("ITEMINSTANCE"));

                        if (certificateNumberType != null) {
                            ObjectAttribute certificateNumber = objectAttributeRepository.findByObjectIdAndAttributeDefId(itemInstance.getId(), certificateNumberType.getId());

                            if (certificateNumber != null && certificateNumber.getStringValue() != null) {
                                itemInstance.setCertificateNumber(certificateNumber.getStringValue());
                            }
                        }
                    });

                    withOutSectionInventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSectionIsNull(bomId, bomItem.getItem().getId(), bomItem.getUniqueCode());
                    if (withOutSectionInventory != null) {
                        if (bomItem.getItem().getItemMaster().getItemType().getHasLots()) {
                            bomAllocationDto.setFractionalStock(bomAllocationDto.getFractionalStock() + withOutSectionInventory.getFractionalQtyOnHand());
                            bomAllocationDto.setCommonAllocated(bomAllocationDto.getCommonAllocated() + withOutSectionInventory.getFractionalQtyAllocated());
                            bomAllocationDto.setCommonIssuedQty(bomAllocationDto.getCommonIssuedQty()+withOutSectionInventory.getFractionalQtyIssued());
                        } else {
                            bomAllocationDto.setStock(bomAllocationDto.getStock() + withOutSectionInventory.getQuantity());
                            bomAllocationDto.setCommonAllocated(bomAllocationDto.getCommonAllocated() + withOutSectionInventory.getQtyAllocated());
                            bomAllocationDto.setCommonIssuedQty(bomAllocationDto.getCommonIssuedQty()+withOutSectionInventory.getQtyIssued());
                        }
                    }


//                bomAllocationDto.setItemInstances(itemInstanceRepository.getInstancesByItem(bomItem.getItem().getId(), ItemInstanceStatus.REJECTED));
                    if (ids.length > 0) {
                        Map<Integer, ItemAllocation> allocationMap = new HashMap();
                        for (Integer instance : ids) {
                            BomInstance bomInstance = bomInstanceMap.get(instance);
                            BomInstanceItem bomInstanceItem = bomInstanceItemRepository.findByBomItemAndStartWithIdPath(bomItem.getId(), bomInstance.getId().toString());
                            if (bomInstanceItem != null) {
                                ItemAllocation itemAllocation = allocationRepository.findByBomAndBomInstanceAndBomInstanceItem(bomId, bomInstance.getId(), bomInstanceItem.getId());
                                if (itemAllocation == null) {
                                    itemAllocation = new ItemAllocation();
                                    itemAllocation.setId(null);
                                    itemAllocation.setAllocateQty(0.0);
                                    itemAllocation.setFailedQty(0.0);
                                    itemAllocation.setIssuedQty(0.0);
                                    itemAllocation.setBomInstanceItem(bomInstanceItem.getId());
                                    itemAllocation.setBomInstance(bomInstance.getId());
                                    itemAllocation.setItemInstance(instance);
                                }
                                itemAllocation.setItemInstance(instance);
                                allocationMap.put(instance, itemAllocation);
                            }
                        }
                        bomAllocationDto.setListMap(allocationMap);
                    }
                }

                bomInventoryDtoList1.add(new BomAllocationDto(bomAllocationDto));

                if (selectedItem.getBomItemType().equals(BomItemType.SECTION) || selectedItem.getBomItemType().equals(BomItemType.COMMONPART)) {
                    bomInventoryDtoList.add(bomAllocationDto);
                    visitChildren(bomId, bomAllocationDto, bomInventoryDtoList, ids, selectedIds);
                }

            });
        }

        calculateShortage(bomInventoryDtoList1, ids, selectedIds);
    }

    @Transactional(readOnly = false)
    public void loadBomChildrenAllocation(Integer bomId, Integer bomItemId, Integer[] ids, Integer[] selectedIds) {
        List<BomInstance> bomInstances = bomInstanceRepository.findAll();
        List<ItemType> itemTypeList = itemTypeRepository.findAll();
        bomInstances.forEach(bomInstance -> {
            bomInstanceMap.put(bomInstance.getItem().getId(), bomInstance);
        });
        itemTypeList.forEach(itemType -> {
            integerItemTypeMap.put(itemType.getId(), itemType);
        });
        getBomChildren(bomId, bomItemId, ids, selectedIds, null, null, true);
        this.bomItemsloaded = true;
    }

    @Transactional(readOnly = true)
    public List<BomAllocationDto> getBomChildrenAllocation(Integer bomId, Integer bomItemId, Integer[] ids, Integer[] selectedIds, String workCenter, String searchBomText) {
        BomItem selectedItem = bomItemRepository.findOne(bomItemId);
        if (searchBomText.equals("null") && workCenter.equals("null") && selectedItem.getBomItemType().equals(BomItemType.SECTION))
            this.bomItemsloaded = false;
        return getBomChildren(bomId, bomItemId, ids, selectedIds, workCenter, searchBomText, false);

    }

    public List<BomAllocationDto> getBomChildren(Integer bomId, Integer bomItemId, Integer[] ids, Integer[] selectedIds, String workCenter, String searchBomText, Boolean visitChildren) {
        List<BomAllocationDto> bomInventoryDtoList1 = new ArrayList<>();
        BomItem selectedItem = bomItemRepository.findOne(bomItemId);
        if (selectedItem.getBomItemType().equals(BomItemType.SECTION) || selectedItem.getBomItemType().equals(BomItemType.COMMONPART)) {
            bomInventoryDtoList = new ArrayList<>();
        }
        List<BomItem> bomItems = new ArrayList<>();

        if ((workCenter != null && !workCenter.equals("null") && !workCenter.equals("")) || (searchBomText != null && !searchBomText.equals("null") && !searchBomText.equals(""))) {
            BomSearchCriteria bomSearchCriteria = new BomSearchCriteria();
            bomSearchCriteria.setPlanningFilter(true);
            bomSearchCriteria.setSection(bomItemId);
            bomSearchCriteria.setSearchQuery(workCenter);
            bomSearchCriteria.setSearchText(searchBomText);
            Predicate predicate = bomSearchPredicateBuilder.build(bomSearchCriteria, QBomItem.bomItem);

            Pageable pageable1 = new PageRequest(0, 10000,
                    new Sort(new Sort.Order(Sort.Direction.ASC, "createdDate")));

            Page<BomItem> bomItemPage = bomItemRepository.findAll(predicate, pageable1);

            if (bomItemPage.getContent().size() > 0) {
                Map<Integer, BomItem> unitMap = new HashMap<>();
                Map<Integer, BomItem> subsystemMap = new HashMap<>();
                Map<Integer, BomItem> sectionMap = new HashMap<>();
                List<BomItem> bomItemList = new ArrayList<>();
                for (BomItem bomItem : bomItemPage.getContent()) {
                    BomItem unit = null;
                    BomItem subsystem = null;
                    BomItem section = null;

                    BomItem parentBomItem = bomItemRepository.findOne(bomItem.getParent());
                    if (parentBomItem.getBomItemType().equals(BomItemType.UNIT)) {
                        unit = parentBomItem;
                        parentBomItem = bomItemRepository.findOne(unit.getParent());
                        if (parentBomItem.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                            subsystem = parentBomItem;
                            section = bomItemRepository.findOne(subsystem.getParent());
                        } else if (parentBomItem.getBomItemType().equals(BomItemType.SECTION) || parentBomItem.getBomItemType().equals(BomItemType.COMMONPART)) {
                            section = parentBomItem;
                        }
                    } else if (parentBomItem.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                        subsystem = parentBomItem;
                        section = bomItemRepository.findOne(subsystem.getParent());
                    } else if (parentBomItem.getBomItemType().equals(BomItemType.SECTION) || parentBomItem.getBomItemType().equals(BomItemType.COMMONPART)) {
                        section = parentBomItem;
                    }


                    if (section != null && sectionMap.get(section.getId()) == null) {
                        section.setLevel(0);
                        section.setExpanded(true);
                        bomItemList.add(section);
                        sectionMap.put(section.getId(), section);
                        if (subsystem != null && subsystemMap.get(subsystem.getId()) == null) {
                            subsystem.setLevel(section.getLevel() + 1);
                            subsystem.setExpanded(true);
                            bomItemList.add(bomItemList.indexOf(section) + 1, subsystem);

                            BomItem sectionItem = bomItemList.get(bomItemList.indexOf(section));
                            sectionItem.getChildren().add(subsystem);
                            sectionItem.getBomChildren().add(subsystem);

                            subsystemMap.put(subsystem.getId(), subsystem);

                            if (unit != null && unitMap.get(unit.getId()) == null) {
                                unit.setLevel(subsystem.getLevel() + 1);
                                unit.setExpanded(true);
                                bomItemList.add(bomItemList.indexOf(subsystem) + 1, unit);

                                BomItem subsystemItem = bomItemList.get(bomItemList.indexOf(subsystem));
                                subsystemItem.getChildren().add(unit);
                                subsystemItem.getBomChildren().add(unit);

                                unitMap.put(unit.getId(), unit);

                                bomItem.setLevel(unit.getLevel() + 1);
                                bomItemList.add(bomItemList.indexOf(unit) + 1, bomItem);

                                BomItem unitItem = bomItemList.get(bomItemList.indexOf(unit));
                                unitItem.getChildren().add(bomItem);
                                unitItem.getBomChildren().add(bomItem);

                            } else if (unit != null) {
                                BomItem existUnit = unitMap.get(unit.getId());
                                bomItem.setLevel(existUnit.getLevel());
                                bomItemList.add(bomItemList.indexOf(existUnit) + 1, bomItem);
                                BomItem unitItem = bomItemList.get(bomItemList.indexOf(existUnit));
                                unitItem.getChildren().add(bomItem);
                                unitItem.getBomChildren().add(bomItem);
                            } else {
                                bomItem.setLevel(subsystem.getLevel() + 1);
                                bomItemList.add(bomItemList.indexOf(subsystem) + 1, bomItem);

                                BomItem subsystemItem = bomItemList.get(bomItemList.indexOf(subsystem));
                                subsystemItem.getChildren().add(bomItem);
                                subsystemItem.getBomChildren().add(bomItem);
                            }
                        } else if (subsystem != null) {
                            BomItem existSubsystem = subsystemMap.get(subsystem.getId());

                            if (unit != null && unitMap.get(unit.getId()) == null) {
                                unit.setLevel(existSubsystem.getLevel() + 1);
                                unit.setExpanded(true);
                                bomItemList.add(bomItemList.indexOf(existSubsystem) + 1, unit);
                                unitMap.put(unit.getId(), unit);

                                BomItem subsystemItem = bomItemList.get(bomItemList.indexOf(existSubsystem));
                                subsystemItem.getChildren().add(unit);
                                subsystemItem.getBomChildren().add(unit);

                                bomItem.setLevel(unit.getLevel() + 1);
                                bomItemList.add(bomItemList.indexOf(unit) + 1, bomItem);

                                BomItem unitItem = bomItemList.get(bomItemList.indexOf(unit));
                                unitItem.getChildren().add(bomItem);
                                unitItem.getBomChildren().add(bomItem);
                            } else if (unit != null) {
                                BomItem existUnit = unitMap.get(unit.getId());
                                bomItem.setLevel(existUnit.getLevel() + 1);
                                bomItemList.add(bomItemList.indexOf(existUnit) + 1, bomItem);

                                BomItem unitItem = bomItemList.get(bomItemList.indexOf(existUnit));
                                unitItem.getChildren().add(bomItem);
                                unitItem.getBomChildren().add(bomItem);
                            } else {
                                bomItem.setLevel(existSubsystem.getLevel());
                                bomItemList.add(bomItemList.indexOf(existSubsystem) + 1, bomItem);

                                BomItem subsystemItem = bomItemList.get(bomItemList.indexOf(existSubsystem));
                                subsystemItem.getChildren().add(bomItem);
                                subsystemItem.getBomChildren().add(bomItem);
                            }
                        } else {
                            bomItem.setLevel(section.getLevel() + 1);
                            bomItemList.add(bomItemList.indexOf(section) + 1, bomItem);

                            BomItem sectionItem = bomItemList.get(bomItemList.indexOf(section));
                            sectionItem.getChildren().add(bomItem);
                            sectionItem.getBomChildren().add(bomItem);
                        }
                    } else if (section != null) {
                        BomItem existSection = sectionMap.get(section.getId());

                        if (subsystem != null && subsystemMap.get(subsystem.getId()) == null) {
                            subsystem.setLevel(existSection.getLevel() + 1);
                            subsystem.setExpanded(true);
                            bomItemList.add(bomItemList.indexOf(existSection) + 1, subsystem);

                            BomItem sectionItem = bomItemList.get(bomItemList.indexOf(existSection));
                            sectionItem.getChildren().add(subsystem);
                            sectionItem.getBomChildren().add(subsystem);

                            subsystemMap.put(subsystem.getId(), subsystem);

                            if (unit != null && unitMap.get(unit.getId()) == null) {
                                unit.setLevel(subsystem.getLevel() + 1);
                                unit.setExpanded(true);
                                bomItemList.add(bomItemList.indexOf(subsystem) + 1, unit);

                                BomItem subsystemItem = bomItemList.get(bomItemList.indexOf(subsystem));
                                subsystemItem.getChildren().add(unit);
                                subsystemItem.getBomChildren().add(unit);

                                unitMap.put(unit.getId(), unit);

                                bomItem.setLevel(unit.getLevel() + 1);
                                bomItemList.add(bomItemList.indexOf(unit) + 1, bomItem);

                                BomItem unitItem = bomItemList.get(bomItemList.indexOf(unit));
                                unitItem.getChildren().add(bomItem);
                                unitItem.getBomChildren().add(bomItem);
                            }
                        } else if (subsystem != null) {
                            BomItem existSubsystem = subsystemMap.get(subsystem.getId());

                            if (unit != null && unitMap.get(unit.getId()) == null) {
                                unit.setLevel(existSubsystem.getLevel() + 1);
                                unit.setExpanded(true);
                                bomItemList.add(bomItemList.indexOf(existSubsystem) + 1, unit);

                                BomItem subsystemItem = bomItemList.get(bomItemList.indexOf(existSubsystem));
                                subsystemItem.getChildren().add(unit);
                                subsystemItem.getBomChildren().add(unit);

                                unitMap.put(unit.getId(), unit);

                                bomItem.setLevel(unit.getLevel() + 1);
                                bomItemList.add(bomItemList.indexOf(unit) + 1, bomItem);

                                BomItem unitItem = bomItemList.get(bomItemList.indexOf(unit));
                                unitItem.getChildren().add(bomItem);
                                unitItem.getBomChildren().add(bomItem);
                            } else if (unit != null) {
                                BomItem existUnit = unitMap.get(unit.getId());
                                bomItem.setLevel(existUnit.getLevel() + 1);
                                bomItemList.add(bomItemList.indexOf(existUnit) + 1, bomItem);

                                BomItem unitItem = bomItemList.get(bomItemList.indexOf(existUnit));
                                unitItem.getChildren().add(bomItem);
                                unitItem.getBomChildren().add(bomItem);
                            } else {
                                bomItem.setLevel(existSubsystem.getLevel() + 1);
                                bomItemList.add(bomItemList.indexOf(existSubsystem) + 1, bomItem);

                                BomItem subsystemItem = bomItemList.get(bomItemList.indexOf(existSubsystem));
                                subsystemItem.getChildren().add(bomItem);
                                subsystemItem.getBomChildren().add(bomItem);
                            }
                        } else {
                            bomItem.setLevel(existSection.getLevel() + 1);
                            bomItemList.add(bomItemList.indexOf(existSection) + 1, bomItem);

                            BomItem sectionItem = bomItemList.get(bomItemList.indexOf(existSection));
                            sectionItem.getChildren().add(bomItem);
                            sectionItem.getBomChildren().add(bomItem);
                        }
                    }
                }


                bomItemList.forEach(bomItem -> {
                    bomItem.getChildren().addAll(bomItemRepository.findByParentOrderByCreatedDateAsc(bomItem.getId()));
                    BomAllocationDto bomAllocationDto = new BomAllocationDto();

                    bomAllocationDto.setLevel(bomItem.getLevel());
                    bomAllocationDto.setExpanded(true);
                    bomAllocationDto.setItem(bomItem);

                    if (bomItem.getBomItemType().equals(BomItemType.PART)) {
                        BomItem bomItemSection = null;
                        BomItem parent = bomItemRepository.findOne(bomItem.getParent());
                        if (parent.getBomItemType().equals(BomItemType.UNIT)) {
                            BomItem parent1 = bomItemRepository.findOne(parent.getParent());
                            if (parent1.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                                BomItem parent2 = bomItemRepository.findOne(parent1.getParent());
                                if (parent2.getBomItemType().equals(BomItemType.SECTION) || parent2.getBomItemType().equals(BomItemType.COMMONPART)) {
                                    bomItemSection = parent2;
                                }
                            } else {
                                bomItemSection = parent1;
                            }
                        } else if (parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                            BomItem parent1 = bomItemRepository.findOne(parent.getParent());
                            bomItemSection = parent1;
                        } else if (parent.getBomItemType().equals(BomItemType.COMMONPART)) {
                            bomItemSection = parent;
                        }
                        Inventory withSectionInventory = null;
                        Inventory withOutSectionInventory = null;

                        if (bomItemSection != null) {
                            withSectionInventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSection(bomId, bomItem.getItem().getId(), bomItem.getUniqueCode(), bomItemSection.getTypeRef().getId());
                            bomAllocationDto.getItemInstances().addAll(itemInstanceRepository.getItemInstancesBySectionAndBomAndUniqueCode(bomItem.getItem().getId(), bomItemSection.getTypeRef().getId(), bomId, bomItem.getUniqueCode()));
                            if (withSectionInventory != null) {
                                if (bomItem.getItem().getItemMaster().getItemType().getHasLots()) {
                                    bomAllocationDto.setFractionalStock(bomAllocationDto.getFractionalStock() + withSectionInventory.getFractionalQtyOnHand());
                                    bomAllocationDto.setAllocated(bomAllocationDto.getAllocated() + withSectionInventory.getFractionalQtyAllocated());
                                    bomAllocationDto.setIssuedQty(bomAllocationDto.getIssuedQty() + withSectionInventory.getFractionalQtyIssued());
                                } else {
                                    bomAllocationDto.setStock(bomAllocationDto.getStock() + withSectionInventory.getQuantity());
                                    bomAllocationDto.setAllocated(bomAllocationDto.getAllocated() + withSectionInventory.getQtyAllocated());
                                    bomAllocationDto.setIssuedQty(bomAllocationDto.getIssuedQty() + withSectionInventory.getQtyIssued());
                                }
                            }
                        }
                        bomAllocationDto.getItemInstances().addAll(itemInstanceRepository.getItemInstancesByBomAndUniqueCodeAndSectionIsNull(bomItem.getItem().getId(), bomId, bomItem.getUniqueCode()));

                        bomAllocationDto.getItemInstances().forEach(itemInstance -> {
                            ObjectTypeAttribute certificateNumberType = objectTypeAttributeRepository.findByNameAndObjectType("Certificate Number", ObjectType.valueOf("ITEMINSTANCE"));

                            if (certificateNumberType != null) {
                                ObjectAttribute certificateNumber = objectAttributeRepository.findByObjectIdAndAttributeDefId(itemInstance.getId(), certificateNumberType.getId());

                                if (certificateNumber != null && certificateNumber.getStringValue() != null) {
                                    itemInstance.setCertificateNumber(certificateNumber.getStringValue());
                                }
                            }
                        });

                        withOutSectionInventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSectionIsNull(bomId, bomItem.getItem().getId(), bomItem.getUniqueCode());
                        if (withOutSectionInventory != null) {
                            if (bomItem.getItem().getItemMaster().getItemType().getHasLots()) {
                                bomAllocationDto.setFractionalStock(bomAllocationDto.getFractionalStock() + withOutSectionInventory.getFractionalQtyOnHand());
                                bomAllocationDto.setCommonAllocated(bomAllocationDto.getCommonAllocated() + withOutSectionInventory.getFractionalQtyAllocated());
                                bomAllocationDto.setCommonIssuedQty(bomAllocationDto.getCommonIssuedQty() + withOutSectionInventory.getFractionalQtyIssued());
                            } else {
                                bomAllocationDto.setStock(bomAllocationDto.getStock() + withOutSectionInventory.getQuantity());
                                bomAllocationDto.setCommonAllocated(bomAllocationDto.getCommonAllocated() + withOutSectionInventory.getQtyAllocated());
                                bomAllocationDto.setCommonIssuedQty(bomAllocationDto.getCommonIssuedQty() + withOutSectionInventory.getQtyIssued());
                            }
                        }

                        if (ids.length > 0) {
                            Map<Integer, ItemAllocation> allocationMap = new HashMap();
                            for (Integer instance : ids) {
                                BomInstance bomInstance = bomInstanceMap.get(instance);
                                BomInstanceItem bomInstanceItem = bomInstanceItemRepository.findByBomItemAndStartWithIdPath(bomItem.getId(), bomInstance.getId().toString());
                                if (bomInstanceItem != null) {
                                    ItemAllocation itemAllocation = allocationRepository.findByBomAndBomInstanceAndBomInstanceItem(bomId, bomInstance.getId(), bomInstanceItem.getId());
                                    if (itemAllocation == null) {
                                        itemAllocation = new ItemAllocation();
                                        itemAllocation.setId(null);
                                        itemAllocation.setAllocateQty(0.0);
                                        itemAllocation.setFailedQty(0.0);
                                        itemAllocation.setIssuedQty(0.0);
                                        itemAllocation.setBomInstanceItem(bomInstanceItem.getId());
                                        itemAllocation.setBomInstance(bomInstance.getId());
                                        itemAllocation.setItemInstance(instance);
                                    }
                                    itemAllocation.setItemInstance(instance);
                                    allocationMap.put(instance, itemAllocation);
                                }
                            }
                            bomAllocationDto.setListMap(allocationMap);
                        }
                    }

                    bomInventoryDtoList1.add(new BomAllocationDto(bomAllocationDto));
                });
            }

        } else {
            bomItems = bomItemRepository.findByParentOrderByCreatedDateAsc(bomItemId);

            bomItems.forEach(bomItem -> {
                bomItem.getChildren().addAll(bomItemRepository.findByParentOrderByCreatedDateAsc(bomItem.getId()));
                BomAllocationDto bomAllocationDto = new BomAllocationDto();

                if (bomItem.getBomItemType().equals(BomItemType.PART)) {
                    ItemType itemType = integerItemTypeMap.get(bomItem.getItem().getItemMaster().getItemType().getParentType());

                    if (!itemType.getParentNode()) {
                        bomItem.getItem().getItemMaster().setParentType(itemType);
                    } else {
                        bomItem.getItem().getItemMaster().setParentType(bomItem.getItem().getItemMaster().getItemType());
                    }
                }

                if (selectedItem.getBomItemType().equals(BomItemType.SECTION) || selectedItem.getBomItemType().equals(BomItemType.COMMONPART)) {
                    bomAllocationDto.setLevel(1);
                    bomAllocationDto.setExpanded(true);
                }
                bomAllocationDto.setItem(bomItem);
//            Inventory inventory = inventoryRepository.findByBomAndItem(bomId, bomItem.getItem());
                if (/*inventory != null && */bomItem.getBomItemType().equals(BomItemType.PART)) {
                    BomItem bomItemSection = null;
                    BomItem parent = bomItemRepository.findOne(bomItem.getParent());
                    if (parent.getBomItemType().equals(BomItemType.UNIT)) {
                        BomItem parent1 = bomItemRepository.findOne(parent.getParent());
                        if (parent1.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                            BomItem parent2 = bomItemRepository.findOne(parent1.getParent());
                            if (parent2.getBomItemType().equals(BomItemType.SECTION) || parent2.getBomItemType().equals(BomItemType.COMMONPART)) {
                                bomItemSection = parent2;
                            }
                        } else {
                            bomItemSection = parent1;
                        }
                    } else if (parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                        BomItem parent1 = bomItemRepository.findOne(parent.getParent());
                        bomItemSection = parent1;
                    } else if (parent.getBomItemType().equals(BomItemType.COMMONPART)) {
                        bomItemSection = parent;
                    }
                    Inventory withSectionInventory = null;
                    Inventory withOutSectionInventory = null;

                    if (bomItemSection != null) {
                        withSectionInventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSection(bomId, bomItem.getItem().getId(), bomItem.getUniqueCode(), bomItemSection.getTypeRef().getId());
                        bomAllocationDto.getItemInstances().addAll(itemInstanceRepository.getItemInstancesBySectionAndBomAndUniqueCode(bomItem.getItem().getId(), bomItemSection.getTypeRef().getId(), bomId, bomItem.getUniqueCode()));
                        if (withSectionInventory != null) {
                            if (bomItem.getItem().getItemMaster().getItemType().getHasLots()) {
                                bomAllocationDto.setFractionalStock(bomAllocationDto.getFractionalStock() + withSectionInventory.getFractionalQtyOnHand());
                                bomAllocationDto.setAllocated(bomAllocationDto.getAllocated() + withSectionInventory.getFractionalQtyAllocated());
                                bomAllocationDto.setIssuedQty(bomAllocationDto.getIssuedQty() + withSectionInventory.getFractionalQtyIssued());
                            } else {
                                bomAllocationDto.setStock(bomAllocationDto.getStock() + withSectionInventory.getQuantity());
                                bomAllocationDto.setAllocated(bomAllocationDto.getAllocated() + withSectionInventory.getQtyAllocated());
                                bomAllocationDto.setIssuedQty(bomAllocationDto.getIssuedQty() + withSectionInventory.getQtyIssued());
                            }
                        }
                    }
                    bomAllocationDto.getItemInstances().addAll(itemInstanceRepository.getItemInstancesByBomAndUniqueCodeAndSectionIsNull(bomItem.getItem().getId(), bomId, bomItem.getUniqueCode()));

                    bomAllocationDto.getItemInstances().forEach(itemInstance -> {
                        ObjectTypeAttribute certificateNumberType = objectTypeAttributeRepository.findByNameAndObjectType("Certificate Number", ObjectType.valueOf("ITEMINSTANCE"));

                        if (certificateNumberType != null) {
                            ObjectAttribute certificateNumber = objectAttributeRepository.findByObjectIdAndAttributeDefId(itemInstance.getId(), certificateNumberType.getId());

                            if (certificateNumber != null && certificateNumber.getStringValue() != null) {
                                itemInstance.setCertificateNumber(certificateNumber.getStringValue());
                            }
                        }
                    });

                    withOutSectionInventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSectionIsNull(bomId, bomItem.getItem().getId(), bomItem.getUniqueCode());
                    if (withOutSectionInventory != null) {
                        if (bomItem.getItem().getItemMaster().getItemType().getHasLots()) {
                            bomAllocationDto.setFractionalStock(bomAllocationDto.getFractionalStock() + withOutSectionInventory.getFractionalQtyOnHand());
                            bomAllocationDto.setCommonAllocated(bomAllocationDto.getCommonAllocated() + withOutSectionInventory.getFractionalQtyAllocated());
                            bomAllocationDto.setCommonIssuedQty(bomAllocationDto.getCommonIssuedQty() + withOutSectionInventory.getFractionalQtyIssued());
                        } else {
                            bomAllocationDto.setStock(bomAllocationDto.getStock() + withOutSectionInventory.getQuantity());
                            bomAllocationDto.setCommonAllocated(bomAllocationDto.getCommonAllocated() + withOutSectionInventory.getQtyAllocated());
                            bomAllocationDto.setCommonIssuedQty(bomAllocationDto.getCommonIssuedQty() + withOutSectionInventory.getQtyIssued());
                        }
                    }

//                bomAllocationDto.setItemInstances(itemInstanceRepository.getInstancesByItem(bomItem.getItem().getId(), ItemInstanceStatus.REJECTED));
                    if (ids.length > 0) {
                        Map<Integer, ItemAllocation> allocationMap = new HashMap();
                        for (Integer instance : ids) {
                            BomInstance bomInstance = bomInstanceMap.get(instance);
                            BomInstanceItem bomInstanceItem = bomInstanceItemRepository.findByBomItemAndStartWithIdPath(bomItem.getId(), bomInstance.getId().toString());
                            if (bomInstanceItem != null) {
                                ItemAllocation itemAllocation = allocationRepository.findByBomAndBomInstanceAndBomInstanceItem(bomId, bomInstance.getId(), bomInstanceItem.getId());
                                if (itemAllocation == null) {
                                    itemAllocation = new ItemAllocation();
                                    itemAllocation.setId(null);
                                    itemAllocation.setAllocateQty(0.0);
                                    itemAllocation.setFailedQty(0.0);
                                    itemAllocation.setIssuedQty(0.0);
                                    itemAllocation.setBomInstanceItem(bomInstanceItem.getId());
                                    itemAllocation.setBomInstance(bomInstance.getId());
                                    itemAllocation.setItemInstance(instance);
                                }
                                itemAllocation.setItemInstance(instance);
                                allocationMap.put(instance, itemAllocation);
                            }
                        }
                        bomAllocationDto.setListMap(allocationMap);
                    }
                }

                bomInventoryDtoList1.add(new BomAllocationDto(bomAllocationDto));

                if (visitChildren) {
                    if (selectedItem.getBomItemType().equals(BomItemType.SECTION) || selectedItem.getBomItemType().equals(BomItemType.COMMONPART)) {
                        bomInventoryDtoList.add(bomAllocationDto);
                        visitChildren(bomId, bomAllocationDto, bomInventoryDtoList, ids, selectedIds);
                    }
                }

            });
        }

        calculateShortage(bomInventoryDtoList1, ids, selectedIds);
        return bomInventoryDtoList1;
    }

    @Async
    public List<BomAllocationDto> visitChildren(Integer bomId, BomAllocationDto allocationDto, List<BomAllocationDto> bomAllocationDtos, Integer[] ids, Integer[] selectedIds) {
        List<BomItem> children = null;
        if (!allocationDto.getItem().getBomItemType().equals(BomItemType.PART)) {
            children = bomItemRepository.findByParentOrderByCreatedDateAsc(allocationDto.getItem().getId());
        }

        if (children != null) {
            for (BomItem child : children) {
                BomAllocationDto bomAllocationDto = new BomAllocationDto();

                if (!child.getBomItemType().equals(BomItemType.PART)) {
                    child.getChildren().addAll(bomItemRepository.findByParentOrderByCreatedDateAsc(child.getId()));
                }
                if (child.getBomItemType().equals(BomItemType.PART)) {
                    ItemType itemType = integerItemTypeMap.get(child.getItem().getItemMaster().getItemType().getParentType());

                    if (!itemType.getParentNode()) {
                        child.getItem().getItemMaster().setParentType(itemType);
                    } else {
                        child.getItem().getItemMaster().setParentType(child.getItem().getItemMaster().getItemType());
                    }
                }
                bomAllocationDto.setLevel(allocationDto.getLevel() + 1);
                bomAllocationDto.setExpanded(true);
                bomAllocationDto.setItem(child);
                if (child.getBomItemType().equals(BomItemType.PART)) {
                    BomItem bomItemSection = null;
                    BomItem parent = bomItemRepository.findOne(child.getParent());
                    if (parent.getBomItemType().equals(BomItemType.UNIT)) {
                        BomItem parent1 = bomItemRepository.findOne(parent.getParent());
                        if (parent1.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                            BomItem parent2 = bomItemRepository.findOne(parent1.getParent());
                            if (parent2.getBomItemType().equals(BomItemType.SECTION) || parent2.getBomItemType().equals(BomItemType.COMMONPART)) {
                                bomItemSection = parent2;
                            }
                        } else {
                            bomItemSection = parent1;
                        }
                    } else if (parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                        BomItem parent1 = bomItemRepository.findOne(parent.getParent());
                        bomItemSection = parent1;
                    } else if (parent.getBomItemType().equals(BomItemType.COMMONPART)) {
                        bomItemSection = parent;
                    }
                    Inventory withSectionInventory = null;
                    Inventory withOutSectionInventory = null;

                    if (bomItemSection != null) {
                        withSectionInventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSection(bomId, child.getItem().getId(), child.getUniqueCode(), bomItemSection.getTypeRef().getId());
                        bomAllocationDto.getItemInstances().addAll(itemInstanceRepository.getItemInstancesBySectionAndBomAndUniqueCode(child.getItem().getId(), bomItemSection.getTypeRef().getId(), bomId, child.getUniqueCode()));
                        if (withSectionInventory != null) {
                            if (child.getItem().getItemMaster().getItemType().getHasLots()) {
                                bomAllocationDto.setFractionalStock(bomAllocationDto.getFractionalStock() + withSectionInventory.getFractionalQtyOnHand());
                                bomAllocationDto.setAllocated(bomAllocationDto.getAllocated() + withSectionInventory.getFractionalQtyAllocated());
                                bomAllocationDto.setIssuedQty(bomAllocationDto.getIssuedQty() + withSectionInventory.getFractionalQtyIssued());
                            } else {
                                bomAllocationDto.setStock(bomAllocationDto.getStock() + withSectionInventory.getQuantity());
                                bomAllocationDto.setAllocated(bomAllocationDto.getAllocated() + withSectionInventory.getQtyAllocated());
                                bomAllocationDto.setIssuedQty(bomAllocationDto.getIssuedQty() + withSectionInventory.getQtyIssued());
                            }
                        }
                    }
                    bomAllocationDto.getItemInstances().addAll(itemInstanceRepository.getItemInstancesByBomAndUniqueCodeAndSectionIsNull(child.getItem().getId(), bomId, child.getUniqueCode()));

                    bomAllocationDto.getItemInstances().forEach(itemInstance -> {
                        ObjectTypeAttribute certificateNumberType = objectTypeAttributeRepository.findByNameAndObjectType("Certificate Number", ObjectType.valueOf("ITEMINSTANCE"));

                        if (certificateNumberType != null) {
                            ObjectAttribute certificateNumber = objectAttributeRepository.findByObjectIdAndAttributeDefId(itemInstance.getId(), certificateNumberType.getId());

                            if (certificateNumber != null && certificateNumber.getStringValue() != null) {
                                itemInstance.setCertificateNumber(certificateNumber.getStringValue());
                            }
                        }
                    });

                    withOutSectionInventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSectionIsNull(bomId, child.getItem().getId(), child.getUniqueCode());
                    if (withOutSectionInventory != null) {
                        if (child.getItem().getItemMaster().getItemType().getHasLots()) {
                            bomAllocationDto.setFractionalStock(bomAllocationDto.getFractionalStock() + withOutSectionInventory.getFractionalQtyOnHand());
                            bomAllocationDto.setCommonAllocated(bomAllocationDto.getCommonAllocated() + withOutSectionInventory.getFractionalQtyAllocated());
                            bomAllocationDto.setCommonIssuedQty(bomAllocationDto.getCommonIssuedQty() + withOutSectionInventory.getFractionalQtyIssued());
                        } else {
                            bomAllocationDto.setStock(bomAllocationDto.getStock() + withOutSectionInventory.getQuantity());
                            bomAllocationDto.setCommonAllocated(bomAllocationDto.getCommonAllocated() + withOutSectionInventory.getQtyAllocated());
                            bomAllocationDto.setCommonIssuedQty(bomAllocationDto.getCommonIssuedQty() + withOutSectionInventory.getQtyIssued());
                        }
                    }


                    if (ids.length > 0) {
                        Map<Integer, ItemAllocation> allocationMap = new HashMap();
                        for (Integer instance : ids) {
                            BomInstance bomInstance = bomInstanceMap.get(instance);
                            BomInstanceItem bomInstanceItem = bomInstanceItemRepository.findByBomItemAndStartWithIdPath(child.getId(), bomInstance.getId().toString());
                            if (bomInstanceItem != null) {
                                ItemAllocation itemAllocation = allocationRepository.findByBomAndBomInstanceAndBomInstanceItem(bomId, bomInstance.getId(), bomInstanceItem.getId());
                                if (itemAllocation == null) {
                                    itemAllocation = new ItemAllocation();
                                    itemAllocation.setId(null);
                                    itemAllocation.setAllocateQty(0.0);
                                    itemAllocation.setFailedQty(0.0);
                                    itemAllocation.setIssuedQty(0.0);
                                    itemAllocation.setBomInstanceItem(bomInstanceItem.getId());
                                    itemAllocation.setBomInstance(bomInstance.getId());
                                }
                                itemAllocation.setItemInstance(instance);
                                allocationMap.put(instance, itemAllocation);
                            }
                        }
                        bomAllocationDto.setListMap(allocationMap);
                    }
                }
                calculateItemShortage(bomAllocationDto, ids, selectedIds);
                allocationDto.getChildren().add(bomAllocationDto);
                bomAllocationDtos.add(bomAllocationDto);

                bomAllocationDtos = visitChildren(bomId, bomAllocationDto, bomAllocationDtos, ids, selectedIds);
            }
        }

        return bomAllocationDtos;
    }


    @Async
    public void calculateShortage(List<BomAllocationDto> bomAllocationDtos, Integer[] missileIds, Integer[] selectedIds) {
//        Double fShortage = 0.0;
//        for (BomAllocationDto bomAllocationDto : bomAllocationDtos) {
        bomAllocationDtos.parallelStream().forEach((bomAllocationDto) -> {
            /*fShortage = 0.0;
            if (bomAllocationDto.getListMap().size() > 0) {
                Map<Integer, ItemAllocation> allocationMap = bomAllocationDto.getListMap();
                for (ItemAllocation itemAllocation : allocationMap.values()) {
                    if (bomAllocationDto.getItem().getItem().getItemMaster().getItemType().getHasLots()) {
                        fShortage = fShortage + (bomAllocationDto.getItem().getFractionalQuantity() + itemAllocation.getFailedQty()) - (itemAllocation.getAllocateQty() *//*+ itemAllocation.getIssuedQty()*//*);
                    } else {
                        fShortage = fShortage + (bomAllocationDto.getItem().getQuantity() + itemAllocation.getFailedQty()) - (itemAllocation.getAllocateQty()*//* + itemAllocation.getIssuedQty()*//*);
                    }
                }
            }
            bomAllocationDto.setShortage(fShortage);*/
            calculateItemShortage(bomAllocationDto, missileIds, selectedIds);
        });
    }

    @Async
    public void calculateItemShortage(BomAllocationDto bomAllocationDto, Integer[] missileIds, Integer[] selectedIds) {
        Double fShortage = 0.0;
        Double issueQty = 0.0;
        List<Integer> list = Arrays.asList(selectedIds);
        Double fTempStock = calculateFractionalStock(bomAllocationDto, missileIds);
        Integer tempStock = calculateStock(bomAllocationDto, missileIds);
        if (bomAllocationDto.getListMap().size() > 0) {
            Map<Integer, ItemAllocation> allocationMap = bomAllocationDto.getListMap();
            for (ItemAllocation itemAllocation : allocationMap.values()) {
                if (list.contains(itemAllocation.getItemInstance())) {
                    if (bomAllocationDto.getItem().getItem().getItemMaster().getItemType().getHasLots()) {
                        fShortage = fShortage + (bomAllocationDto.getItem().getFractionalQuantity() + itemAllocation.getFailedQty()) - (itemAllocation.getAllocateQty() /*+ itemAllocation.getIssuedQty()*/);
                        if (fShortage <= fTempStock) {
                            fTempStock = fTempStock - fShortage;
                            fShortage = 0.0;
                        } else if (fTempStock > 0) {
                            fShortage = fShortage - fTempStock;
                            fTempStock = 0.0;
                        }
                    } else {
                        fShortage = fShortage + (bomAllocationDto.getItem().getQuantity() + itemAllocation.getFailedQty()) - (itemAllocation.getAllocateQty()/* + itemAllocation.getIssuedQty()*/);

                        if (fShortage <= tempStock) {
                            tempStock = tempStock - fShortage.intValue();
                            fShortage = 0.0;
                        } else if (tempStock > 0) {
                            fShortage = fShortage - tempStock;
                            tempStock = 0;
                        }
                    }
                }
                issueQty = issueQty + itemAllocation.getIssuedQty();
            }
        }
        bomAllocationDto.setShortage(fShortage);
        bomAllocationDto.setItemIssued(issueQty);
    }

    @Transactional(readOnly = true)
    public List<MissileAllocationDto> getBomInstanceChildrenInventory(Integer bomId, Integer bomItemId) {

        List<MissileAllocationDto> inventoryDtoList = new ArrayList<>();

        List<BomInstanceItem> bomInstanceItems = bomInstanceItemRepository.findByParentOrderByCreatedDateAsc(bomItemId);

        bomInstanceItems.forEach(bomInstanceItem -> {
            bomInstanceItem.getChildren().addAll(bomInstanceItemRepository.findByParentOrderByCreatedDateAsc(bomInstanceItem.getId()));
            MissileAllocationDto allocationDto = new MissileAllocationDto();
            if (bomInstanceItem.getBomItemType().equals(BomItemType.PART)) {
                ItemType itemType = integerItemTypeMap.get(bomInstanceItem.getItem().getItemMaster().getItemType().getParentType());

                if (!itemType.getParentNode()) {
                    bomInstanceItem.getItem().getItemMaster().setParentType(itemType);
                } else {
                    bomInstanceItem.getItem().getItemMaster().setParentType(bomInstanceItem.getItem().getItemMaster().getItemType());
                }
            }

            allocationDto.setItem(bomInstanceItem);
            inventoryDtoList.add(allocationDto);
        });

        return inventoryDtoList;
    }

    @Transactional(readOnly = false)
    public void planInventoryToCurrentMissiles(List<ItemAllocation> itemAllocations) {

        itemAllocations.forEach(itemAllocation -> {
            BomInstanceItem bomInstanceItem = bomInstanceItemRepository.findOne(itemAllocation.getBomInstanceItem());

            BomInstanceItem section = null;
            BomInstanceItem parent = bomInstanceItemRepository.findOne(bomInstanceItem.getParent());
            if (parent.getBomItemType().equals(BomItemType.UNIT)) {
                BomInstanceItem parent1 = bomInstanceItemRepository.findOne(parent.getParent());
                if (parent1.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                    BomInstanceItem parent2 = bomInstanceItemRepository.findOne(parent1.getParent());
                    if (parent2.getBomItemType().equals(BomItemType.SECTION) || parent2.getBomItemType().equals(BomItemType.COMMONPART)) {
                        section = parent2;
                    }
                } else {
                    section = parent1;
                }
            } else if (parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                BomInstanceItem parent1 = bomInstanceItemRepository.findOne(parent.getParent());
                section = parent1;
            } else if (parent.getBomItemType().equals(BomItemType.COMMONPART)) {
                section = parent;
            }

            Inventory inventory = null;
            if (section != null) {
                inventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSection(itemAllocation.getBom(), bomInstanceItem.getItem().getId(), bomInstanceItem.getUniqueCode(), section.getTypeRef().getId());
            }

            Inventory withOutSectionInventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSectionIsNull(itemAllocation.getBom(), bomInstanceItem.getItem().getId(), bomInstanceItem.getUniqueCode());


            if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                if (inventory != null && itemAllocation.getCurrentAllocateQty() != null) {
                    Double temp1 = inventory.getFractionalQtyOnHand() - (inventory.getFractionalQtyAllocated() - inventory.getFractionalQtyIssued());
                    if (temp1 >= itemAllocation.getCurrentAllocateQty()) {
                        inventory.setFractionalQtyAllocated(inventory.getFractionalQtyAllocated() + itemAllocation.getCurrentAllocateQty());
                        inventoryRepository.save(inventory);
                    } else if (temp1 < itemAllocation.getCurrentAllocateQty()) {
                        inventory.setFractionalQtyAllocated(inventory.getFractionalQtyAllocated() + temp1);
                        inventoryRepository.save(inventory);
                        if (withOutSectionInventory != null) {
                            Double temp2 = (itemAllocation.getCurrentAllocateQty() - temp1);
                            if (temp2 > 0) {
                                withOutSectionInventory.setFractionalQtyAllocated(withOutSectionInventory.getFractionalQtyAllocated() + temp2);
                                inventoryRepository.save(withOutSectionInventory);
                            }
                        }
                    } else {
                        if (withOutSectionInventory != null) {
                            Double temp = itemAllocation.getCurrentAllocateQty() - inventory.getFractionalQtyOnHand();
                            if (temp > 0) {
                                withOutSectionInventory.setFractionalQtyAllocated(withOutSectionInventory.getFractionalQtyAllocated() + temp);
                                inventoryRepository.save(withOutSectionInventory);
                            }
                        }
                    }
                } else if (withOutSectionInventory != null && itemAllocation.getCurrentAllocateQty() != null) {
                    if ((withOutSectionInventory.getFractionalQtyOnHand() - withOutSectionInventory.getFractionalQtyAllocated()) >= itemAllocation.getCurrentAllocateQty()) {
                        withOutSectionInventory.setFractionalQtyAllocated(withOutSectionInventory.getFractionalQtyAllocated() + itemAllocation.getCurrentAllocateQty());
                        inventoryRepository.save(withOutSectionInventory);
                    }
                }
            } else {
                if (inventory != null && itemAllocation.getCurrentAllocateQty() != null) {
                    Integer temp1 = inventory.getQuantity() - (inventory.getQtyAllocated() - inventory.getQtyIssued());
                    if (temp1 >= itemAllocation.getCurrentAllocateQty()) {
                        inventory.setQtyAllocated(inventory.getQtyAllocated() + itemAllocation.getCurrentAllocateQty().intValue());
                        inventoryRepository.save(inventory);
                    } else if (temp1 < itemAllocation.getCurrentAllocateQty()) {
                        inventory.setQtyAllocated(inventory.getQtyAllocated() + temp1);
                        inventoryRepository.save(inventory);
                        if (withOutSectionInventory != null) {
                            Integer temp2 = (itemAllocation.getCurrentAllocateQty().intValue() - temp1);
                            if (temp2 > 0) {
                                withOutSectionInventory.setQtyAllocated(withOutSectionInventory.getQtyAllocated() + temp2);
                                inventoryRepository.save(withOutSectionInventory);
                            }
                        }
                    } else {
                        if (withOutSectionInventory != null && itemAllocation.getCurrentAllocateQty() != null) {
                            Integer temp;
                            if (inventory != null) {
                                temp = (itemAllocation.getCurrentAllocateQty().intValue()) - inventory.getQuantity();
                            } else {
                                temp = (itemAllocation.getCurrentAllocateQty().intValue());
                            }

                            if (temp > 0) {
                                withOutSectionInventory.setQtyAllocated(withOutSectionInventory.getQtyAllocated() + temp);
                                inventoryRepository.save(withOutSectionInventory);
                            }
                        }
                    }
                } else {
                    if (withOutSectionInventory != null && withOutSectionInventory.getQuantity() != null && itemAllocation.getCurrentAllocateQty() != null) {
                        if ((withOutSectionInventory.getQuantity()) >= itemAllocation.getCurrentAllocateQty()) {
                            withOutSectionInventory.setQtyAllocated(withOutSectionInventory.getQtyAllocated() + itemAllocation.getCurrentAllocateQty().intValue());
                            inventoryRepository.save(withOutSectionInventory);
                        }
                    }
                }
            }
        });

        allocationRepository.save(itemAllocations);
    }

    @Transactional(readOnly = false)
    public void resetInventoryToCurrentMissiles(List<ItemAllocation> itemAllocations) {

        itemAllocations.forEach(itemAllocation -> {
            BomInstanceItem bomInstanceItem = bomInstanceItemRepository.findOne(itemAllocation.getBomInstanceItem());

            BomInstanceItem section = null;
            BomInstanceItem parent = bomInstanceItemRepository.findOne(bomInstanceItem.getParent());
            if (parent.getBomItemType().equals(BomItemType.UNIT)) {
                BomInstanceItem parent1 = bomInstanceItemRepository.findOne(parent.getParent());
                if (parent1.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                    BomInstanceItem parent2 = bomInstanceItemRepository.findOne(parent1.getParent());
                    if (parent2.getBomItemType().equals(BomItemType.SECTION) || parent2.getBomItemType().equals(BomItemType.COMMONPART)) {
                        section = parent2;
                    }
                } else {
                    section = parent1;
                }
            } else if (parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                BomInstanceItem parent1 = bomInstanceItemRepository.findOne(parent.getParent());
                section = parent1;
            } else if (parent.getBomItemType().equals(BomItemType.COMMONPART)) {
                section = parent;
            }

            Inventory inventory = null;

            if (section != null) {
                inventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSection(itemAllocation.getBom(), bomInstanceItem.getItem().getId(), bomInstanceItem.getUniqueCode(), section.getTypeRef().getId());
            }

            Inventory withOutSectionInventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSectionIsNull(itemAllocation.getBom(), bomInstanceItem.getItem().getId(), bomInstanceItem.getUniqueCode());


            if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                if (inventory != null) {
                    if (inventory.getFractionalQtyOnHand() >= itemAllocation.getResetQty()) {
                        Double aDouble = inventory.getFractionalQtyAllocated() - itemAllocation.getResetQty();
                        if (aDouble >= 0.0) {
                            inventory.setFractionalQtyAllocated(aDouble);
                        } else {
                            inventory.setFractionalQtyAllocated(0.0);
                        }
                        inventoryRepository.save(inventory);
                    } else {
                        Double temp = itemAllocation.getResetQty() - inventory.getFractionalQtyOnHand();
                        if (inventory.getFractionalQtyOnHand() < itemAllocation.getResetQty()) {
                            inventory.setFractionalQtyAllocated(0.0);
                            inventoryRepository.save(inventory);
                        } else {
                            if (withOutSectionInventory != null) {
                                Double temp4 = withOutSectionInventory.getFractionalQtyAllocated() - temp;
                                if (temp4 <= 0.0) {
                                    withOutSectionInventory.setFractionalQtyAllocated(0.0);
                                } else {
                                    withOutSectionInventory.setFractionalQtyAllocated(temp4);
                                }

                                inventoryRepository.save(withOutSectionInventory);
                            }
                        }
                    }
                } else if (withOutSectionInventory != null) {
                    if (withOutSectionInventory.getFractionalQtyOnHand() >= itemAllocation.getResetQty()) {
                        Double temp5 = withOutSectionInventory.getFractionalQtyAllocated() - itemAllocation.getResetQty();
                        if (temp5 >= 0.0) {
                            withOutSectionInventory.setFractionalQtyAllocated(temp5);
                        } else {
                            withOutSectionInventory.setFractionalQtyAllocated(0.0);
                        }

                        inventoryRepository.save(withOutSectionInventory);
                    }
                }
            } else {
                if (inventory != null) {
                    if (inventory.getQuantity() >= itemAllocation.getResetQty()) {
                        if (inventory.getQtyAllocated() >= itemAllocation.getResetQty()) {
                            Integer integer = inventory.getQtyAllocated() - itemAllocation.getResetQty().intValue();
                            inventory.setQtyAllocated(integer);
                        } else {
                            Integer integer1 = itemAllocation.getResetQty().intValue() - inventory.getQtyAllocated();
                            inventory.setQtyAllocated(0);
                            if (withOutSectionInventory != null) {
                                if (withOutSectionInventory.getQtyAllocated() >= integer1) {
                                    Integer integer = withOutSectionInventory.getQtyAllocated() - integer1;
                                    if (integer <= 0) {
                                        withOutSectionInventory.setQtyAllocated(0);
                                    } else {
                                        withOutSectionInventory.setQtyAllocated(integer);
                                    }
                                    inventoryRepository.save(withOutSectionInventory);
                                }
                            }
                        }
                        inventoryRepository.save(inventory);
                    } else {
                        Integer temp = itemAllocation.getResetQty().intValue() - inventory.getQuantity();
                        if (inventory.getQuantity() < itemAllocation.getResetQty()) {
                            inventory.setQtyAllocated(0);
                            inventoryRepository.save(inventory);
                        } else {
                            if (withOutSectionInventory != null) {
                                Integer temp4 = withOutSectionInventory.getQtyAllocated() - temp;
                                if (temp4 >= 0) {
                                    withOutSectionInventory.setQtyAllocated(temp4);
                                } else {
                                    withOutSectionInventory.setQtyAllocated(0);
                                }

                                inventoryRepository.save(withOutSectionInventory);
                            }
                        }
                    }
                } else if (withOutSectionInventory != null) {
                    if (withOutSectionInventory.getQuantity() >= itemAllocation.getResetQty()) {
                        Integer temp5 = withOutSectionInventory.getQtyAllocated() - itemAllocation.getResetQty().intValue();
                        if (temp5 >= 0) {
                            withOutSectionInventory.setQtyAllocated(temp5);
                        } else {
                            withOutSectionInventory.setQtyAllocated(0);
                        }

                        inventoryRepository.save(withOutSectionInventory);
                    }
                }
            }
        });

        allocationRepository.save(itemAllocations);
    }

    public void reSetPlanForSelectedMissiles(Integer[] ids) {
        try {
            while (!bomItemsloaded) {
                Thread.sleep(1000 * 3);
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        List<ItemAllocation> itemAllocations1 = new ArrayList();
        for (BomAllocationDto bomAllocationDto : bomInventoryDtoList) {
            if (bomAllocationDto.getItem().getBomItemType().equals(BomItemType.PART)) {

                if (bomAllocationDto.getListMap().size() > 0) {
                    Map<Integer, ItemAllocation> allocationMap = bomAllocationDto.getListMap();
                    for (Integer id : ids) {

                        ItemAllocation itemAllocation = allocationMap.get(id);
                        if (itemAllocation != null) {
                            Double temp = 0.0;
                            if (bomAllocationDto.getItem().getItem().getItemMaster().getItemType().getHasLots()) {
                                if ((itemAllocation.getAllocateQty() - itemAllocation.getIssuedQty()) > 0) {
                                    temp = itemAllocation.getAllocateQty() - itemAllocation.getIssuedQty();
                                    itemAllocation.setAllocateQty(itemAllocation.getAllocateQty() - (itemAllocation.getAllocateQty() - (itemAllocation.getIssuedQty() + itemAllocation.getIssueProcessQty())));

                                    itemAllocation.setBom(tempBomId);
                                    itemAllocation.setResetQty(temp);

                                    itemAllocations1.add(itemAllocation);
                                }
                            } else {
                                if ((itemAllocation.getAllocateQty() - itemAllocation.getIssuedQty()) > 0) {
                                    temp = itemAllocation.getAllocateQty() - itemAllocation.getIssuedQty();
                                    itemAllocation.setAllocateQty(itemAllocation.getAllocateQty() - (itemAllocation.getAllocateQty() - (itemAllocation.getIssuedQty() + itemAllocation.getIssueProcessQty())));

                                    itemAllocation.setBom(tempBomId);
                                    itemAllocation.setResetQty(temp);

                                    itemAllocations1.add(itemAllocation);
                                }
                            }
                            if (bomAllocationDto.getAllocated() > 0) {
                                if (bomAllocationDto.getAllocated() >= temp) {
                                    bomAllocationDto.setAllocated(bomAllocationDto.getAllocated() - temp);
                                } else if (bomAllocationDto.getAllocated() < temp) {
                                    temp = temp - bomAllocationDto.getAllocated();
                                    bomAllocationDto.setAllocated(0.0);
                                }
                            }
                            if (bomAllocationDto.getCommonAllocated() > 0 && temp > 0) {
                                if (bomAllocationDto.getCommonAllocated() >= temp) {
                                    bomAllocationDto.setCommonAllocated(bomAllocationDto.getCommonAllocated() - temp);
                                } else if (bomAllocationDto.getCommonAllocated() < temp) {
                                    temp = temp - bomAllocationDto.getCommonAllocated();
                                    bomAllocationDto.setCommonAllocated(0.0);
                                }
                            }
                        }
                    }
                }
            }
        }

        resetInventoryToCurrentMissiles(itemAllocations1);
    }

    public void planForSelectedMissiles(Integer[] ids) {

        try {
            while (!this.bomItemsloaded) {
                Thread.sleep(1000 * 3);
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        if (bomItemsloaded) {
            Boolean planned = false;
            List<ItemAllocation> itemAllocations1 = new ArrayList();
            for (Integer id : ids) {
                for (BomAllocationDto bomAllocationDto : bomInventoryDtoList) {
                    if (bomAllocationDto.getItem().getBomItemType().equals(BomItemType.PART)) {
                        if (bomAllocationDto.getListMap().size() > 0) {

                            Double fTempStock = calculateFractionalStock(bomAllocationDto, ids);
                            Double fTempStock1 = fTempStock;
                            Integer tempStock = calculateStock(bomAllocationDto, ids);
                            Integer tempStock1 = tempStock;

                            Map<Integer, ItemAllocation> allocationMap = bomAllocationDto.getListMap();
                            ItemAllocation itemAllocation = allocationMap.get(id);

                            if (itemAllocation != null) {
                                if (bomAllocationDto.getItem().getItem().getItemMaster().getItemType().getHasLots()) {
                                    Double reqFractionalQty = bomAllocationDto.getItem().getFractionalQuantity() + itemAllocation.getFailedQty();
                                    if (itemAllocation.getAllocateQty() < reqFractionalQty) {
                                        if (reqFractionalQty < fTempStock) {
                                            if (itemAllocation.getAllocateQty() == 0) {
                                                itemAllocation.setAllocateQty(reqFractionalQty);
                                                itemAllocation.setCurrentAllocateQty(reqFractionalQty);
                                                bomAllocationDto.setAllocated(bomAllocationDto.getAllocated() + reqFractionalQty);

                                                fTempStock = fTempStock - itemAllocation.getAllocateQty();
                                            } else {
                                                fTempStock = fTempStock - itemAllocation.getAllocateQty();
                                                itemAllocation.setCurrentAllocateQty(reqFractionalQty - itemAllocation.getAllocateQty());

                                                itemAllocation.setAllocateQty(reqFractionalQty);
                                                bomAllocationDto.setAllocated(bomAllocationDto.getAllocated() + reqFractionalQty);
                                            }
                                        } else if (fTempStock > 0) {
                                            if (itemAllocation.getAllocateQty() == 0) {
                                                itemAllocation.setAllocateQty(fTempStock);
                                                itemAllocation.setCurrentAllocateQty(fTempStock);
                                                bomAllocationDto.setAllocated(bomAllocationDto.getAllocated() + fTempStock);
                                                fTempStock = fTempStock - itemAllocation.getAllocateQty();
                                            } else if ((reqFractionalQty - itemAllocation.getAllocateQty()) < fTempStock) {
                                                itemAllocation.setCurrentAllocateQty(reqFractionalQty - itemAllocation.getAllocateQty());
                                                itemAllocation.setAllocateQty(reqFractionalQty);
                                                bomAllocationDto.setAllocated(bomAllocationDto.getAllocated() + reqFractionalQty);
                                                fTempStock = fTempStock - (reqFractionalQty - itemAllocation.getAllocateQty());
                                            } else {
                                                itemAllocation.setAllocateQty(itemAllocation.getAllocateQty() + fTempStock);
                                                itemAllocation.setCurrentAllocateQty(fTempStock);
                                                bomAllocationDto.setAllocated(bomAllocationDto.getAllocated() + fTempStock);
                                                fTempStock = 0.0;
                                            }
                                        }
                                    }
                                } else {
                                    Integer reqQty = bomAllocationDto.getItem().getQuantity() + itemAllocation.getFailedQty().intValue();
                                    if (itemAllocation.getAllocateQty() < reqQty) {
                                        if (reqQty < tempStock) {
                                            if (itemAllocation.getAllocateQty() == 0) {
                                                itemAllocation.setAllocateQty(reqQty.doubleValue());
                                                itemAllocation.setCurrentAllocateQty(reqQty.doubleValue());
                                                bomAllocationDto.setAllocated(bomAllocationDto.getAllocated() + reqQty);
                                                tempStock = tempStock - itemAllocation.getAllocateQty().intValue();
                                            } else {
                                                tempStock = tempStock - itemAllocation.getAllocateQty().intValue();
                                                itemAllocation.setCurrentAllocateQty(reqQty - itemAllocation.getAllocateQty());
                                                itemAllocation.setAllocateQty(reqQty.doubleValue());
                                                bomAllocationDto.setAllocated(bomAllocationDto.getAllocated() + reqQty);
                                            }
                                        } else if (tempStock > 0) {
                                            if (itemAllocation.getAllocateQty() == 0) {
                                                itemAllocation.setAllocateQty(tempStock.doubleValue());
                                                itemAllocation.setCurrentAllocateQty(tempStock.doubleValue());
                                                bomAllocationDto.setAllocated(bomAllocationDto.getAllocated() + tempStock);
                                                tempStock = tempStock - itemAllocation.getAllocateQty().intValue();
                                            } else if ((reqQty - itemAllocation.getAllocateQty()) < tempStock) {
                                                itemAllocation.setCurrentAllocateQty(reqQty - itemAllocation.getAllocateQty());
                                                itemAllocation.setAllocateQty(reqQty.doubleValue());
                                                bomAllocationDto.setAllocated(bomAllocationDto.getAllocated() + reqQty);
                                                tempStock = tempStock - (reqQty - itemAllocation.getAllocateQty().intValue());
                                            } else {
                                                itemAllocation.setAllocateQty(itemAllocation.getAllocateQty() + tempStock);
                                                itemAllocation.setCurrentAllocateQty(fTempStock);
                                                bomAllocationDto.setAllocated(bomAllocationDto.getAllocated() + tempStock);
                                                tempStock = 0;
                                            }
                                        }
                                    }
                                }

                                itemAllocation.setBom(tempBomId);
                                itemAllocations1.add(itemAllocation);
                            }

                            if (tempStock != tempStock1 || fTempStock != fTempStock1) {
                                planned = true;
                            }
                        }
                    }
                }
            }
            if (planned) {
                planInventoryToCurrentMissiles(itemAllocations1);
            } else {
                throw new CassiniException("Allocation happen already on selected Section");
            }
        }

    }


    private Integer calculateStock(BomAllocationDto bomAllocationDto, Integer[] missilesIds) {
        Integer temp = bomAllocationDto.getStock();
        temp = temp - (bomAllocationDto.getAllocated().intValue() + bomAllocationDto.getCommonAllocated().intValue());
        for (Integer id : missilesIds) {
            ItemAllocation itemAllocation = bomAllocationDto.getListMap().get(id);
            if (itemAllocation != null) {
                temp = temp + itemAllocation.getIssuedQty().intValue();
            }
        }
        return temp;
    }

    private Double calculateFractionalStock(BomAllocationDto bomAllocationDto, Integer[] missilesIds) {
        Double temp = bomAllocationDto.getFractionalStock();
        temp = temp - (bomAllocationDto.getAllocated() + bomAllocationDto.getCommonAllocated());
        for (Integer id : missilesIds) {
            ItemAllocation itemAllocation = bomAllocationDto.getListMap().get(id);
            if (itemAllocation != null) {
                temp = temp + itemAllocation.getIssuedQty();
            }
        }
        return temp;
    }

}