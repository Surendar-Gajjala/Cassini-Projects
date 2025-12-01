package com.cassinisys.drdo.model.dto;

import com.cassinisys.drdo.model.bom.ItemInstance;
import com.cassinisys.drdo.model.inventory.Storage;
import com.cassinisys.drdo.model.inventory.StorageItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam reddy on 23-11-2018.
 */
public class StorageDetailsDto {

    private Storage storage;

    private Storage parentData;

    private List<StorageItem> storageItems = new ArrayList<>();

    private List<ItemInstance> storageParts = new ArrayList<>();

    private List<StorageInventoryDto> storageInventory = new ArrayList<>();

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public List<StorageItem> getStorageItems() {
        return storageItems;
    }

    public void setStorageItems(List<StorageItem> storageItems) {
        this.storageItems = storageItems;
    }

    public List<ItemInstance> getStorageParts() {
        return storageParts;
    }

    public void setStorageParts(List<ItemInstance> storageParts) {
        this.storageParts = storageParts;
    }

    public Storage getParentData() {
        return parentData;
    }

    public void setParentData(Storage parentData) {
        this.parentData = parentData;
    }

    public List<StorageInventoryDto> getStorageInventory() {
        return storageInventory;
    }

    public void setStorageInventory(List<StorageInventoryDto> storageInventory) {
        this.storageInventory = storageInventory;
    }
}
