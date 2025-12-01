package com.cassinisys.is.filtering;

import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by swapna on 31/12/18.
 */
public class ISLoanCriteria extends Criteria {

    private String loanNumber;
    private boolean freeTextSearch = false;
    private String searchQuery;

    public String getLoanNumber() {
        return loanNumber;
    }

    public void setLoanNumber(String loanNumber) {
        this.loanNumber = loanNumber;
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
