package com.cassinisys.drdo.filtering;

import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by subra on 27-11-2018.
 */
public class NotificationCriteria extends Criteria {

    private Boolean adminPermission = false;

    private Boolean storeApprove = false;

    private Boolean ssqagApprove = false;

    private Boolean bdlApprove = false;

    private Boolean versityApprove = false;

    private Boolean casApprove = false;

    private Boolean bdlQcApprove = false;

    private Boolean versityQc = false;

    private Boolean bdlPpcReceive = false;

    private Boolean versityPpc = false;

    private Boolean newRequest = false;

    private Boolean versity = false;

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

    public Boolean getNewRequest() {
        return newRequest;
    }

    public void setNewRequest(Boolean newRequest) {
        this.newRequest = newRequest;
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
}
