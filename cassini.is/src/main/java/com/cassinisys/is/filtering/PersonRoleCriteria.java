package com.cassinisys.is.filtering;
/**
 * The class is for PersonRoleCriteria
 */

import com.cassinisys.platform.filtering.Criteria;

public class PersonRoleCriteria extends Criteria {

    private Integer person;
    private Integer project;
    /* private Integer role;*/
    private boolean freeTextSearch = false;
    private String searchQuery;

    /**
     * The methods getters and setters are used to get and set values of different classes and data types
     */
    public Integer getPerson() {
        return person;
    }

    public void setPerson(Integer person) {
        this.person = person;
    }

    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }

  /*  public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }*/

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
