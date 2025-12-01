package com.cassinisys.platform.filtering;

/**
 * Created by GSR CassiniPLM on 27-10-2020.
 */

public class CustomObjectCriteria extends Criteria {
    Integer type;
    Integer customType;
    String name;
    String number;
    String description;
    String searchQuery;
    Integer object;
    Integer bomObject;
    Integer supplier;
    Boolean related = Boolean.FALSE;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public Integer getObject() {
        return object;
    }

    public void setObject(Integer object) {
        this.object = object;
    }

    public Integer getBomObject() {
        return bomObject;
    }

    public void setBomObject(Integer bomObject) {
        this.bomObject = bomObject;
    }

    public Boolean getRelated() {
        return related;
    }

    public void setRelated(Boolean related) {
        this.related = related;
    }

    public Integer getCustomType() {
        return customType;
    }

    public void setCustomType(Integer customType) {
        this.customType = customType;
    }

    public Integer getSupplier() {
        return supplier;
    }

    public void setSupplier(Integer supplier) {
        this.supplier = supplier;
    }
}