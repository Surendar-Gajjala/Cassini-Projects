package com.cassinisys.is.filtering;
/**
 * The class is for ProjectPersonCriteria
 */

import com.cassinisys.platform.filtering.Criteria;

public class ProjectPersonCriteria extends Criteria {

    private String fullName;
    private String phoneMobile;
    private Integer project;
    private boolean freeTextSearch = false;
    private String searchQuery;

    public String getPhoneMobile() {
        return phoneMobile;
    }

    public void setPhoneMobile(String phoneMobile) {
        this.phoneMobile = phoneMobile;
    }

    /**
     * The methods getters and setters are used to get and set values of different classes and data types
     */
    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

}
