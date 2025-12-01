package com.cassinisys.drdo.model.dto;

import com.cassinisys.drdo.model.bom.BomItem;
import com.cassinisys.drdo.model.transactions.InwardItemInstance;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subra on 30-07-2019.
 */
public class GatePassDetailsDto {

    private BomItem bomItem;

    private Integer quantity = 0;

    private Double fractionalQuantity = 0.0;

    private List<InwardItemInstance> inwardItemInstances = new ArrayList<>();

    public BomItem getBomItem() {
        return bomItem;
    }

    public void setBomItem(BomItem bomItem) {
        this.bomItem = bomItem;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getFractionalQuantity() {
        return fractionalQuantity;
    }

    public void setFractionalQuantity(Double fractionalQuantity) {
        this.fractionalQuantity = fractionalQuantity;
    }

    public List<InwardItemInstance> getInwardItemInstances() {
        return inwardItemInstances;
    }

    public void setInwardItemInstances(List<InwardItemInstance> inwardItemInstances) {
        this.inwardItemInstances = inwardItemInstances;
    }
}
