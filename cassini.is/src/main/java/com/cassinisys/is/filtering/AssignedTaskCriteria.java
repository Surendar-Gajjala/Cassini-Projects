package com.cassinisys.is.filtering;

import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by subra on 24-09-2018.
 */
public class AssignedTaskCriteria extends Criteria {

    private Integer person;

    private String assignedType;

    private String searchQuery;

    public Integer getPerson() {
        return person;
    }

    public void setPerson(Integer person) {
        this.person = person;
    }

    public String getAssignedType() {
        return assignedType;
    }

    public void setAssignedType(String assignedType) {
        this.assignedType = assignedType;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }
}
