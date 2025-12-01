package com.cassinisys.erp.model.hrm;

/**
 * Created by lakshmi on 2/12/2016.
 */
public enum AttendanceStatus {
    P("P"), A("A");

    private String status;

    public String getStatus() {
        return status;
    }

    private AttendanceStatus(String s) {
        status = s;
    }

}
