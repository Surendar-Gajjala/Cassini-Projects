package com.cassinisys.drdo.filtering;

import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by subramanyam reddy on 27-11-2018.
 */
public class RequestCriteria extends Criteria {

    private String status;
    private Boolean notification = Boolean.FALSE;
    private String searchQuery;
    private Boolean adminPermission = false;
    private Boolean storeApprove = false;
    private Boolean ssqagApprove = false;
    private Boolean bdlApprove = false;
    private Boolean casApprove = false;
    private Boolean versityApprove = false;
    private Boolean bdlQcApprove = false;
    private Boolean bdlPpcReceive = false;
    private Boolean issued = false;
    private String fromDate;
    private String toDate;
    private String month;
    private Boolean versity = false;

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

    public Boolean getIssued() {
        return issued;
    }

    public void setIssued(Boolean issued) {
        this.issued = issued;
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

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Boolean getVersity() {
        return versity;
    }

    public void setVersity(Boolean versity) {
        this.versity = versity;
    }

    public Boolean getVersityApprove() {
        return versityApprove;
    }

    public void setVersityApprove(Boolean versityApprove) {
        this.versityApprove = versityApprove;
    }

    public Boolean getBdlQcApprove() {
        return bdlQcApprove;
    }

    public void setBdlQcApprove(Boolean bdlQcApprove) {
        this.bdlQcApprove = bdlQcApprove;
    }

    public Boolean getBdlPpcReceive() {
        return bdlPpcReceive;
    }

    public void setBdlPpcReceive(Boolean bdlPpcReceive) {
        this.bdlPpcReceive = bdlPpcReceive;
    }
}
