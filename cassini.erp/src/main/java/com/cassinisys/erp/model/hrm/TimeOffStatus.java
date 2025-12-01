package com.cassinisys.erp.model.hrm;

public enum TimeOffStatus {

	 PENDING("PENDING"), APPROVED("APPROVED"), REJECTED("REJECTED");

	    private String status;

	    public String getStatus() {
	        return status;
	    }

	    private TimeOffStatus(String s) {
	        status = s;
	    }

}
