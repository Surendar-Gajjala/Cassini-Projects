package com.cassinisys.is.filtering;

import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by swapna on 31/12/18.
 */
public class StockReturnCriteria extends Criteria {

    private String notes;
    private String returnNumberSource;
    private String approvedBy;
    private boolean freeTextSearch = false;
    private String searchQuery;

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getReturnNumberSource() {
        return returnNumberSource;
    }

    public void setReturnNumberSource(String returnNumberSource) {
        this.returnNumberSource = returnNumberSource;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
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
