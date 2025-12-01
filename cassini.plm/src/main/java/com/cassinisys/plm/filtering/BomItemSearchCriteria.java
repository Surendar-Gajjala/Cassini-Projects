package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by subramanyam reddy on 19-09-2018.
 */
public class BomItemSearchCriteria extends Criteria {

    private String searchQuery;
    private Integer item;
    private String fromDate;
    private String toDate;

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
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
}
