package com.cassinisys.drdo.model.dto;

import com.cassinisys.drdo.model.bom.ItemInstance;
import com.cassinisys.drdo.model.bom.ItemRevision;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam reddy on 24-12-2018.
 */
public class StorageInventoryDto {

    private ItemRevision item;

    private List<ItemInstance> itemInstances = new ArrayList<>();

    public ItemRevision getItem() {
        return item;
    }

    public void setItem(ItemRevision item) {
        this.item = item;
    }

    public List<ItemInstance> getItemInstances() {
        return itemInstances;
    }

    public void setItemInstances(List<ItemInstance> itemInstances) {
        this.itemInstances = itemInstances;
    }
}
