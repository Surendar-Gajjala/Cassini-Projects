package com.cassinisys.is.filtering;

import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by anuko on 04-10-2018.
 */
public class SupplierCriteria extends Criteria {

    private String name;
    private String description;
    private Boolean freeTextSearch = false;
    private String searchQuery;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
