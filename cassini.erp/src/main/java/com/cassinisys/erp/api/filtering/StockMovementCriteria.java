package com.cassinisys.erp.api.filtering;


import com.cassinisys.erp.model.production.InventoryType;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class StockMovementCriteria extends Criteria {

    String itemName;

    String projectName;

    String itemNumber;

    Integer quantity;

    Integer itemType;

    Integer storeId;

    InventoryType movementType;

    String description;

    Date timeStamp;

    private Boolean freeTextSearch = false;
    private String searchQuery;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setFreeTextSearch(Boolean freeTextSearch) {
        this.freeTextSearch = freeTextSearch;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public Boolean getFreeTextSearch() {
        return freeTextSearch;
    }

    public InventoryType getMovementType() {
        return movementType;
    }

    public void setMovementType(InventoryType movementType) {
        this.movementType = movementType;
    }
}
