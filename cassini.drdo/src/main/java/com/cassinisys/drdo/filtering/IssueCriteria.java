package com.cassinisys.drdo.filtering;

import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by Nageshreddy on 14-02-2019.
 */
public class IssueCriteria extends Criteria {

    private String fromDate;
    private String toDate;
    private String month;
    private String searchQuery;
    private Boolean adminPermission = false;
    private Boolean storeApprove = false;
    private Boolean bdlQcApprove = false;
    private Boolean notification = false;
    private Boolean bdlPpcReceive = false;
    private Boolean casApprove = false;
    private Boolean bdlApprove = false;
    private Integer missile;
    private Boolean versity = false;
    private Boolean versityApprove = false;
    private Boolean versityQc = false;
    private Boolean versityPpc = false;

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

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
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

    public Boolean getBdlQcApprove() {
        return bdlQcApprove;
    }

    public void setBdlQcApprove(Boolean bdlQcApprove) {
        this.bdlQcApprove = bdlQcApprove;
    }

    public Boolean getNotification() {
        return notification;
    }

    public void setNotification(Boolean notification) {
        this.notification = notification;
    }

    public Boolean getBdlPpcReceive() {
        return bdlPpcReceive;
    }

    public void setBdlPpcReceive(Boolean bdlPpcReceive) {
        this.bdlPpcReceive = bdlPpcReceive;
    }

    public Boolean getCasApprove() {
        return casApprove;
    }

    public void setCasApprove(Boolean casApprove) {
        this.casApprove = casApprove;
    }

    public Boolean getBdlApprove() {
        return bdlApprove;
    }

    public void setBdlApprove(Boolean bdlApprove) {
        this.bdlApprove = bdlApprove;
    }

    public Integer getMissile() {
        return missile;
    }

    public void setMissile(Integer missile) {
        this.missile = missile;
    }

    public Boolean getVersity() {
        return versity;
    }

    public void setVersity(Boolean versity) {
        this.versity = versity;
    }

    public Boolean getVersityQc() {
        return versityQc;
    }

    public void setVersityQc(Boolean versityQc) {
        this.versityQc = versityQc;
    }

    public Boolean getVersityPpc() {
        return versityPpc;
    }

    public void setVersityPpc(Boolean versityPpc) {
        this.versityPpc = versityPpc;
    }

    public Boolean getVersityApprove() {
        return versityApprove;
    }

    public void setVersityApprove(Boolean versityApprove) {
        this.versityApprove = versityApprove;
    }
}


