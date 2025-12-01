package com.cassinisys.is.filtering;

import com.cassinisys.platform.filtering.Criteria;

public class SiteCriteria extends Criteria {

    private String name;
    private String description;
    private Boolean freeTextSearch = false;
    private String searchQuery;

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

    /**
     * The methods getters and setters are used to get and set values of different classes and datatypes
     */

    public Boolean getFreeTextSearch() {
        return freeTextSearch;
    }

    public void setFreeTextSearch(Boolean freeTextSearch) {
        this.freeTextSearch = freeTextSearch;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

}
