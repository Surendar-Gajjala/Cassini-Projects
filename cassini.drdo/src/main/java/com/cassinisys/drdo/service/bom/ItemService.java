package com.cassinisys.drdo.service.bom;

import com.cassinisys.drdo.filtering.ItemSearchCriteria;
import com.cassinisys.drdo.filtering.ItemSearchPredicateBuilder;
import com.cassinisys.drdo.model.DRDOObjectType;
import com.cassinisys.drdo.model.bom.*;
import com.cassinisys.drdo.model.dto.ItemReportDto;
import com.cassinisys.drdo.model.inventory.Storage;
import com.cassinisys.drdo.model.transactions.*;
import com.cassinisys.drdo.repo.bom.*;
import com.cassinisys.drdo.repo.inventory.StorageRepository;
import com.cassinisys.drdo.repo.transactions.*;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.*;
import com.cassinisys.platform.repo.core.AutoNumberRepository;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.mysema.query.types.Predicate;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by subra on 04-10-2018.
 */
@Service
public class ItemService implements CrudService<Item, Integer> {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private SessionWrapper sessionWrapper;

    @Autowired
    private ItemRevisionRepository itemRevisionRepository;

    @Autowired
    private ItemTypeRepository itemTypeRepository;

    @Autowired
    private ItemTypeAttributeRepository itemTypeAttributeRepository;

    @Autowired
    private ItemAttributeValueRepository itemAttributeValueRepository;

    @Autowired
    private ItemRevisionAttributeValueRepository itemRevisionAttributeValueRepository;

    @Autowired
    private ItemInstanceStatusHistoryRepository itemInstanceStatusHistoryRepository;

    @Autowired
    private ItemSearchPredicateBuilder itemSearchPredicateBuilder;

    @Autowired
    private BomRepository bomRepository;

    @Autowired
    private BomItemRepository bomItemRepository;

    @Autowired
    private AutoNumberRepository autoNumberRepository;

    @Autowired
    private ItemInstanceRepository itemInstanceRepository;

    @Autowired
    private ItemTypeService itemTypeService;

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;

    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;

    @Autowired
    private LotInstanceHistoryRepository lotInstanceHistoryRepository;

    @Autowired
    private LotInstanceRepository lotInstanceRepository;

    @Autowired
    private BomItemInstanceRepository bomItemInstanceRepository;

    @Autowired
    private InwardRepository inwardRepository;

    @Autowired
    private InwardItemRepository inwardItemRepository;

    @Autowired
    private InwardItemInstanceRepository inwardItemInstanceRepository;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private IssueItemRepository issueItemRepository;

    @Autowired
    private BomGroupRepository bomGroupRepository;

