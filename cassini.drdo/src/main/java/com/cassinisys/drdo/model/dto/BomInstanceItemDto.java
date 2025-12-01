package com.cassinisys.drdo.model.dto;

import com.cassinisys.drdo.model.bom.BomItemType;
import com.cassinisys.drdo.model.bom.ItemInstance;
import com.cassinisys.drdo.model.bom.ItemRevision;
import com.cassinisys.drdo.model.bom.LotInstance;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam reddy on 20-09-2019.
 */
public class BomInstanceItemDto {

    private ItemRevision itemRevision;

    private BomItemType bomItemType;

    private Double quantity = 0.0;

    private Double issuedQuantity = 0.0;

    private Double inProcessQty = 0.0;

    private List<ItemInstance> itemInstances = new ArrayList<>();

    private List<LotInstance> lotInstances = new ArrayList<>();

    public ItemRevision getItemRevision() {
        return itemRevision;
    }

    public void setItemRevision(ItemRevision itemRevision) {
        this.itemRevision = itemRevision;
    }

    public BomItemType getBomItemType() {
        return bomItemType;
    }

    public void setBomItemType(BomItemType bomItemType) {
        this.bomItemType = bomItemType;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getIssuedQuantity() {
        return issuedQuantity;
    }

    public void setIssuedQuantity(Double issuedQuantity) {
        this.issuedQuantity = issuedQuantity;
    }

    public Double getInProcessQty() {
        return inProcessQty;
    }

    public void setInProcessQty(Double inProcessQty) {
        this.inProcessQty = inProcessQty;
    }

    public List<ItemInstance> getItemInstances() {
        return itemInstances;
    }

    public void setItemInstances(List<ItemInstance> itemInstances) {
        this.itemInstances = itemInstances;
    }

    public List<LotInstance> getLotInstances() {
        return lotInstances;
    }

    public void setLotInstances(List<LotInstance> lotInstances) {
        this.lotInstances = lotInstances;
    }
}
