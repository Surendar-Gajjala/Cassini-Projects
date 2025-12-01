package com.cassinisys.drdo.service.bom;

import com.cassinisys.drdo.filtering.BomInstanceItemSearchPredicateBuilder;
import com.cassinisys.drdo.filtering.BomSearchCriteria;
import com.cassinisys.drdo.filtering.BomSearchPredicateBuilder;
import com.cassinisys.drdo.filtering.WorkCenterPredicateBuilder;
import com.cassinisys.drdo.model.DRDOObjectType;
import com.cassinisys.drdo.model.bom.*;
import com.cassinisys.drdo.model.dto.*;
import com.cassinisys.drdo.model.inventory.Inventory;
import com.cassinisys.drdo.model.inventory.ItemAllocation;
import com.cassinisys.drdo.model.inventory.Storage;
import com.cassinisys.drdo.model.inventory.StorageItem;
import com.cassinisys.drdo.model.transactions.*;
import com.cassinisys.drdo.repo.bom.*;
import com.cassinisys.drdo.repo.inventory.InventoryRepository;
import com.cassinisys.drdo.repo.inventory.ItemAllocationRepository;
import com.cassinisys.drdo.repo.inventory.StorageItemRepository;
import com.cassinisys.drdo.repo.inventory.StorageRepository;
import com.cassinisys.drdo.repo.transactions.*;
import com.cassinisys.drdo.service.DRDOUpdatesService;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.converter.ImportConverter;
import com.mysema.query.types.Predicate;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by subramanyam reddy on 08-10-2018.
 */
@Service
public class BomService implements CrudService<Bom, Integer> {

    @Autowired
    private BomRepository bomRepository;

    @Autowired
    private BomInstanceRepository bomInstanceRepository;

    @Autowired
    private BomItemRepository bomItemRepository;

    @Autowired
    private ItemRevisionRepository itemRevisionRepository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemInstanceRepository itemInstanceRepository;

    @Autowired
    private BomInstanceItemRepository bomInstanceItemRepository;

    @Autowired
    private BomSearchPredicateBuilder bomSearchPredicateBuilder;

    @Autowired
    private BomInstanceItemSearchPredicateBuilder bomInstanceItemSearchPredicateBuilder;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private RequestItemRepository requestItemRepository;

    @Autowired
    private ItemTypeService itemTypeService;

    @Autowired
    private ItemTypeRepository itemTypeRepository;

    @Autowired
    private BomItemInstanceRepository bomItemInstanceRepository;

    @Autowired
    private StorageItemRepository storageItemRepository;

    @Autowired
    private ItemAllocationRepository itemAllocationRepository;

    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;

    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private BomGroupRepository bomGroupRepository;

    @Autowired
    private LotInstanceRepository lotInstanceRepository;

    @Autowired
    private IssueItemRepository issueItemRepository;

    @Autowired
    private AutoNumberService autoNumberService;

    @Autowired
    private WorkCenterPredicateBuilder workCenterPredicateBuilder;

    @Autowired
    private ItemTypeSpecsRepository itemTypeSpecsRepository;

    @Autowired
    private InwardRepository inwardRepository;

    @Autowired
    private InwardItemRepository inwardItemRepository;

    @Autowired
    private InwardItemInstanceRepository inwardItemInstanceRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private DRDOUpdatesService drdoUpdatesService;

    @Autowired
    private SessionWrapper sessionWrapper;

    @Autowired
    private InventoryRepository inventoryRepository;