    @Override
    @Transactional(readOnly = false)
    public Item create(Item item) {

        checkNotNull(item);

        String drawingNumber = item.getDrawingNumber();

        item.setId(null);

        Map<String, Item> itemCodeMap = new HashMap<>();

        if (item.getItemType().getHasSpec()) {
            List<Item> itemList = itemTypeService.getItemWithTypeParentNode(item.getItemType().getId());

            itemList.forEach(item1 -> {
                if (item1.getPartSpec() != null) {
                    itemCodeMap.put(item1.getItemCode() + "" + item1.getPartSpec().getSpecName(), item1);
                }
            });

            Item existItem = itemCodeMap.get(item.getItemCode() + "" + item.getPartSpec().getSpecName());

            if (existItem != null) {
                throw new CassiniException(existItem.getItemName() + " with Item code (" + item.getItemCode() + ")specification " + " - " + item.getPartSpec().getSpecName() + " already exist");
            }
            item.setItemName(item.getItemName() + " - " + item.getPartSpec().getSpecName());
        } else {
            List<Item> itemList = itemTypeService.getItemWithTypeParentNode(item.getItemType().getId());

            itemList.forEach(item1 -> {
                itemCodeMap.put(item1.getItemCode(), item1);
            });

            Item existItem = itemCodeMap.get(item.getItemCode());

            if (existItem != null) {
                throw new CassiniException(existItem.getItemName() + " with Item code ( " + item.getItemCode() + ") already exist");
            }
        }

        AutoNumber autoNumber = autoNumberRepository.findOne(item.getItemType().getItemNumberSource().getId());
        String itemNumber = autoNumber.next();

        autoNumber = autoNumberRepository.save(autoNumber);

        item.setItemNumber(itemNumber);
        item.setItemCode(item.getItemCode().toUpperCase());
        item = itemRepository.save(item);

        ItemRevision revision = new ItemRevision();
        revision.setItemMaster(item);
        revision.setObjectType(DRDOObjectType.ITEMREVISION);
        ItemType plmItemType = itemTypeRepository.findOne(item.getItemType().getId());
        Lov lov = plmItemType.getRevisionSequence();

        revision.setRevision(lov.getDefaultValue());
        revision.setHasBom(false);
        revision.setHasFiles(false);
        revision.setDrawingNumber(drawingNumber);
        ItemRevision itemRevision1 = itemRevisionRepository.save(revision);
        item.setLatestRevision(itemRevision1.getId());
        item = itemRepository.save(item);

        ItemType itemType = itemTypeService.getParentType(item.getItemType());

        if (itemType.getParentNode() && itemType.getName().equals("System")) {
            Bom bom = new Bom();
            bom.setItem(revision);

            bom = bomRepository.save(bom);

            BomGroup bomGroup = bomGroupRepository.findByTypeAndNameAndVersity(BomItemType.COMMONPART, "Common Parts", false);
            BomItem bomItem = bomItemRepository.findByBomAndTypeRef(bom.getId(), bomGroup);
            if (bomItem == null) {
                bomItem = new BomItem();
                bomItem.setBom(bom.getId());
                bomItem.setTypeRef(bomGroup);
                bomItem.setBomItemType(BomItemType.COMMONPART);
                bomItem.setHierarchicalCode(bom.getItem().getItemMaster().getItemCode());

                bomItem = bomItemRepository.save(bomItem);
            }

            /*Storage storage = new Storage();
            storage.setName(item.getItemName() + " On Hold");
            storage.setDescription("Default " + item.getItemName() + " On Hold Store");
            storage.setOnHold(true);
            storage.setType(StorageType.WAREHOUSE);
            storage.setBom(bom);

            storage = storageRepository.save(storage);

            storage = new Storage();
            storage.setName(item.getItemName() + " Return");
            storage.setDescription("Default " + item.getItemName() + " Return Store");
            storage.setType(StorageType.WAREHOUSE);
            storage.setReturned(true);
            storage.setBom(bom);

            storage = storageRepository.save(storage);*/
        } else {
            if (item.getItemType().getHasBom()) {
                Bom bom = new Bom();
                bom.setItem(revision);

                bom = bomRepository.save(bom);
            }
        }

        return item;
    }

