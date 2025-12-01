package com.cassinisys.drdo.filtering;

import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by subramanyam reddy on 27-11-2018.
 */
public class InwardItemCriteria extends Criteria {

    private String searchQuery;
    private Integer bom;

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public Integer getBom() {
        return bom;
    }

    public void setBom(Integer bom) {
        this.bom = bom;
    }
}
