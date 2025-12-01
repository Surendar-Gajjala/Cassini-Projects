package com.cassinisys.erp.model.api.criteria;

import com.cassinisys.erp.api.filtering.Criteria;

/**
 * Created by reddy on 18/03/16.
 */
public class OrderVerificationCriteria extends Criteria {
    String orderNumber;
    String status;
    String poNumber;
    String customer;
    String assignedTo;


    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }
}
