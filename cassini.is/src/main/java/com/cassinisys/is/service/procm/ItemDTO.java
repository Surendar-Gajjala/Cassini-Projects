package com.cassinisys.is.service.procm;

import com.cassinisys.is.model.pm.ISProjectResource;
import com.cassinisys.is.model.procm.ISMaterialType;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

/**
 * Created by swapna on 16/08/18.
 */
public class ItemDTO {

    private Integer id;

    private String itemNumber;

    private String itemType;

    private String itemName;

    private String description;

    private Double resourceQuantity;

    private String resourceType;

    private Double itemReceiveQuantity;

    private Double itemIssueQuantity;

    private Double itemReturnQuantity;

    private String units;

    private Double storeInventory;

    private Integer boqReference;

    private Integer person;

    private Double shortage;

    private ISProjectResource projectResource;

    private String supplierName;

    private String issuedToName;

    private ISMaterialType materialType;

    private int totalPages;

    private int totalElements;

    private int numberOfElements;

    private boolean editAttribute;
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    private Date timeStamp;

    public boolean isEditAttribute() {
        return editAttribute;
    }

    public void setEditAttribute(boolean editAttribute) {
        this.editAttribute = editAttribute;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getResourceQuantity() {
        return resourceQuantity;
    }

    public void setResourceQuantity(Double resourceQuantity) {
        this.resourceQuantity = resourceQuantity;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public Double getItemReceiveQuantity() {
        return itemReceiveQuantity;
    }

    public void setItemReceiveQuantity(Double itemReceiveQuantity) {
        this.itemReceiveQuantity = itemReceiveQuantity;
    }

    public Double getItemIssueQuantity() {
        return itemIssueQuantity;
    }

    public void setItemIssueQuantity(Double itemIssueQuantity) {
        this.itemIssueQuantity = itemIssueQuantity;
    }

    public Double getItemReturnQuantity() {
        return itemReturnQuantity;
    }

    public void setItemReturnQuantity(Double itemReturnQuantity) {
        this.itemReturnQuantity = itemReturnQuantity;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Double getStoreInventory() {
        return storeInventory;
    }

    public void setStoreInventory(Double storeInventory) {
        this.storeInventory = storeInventory;
    }

    public Integer getBoqReference() {
        return boqReference;
    }

    public void setBoqReference(Integer boqReference) {
        this.boqReference = boqReference;
    }

    public Integer getPerson() {
        return person;
    }

    public void setPerson(Integer person) {
        this.person = person;
    }

    public Double getShortage() {
        return shortage;
    }

    public void setShortage(Double shortage) {
        this.shortage = shortage;
    }

    public ISProjectResource getProjectResource() {
        return projectResource;
    }

    public void setProjectResource(ISProjectResource projectResource) {
        this.projectResource = projectResource;
    }

    public ISMaterialType getMaterialType() {
        return materialType;
    }

    public void setMaterialType(ISMaterialType materialType) {
        this.materialType = materialType;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getIssuedToName() {
        return issuedToName;
    }

    public void setIssuedToName(String issuedToName) {
        this.issuedToName = issuedToName;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }
}
