package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;

public class ProblemReportCriteria extends Criteria {

    Integer prType;
    String prNumber;
    String problem;
    String description;
    String searchQuery;
    String product;
    Integer qcr;
    Integer ecr;
    Boolean released = false;

    public Integer getPrType() {
        return prType;
    }

    public void setPrType(Integer prType) {
        this.prType = prType;
    }

    public String getPrNumber() {
        return prNumber;
    }

    public void setPrNumber(String prNumber) {
        this.prNumber = prNumber;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
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

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
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

    public Integer getEcr() {
        return ecr;
    }

    public void setEcr(Integer ecr) {
        this.ecr = ecr;
    }
}
