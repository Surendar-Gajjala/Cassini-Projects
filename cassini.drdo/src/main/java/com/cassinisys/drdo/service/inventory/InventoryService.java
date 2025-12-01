package com.cassinisys.drdo.service.inventory;

import com.cassinisys.drdo.filtering.BomInstanceItemSearchPredicateBuilder;
import com.cassinisys.drdo.filtering.BomSearchCriteria;
import com.cassinisys.drdo.filtering.BomSearchPredicateBuilder;
import com.cassinisys.drdo.model.bom.*;
import com.cassinisys.drdo.model.dto.BomInstanceInventoryDto;
import com.cassinisys.drdo.model.dto.BomInventoryDto;
import com.cassinisys.drdo.model.inventory.Inventory;
import com.cassinisys.drdo.model.inventory.ItemAllocation;
import com.cassinisys.drdo.model.transactions.Issue;
import com.cassinisys.drdo.model.transactions.IssueItem;
import com.cassinisys.drdo.model.transactions.IssueStatus;
import com.cassinisys.drdo.model.transactions.RequestItem;
import com.cassinisys.drdo.repo.bom.*;
import com.cassinisys.drdo.repo.inventory.InventoryRepository;
import com.cassinisys.drdo.repo.inventory.ItemAllocationRepository;
import com.cassinisys.drdo.repo.transactions.InwardItemInstanceRepository;
import com.cassinisys.drdo.repo.transactions.IssueItemRepository;
import com.cassinisys.drdo.repo.transactions.IssueRepository;
import com.cassinisys.drdo.repo.transactions.RequestItemRepository;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by subramanyam reddy on 19-11-2018.
 */
@Service
public class InventoryService implements CrudService<Inventory, Integer> {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private BomRepository bomRepository;

    @Autowired
    private BomInstanceRepository bomInstanceRepository;

    @Autowired
    private BomItemRepository bomItemRepository;

    @Autowired
    private BomInstanceItemRepository bomInstanceItemRepository;

    @Autowired
    private ItemInstanceRepository itemInstanceRepository;

    @Autowired
    private RequestItemRepository requestItemRepository;

    @Autowired
    private BomItemInstanceRepository bomItemInstanceRepository;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private IssueItemRepository issueItemRepository;

    @Autowired
    private LotInstanceRepository lotInstanceRepository;

    @Autowired
    private ItemAllocationRepository itemAllocationRepository;

    @Autowired
    private ItemTypeRepository itemTypeRepository;

    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;

    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;

    @Autowired
    private InwardItemInstanceRepository inwardItemInstanceRepository;

    @Autowired
    private BomGroupRepository bomGroupRepository;

    @Autowired
    private BomSearchPredicateBuilder bomSearchPredicateBuilder;


    @Autowired
    private BomInstanceItemSearchPredicateBuilder bomInstanceItemSearchPredicateBuilder;

