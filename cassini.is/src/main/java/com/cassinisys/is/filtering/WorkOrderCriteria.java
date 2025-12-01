package com.cassinisys.is.filtering;

import com.cassinisys.is.model.procm.WorkOrderStatus;
import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by swapna on 29/01/19.
 */
public class WorkOrderCriteria extends Criteria {

    private String number;
    private Integer contractor;
    private WorkOrderStatus status;
    private Integer project;
    private boolean freeTextSearch = false;
    private String searchQuery;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getContractor() {
        return contractor;
    }

    public void setContractor(Integer contractor) {
        this.contractor = contractor;
    }

    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }

    public WorkOrderStatus getStatus() {
        return status;
    }

    public void setStatus(WorkOrderStatus status) {
        this.status = status;
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
}
