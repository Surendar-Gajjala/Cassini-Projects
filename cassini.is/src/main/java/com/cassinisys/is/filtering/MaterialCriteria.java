package com.cassinisys.is.filtering;

import com.cassinisys.is.model.store.AttributeSearchDto;
import com.cassinisys.platform.filtering.Criteria;

import java.util.ArrayList;
import java.util.List;

public class MaterialCriteria extends Criteria {

    String itemType;
    String itemNumber;
    String itemName;
    String description;
    String units;
    private boolean freeTextSearch = false;
    private String searchQuery;

    private Boolean attributeSearch = false;
    private List<Integer> attributeIds = new ArrayList<>();
    private List<AttributeSearchDto> searchAttributes = new ArrayList<>();

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

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
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

    public Boolean getAttributeSearch() {
        return attributeSearch;
    }

    public void setAttributeSearch(Boolean attributeSearch) {
        this.attributeSearch = attributeSearch;
    }

    public List<Integer> getAttributeIds() {
        return attributeIds;
    }

    public void setAttributeIds(List<Integer> attributeIds) {
        this.attributeIds = attributeIds;
    }

    public List<AttributeSearchDto> getSearchAttributes() {
        return searchAttributes;
    }

    public void setSearchAttributes(List<AttributeSearchDto> searchAttributes) {
        this.searchAttributes = searchAttributes;
    }
}