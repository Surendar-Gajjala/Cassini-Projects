package com.cassinisys.tm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import java.util.Date;

/**
 * Created by Rajabrahmachary on 21-07-2016.
 */
public class ShiftCriteria extends Criteria {


    private  String name;
    private Date startTime;
    private Date endTime;
    private boolean freeTextSearch = false;
    private String searchQuery;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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
