package com.cassinisys.drdo.filtering;

import com.cassinisys.drdo.model.DRDOObjectType;
import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by subramanyam reddy on 04-03-2019.
 */
public class ItemInstanceCriteria extends Criteria {

    private String searchQuery;

    private DRDOObjectType objectType;

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public DRDOObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(DRDOObjectType objectType) {
        this.objectType = objectType;
    }
}
