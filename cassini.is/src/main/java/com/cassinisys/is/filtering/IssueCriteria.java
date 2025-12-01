package com.cassinisys.is.filtering;
/**
 * The class is for IssueCriteria
 */

import com.cassinisys.platform.filtering.Criteria;

public class IssueCriteria extends Criteria {
    private String targetObjectId;
    private String targetObjectType;
    private String type;
    private String priority;
    private String title;
    private String assignedTo;
    private Boolean freeTextSearch = false;
    private String searchQuery;
    private String resolution;
    private String status;

    /**
     * The methods getters and setters are used to get and set values of different classes and datatypes
     */

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public Boolean getFreeTextSearch() {
        return freeTextSearch;
    }

    public void setFreeTextSearch(Boolean freeTextSearch) {
        this.freeTextSearch = freeTextSearch;
    }

    public String getTargetObjectId() {
        return targetObjectId;
    }

    public void setTargetObjectId(String targetObjectId) {
        this.targetObjectId = targetObjectId;
    }

    public String getTargetObjectType() {
        return targetObjectType;
    }

    public void setTargetObjectType(String targetObjectType) {
        this.targetObjectType = targetObjectType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
