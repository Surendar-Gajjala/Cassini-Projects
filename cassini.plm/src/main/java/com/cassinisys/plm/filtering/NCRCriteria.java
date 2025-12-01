package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;

public class NCRCriteria extends Criteria {

    Integer ncrType;
    String ncrNumber;
    String title;
    String description;
    String searchQuery;
    Integer qcr;
    Boolean released = false;

    public Integer getNcrType() {
        return ncrType;
    }

    public void setNcrType(Integer ncrType) {
        this.ncrType = ncrType;
    }

    public String getNcrNumber() {
        return ncrNumber;
    }

    public void setNcrNumber(String ncrNumber) {
        this.ncrNumber = ncrNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Integer getQcr() {
        return qcr;
    }

    public void setQcr(Integer qcr) {
        this.qcr = qcr;
    }

    public Boolean getReleased() {
        return released;
    }

    public void setReleased(Boolean released) {
        this.released = released;
    }
}
