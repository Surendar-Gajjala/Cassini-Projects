package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;

import java.util.ArrayList;
import java.util.List;

public class SharedObjectCriteria extends Criteria {

    String searchQuery;
    String sharedObjectType;
    List<Integer> personIds = new ArrayList<>();
    Integer person;

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getSharedObjectType() {
        return sharedObjectType;
    }

    public void setSharedObjectType(String sharedObjectType) {
        this.sharedObjectType = sharedObjectType;
    }

    public List<Integer> getPersonIds() {
        return personIds;
    }

    public void setPersonIds(List<Integer> personIds) {
        this.personIds = personIds;
    }

    public Integer getPerson() {
        return person;
    }

    public void setPerson(Integer person) {
        this.person = person;
    }
}
