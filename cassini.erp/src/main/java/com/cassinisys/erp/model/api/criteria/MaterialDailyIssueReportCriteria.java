package com.cassinisys.erp.model.api.criteria;

import com.cassinisys.erp.api.filtering.Criteria;

/**
 * Created by Nageshreddy on 22-08-2018.
 */
public class MaterialDailyIssueReportCriteria extends Criteria {

    private String timestamp;
    private String sku;
    private String name;
    private String issuedQty;
    private String consumeQty;
    private String remainingQty;
    private boolean freeTextSearch = false;
    private String searchQuery;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIssuedQty() {
        return issuedQty;
    }

    public void setIssuedQty(String issuedQty) {
        this.issuedQty = issuedQty;
    }

    public String getConsumeQty() {
        return consumeQty;
    }

    public void setConsumeQty(String consumeQty) {
        this.consumeQty = consumeQty;
    }

    public String getRemainingQty() {
        return remainingQty;
    }

    public void setRemainingQty(String remainingQty) {
        this.remainingQty = remainingQty;
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
