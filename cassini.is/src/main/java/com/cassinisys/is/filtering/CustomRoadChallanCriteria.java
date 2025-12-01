package com.cassinisys.is.filtering;

import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by swapna on 31/12/18.
 */
public class CustomRoadChallanCriteria extends Criteria {

    private String chalanNumber;
    private String store;
    private String goingFrom;
    private String goingTo;
    private String meansOfTrans;
    private String vehicleDetails;
    private String issuingAuthority;
    private boolean freeTextSearch = false;
    private String searchQuery;

    public String getChalanNumber() {
        return chalanNumber;
    }

    public void setChalanNumber(String chalanNumber) {
        this.chalanNumber = chalanNumber;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getGoingFrom() {
        return goingFrom;
    }

    public void setGoingFrom(String goingFrom) {
        this.goingFrom = goingFrom;
    }

    public String getGoingTo() {
        return goingTo;
    }

    public void setGoingTo(String goingTo) {
        this.goingTo = goingTo;
    }

    public String getMeansOfTrans() {
        return meansOfTrans;
    }

    public void setMeansOfTrans(String meansOfTrans) {
        this.meansOfTrans = meansOfTrans;
    }

    public String getVehicleDetails() {
        return vehicleDetails;
    }

    public void setVehicleDetails(String vehicleDetails) {
        this.vehicleDetails = vehicleDetails;
    }

    public String getIssuingAuthority() {
        return issuingAuthority;
    }

    public void setIssuingAuthority(String issuingAuthority) {
        this.issuingAuthority = issuingAuthority;
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
