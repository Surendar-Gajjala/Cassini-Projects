package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;

public class WorkCenterCriteria extends Criteria {

    String number;
    String name;
    Integer type;
    String description;
    String searchQuery;
    Boolean assemblyLine = Boolean.FALSE;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Boolean getAssemblyLine() {
        return assemblyLine;
    }

    public void setAssemblyLine(Boolean assemblyLine) {
        this.assemblyLine = assemblyLine;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
