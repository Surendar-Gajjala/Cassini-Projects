package com.cassinisys.is.filtering;
/**
 * The class is for ItemTypeCriteria
 */

import com.cassinisys.platform.filtering.Criteria;

public class ItemTypeCriteria extends Criteria {

    String name;
    Integer parentType;

    /**
     * The methods getters and setters are used to get and set values of different classes and data types
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentType() {
        return parentType;
    }

    public void setParentType(Integer parentType) {
        this.parentType = parentType;
    }

}
