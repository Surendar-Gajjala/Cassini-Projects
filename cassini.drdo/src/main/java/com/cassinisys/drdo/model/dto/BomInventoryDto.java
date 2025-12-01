package com.cassinisys.drdo.model.dto;

import com.cassinisys.drdo.model.bom.BomItem;
import com.cassinisys.drdo.model.bom.ItemInstance;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by subramanyam reddy on 19-11-2018.
 */
public class BomInventoryDto {

    private BomItem item;

    private List<ItemInstance> itemInstances = new ArrayList<>();

    private List<ItemInstance> returnInstances = new ArrayList<>();

    private List<ItemInstance> stockInstances = new ArrayList<>();

    private List<ItemInstance> onHoldInstances = new ArrayList<>();

    private Integer stock = 0;

    private Double fractionalStock = 0.0;

    private Integer onHold = 0;

    private Double fractionalOnHold = 0.0;

    private Integer returned = 0;

    private Double fractionalReturned = 0.0;

    private List<BomInventoryDto> children = new LinkedList<>();

    private Integer level = 0;

    private Boolean expanded = Boolean.FALSE;

    public BomItem getItem() {
        return item;
    }

    public void setItem(BomItem item) {
        this.item = item;
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

    public Integer getOnHold() {
        return onHold;
    }

    public void setOnHold(Integer onHold) {
        this.onHold = onHold;
    }

    public Double getFractionalOnHold() {
        return fractionalOnHold;
    }

    public void setFractionalOnHold(Double fractionalOnHold) {
        this.fractionalOnHold = fractionalOnHold;
    }

    public Integer getReturned() {
        return returned;
    }

    public void setReturned(Integer returned) {
        this.returned = returned;
    }

    public Double getFractionalReturned() {
        return fractionalReturned;
    }

    public void setFractionalReturned(Double fractionalReturned) {
        this.fractionalReturned = fractionalReturned;
    }

    public List<ItemInstance> getReturnInstances() {
        return returnInstances;
    }

    public void setReturnInstances(List<ItemInstance> returnInstances) {
        this.returnInstances = returnInstances;
    }

    public List<ItemInstance> getStockInstances() {
        return stockInstances;
    }

    public void setStockInstances(List<ItemInstance> stockInstances) {
        this.stockInstances = stockInstances;
    }

    public List<ItemInstance> getOnHoldInstances() {
        return onHoldInstances;
    }

    public void setOnHoldInstances(List<ItemInstance> onHoldInstances) {
        this.onHoldInstances = onHoldInstances;
    }

    public List<BomInventoryDto> getChildren() {
        return children;
    }

    public void setChildren(List<BomInventoryDto> children) {
        this.children = children;
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
}

