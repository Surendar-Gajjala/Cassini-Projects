package com.cassinisys.drdo.filtering;

import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by Nageshreddy on 27-10-2018.
 */
public class GatePassCriteria extends Criteria {

    private String searchQuery;
    private String gatePassNumber;
    private String gatePassName;
    private Boolean finish = false;
    private boolean freeTextSearch = false;
    private String fromDate;
    private String toDate;
    private String month;

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getGatePassNumber() {
        return gatePassNumber;
    }

    public void setGatePassNumber(String gatePassNumber) {
        this.gatePassNumber = gatePassNumber;
    }

    public String getGatePassName() {
        return gatePassName;
    }

    public void setGatePassName(String gatePassName) {
        this.gatePassName = gatePassName;
    }

    public boolean isFreeTextSearch() {
        return freeTextSearch;
    }

    public void setFreeTextSearch(boolean freeTextSearch) {
        this.freeTextSearch = freeTextSearch;
    }

    public Boolean getFinish() {
        return finish;
    }

    public void setFinish(Boolean finish) {
        this.finish = finish;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
