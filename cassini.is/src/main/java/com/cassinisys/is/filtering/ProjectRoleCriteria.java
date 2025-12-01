package com.cassinisys.is.filtering;
/**
 * The class is for ProjectRoleCriteria
 */

import com.cassinisys.platform.filtering.Criteria;

public class ProjectRoleCriteria extends Criteria {

    private String role;
    private boolean freeTextSearch = false;
    private String searchQuery;

    /**
     * The methods getters and setters are used to get and set values of different classes and data types
     */
    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isFreeTextSearch() {
        return freeTextSearch;
    }

    public void setFreeTextSearch(boolean freeTextSearch) {
        this.freeTextSearch = freeTextSearch;
    }

}
