package com.cassinisys.is.filtering;

import com.cassinisys.is.model.store.AttributeSearchDto;
import com.cassinisys.platform.filtering.Criteria;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swapna on 25/07/18.
 */

@Component
public class TopInventoryCriteria extends Criteria {

    Integer storeId;

    String itemNumber;

    String itemName;

    String itemDescription;

    String units;

    Integer projectId;

    private Boolean freeTextSearch = false;

    private String searchQuery;

    private Integer itemType;

    private Boolean attributeSearch = false;

    private List<Integer> attributeIds = new ArrayList<>();

    private List<AttributeSearchDto> searchAttributes = new ArrayList<>();

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
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

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public Boolean getFreeTextSearch() {
        return freeTextSearch;
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

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
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

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }
}
