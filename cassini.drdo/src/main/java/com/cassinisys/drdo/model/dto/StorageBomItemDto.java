package com.cassinisys.drdo.model.dto;

import com.cassinisys.drdo.model.bom.BomItem;
import com.cassinisys.drdo.model.inventory.Storage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subra on 03-02-2019.
 */
public class StorageBomItemDto {

    private BomItem bomItem;

    private List<Storage> onHoldStorages = new ArrayList<>();
    private List<Storage> returnStorages = new ArrayList<>();
    private List<Storage> inventoryStorages = new ArrayList<>();

    public BomItem getBomItem() {
        return bomItem;
    }

    public void setBomItem(BomItem bomItem) {
        this.bomItem = bomItem;
    }

    public List<Storage> getOnHoldStorages() {
        return onHoldStorages;
    }

    public void setOnHoldStorages(List<Storage> onHoldStorages) {
        this.onHoldStorages = onHoldStorages;
    }

    public List<Storage> getReturnStorages() {
        return returnStorages;
    }

    public void setReturnStorages(List<Storage> returnStorages) {
        this.returnStorages = returnStorages;
    }

    public List<Storage> getInventoryStorages() {
        return inventoryStorages;
    }

    public void setInventoryStorages(List<Storage> inventoryStorages) {
        this.inventoryStorages = inventoryStorages;
    }
}
