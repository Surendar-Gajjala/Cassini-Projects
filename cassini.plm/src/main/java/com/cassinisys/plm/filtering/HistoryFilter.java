package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by subramanyam on 02-05-2020.
 */
public class HistoryFilter extends Criteria {

    private String type;

    private String date;

    private Integer item;

    private Integer plan;

    private Integer inspection;

    private Integer problemReport;

    private Integer ncr;

    private Integer qcr;

    private String user;

    private Integer revision;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    public Integer getPlan() {
        return plan;
    }

    public void setPlan(Integer plan) {
        this.plan = plan;
    }

    public Integer getInspection() {
        return inspection;
    }

    public void setInspection(Integer inspection) {
        this.inspection = inspection;
    }

    public Integer getProblemReport() {
        return problemReport;
    }

    public void setProblemReport(Integer problemReport) {
        this.problemReport = problemReport;
    }

    public Integer getNcr() {
        return ncr;
    }

    public void setNcr(Integer ncr) {
        this.ncr = ncr;
    }

    public Integer getQcr() {
        return qcr;
    }

    public void setQcr(Integer qcr) {
        this.qcr = qcr;
    }
}
