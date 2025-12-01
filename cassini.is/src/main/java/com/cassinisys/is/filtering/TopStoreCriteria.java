package com.cassinisys.is.filtering;
/**
 * The class is for StoreCriteria
 */

import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by Home on 6/15/2016.
 */
public class TopStoreCriteria extends Criteria {

    String storeName;
    String description;
    String locationName;
    Integer person;
    String createdOn;
    private boolean freeTextSearch = false;
    private String searchQuery;

    /**
     * The methods getters and setters are used to get and set values of different classes and data types
     */
    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Integer getPerson() {
        return person;
    }

    public void setPerson(Integer person) {
        this.person = person;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public boolean getFreeTextSearch() {
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
