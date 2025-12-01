package com.cassinisys.erp.model.crm;

/**
 * Created by reddy on 10/5/15.
 */
public enum ConsignmentStatus {

    PENDING("PENDING"),
    SHIPPED("SHIPPED"),
    FINISHED("FINISHED");

    private String status;

    public String getStatus() {
        return status;
    }

    ConsignmentStatus(String s) {
        status = s;
    }
}
