package com.cassinisys.drdo.model.dto;

import com.cassinisys.drdo.model.bom.BomItem;
import com.cassinisys.drdo.model.bom.ItemInstance;
import com.cassinisys.drdo.model.inventory.ItemAllocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nageshreddy on 10-12-2018.
 */
public class BomAllocationDto {

    private BomItem item;

    private Map<Integer, ItemAllocation> listMap = new HashMap();

    private List<ItemInstance> itemInstances = new ArrayList();

    private Integer stock = 0;

    private Double fractionalStock = 0.0;

    private Double shortage = 0.0;

    private Double fractionalShortage = 0.0;

    private Integer level = 0;

    private Double allocated = 0.0;

    private Double itemIssued = 0.0;

    private Double issuedQty = 0.0;

    private Double commonIssuedQty = 0.0;

    private Double commonAllocated = 0.0;

    private Boolean expanded = Boolean.FALSE;

    private List<BomAllocationDto> children = new ArrayList<>();

    public BomAllocationDto() {
    }

    public BomAllocationDto(BomAllocationDto bomAllocationDto) {
        this.item = bomAllocationDto.getItem();
        this.listMap = bomAllocationDto.getListMap();
        this.itemInstances = bomAllocationDto.getItemInstances();
        this.stock = bomAllocationDto.getStock();
        this.fractionalStock = bomAllocationDto.getFractionalStock();
        this.shortage = bomAllocationDto.getShortage();
        this.fractionalShortage = bomAllocationDto.getFractionalShortage();
        this.level = bomAllocationDto.getLevel();
        this.allocated = bomAllocationDto.getAllocated();
        this.issuedQty = bomAllocationDto.getIssuedQty();
        this.commonIssuedQty = bomAllocationDto.getCommonIssuedQty();
        this.commonAllocated = bomAllocationDto.getCommonAllocated();
        this.expanded = bomAllocationDto.getExpanded();
        this.itemIssued = bomAllocationDto.getItemIssued();
    }

    public BomItem getItem() {
        return item;
    }

    public void setItem(BomItem item) {
        this.item = item;
    }

    public Map<Integer, ItemAllocation> getListMap() {
        return listMap;
    }

    public void setListMap(Map<Integer, ItemAllocation> listMap) {
        this.listMap = listMap;
    }

    public List<ItemInstance> getItemInstances() {
        return itemInstances;
    }

    public void setItemInstances(List<ItemInstance> itemInstances) {
        this.itemInstances = itemInstances;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Double getFractionalStock() {
        return fractionalStock;
    }

    public void setFractionalStock(Double fractionalStock) {
        this.fractionalStock = fractionalStock;
    }

    public Double getShortage() {
        return shortage;
    }

    public void setShortage(Double shortage) {
        this.shortage = shortage;
    }

    public Double getFractionalShortage() {
        return fractionalShortage;
    }

    public void setFractionalShortage(Double fractionalShortage) {
        this.fractionalShortage = fractionalShortage;
    }

    public Boolean getExpanded() {
        return expanded;
    }

    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }

    public Integer getLevel() {

        return level;
    }

    public Double getIssuedQty() {
        return issuedQty;
    }

    public void setIssuedQty(Double issuedQty) {
        this.issuedQty = issuedQty;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public List<BomAllocationDto> getChildren() {
        return children;
    }

    public void setChildren(List<BomAllocationDto> children) {
        this.children = children;
    }

    public Double getAllocated() {
        return allocated;
    }

    public void setAllocated(Double allocated) {
        this.allocated = allocated;
    }

    public Double getCommonAllocated() {
        return commonAllocated;
    }

    public void setCommonAllocated(Double commonAllocated) {
        this.commonAllocated = commonAllocated;
    }

    public Double getCommonIssuedQty() {
        return commonIssuedQty;
    }

    public void setCommonIssuedQty(Double commonIssuedQty) {
        this.commonIssuedQty = commonIssuedQty;
    }

    public Double getItemIssued() {
        return itemIssued;
    }

    public void setItemIssued(Double itemIssued) {
        this.itemIssued = itemIssued;
    }
}
