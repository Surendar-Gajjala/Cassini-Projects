package com.cassinisys.is.filtering;
/**
 * The class is for ItemCriteria
 */

import com.cassinisys.platform.filtering.Criteria;

public class ItemCriteria extends Criteria {

    String itemType;
    String itemNumber;
    String active;
    String unitPrice;
    String unitCost;
    String description;
    String createdDate;
    String modifiedDate;
    private boolean freeTextSearch = false;
    private String searchQuery;

    /**
     * The methods getters and setters are used to get and set values of different classes and data types
     */
    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(String unitCost) {
        this.unitCost = unitCost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public boolean isFreeTextSearch() {
        return freeTextSearch;
    }

    public void setFreeTextSearch(boolean freeTextSearch) {
        this.freeTextSearch = freeTextSearch;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }
}