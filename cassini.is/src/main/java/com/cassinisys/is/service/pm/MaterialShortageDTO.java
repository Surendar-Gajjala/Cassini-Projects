package com.cassinisys.is.service.pm;

import com.cassinisys.is.model.pm.ResourceType;

/**
 * Created by swapna on 11/07/18.
 */
public class MaterialShortageDTO {

    private Double shortage;

    private Double inventoryQty;

    private Double resourceQty;

    private String taskName;

    private String itemNumber;

    private ResourceType itemType;

    private Integer receivedQty;

    public Double getShortage() {
        return shortage;
    }

    public void setShortage(Double shortage) {
        this.shortage = shortage;
    }

    public Double getInventoryQty() {
        return inventoryQty;
    }

    public void setInventoryQty(Double inventoryQty) {
        this.inventoryQty = inventoryQty;
    }

    public Double getResourceQty() {
        return resourceQty;
    }

    public void setResourceQty(Double resourceQty) {
        this.resourceQty = resourceQty;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public ResourceType getItemType() {
        return itemType;
    }

    public void setItemType(ResourceType itemType) {
        this.itemType = itemType;
    }

    public Integer getReceivedQty() {
        return receivedQty;
    }

    public void setReceivedQty(Integer receivedQty) {
        this.receivedQty = receivedQty;
    }
}
