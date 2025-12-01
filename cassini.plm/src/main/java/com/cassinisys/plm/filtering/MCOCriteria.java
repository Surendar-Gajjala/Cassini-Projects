package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by CassiniSystems on 09-06-2020.
 */
public class MCOCriteria extends Criteria {

    Integer mcoType;
    String mcoNumber;
    String description;
    String searchQuery;
    String title;
    Integer type;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
