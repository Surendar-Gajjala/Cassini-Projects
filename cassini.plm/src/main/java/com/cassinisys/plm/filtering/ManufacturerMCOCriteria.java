package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by CassiniSystems on 09-06-2020.
 */
public class ManufacturerMCOCriteria extends Criteria {

    Integer mcoType;
    String mcoNumber;
    String description;
    String searchQuery;
    String title;
    Integer changeAnalyst;
    String status;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public Integer getMcoType() {
        return mcoType;
    }

    public void setMcoType(Integer mcoType) {
        this.mcoType = mcoType;
    }

    public String getMcoNumber() {
        return mcoNumber;
    }

    public void setMcoNumber(String mcoNumber) {
        this.mcoNumber = mcoNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getChangeAnalyst() {
        return changeAnalyst;
    }

    public void setChangeAnalyst(Integer changeAnalyst) {
        this.changeAnalyst = changeAnalyst;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
