package com.cassinisys.erp.model.crm;

/**
 * Created by reddy on 11/3/15.
 */
public enum OrderVerificationStatus {
    PENDING("PENDING"),
    VERIFIED("VERIFIED");

    private String status;

    public String getStatus() {
        return status;
    }

    OrderVerificationStatus(String s) {
        status = s;
    }
}
