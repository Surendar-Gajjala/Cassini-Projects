package com.cassinisys.tm.filtering;

import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by CassiniSystems on 03-08-2016.
 */
public class SuitPersonCriteria extends Criteria {

    private String suite;
    private String person;
    private boolean freeTextSearch = false;
    private String searchQuery;

    public String getSuite() {
        return suite;
    }

    public void setSuite(String suite) {
        this.suite = suite;
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
