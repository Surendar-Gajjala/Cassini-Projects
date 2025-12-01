package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by CassiniSystems on 12-06-2020.
 */
public class DCRItemsCriteria extends Criteria {

    private String itemNumber;
    private String itemName;
    private Integer itemType;
    private Integer item;
    private Integer dcr;
    private Integer dco;
    private Integer ecr;
    private Integer mco;
    private Integer problemReport;
    private Integer ncr;
    private Integer qcr;
    private Integer inspection;
    private Integer variance;
    private Boolean related = false;


    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public Integer getDcr() {
        return dcr;
    }

    public void setDcr(Integer dcr) {
        this.dcr = dcr;
    }

    public Integer getDco() {
        return dco;
    }

    public void setDco(Integer dco) {
        this.dco = dco;
    }

    public Integer getEcr() {
        return ecr;
    }

    public void setEcr(Integer ecr) {
        this.ecr = ecr;
    }

    public Integer getMco() {
        return mco;
    }

    public void setMco(Integer mco) {
        this.mco = mco;
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

    public Integer getInspection() {
        return inspection;
    }

    public void setInspection(Integer inspection) {
        this.inspection = inspection;
    }

    public Boolean getRelated() {
        return related;
    }

    public void setRelated(Boolean related) {
        this.related = related;
    }

    public Integer getVariance() {
        return variance;
    }

    public void setVariance(Integer variance) {
        this.variance = variance;
    }
}
