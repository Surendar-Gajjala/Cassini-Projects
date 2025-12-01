package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by Suresh Cassini on 18-11-2020.
 */
public class SparePartCriteria extends Criteria {
    Integer partType;
    String name;
    String number;
    String description;
    String searchQuery;
    Integer workOrder;
    Integer asset;

    public Integer getPartType() {
        return partType;
    }

    public void setPartType(Integer partType) {
        this.partType = partType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public Integer getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(Integer workOrder) {
        this.workOrder = workOrder;
    }

    public Integer getAsset() {
        return asset;
    }

    public void setAsset(Integer asset) {
        this.asset = asset;
    }
}