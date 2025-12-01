package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam reddy on 19-09-2018.
 */
public class BomItemFilterCriteria extends Criteria {

    private String itemNumber;
    private String itemName;
    private Integer itemType;
    private String item;
    private Integer bomItem;
    private Integer replaceBomItem;
    private Integer eco;
    private Integer dcr;
    private Integer dco;
    private Integer ecr;
    private Integer variance;
    private Boolean configured;
    private Integer problemReport;
    private Integer ncr;
    private Integer qcr;
    private Boolean related = false;
    private List<Integer> affectedItemIds = new ArrayList<>();
    private Integer project;
    private Integer substitutePart;
    private Integer alternatePart;
    private Integer requirement;

    public Boolean getRelated() {
        return related;
    }

    public void setRelated(Boolean related) {
        this.related = related;
    }

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

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Integer getBomItem() {
        return bomItem;
    }

    public void setBomItem(Integer bomItem) {
        this.bomItem = bomItem;
    }

    public Integer getReplaceBomItem() {
        return replaceBomItem;
    }

    public void setReplaceBomItem(Integer replaceBomItem) {
        this.replaceBomItem = replaceBomItem;
    }

    public Integer getEco() {
        return eco;
    }

    public void setEco(Integer eco) {
        this.eco = eco;
    }

    public Boolean getConfigured() {
        return configured;
    }

    public void setConfigured(Boolean configured) {
        this.configured = configured;
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

    public Integer getVariance() {
        return variance;
    }

    public void setVariance(Integer variance) {
        this.variance = variance;
    }

    public Integer getEcr() {
        return ecr;
    }

    public void setEcr(Integer ecr) {
        this.ecr = ecr;
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

    public List<Integer> getAffectedItemIds() {
        return affectedItemIds;
    }

    public void setAffectedItemIds(List<Integer> affectedItemIds) {
        this.affectedItemIds = affectedItemIds;
    }

    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }

    public Integer getSubstitutePart() {
        return substitutePart;
    }

    public void setSubstitutePart(Integer substitutePart) {
        this.substitutePart = substitutePart;
    }

    public Integer getAlternatePart() {
        return alternatePart;
    }

    public void setAlternatePart(Integer alternatePart) {
        this.alternatePart = alternatePart;
    }

    public Integer getRequirement() {
        return requirement;
    }

    public void setRequirement(Integer requirement) {
        this.requirement = requirement;
    }
}
