package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by GSR  on 27-10-2020.
 */
public class SubstanceCriteria extends Criteria {
    Integer type;
    String name;
    String number;
    String description;
    String casNumber;
    String searchQuery;
    Integer declarationPart;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCasNumber() {
        return casNumber;
    }

    public void setCasNumber(String casNumber) {
        this.casNumber = casNumber;
    }

    public Integer getDeclarationPart() {
        return declarationPart;
    }

    public void setDeclarationPart(Integer declarationPart) {
        this.declarationPart = declarationPart;
    }
}