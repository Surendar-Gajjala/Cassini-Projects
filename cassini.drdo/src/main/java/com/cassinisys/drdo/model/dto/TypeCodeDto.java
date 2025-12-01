package com.cassinisys.drdo.model.dto;

import com.cassinisys.drdo.model.bom.Item;
import com.cassinisys.drdo.model.bom.ItemType;
import com.cassinisys.drdo.model.bom.ItemTypeSpecs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subra on 20-10-2018.
 */
public class TypeCodeDto {

    private ItemType itemType;

    private List<Item> items = new ArrayList<>();

    private List<ItemTypeSpecs> itemTypeSpecs = new ArrayList<>();

    public ItemType getItemType() {

        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<ItemTypeSpecs> getItemTypeSpecs() {
        return itemTypeSpecs;
    }

    public void setItemTypeSpecs(List<ItemTypeSpecs> itemTypeSpecs) {
        this.itemTypeSpecs = itemTypeSpecs;
    }
}
