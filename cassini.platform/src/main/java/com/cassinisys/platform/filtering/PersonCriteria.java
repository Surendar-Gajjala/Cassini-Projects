package com.cassinisys.platform.filtering;


import java.util.ArrayList;
import java.util.List;

public class PersonCriteria extends Criteria {

    private String firstName;
    private String phoneNumber;
    private Integer objectPerson;
    private List<Integer> personIds = new ArrayList<>();
    private List<Integer> filterIds = new ArrayList<>();
    private Boolean filterPersons = false;

    public String getPersonType() {
        return personType;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }

    private String personType;
    private boolean freeTextSearch = false;
    private String searchQuery;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public Integer getObjectPerson() {
        return objectPerson;
    }

    public void setObjectPerson(Integer objectPerson) {
        this.objectPerson = objectPerson;
    }

    public List<Integer> getPersonIds() {
        return personIds;
    }

    public void setPersonIds(List<Integer> personIds) {
        this.personIds = personIds;
    }

    public List<Integer> getFilterIds() {
        return filterIds;
    }

    public void setFilterIds(List<Integer> filterIds) {
        this.filterIds = filterIds;
    }

    public Boolean getFilterPersons() {
        return filterPersons;
    }

    public void setFilterPersons(Boolean filterPersons) {
        this.filterPersons = filterPersons;
    }
}
