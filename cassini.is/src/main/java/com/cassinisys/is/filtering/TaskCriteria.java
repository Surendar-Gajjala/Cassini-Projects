package com.cassinisys.is.filtering;
/**
 * The class is for TaskCriteria
 */

import com.cassinisys.platform.filtering.Criteria;

public class TaskCriteria extends Criteria {

    private String name;
    private String description;
    private String percentComplete;
    private String wbsItem;
    private String site;
    private String plannedStartDate;
    private String plannedFinishDate;
    private String actualStartDate;
    private String actualFinishDate;
    private String status;
    private Integer project;
    private Integer person;
    private Integer inspectedBy;
    private String inspectedOn;
    private String inspectionResult;
    private String subContract;
    private Boolean delayTask = false;

    private Boolean freeTextSearch = false;
    private String searchQuery;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlannedStartDate() {
        return plannedStartDate;
    }

    public void setPlannedStartDate(String plannedStartDate) {
        this.plannedStartDate = plannedStartDate;
    }

    public String getPlannedFinishDate() {
        return plannedFinishDate;
    }

    public void setPlannedFinishDate(String plannedFinishDate) {
        this.plannedFinishDate = plannedFinishDate;
    }

    public String getActualStartDate() {
        return actualStartDate;
    }

    public void setActualStartDate(String actualStartDate) {
        this.actualStartDate = actualStartDate;
    }

    public String getActualFinishDate() {
        return actualFinishDate;
    }

    public void setActualFinishDate(String actualFinishDate) {
        this.actualFinishDate = actualFinishDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(String percentComplete) {
        this.percentComplete = percentComplete;
    }

    public String getWbsItem() {
        return wbsItem;
    }

    public void setWbsItem(String wbsItem) {
        this.wbsItem = wbsItem;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }

    public Integer getPerson() {
        return person;
    }

    public void setPerson(Integer person) {
        this.person = person;
    }

    public Integer getInspectedBy() {
        return inspectedBy;
    }

    public void setInspectedBy(Integer inspectedBy) {
        this.inspectedBy = inspectedBy;
    }

    public String getInspectedOn() {
        return inspectedOn;
    }

    public void setInspectedOn(String inspectedOn) {
        this.inspectedOn = inspectedOn;
    }

    public String getInspectionResult() {
        return inspectionResult;
    }

    public void setInspectionResult(String inspectionResult) {
        this.inspectionResult = inspectionResult;
    }

    public String getSubContract() {
        return subContract;
    }

    public void setSubContract(String subContract) {
        this.subContract = subContract;
    }

    public Boolean getDelayTask() {
        return delayTask;
    }

    public void setDelayTask(Boolean delayTask) {
        this.delayTask = delayTask;
    }
}
