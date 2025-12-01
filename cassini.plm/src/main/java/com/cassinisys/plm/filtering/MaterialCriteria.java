package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by GSR CassiniPLM on 27-10-2020.
 */
public class MaterialCriteria extends Criteria {
    Integer type;
    String name;
    String number;
    String description;
    String searchQuery;
    Integer workOrder;
    Integer operation;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(Integer workOrder) {
        this.workOrder = workOrder;
    }

    public Integer getOperation() {
        return operation;
    }   

    public void setOperation(Integer operation) {
        this.operation = operation;
    }
}