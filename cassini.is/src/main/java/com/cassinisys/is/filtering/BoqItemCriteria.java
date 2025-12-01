package com.cassinisys.is.filtering;
/**
 * The class is for BoqItemCriteria
 */

import com.cassinisys.platform.filtering.Criteria;

public class BoqItemCriteria extends Criteria {

    Integer boq;
    Integer project;
    String itemNumber;
    String itemName;
    String itemType;
    String itemDescription;
    String units;
    String unitPrice;
    String quantity;
    String boqName;
    private boolean freeTextSearch = false;
    private String searchQuery;

    /**
     * The methods getters and setters are used to get and set values of different classes and datatypes
     */

    public Integer getBoq() {
        return boq;
    }

    public void setBoq(Integer boq) {
        this.boq = boq;
    }

    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getboqName() {
        return boqName;
    }

    public void setboqName(String boqName) {
        this.boqName = boqName;
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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
}
