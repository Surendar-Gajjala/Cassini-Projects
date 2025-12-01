package com.cassinisys.drdo.service.inventory;

import com.cassinisys.drdo.model.bom.*;
import com.cassinisys.drdo.model.dto.StorageDetailsDto;
import com.cassinisys.drdo.model.dto.StorageInventoryDto;
import com.cassinisys.drdo.model.inventory.Storage;
import com.cassinisys.drdo.model.inventory.StorageItem;
import com.cassinisys.drdo.model.inventory.StorageItemType;
import com.cassinisys.drdo.model.inventory.StorageType;
import com.cassinisys.drdo.repo.bom.*;
import com.cassinisys.drdo.repo.inventory.StorageItemRepository;
import com.cassinisys.drdo.repo.inventory.StorageItemTypeRepository;
import com.cassinisys.drdo.repo.inventory.StorageRepository;
import com.cassinisys.drdo.service.bom.ItemTypeService;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.service.core.CrudService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by subra on 07-10-2018.
 */
@Service
public class StorageService implements CrudService<Storage, Integer> {

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private StorageItemTypeRepository storageItemTypeRepository;

    @Autowired
    private ItemTypeRepository itemTypeRepository;

    @Autowired
    private ItemTypeService itemTypeService;

    @Autowired
    private ItemInstanceRepository itemInstanceRepository;

    @Autowired
    private StorageItemRepository storageItemRepository;

    @Autowired
    private BomGroupRepository bomGroupRepository;

    @Autowired
    private LotInstanceRepository lotInstanceRepository;

    @Autowired
    private BomItemRepository bomItemRepository;

    @Override
    @Transactional(readOnly = false)
    public Storage create(Storage storage) {
        Storage existStorage = null;
        if (storage.getType().equals(StorageType.WAREHOUSE)) {
            if (!storage.getOnHold() && !storage.getReturned()) {
                existStorage = storageRepository.findByNameAndType(storage.getName(), StorageType.WAREHOUSE);
                if (existStorage == null) {
                    storage = storageRepository.save(storage);
                } else {
                    throw new CassiniException("Warehouse name already exist");
                }
            } else {
                if (storage.getReturned()) {
                    existStorage = storageRepository.findByNameAndTypeAndBomAndReturnedTrue(storage.getName(), StorageType.WAREHOUSE, storage.getBom());

                    if (existStorage == null) {
                        storage = storageRepository.save(storage);
                    } else {
                        throw new CassiniException(storage.getBom().getItem().getItemMaster().getItemName() + " Return storage already exist");
                    }
                } else {
                    existStorage = storageRepository.findByNameAndTypeAndBomAndOnHoldTrue(storage.getName(), StorageType.WAREHOUSE, storage.getBom());

                    if (existStorage == null) {
                        storage = storageRepository.save(storage);
                    } else {
                        throw new CassiniException(storage.getBom().getItem().getItemMaster().getItemName() + " On Hold storage already exist");
                    }
                }
            }
        } else if (storage.getType().equals(StorageType.STOCKROOM)) {
            if (storage.getParent() == null) {
                existStorage = storageRepository.findByNameAndType(storage.getName(), StorageType.STOCKROOM);
            } else {
                existStorage = storageRepository.findByParentAndNameAndType(storage.getParent(), storage.getName(), StorageType.STOCKROOM);
            }

            if (existStorage == null) {
                storage = storageRepository.save(storage);
            } else {
                throw new CassiniException("Stockroom name already exist");
            }
        } else if (storage.getType().equals(StorageType.AREA)) {
            existStorage = storageRepository.findByParentAndNameAndType(storage.getParent(), storage.getName(), StorageType.AREA);
            if (existStorage == null) {
                storage = storageRepository.save(storage);
            } else {
                throw new CassiniException("Area name already exist");
            }
        } else if (storage.getType().equals(StorageType.RACK)) {
            existStorage = storageRepository.findByParentAndNameAndType(storage.getParent(), storage.getName(), StorageType.RACK);
            if (existStorage == null) {
                storage = storageRepository.save(storage);
            } else {
                throw new CassiniException("Rack name already exist");
            }
        } else if (storage.getType().equals(StorageType.SHELF)) {
            existStorage = storageRepository.findByParentAndNameAndType(storage.getParent(), storage.getName(), StorageType.SHELF);
            if (existStorage == null) {
                storage = storageRepository.save(storage);
            } else {
                throw new CassiniException("Shelf name already exist");
            }
        } else if (storage.getType().equals(StorageType.BIN)) {
            existStorage = storageRepository.findByParentAndNameAndType(storage.getParent(), storage.getName(), StorageType.BIN);
            if (existStorage == null) {
                storage = storageRepository.save(storage);
            } else {
                throw new CassiniException("Bin name already exist");
            }
        }
        return storage;
    }

