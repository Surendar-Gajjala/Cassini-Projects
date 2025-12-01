package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by GSR.
 */
public class WorkflowCriteria extends Criteria {

    private String type;

    private Integer personId;

    private String searchQuery;

    private Integer typeId;

    private Enum objectType;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Enum getObjectType() {
        return objectType;
    }

    public void setObjectType(Enum objectType) {
        this.objectType = objectType;
    }
}
