package com.cassinisys.drdo.filtering;

import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by subramanyam reddy on 27-11-2018.
 */
public class InwardCriteria extends Criteria {

    private String status;
    private Boolean notification = Boolean.FALSE;
    private String searchQuery;
    private String fromDate;
    private String toDate;
    private Boolean adminPermission = false;
    private Boolean storeApprove = false;
    private Boolean ssqagApprove = false;
    private Boolean bdlApprove = false;
    private Boolean casApprove = false;
    private Boolean gatePassPermission = false;
    private Boolean finishedPage = false;
    private Boolean gatePassView = false;
    private Boolean finish = false;
    private String month;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public Boolean getNotification() {
        return notification;
    }

    public void setNotification(Boolean notification) {
        this.notification = notification;
    }

    public Boolean getAdminPermission() {
        return adminPermission;
    }

    public void setAdminPermission(Boolean adminPermission) {
        this.adminPermission = adminPermission;
    }

    public Boolean getStoreApprove() {
        return storeApprove;
    }

    public void setStoreApprove(Boolean storeApprove) {
        this.storeApprove = storeApprove;
    }

    public Boolean getSsqagApprove() {
        return ssqagApprove;
    }

    public void setSsqagApprove(Boolean ssqagApprove) {
        this.ssqagApprove = ssqagApprove;
    }

    public Boolean getBdlApprove() {
        return bdlApprove;
    }

    public void setBdlApprove(Boolean bdlApprove) {
        this.bdlApprove = bdlApprove;
    }

    public Boolean getCasApprove() {
        return casApprove;
    }

    public void setCasApprove(Boolean casApprove) {
        this.casApprove = casApprove;
    }

    public Boolean getGatePassPermission() {
        return gatePassPermission;
    }

    public void setGatePassPermission(Boolean gatePassPermission) {
        this.gatePassPermission = gatePassPermission;
    }

    public Boolean getFinishedPage() {
        return finishedPage;
    }

    public void setFinishedPage(Boolean finishedPage) {
        this.finishedPage = finishedPage;
    }

    public Boolean getGatePassView() {
        return gatePassView;
    }

    public void setGatePassView(Boolean gatePassView) {
        this.gatePassView = gatePassView;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public Boolean getFinish() {
        return finish;
    }

    public void setFinish(Boolean finish) {
        this.finish = finish;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
