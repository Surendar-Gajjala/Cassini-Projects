package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;

public class MBOMCriteria extends Criteria {
    Integer type;
    String name;
    String number;
    String description;
    String searchQuery;
    Integer mco;

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

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

    public Integer getMco() {
        return mco;
    }

    public void setMco(Integer mco) {
        this.mco = mco;
    }
}