    @Override
    @Transactional(readOnly = false)
    public Item update(Item item) {

        Item existItem = itemRepository.findByItemTypeAndItemNameEqualsIgnoreCase(item.getItemType(), item.getItemName());
        if (existItem != null && !existItem.getId().equals(item.getId())) {
            throw new CassiniException(item.getItemName() + " : Nomenclature already exist");
        }

        if (item.getDrawingNumber() != null) {
            ItemRevision itemRevision = itemRevisionRepository.findOne(item.getLatestRevision());
            itemRevision.setDrawingNumber(item.getDrawingNumber());

            itemRevision = itemRevisionRepository.save(itemRevision);
        }

        return itemRepository.save(item);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {

        Item item = itemRepository.findOne(id);
        ItemRevision itemRevision = itemRevisionRepository.findOne(item.getLatestRevision());

        Bom bom = bomRepository.findByItem(itemRevision);
        if (bom != null) {
            item.getBomItems().addAll(bomItemRepository.findByBomOrderByCreatedDateAsc(bom.getId()));
        }


        List<BomItem> itemCreatedAsBomItem = bomItemRepository.findByItem(itemRevision);
        if (itemCreatedAsBomItem.size() > 0) {
            item.setCreatedAsBomItem(true);
        }

        if (item.getBomItems().size() > 0) {
            throw new CassiniException("Item created has Bom Item. You cannot delete this item");
        }

        if (item.getCreatedAsBomItem()) {
            throw new CassiniException("Item created has Bom Item. You cannot delete this item");
        }

        itemRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Item get(Integer id) {
        return itemRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Item> getAll() {
        return itemRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Item> getAllItems(Pageable pageable) {

        Page<Item> items = itemRepository.findAll(pageable);

        for (Item item : items.getContent()) {

            ItemRevision itemRevision = itemRevisionRepository.findOne(item.getLatestRevision());

            Bom bom = bomRepository.findByItem(itemRevision);
            if (bom != null) {
                item.getBomItems().addAll(bomItemRepository.findByBomOrderByCreatedDateAsc(bom.getId()));
            }


            List<BomItem> itemCreatedAsBomItem = bomItemRepository.findByItem(itemRevision);
            if (itemCreatedAsBomItem.size() > 0) {
                item.setCreatedAsBomItem(true);
            }

        }

        return items;
    }

    @Transactional(readOnly = true)
    public List<ItemRevision> getRevisionsByIds(List<Integer> ids) {
        List<ItemRevision> itemRevisions = itemRevisionRepository.findByIdIn(ids);
        itemRevisions.forEach(itemRevision -> {
            itemRevision.getItemMaster().getItemType().setParentNodeItemType(itemTypeService.getParentType(itemRevision.getItemMaster().getItemType()).getName());
        });
        return itemRevisions;
    }

    @Transactional
    public Item uploadThumbnailImage(Item item) {
        return itemRepository.save(item);
    }

    @Transactional
    public Item saveImageAttributeValue(Integer objectId, Integer attributeId, Map<String, MultipartFile> fileMap) {
        Item item = itemRepository.findOne(objectId);

        ItemRevision itemRevision = itemRevisionRepository.findOne(objectId);


        if (item != null) {
            ItemAttributeValue itemAttribute = new ItemAttributeValue();
            itemAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            List<MultipartFile> files = new ArrayList<>(fileMap.values());
            if (files.size() > 0) {
                MultipartFile file = files.get(0);
                try {
                    itemAttribute.setImageValue(file.getBytes());
                    itemAttribute = itemAttributeValueRepository.save(itemAttribute);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        if (itemRevision != null) {
            ItemRevisionAttributeValue itemRevisionAttribute = new ItemRevisionAttributeValue();
            itemRevisionAttribute.setId(new ObjectAttributeId(objectId, attributeId));

            List<MultipartFile> files = new ArrayList<>(fileMap.values());
            if (files.size() > 0) {
                MultipartFile file = files.get(0);
                try {
                    itemRevisionAttribute.setImageValue(file.getBytes());
                    itemRevisionAttribute = itemRevisionAttributeValueRepository.save(itemRevisionAttribute);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }

        return item;
    }

    @Transactional(readOnly = true)
    public void getImageItem(Integer itemId, HttpServletResponse response) {
        Item plmItem = itemRepository.findOne(itemId);
        if (plmItem != null) {
            InputStream is = new ByteArrayInputStream(plmItem.getThumbnail());
            try {
                IOUtils.copy(is, response.getOutputStream());
                response.flushBuffer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional
    public List<ObjectAttribute> saveItemAttributes(List<ItemAttributeValue> attributes) {
        List<ObjectAttribute> objectAttributes = new ArrayList();
        for (ItemAttributeValue attribute : attributes) {
            if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null ||
                    attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                    attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null ||
                    attribute.getStringValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {
                ItemTypeAttribute itemTypeAttribute = itemTypeAttributeRepository.findOne(attribute.getId().getAttributeDef());
                if (itemTypeAttribute.getRevisionSpecific().equals(true)) {
                    ItemRevision plmItemRevision = itemRevisionRepository.findOne(attribute.getId().getObjectId());
                    ItemRevisionAttributeValue revisionAttribute1 = new ItemRevisionAttributeValue();
                    revisionAttribute1.setId(new ObjectAttributeId(plmItemRevision.getId(), itemTypeAttribute.getId()));
                    revisionAttribute1.setStringValue(attribute.getStringValue());
                    revisionAttribute1.setIntegerValue(attribute.getIntegerValue());
                    revisionAttribute1.setBooleanValue(attribute.getBooleanValue());
                    revisionAttribute1.setDoubleValue((attribute.getDoubleValue()));
                    revisionAttribute1.setDateValue(attribute.getDateValue());
                    revisionAttribute1.setTimeValue(attribute.getTimeValue());
                    revisionAttribute1.setAttachmentValues(attribute.getAttachmentValues());
                    revisionAttribute1.setRefValue(attribute.getRefValue());
                    revisionAttribute1.setCurrencyType(attribute.getCurrencyType());
                    revisionAttribute1.setCurrencyValue(attribute.getCurrencyValue());
                    revisionAttribute1.setTimestampValue(attribute.getTimestampValue());
//                    revisionAttribute1.setImageValue(null);
                    revisionAttribute1.setListValue(attribute.getListValue());
                    ItemRevisionAttributeValue revisionAttribute2 = itemRevisionAttributeValueRepository.save(revisionAttribute1);
                    objectAttributes.add(revisionAttribute2);
                } else if (itemTypeAttribute.getRevisionSpecific().equals(false)) {
                    ItemAttributeValue itemAttribute = new ItemAttributeValue();
                    Item plmItem1 = itemRepository.findOne(attribute.getId().getObjectId());
                    itemAttribute.setId(new ObjectAttributeId(plmItem1.getId(), attribute.getId().getAttributeDef()));
                    itemAttribute.setStringValue(attribute.getStringValue());
                    itemAttribute.setIntegerValue(attribute.getIntegerValue());
                    itemAttribute.setBooleanValue(attribute.getBooleanValue());
                    itemAttribute.setDoubleValue(attribute.getDoubleValue());
                    itemAttribute.setTimeValue(attribute.getTimeValue());
                    itemAttribute.setTimestampValue(attribute.getTimestampValue());
                    itemAttribute.setAttachmentValues(attribute.getAttachmentValues());
//                    itemAttribute.setImageValue(null);
                    itemAttribute.setDateValue(attribute.getDateValue());
                    itemAttribute.setCurrencyType(attribute.getCurrencyType());
                    itemAttribute.setCurrencyValue(attribute.getCurrencyValue());
                    itemAttribute.setRefValue(attribute.getRefValue());
                    itemAttribute.setListValue(attribute.getListValue());
                    ItemAttributeValue itemAttribute1 = itemAttributeValueRepository.save(itemAttribute);
                    objectAttributes.add(itemAttribute1);
                }
            }

        }
        return objectAttributes;

    }

    @Transactional(readOnly = true)
    public ItemRevision getRevision(Integer id) {
        checkNotNull(id);
        return itemRevisionRepository.findOne(id);
    }

    @Transactional(readOnly = false)
    public List<ItemAttributeValue> getItemAttributes(Integer itemId) {
        return itemAttributeValueRepository.findByItemId(itemId);
    }

    @Transactional(readOnly = false)
    public List<ItemRevisionAttributeValue> getItemRevisionAttributes(Integer revisionId) {
        return itemRevisionAttributeValueRepository.findByItemId(revisionId);
    }

    @Transactional(readOnly = false)
    public ItemAttributeValue createItemAttribute(ItemAttributeValue attribute) {
        return itemAttributeValueRepository.save(attribute);
    }

    @Transactional(readOnly = false)
    public ItemRevisionAttributeValue createItemRevisionAttribute(ItemRevisionAttributeValue attribute) {
        attribute = itemRevisionAttributeValueRepository.save(attribute);

        return attribute;
    }

    @Transactional(readOnly = false)
    public ItemAttributeValue updateItemAttribute(ItemAttributeValue attribute) {
        attribute = itemAttributeValueRepository.save(attribute);

        return attribute;
    }

    @Transactional(readOnly = false)
    public ItemRevisionAttributeValue updateItemRevisionAttribute(ItemRevisionAttributeValue attribute) {
        attribute = itemRevisionAttributeValueRepository.save(attribute);

        return attribute;
    }

    @Transactional(readOnly = true)
    public Item getItemByName(String name) {
        return itemRepository.findByItemName(name);
    }

    @Transactional(readOnly = true)
    public Page<Item> getItemSearchResults(ItemSearchCriteria itemSearchCriteria, Pageable pageable) {
        Predicate predicate = itemSearchPredicateBuilder.build(itemSearchCriteria, QItem.item);

        Page<Item> items = itemRepository.findAll(predicate, pageable);
        items.getContent().forEach(item -> {
            item.getItemType().setParentNodeItemType(itemTypeService.getParentType(item.getItemType()).getName());

            if (item.getItemType().getParentType() != null) {
                ItemType itemType = itemTypeRepository.findOne(item.getItemType().getParentType());

                if (!itemType.getParentNode()) {
                    item.setParentType(itemType);
                } else {
                    item.setParentType(item.getItemType());
                }
            }

            ItemRevision itemRevision = itemRevisionRepository.findOne(item.getLatestRevision());

            Bom bom = bomRepository.findByItem(itemRevision);
            if (bom != null) {
                item.getBomItems().addAll(bomItemRepository.findByBomOrderByCreatedDateAsc(bom.getId()));
            }


            List<BomItem> itemCreatedAsBomItem = bomItemRepository.findByItem(itemRevision);
            if (itemCreatedAsBomItem.size() > 0) {
                item.setCreatedAsBomItem(true);
            }
        });

        return items;
    }

    public Map<Integer, List<ObjectAttribute>> getObjectAttributesByItemIdsAndAttributeIds(Integer[] objectIds, Integer[] objectAttributeIds) {

        Map<Integer, List<ObjectAttribute>> objectAttributesMap = new HashMap();

        List<ObjectAttribute> attributes = objectAttributeRepository.findByObjectIdsInAndAttributeDefIdsIn(objectIds, objectAttributeIds);

        for (ObjectAttribute attribute : attributes) {
            Integer id = attribute.getId().getObjectId();
            List<ObjectAttribute> objectAttributes = objectAttributesMap.get(id);
            if (objectAttributes == null) {
                objectAttributes = new ArrayList<>();
                objectAttributesMap.put(id, objectAttributes);
            }

            objectAttributes.add(attribute);
        }

        return objectAttributesMap;

    }

    @Transactional(readOnly = true)
    public List<ItemInstance> getItemInstancesFrom20(Integer itemId) {

        ItemRevision itemRevision = itemRevisionRepository.findOne(itemId);
        List<ItemInstance> itemInstances = new ArrayList<>();
        if (itemRevision != null && !itemRevision.getItemMaster().getItemType().getParentNode()
                && !itemRevision.getItemMaster().getItemType().getName().equals("System")) {
            //Here we have to get instances from 20
            itemInstances = itemInstanceRepository.findByItem(itemId);

            itemInstances.forEach(itemInstance -> {
                ObjectTypeAttribute certificateNumberType = objectTypeAttributeRepository.findByNameAndObjectType("Certificate Number", ObjectType.valueOf("ITEMINSTANCE"));

                if (certificateNumberType != null) {
                    ObjectAttribute certificateNumber = objectAttributeRepository.findByObjectIdAndAttributeDefId(itemInstance.getId(), certificateNumberType.getId());

                    if (certificateNumber != null && certificateNumber.getStringValue() != null) {
                        itemInstance.setCertificateNumber(certificateNumber.getStringValue());
                    }
                }

                ItemType itemType = itemTypeRepository.findOne(itemInstance.getItem().getItemMaster().getItemType().getParentType());

                if (!itemType.getParentNode()) {
                    itemInstance.getItem().getItemMaster().setParentType(itemType);
                } else {
                    itemInstance.getItem().getItemMaster().setParentType(itemInstance.getItem().getItemMaster().getItemType());
                }

                String storageLocation = "";
                if (itemInstance.getStorage() != null && itemInstance.getStorage().getParent() != null) {
                    Storage parentStorage = storageRepository.findOne(itemInstance.getStorage().getParent());

                    if (parentStorage != null) {
                        storageLocation = parentStorage.getName() + "/" + itemInstance.getStorage().getName();
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
                } else if (itemInstance.getStorage() != null) {
                    storageLocation = itemInstance.getStorage().getName();
                }

                itemInstance.setStoragePath(storageLocation);
            });
        }

        return itemInstances;
    }

    @Transactional(readOnly = true)
    public List<ItemInstance> getItemInstances(Integer itemId) {

        ItemRevision itemRevision = itemRevisionRepository.findOne(itemId);

        ItemType parentItemType = itemTypeService.getParentType(itemRevision.getItemMaster().getItemType());

        List<ItemInstance> itemInstances = new ArrayList<>();
//        if (itemRevision != null && !itemRevision.getItemMaster().getItemType().getParentNode() && !itemRevision.getItemMaster().getItemType().getName().equals("System")) {
        if (!parentItemType.getName().equals("System")) {
            itemInstances = itemInstanceRepository.findByItem(itemId);

            itemInstances.forEach(itemInstance -> {
                ObjectTypeAttribute certificateNumberType = objectTypeAttributeRepository.findByNameAndObjectType("Certificate Number", ObjectType.valueOf("ITEMINSTANCE"));

                if (certificateNumberType != null) {
                    ObjectAttribute certificateNumber = objectAttributeRepository.findByObjectIdAndAttributeDefId(itemInstance.getId(), certificateNumberType.getId());

                    if (certificateNumber != null && certificateNumber.getStringValue() != null) {
                        itemInstance.setCertificateNumber(certificateNumber.getStringValue());
                    }
                }

                ItemType itemType = itemTypeRepository.findOne(itemInstance.getItem().getItemMaster().getItemType().getParentType());

                if (!itemType.getParentNode()) {
                    itemInstance.getItem().getItemMaster().setParentType(itemType);
                } else {
                    itemInstance.getItem().getItemMaster().setParentType(itemInstance.getItem().getItemMaster().getItemType());
                }

                String storageLocation = "";
                if (itemInstance.getStorage() != null && itemInstance.getStorage().getParent() != null) {
                    Storage parentStorage = storageRepository.findOne(itemInstance.getStorage().getParent());

                    if (parentStorage != null) {
                        storageLocation = parentStorage.getName() + "/" + itemInstance.getStorage().getName();
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
                } else if (itemInstance.getStorage() != null) {
                    storageLocation = itemInstance.getStorage().getName();
                }

                itemInstance.setStoragePath(storageLocation);
            });
        } else if (itemRevision.getItemMaster().getItemType().getHasBom()) {
            itemInstances = itemInstanceRepository.findByItem(itemId);
        }

        return itemInstances;
    }

    @Transactional(readOnly = false)
    public ItemInstance updateItemInstance(ItemInstance instance) {
        return itemInstanceRepository.save(instance);
    }

    @Transactional(readOnly = false)
    public ItemInstance updateFailureItemInstance(ItemInstance instance) {
        if (instance.getHasFailed()) {
            instance.setStatus(ItemInstanceStatus.FAILURE_PROCESS);
            instance.setPresentStatus("FAILURE PROCESS");
            ItemInstanceStatusHistory itemInstanceStatusHistory = new ItemInstanceStatusHistory();
            itemInstanceStatusHistory.setStatus(ItemInstanceStatus.FAILURE_PROCESS);
            itemInstanceStatusHistory.setPresentStatus("FAILURE PROCESS");
            itemInstanceStatusHistory.setItemInstance(instance);
            itemInstanceStatusHistory.setTimestamp(new Date());
            itemInstanceStatusHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());
            itemInstanceStatusHistoryRepository.save(itemInstanceStatusHistory);
        }
        return itemInstanceRepository.save(instance);
    }

    @Transactional(readOnly = false)
    public LotInstance updateFailureLotInstance(LotInstance lotInstance) {
        if (lotInstance.getHasFailed()) {
            lotInstance.setStatus(ItemInstanceStatus.FAILURE_PROCESS);
            lotInstance.setPresentStatus("FAILURE PROCESS");
            LotInstanceHistory lotInstanceHistory = new LotInstanceHistory();
            lotInstanceHistory.setStatus(ItemInstanceStatus.FAILURE_PROCESS);
            lotInstanceHistory.setLotInstance(lotInstance.getId());
            lotInstanceHistory.setTimestamp(new Date());
            lotInstanceHistory.setUser(sessionWrapper.getSession().getLogin().getPerson());
            lotInstanceHistoryRepository.save(lotInstanceHistory);
        }
        lotInstance = lotInstanceRepository.save(lotInstance);
        lotInstance.setItemInstance(itemInstanceRepository.findOne(lotInstance.getInstance()));
        return lotInstance;
    }

    public Boolean checkItemInstanceWithMfr(Integer mfr) {
        List<ItemInstance> itemInstances = itemInstanceRepository.findItemInstanceByManufacturer(mfr);
        if (itemInstances.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Transactional(readOnly = true)
    public List<ItemReportDto> getItemReport(Integer itemId) {
        List<ItemReportDto> itemReport = new ArrayList<>();

        ItemRevision itemRevision = itemRevisionRepository.findOne(itemId);

        if (itemRevision != null && !itemRevision.getItemMaster().getItemType().getParentNode()
                && !itemRevision.getItemMaster().getItemType().getName().equals("System")) {

            List<BomItem> bomItems = bomItemRepository.findByItem(itemRevision);

            bomItems.forEach(bomItem -> {

                ItemReportDto itemReportDto = new ItemReportDto();

                /*---------------------- Packages  ----------------------------*/

                BomItem parent = bomItemRepository.findOne(bomItem.getParent());
                if (parent.getBomItemType().equals(BomItemType.UNIT)) {
                    bomItem.setNamePath(parent.getTypeRef().getName());
                    BomItem parent1 = bomItemRepository.findOne(parent.getParent());
                    if (parent1.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                        bomItem.setNamePath(parent1.getTypeRef().getName() + " / " + bomItem.getNamePath());
                        BomItem parent2 = bomItemRepository.findOne(parent1.getParent());
                        if (parent2.getBomItemType().equals(BomItemType.SECTION) || parent2.getBomItemType().equals(BomItemType.COMMONPART)) {
                            bomItem.setNamePath(parent2.getTypeRef().getName() + " / " + bomItem.getNamePath());

                            Bom bom = bomRepository.findOne(parent2.getBom());

                            bomItem.setNamePath(bom.getItem().getItemMaster().getItemCode() + " / " + bomItem.getNamePath());
                        }
                    } else {
                        bomItem.setNamePath(parent1.getTypeRef().getName() + " / " + bomItem.getNamePath());
                    }
                } else if (parent.getBomItemType().equals(BomItemType.SUBSYSTEM)) {
                    bomItem.setNamePath(parent.getTypeRef().getName());
                    BomItem parent1 = bomItemRepository.findOne(parent.getParent());
                    bomItem.setNamePath(parent1.getTypeRef().getName() + " / " + bomItem.getNamePath());
                    Bom bom = bomRepository.findOne(parent1.getBom());

                    bomItem.setNamePath(bom.getItem().getItemMaster().getItemCode() + " / " + bomItem.getNamePath());
                } else if (parent.getBomItemType().equals(BomItemType.SECTION) || parent.getBomItemType().equals(BomItemType.COMMONPART)) {
                    bomItem.setNamePath(parent.getTypeRef().getName());
                    Bom bom = bomRepository.findOne(parent.getBom());

                    bomItem.setNamePath(bom.getItem().getItemMaster().getItemCode() + " / " + bomItem.getNamePath());
                }

                itemReportDto.setBomItem(bomItem);


                Map<Integer, GatePass> gatePassMap = new LinkedHashMap<>();
                Map<Integer, Inward> inwardMap = new LinkedHashMap<>();
                Map<Integer, BomInstance> bomInstanceMap = new LinkedHashMap<Integer, BomInstance>();

                List<InwardItem> inwardItems = inwardItemRepository.findByBomItem(bomItem.getId());

                inwardItems.forEach(inwardItem -> {

                    Inward inward = inwardRepository.findOne(inwardItem.getInward());

                    inwardMap.put(inwardItem.getInward(), inward);

                    gatePassMap.put(inward.getGatePass().getId(), inward.getGatePass());

                    List<InwardItemInstance> inwardItemInstances = inwardItemInstanceRepository.findByInwardItemAndLatestTrue(inwardItem.getId());

                    inwardItemInstances.forEach(inwardItemInstance -> {
                        itemReportDto.getItemInstances().add(itemInstanceRepository.findOne(inwardItemInstance.getItem().getId()));
                    });

                    itemReportDto.getItemInstances().forEach(itemInstance -> {
                        if (itemInstance.getItem().getItemMaster().getItemType().getHasLots()) {
                            List<BomItemInstance> bomItemInstances = bomItemInstanceRepository.getByItemInstance(itemInstance.getId());

                            if (bomItemInstances.size() > 0) {
                                bomItemInstances.forEach(bomItemInstance -> {
                                    List<IssueItem> issueItems = issueItemRepository.findByBomItemInstance(bomItemInstance);

                                    issueItems.forEach(issueItem -> {
                                        if (issueItem.getStatus().equals(IssueItemStatus.RECEIVED)) {
                                            itemReportDto.getIssueItems().add(issueItem);
                                        }

                                        Issue issue = issueRepository.findOne(issueItem.getIssue().getId());

                                        bomInstanceMap.put(issue.getRequest().getBomInstance().getId(), issue.getRequest().getBomInstance());
                                    });
                                });
                            }
                        } else {
                            BomItemInstance bomItemInstance = bomItemInstanceRepository.findByItemInstance(itemInstance.getId());

                            if (bomItemInstance != null) {
                                IssueItem issueItem = issueItemRepository.getByBomItemInstance(bomItemInstance.getId());

                                if (issueItem != null) {
                                    if (issueItem.getStatus().equals(IssueItemStatus.RECEIVED)) {
                                        itemReportDto.getIssueItems().add(issueItem);
                                    }

                                    Issue issue = issueRepository.findOne(issueItem.getIssue().getId());

                                    bomInstanceMap.put(issue.getRequest().getBomInstance().getId(), issue.getRequest().getBomInstance());

                                }
                            }
                        }
                    });


                });

                /*------------------  Inwards  -------------------*/

                for (Inward inward : inwardMap.values()) {
                    itemReportDto.getInwards().add(inward);
                }

                /*------------------  GatePass  -------------------*/

                for (GatePass gatePass : gatePassMap.values()) {
                    itemReportDto.getGatePasses().add(gatePass);
                }
                /*------------------  Missiles  -------------------*/

                for (BomInstance bomInstance : bomInstanceMap.values()) {
                    itemReportDto.getMissiles().add(bomInstance);
                }

                itemReport.add(itemReportDto);

            });

        }
        return itemReport;
    }

}
