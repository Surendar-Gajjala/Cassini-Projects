package com.cassinisys.erp.model.hrm;

/**
 * Created by reddy on 10/10/15.
 */
public enum LoanStatus {
    NEW("NEW"), APPROVED("APPROVED"), UNPAID("UNPAID"), PAID("PAID");

    private String status;

    public String getStatus() {
        return status;
    }

    private LoanStatus(String s) {
        status = s;
    }
}
