package com.cassinisys.tm.filtering;

import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by Rajabrahmachary on 21-07-2016.
 */
public class ShiftPersonCriteria extends Criteria {

    private String shift;
    private String person;
    private boolean freeTextSearch = false;
    private String searchQuery;


    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
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
