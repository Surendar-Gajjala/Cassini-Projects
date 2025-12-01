package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by Lenovo on 28-10-2020.
 */
public class EquipmentCriteria extends Criteria{

    Integer equipmentType;
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

    public Integer getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(Integer equipmentType) {
        this.equipmentType = equipmentType;
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
}