    @Override
    @Transactional(readOnly = false)
    public Storage update(Storage storage) {
        Storage existStorage = null;
        if (storage.getType().equals(StorageType.WAREHOUSE)) {
            existStorage = storageRepository.findByNameAndType(storage.getName(), StorageType.WAREHOUSE);
            if (existStorage == null) {
                if (!storage.getIsLeafNode()) {
                    storage.setCapacity(0.0);
                    storage.setRemainingCapacity(0.0);

                    List<StorageItem> storageItems = storageItemRepository.findByStorage(storage.getId());
                    storageItems.forEach(storageItem -> {
                        storageItemRepository.delete(storageItem.getId());
                    });
                }
                storage = storageRepository.save(storage);

                if (storage.getBom() != null) {
                    saveBomToChildrenStorages(storage.getId(), storage.getBom());
                }

            } else {
                if (existStorage.getId().equals(storage.getId())) {
                    if (!storage.getIsLeafNode()) {
                        storage.setCapacity(0.0);
                        storage.setRemainingCapacity(0.0);

                        List<StorageItem> storageItems = storageItemRepository.findByStorage(storage.getId());
                        storageItems.forEach(storageItem -> {
                            storageItemRepository.delete(storageItem.getId());
                        });
                    }
                    storage = storageRepository.save(storage);

                    if (storage.getBom() != null) {
                        saveBomToChildrenStorages(storage.getId(), storage.getBom());
                    }
                } else {
                    throw new CassiniException("Warehouse name already exist");
                }
            }
        } else if (storage.getType().equals(StorageType.STOCKROOM)) {
            if (storage.getParent() == null) {
                existStorage = storageRepository.findByNameAndType(storage.getName(), StorageType.STOCKROOM);
            } else {
                existStorage = storageRepository.findByParentAndNameAndType(storage.getParent(), storage.getName(), StorageType.STOCKROOM);
            }

            if (existStorage == null) {
                if (!storage.getIsLeafNode()) {
                    storage.setCapacity(0.0);
                    storage.setRemainingCapacity(0.0);

                    List<StorageItem> storageItems = storageItemRepository.findByStorage(storage.getId());
                    storageItems.forEach(storageItem -> {
                        storageItemRepository.delete(storageItem.getId());
                    });
                }
                storage = storageRepository.save(storage);

                if (storage.getBom() != null) {
                    saveBomToChildrenStorages(storage.getId(), storage.getBom());
                }
            } else {
                if (existStorage.getId().equals(storage.getId())) {
                    if (!storage.getIsLeafNode()) {
                        storage.setCapacity(0.0);
                        storage.setRemainingCapacity(0.0);

                        List<StorageItem> storageItems = storageItemRepository.findByStorage(storage.getId());
                        storageItems.forEach(storageItem -> {
                            storageItemRepository.delete(storageItem.getId());
                        });
                    }
                    storage = storageRepository.save(storage);

                    if (storage.getBom() != null) {
                        saveBomToChildrenStorages(storage.getId(), storage.getBom());
                    }
                } else {
                    throw new CassiniException("Stockroom name already exist");
                }
            }
        } else if (storage.getType().equals(StorageType.AREA)) {
            existStorage = storageRepository.findByParentAndNameAndType(storage.getParent(), storage.getName(), StorageType.AREA);
            if (existStorage == null) {
                if (!storage.getIsLeafNode()) {
                    storage.setCapacity(0.0);
                    storage.setRemainingCapacity(0.0);

                    List<StorageItem> storageItems = storageItemRepository.findByStorage(storage.getId());
                    storageItems.forEach(storageItem -> {
                        storageItemRepository.delete(storageItem.getId());
                    });
                }
                storage = storageRepository.save(storage);

                if (storage.getBom() != null) {
                    saveBomToChildrenStorages(storage.getId(), storage.getBom());
                }
            } else {
                if (existStorage.getId().equals(storage.getId())) {
                    if (!storage.getIsLeafNode()) {
                        storage.setCapacity(0.0);
                        storage.setRemainingCapacity(0.0);

                        List<StorageItem> storageItems = storageItemRepository.findByStorage(storage.getId());
                        storageItems.forEach(storageItem -> {
                            storageItemRepository.delete(storageItem.getId());
                        });
                    }
                    storage = storageRepository.save(storage);

                    if (storage.getBom() != null) {
                        saveBomToChildrenStorages(storage.getId(), storage.getBom());
                    }
                } else {
                    throw new CassiniException("Area name already exist");
                }
            }
        } else if (storage.getType().equals(StorageType.RACK)) {
            existStorage = storageRepository.findByParentAndNameAndType(storage.getParent(), storage.getName(), StorageType.RACK);
            if (existStorage == null) {
                if (!storage.getIsLeafNode()) {
                    storage.setCapacity(0.0);
                    storage.setRemainingCapacity(0.0);

                    List<StorageItem> storageItems = storageItemRepository.findByStorage(storage.getId());
                    storageItems.forEach(storageItem -> {
                        storageItemRepository.delete(storageItem.getId());
                    });
                }
                storage = storageRepository.save(storage);

                if (storage.getBom() != null) {
                    saveBomToChildrenStorages(storage.getId(), storage.getBom());
                }
            } else {
                if (existStorage.getId().equals(storage.getId())) {
                    if (!storage.getIsLeafNode()) {
                        storage.setCapacity(0.0);
                        storage.setRemainingCapacity(0.0);

                        List<StorageItem> storageItems = storageItemRepository.findByStorage(storage.getId());
                        storageItems.forEach(storageItem -> {
                            storageItemRepository.delete(storageItem.getId());
                        });
                    }
                    storage = storageRepository.save(storage);

                    if (storage.getBom() != null) {
                        saveBomToChildrenStorages(storage.getId(), storage.getBom());
                    }
                } else {
                    throw new CassiniException("Rack name already exist");
                }
            }
        } else if (storage.getType().equals(StorageType.SHELF)) {
            existStorage = storageRepository.findByParentAndNameAndType(storage.getParent(), storage.getName(), StorageType.SHELF);
            if (existStorage == null) {
                if (!storage.getIsLeafNode()) {
                    storage.setCapacity(0.0);
                    storage.setRemainingCapacity(0.0);

                    List<StorageItem> storageItems = storageItemRepository.findByStorage(storage.getId());
                    storageItems.forEach(storageItem -> {
                        storageItemRepository.delete(storageItem.getId());
                    });
                }
                storage = storageRepository.save(storage);

                if (storage.getBom() != null) {
                    saveBomToChildrenStorages(storage.getId(), storage.getBom());
                }
            } else {
                if (existStorage.getId().equals(storage.getId())) {
                    if (!storage.getIsLeafNode()) {
                        storage.setCapacity(0.0);
                        storage.setRemainingCapacity(0.0);

                        List<StorageItem> storageItems = storageItemRepository.findByStorage(storage.getId());
                        storageItems.forEach(storageItem -> {
                            storageItemRepository.delete(storageItem.getId());
                        });
                    }
                    storage = storageRepository.save(storage);

                    if (storage.getBom() != null) {
                        saveBomToChildrenStorages(storage.getId(), storage.getBom());
                    }
                } else {
                    throw new CassiniException("Shelf name already exist");
                }
            }
        } else if (storage.getType().equals(StorageType.BIN)) {
            existStorage = storageRepository.findByParentAndNameAndType(storage.getParent(), storage.getName(), StorageType.BIN);
            if (existStorage == null) {
                if (!storage.getIsLeafNode()) {
                    storage.setCapacity(0.0);
                    storage.setRemainingCapacity(0.0);

                    List<StorageItem> storageItems = storageItemRepository.findByStorage(storage.getId());
                    storageItems.forEach(storageItem -> {
                        storageItemRepository.delete(storageItem.getId());
                    });
                }
                storage = storageRepository.save(storage);

                if (storage.getBom() != null) {
                    saveBomToChildrenStorages(storage.getId(), storage.getBom());
                }
            } else {
                if (existStorage.getId().equals(storage.getId())) {
                    if (!storage.getIsLeafNode()) {
                        storage.setCapacity(0.0);
                        storage.setRemainingCapacity(0.0);

                        List<StorageItem> storageItems = storageItemRepository.findByStorage(storage.getId());
                        storageItems.forEach(storageItem -> {
                            storageItemRepository.delete(storageItem.getId());
                        });
                    }

                    storage = storageRepository.save(storage);

                    if (storage.getBom() != null) {
                        saveBomToChildrenStorages(storage.getId(), storage.getBom());
                    }
                } else {
                    throw new CassiniException("Bin name already exist");
                }
            }
        }


        return storage;
    }


