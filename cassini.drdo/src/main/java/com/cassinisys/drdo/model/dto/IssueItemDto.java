package com.cassinisys.drdo.model.dto;

import com.cassinisys.drdo.model.bom.ItemInstance;
import com.cassinisys.drdo.model.bom.LotInstance;
import com.cassinisys.drdo.model.transactions.IssueItemStatus;
import com.cassinisys.drdo.model.transactions.RequestItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subra on 12-12-2018.
 */
public class IssueItemDto {

    private RequestItem requestItem;

    private Double allocateQty = 0.0;

    private String scanUpn;

    private String serialNumber;

    private Integer requestQuantity = 0;

    private Double fractionalRequestQuantity = 0.0;

    private Boolean issued = Boolean.FALSE;

    private Boolean notYetApproved = Boolean.FALSE;

    private Boolean approved = Boolean.FALSE;

    private ItemInstance issuedItemInstance;

    private ItemInstance itemInstance;

    private LotInstance lotInstance;

    private List<String> storageLocations = new ArrayList<>();

    private IssueItemStatus status;

    private ItemInstance selectInstanceToIssue;

    private List<ItemInstance> inventoryInstances = new ArrayList<>();

    public RequestItem getRequestItem() {
        return requestItem;
    }

    public void setRequestItem(RequestItem requestItem) {
        this.requestItem = requestItem;
    }

    public Double getAllocateQty() {
        return allocateQty;
    }

    public void setAllocateQty(Double allocateQty) {
        this.allocateQty = allocateQty;
    }

    public String getScanUpn() {
        return scanUpn;
    }

    public void setScanUpn(String scanUpn) {
        this.scanUpn = scanUpn;
    }

    public Integer getRequestQuantity() {
        return requestQuantity;
    }

    public void setRequestQuantity(Integer requestQuantity) {
        this.requestQuantity = requestQuantity;
    }

    public Double getFractionalRequestQuantity() {
        return fractionalRequestQuantity;
    }

    public void setFractionalRequestQuantity(Double fractionalRequestQuantity) {
        this.fractionalRequestQuantity = fractionalRequestQuantity;
    }

    public Boolean getIssued() {
        return issued;
    }

    public void setIssued(Boolean issued) {
        this.issued = issued;
    }

    public ItemInstance getIssuedItemInstance() {
        return issuedItemInstance;
    }

    public void setIssuedItemInstance(ItemInstance issuedItemInstance) {
        this.issuedItemInstance = issuedItemInstance;
    }

    public ItemInstance getItemInstance() {
        return itemInstance;
    }

    public void setItemInstance(ItemInstance itemInstance) {
        this.itemInstance = itemInstance;
    }

    public List<String> getStorageLocations() {
        return storageLocations;
    }

    public void setStorageLocations(List<String> storageLocations) {
        this.storageLocations = storageLocations;
    }

    public LotInstance getLotInstance() {
        return lotInstance;
    }

    public void setLotInstance(LotInstance lotInstance) {
        this.lotInstance = lotInstance;
    }

    public Boolean getNotYetApproved() {
        return notYetApproved;
    }

    public void setNotYetApproved(Boolean notYetApproved) {
        this.notYetApproved = notYetApproved;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public IssueItemStatus getStatus() {
        return status;
    }

    public void setStatus(IssueItemStatus status) {
        this.status = status;
    }

    public ItemInstance getSelectInstanceToIssue() {
        return selectInstanceToIssue;
    }

    public void setSelectInstanceToIssue(ItemInstance selectInstanceToIssue) {
        this.selectInstanceToIssue = selectInstanceToIssue;
    }

    public List<ItemInstance> getInventoryInstances() {
        return inventoryInstances;
    }

    public void setInventoryInstances(List<ItemInstance> inventoryInstances) {
        this.inventoryInstances = inventoryInstances;
    }
}
