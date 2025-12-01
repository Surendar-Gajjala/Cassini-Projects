package com.cassinisys.is.filtering;

import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by swapna on 31/12/18.
 */
public class CustomPurchaseOrderCriteria extends Criteria {

    private String poNumber;
    private String supplier;
    private String notes;
    private boolean freeTextSearch = false;
    private String searchQuery;

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
