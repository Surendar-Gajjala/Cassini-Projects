package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.plm.model.dto.AttributeSearchDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subra on 23-10-2018.
 */
public class RequirementSearchCriteria extends Criteria {

    private String name;

    private Integer version;

    private String description;

    private Integer[] assignedTo;

    private String status;

    private String objectNumber;

    private String searchQuery;

    private Integer specification;

    private String plannedFinishdate;

    private Boolean attributeSearch = false;

    private List<Integer> attributeId = new ArrayList<>();

    private List<AttributeSearchDto> searchAttributes = new ArrayList<>();

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObjectNumber() {
        return objectNumber;
    }

    public void setObjectNumber(String objectNumber) {
        this.objectNumber = objectNumber;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public Integer getSpecification() {
        return specification;
    }

    public void setSpecification(Integer specification) {
        this.specification = specification;
    }

    public String getPlannedFinishdate() {
        return plannedFinishdate;
    }

    public void setPlannedFinishdate(String plannedFinishdate) {
        this.plannedFinishdate = plannedFinishdate;
    }

    public Integer[] getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Integer[] assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean getAttributeSearch() {
        return attributeSearch;
    }

    public void setAttributeSearch(Boolean attributeSearch) {
        this.attributeSearch = attributeSearch;
    }

    public List<Integer> getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(List<Integer> attributeId) {
        this.attributeId = attributeId;
    }

    public List<AttributeSearchDto> getSearchAttributes() {
        return searchAttributes;
    }

    public void setSearchAttributes(List<AttributeSearchDto> searchAttributes) {
        this.searchAttributes = searchAttributes;
    }
}

