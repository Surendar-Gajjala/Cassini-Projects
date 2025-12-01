package com.cassinisys.is.filtering;

import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by swapna on 31/12/18.
 */
public class ScrapRequestCriteria extends Criteria {

    private String scrapNumber;
    private String notes;
    private boolean freeTextSearch = false;
    private String searchQuery;

    public String getScrapNumber() {
        return scrapNumber;
    }

    public void setScrapNumber(String scrapNumber) {
        this.scrapNumber = scrapNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