    @Override
    @Transactional(readOnly = false)
    public Inventory create(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    @Override
    @Transactional(readOnly = false)
    public Inventory update(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        inventoryRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Inventory get(Integer id) {
        return inventoryRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getAll() {
        return inventoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<BomInventoryDto> getBomInventory(Integer bomId) {
        List<BomInventoryDto> bomInventoryDtoList = new ArrayList<>();
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

                BomInventoryDto bomInventoryDto = new BomInventoryDto();
                bomInventoryDto.setItem(bomItem);

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
                                bomInventoryDto.setFractionalOnHold(bomInventoryDto.getFractionalOnHold() + withSectionInventory.getFractionalQtyBuffered());
                                bomInventoryDto.setFractionalStock(bomInventoryDto.getFractionalStock() + withSectionInventory.getFractionalQtyOnHand());
                            } else {
                                bomInventoryDto.setOnHold(bomInventoryDto.getOnHold() + withSectionInventory.getQtyBuffered());
                                bomInventoryDto.setStock(bomInventoryDto.getStock() + withSectionInventory.getQuantity());
                            }
                        }

                        bomInventoryDto.setItemInstances(itemInstanceRepository.findByItem(bomItem.getItem().getId()));
                    }

                    withOutSectionInventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSectionIsNull(bom.getId(), bomItem.getItem().getId(), bomItem.getUniqueCode());
                    if (withOutSectionInventory != null) {
                        if (bomItem.getItem().getItemMaster().getItemType().getHasLots()) {
                            bomInventoryDto.setFractionalOnHold(bomInventoryDto.getFractionalOnHold() + withOutSectionInventory.getFractionalQtyBuffered());
                            bomInventoryDto.setFractionalStock(bomInventoryDto.getFractionalStock() + withOutSectionInventory.getFractionalQtyOnHand());
                        } else {
                            bomInventoryDto.setOnHold(bomInventoryDto.getOnHold() + withOutSectionInventory.getQtyBuffered());
                            bomInventoryDto.setStock(bomInventoryDto.getStock() + withOutSectionInventory.getQuantity());
                        }
                    }
                }

                bomInventoryDtoList.add(bomInventoryDto);
            }
        }

        return bomInventoryDtoList;
    }

    @Transactional(readOnly = true)
    public List<BomInventoryDto> searchBomInventory(Integer bomId, String searchText) {
        List<BomInventoryDto> bomInventoryDtoList = new ArrayList<>();
        Bom bom = bomRepository.findOne(bomId);

        if (bom != null) {
            BomSearchCriteria bomSearchCriteria = new BomSearchCriteria();
            bomSearchCriteria.setBom(bomId);
            bomSearchCriteria.setSearchQuery(searchText);

            Predicate predicate = bomSearchPredicateBuilder.build(bomSearchCriteria, QBomItem.bomItem);

            Pageable pageable1 = new PageRequest(0, 1000,
                    new Sort(new Sort.Order(Sort.Direction.ASC, "createdDate")));

            List<BomItem> bomItemList = new ArrayList<>();
            Page<BomItem> bomItems = null;
            if (predicate != null) {
                bomItems = bomItemRepository.findAll(predicate, pageable1);

                Map<Integer, BomItem> unitMap = new HashMap<>();
                Map<Integer, BomItem> subsystemMap = new HashMap<>();
                Map<Integer, BomItem> sectionMap = new HashMap<>();

                for (BomItem bomItem : bomItems.getContent()) {
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
            }

            for (BomItem bomItem : bomItemList) {
                BomInventoryDto bomInventoryDto = new BomInventoryDto();
                bomInventoryDto.setItem(bomItem);
                bomInventoryDto.setLevel(bomItem.getLevel());
                bomInventoryDto.setExpanded(bomItem.getExpanded());
                if (bomItem.getBomItemType().equals(BomItemType.PART)) {
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

                    Inventory withSectionInventory = null;
                    Inventory withOutSectionInventory = null;

                    if (section != null) {
                        withSectionInventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSection(bom.getId(), bomItem.getItem().getId(), bomItem.getUniqueCode(), section.getTypeRef().getId());
                        if (withSectionInventory != null) {
                            if (bomItem.getItem().getItemMaster().getItemType().getHasLots()) {
                                bomInventoryDto.setFractionalOnHold(bomInventoryDto.getFractionalOnHold() + withSectionInventory.getFractionalQtyBuffered());
                                bomInventoryDto.setFractionalStock(bomInventoryDto.getFractionalStock() + withSectionInventory.getFractionalQtyOnHand());
                            } else {
                                bomInventoryDto.setOnHold(bomInventoryDto.getOnHold() + withSectionInventory.getQtyBuffered());
                                bomInventoryDto.setStock(bomInventoryDto.getStock() + withSectionInventory.getQuantity());
                            }
                        }
                        bomInventoryDto.getItemInstances().addAll(itemInstanceRepository.getItemInstancesBySectionAndBomAndUniqueCode(bomItem.getItem().getId(), section.getTypeRef().getId(), bomId, bomItem.getUniqueCode()));
                        bomInventoryDto.getOnHoldInstances().addAll(itemInstanceRepository.getOnHoldInstancesBySectionAndBomAndUniqueCode(bomItem.getItem().getId(), section.getTypeRef().getId(), bomId, bomItem.getUniqueCode()));
                        bomInventoryDto.getReturnInstances().addAll(itemInstanceRepository.getReturnInstancesByItemAndSectionAndUniqueCode(bomItem.getItem().getId(), ItemInstanceStatus.REJECTED, section.getTypeRef().getId(), bomItem.getUniqueCode()));
                    }

                    bomInventoryDto.getItemInstances().addAll(itemInstanceRepository.getItemInstancesByBomAndUniqueCodeAndSectionIsNull(bomItem.getItem().getId(), bomId, bomItem.getUniqueCode()));
                    bomInventoryDto.getOnHoldInstances().addAll(itemInstanceRepository.getOnHoldInstancesByBomAndUniqueCodeAndSectionIsNull(bomItem.getItem().getId(), bomId, bomItem.getUniqueCode()));
                    bomInventoryDto.getReturnInstances().addAll(itemInstanceRepository.getReturnInstancesByItemAndUniqueCodeAndSectionIsNull(bomItem.getItem().getId(), ItemInstanceStatus.REJECTED, bomItem.getUniqueCode()));

                    withOutSectionInventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSectionIsNull(bom.getId(), bomItem.getItem().getId(), bomItem.getUniqueCode());
                    if (withOutSectionInventory != null) {
                        if (bomItem.getItem().getItemMaster().getItemType().getHasLots()) {
                            bomInventoryDto.setFractionalOnHold(bomInventoryDto.getFractionalOnHold() + withOutSectionInventory.getFractionalQtyBuffered());
                            bomInventoryDto.setFractionalStock(bomInventoryDto.getFractionalStock() + withOutSectionInventory.getFractionalQtyOnHand());
                        } else {
                            bomInventoryDto.setOnHold(bomInventoryDto.getOnHold() + withOutSectionInventory.getQtyBuffered());
                            bomInventoryDto.setStock(bomInventoryDto.getStock() + withOutSectionInventory.getQuantity());
                        }
                    }

                    bomInventoryDto.getItemInstances().forEach(itemInstance1 -> {
                        if (itemInstance1.getItem().getItemMaster().getItemType().getHasLots()) {
                            List<LotInstance> lotInstances = lotInstanceRepository.findByInstance(itemInstance1.getId());
                            Double issuedQuantity = 0.0;
                            for (LotInstance lotInstance : lotInstances) {
                                issuedQuantity = issuedQuantity + lotInstance.getLotQty();
                            }
                            itemInstance1.setRemainingQuantity(itemInstance1.getLotSize() - issuedQuantity);

                        }
                    });
                }

                bomInventoryDtoList.add(bomInventoryDto);
            }
        }

        return bomInventoryDtoList;
    }


    @Transactional(readOnly = true)
    public List<BomInstanceInventoryDto> searchBomInstanceInventory(Integer bomId, String searchText) {
        List<BomInstanceInventoryDto> bomInstanceInventoryDtos = new ArrayList<>();
        BomInstance bomInstance = bomInstanceRepository.findOne(bomId);

        if (bomInstance != null) {
            BomSearchCriteria bomSearchCriteria = new BomSearchCriteria();
            bomSearchCriteria.setBom(bomId);
            bomSearchCriteria.setSearchQuery(searchText);

            Predicate predicate = bomInstanceItemSearchPredicateBuilder.build(bomSearchCriteria, QBomInstanceItem.bomInstanceItem);

            Pageable pageable1 = new PageRequest(0, 1000,
                    new Sort(new Sort.Order(Sort.Direction.ASC, "createdDate")));

            List<BomInstanceItem> bomItemList = new ArrayList<>();
            Page<BomInstanceItem> bomItems = null;
            if (predicate != null) {
                bomItems = bomInstanceItemRepository.findAll(predicate, pageable1);

                Map<Integer, BomInstanceItem> unitMap = new HashMap<>();
                Map<Integer, BomInstanceItem> subsystemMap = new HashMap<>();
                Map<Integer, BomInstanceItem> sectionMap = new HashMap<>();

                for (BomInstanceItem bomItem : bomItems.getContent()) {
                    BomInstanceItem unit = null;
                    BomInstanceItem subsystem = null;
                    BomInstanceItem section = null;

                    BomInstanceItem parentBomItem = bomInstanceItemRepository.findOne(bomItem.getParent());
                    if (parentBomItem.getBomItemType().equals(BomItemType.UNIT)) {
                        unit = parentBomItem;
                        parentBomItem = bomInstanceItemRepository.findOne(unit.getParent());
                        if (parentBomItem.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                            subsystem = parentBomItem;
                            section = bomInstanceItemRepository.findOne(subsystem.getParent());
                        } else if (parentBomItem.getBomItemType().equals(BomItemType.SECTION) || parentBomItem.getBomItemType().equals(BomItemType.COMMONPART)) {
                            section = parentBomItem;
                        }
                    } else if (parentBomItem.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                        subsystem = parentBomItem;
                        section = bomInstanceItemRepository.findOne(subsystem.getParent());
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

                            BomInstanceItem sectionItem = bomItemList.get(bomItemList.indexOf(section));
                            sectionItem.getChildren().add(subsystem);

                            subsystemMap.put(subsystem.getId(), subsystem);

                            if (unit != null && unitMap.get(unit.getId()) == null) {
                                unit.setLevel(subsystem.getLevel() + 1);
                                unit.setExpanded(true);
                                bomItemList.add(bomItemList.indexOf(subsystem) + 1, unit);

                                BomInstanceItem subsystemItem = bomItemList.get(bomItemList.indexOf(subsystem));
                                subsystemItem.getChildren().add(unit);

                                unitMap.put(unit.getId(), unit);

                                bomItem.setLevel(unit.getLevel() + 1);
                                bomItemList.add(bomItemList.indexOf(unit) + 1, bomItem);

                                BomInstanceItem unitItem = bomItemList.get(bomItemList.indexOf(unit));
                                unitItem.getChildren().add(bomItem);

                            } else if (unit != null) {
                                BomInstanceItem existUnit = unitMap.get(unit.getId());
                                bomItem.setLevel(existUnit.getLevel());
                                bomItemList.add(bomItemList.indexOf(existUnit) + 1, bomItem);
                                BomInstanceItem unitItem = bomItemList.get(bomItemList.indexOf(existUnit));
                                unitItem.getChildren().add(bomItem);
                            } else {
                                bomItem.setLevel(subsystem.getLevel() + 1);
                                bomItemList.add(bomItemList.indexOf(subsystem) + 1, bomItem);

                                BomInstanceItem subsystemItem = bomItemList.get(bomItemList.indexOf(subsystem));
                                subsystemItem.getChildren().add(bomItem);
                            }
                        } else if (subsystem != null) {
                            BomInstanceItem existSubsystem = subsystemMap.get(subsystem.getId());

                            if (unit != null && unitMap.get(unit.getId()) == null) {
                                unit.setLevel(existSubsystem.getLevel() + 1);
                                unit.setExpanded(true);
                                bomItemList.add(bomItemList.indexOf(existSubsystem) + 1, unit);
                                unitMap.put(unit.getId(), unit);

                                BomInstanceItem subsystemItem = bomItemList.get(bomItemList.indexOf(existSubsystem));
                                subsystemItem.getChildren().add(unit);

                                bomItem.setLevel(unit.getLevel() + 1);
                                bomItemList.add(bomItemList.indexOf(unit) + 1, bomItem);

                                BomInstanceItem unitItem = bomItemList.get(bomItemList.indexOf(unit));
                                unitItem.getChildren().add(bomItem);
                            } else if (unit != null) {
                                BomInstanceItem existUnit = unitMap.get(unit.getId());
                                bomItem.setLevel(existUnit.getLevel() + 1);
                                bomItemList.add(bomItemList.indexOf(existUnit) + 1, bomItem);

                                BomInstanceItem unitItem = bomItemList.get(bomItemList.indexOf(existUnit));
                                unitItem.getChildren().add(bomItem);
                            } else {
                                bomItem.setLevel(existSubsystem.getLevel());
                                bomItemList.add(bomItemList.indexOf(existSubsystem) + 1, bomItem);

                                BomInstanceItem subsystemItem = bomItemList.get(bomItemList.indexOf(existSubsystem));
                                subsystemItem.getChildren().add(bomItem);
                            }
                        } else {
                            bomItem.setLevel(section.getLevel() + 1);
                            bomItemList.add(bomItemList.indexOf(section) + 1, bomItem);

                            BomInstanceItem sectionItem = bomItemList.get(bomItemList.indexOf(section));
                            sectionItem.getChildren().add(bomItem);
                        }
                    } else if (section != null) {
                        BomInstanceItem existSection = sectionMap.get(section.getId());

                        if (subsystem != null && subsystemMap.get(subsystem.getId()) == null) {
                            subsystem.setLevel(existSection.getLevel() + 1);
                            subsystem.setExpanded(true);
                            bomItemList.add(bomItemList.indexOf(existSection) + 1, subsystem);

                            BomInstanceItem sectionItem = bomItemList.get(bomItemList.indexOf(existSection));
                            sectionItem.getChildren().add(subsystem);

                            subsystemMap.put(subsystem.getId(), subsystem);

                            if (unit != null && unitMap.get(unit.getId()) == null) {
                                unit.setLevel(subsystem.getLevel() + 1);
                                unit.setExpanded(true);
                                bomItemList.add(bomItemList.indexOf(subsystem) + 1, unit);

                                BomInstanceItem subsystemItem = bomItemList.get(bomItemList.indexOf(subsystem));
                                subsystemItem.getChildren().add(unit);

                                unitMap.put(unit.getId(), unit);

                                bomItem.setLevel(unit.getLevel() + 1);
                                bomItemList.add(bomItemList.indexOf(unit) + 1, bomItem);

                                BomInstanceItem unitItem = bomItemList.get(bomItemList.indexOf(unit));
                                unitItem.getChildren().add(bomItem);
                            }
                        } else if (subsystem != null) {
                            BomInstanceItem existSubsystem = subsystemMap.get(subsystem.getId());

                            if (unit != null && unitMap.get(unit.getId()) == null) {
                                unit.setLevel(existSubsystem.getLevel() + 1);
                                unit.setExpanded(true);
                                bomItemList.add(bomItemList.indexOf(existSubsystem) + 1, unit);

                                BomInstanceItem subsystemItem = bomItemList.get(bomItemList.indexOf(existSubsystem));
                                subsystemItem.getChildren().add(unit);

                                unitMap.put(unit.getId(), unit);

                                bomItem.setLevel(unit.getLevel() + 1);
                                bomItemList.add(bomItemList.indexOf(unit) + 1, bomItem);

                                BomInstanceItem unitItem = bomItemList.get(bomItemList.indexOf(unit));
                                unitItem.getChildren().add(bomItem);
                            } else if (unit != null) {
                                BomInstanceItem existUnit = unitMap.get(unit.getId());
                                bomItem.setLevel(existUnit.getLevel() + 1);
                                bomItemList.add(bomItemList.indexOf(existUnit) + 1, bomItem);

                                BomInstanceItem unitItem = bomItemList.get(bomItemList.indexOf(existUnit));
                                unitItem.getChildren().add(bomItem);
                            } else {
                                bomItem.setLevel(existSubsystem.getLevel() + 1);
                                bomItemList.add(bomItemList.indexOf(existSubsystem) + 1, bomItem);

                                BomInstanceItem subsystemItem = bomItemList.get(bomItemList.indexOf(existSubsystem));
                                subsystemItem.getChildren().add(bomItem);
                            }
                        } else {
                            bomItem.setLevel(existSection.getLevel() + 1);
                            bomItemList.add(bomItemList.indexOf(existSection) + 1, bomItem);

                            BomInstanceItem sectionItem = bomItemList.get(bomItemList.indexOf(existSection));
                            sectionItem.getChildren().add(bomItem);
                        }
                    }
                }
            }

            for (BomInstanceItem bomInstanceItem : bomItemList) {
                BomInstanceInventoryDto bomInventoryDto = new BomInstanceInventoryDto();

                bomInventoryDto.setItem(bomInstanceItem);
                bomInventoryDto.setLevel(bomInstanceItem.getLevel());
                bomInventoryDto.setExpanded(bomInstanceItem.getExpanded());

                if (bomInstanceItem.getBomItemType().equals(BomItemType.PART)) {

                    ItemType itemType = itemTypeRepository.findOne(bomInstanceItem.getItem().getItemMaster().getItemType().getParentType());

                    if (!itemType.getParentNode()) {
                        bomInstanceItem.getItem().getItemMaster().setParentType(itemType);
                    } else {
                        bomInstanceItem.getItem().getItemMaster().setParentType(bomInstanceItem.getItem().getItemMaster().getItemType());
                    }

                    List<RequestItem> requestItems = requestItemRepository.getRequestedByInstanceAndItem(bomInstance.getId(), bomInstanceItem.getId());
                    Integer qty = 0;
                    Double fractionalQty = 0.0;

                    if (requestItems.size() > 0) {
                        for (RequestItem requestItem : requestItems) {
                            if (requestItem.getItem().getItem().getItemMaster().getItemType().getHasLots()) {
                                fractionalQty = fractionalQty + requestItem.getFractionalQuantity();
                            } else {
                                qty = qty + requestItem.getQuantity();
                            }
                        }
                    }
                    bomInventoryDto.setRequested(qty);
                    bomInventoryDto.setFractionalRequested(fractionalQty);

                    List<Issue> issueList = issueRepository.findByBomInstance(bomInstance.getId());

                    for (Issue issue : issueList) {

                        if (issue.getStatus().equals(IssueStatus.RECEIVED) || issue.getStatus().equals(IssueStatus.PARTIALLY_RECEIVED)) {
                            if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                                List<IssueItem> issueItems = issueItemRepository.getReceivedItemsByIssueAndBomItemInstance(issue.getId(), bomInstanceItem.getId());

                                issueItems.forEach(issueItem -> {
                                    if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                                        bomInventoryDto.getIssuedLotInstances().addAll(lotInstanceRepository.findByIssueItem(issueItem.getId()));
                                    } else {
                                        bomInventoryDto.getIssuedInstances().add(itemInstanceRepository.findOne(issueItem.getBomItemInstance().getItemInstance()));
                                    }
                                });

                                bomInventoryDto.setFractionalIssued(bomInventoryDto.getFractionalIssued() + issueItems.size());
                            } else {
                                List<IssueItem> issueItems = issueItemRepository.getReceivedItemsByIssueAndBomItemInstance(issue.getId(), bomInstanceItem.getId());

                                issueItems.forEach(issueItem -> {
                                    bomInventoryDto.getIssuedInstances().add(itemInstanceRepository.findOne(issueItem.getBomItemInstance().getItemInstance()));
                                });

                                bomInventoryDto.setIssued(bomInventoryDto.getIssued() + issueItems.size());
                            }

                            bomInventoryDto.getIssuedLotInstances().forEach(lotInstance -> {
                                lotInstance.setItemInstance(itemInstanceRepository.findOne(lotInstance.getInstance()));
                                ObjectTypeAttribute certificateNumberType = objectTypeAttributeRepository.findByNameAndObjectType("Certificate Number", ObjectType.valueOf("ITEMINSTANCE"));

                                if (certificateNumberType != null) {
                                    ObjectAttribute certificateNumber = objectAttributeRepository.findByObjectIdAndAttributeDefId(lotInstance.getInstance(), certificateNumberType.getId());

                                    if (certificateNumber != null && certificateNumber.getStringValue() != null) {
                                        lotInstance.setCertificateNumber(certificateNumber.getStringValue());
                                    }
                                }
                            });

                            bomInventoryDto.getIssuedInstances().forEach(itemInstance -> {
                                ObjectTypeAttribute certificateNumberType = objectTypeAttributeRepository.findByNameAndObjectType("Certificate Number", ObjectType.valueOf("ITEMINSTANCE"));

                                if (certificateNumberType != null) {
                                    ObjectAttribute certificateNumber = objectAttributeRepository.findByObjectIdAndAttributeDefId(itemInstance.getId(), certificateNumberType.getId());

                                    if (certificateNumber != null && certificateNumber.getStringValue() != null) {
                                        itemInstance.setCertificateNumber(certificateNumber.getStringValue());
                                    }
                                }
                            });
                        }

                    }

                    ItemAllocation itemAllocation = itemAllocationRepository.findByBomInstanceAndBomInstanceItem(bomId, bomInstanceItem.getId());
                    if (itemAllocation != null) {
                        bomInventoryDto.setAllocatedQty(itemAllocation.getAllocateQty());
                    }

                }
                bomInventoryDto.setItem(bomInstanceItem);

                bomInstanceInventoryDtos.add(bomInventoryDto);
            }
        }

        return bomInstanceInventoryDtos;
    }


    @Transactional(readOnly = true)
    public List<BomInventoryDto> getBomChildrenInventory(Integer bomId, Integer bomItemId) {

        List<BomInventoryDto> bomInventoryDtoList = new ArrayList<>();

        BomItem selectedItem = bomItemRepository.findOne(bomItemId);

        List<BomItem> bomItems = bomItemRepository.findByParentOrderByCreatedDateAsc(bomItemId);

        bomItems.forEach(bomItem -> {
            bomItem.getChildren().addAll(bomItemRepository.findByParentOrderByCreatedDateAsc(bomItem.getId()));
            BomInventoryDto bomInventoryDto = new BomInventoryDto();
            if (bomItem.getBomItemType().equals(BomItemType.PART)) {
                ItemType itemType = itemTypeRepository.findOne(bomItem.getItem().getItemMaster().getItemType().getParentType());

                if (!itemType.getParentNode()) {
                    bomItem.getItem().getItemMaster().setParentType(itemType);
                } else {
                    bomItem.getItem().getItemMaster().setParentType(bomItem.getItem().getItemMaster().getItemType());
                }
            }
            bomInventoryDto.setItem(bomItem);
            if (selectedItem.getBomItemType().equals(BomItemType.SECTION) || selectedItem.getBomItemType().equals(BomItemType.COMMONPART)) {
                bomInventoryDto.setLevel(1);
                bomInventoryDto.setExpanded(true);
            }

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
                    if (withSectionInventory != null) {
                        if (bomItem.getItem().getItemMaster().getItemType().getHasLots()) {
                            bomInventoryDto.setFractionalOnHold(bomInventoryDto.getFractionalOnHold() + withSectionInventory.getFractionalQtyBuffered());
                            bomInventoryDto.setFractionalStock(bomInventoryDto.getFractionalStock() + withSectionInventory.getFractionalQtyOnHand());
                        } else {
                            bomInventoryDto.setOnHold(bomInventoryDto.getOnHold() + withSectionInventory.getQtyBuffered());
                            bomInventoryDto.setStock(bomInventoryDto.getStock() + withSectionInventory.getQuantity());
                        }
                    }
                }

                withOutSectionInventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSectionIsNull(bomId, bomItem.getItem().getId(), bomItem.getUniqueCode());
                if (withOutSectionInventory != null) {
                    if (bomItem.getItem().getItemMaster().getItemType().getHasLots()) {
                        bomInventoryDto.setFractionalOnHold(bomInventoryDto.getFractionalOnHold() + withOutSectionInventory.getFractionalQtyBuffered());
                        bomInventoryDto.setFractionalStock(bomInventoryDto.getFractionalStock() + withOutSectionInventory.getFractionalQtyOnHand());
                    } else {
                        bomInventoryDto.setOnHold(bomInventoryDto.getOnHold() + withOutSectionInventory.getQtyBuffered());
                        bomInventoryDto.setStock(bomInventoryDto.getStock() + withOutSectionInventory.getQuantity());
                    }
                }

                String uniqueCode = bomItem.getUniqueCode();
                if (bomItemSection != null) {

                    bomInventoryDto.getItemInstances().addAll(itemInstanceRepository.getItemInstancesBySectionAndBomAndUniqueCode(bomItem.getItem().getId(), bomItemSection.getTypeRef().getId(), bomId, bomItem.getUniqueCode()));
                    bomInventoryDto.getOnHoldInstances().addAll(itemInstanceRepository.getOnHoldInstancesBySectionAndBomAndUniqueCode(bomItem.getItem().getId(), bomItemSection.getTypeRef().getId(), bomId, bomItem.getUniqueCode()));

                    /*List<ItemInstance> itemInstances = itemInstanceRepository.getItemInstancesBySectionAndBom(bomItem.getItem().getId(), bomItemSection.getTypeRef().getId(), bomId);
                    itemInstances.forEach(itemInstance -> {
                        String itemInstanceUniqueCode = null;
                        if (itemInstance.getItem().getItemMaster().getItemType().getHasLots()) {
                            if (itemInstance.getItem().getItemMaster().getItemType().getHasSpec()) {
                                itemInstanceUniqueCode = itemInstance.getInitialUpn().substring(5, 14);
                            } else {
                                itemInstanceUniqueCode = itemInstance.getInitialUpn().substring(5, 10) + "" + "0000";
                            }
                        } else {
                            if (itemInstance.getItem().getItemMaster().getItemType().getHasSpec()) {
                                String spec = "0000";
                                String specName = itemInstance.getItem().getItemMaster().getPartSpec().getSpecName();
                                String serialNumber = "";
                                if (specName.length() > 4) {
                                    serialNumber = specName.substring(specName.length() - 4);
                                } else {
                                    serialNumber = specName;
                                }
                                spec = spec.substring(serialNumber.length()) + serialNumber;

                                itemInstanceUniqueCode = itemInstance.getInitialUpn().substring(5, 10) + "" + spec;
                            } else {
                                itemInstanceUniqueCode = itemInstance.getInitialUpn().substring(5, 10) + "" + "0000";
                            }
                        }


                        if (itemInstanceUniqueCode.equals(uniqueCode)) {
                            bomInventoryDto.getItemInstances().add(itemInstance);
                        }
                    });*/
                }

                bomInventoryDto.getItemInstances().addAll(itemInstanceRepository.getItemInstancesByBomAndUniqueCodeAndSectionIsNull(bomItem.getItem().getId(), bomId, bomItem.getUniqueCode()));
                bomInventoryDto.getOnHoldInstances().addAll(itemInstanceRepository.getOnHoldInstancesByBomAndUniqueCodeAndSectionIsNull(bomItem.getItem().getId(), bomId, bomItem.getUniqueCode()));

                /*List<ItemInstance> itemInstances = itemInstanceRepository.getItemInstancesByBomAndSectionIsNull(bomItem.getItem().getId(), bomId);
                itemInstances.forEach(itemInstance -> {
                    String commonUniqueCode = "000" + bomItem.getUniqueCode().substring(3, 9);
                    String itemInstanceUniqueCode = null;
                    if (itemInstance.getItem().getItemMaster().getItemType().getHasLots()) {
                        if (itemInstance.getItem().getItemMaster().getItemType().getHasSpec()) {
                            itemInstanceUniqueCode = itemInstance.getInitialUpn().substring(5, 14);
                        } else {
                            itemInstanceUniqueCode = itemInstance.getInitialUpn().substring(5, 10) + "" + "0000";
                        }
                    } else {
                        if (itemInstance.getItem().getItemMaster().getItemType().getHasSpec()) {
                            String spec = "0000";
                            String specName = itemInstance.getItem().getItemMaster().getPartSpec().getSpecName();
                            String serialNumber = "";
                            if (specName.length() > 4) {
                                serialNumber = specName.substring(specName.length() - 4);
                            } else {
                                serialNumber = specName;
                            }
                            spec = spec.substring(serialNumber.length()) + serialNumber;

                            itemInstanceUniqueCode = itemInstance.getInitialUpn().substring(5, 10) + "" + spec;
                        } else {
                            itemInstanceUniqueCode = itemInstance.getInitialUpn().substring(5, 10) + "" + "0000";
                        }
                    }

                    if (itemInstanceUniqueCode.equals(commonUniqueCode)) {
                        bomInventoryDto.getItemInstances().add(itemInstance);
                    }
                });*/


                bomInventoryDto.getItemInstances().forEach(itemInstance1 -> {
                    if (itemInstance1.getItem().getItemMaster().getItemType().getHasLots()) {
                        List<LotInstance> lotInstances = lotInstanceRepository.findByInstance(itemInstance1.getId());
                        Double issuedQuantity = 0.0;
                        for (LotInstance lotInstance : lotInstances) {
                            issuedQuantity = issuedQuantity + lotInstance.getLotQty();
                        }
                        itemInstance1.setRemainingQuantity(itemInstance1.getLotSize() - issuedQuantity);

                    }
                });


                if (bomItemSection != null) {

                    bomInventoryDto.getReturnInstances().addAll(itemInstanceRepository.getReturnInstancesByItemAndSectionAndUniqueCode(bomItem.getItem().getId(), ItemInstanceStatus.REJECTED, bomItemSection.getTypeRef().getId(), bomItem.getUniqueCode()));
                    /*List<ItemInstance> returnInstances = itemInstanceRepository.getReturnInstancesByItemAndSection(bomItem.getItem().getId(), ItemInstanceStatus.REJECTED, bomItemSection.getTypeRef().getId());
                    returnInstances.forEach(itemInstance -> {
                        String itemInstanceUniqueCode = null;
                        if (itemInstance.getItem().getItemMaster().getItemType().getHasLots()) {
                            if (itemInstance.getItem().getItemMaster().getItemType().getHasSpec()) {
                                itemInstanceUniqueCode = itemInstance.getInitialUpn().substring(5, 14);
                            } else {
                                itemInstanceUniqueCode = itemInstance.getInitialUpn().substring(5, 10) + "" + "0000";
                            }
                        } else {
                            if (itemInstance.getItem().getItemMaster().getItemType().getHasSpec()) {
                                String spec = "0000";
                                String specName = itemInstance.getItem().getItemMaster().getPartSpec().getSpecName();
                                String serialNumber = "";
                                if (specName.length() > 4) {
                                    serialNumber = specName.substring(specName.length() - 4);
                                } else {
                                    serialNumber = specName;
                                }
                                spec = spec.substring(serialNumber.length()) + serialNumber;

                                itemInstanceUniqueCode = itemInstance.getInitialUpn().substring(5, 10) + "" + spec;
                            } else {
                                itemInstanceUniqueCode = itemInstance.getInitialUpn().substring(5, 10) + "" + "0000";
                            }
                        }

                        if (itemInstanceUniqueCode.equals(uniqueCode)) {
                            bomInventoryDto.getReturnInstances().add(itemInstance);
                        }
                    });*/
                }

                bomInventoryDto.getReturnInstances().addAll(itemInstanceRepository.getReturnInstancesByItemAndUniqueCodeAndSectionIsNull(bomItem.getItem().getId(), ItemInstanceStatus.REJECTED, bomItem.getUniqueCode()));

                /*List<ItemInstance> returnInstances = itemInstanceRepository.getReturnInstancesByItemAndSectionIsNull(bomItem.getItem().getId(), ItemInstanceStatus.REJECTED);
                returnInstances.forEach(itemInstance -> {
                    String commonUniqueCode = "000" + bomItem.getUniqueCode().substring(3, 9);
                    String itemInstanceUniqueCode = null;
                    if (itemInstance.getItem().getItemMaster().getItemType().getHasLots()) {
                        if (itemInstance.getItem().getItemMaster().getItemType().getHasSpec()) {
                            itemInstanceUniqueCode = itemInstance.getInitialUpn().substring(5, 14);
                        } else {
                            itemInstanceUniqueCode = itemInstance.getInitialUpn().substring(5, 10) + "" + "0000";
                        }
                    } else {
                        if (itemInstance.getItem().getItemMaster().getItemType().getHasSpec()) {
                            String spec = "0000";
                            String specName = itemInstance.getItem().getItemMaster().getPartSpec().getSpecName();
                            String serialNumber = "";
                            if (specName.length() > 4) {
                                serialNumber = specName.substring(specName.length() - 4);
                            } else {
                                serialNumber = specName;
                            }
                            spec = spec.substring(serialNumber.length()) + serialNumber;

                            itemInstanceUniqueCode = itemInstance.getInitialUpn().substring(5, 10) + "" + spec;
                        } else {
                            itemInstanceUniqueCode = itemInstance.getInitialUpn().substring(5, 10) + "" + "0000";
                        }
                    }

                    if (itemInstanceUniqueCode.equals(commonUniqueCode)) {
                        bomInventoryDto.getReturnInstances().add(itemInstance);
                    }
                });*/

                bomInventoryDto.getItemInstances().forEach(itemInstance -> {
                    ObjectTypeAttribute certificateNumberType = objectTypeAttributeRepository.findByNameAndObjectType("Certificate Number", ObjectType.valueOf("ITEMINSTANCE"));

                    if (certificateNumberType != null) {
                        ObjectAttribute certificateNumber = objectAttributeRepository.findByObjectIdAndAttributeDefId(itemInstance.getId(), certificateNumberType.getId());

                        if (certificateNumber != null && certificateNumber.getStringValue() != null) {
                            itemInstance.setCertificateNumber(certificateNumber.getStringValue());
                        }
                    }
                });

                bomInventoryDto.getReturnInstances().forEach(itemInstance -> {

                    if (bomItem.getItem().getItemMaster().getItemType().getHasLots()) {
                        bomInventoryDto.setFractionalReturned(bomInventoryDto.getFractionalReturned() + itemInstance.getLotSize());
                    }

                    ObjectTypeAttribute certificateNumberType = objectTypeAttributeRepository.findByNameAndObjectType("Certificate Number", ObjectType.valueOf("ITEMINSTANCE"));

                    if (certificateNumberType != null) {
                        ObjectAttribute certificateNumber = objectAttributeRepository.findByObjectIdAndAttributeDefId(itemInstance.getId(), certificateNumberType.getId());

                        if (certificateNumber != null && certificateNumber.getStringValue() != null) {
                            itemInstance.setCertificateNumber(certificateNumber.getStringValue());
                        }
                    }
                });
            }

            bomInventoryDtoList.add(bomInventoryDto);

            /*--------------- To Expand Entire Section --------------------*/

            /*if (selectedItem.getBomItemType().equals(BomItemType.SECTION) || selectedItem.getBomItemType().equals(BomItemType.COMMONPART)) {
                visitChildren(bomId, bomInventoryDto, bomInventoryDtoList);
            }*/
        });

        return bomInventoryDtoList;
    }

    private List<BomInventoryDto> visitChildren(Integer bomId, BomInventoryDto bomInventoryDto, List<BomInventoryDto> bomInventoryDtoList) {

        List<BomItem> children = bomItemRepository.findByParentOrderByCreatedDateAsc(bomInventoryDto.getItem().getId());

        for (BomItem child : children) {
            child.getChildren().addAll(bomItemRepository.findByParentOrderByCreatedDateAsc(child.getId()));
            BomInventoryDto childInventoryDto = new BomInventoryDto();
            if (child.getBomItemType().equals(BomItemType.PART)) {
                ItemType itemType = itemTypeRepository.findOne(child.getItem().getItemMaster().getItemType().getParentType());

                if (!itemType.getParentNode()) {
                    child.getItem().getItemMaster().setParentType(itemType);
                } else {
                    child.getItem().getItemMaster().setParentType(child.getItem().getItemMaster().getItemType());
                }
            }
            childInventoryDto.setItem(child);
            childInventoryDto.setExpanded(true);
            childInventoryDto.setLevel(bomInventoryDto.getLevel() + 1);

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
                    if (withSectionInventory != null) {
                        if (child.getItem().getItemMaster().getItemType().getHasLots()) {
                            childInventoryDto.setFractionalOnHold(childInventoryDto.getFractionalOnHold() + withSectionInventory.getFractionalQtyBuffered());
                            childInventoryDto.setFractionalStock(childInventoryDto.getFractionalStock() + withSectionInventory.getFractionalQtyOnHand());
                        } else {
                            childInventoryDto.setOnHold(childInventoryDto.getOnHold() + withSectionInventory.getQtyBuffered());
                            childInventoryDto.setStock(childInventoryDto.getStock() + withSectionInventory.getQuantity());
                        }
                    }
                }

                withOutSectionInventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSectionIsNull(bomId, child.getItem().getId(), child.getUniqueCode());
                if (withOutSectionInventory != null) {
                    if (child.getItem().getItemMaster().getItemType().getHasLots()) {
                        childInventoryDto.setFractionalOnHold(childInventoryDto.getFractionalOnHold() + withOutSectionInventory.getFractionalQtyBuffered());
                        childInventoryDto.setFractionalStock(childInventoryDto.getFractionalStock() + withOutSectionInventory.getFractionalQtyOnHand());
                    } else {
                        childInventoryDto.setOnHold(childInventoryDto.getOnHold() + withOutSectionInventory.getQtyBuffered());
                        childInventoryDto.setStock(childInventoryDto.getStock() + withOutSectionInventory.getQuantity());
                    }
                }

                if (bomItemSection != null) {
                    childInventoryDto.getItemInstances().addAll(itemInstanceRepository.getItemInstancesBySectionAndBomAndUniqueCode(child.getItem().getId(), bomItemSection.getTypeRef().getId(), bomId, child.getUniqueCode()));
                    childInventoryDto.getOnHoldInstances().addAll(itemInstanceRepository.getOnHoldInstancesBySectionAndBomAndUniqueCode(child.getItem().getId(), bomItemSection.getTypeRef().getId(), bomId, child.getUniqueCode()));
                }

                childInventoryDto.getItemInstances().addAll(itemInstanceRepository.getItemInstancesByBomAndUniqueCodeAndSectionIsNull(child.getItem().getId(), bomId, child.getUniqueCode()));
                childInventoryDto.getOnHoldInstances().addAll(itemInstanceRepository.getOnHoldInstancesByBomAndUniqueCodeAndSectionIsNull(child.getItem().getId(), bomId, child.getUniqueCode()));

                childInventoryDto.getItemInstances().forEach(itemInstance1 -> {
                    if (itemInstance1.getItem().getItemMaster().getItemType().getHasLots()) {
                        List<LotInstance> lotInstances = lotInstanceRepository.findByInstance(itemInstance1.getId());
                        Double issuedQuantity = 0.0;
                        for (LotInstance lotInstance : lotInstances) {
                            issuedQuantity = issuedQuantity + lotInstance.getLotQty();
                        }
                        itemInstance1.setRemainingQuantity(itemInstance1.getLotSize() - issuedQuantity);

                    }
                });


                if (bomItemSection != null) {
                    childInventoryDto.getReturnInstances().addAll(itemInstanceRepository.getReturnInstancesByItemAndSectionAndUniqueCode(child.getItem().getId(), ItemInstanceStatus.REJECTED, bomItemSection.getTypeRef().getId(), child.getUniqueCode()));
                }

                childInventoryDto.getReturnInstances().addAll(itemInstanceRepository.getReturnInstancesByItemAndUniqueCodeAndSectionIsNull(child.getItem().getId(), ItemInstanceStatus.REJECTED, child.getUniqueCode()));

                /*childInventoryDto.getItemInstances().forEach(itemInstance -> {
                    ObjectTypeAttribute certificateNumberType = objectTypeAttributeRepository.findByNameAndObjectType("Certificate Number", ObjectType.valueOf("ITEMINSTANCE"));

                    if (certificateNumberType != null) {
                        ObjectAttribute certificateNumber = objectAttributeRepository.findByObjectIdAndAttributeDefId(itemInstance.getId(), certificateNumberType.getId());

                        if (certificateNumber != null && certificateNumber.getStringValue() != null) {
                            itemInstance.setCertificateNumber(certificateNumber.getStringValue());
                        }
                    }
                });

                childInventoryDto.getReturnInstances().forEach(itemInstance -> {

                    if (child.getItem().getItemMaster().getItemType().getHasLots()) {
                        childInventoryDto.setFractionalReturned(childInventoryDto.getFractionalReturned() + itemInstance.getLotSize());
                    }

                    ObjectTypeAttribute certificateNumberType = objectTypeAttributeRepository.findByNameAndObjectType("Certificate Number", ObjectType.valueOf("ITEMINSTANCE"));

                    if (certificateNumberType != null) {
                        ObjectAttribute certificateNumber = objectAttributeRepository.findByObjectIdAndAttributeDefId(itemInstance.getId(), certificateNumberType.getId());

                        if (certificateNumber != null && certificateNumber.getStringValue() != null) {
                            itemInstance.setCertificateNumber(certificateNumber.getStringValue());
                        }
                    }
                });*/
            }

            bomInventoryDto.getChildren().add(childInventoryDto);

            bomInventoryDtoList = visitChildren(bomId, childInventoryDto, bomInventoryDtoList);
        }

        return bomInventoryDtoList;
    }

    @Transactional(readOnly = true)
    public List<BomInstanceInventoryDto> getBomInstanceInventory(Integer bomId) {
        List<BomInstanceInventoryDto> inventoryDtoList = new ArrayList<>();
        BomInstance bomInstance = bomInstanceRepository.findOne(bomId);

        if (bomInstance != null) {
            List<BomInstanceItem> bomInstanceItems = bomInstanceItemRepository.findByBomOrderByCreatedDateAsc(bomInstance.getId());
            for (BomInstanceItem bomInstanceItem : bomInstanceItems) {

                bomInstanceItem.getChildren().addAll(bomInstanceItemRepository.findByParentOrderByCreatedDateAsc(bomInstanceItem.getId()));

                BomInstanceInventoryDto inventoryDto = new BomInstanceInventoryDto();
                inventoryDto.setItem(bomInstanceItem);

                if (bomInstanceItem.getBomItemType().equals(BomItemType.PART)) {
                    inventoryDto.setRequested(requestItemRepository.getRequestedByInstanceAndItem(bomInstance.getId(), bomInstanceItem.getId()).size());
                }

                inventoryDtoList.add(inventoryDto);
            }
        }

        return inventoryDtoList;
    }

    @Transactional(readOnly = true)
    public List<BomInstanceInventoryDto> getBomInstanceChildrenInventory(Integer bomId, Integer bomItemId) {

        BomInstance bomInstance = bomInstanceRepository.findOne(bomId);
        List<BomInstanceInventoryDto> inventoryDtoList = new ArrayList<>();

        BomInstanceItem selectedItem = bomInstanceItemRepository.findOne(bomItemId);

        List<BomInstanceItem> bomInstanceItems = bomInstanceItemRepository.findByParentOrderByCreatedDateAsc(bomItemId);

        bomInstanceItems.forEach(bomInstanceItem -> {
            bomInstanceItem.getChildren().addAll(bomInstanceItemRepository.findByParentOrderByCreatedDateAsc(bomInstanceItem.getId()));
            BomInstanceInventoryDto inventoryDto = new BomInstanceInventoryDto();
            if (bomInstanceItem.getBomItemType().equals(BomItemType.PART)) {
                ItemType itemType = itemTypeRepository.findOne(bomInstanceItem.getItem().getItemMaster().getItemType().getParentType());

                if (!itemType.getParentNode()) {
                    bomInstanceItem.getItem().getItemMaster().setParentType(itemType);
                } else {
                    bomInstanceItem.getItem().getItemMaster().setParentType(bomInstanceItem.getItem().getItemMaster().getItemType());
                }
            }
            inventoryDto.setItem(bomInstanceItem);
            if (selectedItem.getBomItemType().equals(BomItemType.SECTION) || selectedItem.getBomItemType().equals(BomItemType.COMMONPART)) {
                inventoryDto.setLevel(1);
                inventoryDto.setExpanded(true);
            }

            if (bomInstanceItem.getBomItemType().equals(BomItemType.PART)) {

                List<RequestItem> requestItems = requestItemRepository.getRequestedByInstanceAndItem(bomInstance.getId(), bomInstanceItem.getId());
                Integer qty = 0;
                Double fractionalQty = 0.0;

                if (requestItems.size() > 0) {
                    for (RequestItem requestItem : requestItems) {
                        if (requestItem.getItem().getItem().getItemMaster().getItemType().getHasLots()) {
                            fractionalQty = fractionalQty + requestItem.getFractionalQuantity();
                        } else {
                            qty = qty + requestItem.getQuantity();
                        }
                    }
                }
                inventoryDto.setRequested(qty);
                inventoryDto.setFractionalRequested(fractionalQty);

                List<Issue> issueList = issueRepository.findByBomInstance(bomInstance.getId());

                for (Issue issue : issueList) {

                    if (issue.getStatus().equals(IssueStatus.RECEIVED) || issue.getStatus().equals(IssueStatus.PARTIALLY_RECEIVED)) {
                        if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                            List<IssueItem> issueItems = issueItemRepository.getReceivedItemsByIssueAndBomItemInstance(issue.getId(), bomInstanceItem.getId());

                            issueItems.forEach(issueItem -> {
                                if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                                    inventoryDto.getIssuedLotInstances().addAll(lotInstanceRepository.findByIssueItem(issueItem.getId()));
                                } else {
                                    inventoryDto.getIssuedInstances().add(itemInstanceRepository.findOne(issueItem.getBomItemInstance().getItemInstance()));
                                }
                            });

                            inventoryDto.setFractionalIssued(inventoryDto.getFractionalIssued() + issueItems.size());
                        } else {
                            List<IssueItem> issueItems = issueItemRepository.getReceivedItemsByIssueAndBomItemInstance(issue.getId(), bomInstanceItem.getId());

                            issueItems.forEach(issueItem -> {
                                inventoryDto.getIssuedInstances().add(itemInstanceRepository.findOne(issueItem.getBomItemInstance().getItemInstance()));
                            });

                            inventoryDto.setIssued(inventoryDto.getIssued() + issueItems.size());
                        }

                        inventoryDto.getIssuedLotInstances().forEach(lotInstance -> {
                            lotInstance.setItemInstance(itemInstanceRepository.findOne(lotInstance.getInstance()));
                            ObjectTypeAttribute certificateNumberType = objectTypeAttributeRepository.findByNameAndObjectType("Certificate Number", ObjectType.valueOf("ITEMINSTANCE"));

                            if (certificateNumberType != null) {
                                ObjectAttribute certificateNumber = objectAttributeRepository.findByObjectIdAndAttributeDefId(lotInstance.getInstance(), certificateNumberType.getId());

                                if (certificateNumber != null && certificateNumber.getStringValue() != null) {
                                    lotInstance.setCertificateNumber(certificateNumber.getStringValue());
                                }
                            }
                        });

                        inventoryDto.getIssuedInstances().forEach(itemInstance -> {
                            ObjectTypeAttribute certificateNumberType = objectTypeAttributeRepository.findByNameAndObjectType("Certificate Number", ObjectType.valueOf("ITEMINSTANCE"));

                            if (certificateNumberType != null) {
                                ObjectAttribute certificateNumber = objectAttributeRepository.findByObjectIdAndAttributeDefId(itemInstance.getId(), certificateNumberType.getId());

                                if (certificateNumber != null && certificateNumber.getStringValue() != null) {
                                    itemInstance.setCertificateNumber(certificateNumber.getStringValue());
                                }
                            }
                        });
                    }

                }

                ItemAllocation itemAllocation = itemAllocationRepository.findByBomInstanceAndBomInstanceItem(bomId, bomInstanceItem.getId());
                if (itemAllocation != null) {
                    inventoryDto.setAllocatedQty(itemAllocation.getAllocateQty());
                }

            }

            inventoryDtoList.add(inventoryDto);

            /*if (selectedItem.getBomItemType().equals(BomItemType.SECTION) || selectedItem.getBomItemType().equals(BomItemType.COMMONPART)) {
                visitInstanceChildren(bomInstance, inventoryDto, inventoryDtoList);
            }*/
        });

        return inventoryDtoList;
    }

    private List<BomInstanceInventoryDto> visitInstanceChildren(BomInstance bomInstance, BomInstanceInventoryDto instanceInventoryDto, List<BomInstanceInventoryDto> instanceInventoryDtoList) {

        List<BomInstanceItem> bomInstanceItems = bomInstanceItemRepository.findByParentOrderByCreatedDateAsc(instanceInventoryDto.getItem().getId());

        for (BomInstanceItem bomInstanceItem : bomInstanceItems) {
            bomInstanceItem.getChildren().addAll(bomInstanceItemRepository.findByParentOrderByCreatedDateAsc(bomInstanceItem.getId()));
            BomInstanceInventoryDto inventoryDto = new BomInstanceInventoryDto();
            if (bomInstanceItem.getBomItemType().equals(BomItemType.PART)) {
                ItemType itemType = itemTypeRepository.findOne(bomInstanceItem.getItem().getItemMaster().getItemType().getParentType());

                if (!itemType.getParentNode()) {
                    bomInstanceItem.getItem().getItemMaster().setParentType(itemType);
                } else {
                    bomInstanceItem.getItem().getItemMaster().setParentType(bomInstanceItem.getItem().getItemMaster().getItemType());
                }
            }
            inventoryDto.setItem(bomInstanceItem);
            inventoryDto.setLevel(instanceInventoryDto.getLevel() + 1);
            inventoryDto.setExpanded(true);

            if (bomInstanceItem.getBomItemType().equals(BomItemType.PART)) {

                List<RequestItem> requestItems = requestItemRepository.getRequestedByInstanceAndItem(bomInstance.getId(), bomInstanceItem.getId());
                Integer qty = 0;
                Double fractionalQty = 0.0;

                if (requestItems.size() > 0) {
                    for (RequestItem requestItem : requestItems) {
                        if (requestItem.getItem().getItem().getItemMaster().getItemType().getHasLots()) {
                            fractionalQty = fractionalQty + requestItem.getFractionalQuantity();
                        } else {
                            qty = qty + requestItem.getQuantity();
                        }
                    }
                }
                inventoryDto.setRequested(qty);
                inventoryDto.setFractionalRequested(fractionalQty);

                List<Issue> issueList = issueRepository.findByBomInstance(bomInstance.getId());

                for (Issue issue : issueList) {

                    if (issue.getStatus().equals(IssueStatus.RECEIVED) || issue.getStatus().equals(IssueStatus.PARTIALLY_RECEIVED)) {
                        if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                            List<IssueItem> issueItems = issueItemRepository.getReceivedItemsByIssueAndBomItemInstance(issue.getId(), bomInstanceItem.getId());

                            issueItems.forEach(issueItem -> {
                                if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                                    inventoryDto.getIssuedLotInstances().addAll(lotInstanceRepository.findByIssueItem(issueItem.getId()));
                                } else {
                                    inventoryDto.getIssuedInstances().add(itemInstanceRepository.findOne(issueItem.getBomItemInstance().getItemInstance()));
                                }
                            });

                            inventoryDto.setFractionalIssued(inventoryDto.getFractionalIssued() + issueItems.size());
                        } else {
                            List<IssueItem> issueItems = issueItemRepository.getReceivedItemsByIssueAndBomItemInstance(issue.getId(), bomInstanceItem.getId());

                            issueItems.forEach(issueItem -> {
                                inventoryDto.getIssuedInstances().add(itemInstanceRepository.findOne(issueItem.getBomItemInstance().getItemInstance()));
                            });

                            inventoryDto.setIssued(inventoryDto.getIssued() + issueItems.size());
                        }

                        inventoryDto.getIssuedLotInstances().forEach(lotInstance -> {
                            lotInstance.setItemInstance(itemInstanceRepository.findOne(lotInstance.getInstance()));
                            ObjectTypeAttribute certificateNumberType = objectTypeAttributeRepository.findByNameAndObjectType("Certificate Number", ObjectType.valueOf("ITEMINSTANCE"));

                            if (certificateNumberType != null) {
                                ObjectAttribute certificateNumber = objectAttributeRepository.findByObjectIdAndAttributeDefId(lotInstance.getInstance(), certificateNumberType.getId());

                                if (certificateNumber != null && certificateNumber.getStringValue() != null) {
                                    lotInstance.setCertificateNumber(certificateNumber.getStringValue());
                                }
                            }
                        });

                        inventoryDto.getIssuedInstances().forEach(itemInstance -> {
                            ObjectTypeAttribute certificateNumberType = objectTypeAttributeRepository.findByNameAndObjectType("Certificate Number", ObjectType.valueOf("ITEMINSTANCE"));

                            if (certificateNumberType != null) {
                                ObjectAttribute certificateNumber = objectAttributeRepository.findByObjectIdAndAttributeDefId(itemInstance.getId(), certificateNumberType.getId());

                                if (certificateNumber != null && certificateNumber.getStringValue() != null) {
                                    itemInstance.setCertificateNumber(certificateNumber.getStringValue());
                                }
                            }
                        });
                    }

                }

                ItemAllocation itemAllocation = itemAllocationRepository.findByBomInstanceAndBomInstanceItem(bomInstance.getId(), bomInstanceItem.getId());
                if (itemAllocation != null) {
                    inventoryDto.setAllocatedQty(itemAllocation.getAllocateQty());
                }

            }

            instanceInventoryDto.getChildren().add(inventoryDto);

            instanceInventoryDtoList = visitInstanceChildren(bomInstance, inventoryDto, instanceInventoryDtoList);
        }

        return instanceInventoryDtoList;
    }

    @Transactional(readOnly = true)
    public List<BomInventoryDto> getInventoryReportByBom(Integer bomId) {

        List<BomInventoryDto> bomInventoryDtoList = new ArrayList<>();

        List<BomItem> sections = bomItemRepository.findByBomOrderByCreatedDateAsc(bomId);

        for (BomItem section : sections) {
            section.setLevel(0);
            BomInventoryDto bomInventoryDto = new BomInventoryDto();
            bomInventoryDto.setItem(section);

            bomInventoryDtoList.add(bomInventoryDto);

            bomInventoryDtoList = getBomItemChildrenReport(bomId, section, bomInventoryDtoList);
        }

        return bomInventoryDtoList;
    }

    @Transactional(readOnly = true)
    public List<BomInventoryDto> getInventoryReportBySection(Integer bomId, Integer sectionId) {

        List<BomInventoryDto> bomInventoryDtoList = new ArrayList<>();
        BomItem section = bomItemRepository.findOne(sectionId);

        section.setLevel(0);
        BomInventoryDto bomInventoryDto = new BomInventoryDto();
        bomInventoryDto.setItem(section);

        bomInventoryDtoList.add(bomInventoryDto);

        bomInventoryDtoList = getBomItemChildrenReport(bomId, section, bomInventoryDtoList);
        return bomInventoryDtoList;
    }

    private List<BomInventoryDto> getBomItemChildrenReport(Integer bomId, BomItem section, List<BomInventoryDto> bomInventoryDtos) {

        List<BomItem> bomItems = bomItemRepository.findByParentOrderByCreatedDateAsc(section.getId());

        for (BomItem bomItem : bomItems) {
            BomInventoryDto bomInventoryDto = new BomInventoryDto();
            if (bomItem.getBomItemType().equals(BomItemType.PART)) {
                ItemType itemType = itemTypeRepository.findOne(bomItem.getItem().getItemMaster().getItemType().getParentType());

                if (!itemType.getParentNode()) {
                    bomItem.getItem().getItemMaster().setParentType(itemType);
                } else {
                    bomItem.getItem().getItemMaster().setParentType(bomItem.getItem().getItemMaster().getItemType());
                }
            }
            bomItem.setLevel(section.getLevel() + 1);
            bomInventoryDto.setItem(bomItem);

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
                    if (withSectionInventory != null) {
                        if (bomItem.getItem().getItemMaster().getItemType().getHasLots()) {
                            bomInventoryDto.setFractionalOnHold(bomInventoryDto.getFractionalOnHold() + withSectionInventory.getFractionalQtyBuffered());
                            bomInventoryDto.setFractionalStock(bomInventoryDto.getFractionalStock() + withSectionInventory.getFractionalQtyOnHand());
                        } else {
                            bomInventoryDto.setOnHold(bomInventoryDto.getOnHold() + withSectionInventory.getQtyBuffered());
                            bomInventoryDto.setStock(bomInventoryDto.getStock() + withSectionInventory.getQuantity());
                        }
                    }
                }

                withOutSectionInventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSectionIsNull(bomId, bomItem.getItem().getId(), bomItem.getUniqueCode());
                if (withOutSectionInventory != null) {
                    if (bomItem.getItem().getItemMaster().getItemType().getHasLots()) {
                        bomInventoryDto.setFractionalOnHold(bomInventoryDto.getFractionalOnHold() + withOutSectionInventory.getFractionalQtyBuffered());
                        bomInventoryDto.setFractionalStock(bomInventoryDto.getFractionalStock() + withOutSectionInventory.getFractionalQtyOnHand());
                    } else {
                        bomInventoryDto.setOnHold(bomInventoryDto.getOnHold() + withOutSectionInventory.getQtyBuffered());
                        bomInventoryDto.setStock(bomInventoryDto.getStock() + withOutSectionInventory.getQuantity());
                    }
                }

                if (bomItemSection != null) {
                    bomInventoryDto.getItemInstances().addAll(itemInstanceRepository.getItemInstancesBySectionAndBom(bomItem.getItem().getId(), bomItemSection.getTypeRef().getId(), bomId));
                }

                bomInventoryDto.getItemInstances().addAll(itemInstanceRepository.getItemInstancesByBomAndSectionIsNull(bomItem.getItem().getId(), bomId));

                bomInventoryDto.setReturnInstances(itemInstanceRepository.getReturnInstancesByItem(bomItem.getItem().getId(), ItemInstanceStatus.REJECTED));
            }

            bomInventoryDtos.add(bomInventoryDto);

            bomInventoryDtos = getBomItemChildrenReport(bomId, bomItem, bomInventoryDtos);
        }

        return bomInventoryDtos;
    }

    @Transactional(readOnly = true)
    public List<BomInstanceInventoryDto> getInventoryReportByInstance(Integer bomId) {

        List<BomInstanceInventoryDto> bomInventoryDtoList = new ArrayList<>();

        List<BomInstanceItem> sections = bomInstanceItemRepository.findByBomOrderByCreatedDateAsc(bomId);

        for (BomInstanceItem section : sections) {
            section.setLevel(0);
            BomInstanceInventoryDto bomInventoryDto = new BomInstanceInventoryDto();
            bomInventoryDto.setItem(section);

            bomInventoryDtoList.add(bomInventoryDto);

            bomInventoryDtoList = getBomInstanceChildrenReport(bomId, section, bomInventoryDtoList);
        }

        return bomInventoryDtoList;
    }

    @Transactional(readOnly = true)
    public List<BomInstanceInventoryDto> getInventoryReportByInstanceSection(Integer bomId, Integer sectionId) {

        List<BomInstanceInventoryDto> bomInventoryDtoList = new ArrayList<>();
        BomInstanceItem section = bomInstanceItemRepository.findOne(sectionId);

        section.setLevel(0);
        BomInstanceInventoryDto bomInventoryDto = new BomInstanceInventoryDto();
        bomInventoryDto.setItem(section);

        bomInventoryDtoList.add(bomInventoryDto);

        bomInventoryDtoList = getBomInstanceChildrenReport(bomId, section, bomInventoryDtoList);
        return bomInventoryDtoList;
    }

    @Transactional(readOnly = true)
    public List<BomInstanceInventoryDto> getBomInstanceChildrenReport(Integer instanceId, BomInstanceItem section, List<BomInstanceInventoryDto> bomInstanceInventoryDtos) {

        List<BomInstanceItem> bomInstanceItems = bomInstanceItemRepository.findByParentOrderByCreatedDateAsc(section.getId());

        for (BomInstanceItem bomInstanceItem : bomInstanceItems) {
            bomInstanceItem.getChildren().addAll(bomInstanceItemRepository.findByParentOrderByCreatedDateAsc(bomInstanceItem.getId()));
            BomInstanceInventoryDto inventoryDto = new BomInstanceInventoryDto();
            if (bomInstanceItem.getBomItemType().equals(BomItemType.PART)) {
                ItemType itemType = itemTypeRepository.findOne(bomInstanceItem.getItem().getItemMaster().getItemType().getParentType());

                if (!itemType.getParentNode()) {
                    bomInstanceItem.getItem().getItemMaster().setParentType(itemType);
                } else {
                    bomInstanceItem.getItem().getItemMaster().setParentType(bomInstanceItem.getItem().getItemMaster().getItemType());
                }
            }

            inventoryDto.setItem(bomInstanceItem);
            inventoryDto.getItem().setLevel(section.getLevel() + 1);
            if (bomInstanceItem.getBomItemType().equals(BomItemType.PART)) {

                List<RequestItem> requestItems = requestItemRepository.getRequestedByInstanceAndItem(instanceId, bomInstanceItem.getId());
                Integer qty = 0;
                Double fractionalQty = 0.0;

                if (requestItems.size() > 0) {
                    for (RequestItem requestItem : requestItems) {
                        if (requestItem.getItem().getItem().getItemMaster().getItemType().getHasLots()) {
                            fractionalQty = fractionalQty + requestItem.getFractionalQuantity();
                        } else {
                            qty = qty + requestItem.getQuantity();
                        }
                    }
                }
                inventoryDto.setRequested(qty);
                inventoryDto.setFractionalRequested(fractionalQty);

                List<Issue> issueList = issueRepository.findByBomInstance(instanceId);

                for (Issue issue : issueList) {

                    if (issue.getStatus().equals(IssueStatus.RECEIVED) || issue.getStatus().equals(IssueStatus.PARTIALLY_RECEIVED)) {
                        if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                            List<IssueItem> issueItems = issueItemRepository.getReceivedItemsByIssueAndBomItemInstance(issue.getId(), bomInstanceItem.getId());

                            issueItems.forEach(issueItem -> {
                                if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                                    inventoryDto.getIssuedLotInstances().addAll(lotInstanceRepository.findByIssueItem(issueItem.getId()));
                                } else {
                                    inventoryDto.getIssuedInstances().add(itemInstanceRepository.findOne(issueItem.getBomItemInstance().getItemInstance()));
                                }
                            });

                            inventoryDto.setFractionalIssued(inventoryDto.getFractionalIssued() + issueItems.size());
                        } else {
                            List<IssueItem> issueItems = issueItemRepository.getReceivedItemsByIssueAndBomItemInstance(issue.getId(), bomInstanceItem.getId());

                            issueItems.forEach(issueItem -> {
                                inventoryDto.getIssuedInstances().add(itemInstanceRepository.findOne(issueItem.getBomItemInstance().getItemInstance()));
                            });

                            inventoryDto.setIssued(inventoryDto.getIssued() + issueItems.size());
                        }
                    }
                }

                ItemAllocation itemAllocation = itemAllocationRepository.findByBomInstanceAndBomInstanceItem(instanceId, bomInstanceItem.getId());
                if (itemAllocation != null) {
                    inventoryDto.setAllocatedQty(itemAllocation.getAllocateQty());
                }

            }

            bomInstanceInventoryDtos.add(inventoryDto);

            bomInstanceInventoryDtos = getBomInstanceChildrenReport(instanceId, bomInstanceItem, bomInstanceInventoryDtos);
        }

        return bomInstanceInventoryDtos;
    }
}