    public static boolean isRowEmpty(Row row) {
        int i = 0;
        DataFormatter df = new DataFormatter();
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && df.formatCellValue(cell).equals("")) {
                i++;
            }
        }

        if (i == row.getLastCellNum()) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = false)
    public Bom create(Bom bom) {
        return bomRepository.save(bom);
    }

    @Override
    @Transactional(readOnly = false)
    public Bom update(Bom bom) {
        return bomRepository.save(bom);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Integer bomId) {
        bomRepository.delete(bomId);
    }

    @Override
    @Transactional(readOnly = true)
    public Bom get(Integer bomId) {
        return bomRepository.findOne(bomId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bom> getAll() {
        ItemType itemType = itemTypeRepository.findByName("System");
        List<ItemType> itemTypes = itemTypeService.getHierarchicalChildren(itemType);
        List<Bom> boms = bomRepository.findSystemTypeBoms(itemTypes);
        return boms;
    }

    @Transactional(readOnly = true)
    public List<Bom> getBomTree() {
        ItemType itemType = itemTypeRepository.findByName("System");
        List<ItemType> itemTypes = itemTypeService.getHierarchicalChildren(itemType);
        List<Bom> boms = bomRepository.findSystemTypeBoms(itemTypes);
        for (Bom bom : boms) {
            List<BomInstance> bomInstances = bomInstanceRepository.findByBomOrderByCreatedDateAsc(bom.getId());
            bom.setChildren(bomInstances);
        }

        return boms;
    }

    @Transactional(readOnly = true)
    public List<BomItem> getBomStructure(Integer bomId) {

        List<BomItem> bomItems = bomItemRepository.getChildrenByBom(bomId);

        for (BomItem bomItem : bomItems) {
            if (bomItem.getBomItemType().equals(BomItemType.PART)) {
                ItemType itemType = itemTypeRepository.findOne(bomItem.getItem().getItemMaster().getItemType().getParentType());

                if (!itemType.getParentNode()) {
                    bomItem.getItem().getItemMaster().setParentType(itemType);
                } else {
                    bomItem.getItem().getItemMaster().setParentType(bomItem.getItem().getItemMaster().getItemType());
                }
            }
            bomItem = visitBomChildren(bomItem);
        }

        return bomItems;
    }

    @Transactional(readOnly = false)
    public void updateUniqueCodes() {
        List<BomItem> bomItems = bomItemRepository.getUniqueCodeIsNullAndEmpty();

        bomItems.forEach(bomItem -> {
            String specName = "";
            String spec = "0000";
            BomItem parent = bomItemRepository.findOne(bomItem.getParent());

            if (parent != null && parent.getBomItemType().equals(BomItemType.UNIT)) {
                BomItem subSystem = bomItemRepository.findOne(parent.getParent());

                if (subSystem != null && subSystem.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                    bomItem.setUniqueCode(subSystem.getTypeRef().getCode() + "" + parent.getTypeRef().getCode() + bomItem.getItem().getItemMaster().getItemCode());
                }
            } else if (parent != null && parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                bomItem.setUniqueCode(parent.getTypeRef().getCode() + "00" + bomItem.getItem().getItemMaster().getItemCode());
            } else if (parent != null && parent.getBomItemType().equals(BomItemType.COMMONPART)) {
                bomItem.setUniqueCode("000" + bomItem.getItem().getItemMaster().getItemCode());
            }

            if (bomItem.getItem().getItemMaster().getPartSpec() != null) {
                if (bomItem.getItem().getItemMaster().getPartSpec().getSpecName().length() > 4) {
                    specName = bomItem.getItem().getItemMaster().getPartSpec().getSpecName().substring(bomItem.getItem().getItemMaster().getPartSpec().getSpecName().length() - 4);
                } else {
                    specName = bomItem.getItem().getItemMaster().getPartSpec().getSpecName();
                }
                spec = spec.substring(specName.length()) + specName;
                bomItem.setUniqueCode(bomItem.getUniqueCode() + "" + spec);
            } else {
                bomItem.setUniqueCode(bomItem.getUniqueCode() + "0000");
            }

            bomItem = bomItemRepository.save(bomItem);
        });

        List<InwardItem> inwardItems = inwardItemRepository.getInstancesNotCreatedItems();

        if (inwardItems.size() > 0) {
            for (InwardItem inwardItem : inwardItems) {
                inwardItemRepository.delete(inwardItem.getId());
            }
        }

        inwardItems = inwardItemRepository.findAll();

        inwardItems.forEach(inwardItem -> {
            List<InwardItemInstance> inwardItemInstances = inwardItemInstanceRepository.findByInwardItem(inwardItem.getId());

            if (inwardItemInstances.size() == 0) {
                Inward inward = inwardRepository.findOne(inwardItem.getInward());
                if (inwardItem.getSection() != null) {
                    Inventory inventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSection(inward.getBom().getId(), inwardItem.getBomItem().getItem().getId(), inwardItem.getBomItem().getUniqueCode(), inwardItem.getSection().getId());
                    if (inventory != null) {
                        if (inwardItem.getBomItem().getItem().getItemMaster().getItemType().getHasLots()) {
                            inventory.setFractionalQtyOnHand(inventory.getFractionalQtyOnHand() - inwardItem.getFractionalQuantity());
                            inventory = inventoryRepository.save(inventory);
                        } else {
                            inventory.setQuantity(inventory.getQuantity() - inwardItem.getQuantity());
                            inventory = inventoryRepository.save(inventory);
                        }
                    }
                } else {
                    Inventory inventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSectionIsNull(inward.getId(), inwardItem.getBomItem().getItem().getId(), inwardItem.getBomItem().getUniqueCode());
                    if (inventory != null) {
                        if (inwardItem.getBomItem().getItem().getItemMaster().getItemType().getHasLots()) {
                            inventory.setFractionalQtyOnHand(inventory.getFractionalQtyOnHand() - inwardItem.getFractionalQuantity());
                            inventory = inventoryRepository.save(inventory);
                        } else {
                            inventory.setQuantity(inventory.getQuantity() - inwardItem.getQuantity());
                            inventory = inventoryRepository.save(inventory);
                        }
                    }
                }

                inwardItemRepository.delete(inwardItem.getId());
            }
        });

        /*List<ItemInstance> itemInstances = itemInstanceRepository.getIssuedInstances();

        itemInstances.forEach(itemInstance -> {
            if (itemInstance.getItem().getItemMaster().getItemType().getHasLots()) {
                List<LotInstance> lotInstances = lotInstanceRepository.findByInstance(itemInstance.getId());
                Integer count = 1;
                for (LotInstance lotInstance : lotInstances) {
                    if (lotInstance.getSequence() == null) {
                        lotInstance.setSequence(count);
                        lotInstance = lotInstanceRepository.save(lotInstance);
                        count++;
                    }
                }
            }
        });*/

        List<Request> rejectedRequests = requestRepository.getRequestsByStatus(RequestStatus.REJECTED);
        for (Request rejectedRequest : rejectedRequests) {
            requestRepository.delete(rejectedRequest.getId());
        }

        List<Request> requestList = requestRepository.getRequestsWithoutByStatus(RequestStatus.REQUESTED);

        for (Request request : requestList) {
            if (!request.getIssued()) {
                request.setStatus(RequestStatus.REQUESTED);
                request = requestRepository.save(request);

                List<RequestItem> requestItems = requestItemRepository.findByRequest(request);

                for (RequestItem requestItem : requestItems) {
                    requestItem.setAccepted(true);
                    requestItem.setApproved(true);
                    requestItem.setStatus(RequestItemStatus.APPROVED);

                    requestItem = requestItemRepository.save(requestItem);
                }
            }
        }

        /*List<Request> requestList = requestRepository.getRequestsByStatus(RequestStatus.BDL_MANAGER);
        requestList.addAll(requestRepository.getRequestsByStatus(RequestStatus.VERSITY_MANAGER));

        requestList.forEach(request -> {
            List<RequestItem> requestItems = requestItemRepository.findByRequest(request);
            if (requestItems.size() == 0) {
                requestRepository.delete(request.getId());
            }
        });

        List<RequestItem> requestItems = requestItemRepository.getRequestItemsByStatus(RequestItemStatus.PENDING);

        if (requestItems.size() > 0) {
            requestItems.forEach(requestItem -> {
                if (requestItem.getItem().getItem().getItemMaster().getItemType().getHasLots() && (requestItem.getFractionalQuantity().equals(0.0) || requestItem.getFractionalQuantity() == null)) {
                    requestItem.setFractionalQuantity(requestItem.getQuantity().doubleValue());

                    requestItem = requestItemRepository.save(requestItem);
                }

                if (!requestItem.getItem().getItem().getItemMaster().getItemType().getHasLots() && (requestItem.getQuantity().equals(0))) {
                    requestItem.setQuantity(requestItem.getFractionalQuantity().intValue());

                    requestItem = requestItemRepository.save(requestItem);
                }
            });
        }*/

        /*List<InwardItemInstance> inwardItemInstances = inwardItemInstanceRepository.findAll();
        List<LotInstance> lotInstances = lotInstanceRepository.findAll();

        for (InwardItemInstance inwardItemInstance : inwardItemInstances) {

            if (!inwardItemInstance.getItem().getItem().getItemMaster().getItemType().getHasLots() && inwardItemInstance.getItem().getStatus().equals(ItemInstanceStatus.ISSUE)) {
                ItemInstance itemInstance = itemInstanceRepository.findOne(inwardItemInstance.getItem().getId());
                BomItemInstance bomItemInstance = bomItemInstanceRepository.findByItemInstance(inwardItemInstance.getItem().getId());
                BomInstanceItem bomInstanceItem = bomInstanceItemRepository.findOne(bomItemInstance.getBomInstanceItem());

                BomInstance bomInstance = null;
                BomInstanceItem bomItemSection = null;
                BomInstanceItem bomItemSubSystem = null;
                BomInstanceItem bomItemUnit = null;

                if (bomInstanceItem != null) {
                    BomInstanceItem parent = bomInstanceItemRepository.findOne(bomInstanceItem.getParent());
                    if (parent.getBomItemType().equals(BomItemType.UNIT)) {
                        bomItemUnit = parent;
                        BomInstanceItem parent1 = bomInstanceItemRepository.findOne(parent.getParent());
                        if (parent1.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                            BomInstanceItem parent2 = bomInstanceItemRepository.findOne(parent1.getParent());
                            bomItemSubSystem = parent1;
                            if (parent2.getBomItemType().equals(BomItemType.SECTION)) {
                                bomItemSection = parent2;

                                bomInstance = bomInstanceRepository.findOne(bomItemSection.getBom());
                            }
                        } else {
                            bomItemSection = parent1;

                            bomInstance = bomInstanceRepository.findOne(bomItemSection.getBom());
                        }
                    } else if (parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                        bomItemSubSystem = parent;
                        BomInstanceItem parent1 = bomInstanceItemRepository.findOne(parent.getParent());
                        bomItemSection = parent1;

                        bomInstance = bomInstanceRepository.findOne(bomItemSection.getBom());
                    } else if (parent.getBomItemType().equals(BomItemType.COMMONPART)) {
                        bomItemSection = parent;

                        bomInstance = bomInstanceRepository.findOne(bomItemSection.getBom());
                    }
                }
                String missile = "00";
                String system = itemInstance.getUpnNumber().substring(0, 2);
                if (bomInstance != null) {
                    missile = bomInstance.getItem().getInstanceName();
                }

                String section = "0";
                String subSystem = "0";
                String unit = "00";
                String part = "00";

                if (bomItemSection != null) {
                    section = bomItemSection.getTypeRef().getCode();
                }

                if (bomItemSubSystem != null) {
                    subSystem = bomItemSubSystem.getTypeRef().getCode();
                }

                if (bomItemUnit != null) {
                    unit = bomItemUnit.getTypeRef().getCode();
                }

                if (bomInstanceItem != null) {
                    part = bomInstanceItem.getItem().getItemMaster().getItemCode();
                }


                itemInstance.setUpnNumber(system + "" + missile + "" + section + "" + subSystem + "" + unit + "" + part);

                itemInstance = itemInstanceRepository.save(itemInstance);

            }
        }*/
    }

    private BomItem visitBomChildren(BomItem bomItem) {

        List<BomItem> bomItemList = bomItemRepository.getChildrenByBomItem(bomItem.getId());

        bomItem.setChildren(bomItemList);

        for (BomItem child : bomItemList) {
            if (child.getBomItemType().equals(BomItemType.PART)) {

                ItemType itemType = itemTypeRepository.findOne(child.getItem().getItemMaster().getItemType().getParentType());

                if (!itemType.getParentNode()) {
                    child.getItem().getItemMaster().setParentType(itemType);
                } else {
                    child.getItem().getItemMaster().setParentType(child.getItem().getItemMaster().getItemType());
                }
            }

            child = visitBomChildren(child);
        }

        return bomItem;
    }

    @Transactional(readOnly = true)
    public StoragePartsDto getBomPartsByBomType(Integer bomItemId, Integer storageId) {
        List<StorageItem> storageItems = new ArrayList<>();
        if (storageId != null && !storageId.equals("null")) {
            storageItems = storageItemRepository.findByStorage(storageId);
        }

        List<BomItem> parts = new ArrayList<>();

        List<BomItem> bomItemList = bomItemRepository.findByParentOrderByCreatedDateAsc(bomItemId);

        for (BomItem child : bomItemList) {
            if (child.getBomItemType().equals(BomItemType.PART)) {
                if (child.getItem().getItemMaster().getItemType().getParentType() != null) {
                    child.getItem().getItemMaster().setParentType(itemTypeRepository.findOne(child.getItem().getItemMaster().getItemType().getParentType()));
                } else {
                    child.getItem().getItemMaster().setParentType(itemTypeRepository.findOne(child.getItem().getItemMaster().getItemType().getId()));
                }
                parts.add(child);
            }
            parts = visitBomPartChildren(child, parts);
        }

        StoragePartsDto storagePartsDto = new StoragePartsDto();

        Bom bom = null;

        BomItem selectedBomItem = bomItemRepository.findOne(bomItemId);

        if (selectedBomItem.getBomItemType().equals(BomItemType.SECTION) || selectedBomItem.getBomItemType().equals(BomItemType.COMMONPART)) {
            bom = bomRepository.findOne(selectedBomItem.getBom());
        } else if (selectedBomItem.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
            BomItem bomItem = bomItemRepository.findOne(selectedBomItem.getParent());
            bom = bomRepository.findOne(bomItem.getBom());
        } else if (selectedBomItem.getBomItemType().equals(BomItemType.UNIT)) {
            BomItem parent = bomItemRepository.findOne(selectedBomItem.getParent());
            if (parent.getBomItemType().equals(BomItemType.SECTION) || parent.getBomItemType().equals(BomItemType.COMMONPART)) {
                bom = bomRepository.findOne(parent.getBom());
            } else {
                BomItem bomItem = bomItemRepository.findOne(parent.getParent());
                bom = bomRepository.findOne(bomItem.getBom());
            }
        }

        if (bom != null) {
            List<BomItem> sections = bomItemRepository.findByBomOrderByCreatedDateAsc(bom.getId());

            for (BomItem section : sections) {
                parts = getUniqueAndCommonParts(section, parts);
            }
        }

        for (BomItem bomItem : parts) {
            BomGroup defaultGroup = new BomGroup();
            defaultGroup.setId(0);
            defaultGroup.setName("Common");
            if (bomItem.getPathCount() > 1) {
                bomItem.setDefaultSection(defaultGroup);
                bomItem.getSections().add(0, defaultGroup);
            }

            for (StorageItem storageItem : storageItems) {
                if (bomItem.getDefaultSection() != null) {
                    if (bomItem.getUniqueCode().equals(storageItem.getUniqueCode()) && storageItem.getSection() != null) {

                        List<BomGroup> sections = bomItem.getSections();
                        sections.remove(storageItem.getSection());
                        bomItem.setPathCount(bomItem.getPathCount() - 1);
                        bomItem.setSections(sections);
                        if (bomItem.getPathCount() == 1) {
                            sections.remove(defaultGroup);
                            bomItem.setSections(sections);

                            bomItem.setDefaultSection(sections.get(0));
                        }

                    } else if (bomItem.getUniqueCode().equals(storageItem.getUniqueCode()) && storageItem.getSection() == null) {

                        List<BomGroup> sections = bomItem.getSections();
                        sections.remove(defaultGroup);
                        bomItem.setDefaultSection(sections.get(0));

                        bomItem.setSections(sections);
                    }
                }

            }

            List<StorageItem> storageItemList = storageItemRepository.findByUniqueCode(bomItem.getUniqueCode());
            storageItemList.forEach(storageItem -> {
                bomItem.getStorages().add(storageItem.getStorage());
            });

            if (bomItem.getPathCount() > 1) {
                storagePartsDto.getCommonParts().add(bomItem);
            } else if (bomItem.getPathCount() == 1) {
                storagePartsDto.getUniqueParts().add(bomItem);
            }
        }


        return storagePartsDto;
    }

    @Transactional(readOnly = true)
    public StoragePartsDto getBomPartsByBomTypeAndText(Integer bomItemId, Integer storageId, String searchText) {
        List<StorageItem> storageItems = new ArrayList<>();
        if (storageId != null) {
            storageItems = storageItemRepository.findByStorage(storageId);
        }

        List<BomItem> parts = new ArrayList<>();

        BomItem selectedBomItem = bomItemRepository.findOne(bomItemId);

        Bom selectedBom = bomRepository.findOne(bomItemId);

        List<BomItem> bomItemList = new ArrayList<>();

        if (selectedBomItem != null) {
            if (selectedBomItem.getBomItemType().equals(BomItemType.UNIT)) {
                bomItemList = bomItemRepository.getItemsByParentAndText(bomItemId, searchText);
            } else {
                List<BomItem> totalChildren = bomItemRepository.findByParentOrderByCreatedDateAsc(selectedBomItem.getId());

                List<BomItem> itemList = bomItemRepository.getChildrenByParentAndBomItemType(selectedBomItem.getId(), BomItemType.PART);
                if (itemList.size() > 0) {
                    bomItemList = bomItemRepository.getItemsByParentAndText(bomItemId, searchText);

                    if (totalChildren.size() > itemList.size()) {
                        bomItemList = bomItemRepository.findByParentOrderByCreatedDateAsc(bomItemId);
                    }
                } else {
                    bomItemList = bomItemRepository.findByParentOrderByCreatedDateAsc(bomItemId);
                }
            }
        }

        if (selectedBom != null) {
            bomItemList = bomItemRepository.findByBomOrderByCreatedDateAsc(selectedBom.getId());
        }


        for (BomItem child : bomItemList) {
            if (child.getBomItemType().equals(BomItemType.PART)) {
                if (child.getItem().getItemMaster().getItemType().getParentType() != null) {
                    child.getItem().getItemMaster().setParentType(itemTypeRepository.findOne(child.getItem().getItemMaster().getItemType().getParentType()));
                } else {
                    child.getItem().getItemMaster().setParentType(itemTypeRepository.findOne(child.getItem().getItemMaster().getItemType().getId()));
                }
                parts.add(child);
            }
            parts = visitBomPartChildrenWithSearchText(child, parts, searchText);
        }

        StoragePartsDto storagePartsDto = new StoragePartsDto();

        Bom bom = null;

        if (selectedBomItem != null) {
            if (selectedBomItem.getBomItemType().equals(BomItemType.SECTION) || selectedBomItem.getBomItemType().equals(BomItemType.COMMONPART)) {
                bom = bomRepository.findOne(selectedBomItem.getBom());
            } else if (selectedBomItem.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                BomItem bomItem = bomItemRepository.findOne(selectedBomItem.getParent());
                bom = bomRepository.findOne(bomItem.getBom());
            } else if (selectedBomItem.getBomItemType().equals(BomItemType.UNIT)) {
                BomItem parent = bomItemRepository.findOne(selectedBomItem.getParent());
                if (parent.getBomItemType().equals(BomItemType.SECTION) || parent.getBomItemType().equals(BomItemType.COMMONPART)) {
                    bom = bomRepository.findOne(parent.getBom());
                } else {
                    BomItem bomItem = bomItemRepository.findOne(parent.getParent());
                    bom = bomRepository.findOne(bomItem.getBom());
                }
            }

            if (bom != null) {
                List<BomItem> sections = bomItemRepository.findByBomOrderByCreatedDateAsc(bom.getId());

                for (BomItem section : sections) {
                    parts = getUniqueAndCommonParts(section, parts);
                }
            }
        }


        if (selectedBom != null) {
            List<BomItem> sections = bomItemRepository.findByBomOrderByCreatedDateAsc(selectedBom.getId());

            for (BomItem section : sections) {
                parts = getUniqueAndCommonParts(section, parts);
            }
        }

        for (BomItem bomItem : parts) {
            BomGroup defaultGroup = new BomGroup();
            defaultGroup.setId(0);
            defaultGroup.setName("Common");
            if (bomItem.getPathCount() > 1) {
                bomItem.setDefaultSection(defaultGroup);
                bomItem.getSections().add(0, defaultGroup);
            }

            for (StorageItem storageItem : storageItems) {
                if (bomItem.getDefaultSection() != null) {
                    if (bomItem.getUniqueCode().equals(storageItem.getUniqueCode()) && storageItem.getSection() != null) {

                        List<BomGroup> sections = bomItem.getSections();
                        sections.remove(storageItem.getSection().getName());
                        bomItem.setPathCount(bomItem.getPathCount() - 1);
                        bomItem.setSections(sections);
                        if (bomItem.getPathCount() == 1) {
                            sections.remove(defaultGroup);
                            bomItem.setSections(sections);

                            bomItem.setDefaultSection(sections.get(0));
                        }

                    } else if (bomItem.getUniqueCode().equals(storageItem.getUniqueCode()) && storageItem.getSection() == null) {

                        List<BomGroup> sections = bomItem.getSections();
                        sections.remove(defaultGroup);
                        bomItem.setDefaultSection(sections.get(0));

                        bomItem.setSections(sections);
                    }
                }

            }

            List<StorageItem> storageItemList = storageItemRepository.findByUniqueCode(bomItem.getUniqueCode());
            storageItemList.forEach(storageItem -> {
                bomItem.getStorages().add(storageItem.getStorage());
            });

            if (bomItem.getPathCount() > 1) {
                storagePartsDto.getCommonParts().add(bomItem);
            } else if (bomItem.getPathCount() == 1) {
                storagePartsDto.getUniqueParts().add(bomItem);
            }
        }


        return storagePartsDto;
    }

    private List<BomItem> getUniqueAndCommonParts(BomItem bomItem, List<BomItem> parts) {

        List<BomItem> bomItems = bomItemRepository.findByParentOrderByCreatedDateAsc(bomItem.getId());


        for (BomItem item : bomItems) {

            for (BomItem part : parts) {
                if (item.getBomItemType().equals(BomItemType.PART) && item.getUniqueCode().equals(part.getUniqueCode())) {
                    part.setPathCount(part.getPathCount() + 1);
                    if (part.getIdPath().equals("")) {
                        part.setIdPath(item.getId().toString());
                    } else {
                        part.setIdPath(part.getIdPath() + "/" + item.getId());
                    }

                    BomItem parent = bomItemRepository.findOne(item.getParent());
                    if (parent.getBomItemType().equals(BomItemType.UNIT)) {
                        BomItem parent1 = bomItemRepository.findOne(parent.getParent());
                        if (parent1.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                            BomItem parent2 = bomItemRepository.findOne(parent1.getParent());
                            if (parent2.getBomItemType().equals(BomItemType.SECTION) || parent2.getBomItemType().equals(BomItemType.COMMONPART)) {
                                if (part.getDefaultSection() == null) {
                                    part.setDefaultSection(parent2.getTypeRef());
                                }
                                part.getSections().add(parent2.getTypeRef());
                            }
                        } else {
                            if (part.getDefaultSection() == null) {
                                part.setDefaultSection(parent1.getTypeRef());
                            }
                            part.getSections().add(parent1.getTypeRef());
                        }
                    } else if (parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                        BomItem parent1 = bomItemRepository.findOne(parent.getParent());
                        if (parent1.getBomItemType().equals(BomItemType.SECTION) || parent1.getBomItemType().equals(BomItemType.COMMONPART)) {
                            if (part.getDefaultSection() == null) {
                                part.setDefaultSection(parent1.getTypeRef());
                            }
                            part.getSections().add(parent1.getTypeRef());
                        }
                    } else if (parent.getBomItemType().equals(BomItemType.COMMONPART)) {
                        if (part.getDefaultSection() == null) {
                            part.setDefaultSection(parent.getTypeRef());
                        }
                        part.getSections().add(parent.getTypeRef());
                    }
                }
            }

            parts = getUniqueAndCommonParts(item, parts);
        }

        return parts;
    }

    private List<BomItem> visitBomPartChildren(BomItem bomItem, List<BomItem> parts) {

        List<BomItem> bomItemList = bomItemRepository.findByParentOrderByCreatedDateAsc(bomItem.getId());

        for (BomItem child : bomItemList) {
            if (child.getBomItemType().equals(BomItemType.PART)) {
                if (child.getItem().getItemMaster().getItemType().getParentType() != null) {
                    child.getItem().getItemMaster().setParentType(itemTypeRepository.findOne(child.getItem().getItemMaster().getItemType().getParentType()));
                } else {
                    child.getItem().getItemMaster().setParentType(itemTypeRepository.findOne(child.getItem().getItemMaster().getItemType().getId()));
                }

                parts.add(child);
            }
            parts = visitBomPartChildren(child, parts);
        }


        return parts;
    }

    private List<BomItem> visitBomPartChildrenWithSearchText(BomItem bomItem, List<BomItem> parts, String searchText) {

        List<BomItem> bomItemList = new ArrayList<>();

        if (bomItem.getBomItemType().equals(BomItemType.UNIT)) {
            bomItemList = bomItemRepository.getItemsByParentAndText(bomItem.getId(), searchText);
        } else {

            List<BomItem> totalChildren = bomItemRepository.findByParentOrderByCreatedDateAsc(bomItem.getId());

            List<BomItem> itemList = bomItemRepository.getChildrenByParentAndBomItemType(bomItem.getId(), BomItemType.PART);
            if (itemList.size() > 0) {
                bomItemList = bomItemRepository.getItemsByParentAndText(bomItem.getId(), searchText);

                if (totalChildren.size() > itemList.size()) {
                    bomItemList = bomItemRepository.findByParentOrderByCreatedDateAsc(bomItem.getId());
                }
            } else {
                bomItemList = bomItemRepository.findByParentOrderByCreatedDateAsc(bomItem.getId());
            }
        }

        for (BomItem child : bomItemList) {
            if (child.getBomItemType().equals(BomItemType.PART)) {
                if (child.getItem().getItemMaster().getItemType().getParentType() != null) {
                    child.getItem().getItemMaster().setParentType(itemTypeRepository.findOne(child.getItem().getItemMaster().getItemType().getParentType()));
                } else {
                    child.getItem().getItemMaster().setParentType(itemTypeRepository.findOne(child.getItem().getItemMaster().getItemType().getId()));
                }
                parts.add(child);
            }
            parts = visitBomPartChildrenWithSearchText(child, parts, searchText);
        }


        return parts;
    }

    @Transactional(readOnly = false)
    public BomItem createBomItem(Integer itemId, BomItem bomItem) {

        String specName = "";
        String spec = "0000";
        if (bomItem.getBomItemType().equals(BomItemType.PART)) {
            BomItem parent = bomItemRepository.findOne(bomItem.getParent());

            if (parent != null && parent.getBomItemType().equals(BomItemType.UNIT)) {
                BomItem subSystem = bomItemRepository.findOne(parent.getParent());

                if (subSystem != null && subSystem.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                    bomItem.setUniqueCode(subSystem.getTypeRef().getCode() + "" + parent.getTypeRef().getCode() + bomItem.getItem().getItemMaster().getItemCode());
                } else if (subSystem != null && (subSystem.getBomItemType().equals(BomItemType.COMMONPART) || subSystem.getBomItemType().equals(BomItemType.SECTION))) {
                    bomItem.setUniqueCode("0" + parent.getTypeRef().getCode() + "" + bomItem.getItem().getItemMaster().getItemCode());
                }
            } else if (parent != null && parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                bomItem.setUniqueCode(parent.getTypeRef().getCode() + "00" + bomItem.getItem().getItemMaster().getItemCode());
            } else if (parent != null && parent.getBomItemType().equals(BomItemType.COMMONPART)) {
                bomItem.setUniqueCode("000" + bomItem.getItem().getItemMaster().getItemCode());
            }

            if (bomItem.getItem().getItemMaster().getPartSpec() != null) {
                if (bomItem.getItem().getItemMaster().getPartSpec().getSpecName().length() > 4) {
                    specName = bomItem.getItem().getItemMaster().getPartSpec().getSpecName().substring(bomItem.getItem().getItemMaster().getPartSpec().getSpecName().length() - 4);
                } else {
                    specName = bomItem.getItem().getItemMaster().getPartSpec().getSpecName();
                }
                spec = spec.substring(specName.length()) + specName;
                bomItem.setUniqueCode(bomItem.getUniqueCode() + "" + spec);
            } else {
                bomItem.setUniqueCode(bomItem.getUniqueCode() + "0000");
            }
        }

        bomItem = bomItemRepository.save(bomItem);

        if (bomItem.getBomItemType().equals(BomItemType.PART)) {
            ItemType itemType = itemTypeRepository.findOne(bomItem.getItem().getItemMaster().getItemType().getParentType());

            if (!itemType.getParentNode()) {
                bomItem.getItem().getItemMaster().setParentType(itemType);
            } else {
                bomItem.getItem().getItemMaster().setParentType(bomItem.getItem().getItemMaster().getItemType());
            }

            BomItem section = getSectionName(bomItem);
            if (section != null) {
                Bom bom = bomRepository.findOne(section.getBom());
                String message = sessionWrapper.getSession().getLogin().getPerson().getFullName() + " has added '" + bomItem.getItem().getItemMaster().getItemName()
                        + "' item in '" + section.getTypeRef().getName() + "' Section of " + bom.getItem().getItemMaster().getItemName() + " System";
                drdoUpdatesService.updateMessage(message, DRDOObjectType.BOMITEM);
            }
        }

        return bomItem;
    }

    private BomItem getSectionName(BomItem bomItem) {
        BomItem section = null;
        BomItem parent = bomItemRepository.findOne(bomItem.getParent());

        if (parent.getBomItemType().equals(BomItemType.UNIT)) {
            BomItem parent1 = bomItemRepository.findOne(parent.getParent());
            if (parent1.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                BomItem parent2 = bomItemRepository.findOne(parent1.getParent());
                if (parent2 != null && (parent2.getBomItemType().equals(BomItemType.SECTION) || parent2.getBomItemType().equals(BomItemType.COMMONPART))) {
                    section = parent2;
                }

            } else if (parent1.getBomItemType().equals(BomItemType.SECTION) || parent1.getBomItemType().equals(BomItemType.COMMONPART)) {
                section = parent1;
            }
        } else if (parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
            BomItem parent1 = bomItemRepository.findOne(parent.getParent());
            if (parent1 != null && (parent1.getBomItemType().equals(BomItemType.SECTION) || parent1.getBomItemType().equals(BomItemType.COMMONPART))) {
                section = parent1;
            }

        } else if (parent.getBomItemType().equals(BomItemType.SECTION) || parent.getBomItemType().equals(BomItemType.COMMONPART)) {
            section = parent;
        }

        return section;

    }

    @Transactional(readOnly = false)
    public BomItem updateBomItem(Integer itemId, BomItem bomItem) {
        return bomItemRepository.save(bomItem);
    }

    @Transactional(readOnly = false)
    public void deleteBomItem(Integer bomItemId) {

        BomItem bomItem = bomItemRepository.findOne(bomItemId);

        if (bomItem.getBomItemType().equals(BomItemType.PART)) {

            List<InwardItem> inwardItems = inwardItemRepository.findByBomItem(bomItem.getId());
            if (inwardItems.size() > 0) {
                throw new CassiniException("This item has inwards. You can't delete this item");
            }

            List<BomInstanceItem> bomInstanceItems = bomInstanceItemRepository.findByBomItem(bomItem.getId());

            if (bomInstanceItems.size() > 0) {
                throw new CassiniException("This item already using in missile. You can't delete this item");
            }

        } else {
            List<BomInstanceItem> bomInstanceItems = bomInstanceItemRepository.findByBomItem(bomItem.getId());

            if (bomInstanceItems.size() > 0) {
                throw new CassiniException("This item already using in missile. You can't delete this item");
            }
        }

        bomItemRepository.delete(bomItemId);
    }

    @Transactional(readOnly = false)
    public List<BomInstanceItem> getItemInstanceBom(Integer id, Boolean versity) {
        if (versity) {
            List<BomInstanceItem> itemInstances = bomInstanceItemRepository.getVersitySections(id);
            itemInstances.forEach(itemInstance -> {
                itemInstance.getChildren().addAll(bomInstanceItemRepository.findByParentOrderByCreatedDateAsc(itemInstance.getId()));
            });
            return itemInstances;
        } else {
            List<BomInstanceItem> itemInstances = bomInstanceItemRepository.findByBomOrderByCreatedDateAsc(id);
            itemInstances.forEach(itemInstance -> {
                itemInstance.getChildren().addAll(bomInstanceItemRepository.findByParentOrderByCreatedDateAsc(itemInstance.getId()));
            });
            return itemInstances;
        }

    }

    @Transactional(readOnly = false)
    public List<BomInstanceItem> getItemInstanceToggle(Integer id) {

        BomInstanceItem selectedItem = bomInstanceItemRepository.findOne(id);

        List<BomInstanceItem> bomInstanceItems = bomInstanceItemRepository.findByParentOrderByCreatedDateAsc(id);

        bomInstanceItems.forEach(bomInstanceItem -> {
            List<BomItemInstance> bomItemInstances = bomItemInstanceRepository.findByBomInstanceItem(bomInstanceItem.getId());

            bomItemInstances.forEach(bomItemInstance -> {
                List<IssueItem> issueItems = issueItemRepository.findByBomItemInstance(bomItemInstance);
                issueItems.forEach(issueItem -> {
                    if (issueItem.getStatus().equals(IssueItemStatus.RECEIVED)) {
                        if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                            List<LotInstance> lotInstances = lotInstanceRepository.findByInstance(bomItemInstance.getItemInstance());
                            for (LotInstance lotInstance : lotInstances) {
                                IssueItem issueItem1 = issueItemRepository.findOne(lotInstance.getIssueItem());
                                if (bomItemInstance.getId().equals(issueItem1.getBomItemInstance().getId())) {
                                    ItemInstance itemInstance = itemInstanceRepository.findOne(lotInstance.getInstance());
                                    ObjectTypeAttribute certificateNumberType = objectTypeAttributeRepository.findByNameAndObjectType("Certificate Number", ObjectType.valueOf("ITEMINSTANCE"));
                                    if (certificateNumberType != null) {
                                        ObjectAttribute certificateNumber = objectAttributeRepository.findByObjectIdAndAttributeDefId(itemInstance.getId(), certificateNumberType.getId());
                                        if (certificateNumber != null && certificateNumber.getStringValue() != null) {
                                            itemInstance.setCertificateNumber(certificateNumber.getStringValue());
                                        }
                                    }
                                    lotInstance.setItemInstance(itemInstance);
                                    bomInstanceItem.getLotInstances().add(lotInstance);
                                }
                            }
                        } else {
                            bomInstanceItem.getIssuedInstances().add(itemInstanceRepository.findOne(bomItemInstance.getItemInstance()));
                        }
                    }
                });
            });

            bomInstanceItem.getIssuedInstances().forEach(itemInstance -> {
                ObjectTypeAttribute certificateNumberType = objectTypeAttributeRepository.findByNameAndObjectType("Certificate Number", ObjectType.valueOf("ITEMINSTANCE"));

                if (certificateNumberType != null) {
                    ObjectAttribute certificateNumber = objectAttributeRepository.findByObjectIdAndAttributeDefId(itemInstance.getId(), certificateNumberType.getId());

                    if (certificateNumber != null && certificateNumber.getStringValue() != null) {
                        itemInstance.setCertificateNumber(certificateNumber.getStringValue());
                    }
                }
            });

            if (bomInstanceItem.getBomItemType().equals(BomItemType.PART)) {
                ItemType itemType = itemTypeRepository.findOne(bomInstanceItem.getItem().getItemMaster().getItemType().getParentType());

                if (!itemType.getParentNode()) {
                    bomInstanceItem.getItem().getItemMaster().setParentType(itemType);
                } else {
                    bomInstanceItem.getItem().getItemMaster().setParentType(bomInstanceItem.getItem().getItemMaster().getItemType());
                }
            }


            bomInstanceItem.getChildren().addAll(bomInstanceItemRepository.findByParentOrderByCreatedDateAsc(bomInstanceItem.getId()));
            /*if (selectedItem.getBomItemType().equals(BomItemType.SECTION)) {
                visitInstanceItemChildren(bomInstanceItem);
            } else {
                bomInstanceItem.getChildren().addAll(bomInstanceItemRepository.findByParentOrderByCreatedDateAsc(bomInstanceItem.getId()));
            }*/
        });

        return bomInstanceItems;
    }

    private void visitInstanceItemChildren(BomInstanceItem instanceItem) {


        List<BomInstanceItem> bomInstanceItems = bomInstanceItemRepository.findByParentOrderByCreatedDateAsc(instanceItem.getId());

        bomInstanceItems.forEach(bomInstanceItem -> {
            List<BomItemInstance> bomItemInstances = bomItemInstanceRepository.findByBomInstanceItem(bomInstanceItem.getId());

            bomItemInstances.forEach(bomItemInstance -> {
                if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                    List<LotInstance> lotInstances = lotInstanceRepository.findByInstance(bomItemInstance.getItemInstance());
                    for (LotInstance lotInstance : lotInstances) {
                        IssueItem issueItem = issueItemRepository.findOne(lotInstance.getIssueItem());
                        if (bomItemInstance.getId().equals(issueItem.getBomItemInstance().getId())) {
                            ItemInstance itemInstance = itemInstanceRepository.findOne(lotInstance.getInstance());
                            ObjectTypeAttribute certificateNumberType = objectTypeAttributeRepository.findByNameAndObjectType("Certificate Number", ObjectType.valueOf("ITEMINSTANCE"));
                            if (certificateNumberType != null) {
                                ObjectAttribute certificateNumber = objectAttributeRepository.findByObjectIdAndAttributeDefId(itemInstance.getId(), certificateNumberType.getId());
                                if (certificateNumber != null && certificateNumber.getStringValue() != null) {
                                    itemInstance.setCertificateNumber(certificateNumber.getStringValue());
                                }
                            }
                            lotInstance.setItemInstance(itemInstance);
                            bomInstanceItem.getLotInstances().add(lotInstance);
                        }
                    }

                } else {
                    bomInstanceItem.getIssuedInstances().add(itemInstanceRepository.findOne(bomItemInstance.getItemInstance()));
                }
            });

            bomInstanceItem.getIssuedInstances().forEach(itemInstance -> {
                ObjectTypeAttribute certificateNumberType = objectTypeAttributeRepository.findByNameAndObjectType("Certificate Number", ObjectType.valueOf("ITEMINSTANCE"));

                if (certificateNumberType != null) {
                    ObjectAttribute certificateNumber = objectAttributeRepository.findByObjectIdAndAttributeDefId(itemInstance.getId(), certificateNumberType.getId());

                    if (certificateNumber != null && certificateNumber.getStringValue() != null) {
                        itemInstance.setCertificateNumber(certificateNumber.getStringValue());
                    }
                }
            });

            if (bomInstanceItem.getBomItemType().equals(BomItemType.PART)) {
                ItemType itemType = itemTypeRepository.findOne(bomInstanceItem.getItem().getItemMaster().getItemType().getParentType());

                if (!itemType.getParentNode()) {
                    bomInstanceItem.getItem().getItemMaster().setParentType(itemType);
                } else {
                    bomInstanceItem.getItem().getItemMaster().setParentType(bomInstanceItem.getItem().getItemMaster().getItemType());
                }
            }

            instanceItem.getChildren().add(bomInstanceItem);

            visitInstanceItemChildren(bomInstanceItem);
        });

    }

    @Transactional(readOnly = true)
    public List<BomItem> getItemBom(Integer itemId, Boolean versity) {
        ItemRevision itemRevision = itemRevisionRepository.findOne(itemId);
        Bom bom = bomRepository.findByItem(itemRevision);
        List<BomItem> bomItems = new ArrayList();
        if (bom != null) {
            if (versity) {
                bomItems = bomItemRepository.getVersitySections(bom.getId());
                for (BomItem bomItem : bomItems) {
                    if (bomItem.getBomItemType().equals(BomItemType.PART)) {
                        bomItem.getItem().getItemMaster().getItemType().setParentNodeItemType(itemTypeService.getParentType(bomItem.getItem().getItemMaster().getItemType()).getName());
                    }

                    bomItem.getChildren().addAll(bomItemRepository.findByParentOrderByCreatedDateAsc(bomItem.getId()));
                }
            } else {
                bomItems = bomItemRepository.findByBomOrderByCreatedDateAsc(bom.getId());
                for (BomItem bomItem : bomItems) {
                    if (bomItem.getBomItemType().equals(BomItemType.PART)) {
                        bomItem.getItem().getItemMaster().getItemType().setParentNodeItemType(itemTypeService.getParentType(bomItem.getItem().getItemMaster().getItemType()).getName());
                    }

                    bomItem.getChildren().addAll(bomItemRepository.findByParentOrderByCreatedDateAsc(bomItem.getId()));
                }

                List<BomGroup> commonPartsSections = bomGroupRepository.findByNameAndType("Common Parts", BomItemType.COMMONPART);
                if (commonPartsSections.size() > 0) {
                    for (BomGroup bomGroup : commonPartsSections) {
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
            }
        }

        return bomItems;
    }

    @Transactional(readOnly = false)
    public Bom synchronizeBom(Integer bomId) {

        Bom bom = bomRepository.findOne(bomId);
        List<BomItem> bomItems = bomItemRepository.findByBomOrderByCreatedDateAsc(bom.getId());

        List<BomInstance> bomInstances = bomInstanceRepository.findByBomOrderByCreatedDateAsc(bom.getId());

            /*---------  Missiles ----------------*/
        for (BomInstance bomInstance : bomInstances) {

                /*------------ Bom Sections ------------------*/
            for (BomItem bomItem : bomItems) {

                    /*-----------------  Missile Section -----------------------------*/

                BomInstanceItem missileSection = bomInstanceItemRepository.findByBomAndBomItem(bomInstance.getId(), bomItem.getId());

                if (missileSection == null) {

                    BomInstanceItem bomInstanceItem = new BomInstanceItem();

                    bomInstanceItem.setBom(bomInstance.getId());
                    bomInstanceItem.setBomItem(bomItem.getId());
                    bomInstanceItem.setBomItemType(bomItem.getBomItemType());
                    bomInstanceItem.setTypeRef(bomItem.getTypeRef());
                    bomInstanceItem.setHierarchicalCode(bomInstance.getItem().getInstanceName());
                    bomInstanceItem.setIdPath(bomInstance.getId().toString());
                    bomInstanceItem.setNamePath(bomInstance.getItem().getInstanceName());

                    bomInstanceItem = bomInstanceItemRepository.save(bomInstanceItem);

                    if (bomInstanceItem.getBomItemType().equals(BomItemType.PART)) {
                        ItemAllocation itemAllocation = new ItemAllocation();
                        itemAllocation.setBom(bom.getId());
                        itemAllocation.setBomInstance(bomInstance.getId());
                        itemAllocation.setBomInstanceItem(bomInstanceItem.getId());

                        itemAllocation = itemAllocationRepository.save(itemAllocation);
                    }

                    createBomItemChildren(bom, bomInstance, bomItem, bomInstanceItem);

                } else {
                    bomInstance = visitBomChildrenAndSynchronizeBom(bom, bomItem, missileSection, bomInstance);
                }

            }
        }

        return bom;
    }

    @Transactional(readOnly = false)
    public BomItem synchronizeBomSection(Integer bomId, Integer sectionId) {

        Bom bom = bomRepository.findOne(bomId);
//        List<BomItem> bomItems = bomItemRepository.findByBomOrderByCreatedDateAsc(bom.getId());
        BomItem bomItem = bomItemRepository.findOne(sectionId);
        List<BomInstance> bomInstances = bomInstanceRepository.findByBomOrderByCreatedDateAsc(bom.getId());

            /*---------  Missiles ----------------*/
        for (BomInstance bomInstance : bomInstances) {

            /*-----------------  Missile Section -----------------------------*/
            BomInstanceItem missileSection = bomInstanceItemRepository.findByBomAndBomItem(bomInstance.getId(), bomItem.getId());

            if (missileSection == null) {

                BomInstanceItem bomInstanceItem = new BomInstanceItem();

                bomInstanceItem.setBom(bomInstance.getId());
                bomInstanceItem.setBomItem(bomItem.getId());
                bomInstanceItem.setBomItemType(bomItem.getBomItemType());
                bomInstanceItem.setTypeRef(bomItem.getTypeRef());
                bomInstanceItem.setHierarchicalCode(bomInstance.getItem().getInstanceName());
                bomInstanceItem.setIdPath(bomInstance.getId().toString());

                bomInstanceItem.setNamePath(bomInstance.getItem().getInstanceName());
                if (bomItem.getBomItemType().equals(BomItemType.PART)) {
                    bomInstanceItem.setQuantity(bomItem.getQuantity());
                    bomInstanceItem.setFractionalQuantity(bomItem.getFractionalQuantity());
                    bomInstanceItem.setWorkCenter(bomItem.getWorkCenter());
                    bomInstanceItem.setUniqueCode(bomItem.getUniqueCode());
                }

                bomInstanceItem = bomInstanceItemRepository.save(bomInstanceItem);

                if (bomInstanceItem.getBomItemType().equals(BomItemType.PART)) {
                    ItemAllocation itemAllocation = new ItemAllocation();
                    itemAllocation.setBom(bom.getId());
                    itemAllocation.setBomInstance(bomInstance.getId());
                    itemAllocation.setBomInstanceItem(bomInstanceItem.getId());

                    itemAllocation = itemAllocationRepository.save(itemAllocation);
                }

                createBomItemChildren(bom, bomInstance, bomItem, bomInstanceItem);

            } else {
                bomInstance = visitBomChildrenAndSynchronizeBom(bom, bomItem, missileSection, bomInstance);
            }
        }

        return bomItem;
    }


    @Transactional(readOnly = false)
    public BomItem synchronizeBomUnit(Integer bomId, Integer unitId) {
        BomItem sec = new BomItem();
        Bom bom = bomRepository.findOne(bomId);
//        List<BomItem> bomItems = bomItemRepository.findByBomOrderByCreatedDateAsc(bom.getId());
        BomItem bomItem = bomItemRepository.findOne(unitId);
        BomItem subsec = bomItemRepository.findOne(bomItem.getParent());
        if (subsec.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
            sec = bomItemRepository.findOne(subsec.getParent());
        } else if (subsec.getBomItemType().equals(BomItemType.SECTION) || subsec.getBomItemType().equals(BomItemType.COMMONPART)) {
            sec = bomItemRepository.findOne(subsec.getParent());
        }


        List<BomInstance> bomInstances = bomInstanceRepository.findByBomOrderByCreatedDateAsc(bom.getId());

            /*---------  Missiles ----------------*/
        for (BomInstance bomInstance : bomInstances) {

            /*-----------------  Missile Section -----------------------------*/
            BomInstanceItem missileSection = bomInstanceItemRepository.findByBomAndBomItem(bomInstance.getId(), sec.getId());

            List<BomInstanceItem> children = bomInstanceItemRepository.getParentChildrenOrderByCreatedDateAsc(missileSection.getId());

            if (children.size() > 0) {
                children.forEach(bomInstanceItem -> {
                    if (bomInstanceItem.getBomItemType().equals(BomItemType.UNIT) && bomInstanceItem.getBomItem().equals(unitId)) {
                        List<BomItem> bomItems = bomItemRepository.findByParentOrderByCreatedDateAsc(bomItem.getId());

                        for (BomItem bomItem1 : bomItems) {
                            BomInstanceItem missileSectionChild = bomInstanceItemRepository.findByParentAndBomItem(bomInstanceItem.getId(), bomItem1.getId());
                            if (missileSectionChild == null) {
                                BomInstanceItem child = new BomInstanceItem();
                                child.setParent(bomInstanceItem.getId());
                                if (bomItem1.getBomItemType().equals(BomItemType.PART)) {
                                    child.setItem(bomItem1.getItem());
                                    child.setQuantity(bomItem1.getQuantity());
                                    child.setFractionalQuantity(bomItem1.getFractionalQuantity());
                                    child.setUniqueCode(bomItem1.getUniqueCode());
                                    child.setWorkCenter(bomItem1.getWorkCenter());
                                }
                                child.setBomItem(bomItem1.getId());
                                child.setBomItemType(bomItem1.getBomItemType());
                                child.setTypeRef(bomItem1.getTypeRef());
                                child.setHierarchicalCode(bomInstanceItem.getHierarchicalCode() + "" + bomInstanceItem.getTypeRef().getCode());

                                if (!bomInstanceItem.getBomItemType().equals(BomItemType.PART)) {
                                    child.setNamePath(bomInstanceItem.getNamePath() + "/ " + bomInstanceItem.getTypeRef().getName());
                                    child.setIdPath(bomInstanceItem.getIdPath() + "/ " + bomInstanceItem.getId().toString());
                                }

                                child = bomInstanceItemRepository.save(child);

                                if (child.getBomItemType().equals(BomItemType.PART)) {
                                    ItemAllocation itemAllocation = new ItemAllocation();
                                    itemAllocation.setBom(bom.getId());
                                    itemAllocation.setBomInstance(bomInstance.getId());
                                    itemAllocation.setBomInstanceItem(child.getId());

                                    itemAllocation = itemAllocationRepository.save(itemAllocation);
                                }

                            } else {
                                if (missileSectionChild.getBomItemType().equals(BomItemType.PART)) {
                                    missileSectionChild.setQuantity(bomItem1.getQuantity());
                                    missileSectionChild.setFractionalQuantity(bomItem1.getFractionalQuantity());
                                    missileSectionChild.setUniqueCode(bomItem1.getUniqueCode());
                                    missileSectionChild.setWorkCenter(bomItem1.getWorkCenter());
                                    missileSectionChild = bomInstanceItemRepository.save(missileSectionChild);
                                }
                            }
                        }
                    } else if (bomInstanceItem.getBomItemType().equals(BomItemType.SUBSYSTEM)) {

                        List<BomInstanceItem> bomInstanceItemChildren = bomInstanceItemRepository.findByParentOrderByCreatedDateAsc(bomInstanceItem.getId());

                        for (BomInstanceItem bomInstanceItemChild : bomInstanceItemChildren) {
                            if (bomInstanceItemChild.getBomItemType().equals(BomItemType.UNIT) && bomInstanceItemChild.getBomItem().equals(unitId)) {
                                List<BomItem> bomItems = bomItemRepository.findByParentOrderByCreatedDateAsc(bomItem.getId());

                                for (BomItem bomItem1 : bomItems) {
                                    BomInstanceItem missileSectionChild = bomInstanceItemRepository.findByParentAndBomItem(bomInstanceItemChild.getId(), bomItem1.getId());
                                    if (missileSectionChild == null) {
                                        BomInstanceItem child = new BomInstanceItem();
                                        child.setParent(bomInstanceItemChild.getId());
                                        if (bomItem1.getBomItemType().equals(BomItemType.PART)) {
                                            child.setItem(bomItem1.getItem());
                                            child.setQuantity(bomItem1.getQuantity());
                                            child.setFractionalQuantity(bomItem1.getFractionalQuantity());
                                            child.setUniqueCode(bomItem1.getUniqueCode());
                                            child.setWorkCenter(bomItem1.getWorkCenter());
                                        }
                                        child.setBomItem(bomItem1.getId());
                                        child.setBomItemType(bomItem1.getBomItemType());
                                        child.setTypeRef(bomItem1.getTypeRef());
                                        child.setHierarchicalCode(bomInstanceItemChild.getHierarchicalCode() + "" + bomInstanceItemChild.getTypeRef().getCode());

                                        if (!bomInstanceItemChild.getBomItemType().equals(BomItemType.PART)) {
                                            child.setNamePath(bomInstanceItemChild.getNamePath() + "/ " + bomInstanceItemChild.getTypeRef().getName());
                                            child.setIdPath(bomInstanceItemChild.getIdPath() + "/ " + bomInstanceItemChild.getId().toString());
                                        }

                                        child = bomInstanceItemRepository.save(child);

                                        if (child.getBomItemType().equals(BomItemType.PART)) {
                                            ItemAllocation itemAllocation = new ItemAllocation();
                                            itemAllocation.setBom(bom.getId());
                                            itemAllocation.setBomInstance(bomInstance.getId());
                                            itemAllocation.setBomInstanceItem(child.getId());

                                            itemAllocation = itemAllocationRepository.save(itemAllocation);
                                        }
                                    } else {
                                        if (missileSectionChild.getBomItemType().equals(BomItemType.PART)) {
                                            missileSectionChild.setQuantity(bomItem1.getQuantity());
                                            missileSectionChild.setFractionalQuantity(bomItem1.getFractionalQuantity());
                                            missileSectionChild.setUniqueCode(bomItem1.getUniqueCode());
                                            missileSectionChild.setWorkCenter(bomItem1.getWorkCenter());
                                            missileSectionChild = bomInstanceItemRepository.save(missileSectionChild);
                                        }
                                    }
                                }
                            }
                        }

                        BomInstanceItem childItem = bomInstanceItemRepository.findByParentAndBomItem(bomInstanceItem.getId(), unitId);

                        if (childItem == null) {
                            BomInstanceItem child = new BomInstanceItem();
                            child.setParent(bomInstanceItem.getId());
                            child.setBomItem(bomItem.getId());
                            child.setBomItemType(bomItem.getBomItemType());
                            child.setTypeRef(bomItem.getTypeRef());
                            child.setHierarchicalCode(bomItem.getHierarchicalCode() + "" + bomItem.getTypeRef().getCode());

                            if (!bomItem.getBomItemType().equals(BomItemType.PART)) {
                                child.setNamePath(bomItem.getNamePath() + "/ " + bomItem.getTypeRef().getName());
                                child.setIdPath(bomItem.getIdPath() + "/ " + bomItem.getId().toString());
                            }

                            child = bomInstanceItemRepository.save(child);

                            if (child.getBomItemType().equals(BomItemType.PART)) {
                                ItemAllocation itemAllocation = new ItemAllocation();
                                itemAllocation.setBom(bom.getId());
                                itemAllocation.setBomInstance(bomInstance.getId());
                                itemAllocation.setBomInstanceItem(child.getId());

                                itemAllocation = itemAllocationRepository.save(itemAllocation);
                            }
                            createBomItemChildren(bom, bomInstance, bomItem, child);
                        }
                    }
                });
            }
        }

        return bomItem;
    }

    private BomInstance visitBomChildrenAndSynchronizeBom(Bom bom, BomItem bomItem, BomInstanceItem bomInstanceItem, BomInstance bomInstance) {

        List<BomItem> sectionChildren = bomItemRepository.findByParentOrderByCreatedDateAsc(bomItem.getId());

        for (BomItem sectionChild : sectionChildren) {

            BomInstanceItem missileSectionChild = bomInstanceItemRepository.findByParentAndBomItem(bomInstanceItem.getId(), sectionChild.getId());

            if (missileSectionChild == null) {
                BomInstanceItem child = new BomInstanceItem();
                child.setParent(bomInstanceItem.getId());
                if (sectionChild.getBomItemType().equals(BomItemType.PART)) {
                    child.setItem(sectionChild.getItem());
                    child.setQuantity(sectionChild.getQuantity());
                    child.setFractionalQuantity(sectionChild.getFractionalQuantity());
                    child.setUniqueCode(sectionChild.getUniqueCode());
                    child.setWorkCenter(sectionChild.getWorkCenter());
                }
                child.setBomItem(sectionChild.getId());
                child.setBomItemType(sectionChild.getBomItemType());
                child.setTypeRef(sectionChild.getTypeRef());
                child.setHierarchicalCode(bomInstanceItem.getHierarchicalCode() + "" + bomInstanceItem.getTypeRef().getCode());

                if (!bomInstanceItem.getBomItemType().equals(BomItemType.PART)) {
                    child.setNamePath(bomInstanceItem.getNamePath() + "/ " + bomInstanceItem.getTypeRef().getName());
                    child.setIdPath(bomInstanceItem.getIdPath() + "/ " + bomInstanceItem.getId().toString());
                }

                child = bomInstanceItemRepository.save(child);

                if (child.getBomItemType().equals(BomItemType.PART)) {
                    ItemAllocation itemAllocation = new ItemAllocation();
                    itemAllocation.setBom(bom.getId());
                    itemAllocation.setBomInstance(bomInstance.getId());
                    itemAllocation.setBomInstanceItem(child.getId());

                    itemAllocation = itemAllocationRepository.save(itemAllocation);
                }

                createBomItemChildren(bom, bomInstance, sectionChild, child);

            } else {
                if (missileSectionChild.getBomItemType().equals(BomItemType.PART)) {
                    missileSectionChild.setQuantity(sectionChild.getQuantity());
                    missileSectionChild.setFractionalQuantity(sectionChild.getFractionalQuantity());
                    missileSectionChild.setUniqueCode(sectionChild.getUniqueCode());
                    missileSectionChild.setWorkCenter(sectionChild.getWorkCenter());
                    missileSectionChild = bomInstanceItemRepository.save(missileSectionChild);
                }
                bomInstance = visitBomChildrenAndSynchronizeBom(bom, sectionChild, missileSectionChild, bomInstance);
            }
        }

        return bomInstance;
    }

    @Transactional(readOnly = true)
    public List<BomItem> getChildrenByBomItem(Integer bomItemId) {
        List<BomItem> bomItems = bomItemRepository.getSubSystemAndUnitChildren(bomItemId);
        return bomItems;
    }

    @Transactional(readOnly = true)
    public List<BomItem> getBomItemChildren(Integer bomItemId) {
        List<BomItem> bomItems = bomItemRepository.findByParentOrderByCreatedDateAsc(bomItemId);

        BomItem selectedItem = bomItemRepository.findOne(bomItemId);

        for (BomItem bomItem : bomItems) {

            if (bomItem.getBomItemType().equals(BomItemType.PART)) {
                ItemType itemType = itemTypeRepository.findOne(bomItem.getItem().getItemMaster().getItemType().getParentType());

                if (!itemType.getParentNode()) {
                    bomItem.getItem().getItemMaster().setParentType(itemType);
                } else {
                    bomItem.getItem().getItemMaster().setParentType(bomItem.getItem().getItemMaster().getItemType());
                }
            }

            BomItem parent = bomItemRepository.findOne(bomItem.getParent());
            if (parent.getBomItemType().equals(BomItemType.UNIT)) {
                bomItem.setNamePath(parent.getTypeRef().getName());
                BomItem parent1 = bomItemRepository.findOne(parent.getParent());
                if (parent1.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                    bomItem.setNamePath(parent1.getTypeRef().getName() + " / " + bomItem.getNamePath());
                    BomItem parent2 = bomItemRepository.findOne(parent1.getParent());
                    if (parent2.getBomItemType().equals(BomItemType.SECTION) || parent2.getBomItemType().equals(BomItemType.COMMONPART)) {
                        bomItem.setNamePath(parent2.getTypeRef().getName() + " / " + bomItem.getNamePath());
                    }
                } else {
                    bomItem.setNamePath(parent1.getTypeRef().getName() + " / " + bomItem.getNamePath());
                }
            } else if (parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                bomItem.setNamePath(parent.getTypeRef().getName());
                BomItem parent1 = bomItemRepository.findOne(parent.getParent());
                if (parent1.getBomItemType().equals(BomItemType.SECTION) || parent1.getBomItemType().equals(BomItemType.COMMONPART)) {
                    bomItem.setNamePath(parent1.getTypeRef().getName() + " / " + bomItem.getNamePath());
                }
            } else if (parent.getBomItemType().equals(BomItemType.SECTION) || parent.getBomItemType().equals(BomItemType.COMMONPART)) {
                bomItem.setNamePath(parent.getTypeRef().getName());
            }


            List<BomItem> children = bomItemRepository.findByParentOrderByCreatedDateAsc(bomItem.getId());
            bomItem.getChildren().addAll(children);

            /*----------- To Expand Entire Section -------------*/

            /*if (selectedItem.getBomItemType().equals(BomItemType.SECTION)) {
                populateBomItemChildren(bomItem);
            } else {
                List<BomItem> children = bomItemRepository.findByParentOrderByCreatedDateAsc(bomItem.getId());
                bomItem.getChildren().addAll(children);
            }*/
        }

        return bomItems;
    }

    public void populateBomItemChildren(BomItem bomItem) {
        List<BomItem> children = bomItemRepository.findByParentOrderByCreatedDateAsc(bomItem.getId());

        for (BomItem child : children) {

            if (child.getBomItemType().equals(BomItemType.PART)) {
                ItemType itemType = itemTypeRepository.findOne(child.getItem().getItemMaster().getItemType().getParentType());

                if (!itemType.getParentNode()) {
                    child.getItem().getItemMaster().setParentType(itemType);
                } else {
                    child.getItem().getItemMaster().setParentType(child.getItem().getItemMaster().getItemType());
                }
            }

            BomItem parent = bomItemRepository.findOne(child.getParent());
            if (parent.getBomItemType().equals(BomItemType.UNIT)) {
                child.setNamePath(parent.getTypeRef().getName());
                BomItem parent1 = bomItemRepository.findOne(parent.getParent());
                if (parent1.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                    child.setNamePath(parent1.getTypeRef().getName() + " / " + child.getNamePath());
                    BomItem parent2 = bomItemRepository.findOne(parent1.getParent());
                    if (parent2.getBomItemType().equals(BomItemType.SECTION) || parent2.getBomItemType().equals(BomItemType.COMMONPART)) {
                        child.setNamePath(parent2.getTypeRef().getName() + " / " + child.getNamePath());
                    }
                } else {
                    child.setNamePath(parent1.getTypeRef().getName() + " / " + child.getNamePath());
                }
            } else if (parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                child.setNamePath(parent.getTypeRef().getName());
                BomItem parent1 = bomItemRepository.findOne(parent.getParent());
                if (parent1.getBomItemType().equals(BomItemType.SECTION) || parent1.getBomItemType().equals(BomItemType.COMMONPART)) {
                    child.setNamePath(parent1.getTypeRef().getName() + " / " + child.getNamePath());
                }
            } else if (parent.getBomItemType().equals(BomItemType.SECTION) || parent.getBomItemType().equals(BomItemType.COMMONPART)) {
                child.setNamePath(parent.getTypeRef().getName());
            }

            bomItem.getChildren().add(child);

            populateBomItemChildren(child);
        }

    }

    @Transactional
    public List<ItemInstance> getBomInstance(Integer item) {
        List<ItemInstance> itemInstances = itemInstanceRepository.findByItem(item);
        return itemInstances;
    }

    @Transactional(readOnly = true)
    public List<BomInstance> getBomInstances(Integer item) {
        List<BomInstance> itemInstances = bomInstanceRepository.findByBomOrderByCreatedDateAsc(item);
        return itemInstances;
    }

    @Transactional
    public BomInstanceItem getBomInstanceById(Integer id) {
        return bomInstanceItemRepository.findOne(id);
    }

    @Transactional(readOnly = false)
    public synchronized List<BomInstance> createBomInstance(Integer revisionId, List<String> itemNames) {

        List<BomInstance> bomInstances = new ArrayList<>();

        for (String name : itemNames) {

            ItemInstance instance = new ItemInstance();

            ItemRevision itemRevision = itemRevisionRepository.findOne(revisionId);

            instance.setInstanceName(name.toUpperCase());
            instance.setItem(itemRevision);
            instance.setPresentStatus(ItemInstanceStatus.NEW.toString());
            instance = itemInstanceRepository.save(instance);

            BomInstance bomInstance = new BomInstance();
            Bom bom = bomRepository.findByItemRevisionId(revisionId);

            BomInstance existInstance = bomInstanceRepository.getInstanceByBomAndName(bom.getId(), name);
            if (existInstance != null) {
                throw new CassiniException(name + " Instance already exist");
            }

            bomInstance.setBom(bom.getId());
            bomInstance.setItem(instance);
            bomInstance = bomInstanceRepository.save(bomInstance);

            List<BomItem> bomItems = bomItemRepository.findByBomOrderByCreatedDateAsc(bom.getId());

            if (bomItems.size() > 0) {
                for (BomItem bomItem : bomItems) {
                    BomInstanceItem bomInstanceItem = new BomInstanceItem();
                    bomInstanceItem.setBom(bomInstance.getId());
                    bomInstanceItem.setBomItemType(bomItem.getBomItemType());
                    bomInstanceItem.setTypeRef(bomItem.getTypeRef());
                    bomInstanceItem.setBomItem(bomItem.getId());
                    bomInstanceItem.setHierarchicalCode(bomInstance.getItem().getInstanceName());
                    bomInstanceItem.setIdPath(bomInstance.getId().toString());
                    bomInstanceItem.setNamePath(bomInstance.getItem().getInstanceName());

                    bomInstanceItem = bomInstanceItemRepository.save(bomInstanceItem);

                    if (bomItem.getBomItemType().equals(BomItemType.PART)) {
                        ItemAllocation itemAllocation = new ItemAllocation();
                        itemAllocation.setBom(bom.getId());
                        itemAllocation.setBomInstance(bomInstance.getId());
                        itemAllocation.setBomInstanceItem(bomInstanceItem.getId());

                        itemAllocationRepository.save(itemAllocation);
                    }

                    createBomItemChildren(bom, bomInstance, bomItem, bomInstanceItem);
                }
            }

            bomInstances.add(bomInstance);
        }

       /* if (bomItems.size() > 0) {
            createBomInstanceItem(bomItems, bomInstance, instance);
        }*/

        return bomInstances;
    }

    private synchronized void createBomItemChildren(Bom bom, BomInstance bomInstance, BomItem bomItem, BomInstanceItem parentBomInstanceItem) {
        List<BomItem> bomItems = bomItemRepository.findByParentOrderByCreatedDateAsc(bomItem.getId());

        for (BomItem item : bomItems) {
            if (item.getBomItemType().equals(BomItemType.PART)) {
                BomInstanceItem bomInstanceItem = new BomInstanceItem();
                bomInstanceItem.setParent(parentBomInstanceItem.getId());
                bomInstanceItem.setItem(item.getItem());
                bomInstanceItem.setQuantity(item.getQuantity());
                bomInstanceItem.setBomItem(item.getId());
                bomInstanceItem.setFractionalQuantity(item.getFractionalQuantity());
                bomInstanceItem.setBomItemType(item.getBomItemType());
                bomInstanceItem.setTypeRef(item.getTypeRef());
                bomInstanceItem.setHierarchicalCode(parentBomInstanceItem.getHierarchicalCode() + "" + parentBomInstanceItem.getTypeRef().getCode());
                bomInstanceItem.setUniqueCode(item.getUniqueCode());
                bomInstanceItem.setIdPath(parentBomInstanceItem.getIdPath() + "/" + parentBomInstanceItem.getId());
                bomInstanceItem.setNamePath(parentBomInstanceItem.getNamePath() + "/ " + parentBomInstanceItem.getTypeRef().getName());

                bomInstanceItem = bomInstanceItemRepository.save(bomInstanceItem);

                ItemAllocation itemAllocation = new ItemAllocation();
                itemAllocation.setBom(bom.getId());
                itemAllocation.setBomInstance(bomInstance.getId());
                itemAllocation.setBomInstanceItem(bomInstanceItem.getId());

                itemAllocationRepository.save(itemAllocation);
            } else {
                BomInstanceItem bomInstanceItem = new BomInstanceItem();
                bomInstanceItem.setParent(parentBomInstanceItem.getId());
                bomInstanceItem.setBomItemType(item.getBomItemType());
                bomInstanceItem.setTypeRef(item.getTypeRef());
                bomInstanceItem.setBomItem(item.getId());
                bomInstanceItem.setHierarchicalCode(parentBomInstanceItem.getHierarchicalCode() + "" + parentBomInstanceItem.getTypeRef().getCode());
                bomInstanceItem.setIdPath(parentBomInstanceItem.getIdPath() + "/" + parentBomInstanceItem.getId());
                bomInstanceItem.setNamePath(parentBomInstanceItem.getNamePath() + "/ " + parentBomInstanceItem.getTypeRef().getName());

                bomInstanceItem = bomInstanceItemRepository.save(bomInstanceItem);

                createBomItemChildren(bom, bomInstance, item, bomInstanceItem);
            }
        }
    }

    @Transactional(readOnly = false)
    public void deleteBomInstance(Integer id) {
        ItemInstance instance = itemInstanceRepository.findOne(id);
        BomInstance bomInstance = bomInstanceRepository.findByItem(instance);
        if (bomInstance != null) {
            List<BomInstanceItem> bomInstanceItems = bomInstanceItemRepository.findByBom(bomInstance.getId());
            if (bomInstanceItems.size() > 0) {
                List<BomItemInstance> bomItemInstances = new ArrayList<>();
                for (BomInstanceItem bomInstanceItem : bomInstanceItems) {
                    List<BomItemInstance> bomItemInstance = bomItemInstanceRepository.findByBomInstanceItem(bomInstanceItem.getId());
                    bomItemInstances.addAll(bomItemInstance);
                }
                deleteBomInstances(bomItemInstances);
            }
        }
        itemInstanceRepository.delete(instance);
    }

    private synchronized void deleteBomInstances(List<BomItemInstance> bomItemInstances) {
        for (BomItemInstance bomItemInstance : bomItemInstances) {
            if (bomItemInstance != null) {
                Integer id = bomItemInstance.getItemInstance();
                ItemInstance instance = itemInstanceRepository.findOne(id);
                BomInstance bomInstance = bomInstanceRepository.findByItem(instance);

                if (bomInstance != null) {
                    List<BomInstanceItem> bomInstanceItems = bomInstanceItemRepository.findByBom(bomInstance.getId());
                    if (bomInstanceItems.size() > 0) {
                        List<BomItemInstance> bomItemInstances1 = new ArrayList<>();
                        for (BomInstanceItem bomInstanceItem : bomInstanceItems) {
                            List<BomItemInstance> bomItemInstance1 = bomItemInstanceRepository.findByBomInstanceItem(bomInstanceItem.getId());
                            bomItemInstances1.addAll(bomItemInstance1);
                        }

                        deleteBomInstances(bomItemInstances1);
                    }
                }
                itemInstanceRepository.delete(instance);
            }
        }

    }

    private synchronized void createBomInstanceItem(List<BomItem> bomItems, BomInstance
            bomInstance, ItemInstance instance) {
        for (BomItem item : bomItems) {
            if (!itemTypeService.getParentType(item.getItem().getItemMaster().getItemType()).getName().equals("Part")) {
                ItemInstance instance1 = new ItemInstance();
                instance1.setInstanceName(item.getItem().getItemMaster().getItemName());
                instance1.setItem(item.getItem());
                instance = itemInstanceRepository.save(instance1);
            }

            BomInstanceItem bomInstanceItem = new BomInstanceItem();
            BomItemInstance forNamePathItemInstance = bomItemInstanceRepository.findByItemInstance(bomInstance.getItem().getId());
            if (forNamePathItemInstance != null) {
                BomInstanceItem forNamePathInstanceItem = bomInstanceItemRepository.findOne(forNamePathItemInstance.getBomInstanceItem());

                if (forNamePathInstanceItem == null) {
                    bomInstanceItem.setIdPath(bomInstance.getId().toString());
                    bomInstanceItem.setNamePath(bomInstance.getItem().getInstanceName());
                } else {
                    bomInstanceItem.setIdPath(forNamePathInstanceItem.getIdPath() + "/" + bomInstance.getId().toString());
                    bomInstanceItem.setNamePath(forNamePathInstanceItem.getNamePath() + "/" + bomInstance.getItem().getInstanceName());
                }
            } else {
                bomInstanceItem.setIdPath(bomInstance.getId().toString());
                bomInstanceItem.setNamePath(bomInstance.getItem().getInstanceName());
            }

            bomInstanceItem.setBom(bomInstance.getId());
            bomInstanceItem.setItem(item.getItem());
            if (item.getItem().getItemMaster().getItemType().getHasLots()) {
                bomInstanceItem.setFractionalQuantity(item.getFractionalQuantity());
            } else {
                bomInstanceItem.setQuantity(item.getQuantity());
            }

            bomInstanceItem = bomInstanceItemRepository.save(bomInstanceItem);
            Bom bom = bomRepository.findByItemRevisionId(item.getItem().getId());

            if (!itemTypeService.getParentType(bomInstanceItem.getItem().getItemMaster().getItemType()).getName().equals("Part")) {
                BomItemInstance bomItemInstance = new BomItemInstance();
                bomItemInstance.setBomInstanceItem(bomInstanceItem.getId());
                bomItemInstance.setItemInstance(instance.getId());
                bomItemInstanceRepository.save(bomItemInstance);
            }

            List<BomItem> bomItems1 = new ArrayList();
            if (bom != null) {
                bomItems1 = bomItemRepository.findByBomOrderByCreatedDateAsc(bom.getId());

                if (bomItems1.size() > 0) {
                    BomInstance bomInstance1 = new BomInstance();
                    bomInstance1.setBom(bom.getId());
                    bomInstance1.setItem(instance);
                    bomInstance1 = bomInstanceRepository.save(bomInstance1);
                    createBomInstanceItem(bomItems1, bomInstance1, instance);
                }
            }
        }
    }

    @Transactional(readOnly = false)
    public BomInstanceItem updateBomInstanceItem(BomInstanceItem bomInstanceItem) {
        return bomInstanceItemRepository.save(bomInstanceItem);
    }

    @Transactional(readOnly = false)
    public BomInstance updateBomInstance(BomInstance bomInstance) {
        return bomInstanceRepository.save(bomInstance);
    }

    @Transactional(readOnly = true)
    public BomInstance findBomInstance(Integer id) {
        return bomInstanceRepository.findOne(id);
    }

    @Transactional(readOnly = false)
    public BomItem createNewBomItem(Integer itemId, BomItem bomItem) {

        Item item = itemService.create(bomItem.getNewItem());

        ItemRevision itemRevision = itemRevisionRepository.findOne(item.getLatestRevision());

        bomItem.setItem(itemRevision);

        String specName = "";
        String spec = "0000";
        if (bomItem.getBomItemType().equals(BomItemType.PART)) {
            BomItem parent = bomItemRepository.findOne(bomItem.getParent());

            if (parent != null && parent.getBomItemType().equals(BomItemType.UNIT)) {
                BomItem subSystem = bomItemRepository.findOne(parent.getParent());

                if (subSystem != null && subSystem.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                    bomItem.setUniqueCode(subSystem.getTypeRef().getCode() + "" + parent.getTypeRef().getCode() + bomItem.getItem().getItemMaster().getItemCode());
                }
            } else if (parent != null && parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                bomItem.setUniqueCode(parent.getTypeRef().getCode() + "00" + bomItem.getItem().getItemMaster().getItemCode());
            } else if (parent != null && parent.getBomItemType().equals(BomItemType.COMMONPART)) {
                bomItem.setUniqueCode("000" + bomItem.getItem().getItemMaster().getItemCode());
            }

            if (bomItem.getItem().getItemMaster().getPartSpec() != null) {
                if (bomItem.getItem().getItemMaster().getPartSpec().getSpecName().length() > 4) {
                    specName = bomItem.getItem().getItemMaster().getPartSpec().getSpecName().substring(bomItem.getItem().getItemMaster().getPartSpec().getSpecName().length() - 4);
                } else {
                    specName = bomItem.getItem().getItemMaster().getPartSpec().getSpecName();
                }
                spec = spec.substring(specName.length()) + specName;
                bomItem.setUniqueCode(bomItem.getUniqueCode() + "" + spec);
            } else {
                bomItem.setUniqueCode(bomItem.getUniqueCode() + "0000");
            }
        }

        bomItem = bomItemRepository.save(bomItem);

        if (bomItem.getBomItemType().equals(BomItemType.PART)) {
            ItemType itemType = itemTypeRepository.findOne(bomItem.getItem().getItemMaster().getItemType().getParentType());

            if (!itemType.getParentNode()) {
                bomItem.getItem().getItemMaster().setParentType(itemType);
            } else {
                bomItem.getItem().getItemMaster().setParentType(bomItem.getItem().getItemMaster().getItemType());
            }
        }

        return bomItem;
    }

    private BomInstance visitChildrenAndAddBomItemToInstance(BomInstance bomInstance, BomItem section, BomInstanceItem instanceSection, BomItem bomItem) {

        List<BomItem> sectionChildren = bomItemRepository.findByParentOrderByCreatedDateAsc(section.getId());
        List<BomInstanceItem> instanceSectionChildren = bomInstanceItemRepository.findByParentOrderByCreatedDateAsc(instanceSection.getId());


        for (BomItem sectionChild : sectionChildren) {

            for (BomInstanceItem instanceSectionChild : instanceSectionChildren) {
                if (sectionChild.getId().equals(instanceSectionChild.getBomItem()) && instanceSectionChild.getBomItem().equals(bomItem.getParent())) {
                    BomInstanceItem bomInstanceItem = new BomInstanceItem();
                    bomInstanceItem.setParent(instanceSectionChild.getId());
                    bomInstanceItem.setBomItem(bomItem.getId());
                    bomInstanceItem.setBomItemType(bomItem.getBomItemType());
                    if (bomItem.getBomItemType().equals(BomItemType.PART)) {
                        bomInstanceItem.setQuantity(bomItem.getQuantity());
                        bomInstanceItem.setFractionalQuantity(bomItem.getFractionalQuantity());
                        bomInstanceItem.setItem(bomItem.getItem());
                    } else {
                        bomInstanceItem.setTypeRef(bomItem.getTypeRef());
                    }
                    bomInstanceItem.setUniqueCode(bomItem.getUniqueCode());
                    bomInstanceItem.setIdPath(instanceSectionChild.getIdPath() + "/" + instanceSectionChild.getId());
                    bomInstanceItem.setNamePath(instanceSectionChild.getNamePath() + "/ " + instanceSectionChild.getTypeRef().getName());
                    bomInstanceItem.setHierarchicalCode(instanceSectionChild.getHierarchicalCode() + "" + instanceSectionChild.getTypeRef().getCode());

                    bomInstanceItem = bomInstanceItemRepository.save(bomInstanceItem);
                }

                bomInstance = visitChildrenAndAddBomItemToInstance(bomInstance, sectionChild, instanceSectionChild, bomItem);

            }

        }

        return bomInstance;
    }

    @Transactional(readOnly = true)
    public Page<BomItem> getInwardSearchResults(BomSearchCriteria bomSearchCriteria, Pageable pageable) {

        Predicate predicate = bomSearchPredicateBuilder.build(bomSearchCriteria, QBomItem.bomItem);

        Pageable pageable1 = new PageRequest(0, 1000,
                new Sort(new Sort.Order(Sort.Direction.ASC, "createdDate")));

        List<BomItem> bomItemList = new ArrayList<>();
        Page<BomItem> bomItems = null;
        if (predicate != null) {
            Bom bom = bomRepository.findOne(bomSearchCriteria.getBom());
            bomItems = bomItemRepository.findAll(predicate, pageable1);

            List<BomGroup> sections = bomGroupRepository.findByType(BomItemType.SECTION);
            sections.addAll(bomGroupRepository.findByType(BomItemType.COMMONPART));
            List<BomGroup> subsystems = bomGroupRepository.findByType(BomItemType.SUBSYSTEM);
            List<BomGroup> units = bomGroupRepository.findByType(BomItemType.UNIT);

            HashMap<String, BomGroup> sectionMap = new HashMap<>();
            HashMap<String, BomGroup> subsystemMap = new HashMap<>();
            HashMap<String, BomGroup> unitMap = new HashMap<>();

            sections.forEach(section -> {
                sectionMap.put(section.getCode(), section);
            });

            subsystems.forEach(subsystem -> {
                subsystemMap.put(subsystem.getCode(), subsystem);
            });

            units.forEach(unit -> {
                unitMap.put(unit.getCode(), unit);
            });

            Map<String, BomItem> bomItemMap = new HashMap<>();
            String systemCode = bom.getItem().getItemMaster().getItemCode();
            for (BomItem bomItem : bomItems.getContent()) {

                List<BomItem> systemBomItems = new ArrayList<>();
                BomGroup section = null;
                String subsystem = "";
                String unit = "";
                List<BomItem> itemList = bomItemRepository.findByItemAndUniqueCode(bomItem.getItem(), bomItem.getUniqueCode());

                if (itemList.size() > 0) {
                    itemList.forEach(item -> {
                        String itemSystemCode = item.getHierarchicalCode().substring(0, 2);
                        if (systemCode.equals(itemSystemCode)) {
                            systemBomItems.add(item);
                        }
                    });
                }

                if (systemBomItems.size() > 1) {
                    String subsystemCode = bomItem.getUniqueCode().substring(0, 1);
                    String unicodeCode = bomItem.getUniqueCode().substring(1, 3);

                    systemBomItems.forEach(systemBomItem -> {
                        String itemSystemCode = systemBomItem.getHierarchicalCode().substring(2, 3);
                        BomGroup sectionName = sectionMap.get(itemSystemCode);


                        if (bomItem.getDefaultSection() == null) {
                            bomItem.setDefaultSection(sectionName);
                        }

                        bomItem.getSections().add(sectionName);
                    });

                    if (!subsystemCode.equals("0")) {
                        subsystem = subsystemMap.get(subsystemCode).getName();
                        bomItem.setUniquePath(subsystem);
                    }

                    if (!unicodeCode.equals("00")) {
                        unit = unitMap.get(unicodeCode).getName();
                        bomItem.setUniquePath(bomItem.getUniquePath() + " / " + unit);
                    }
                    bomItem.setPathCount(systemBomItems.size());
                } else if (systemBomItems.size() == 1) {
                    String subsystemCode = bomItem.getUniqueCode().substring(0, 1);
                    String unicodeCode = bomItem.getUniqueCode().substring(1, 3);
                    String sectionCode = bomItem.getHierarchicalCode().substring(2, 3);
                    section = sectionMap.get(sectionCode);
                    BomGroup sectionGroup = sectionMap.get(sectionCode);
                    if (sectionGroup.getVersity()) {
                        bomItem.setUniquePath(bom.getItem().getItemMaster().getItemName() + " / " + section.getName() + " ( VSPL )");
                    } else {
                        bomItem.setUniquePath(bom.getItem().getItemMaster().getItemName() + " / " + section.getName());
                    }

                    if (bomItem.getDefaultSection() == null) {
                        bomItem.setDefaultSection(section);
                    }

                    bomItem.getSections().add(section);


                    if (!subsystemCode.equals("0")) {
                        subsystem = subsystemMap.get(subsystemCode).getName();
                        bomItem.setUniquePath(bomItem.getUniquePath() + " / " + subsystem);
                    }

                    if (!unicodeCode.equals("00")) {
                        unit = unitMap.get(unicodeCode).getName();
                        bomItem.setUniquePath(bomItem.getUniquePath() + " / " + unit);
                    }
                    bomItem.setPathCount(1);
                }

                bomItemMap.put(bomItem.getUniqueCode(), bomItem);
                /*bomItemMap.put(bomItem.getUniqueCode(), bomItem);

                Bom bom = bomRepository.findOne(bomSearchCriteria.getBom());

                List<BomItem> bomChildren = bomItemRepository.findByBomOrderByCreatedDateAsc(bom.getId());
                for (BomItem bomChild : bomChildren) {
                    if (bomChild.getBomItemType().equals(BomItemType.PART) && bomChild.getUniqueCode().equals(bomItem.getUniqueCode())) {
                        bomItem.setNamePath(bomChild.getNamePath());
                        bomItem.setPathCount(bomItem.getPathCount() + 1);
                        BomItem parent = bomItemRepository.findOne(bomChild.getParent());
                        if (parent.getBomItemType().equals(BomItemType.UNIT)) {
                            BomItem parent1 = bomItemRepository.findOne(parent.getParent());
                            if (parent1.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                                BomItem parent2 = bomItemRepository.findOne(parent1.getParent());
                                if (parent2.getBomItemType().equals(BomItemType.SECTION)) {
                                    if (bomItem.getDefaultSection() == null) {
                                        bomItem.setDefaultSection(parent2.getTypeRef().getName());
                                    }
                                    bomItem.getSections().add(parent2.getTypeRef().getName());
                                }
                            } else {
                                if (bomItem.getDefaultSection() == null) {
                                    bomItem.setDefaultSection(parent1.getTypeRef().getName());
                                }
                                bomItem.getSections().add(parent1.getTypeRef().getName());
                            }
                        } else if (parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                            BomItem parent1 = bomItemRepository.findOne(parent.getParent());
                            if (parent1.getBomItemType().equals(BomItemType.SECTION)) {
                                if (bomItem.getDefaultSection() == null) {
                                    bomItem.setDefaultSection(parent1.getTypeRef().getName());
                                }
                                bomItem.getSections().add(parent1.getTypeRef().getName());
                            }
                        }
                    }

                    if (bomChild.getBomItemType().equals(BomItemType.SUBSYSTEM) || bomChild.getBomItemType().equals(BomItemType.UNIT)) {
                        bomItem.setUniquePath(bomChild.getUniquePath());
                    }
                    bomChild.setNamePath(bom.getItem().getItemMaster().getItemName());
                    visitChidrenPath(bomItem, bomChild);
                }*/
            }


            for (BomItem item : bomItemMap.values()) {
                BomGroup defaultGroup = new BomGroup();
                defaultGroup.setId(0);
                defaultGroup.setName("Common");
                if (item.getPathCount() > 1) {
                    item.setDefaultSection(defaultGroup);
                    item.getSections().add(0, defaultGroup);
                }

                if (item.getBomItemType().equals(BomItemType.PART)) {
                    ItemType itemType = itemTypeRepository.findOne(item.getItem().getItemMaster().getItemType().getParentType());

                    if (!itemType.getParentNode()) {
                        item.getItem().getItemMaster().setParentType(itemType);
                    } else {
                        item.getItem().getItemMaster().setParentType(item.getItem().getItemMaster().getItemType());
                    }
                }
                bomItemList.add(item);
            }

            int start = pageable.getOffset();
            int end = (start + pageable.getPageSize()) > bomItemList.size() ? bomItemList.size() : (start + pageable.getPageSize());


            Page<BomItem> bomItemsPage = new PageImpl<BomItem>(bomItemList.subList(start, end), pageable, bomItemList.size());
            return bomItemsPage;
        } else {
            return null;
        }


    }

    private void visitChidrenPath(BomItem bomItem, BomItem bomChild) {
        List<BomItem> bomItems = bomItemRepository.findByParentOrderByCreatedDateAsc(bomChild.getId());
        if (bomItems.size() > 0) {
            for (BomItem item : bomItems) {
                if (item.getBomItemType().equals(BomItemType.PART) && item.getUniqueCode().equals(bomItem.getUniqueCode())) {
                    bomItem.setNamePath(item.getNamePath());
                    bomItem.setUniquePath(item.getUniquePath());
                    bomItem.setPathCount(bomItem.getPathCount() + 1);
                    BomItem parent = bomItemRepository.findOne(item.getParent());
                    if (parent.getBomItemType().equals(BomItemType.UNIT)) {
                        BomItem parent1 = bomItemRepository.findOne(parent.getParent());
                        if (parent1.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                            BomItem parent2 = bomItemRepository.findOne(parent1.getParent());
                            if (parent2.getBomItemType().equals(BomItemType.SECTION) || parent2.getBomItemType().equals(BomItemType.COMMONPART)) {
                                if (bomItem.getDefaultSection() == null) {
                                    bomItem.setDefaultSection(parent2.getTypeRef());
                                }
                                bomItem.getSections().add(parent2.getTypeRef());
                            }
                        } else {
                            if (bomItem.getDefaultSection() == null) {
                                bomItem.setDefaultSection(parent1.getTypeRef());
                            }
                            bomItem.getSections().add(parent1.getTypeRef());
                        }
                    } else if (parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                        BomItem parent1 = bomItemRepository.findOne(parent.getParent());
                        if (parent1.getBomItemType().equals(BomItemType.SECTION) || parent1.getBomItemType().equals(BomItemType.COMMONPART)) {
                            if (bomItem.getDefaultSection() == null) {
                                bomItem.setDefaultSection(parent1.getTypeRef());
                            }
                            bomItem.getSections().add(parent1.getTypeRef());
                        }
                    } else if (parent.getBomItemType().equals(BomItemType.COMMONPART)) {
                        if (bomItem.getDefaultSection() == null) {
                            bomItem.setDefaultSection(parent.getTypeRef());
                        }
                        bomItem.getSections().add(parent.getTypeRef());
                    }
                }

                if (bomChild.getBomItemType().equals(BomItemType.SUBSYSTEM) || bomChild.getBomItemType().equals(BomItemType.UNIT)) {
                    item.setUniquePath(bomChild.getUniquePath() + "/ " + bomChild.getTypeRef().getName());
                }
                item.setNamePath(bomChild.getNamePath() + "/ " + bomChild.getTypeRef().getName());
                visitChidrenPath(bomItem, item);
            }
        }
    }

    @Transactional(readOnly = true)
    public Page<BomInstanceItem> getRequestSearchResults(BomSearchCriteria bomSearchCriteria, Pageable pageable) {

        Predicate predicate = bomInstanceItemSearchPredicateBuilder.build(bomSearchCriteria, QBomInstanceItem.bomInstanceItem);
        Page<BomInstanceItem> bomInstanceItems = null;

        if (predicate != null) {
            bomInstanceItems = bomInstanceItemRepository.findAll(predicate, pageable);

            BomInstance bomInstance = bomInstanceRepository.findOne(bomSearchCriteria.getBom());

            for (BomInstanceItem bomInstanceItem : bomInstanceItems) {
                List<RequestItem> requestItems = requestItemRepository.getNotRejectedRequestItems(bomInstanceItem.getId(), bomInstance.getId(), RequestItemStatus.REJECTED);
                for (RequestItem requestItem : requestItems) {
                    if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                        bomInstanceItem.setFractionalRequestedQuantity(bomInstanceItem.getFractionalRequestedQuantity() + requestItem.getFractionalQuantity());
                    } else {
                        bomInstanceItem.setRequestedQuantity(bomInstanceItem.getRequestedQuantity() + requestItem.getQuantity());
                    }
                }

                if (!bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                    bomInstanceItem.setCanRequestMore(bomInstanceItem.getQuantity().doubleValue() - bomInstanceItem.getRequestedQuantity().doubleValue());
                } else {
                    bomInstanceItem.setCanRequestMore(bomInstanceItem.getFractionalQuantity() - bomInstanceItem.getFractionalRequestedQuantity());
                }

                ItemAllocation itemAllocation = itemAllocationRepository.findByBomInstanceAndBomInstanceItem(bomSearchCriteria.getBom(), bomInstanceItem.getId());

                if (itemAllocation != null) {
                    bomInstanceItem.setIssuedQuantity(itemAllocation.getIssuedQty());
                    bomInstanceItem.setFailedQuantity(itemAllocation.getFailedQty());
                    bomInstanceItem.setAllocatedQty(itemAllocation.getAllocateQty() - itemAllocation.getIssuedQty());
                    bomInstanceItem.setCanRequestMore(bomInstanceItem.getCanRequestMore() + itemAllocation.getFailedQty());
                }

                if (bomInstanceItem.getBomItemType().equals(BomItemType.PART)) {
                    ItemType itemType = itemTypeRepository.findOne(bomInstanceItem.getItem().getItemMaster().getItemType().getParentType());

                    if (!itemType.getParentNode()) {
                        bomInstanceItem.getItem().getItemMaster().setParentType(itemType);
                    } else {
                        bomInstanceItem.getItem().getItemMaster().setParentType(bomInstanceItem.getItem().getItemMaster().getItemType());
                    }
                }
            }
        }

        return bomInstanceItems;
    }

    @Transactional(readOnly = true)
    public Page<BomInstanceItem> getRequestSearchBySection(BomSearchCriteria bomSearchCriteria, Pageable pageable) {

        Predicate predicate = bomInstanceItemSearchPredicateBuilder.build(bomSearchCriteria, QBomInstanceItem.bomInstanceItem);
        Page<BomInstanceItem> bomInstanceItems = null;

        if (predicate != null) {
            bomInstanceItems = bomInstanceItemRepository.findAll(predicate, pageable);

            BomInstance bomInstance = bomInstanceRepository.findOne(bomSearchCriteria.getBom());

            for (BomInstanceItem bomInstanceItem : bomInstanceItems) {
                List<RequestItem> requestItems = requestItemRepository.getNotRejectedRequestItems(bomInstanceItem.getId(), bomInstance.getId(), RequestItemStatus.REJECTED);
                for (RequestItem requestItem : requestItems) {
                    if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                        bomInstanceItem.setFractionalRequestedQuantity(bomInstanceItem.getFractionalRequestedQuantity() + requestItem.getFractionalQuantity());
                    } else {
                        bomInstanceItem.setRequestedQuantity(bomInstanceItem.getRequestedQuantity() + requestItem.getQuantity());
                    }
                }

                if (!bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                    bomInstanceItem.setCanRequestMore(bomInstanceItem.getQuantity().doubleValue() - bomInstanceItem.getRequestedQuantity().doubleValue());
                } else {
                    bomInstanceItem.setCanRequestMore(bomInstanceItem.getFractionalQuantity() - bomInstanceItem.getFractionalRequestedQuantity());
                }

                ItemAllocation itemAllocation = itemAllocationRepository.findByBomInstanceAndBomInstanceItem(bomSearchCriteria.getBom(), bomInstanceItem.getId());

                if (itemAllocation != null) {
                    bomInstanceItem.setIssuedQuantity(itemAllocation.getIssuedQty());
                    bomInstanceItem.setFailedQuantity(itemAllocation.getFailedQty());
                    bomInstanceItem.setAllocatedQty(itemAllocation.getAllocateQty() - itemAllocation.getIssuedQty());
                    bomInstanceItem.setCanRequestMore(bomInstanceItem.getCanRequestMore() + itemAllocation.getFailedQty());
                }

                if (bomInstanceItem.getBomItemType().equals(BomItemType.PART)) {
                    ItemType itemType = itemTypeRepository.findOne(bomInstanceItem.getItem().getItemMaster().getItemType().getParentType());

                    if (!itemType.getParentNode()) {
                        bomInstanceItem.getItem().getItemMaster().setParentType(itemType);
                    } else {
                        bomInstanceItem.getItem().getItemMaster().setParentType(bomInstanceItem.getItem().getItemMaster().getItemType());
                    }
                }
            }
        }

        return bomInstanceItems;
    }

    @Transactional(readOnly = true)
    public List<BomInstanceItem> getPartsBySection(String path) {
        List<BomInstanceItem> bomInstanceItems1 = new ArrayList();
        List<BomInstanceItem> bomInstanceItems = bomInstanceItemRepository.findByStartWithIdPath(path);
        bomInstanceItems.forEach(bomInstanceItem -> {
            if (bomInstanceItem.getBomItemType().toString().equals("PART")) {
                List<BomItemInstance> bomItemInstances = bomItemInstanceRepository.findByBomInstanceItem(bomInstanceItem.getId());

                bomItemInstances.forEach(bomItemInstance -> {
                    bomInstanceItem.getIssuedInstances().add(itemInstanceRepository.findOne(bomItemInstance.getItemInstance()));
                });
                bomInstanceItems1.add(bomInstanceItem);
            }

        });

        return bomInstanceItems1;
    }

    public String getDrdoLogoImage() {

        String drdoLogoPathAsBase64 = null;
        try {
            String path = this.getClass().getClassLoader().getResource("").getPath();
            String fullPath = URLDecoder.decode(path, "UTF-8");
            String pathArr[] = fullPath.split("/WEB-INF/classes/");

            String drdoLogoPath = "";

            drdoLogoPath = new java.io.File(pathArr[0]).getPath() + java.io.File.separatorChar + "app" + java.io.File.separatorChar + "assets"
                    + java.io.File.separatorChar + "images" + java.io.File.separatorChar + "caslogo.png";

            Path path2 = Paths.get(drdoLogoPath);

            byte[] data2 = Files.readAllBytes(path2);
            byte[] encoded2 = Base64.getEncoder().encode(data2);

            drdoLogoPathAsBase64 = new String(encoded2);
            drdoLogoPathAsBase64 = "data:image/png;base64," + drdoLogoPathAsBase64;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return drdoLogoPathAsBase64;
    }

    @Transactional(readOnly = true)
    public List<BomItem> getBomItemStructure(Integer bomItemId) {

        List<BomItem> bomItems = new ArrayList<>();

        BomItem bomItem = bomItemRepository.findOne(bomItemId);

        bomItem.setLevel(0);
        bomItems.add(bomItem);

        bomItems = visitBomItemChildren(bomItem, bomItems);


        return bomItems;
    }

    @Transactional(readOnly = true)
    public List<BomItem> getBomSectionsStructure(List<Integer> ids) {

        List<BomItem> bomItems = new ArrayList<>();

        for (Integer bomItemId : ids) {
            BomItem bomItem = bomItemRepository.findOne(bomItemId);
            bomItem.setLevel(0);
            bomItems.add(bomItem);

            bomItems = visitBomItemChildren(bomItem, bomItems);
        }


        return bomItems;
    }

    private List<BomItem> visitBomItemChildren(BomItem bomItem, List<BomItem> bomItems) {
        List<BomItem> items = bomItemRepository.findByParentOrderByCreatedDateAsc(bomItem.getId());

        items.forEach(bomItem1 -> {

            bomItem1.setLevel(bomItem.getLevel() + 1);
            if (bomItem1.getBomItemType().equals(BomItemType.PART)) {
                ItemType itemType = itemTypeRepository.findOne(bomItem1.getItem().getItemMaster().getItemType().getParentType());

                if (!itemType.getParentNode()) {
                    bomItem1.getItem().getItemMaster().setParentType(itemType);
                } else {
                    bomItem1.getItem().getItemMaster().setParentType(bomItem1.getItem().getItemMaster().getItemType());
                }
            }
            bomItems.add(bomItem1);
            visitBomItemChildren(bomItem1, bomItems);
        });

        return bomItems;
    }

    @Transactional(readOnly = true)
    public List<BomInstanceInventoryDto> getRequestReportByInstance(Integer instanceId) {

        List<BomInstanceInventoryDto> requestReport = new ArrayList<>();

        List<BomInstanceItem> sections = bomInstanceItemRepository.findByBomOrderByCreatedDateAsc(instanceId);

        for (BomInstanceItem bomInstanceItem : sections) {
            bomInstanceItem.setLevel(0);
            BomInstanceInventoryDto instanceInventoryDto = new BomInstanceInventoryDto();

            if (bomInstanceItem.getBomItemType().equals(BomItemType.PART)) {
                ItemType itemType = itemTypeRepository.findOne(bomInstanceItem.getItem().getItemMaster().getItemType().getParentType());

                if (!itemType.getParentNode()) {
                    bomInstanceItem.getItem().getItemMaster().setParentType(itemType);
                } else {
                    bomInstanceItem.getItem().getItemMaster().setParentType(bomInstanceItem.getItem().getItemMaster().getItemType());
                }

            }

            instanceInventoryDto.setItem(bomInstanceItem);

            if (bomInstanceItem.getBomItemType().equals(BomItemType.PART)) {
                List<RequestItem> requestItems = requestItemRepository.getNotRejectedRequestItems(bomInstanceItem.getId(), instanceId, RequestItemStatus.REJECTED);

                requestItems.forEach(requestItem -> {
                    if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                        instanceInventoryDto.setFractionalRequested(instanceInventoryDto.getFractionalRequested() + requestItem.getFractionalQuantity());
                    } else {
                        instanceInventoryDto.setRequested(instanceInventoryDto.getRequested() + requestItem.getQuantity());
                    }
                });
            }

            requestReport.add(instanceInventoryDto);

            requestReport = visitChildrens(instanceId, bomInstanceItem, requestReport);
        }

        return requestReport;
    }

    @Transactional(readOnly = true)
    public List<BomInstanceInventoryDto> getRequestReportBySection(Integer instanceId, Integer sectionId) {

        List<BomInstanceInventoryDto> requestReport = new ArrayList<>();

        BomInstanceItem section = bomInstanceItemRepository.findOne(sectionId);

        BomInstanceInventoryDto instanceInventoryDto = new BomInstanceInventoryDto();
        section.setLevel(0);
        instanceInventoryDto.setItem(section);

        requestReport.add(instanceInventoryDto);

        List<BomInstanceItem> sections = bomInstanceItemRepository.findByParentOrderByCreatedDateAsc(sectionId);

        for (BomInstanceItem bomInstanceItem : sections) {
            bomInstanceItem.setLevel(section.getLevel() + 1);
            BomInstanceInventoryDto bomInstanceInventoryDto = new BomInstanceInventoryDto();

            if (bomInstanceItem.getBomItemType().equals(BomItemType.PART)) {
                ItemType itemType = itemTypeRepository.findOne(bomInstanceItem.getItem().getItemMaster().getItemType().getParentType());
                if (!itemType.getParentNode()) {
                    bomInstanceItem.getItem().getItemMaster().setParentType(itemType);
                } else {
                    bomInstanceItem.getItem().getItemMaster().setParentType(bomInstanceItem.getItem().getItemMaster().getItemType());
                }
            }

            bomInstanceInventoryDto.setItem(bomInstanceItem);

            if (bomInstanceItem.getBomItemType().equals(BomItemType.PART)) {
                List<RequestItem> requestItems = requestItemRepository.getNotRejectedRequestItems(bomInstanceItem.getId(), instanceId, RequestItemStatus.REJECTED);

                requestItems.forEach(requestItem -> {
                    if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                        bomInstanceInventoryDto.setFractionalRequested(bomInstanceInventoryDto.getFractionalRequested() + requestItem.getFractionalQuantity());
                    } else {
                        bomInstanceInventoryDto.setRequested(bomInstanceInventoryDto.getRequested() + requestItem.getQuantity());
                    }
                });
            }

            requestReport.add(bomInstanceInventoryDto);

            requestReport = visitChildrens(instanceId, bomInstanceItem, requestReport);
        }

        return requestReport;
    }

    private List<BomInstanceInventoryDto> visitChildrens(Integer instanceId, BomInstanceItem bomInstanceItem, List<BomInstanceInventoryDto> requestReport) {

        List<BomInstanceItem> childrenItems = bomInstanceItemRepository.findByParentOrderByCreatedDateAsc(bomInstanceItem.getId());

        for (BomInstanceItem instanceItem : childrenItems) {
            instanceItem.setLevel(bomInstanceItem.getLevel() + 1);
            BomInstanceInventoryDto instanceInventoryDto = new BomInstanceInventoryDto();
            if (instanceItem.getBomItemType().equals(BomItemType.PART)) {
                ItemType itemType = itemTypeRepository.findOne(instanceItem.getItem().getItemMaster().getItemType().getParentType());
                if (!itemType.getParentNode()) {
                    instanceItem.getItem().getItemMaster().setParentType(itemType);
                } else {
                    instanceItem.getItem().getItemMaster().setParentType(instanceItem.getItem().getItemMaster().getItemType());
                }
            }

            instanceInventoryDto.setItem(instanceItem);

            if (instanceItem.getBomItemType().equals(BomItemType.PART)) {
                List<RequestItem> requestItems = requestItemRepository.getNotRejectedRequestItems(instanceItem.getId(), instanceId, RequestItemStatus.REJECTED);

                requestItems.forEach(requestItem -> {
                    if (instanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                        instanceInventoryDto.setFractionalRequested(instanceInventoryDto.getFractionalRequested() + requestItem.getFractionalQuantity());
                    } else {
                        instanceInventoryDto.setRequested(instanceInventoryDto.getRequested() + requestItem.getQuantity());
                    }
                });
            }

            requestReport.add(instanceInventoryDto);

            requestReport = visitChildrens(instanceId, instanceItem, requestReport);
        }

        return requestReport;
    }

    @Transactional(readOnly = true)
    public List<BomInstanceItem> getSectionsByInstance(Integer instanceId, Boolean admin, Boolean versity) {
        if (admin) {
            List<BomInstanceItem> sections = bomInstanceItemRepository.findByBomOrderByCreatedDateAsc(instanceId);
            return sections;
        } else {
            if (versity) {
                List<BomInstanceItem> sections = bomInstanceItemRepository.getVersitySections(instanceId);
                return sections;
            } else {
                List<BomInstanceItem> sections = bomInstanceItemRepository.getWithoutVersitySections(instanceId);
                return sections;
            }
        }
    }

    @Transactional(readOnly = true)
    public List<BomItem> getSectionsByBom(Integer bomId) {
        List<BomItem> sections = bomItemRepository.findByBomOrderByCreatedDateAsc(bomId);
        return sections;
    }

    @Transactional(readOnly = true)
    public List<BomInstanceItem> getChildrenByItem(Integer itemId) {
        List<BomInstanceItem> children = bomInstanceItemRepository.findByParentOrderByCreatedDateAsc(itemId);
        return children;
    }

    @Transactional(readOnly = true)
    public RequestSectionDto getSectionBomInstances(Integer instanceId, BomInstanceItem section) {

        List<BomInstanceItem> bomInstanceItems = new ArrayList<>();
//        List<BomInstanceItem> notYetRequestedItems = new ArrayList<>();
        BomInstance bomInstance = bomInstanceRepository.findOne(instanceId);
        RequestSectionDto sectionDto = new RequestSectionDto();
        List<BomInstanceItem> units = new ArrayList<>();

        units = bomInstanceItemRepository.getParentChildrenOrderByCreatedDateAsc(section.getId());
        if (section.getBomItemType().equals(BomItemType.SECTION) || section.getBomItemType().equals(BomItemType.COMMONPART)) {
            List<BomInstanceItem> instanceItems = bomInstanceItemRepository.findByParentOrderByCreatedDateAsc(section.getId());

            for (BomInstanceItem bomInstanceItem : instanceItems) {
                if (bomInstanceItem.getBomItemType().equals(BomItemType.PART)) {
                    ItemType itemType = itemTypeRepository.findOne(bomInstanceItem.getItem().getItemMaster().getItemType().getParentType());

                    if (!itemType.getParentNode()) {
                        bomInstanceItem.getItem().getItemMaster().setParentType(itemType);
                    } else {
                        bomInstanceItem.getItem().getItemMaster().setParentType(bomInstanceItem.getItem().getItemMaster().getItemType());
                    }
                    bomInstanceItems.add(bomInstanceItem);
                }
            }
        } else {
            bomInstanceItems = visitSectionChildren(section, bomInstanceItems);
        }

        for (BomInstanceItem bomInstanceItem : bomInstanceItems) {
            List<RequestItem> requestItems = requestItemRepository.getNotRejectedRequestItems(bomInstanceItem.getId(), instanceId, RequestItemStatus.REJECTED);
            for (RequestItem requestItem : requestItems) {
                if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                    bomInstanceItem.setFractionalRequestedQuantity(bomInstanceItem.getFractionalRequestedQuantity() + requestItem.getFractionalQuantity());
                } else {
                    bomInstanceItem.setRequestedQuantity(bomInstanceItem.getRequestedQuantity() + requestItem.getQuantity());
                }
            }

            if (!bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                bomInstanceItem.setCanRequestMore(bomInstanceItem.getQuantity().doubleValue() - bomInstanceItem.getRequestedQuantity().doubleValue());
            } else {
                bomInstanceItem.setCanRequestMore(bomInstanceItem.getFractionalQuantity() - bomInstanceItem.getFractionalRequestedQuantity());
            }

            /*if (requestItems.size() == 0) {
                notYetRequestedItems.add(bomInstanceItem);
            }*/

            ItemAllocation itemAllocation = itemAllocationRepository.findByBomAndBomInstanceAndBomInstanceItem(bomInstance.getBom(), bomInstance.getId(), bomInstanceItem.getId());

            if (itemAllocation != null) {
                bomInstanceItem.setIssuedQuantity(itemAllocation.getIssuedQty());
                bomInstanceItem.setFailedQuantity(itemAllocation.getFailedQty());
                bomInstanceItem.setAllocatedQty(itemAllocation.getAllocateQty());
                bomInstanceItem.setCanRequestMore(bomInstanceItem.getCanRequestMore() + itemAllocation.getFailedQty());
            }
        }

        if (bomInstanceItems.size() > 0) {
            Collections.sort(bomInstanceItems, new Comparator<BomInstanceItem>() {
                public int compare(final BomInstanceItem object1, final BomInstanceItem object2) {
                    return object1.getItem().getItemMaster().getItemName().compareTo(object2.getItem().getItemMaster().getItemName());
                }
            });
        }

        sectionDto.setSection(section);
        sectionDto.setBomInstanceItems(bomInstanceItems);
