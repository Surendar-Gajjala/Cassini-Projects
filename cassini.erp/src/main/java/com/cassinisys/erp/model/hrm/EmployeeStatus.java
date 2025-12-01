package com.cassinisys.erp.model.hrm;

public enum EmployeeStatus {

	ACTIVE("ACTIVE"), INACTIVE("INACTIVE"), LEFT("LEFT"), LAIDOFF("LAIDOFF");

	private String statusCode;

	public String getStatusCode() {
		return statusCode;
	}

	private EmployeeStatus(String s) {
		statusCode = s;
	}
}
