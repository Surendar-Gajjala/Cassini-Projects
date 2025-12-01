package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by Nageshreddy on 09-06-2016.
 */
public class ECOCriteria extends Criteria {

    String ecoNumber;
    String title;
    String ecoOwner;
    String description;
    String status;
    String statusType;
    Integer type;
    String ecoType;
    private boolean freeTextSearch = false;
    private String searchQuery;

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

    public String getEcoNumber() {
        return ecoNumber;
    }

    public void setEcoNumber(String ecoNumber) {
        this.ecoNumber = ecoNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEcoOwner() {
        return ecoOwner;
    }

    public void setEcoOwner(String ecoOwner) {
        this.ecoOwner = ecoOwner;
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

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
    public String getEcoType() {
        return ecoType;
    }

    public void setEcoType(String ecoType) {
        this.ecoType = ecoType;
    }
}