    private void saveBomToChildrenStorages(Integer storage, Bom bom) {

        List<Storage> storages = storageRepository.findByParentOrderByCreatedDateAsc(storage);

        for (Storage storage1 : storages) {
            storage1.setBom(bom);

            storage1 = storageRepository.save(storage1);

            saveBomToChildrenStorages(storage1.getId(), bom);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {

        List<ItemInstance> itemInstances = itemInstanceRepository.findByStorage(id);

        if (itemInstances.size() > 0) {
            throw new CassiniException("Storage Location has Items. We can't delete");
        } else {
            storageRepository.delete(id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Storage get(Integer id) {
        Storage storage = storageRepository.findOne(id);

        storage.getChildren().addAll(storageRepository.findByParentOrderByCreatedDateAsc(storage.getId()));

        /*for (Storage children : storage.getChildren()) {
            storage.setRemainingCapacity(storage.getRemainingCapacity() + children.getRemainingCapacity());
            storage.setCapacity(storage.getCapacity() + children.getCapacity());
            List<Storage> childrens = storageRepository.findByParentOrderByCreatedDateAsc(children.getId());
            storage = calculateChildrenCapacity(storage, childrens);
        }*/


        return storage;
    }

    private Storage calculateChildrenCapacity(Storage storage, List<Storage> childrens) {
        for (Storage children : childrens) {
            storage.setRemainingCapacity(storage.getRemainingCapacity() + children.getRemainingCapacity());
            storage.setCapacity(storage.getCapacity() + children.getCapacity());

            List<Storage> childrenList = storageRepository.findByParentOrderByCreatedDateAsc(children.getId());
            storage = calculateChildrenCapacity(storage, childrenList);
        }

        return storage;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Storage> getAll() {
        return storageRepository.findAll();
    }

    public List<Storage> getStorageTree() {
        List<Storage> storageItems = getRootTypes();

        for (Storage item : storageItems) {
            visitChildren(item);
        }

        return storageItems;
    }

    @Transactional(readOnly = true)
    public List<Storage> getRootTypes() {
        return storageRepository.findByParentIsNullOrderByCreatedDateAsc();
    }

    private void visitChildren(Storage parent) {
        List<Storage> childrens = getChildren(parent.getId());

        for (Storage child : childrens) {
            visitChildren(child);
        }

        parent.setChildren(childrens);
    }

    @Transactional(readOnly = true)
    public List<Storage> getChildren(Integer parent) {
        return storageRepository.findByParentOrderByCreatedDateAsc(parent);
    }

    @Transactional(readOnly = true)
    public List<Storage> getOnHoldChildren(Integer parent) {
        return storageRepository.findByParentAndOnHoldTrueOrderByCreatedDateAsc(parent);
    }

    @Transactional(readOnly = true)
    public List<Storage> getReturnChildren(Integer parent) {
        return storageRepository.findByParentAndReturnedTrueOrderByCreatedDateAsc(parent);
    }

    @Transactional(readOnly = true)
    public Storage getWarehouseByName(String name) {
        return storageRepository.findByNameAndType(name, StorageType.WAREHOUSE);
    }

    @Transactional(readOnly = true)
    public Storage getStockroomByName(String name) {
        return storageRepository.findByNameAndTypeAndParentIsNull(name, StorageType.STOCKROOM);
    }

    @Transactional(readOnly = true)
    public Storage getStockroomByParentAndName(Integer parent, String name) {
        return storageRepository.findByParentAndNameAndType(parent, name, StorageType.STOCKROOM);
    }

    @Transactional(readOnly = true)
    public Storage getAreaByParentAndName(Integer parent, String name) {
        return storageRepository.findByParentAndNameAndType(parent, name, StorageType.AREA);
    }

    @Transactional(readOnly = true)
    public Storage getRackByParentAndName(Integer parent, String name) {
        return storageRepository.findByParentAndNameAndType(parent, name, StorageType.RACK);
    }

    @Transactional(readOnly = true)
    public Storage getShelfByParentAndName(Integer parent, String name) {
        return storageRepository.findByParentAndNameAndType(parent, name, StorageType.SHELF);
    }

    @Transactional(readOnly = true)
    public Storage getBinByParentAndName(Integer parent, String name) {
        return storageRepository.findByParentAndNameAndType(parent, name, StorageType.BIN);
    }

    public void generateStorageBarcode(Integer id, HttpServletResponse response) {
        Storage storage = storageRepository.findOne(id);
        if (storage != null) {
            BitMatrix bitMatrix;

            try {
                response.setContentType("image/png");
                OutputStream out = response.getOutputStream();
                String msg = "{0}";
                msg = MessageFormat.format(msg, storage.getName());
                bitMatrix = new Code128Writer().encode(msg, BarcodeFormat.CODE_128, 150, 80, null);
                MatrixToImageWriter.writeToStream(bitMatrix, "png", out);
                out.flush();
            } catch (WriterException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional(readOnly = false)
    public List<StorageItemType> saveStorageItemTypes(Integer storageId, List<ItemType> itemTypes) {
        List<StorageItemType> storageItemTypes = new ArrayList<>();
//        Storage storage = storageRepository.findOne(storageId);
        for (ItemType itemType : itemTypes) {
            StorageItemType existStorageType = storageItemTypeRepository.findByStorageAndItemType(storageId, itemType.getId());
            if (existStorageType == null) {
                StorageItemType storageItemType = new StorageItemType();
                storageItemType.setStorage(storageRepository.findOne(storageId));
                storageItemType.setItemType(itemType);

                storageItemType = storageItemTypeRepository.save(storageItemType);
                storageItemTypes.add(storageItemType);
            } else {
                storageItemTypes.add(existStorageType);
            }
        }

//        storageItemTypes = storageItemTypeRepository.save(storageItemTypes);

        return storageItemTypes;
    }

    @Transactional(readOnly = true)
    public List<ItemType> getStorageClassificationTree(Integer storageId) {
        List<StorageItemType> storageItemTypes = storageItemTypeRepository.getByStorage(storageId);

        List<ItemType> itemTypes = new ArrayList<>();

        for (StorageItemType storageItemType : storageItemTypes) {
            ItemType itemType = itemTypeRepository.findOne(storageItemType.getItemType().getId());

            if (!checkStorageTypeExist(itemType, itemTypes)) {
                itemTypes.add(itemType);
                visitItemTypeChildren(itemType, storageItemTypes);
            }
        }

        return itemTypes;
    }


    private Boolean checkStorageTypeExist(ItemType storageType, List<ItemType> itemTypes) {
        Boolean storageTypeExist = false;
        for (ItemType itemType : itemTypes) {
            if (storageType.getId().equals(itemType.getId())) {
                storageTypeExist = true;
            }
            if (storageTypeExist) {
                break;
            } else {
                storageTypeExist = checkStorageTypeExist(storageType, itemType.getChildren());
                if (storageTypeExist) {
                    break;
                }
            }
        }

        return storageTypeExist;
    }

    private void visitItemTypeChildren(ItemType parent, List<StorageItemType> storageItemTypes) {
        List<ItemType> children = itemTypeService.getChildren(parent.getId());

        List<ItemType> existChildren = new ArrayList<>();


        for (ItemType child : children) {

            for (StorageItemType storageItemType : storageItemTypes) {
                if (child.getId().equals(storageItemType.getItemType().getId())) {

                    existChildren.add(child);
                }
            }
            visitItemTypeChildren(child, storageItemTypes);
        }

        if (existChildren.size() > 0) {
            parent.setChildren(existChildren);
        } else {
            parent.setChildren(children);
        }

    }

    private void visitChildrenTypeChildren(ItemType parent, List<StorageItemType> storageItemTypes) {
        List<ItemType> children = itemTypeService.getChildren(parent.getId());

        List<ItemType> existChildren = new ArrayList<>();


        for (ItemType child : children) {

            visitChildrenTypeChildren(child, storageItemTypes);
        }

        parent.setChildren(children);
    }

    @Transactional(readOnly = false)
    public void deleteStorageItemType(Integer storageId, Integer typeId) {
        Storage storage = storageRepository.findOne(storageId);
        ItemType itemType = itemTypeRepository.findOne(typeId);
        if (storage != null) {
            StorageItemType storageItemType = storageItemTypeRepository.findByStorageAndItemType(storage.getId(), itemType.getId());
            if (storageItemType != null) {
                storageItemTypeRepository.delete(storageItemType.getId());
            }

            deleteItemTypeChildrenInStorage(storage, itemType);

            deleteItemTypeFromChildrenStorages(storage, itemType);
        }
    }

    private void deleteItemTypeFromChildrenStorages(Storage storage, ItemType itemType) {

        List<Storage> childrenStorage = storageRepository.findByParentOrderByCreatedDateAsc(storage.getId());
        for (Storage child : childrenStorage) {
            StorageItemType storageItemType1 = storageItemTypeRepository.findByStorageAndItemType(child.getId(), itemType.getId());
            if (storageItemType1 != null) {
                storageItemTypeRepository.delete(storageItemType1.getId());
            }

            deleteItemTypeChildrenInStorage(child, itemType);

            deleteItemTypeFromChildrenStorages(child, itemType);
        }
    }


    private void deleteItemTypeChildrenInStorage(Storage storage, ItemType itemType) {
        List<ItemType> itemTypeChildren = itemTypeRepository.findByParentTypeOrderByNameCreatedDateAsc(itemType.getId());
        for (ItemType itemTypeChild : itemTypeChildren) {
            StorageItemType childStorageItemType = storageItemTypeRepository.findByStorageAndItemType(storage.getId(), itemTypeChild.getId());
            if (childStorageItemType != null) {
                storageItemTypeRepository.delete(childStorageItemType.getId());
            }

            deleteItemTypeChildrenInStorage(storage, itemTypeChild);
        }
    }

    @Transactional(readOnly = true)
    public List<StorageItemType> getStorageTypes(Integer storageId) {
        Storage storage = storageRepository.findOne(storageId);
        List<StorageItemType> storageItemTypes = storageItemTypeRepository.findByStorage(storage);

        return storageItemTypes;
    }

    @Transactional(readOnly = true)
    public List<ItemInstance> getStorageItems(Integer storageId) {
        List<ItemInstance> itemInstances = itemInstanceRepository.findByStorage(storageId);

        itemInstances = visitChildrenStorage(storageId, itemInstances);

        itemInstances.forEach(itemInstance -> {
            if (itemInstance.getItem().getItemMaster().getItemType().getHasLots()) {
                List<LotInstance> lotInstances = lotInstanceRepository.findByInstance(itemInstance.getId());
                lotInstances.forEach(lotInstance -> {
                    itemInstance.setLotIssuedQuantity(itemInstance.getLotIssuedQuantity() + lotInstance.getLotQty());
                });
            }
        });

        return itemInstances;
    }

    private List<ItemInstance> visitChildrenStorage(Integer storageId, List<ItemInstance> itemInstances) {

        List<Storage> storages = storageRepository.findByParentOrderByCreatedDateAsc(storageId);
        for (Storage storage : storages) {
            itemInstances.addAll(itemInstanceRepository.findByStorage(storage.getId()));
            itemInstances = visitChildrenStorage(storage.getId(), itemInstances);
        }
        return itemInstances;
    }

    private List<StorageItem> visitChildrenStorageItems(Integer storageId, List<StorageItem> storageItems) {

        List<Storage> storages = storageRepository.findByParentOrderByCreatedDateAsc(storageId);
        for (Storage storage : storages) {
            storageItems.addAll(storageItemRepository.findByStorage(storage.getId()));
            storageItems = visitChildrenStorageItems(storage.getId(), storageItems);
        }

        return storageItems;
    }

    @Transactional(readOnly = true)
    public StorageDetailsDto getStorageDetails(Integer storageId) {

        StorageDetailsDto storageDetailsDto = new StorageDetailsDto();

        /*-----------------  Storage  ----------------------*/

        storageDetailsDto.setStorage(storageRepository.findOne(storageId));

        if (storageDetailsDto.getStorage().getParent() != null) {
            storageDetailsDto.setParentData(storageRepository.findOne(storageDetailsDto.getStorage().getParent()));
        }

        storageDetailsDto.getStorage().getChildren().addAll(storageRepository.findByParentOrderByCreatedDateAsc(storageId));

        /*------------------ Storage Items  ---------------------------*/

        List<StorageItem> storageItems = storageItemRepository.findByStorage(storageDetailsDto.getStorage().getId());

        storageItems = visitChildrenStorageItems(storageId, storageItems);

        Map<String, StorageItem> storageItemMap = new HashMap<>();

        for (StorageItem storageItem : storageItems) {
            if (storageItem.getSection() == null) {
                storageItemMap.put(storageItem.getUniqueCode(), storageItem);
            } else {
                storageItemMap.put(storageItem.getUniqueCode() + "" + storageItem.getSection().getName(), storageItem);
            }
        }

        for (StorageItem storageItem : storageItemMap.values()) {

            ItemType itemType = itemTypeRepository.findOne(storageItem.getItem().getItem().getItemMaster().getItemType().getParentType());

            if (!itemType.getParentNode()) {
                storageItem.getItem().getItem().getItemMaster().setParentType(itemType);
            } else {
                storageItem.getItem().getItem().getItemMaster().setParentType(storageItem.getItem().getItem().getItemMaster().getItemType());
            }

            storageDetailsDto.getStorageItems().add(storageItem);
        }


        /*------------------  Storage Inventory Parts  -----------------------*/

        List<ItemInstance> itemInstances = itemInstanceRepository.findByStorage(storageId);

        itemInstances = visitChildrenStorage(storageId, itemInstances);

        storageDetailsDto.getStorageParts().addAll(itemInstances);

        HashMap<Integer, ItemRevision> instanceHashMap = new HashMap<>();

        for (ItemInstance itemInstance : itemInstances) {

            if (itemInstance.getItem().getItemMaster().getItemType().getHasLots()) {
                List<LotInstance> lotInstances = lotInstanceRepository.findByInstance(itemInstance.getId());
                lotInstances.forEach(lotInstance -> {
                    itemInstance.setLotIssuedQuantity(itemInstance.getLotIssuedQuantity() + lotInstance.getLotQty());
                });
            }

            ItemRevision itemRevision = instanceHashMap.get(itemInstance.getItem().getId());

            if (itemRevision != null) {
                for (StorageInventoryDto inventoryDto : storageDetailsDto.getStorageInventory()) {
                    if (inventoryDto.getItem().getId().equals(itemRevision.getId())) {
                        inventoryDto.getItemInstances().add(itemInstance);
                    }
                }
            } else {
                StorageInventoryDto storageInventoryDto = new StorageInventoryDto();
                storageInventoryDto.setItem(itemInstance.getItem());
                storageInventoryDto.getItemInstances().add(itemInstance);

                storageDetailsDto.getStorageInventory().add(storageInventoryDto);
            }
            ItemType itemType = itemTypeRepository.findOne(itemInstance.getItem().getItemMaster().getItemType().getParentType());

            if (!itemType.getParentNode()) {
                itemInstance.getItem().getItemMaster().setParentType(itemType);
            } else {
                itemInstance.getItem().getItemMaster().setParentType(itemInstance.getItem().getItemMaster().getItemType());
            }

            instanceHashMap.put(itemInstance.getItem().getId(), itemInstance.getItem());
        }


        return storageDetailsDto;
    }

    @Transactional(readOnly = false)
    public List<StorageItem> saveStorageItems(Integer storageId, List<BomItem> bomItems) {

        List<StorageItem> storageItems = new ArrayList<>();
        Storage storage = storageRepository.findOne(storageId);
        String systemCode = storage.getBom().getItem().getItemMaster().getItemCode();

        for (BomItem bomItem : bomItems) {
            StorageItem storageItem = null;
            if (bomItem.getPathCount() > 1) {
                if (bomItem.getDefaultSection().getName().equals("Common")) {

                    List<BomItem> commonItems = new ArrayList<>();

                    List<BomItem> itemList = bomItemRepository.findByItemAndUniqueCode(bomItem.getItem(), bomItem.getUniqueCode());

                    if (itemList.size() > 0) {
                        itemList.forEach(item -> {
                            String itemSystemCode = item.getHierarchicalCode().substring(0, 2);
                            if (systemCode.equals(itemSystemCode)) {
                                commonItems.add(item);
                            }
                        });
                    }

                    storageItem = storageItemRepository.findByStorageAndUniqueCodeAndSectionIsNull(storageId, bomItem.getUniqueCode());

                    if (storageItem == null) {
                        StorageItem item = new StorageItem();
                        item.setStorage(storageRepository.findOne(storageId));
                        item.setItem(bomItem);
                        item.setUniqueCode(bomItem.getUniqueCode());

                        storageItems.add(storageItemRepository.save(item));
                    } else {
                        storageItems.add(storageItem);
                    }

                    for (BomItem commonItem : commonItems) {
                        storageItem = storageItemRepository.findByStorageAndUniqueCodeAndSectionIsNull(storageId, commonItem.getUniqueCode());

                        if (storageItem == null) {
                            StorageItem item = new StorageItem();
                            item.setStorage(storageRepository.findOne(storageId));
                            item.setItem(commonItem);
                            item.setUniqueCode(commonItem.getUniqueCode());

                            storageItems.add(storageItemRepository.save(item));
                        } else {
                            storageItems.add(storageItem);
                        }
                    }
                } else {
                    BomGroup bomGroup = bomGroupRepository.findByTypeAndNameAndVersity(BomItemType.SECTION, bomItem.getDefaultSection().getName(), bomItem.getDefaultSection().getVersity());
                    if (bomGroup == null) {
                        bomGroup = bomGroupRepository.findByTypeAndNameAndVersity(BomItemType.COMMONPART, bomItem.getDefaultSection().getName(), bomItem.getDefaultSection().getVersity());
                    }
                    if (bomGroup != null) {
                        storageItem = storageItemRepository.findByStorageAndUniqueCodeAndSection(storageId, bomItem.getUniqueCode(), bomGroup.getId());

                        if (bomItem.getPathCount() > 1) {
                            StorageItem commonItem = storageItemRepository.findByStorageAndUniqueCodeAndSectionIsNull(storageId, bomItem.getUniqueCode());
                            if (commonItem == null) {
                                StorageItem item = new StorageItem();
                                item.setStorage(storageRepository.findOne(storageId));
                                item.setUniqueCode(bomItem.getUniqueCode());
                                item.setItem(bomItem);
                                storageItems.add(storageItemRepository.save(item));
                            }
                        }

                        if (storageItem == null) {
                            StorageItem item = new StorageItem();
                            item.setStorage(storageRepository.findOne(storageId));
                            item.setUniqueCode(bomItem.getUniqueCode());
                            item.setSection(bomGroup);
                            item.setItem(bomItem);
                            storageItems.add(storageItemRepository.save(item));
                        } else {
                            storageItems.add(storageItem);
                        }
                    }
                }

            } else {
                BomGroup bomGroup = bomGroupRepository.findByTypeAndNameAndVersity(BomItemType.SECTION, bomItem.getDefaultSection().getName(), bomItem.getDefaultSection().getVersity());
                if (bomGroup == null) {
                    bomGroup = bomGroupRepository.findByTypeAndNameAndVersity(BomItemType.COMMONPART, bomItem.getDefaultSection().getName(), bomItem.getDefaultSection().getVersity());
                }
                if (bomGroup != null) {
                    storageItem = storageItemRepository.findByStorageAndUniqueCodeAndSection(storageId, bomItem.getUniqueCode(), bomGroup.getId());
                    if (storageItem == null) {
                        StorageItem item = new StorageItem();
                        item.setStorage(storageRepository.findOne(storageId));
                        item.setUniqueCode(bomItem.getUniqueCode());
                        item.setSection(bomGroup);
                        item.setItem(bomItem);
                        storageItems.add(storageItemRepository.save(item));
                    } else {
                        storageItems.add(storageItem);
                    }
                }
            }

        }

        storageItems = storageItemRepository.findByStorage(storageId);

        return storageItems;
    }

    private List<BomItem> getCommonItemByBomItem(Integer bomId, Integer bomItemId) {
        List<BomItem> bomItems = new ArrayList<>();

        List<BomItem> bomChildren = bomItemRepository.findByBomOrderByCreatedDateAsc(bomId);

        bomChildren.forEach(bomChild -> {
            if (bomChild.getBomItemType().equals(BomItemType.PART) && bomChild.getItem().getId().equals(bomItemId)) {
                bomItems.add(bomChild);
            }

            visitBomItemChildren(bomChild, bomItemId, bomItems);
        });

        return bomItems;
    }

    private List<BomItem> visitBomItemChildren(BomItem bomItem, Integer itemId, List<BomItem> bomItems) {

        List<BomItem> bomChildren = bomItemRepository.findByParentOrderByCreatedDateAsc(bomItem.getId());

        bomChildren.forEach(bomChild -> {
            if (bomChild.getBomItemType().equals(BomItemType.PART) && bomChild.getItem().getId().equals(itemId)) {
                bomItems.add(bomChild);
            }

            visitBomItemChildren(bomChild, itemId, bomItems);
        });

        return bomItems;
    }

    @Transactional(readOnly = false)
    public void deleteStorageItem(Integer storageItemId) {
        storageItemRepository.delete(storageItemId);
    }
}
