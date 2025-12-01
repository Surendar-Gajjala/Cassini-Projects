package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.plm.model.pqm.QCRFor;

public class QCRCriteria extends Criteria {

    String qcrNumber;
    Integer qcrType;
    QCRFor qcrFor;
    String title;
    String description;
    String searchQuery;
    Boolean released = false;

    public String getQcrNumber() {
        return qcrNumber;
    }

    public void setQcrNumber(String qcrNumber) {
        this.qcrNumber = qcrNumber;
    }

    public Integer getQcrType() {
        return qcrType;
    }

    public void setQcrType(Integer qcrType) {
        this.qcrType = qcrType;
    }

    public QCRFor getQcrFor() {
        return qcrFor;
    }

    public void setQcrFor(QCRFor qcrFor) {
        this.qcrFor = qcrFor;
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

    public Boolean getReleased() {
        return released;
    }

    public void setReleased(Boolean released) {
        this.released = released;
    }
}
