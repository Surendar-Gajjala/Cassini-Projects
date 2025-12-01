package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by Suresh CassiniPLM on 27-10-2020.
 */
public class ToolCriteria extends Criteria {
    Integer toolType;
    String name;
    String number;
    String description;
    String searchQuery;
    Integer workOrder;
    Integer bopRoute;

    public Integer getBopRoute() {
        return bopRoute;
    }

    public void setBopRoute(Integer bopRoute) {
        this.bopRoute = bopRoute;
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

    public Integer getToolType() {
        return toolType;
    }

    public void setToolType(Integer toolType) {
        this.toolType = toolType;
    }

    public Integer getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(Integer workOrder) {
        this.workOrder = workOrder;
    }
}