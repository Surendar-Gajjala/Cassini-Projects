package com.cassinisys.erp.model.crm;

/**
 * Created by reddy on 10/5/15.
 */
public enum ShipmentStatus {

    PENDING("PENDING"),
    SHIPPED("SHIPPED"),
    CANCELLED("CANCELLED");

    private String status;

    public String getStatus() {
        return status;
    }

    ShipmentStatus(String s) {
        status = s;
    }
}
