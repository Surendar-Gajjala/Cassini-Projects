package com.cassinisys.is.filtering;

import com.cassinisys.is.model.store.AttributeSearchDto;
import com.cassinisys.platform.filtering.Criteria;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swapna on 31/12/18.
 */
public class ISStockIssueCriteria extends Criteria {

    private Integer storeId;
    private Integer materialIssueType;

    private boolean freeTextSearch = false;
    private String searchQuery;
    private String name;
    private String notes;
    private String issueNumberSource;
    private String fromDate;
    private String toDate;
    private String project;

    private Boolean attributeSearch = false;
    private List<Integer> attributeIds = new ArrayList<>();
    private List<AttributeSearchDto> searchAttributes = new ArrayList<>();

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public Integer getMaterialIssueType() {
        return materialIssueType;
    }

    public void setMaterialIssueType(Integer materialIssueType) {
        this.materialIssueType = materialIssueType;
    }

    public Boolean getAttributeSearch() {
        return attributeSearch;
    }

    public void setAttributeSearch(Boolean attributeSearch) {
        this.attributeSearch = attributeSearch;
    }

    public List<Integer> getAttributeIds() {
        return attributeIds;
    }

    public void setAttributeIds(List<Integer> attributeIds) {
        this.attributeIds = attributeIds;
    }

    public List<AttributeSearchDto> getSearchAttributes() {
        return searchAttributes;
    }

    public void setSearchAttributes(List<AttributeSearchDto> searchAttributes) {
        this.searchAttributes = searchAttributes;
    }

    public boolean isFreeTextSearch() {
        return freeTextSearch;
    }

    public void setFreeTextSearch(boolean freeTextSearch) {
        this.freeTextSearch = freeTextSearch;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getIssueNumberSource() {
        return issueNumberSource;
    }

    public void setIssueNumberSource(String issueNumberSource) {
        this.issueNumberSource = issueNumberSource;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }
}
