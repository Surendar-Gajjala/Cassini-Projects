package com.cassinisys.erp.model.api.criteria;

import com.cassinisys.erp.api.filtering.Criteria;

/**
 * Created by reddy on 10/14/15.
 */
public class ShipmentCriteria extends Criteria {
    private String invoiceNumber;
    private String orderNumber;
    private String date;
    private String poNumber;
    private String status;
    private String customer;
    private String shipTo;
    private boolean freeTextSearch = false;
    private String searchQuery;

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getShipTo() {
        return shipTo;
    }

    public void setShipTo(String shipTo) {
        this.shipTo = shipTo;
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

    public boolean hasAnyFilters() {
        return (invoiceNumber != null ||
                orderNumber != null ||
                date != null ||
                poNumber != null ||
                status != null ||
                customer != null ||
                shipTo != null);
    }

}
