package com.cassinisys.drdo.model.dto;

import com.cassinisys.drdo.model.bom.BomInstanceItem;
import com.cassinisys.drdo.model.bom.ItemInstance;
import com.cassinisys.drdo.model.bom.LotInstance;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam reddy on 19-11-2018.
 */
public class BomInstanceInventoryDto {

    private BomInstanceItem item;

    private Integer requested = 0;

    private Double fractionalRequested = 0.0;

    private Integer issued = 0;

    private Double fractionalIssued = 0.0;

    private Double allocatedQty = 0.0;

    private List<ItemInstance> issuedInstances = new ArrayList<>();

    private List<LotInstance> issuedLotInstances = new ArrayList<>();

    private List<BomInstanceInventoryDto> children = new ArrayList<>();

    private Integer level = 0;

    private Boolean expanded = Boolean.FALSE;

    public BomInstanceItem getItem() {
        return item;
    }

    public void setItem(BomInstanceItem item) {
        this.item = item;
    }

    public Integer getRequested() {
        return requested;
    }

    public void setRequested(Integer requested) {
        this.requested = requested;
    }

    public Double getFractionalRequested() {
        return fractionalRequested;
    }

    public void setFractionalRequested(Double fractionalRequested) {
        this.fractionalRequested = fractionalRequested;
    }

    public Integer getIssued() {
        return issued;
    }

    public void setIssued(Integer issued) {
        this.issued = issued;
    }

    public Double getFractionalIssued() {
        return fractionalIssued;
    }

    public void setFractionalIssued(Double fractionalIssued) {
        this.fractionalIssued = fractionalIssued;
    }

    public List<ItemInstance> getIssuedInstances() {
        return issuedInstances;
    }

    public void setIssuedInstances(List<ItemInstance> issuedInstances) {
        this.issuedInstances = issuedInstances;
    }

    public List<LotInstance> getIssuedLotInstances() {
        return issuedLotInstances;
    }

    public void setIssuedLotInstances(List<LotInstance> issuedLotInstances) {
        this.issuedLotInstances = issuedLotInstances;
    }

    public Double getAllocatedQty() {
        return allocatedQty;
    }

    public void setAllocatedQty(Double allocatedQty) {
        this.allocatedQty = allocatedQty;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Boolean getExpanded() {
        return expanded;
    }

    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }

    public List<BomInstanceInventoryDto> getChildren() {
        return children;
    }

    public void setChildren(List<BomInstanceInventoryDto> children) {
        this.children = children;
    }
}

