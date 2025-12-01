package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by subramanyamreddy on 018 18-May -17.
 */
public class LifeCycleCriteria extends Criteria {

    String name;

    public LifeCycleCriteria() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