//        sectionDto.setNotYetRequestedItems(notYetRequestedItems);
        sectionDto.setChildren(units);
        return sectionDto;
    }

    @Transactional(readOnly = true)
    public RequestSectionDto getReportByInstanceChildren(Integer instanceId, BomInstanceItem section) {

        List<ItemDetailsReportDto> itemDetailsReportDtos = new ArrayList<>();
        List<BomInstanceItem> bomInstanceItems = new ArrayList<>();

        BomInstance bomInstance = bomInstanceRepository.findOne(instanceId);
        Bom bom = bomRepository.findOne(bomInstance.getBom());
        RequestSectionDto sectionDto = new RequestSectionDto();
        List<BomInstanceItem> units = new ArrayList<>();

        units = bomInstanceItemRepository.getParentChildrenOrderByCreatedDateAsc(section.getId());
        if (section.getBomItemType().equals(BomItemType.SECTION) || section.getBomItemType().equals(BomItemType.COMMONPART)) {
            List<BomInstanceItem> instanceItems = bomInstanceItemRepository.findByParentOrderByCreatedDateAsc(section.getId());

            for (BomInstanceItem bomInstanceItem : instanceItems) {
                if (bomInstanceItem.getBomItemType().equals(BomItemType.PART)) {
                    ItemType itemType = itemTypeRepository.findOne(bomInstanceItem.getItem().getItemMaster().getItemType().getParentType());

                    if (!itemType.getParentNode()) {
                        bomInstanceItem.getItem().getItemMaster().setParentType(itemType);
                    } else {
                        bomInstanceItem.getItem().getItemMaster().setParentType(bomInstanceItem.getItem().getItemMaster().getItemType());
                    }
                    bomInstanceItems.add(bomInstanceItem);
                }
            }
        } else {
            bomInstanceItems = visitSectionChildren(section, bomInstanceItems);
        }

        /*for (BomInstanceItem bomInstanceItem : bomInstanceItems) {


            ItemDetailsReportDto detailsReportDto = new ItemDetailsReportDto();

            detailsReportDto.setItem(bomInstanceItem);

            BomInstanceItem bomItemSection = null;
            BomInstanceItem parent = bomInstanceItemRepository.findOne(bomInstanceItem.getParent());
            if (parent.getBomItemType().equals(BomItemType.UNIT)) {
                BomInstanceItem parent1 = bomInstanceItemRepository.findOne(parent.getParent());
                if (parent1.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                    BomInstanceItem parent2 = bomInstanceItemRepository.findOne(parent1.getParent());
                    if (parent2.getBomItemType().equals(BomItemType.SECTION) || parent2.getBomItemType().equals(BomItemType.COMMONPART)) {
                        bomItemSection = parent2;
                    }
                } else {
                    bomItemSection = parent1;
                }
            } else if (parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                BomInstanceItem parent1 = bomInstanceItemRepository.findOne(parent.getParent());
                bomItemSection = parent1;
            } else if (parent.getBomItemType().equals(BomItemType.COMMONPART)) {
                bomItemSection = parent;
            }

            if (bomItemSection != null) {
                Inventory inventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSection(bom.getId(), bomInstanceItem.getItem().getId(), bomInstanceItem.getUniqueCode(), bomItemSection.getTypeRef().getId());
                if (inventory != null) {
                    if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                        detailsReportDto.setStock(detailsReportDto.getStock() + inventory.getFractionalQtyOnHand());
                    } else {
                        detailsReportDto.setStock(detailsReportDto.getStock() + inventory.getQuantity());
                    }
                }
            }

            Inventory inventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSectionIsNull(bom.getId(), bomInstanceItem.getItem().getId(), bomInstanceItem.getUniqueCode());
            if (inventory != null) {
                if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                    detailsReportDto.setStock(detailsReportDto.getStock() + inventory.getFractionalQtyOnHand());
                } else {
                    detailsReportDto.setStock(detailsReportDto.getStock() + inventory.getQuantity());
                }
            }

            List<RequestItem> requestItems = requestItemRepository.getNotRejectedRequestItems(bomInstanceItem.getId(), instanceId, RequestItemStatus.REJECTED);
            for (RequestItem requestItem : requestItems) {
                if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                    detailsReportDto.setRequested(bomInstanceItem.getFractionalRequestedQuantity() + requestItem.getFractionalQuantity());
                } else {
                    detailsReportDto.setRequested(bomInstanceItem.getRequestedQuantity().doubleValue() + requestItem.getQuantity());
                }

                List<IssueItem> issueItems = issueItemRepository.findByRequestItem(requestItem.getId());

                issueItems.forEach(issueItem -> {
                    if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots() &&
                            (issueItem.getStatus().equals(IssueItemStatus.PENDING) || issueItem.getStatus().equals(IssueItemStatus.APPROVED) || issueItem.getStatus().equals(IssueItemStatus.P_APPROVED))) {
                        detailsReportDto.setIssueProcess(issueItem.getFractionalQuantity() + detailsReportDto.getIssueProcess());
                    } else if (!bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots() &&
                            (issueItem.getStatus().equals(IssueItemStatus.PENDING) || issueItem.getStatus().equals(IssueItemStatus.APPROVED) || issueItem.getStatus().equals(IssueItemStatus.P_APPROVED))) {
                        detailsReportDto.setIssueProcess(issueItem.getQuantity() + detailsReportDto.getIssueProcess());
                    }
                });
            }

            ItemAllocation itemAllocation = itemAllocationRepository.findByBomAndBomInstanceAndBomInstanceItem(bomInstance.getBom(), bomInstance.getId(), bomInstanceItem.getId());

            if (itemAllocation != null) {
                detailsReportDto.setIssued(itemAllocation.getIssuedQty());
                detailsReportDto.setFailure(itemAllocation.getFailedQty());
                detailsReportDto.setAllocated(itemAllocation.getAllocateQty());
            }

            if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                detailsReportDto.setShortage(bomInstanceItem.getFractionalQuantity() - (detailsReportDto.getAllocated() - detailsReportDto.getFailure()));
            } else {
                detailsReportDto.setShortage(bomInstanceItem.getQuantity() - (detailsReportDto.getAllocated() - detailsReportDto.getFailure()));
            }


            itemDetailsReportDtos.add(detailsReportDto);
        }*/

        if (bomInstanceItems.size() > 0) {
            Collections.sort(itemDetailsReportDtos, new Comparator<ItemDetailsReportDto>() {
                public int compare(final ItemDetailsReportDto object1, final ItemDetailsReportDto object2) {
                    return object1.getItem().getItem().getItemMaster().getItemName().compareTo(object2.getItem().getItem().getItemMaster().getItemName());
                }
            });
        }

        sectionDto.setSection(section);
        sectionDto.setItemDetailsReportDtos(itemDetailsReportDtos);
        sectionDto.setChildren(units);
        return sectionDto;
    }

    @Transactional(readOnly = true)
    public RequestSectionDto searchItemReport(BomSearchCriteria bomSearchCriteria, Integer[] ids) {

        List<ItemDetailsReportDto> itemDetailsReportDtos = new ArrayList<>();

        Pageable pageable1 = new PageRequest(0, 1000,
                new Sort(new Sort.Order(Sort.Direction.ASC, "createdDate")));

        Predicate predicate = bomSearchPredicateBuilder.build(bomSearchCriteria, QBomItem.bomItem);

//        BomInstance bomInstance = bomInstanceRepository.findOne(bomSearchCriteria.getBom());
        Bom bom = bomRepository.findOne(bomSearchCriteria.getBom());
        RequestSectionDto sectionDto = new RequestSectionDto();
        List<BomInstanceItem> units = new ArrayList<>();

        Page<BomItem> bomItems = bomItemRepository.findAll(predicate, pageable1);

        for (BomItem bomItem : bomItems.getContent()) {


            ItemDetailsReportDto detailsReportDto = new ItemDetailsReportDto();

            BomItem bomItemSection = null;
            BomItem parent = bomItemRepository.findOne(bomItem.getParent());
            if (parent.getBomItemType().equals(BomItemType.UNIT)) {
                bomItem.setUniquePath(parent.getTypeRef().getName());
                BomItem parent1 = bomItemRepository.findOne(parent.getParent());
                if (parent1.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                    BomItem parent2 = bomItemRepository.findOne(parent1.getParent());
                    bomItem.setUniquePath(parent1.getTypeRef().getName() + " / " + bomItem.getUniquePath());
                    if (parent2.getBomItemType().equals(BomItemType.SECTION) || parent2.getBomItemType().equals(BomItemType.COMMONPART)) {
                        bomItemSection = parent2;
                        if (parent2.getTypeRef().getVersity()) {
                            bomItem.setUniquePath(parent2.getTypeRef().getName() + "( VSPL ) / " + bomItem.getUniquePath());
                        } else {
                            bomItem.setUniquePath(parent2.getTypeRef().getName() + " / " + bomItem.getUniquePath());
                        }
                    }
                } else {
                    bomItemSection = parent1;
                    if (parent1.getTypeRef().getVersity()) {
                        bomItem.setUniquePath(parent1.getTypeRef().getName() + "( VSPL ) / " + bomItem.getUniquePath());
                    } else {
                        bomItem.setUniquePath(parent1.getTypeRef().getName() + " / " + bomItem.getUniquePath());
                    }

                }
            } else if (parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                bomItem.setUniquePath(parent.getTypeRef().getName());
                BomItem parent1 = bomItemRepository.findOne(parent.getParent());
                if (parent1.getTypeRef().getVersity()) {
                    bomItem.setUniquePath(parent1.getTypeRef().getName() + " ( VSPL ) / " + bomItem.getUniquePath());
                } else {
                    bomItem.setUniquePath(parent1.getTypeRef().getName() + " / " + bomItem.getUniquePath());
                }

                bomItemSection = parent1;
            } else if (parent.getBomItemType().equals(BomItemType.COMMONPART)) {
                bomItemSection = parent;
                if (parent.getTypeRef().getVersity()) {
                    bomItem.setUniquePath(parent.getTypeRef().getName() + " ( VSPL )");
                } else {
                    bomItem.setUniquePath(parent.getTypeRef().getName());
                }
            }

            detailsReportDto.setItem(bomItem);

            if (bomItemSection != null) {
                Inventory inventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSection(bom.getId(), bomItem.getItem().getId(), bomItem.getUniqueCode(), bomItemSection.getTypeRef().getId());
                if (inventory != null) {
                    if (bomItem.getItem().getItemMaster().getItemType().getHasLots()) {
                        detailsReportDto.setStock(detailsReportDto.getStock() + inventory.getFractionalQtyOnHand());
                    } else {
                        detailsReportDto.setStock(detailsReportDto.getStock() + inventory.getQuantity());
                    }
                }
            }

            Inventory inventory = inventoryRepository.getInventoryByBomAndItemAndUniqueCodeAndSectionIsNull(bom.getId(), bomItem.getItem().getId(), bomItem.getUniqueCode());
            if (inventory != null) {
                if (bomItem.getItem().getItemMaster().getItemType().getHasLots()) {
                    detailsReportDto.setStock(detailsReportDto.getStock() + inventory.getFractionalQtyOnHand());
                } else {
                    detailsReportDto.setStock(detailsReportDto.getStock() + inventory.getQuantity());
                }
            }

            if (ids.length > 0) {

                Map<Integer, ItemReportQuantitiesDto> allocationMap = new HashMap();
                for (Integer instanceId : ids) {
                    BomInstance instance = bomInstanceRepository.findOne(instanceId);
                    BomInstanceItem bomInstanceItem = bomInstanceItemRepository.findByBomItemAndStartWithIdPath(bomItem.getId(), instance.getId().toString());

                    ItemReportQuantitiesDto quantitiesDto = new ItemReportQuantitiesDto();

                    List<RequestItem> requestItems = requestItemRepository.getNotRejectedRequestItems(bomInstanceItem.getId(), instanceId, RequestItemStatus.REJECTED);
                    for (RequestItem requestItem : requestItems) {
                        if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                            quantitiesDto.setRequested(bomInstanceItem.getFractionalRequestedQuantity() + requestItem.getFractionalQuantity());
                        } else {
                            quantitiesDto.setRequested(bomInstanceItem.getRequestedQuantity().doubleValue() + requestItem.getQuantity());
                        }

                        List<IssueItem> issueItems = issueItemRepository.findByRequestItem(requestItem.getId());

                        issueItems.forEach(issueItem -> {
                            if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots() &&
                                    (issueItem.getStatus().equals(IssueItemStatus.PENDING) || issueItem.getStatus().equals(IssueItemStatus.APPROVED) || issueItem.getStatus().equals(IssueItemStatus.P_APPROVED))) {
                                quantitiesDto.setIssueProcess(issueItem.getFractionalQuantity() + quantitiesDto.getIssueProcess());
                            } else if (!bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots() &&
                                    (issueItem.getStatus().equals(IssueItemStatus.PENDING) || issueItem.getStatus().equals(IssueItemStatus.APPROVED) || issueItem.getStatus().equals(IssueItemStatus.P_APPROVED))) {
                                quantitiesDto.setIssueProcess(issueItem.getQuantity() + quantitiesDto.getIssueProcess());
                            }
                        });

                        quantitiesDto.setRequestedDate(requestItem.getRequest().getCreatedDate());
                    }

                    ItemAllocation itemAllocation = itemAllocationRepository.findByBomAndBomInstanceAndBomInstanceItem(bom.getId(), instance.getId(), bomInstanceItem.getId());

                    if (itemAllocation != null) {
                        quantitiesDto.setIssued(itemAllocation.getIssuedQty());
                        quantitiesDto.setFailure(itemAllocation.getFailedQty());
                        quantitiesDto.setAllocated(itemAllocation.getAllocateQty() - itemAllocation.getIssuedQty());

                        if (quantitiesDto.getAllocated() > 0) {
                            quantitiesDto.setAllocatedDate(itemAllocation.getModifiedDate());
                        }

                        if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                            quantitiesDto.setShortage(bomInstanceItem.getFractionalQuantity() - (itemAllocation.getAllocateQty() - itemAllocation.getFailedQty()));
                        } else {
                            quantitiesDto.setShortage(bomInstanceItem.getQuantity() - (itemAllocation.getAllocateQty() - itemAllocation.getFailedQty()));
                        }
                    }

                    quantitiesDto.setList(quantitiesDto.getRequested().intValue() + " / " + quantitiesDto.getAllocated().intValue() + " / " + quantitiesDto.getIssued().intValue() + " / " + quantitiesDto.getIssueProcess().intValue() + " / " + quantitiesDto.getFailure().intValue() + " / " + quantitiesDto.getShortage().intValue());

                    allocationMap.put(instanceId, quantitiesDto);
                }


                for (ItemReportQuantitiesDto reportDto : allocationMap.values()) {
                    detailsReportDto.setTotalShortage(detailsReportDto.getTotalShortage() + reportDto.getShortage());
                }

                detailsReportDto.setListMap(allocationMap);
            }

            itemDetailsReportDtos.add(detailsReportDto);
        }

        if (itemDetailsReportDtos.size() > 0) {
            Collections.sort(itemDetailsReportDtos, new Comparator<ItemDetailsReportDto>() {
                public int compare(final ItemDetailsReportDto object1, final ItemDetailsReportDto object2) {
                    return object1.getItem().getItem().getItemMaster().getItemName().compareTo(object2.getItem().getItem().getItemMaster().getItemName());
                }
            });
        }
        if (bomSearchCriteria.getSubsystem() != null) {
            sectionDto.getChildren().addAll(bomInstanceItemRepository.findByParentOrderByCreatedDateAsc(bomSearchCriteria.getSubsystem()));
        } else if (bomSearchCriteria.getSection() != null) {
            sectionDto.getChildren().addAll(bomInstanceItemRepository.findByParentOrderByCreatedDateAsc(bomSearchCriteria.getSection()));
        }
        sectionDto.setItemDetailsReportDtos(itemDetailsReportDtos);

        return sectionDto;
    }

    @Transactional(readOnly = true)
    public RequestSectionDto getBomItemsByUnits(Integer instanceId, List<Integer> unitIds) {

        List<BomInstanceItem> bomInstanceItems = new ArrayList<>();
//        List<BomInstanceItem> notYetRequestedItems = new ArrayList<>();
        BomInstance bomInstance = bomInstanceRepository.findOne(instanceId);
        RequestSectionDto sectionDto = new RequestSectionDto();

        for (Integer unitId : unitIds) {
            List<BomInstanceItem> instanceItems = bomInstanceItemRepository.findByParentOrderByCreatedDateAsc(unitId);

            for (BomInstanceItem bomInstanceItem : instanceItems) {
                if (bomInstanceItem.getBomItemType().equals(BomItemType.PART)) {
                    ItemType itemType = itemTypeRepository.findOne(bomInstanceItem.getItem().getItemMaster().getItemType().getParentType());

                    if (!itemType.getParentNode()) {
                        bomInstanceItem.getItem().getItemMaster().setParentType(itemType);
                    } else {
                        bomInstanceItem.getItem().getItemMaster().setParentType(bomInstanceItem.getItem().getItemMaster().getItemType());
                    }
                    bomInstanceItems.add(bomInstanceItem);
                }
            }
        }

        for (BomInstanceItem bomInstanceItem : bomInstanceItems) {
            List<RequestItem> requestItems = requestItemRepository.getNotRejectedRequestItems(bomInstanceItem.getId(), instanceId, RequestItemStatus.REJECTED);
            for (RequestItem requestItem : requestItems) {
                if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                    bomInstanceItem.setFractionalRequestedQuantity(bomInstanceItem.getFractionalRequestedQuantity() + requestItem.getFractionalQuantity());
                } else {
                    bomInstanceItem.setRequestedQuantity(bomInstanceItem.getRequestedQuantity() + requestItem.getQuantity());
                }
            }

            if (!bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                bomInstanceItem.setCanRequestMore(bomInstanceItem.getQuantity().doubleValue() - bomInstanceItem.getRequestedQuantity().doubleValue());
            } else {
                bomInstanceItem.setCanRequestMore(bomInstanceItem.getFractionalQuantity() - bomInstanceItem.getFractionalRequestedQuantity());
            }

            /*if (requestItems.size() == 0) {
                notYetRequestedItems.add(bomInstanceItem);
            }*/

            ItemAllocation itemAllocation = itemAllocationRepository.findByBomAndBomInstanceAndBomInstanceItem(bomInstance.getBom(), bomInstance.getId(), bomInstanceItem.getId());

            if (itemAllocation != null) {
                bomInstanceItem.setIssuedQuantity(itemAllocation.getIssuedQty());
                bomInstanceItem.setFailedQuantity(itemAllocation.getFailedQty());
                bomInstanceItem.setAllocatedQty(itemAllocation.getAllocateQty());
                bomInstanceItem.setCanRequestMore(bomInstanceItem.getCanRequestMore() + itemAllocation.getFailedQty());
            }
        }

        if (bomInstanceItems.size() > 0) {
            Collections.sort(bomInstanceItems, new Comparator<BomInstanceItem>() {
                public int compare(final BomInstanceItem object1, final BomInstanceItem object2) {
                    return object1.getItem().getItemMaster().getItemName().compareTo(object2.getItem().getItemMaster().getItemName());
                }
            });
        }

        sectionDto.setBomInstanceItems(bomInstanceItems);

        return sectionDto;
    }

    private List<BomInstanceItem> visitSectionChildren(BomInstanceItem section, List<BomInstanceItem> bomInstanceItems) {
        List<BomInstanceItem> instanceItems = bomInstanceItemRepository.findByParentOrderByCreatedDateAsc(section.getId());

        for (BomInstanceItem bomInstanceItem : instanceItems) {
            if (bomInstanceItem.getBomItemType().equals(BomItemType.PART)) {
                ItemType itemType = itemTypeRepository.findOne(bomInstanceItem.getItem().getItemMaster().getItemType().getParentType());

                if (!itemType.getParentNode()) {
                    bomInstanceItem.getItem().getItemMaster().setParentType(itemType);
                } else {
                    bomInstanceItem.getItem().getItemMaster().setParentType(bomInstanceItem.getItem().getItemMaster().getItemType());
                }
                bomInstanceItems.add(bomInstanceItem);
            }

            bomInstanceItems = visitSectionChildren(bomInstanceItem, bomInstanceItems);
        }
        return bomInstanceItems;
    }

    public void createTARBDocument(BomItem bomItem) {

        List<BomInstanceItem> bomInstanceItems = bomInstanceItemRepository.findByParentOrderByCreatedDateAsc(bomItem.getId());
        bomInstanceItems.forEach(bomInstanceItem -> {
            List<BomItemInstance> bomItemInstances = bomItemInstanceRepository.findByBomInstanceItem(bomInstanceItem.getId());
        });


    }

    @Transactional(readOnly = true)
    public List<StorageBomItemDto> validateStorage(Integer bomId) {
        List<StorageBomItemDto> storageBomItemDtos = new ArrayList<>();

        List<BomItem> sections = bomItemRepository.findByBomOrderByCreatedDateAsc(bomId);

        for (BomItem section : sections) {
            StorageBomItemDto storageBomItemDto = new StorageBomItemDto();
            section.setLevel(0);
            if (section.getBomItemType().equals(BomItemType.PART)) {

            }
            storageBomItemDto.setBomItem(section);

            storageBomItemDtos.add(storageBomItemDto);

            storageBomItemDtos = validateChildrenStorage(section, storageBomItemDtos);
        }

        return storageBomItemDtos;
    }

    private List<StorageBomItemDto> validateChildrenStorage(BomItem bomItem, List<StorageBomItemDto> storageBomItemDtos) {

        List<BomItem> children = bomItemRepository.findByParentOrderByCreatedDateAsc(bomItem.getId());
        bomItem.setChildren(children);
        for (BomItem child : children) {
            StorageBomItemDto storageBomItemDto = new StorageBomItemDto();
            child.setLevel(bomItem.getLevel() + 1);
            if (child.getBomItemType().equals(BomItemType.PART)) {
                List<StorageItem> storageItems = storageItemRepository.findByItem(child.getId());

                HashMap<String, Storage> storageHashMap = new HashMap<>();

                for (StorageItem storageItem : storageItems) {
                    storageHashMap.put(storageItem.getStorage().getName(), storageItem.getStorage());
                }

                for (Storage storage : storageHashMap.values()) {

                    String storageLocation = "";
                    if (storage.getParent() != null) {
                        Storage parentStorage = storageRepository.findOne(storage.getParent());

                        if (parentStorage != null) {
                            storageLocation = parentStorage.getName() + "/" + storage.getName();
                            if (parentStorage.getParent() != null) {
                                Storage parentStorage1 = storageRepository.findOne(parentStorage.getParent());
                                storageLocation = parentStorage1.getName() + " /" + storageLocation;

                                if (parentStorage1.getParent() != null) {
                                    Storage parentStorage2 = storageRepository.findOne(parentStorage1.getParent());
                                    storageLocation = parentStorage2.getName() + " /" + storageLocation;

                                    if (parentStorage2.getParent() != null) {

                                        Storage parentStorage3 = storageRepository.findOne(parentStorage2.getParent());
                                        storageLocation = parentStorage3.getName() + " /" + storageLocation;

                                        if (parentStorage3.getParent() != null) {

                                            Storage parentStorage4 = storageRepository.findOne(parentStorage3.getParent());
                                            storageLocation = parentStorage4.getName() + " /" + storageLocation;
                                        }
                                    }
                                }

                            }
                        }
                        storage.setStoragePath(storageLocation);

                    } else {
                        storageLocation = storage.getName();
                        storage.setStoragePath(storageLocation);
                    }

                    if (storage.getOnHold()) {
                        storageBomItemDto.getOnHoldStorages().add(storage);
                    } else if (storage.getReturned()) {
                        storageBomItemDto.getReturnStorages().add(storage);
                    } else {
                        storageBomItemDto.getInventoryStorages().add(storage);
                    }
                }

                ItemType itemType = itemTypeRepository.findOne(child.getItem().getItemMaster().getItemType().getParentType());

                if (!itemType.getParentNode()) {
                    child.getItem().getItemMaster().setParentType(itemType);
                } else {
                    child.getItem().getItemMaster().setParentType(child.getItem().getItemMaster().getItemType());
                }
            }
            storageBomItemDto.setBomItem(child);

            storageBomItemDtos.add(storageBomItemDto);

            storageBomItemDtos = validateChildrenStorage(child, storageBomItemDtos);
        }

        return storageBomItemDtos;
    }

    @Transactional(readOnly = true)
    public BomInstanceTarbDto getBomInstanceTarbDocument(Integer instanceId) {
        BomInstanceTarbDto bomInstanceTarbDto = new BomInstanceTarbDto();

        BomInstance bomInstance = bomInstanceRepository.findOne(instanceId);
        Bom bom = bomRepository.findOne(bomInstance.getBom());

        bomInstanceTarbDto.setBomInstance(bomInstance);
        bomInstanceTarbDto.setSystem(bom);
        List<BomInstanceItem> sections = bomInstanceItemRepository.findByBomOrderByCreatedDateAsc(bomInstance.getId());

        for (BomInstanceItem section : sections) {

            List<BomInstanceItem> subSystems = bomInstanceItemRepository.findByParentOrderByCreatedDateAsc(section.getId());

            for (BomInstanceItem subSystem : subSystems) {

                BomInstanceSectionTarbDto instanceSectionTarbDto = new BomInstanceSectionTarbDto();

                instanceSectionTarbDto.setSection(section);
                instanceSectionTarbDto.setSubsystem(subSystem);

                List<BomInstanceItem> parts = new ArrayList<>();
                parts = getSubSystemParts(subSystem, parts);

                instanceSectionTarbDto.getSectionParts().addAll(parts);

                bomInstanceTarbDto.getSectionTarbDtoList().add(instanceSectionTarbDto);
            }
        }

        return bomInstanceTarbDto;
    }

    @Transactional(readOnly = true)
    public BomInstanceTarbDto getBomInstanceSectionTarbDocument(Integer instanceId, Integer sectionId) {
        BomInstanceTarbDto bomInstanceTarbDto = new BomInstanceTarbDto();

        BomInstance bomInstance = bomInstanceRepository.findOne(instanceId);
        Bom bom = bomRepository.findOne(bomInstance.getBom());
        BomInstanceItem section = bomInstanceItemRepository.findOne(sectionId);

        bomInstanceTarbDto.setBomInstance(bomInstance);
        bomInstanceTarbDto.setSystem(bom);
        bomInstanceTarbDto.setSection(section);

        List<BomInstanceItem> subSystems = bomInstanceItemRepository.findByParentOrderByCreatedDateAsc(section.getId());

        for (BomInstanceItem subSystem : subSystems) {

            BomInstanceSectionTarbDto instanceSectionTarbDto = new BomInstanceSectionTarbDto();

            instanceSectionTarbDto.setSection(section);
            instanceSectionTarbDto.setSubsystem(subSystem);

            List<BomInstanceItem> parts = new ArrayList<>();
            parts = getSubSystemParts(subSystem, parts);

            instanceSectionTarbDto.getSectionParts().addAll(parts);

            bomInstanceTarbDto.getSectionTarbDtoList().add(instanceSectionTarbDto);
        }

        return bomInstanceTarbDto;
    }

    private List<BomInstanceItem> getSubSystemParts(BomInstanceItem subSystem, List<BomInstanceItem> bomInstanceItems) {

        List<BomInstanceItem> instanceItems = bomInstanceItemRepository.findByParentOrderByCreatedDateAsc(subSystem.getId());

        for (BomInstanceItem instanceItem : instanceItems) {
            if (instanceItem.getBomItemType().equals(BomItemType.PART)) {

                List<BomItemInstance> bomItemInstances = bomItemInstanceRepository.findByBomInstanceItem(instanceItem.getId());

                bomItemInstances.forEach(bomItemInstance -> {
                    instanceItem.getIssuedInstances().add(itemInstanceRepository.findOne(bomItemInstance.getItemInstance()));
                });
                ItemType itemType = itemTypeRepository.findOne(instanceItem.getItem().getItemMaster().getItemType().getParentType());

                if (!itemType.getParentNode()) {
                    instanceItem.getItem().getItemMaster().setParentType(itemType);
                } else {
                    instanceItem.getItem().getItemMaster().setParentType(instanceItem.getItem().getItemMaster().getItemType());
                }
                bomInstanceItems.add(instanceItem);
            } else {
                bomInstanceItems = getSubSystemParts(instanceItem, bomInstanceItems);
            }
        }

        return bomInstanceItems;
    }

    @Transactional(readOnly = true)
    public BomInstanceItem findBomInstanceItemByItemInstance(Integer id) {
        BomItemInstance bomItemInstance = bomItemInstanceRepository.findByItemInstance(id);
        BomInstanceItem bomInstanceItem = bomInstanceItemRepository.findOne(bomItemInstance.getBomInstanceItem());
        return bomInstanceItem;
    }

    @Transactional(readOnly = true)
    public List<SectionRequestedItemsDto> getInstanceRequestedItems(Integer instanceId) {

        BomInstance bomInstance = bomInstanceRepository.findOne(instanceId);

        List<SectionRequestedItemsDto> requestedItemsDtos = new ArrayList<>();

        List<BomInstanceItem> sections = bomInstanceItemRepository.findByBomOrderByCreatedDateAsc(instanceId);

        for (BomInstanceItem section : sections) {
            SectionRequestedItemsDto sectionRequestedItemsDto = new SectionRequestedItemsDto();
            sectionRequestedItemsDto.setSection(section.getTypeRef());

            List<BomInstanceItem> bomInstanceItems = new ArrayList<>();

            bomInstanceItems = visitSectionChildren(section, bomInstanceItems);

            bomInstanceItems.forEach(bomInstanceItem -> {
                List<RequestItem> requestItems = requestItemRepository.getNotRejectedRequestItems(bomInstanceItem.getId(), instanceId, RequestItemStatus.REJECTED);

                List<IssueItem> issueItems = issueItemRepository.getReceivedItemsByBomInstanceItem(bomInstanceItem.getId());

                requestItems.forEach(requestItem -> {
                    if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                        bomInstanceItem.setFractionalRequestedQuantity(bomInstanceItem.getFractionalRequestedQuantity() + requestItem.getFractionalQuantity());
                    } else {
                        bomInstanceItem.setRequestedQuantity(bomInstanceItem.getRequestedQuantity() + requestItem.getQuantity());
                    }
                });


                issueItems.forEach(issueItem -> {
                    if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                        bomInstanceItem.setIssuedQuantity(bomInstanceItem.getIssuedQuantity() + issueItem.getFractionalQuantity());
                    } else {
                        bomInstanceItem.setIssuedQuantity(bomInstanceItem.getIssuedQuantity() + issueItem.getQuantity().doubleValue());
                    }
                });

                ItemAllocation itemAllocation = itemAllocationRepository.findByBomInstanceAndBomInstanceItem(instanceId, bomInstanceItem.getId());
                if (itemAllocation != null) {
                    bomInstanceItem.setFailedQuantity(itemAllocation.getFailedQty());
                }

                ItemType itemType = itemTypeRepository.findOne(bomInstanceItem.getItem().getItemMaster().getItemType().getParentType());

                if (!itemType.getParentNode()) {
                    bomInstanceItem.getItem().getItemMaster().setParentType(itemType);
                } else {
                    bomInstanceItem.getItem().getItemMaster().setParentType(bomInstanceItem.getItem().getItemMaster().getItemType());
                }
            });

            sectionRequestedItemsDto.getBomInstanceItems().addAll(bomInstanceItems);

            requestedItemsDtos.add(sectionRequestedItemsDto);

        }

        return requestedItemsDtos;
    }


    @Transactional(readOnly = true)
    public List<RequestedItemsDto> getInstanceSectionRequestedItems(Integer instanceId, Integer sectionId, String searchText) {

        List<RequestedItemsDto> requestedItemsDtos = new ArrayList<>();

        BomSearchCriteria bomSearchCriteria = new BomSearchCriteria();
        bomSearchCriteria.setSearchQuery(searchText);
        bomSearchCriteria.setBom(instanceId);
        bomSearchCriteria.setSection(sectionId);

        Pageable pageable1 = new PageRequest(0, 1000,
                new Sort(new Sort.Order(Sort.Direction.ASC, "createdDate")));

        Predicate predicate = bomInstanceItemSearchPredicateBuilder.build(bomSearchCriteria, QBomInstanceItem.bomInstanceItem);

        BomInstanceItem section = bomInstanceItemRepository.findOne(sectionId);

        Page<BomInstanceItem> bomInstanceItems = bomInstanceItemRepository.findAll(predicate, pageable1);

//        bomInstanceItems = visitSectionChildren(section, bomInstanceItems);

        bomInstanceItems.getContent().forEach(bomInstanceItem -> {

            RequestedItemsDto requestedItemsDto = new RequestedItemsDto();

            List<RequestItem> requestItems = requestItemRepository.getNotRejectedRequestItems(bomInstanceItem.getId(), instanceId, RequestItemStatus.REJECTED);

            List<IssueItem> issueItems = issueItemRepository.getReceivedItemsByBomInstanceItem(bomInstanceItem.getId());

            requestItems.forEach(requestItem -> {
                if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                    bomInstanceItem.setFractionalRequestedQuantity(bomInstanceItem.getFractionalRequestedQuantity() + requestItem.getFractionalQuantity());
                } else {
                    bomInstanceItem.setRequestedQuantity(bomInstanceItem.getRequestedQuantity() + requestItem.getQuantity());
                }
            });


            issueItems.forEach(issueItem -> {
                if (bomInstanceItem.getItem().getItemMaster().getItemType().getHasLots()) {
                    bomInstanceItem.setIssuedQuantity(bomInstanceItem.getIssuedQuantity() + issueItem.getFractionalQuantity());
                } else {
                    bomInstanceItem.setIssuedQuantity(bomInstanceItem.getIssuedQuantity() + issueItem.getQuantity().doubleValue());
                }
            });

            ItemAllocation itemAllocation = itemAllocationRepository.findByBomInstanceAndBomInstanceItem(instanceId, bomInstanceItem.getId());
            if (itemAllocation != null) {
                bomInstanceItem.setFailedQuantity(itemAllocation.getFailedQty());
            }

            ItemType itemType = itemTypeRepository.findOne(bomInstanceItem.getItem().getItemMaster().getItemType().getParentType());

            if (!itemType.getParentNode()) {
                bomInstanceItem.getItem().getItemMaster().setParentType(itemType);
            } else {
                bomInstanceItem.getItem().getItemMaster().setParentType(bomInstanceItem.getItem().getItemMaster().getItemType());
            }

            requestedItemsDto.setItem(bomInstanceItem);

            List<RequestItem> requestItems1 = requestItemRepository.getRequestItemsByItem(bomInstanceItem.getId());

            HashMap<Integer, Request> requestHashMap = new HashMap<Integer, Request>();

            requestItems1.forEach(requestItem -> {
                Request request = requestHashMap.get(requestItem.getRequest().getId());
                if (request == null) {
                    requestedItemsDto.getRequests().add(requestItem.getRequest());
                    requestHashMap.put(requestItem.getRequest().getId(), requestItem.getRequest());
                }
            });


            requestedItemsDtos.add(requestedItemsDto);
        });

        return requestedItemsDtos;
    }


    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void importBOM(Integer parentId, MultipartHttpServletRequest request) throws Exception {
        Bom bom = bomRepository.findOne(parentId);
        Item system = itemRepository.findByLatestRevision(bom.getItem().getId());

        for (MultipartFile file1 : request.getFileMap().values()) {
            java.io.File file = ImportConverter.trimAndConvertMultipartFileToFile(file1);
            if (file != null && file.getName().trim().endsWith(".xls") || file.getName().trim().endsWith(".xlsx")) {
                Map<String, BomGroup> bomGroupMap = new HashMap();
                Map<String, BomItem> bomItemMap = new HashMap();
                Workbook workbook = WorkbookFactory.create(file);
                int totalSheets = workbook.getNumberOfSheets();
                for (int j = 0; j < totalSheets; j++) {
                    int i = 0;
                    Sheet worksheet = workbook.getSheetAt(j);
                    int lastRow = worksheet.getLastRowNum();
                    while (i <= lastRow) {
                        Row row = worksheet.getRow(i++);
                        if (row != null && row.getLastCellNum() > 0 && !isRowEmpty(row)) {
                            if (row.getRowNum() == 0) continue;

                            if (row.getCell(2) != null && row.getCell(2).getStringCellValue() != null && row.getCell(2).getStringCellValue() != "") {
                                String type = row.getCell(2).getStringCellValue().toUpperCase();
                                if (type.equals("SECTION")) {
                                    BomGroup bomGroup = new BomGroup();
                                    bomGroup.setType(BomItemType.valueOf(type));
                                    if (row.getCell(0) != null && row.getCell(0).getStringCellValue() != null && row.getCell(0).getStringCellValue() != "") {
                                        bomGroup.setName(row.getCell(0).getStringCellValue());
                                    }
                                    if (row.getCell(3) != null) {
                                        String code = row.getCell(3).getStringCellValue().toUpperCase();
                                        if (code.length() > 1) {
                                            int lineNumber = row.getRowNum() + 1;
                                            throw new CassiniException("Please provide the code is only one digit at row number " + lineNumber);
                                        }
                                        bomGroup.setCode(code);
                                    }
                                    BomGroup existGroup = bomGroupRepository.findByTypeAndCodeAndVersity(BomItemType.valueOf("SECTION"), bomGroup.getCode(), bomGroup.getVersity());
                                    bomGroup = existGroup == null ? bomGroupRepository.save(bomGroup) : existGroup;

                                    BomItem bomItem = new BomItem();
                                    bomItem.setBom(parentId);
                                    bomItem.setBomItemType(BomItemType.SECTION);
                                    bomItem.setTypeRef(bomGroup);
                                    bomItem.setHierarchicalCode(system.getItemCode());
                                    BomItem existBomItem = bomItemRepository.findByBomAndTypeRef(parentId, bomGroup);
                                    if (existBomItem != null)
                                        throw new Exception(bomGroup.getName() + " already exist in this system");
                                    bomItem = bomItemRepository.save(bomItem);
                                    bomItemMap.clear();
                                    bomItemMap.put("SECTION", bomItem);
                                    bomGroupMap.clear();
                                    bomGroupMap.put("SECTION", bomGroup);

                                } else if (type.equals("SUBSYSTEM")) {
                                    BomGroup bomGroup = new BomGroup();
                                    bomGroup.setType(BomItemType.valueOf(type));
                                    if (row.getCell(0) != null && row.getCell(0).getStringCellValue() != null && row.getCell(0).getStringCellValue() != "") {
                                        bomGroup.setName(row.getCell(0).getStringCellValue());
                                    }
                                    if (row.getCell(3) != null) {
                                        String code = row.getCell(3).getStringCellValue().toUpperCase();
                                        if (code.length() > 1) {
                                            int lineNumber = row.getRowNum() + 1;
                                            throw new CassiniException("Please provide the code is only one digit at row number " + lineNumber);
                                        }
                                        bomGroup.setCode(code);
                                    }

                                    BomGroup existGroup = bomGroupRepository.findByTypeAndCode(BomItemType.valueOf("SUBSYSTEM"), bomGroup.getCode());
                                    bomGroup = existGroup == null ? bomGroupRepository.save(bomGroup) : existGroup;

                                    bomGroupMap.remove("UNIT");
                                    bomGroupMap.put("SUBSYSTEM", bomGroup);

                                    BomItem bomItem = new BomItem();
                                    if (bomItemMap.containsKey("SECTION")) {
                                        bomItem.setParent(bomItemMap.get("SECTION").getId());
                                    }
                                    if (bomGroupMap.containsKey("SECTION")) {
                                        bomItem.setHierarchicalCode(system.getItemCode() + "" + bomGroupMap.get("SECTION").getCode());
                                    }

                                    bomItem.setBomItemType(BomItemType.SUBSYSTEM);
                                    bomItem.setTypeRef(bomGroup);

                                    bomItem = bomItemRepository.save(bomItem);
                                    bomItemMap.clear();
                                    bomItemMap.put("SUBSYSTEM", bomItem);

                                } else if (type.equals("UNIT")) {
                                    BomGroup bomGroup = new BomGroup();
                                    bomGroup.setType(BomItemType.valueOf(type));
                                    if (row.getCell(0) != null && row.getCell(0).getStringCellValue() != null && row.getCell(0).getStringCellValue() != "") {
                                        bomGroup.setName(row.getCell(0).getStringCellValue());
                                    }
                                    if (row.getCell(3) != null) {
                                        String code = row.getCell(3).getStringCellValue().toUpperCase();
                                        if (code.length() != 2) {
                                            int lineNumber = row.getRowNum() + 1;
                                            throw new CassiniException("Please provide the two digit code at row number " + lineNumber);
                                        }
                                        bomGroup.setCode(code);
                                    }

                                    BomGroup existGroup = bomGroupRepository.findByTypeAndCode(BomItemType.valueOf("UNIT"), bomGroup.getCode());
                                    bomGroup = existGroup == null ? bomGroupRepository.save(bomGroup) : existGroup;

                                    bomGroupMap.put("UNIT", bomGroup);
                                    BomItem bomItem = new BomItem();
                                    if (bomItemMap.containsKey("SUBSYSTEM")) {
                                        bomItem.setParent(bomItemMap.get("SUBSYSTEM").getId());
                                    }
                                    if (bomGroupMap.containsKey("SECTION") && bomGroupMap.containsKey("SUBSYSTEM")) {
                                        bomItem.setHierarchicalCode(system.getItemCode() + "" + bomGroupMap.get("SECTION").getCode() + "" + bomGroupMap.get("SUBSYSTEM").getCode());
                                    }

                                    bomItem.setBomItemType(BomItemType.UNIT);
                                    bomItem.setTypeRef(bomGroup);

                                    bomItem = bomItemRepository.save(bomItem);
                                    bomItemMap.put("UNIT", bomItem);

                                } else {
                                    Item item1 = null;
                                    Item item = new Item();
                                    BomItem bomItem = new BomItem();
                                    ItemType itemType = itemTypeRepository.findByNameIgnoreCase(type);
                                    item.setItemName(row.getCell(1).getStringCellValue());
                                    item.setItemType(itemType);
                                    if (row.getCell(7) != null && row.getCell(7).getStringCellValue() != null && row.getCell(7).getStringCellValue() != "") {
                                        item.setDrawingNumber(row.getCell(7).getStringCellValue());
                                    }
                                    item.setItemCode(itemType.getTypeCode());
                                    String autoNumber = autoNumberService.getNextNumber(itemType.getItemNumberSource().getId());
                                    item.setItemNumber(autoNumber);

                                    if (itemType.getHasSpec().equals(Boolean.TRUE)) {
                                        if (row.getCell(4) != null && row.getCell(4).getStringCellValue() != null && row.getCell(4).getStringCellValue() != "") {
                                            String spec = row.getCell(4).getStringCellValue().toUpperCase().trim().replaceAll("//s", "");
                                            ItemTypeSpecs itemTypeSpecs = itemTypeSpecsRepository.findByItemTypeAndSpecName(itemType.getId(), spec);
                                            item1 = itemRepository.findByItemCodeAndPartSpec(item.getItemCode(), itemTypeSpecs);
                                            if (itemTypeSpecs == null) {
                                                int lineNumber = row.getRowNum() + 1;
                                                throw new CassiniException("Provided spec is not available at " + lineNumber);
                                            }
                                            item.setPartSpec(itemTypeSpecs);
                                        } else {
                                            int lineNumber = row.getRowNum() + 1;
                                            throw new CassiniException("Please provide specification at row number " + lineNumber);
                                        }
                                    } else {
                                        item1 = itemRepository.findByItemCodeAndPartSpec(item.getItemCode(), null);
                                    }

                                    if (row.getCell(7) != null && row.getCell(7).getStringCellValue() != null && row.getCell(7).getStringCellValue() != "") {
                                        item.setDrawingNumber(row.getCell(7).getStringCellValue());
                                    }
                                    if (row.getCell(8) != null && row.getCell(8).getStringCellValue() != null && row.getCell(8).getStringCellValue() != "") {
                                        item.setMaterial(row.getCell(8).getStringCellValue());
                                    }

                                    bomItem.setNewItem(item);
                                    if (bomItemMap.get("UNIT") != null) {
                                        bomItem.setParent(bomItemMap.get("UNIT").getId());
                                    } else if (bomItemMap.get("SUBSYSTEM") != null) {
                                        bomItem.setParent(bomItemMap.get("SUBSYSTEM").getId());
                                    }

                                    if (!item.getItemType().getHasLots()) {
                                        if (row.getCell(6) != null && row.getCell(6).getStringCellValue() != null && row.getCell(6).getStringCellValue() != "") {
                                            bomItem.setQuantity(Integer.parseInt(row.getCell(6).getStringCellValue()));
                                        }
                                    } else if (item.getItemType().getHasLots()) {
                                        if (row.getCell(6) != null && row.getCell(6).getStringCellValue() != null && row.getCell(6).getStringCellValue() != "") {
                                            bomItem.setFractionalQuantity(Double.parseDouble(row.getCell(6).getStringCellValue()));
                                        }
                                    }
                                    if (row.getCell(9) != null && row.getCell(9).getStringCellValue() != null && row.getCell(9).getStringCellValue() != "") {
                                        bomItem.setWorkCenter(row.getCell(9).getStringCellValue());
                                    }
                                    bomItem.setBomItemType(BomItemType.PART);
                                    if (bomItemMap.containsKey("UNIT")) {
                                        bomItem.setParent(bomItemMap.get("UNIT").getId());
                                    } else if (bomItemMap.containsKey("SECTION")) {
                                        bomItem.setParent(bomItemMap.get("SECTION").getId());
                                    }
                                    String hierarchicalCode = "";
                                    if (bomGroupMap.containsKey("UNIT")) {
                                        hierarchicalCode = system.getItemCode() + bomGroupMap.get("SECTION").getCode() + "" + bomGroupMap.get("SUBSYSTEM").getCode() + "" + bomGroupMap.get("UNIT").getCode() + "" + item.getItemType().getTypeCode();
                                    } else {
                                        hierarchicalCode = system.getItemCode() + bomGroupMap.get("SECTION").getCode() + "" + bomGroupMap.get("SUBSYSTEM").getCode() + "00" + item.getItemType().getTypeCode();
                                    }
                                    bomItem.setHierarchicalCode(hierarchicalCode);

                                    if (item1 == null) {
                                        createNewBomItem(parentId, bomItem);
                                    } else {
                                        ItemRevision itemRevision = itemRevisionRepository.findOne(item1.getLatestRevision());
                                        bomItem.setItem(itemRevision);
                                        createBomItem(parentId, bomItem);
                                    }

                                }

                            } else {
                                int lineNumber = row.getRowNum() + 1;
                                throw new CassiniException("Please provide the Part Type for the row " + lineNumber);
                            }
                        }

                    }
                }

                workbook.close();

            } else {
                throw new CassiniException("Please upload excel sheet with proper Name & Code data");
            }
        }

    }

    private String getSpecString(Item item) {
        String tempSpec = "";
        if (item.getPartSpec() != null) {
            int len = item.getPartSpec().getSpecName().length();
            if (len == 3) {
                tempSpec = "0" + item.getPartSpec().getSpecName();
            } else if (len == 2) {
                tempSpec = "00" + item.getPartSpec().getSpecName();
            } else if (len == 1) {
                tempSpec = "000" + item.getPartSpec().getSpecName();
            } else if (len == 0) {
                tempSpec = "0000";
            }
        } else {
            tempSpec = "0000";
        }

        return tempSpec;
    }

    @Transactional(readOnly = true)
    public List<SectionBomItemsDto> getItemsByBomAndWorkCenter(BomSearchCriteria criteria, Pageable pageable) {
        List<SectionBomItemsDto> sectionBomItemsDtos = new ArrayList<>();
        HashMap<Integer, SectionBomItemsDto> sectionMap = new HashMap<>();
        List<BomItem> items = new ArrayList<>();

        Predicate predicate = workCenterPredicateBuilder.build(criteria, QBomItem.bomItem);

        if (predicate != null) {
            Page<BomItem> bomItems = bomItemRepository.findAll(predicate, pageable);

            for (BomItem bomItem : bomItems.getContent()) {
                BomItem section = null;
                BomItem parent = bomItemRepository.findOne(bomItem.getParent());
                if (parent.getBomItemType().equals(BomItemType.UNIT)) {
                    BomItem parent1 = bomItemRepository.findOne(parent.getParent());
                    if (parent1.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                        BomItem parent2 = bomItemRepository.findOne(parent1.getParent());
                        if (parent2.getBomItemType().equals(BomItemType.SECTION) || parent2.getBomItemType().equals(BomItemType.COMMONPART)) {
                            section = parent2;
                        }
                    } else {
                        section = parent1;
                    }
                } else if (parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                    BomItem parent1 = bomItemRepository.findOne(parent.getParent());
                    section = parent1;
                } else if (parent.getBomItemType().equals(BomItemType.COMMONPART)) {
                    section = parent;
                }

                ItemType itemType = itemTypeRepository.findOne(bomItem.getItem().getItemMaster().getItemType().getParentType());

                if (!itemType.getParentNode()) {
                    bomItem.getItem().getItemMaster().setParentType(itemType);
                } else {
                    bomItem.getItem().getItemMaster().setParentType(bomItem.getItem().getItemMaster().getItemType());
                }

                if (section != null) {
                    Bom bom = bomRepository.findOne(criteria.getBom());

                    List<Inward> inwards = inwardRepository.findInwardsByBom(bom.getId());

                    inwards.forEach(inward -> {
                        List<InwardItem> inwardItems = inwardItemRepository.findByInwardAndBomItem(inward.getId(), bomItem.getId());

                        inwardItems.forEach(inwardItem -> {
                            if (bomItem.getItem().getItemMaster().getItemType().getHasLots()) {
                                bomItem.setFractionalInwardQty(inwardItem.getFractionalQuantity() + bomItem.getFractionalInwardQty());
                            } else {
                                bomItem.setQuantity(inwardItem.getQuantity() + bomItem.getInwardQty());
                            }
                        });
                    });

                    SectionBomItemsDto sectionBomItemsDto = sectionMap.containsKey(section.getId()) ? sectionMap.get(section.getId()) : new SectionBomItemsDto();

                    sectionBomItemsDto.setSection(section);
                    sectionBomItemsDto.getBomItems().add(bomItem);

                    sectionMap.put(section.getId(), sectionBomItemsDto);
                }
            }
        }

        for (SectionBomItemsDto sectionBomItemsDto : sectionMap.values()) {
            sectionBomItemsDtos.add(sectionBomItemsDto);
        }

        if (sectionBomItemsDtos.size() > 0) {
            Collections.sort(sectionBomItemsDtos, new Comparator<SectionBomItemsDto>() {
                public int compare(final SectionBomItemsDto object1, final SectionBomItemsDto object2) {
                    return object1.getSection().getId().compareTo(object2.getSection().getId());
                }
            });
        }


        return sectionBomItemsDtos;
    }


    @Transactional(readOnly = true)
    public List<BomItem> searchBom(Integer bomId, String searchText, Boolean versity) {
        BomSearchCriteria bomSearchCriteria = new BomSearchCriteria();
        bomSearchCriteria.setBom(bomId);
        bomSearchCriteria.setSearchQuery(searchText);
        bomSearchCriteria.setVersity(versity);
        Predicate predicate = bomSearchPredicateBuilder.build(bomSearchCriteria, QBomItem.bomItem);

        Pageable pageable1 = new PageRequest(0, 1000,
                new Sort(new Sort.Order(Sort.Direction.ASC, "createdDate")));

        List<BomItem> bomItemList = new ArrayList<>();
        Page<BomItem> bomItems = null;
        if (predicate != null) {
            Bom bom = bomRepository.findOne(bomSearchCriteria.getBom());
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
                    section.setLevel(1);
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

            return bomItemList;
        } else {
            return bomItemList;
        }
    }

    @Transactional(readOnly = true)
    public List<BomItem> findSectionByBomId(Integer id) {
        List<BomItem> bomItems = bomItems = bomItemRepository.findByBomOrderByCreatedDateAsc(id);
        return bomItems;
    }

}