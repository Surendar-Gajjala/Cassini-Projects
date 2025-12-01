package com.cassinisys.erp.model.crm;

public enum CustomerOrderStatus {

	NEW("NEW"),
	APPROVED("APPROVED"),
	CANCELLED("CANCELLED"),
	PROCESSING("PROCESSING"),
	PROCESSED("PROCESSED"),
	PARTIALLYSHIPPED("PARTIALLYSHIPPED"),
	SHIPPED("SHIPPED"),
	DELIVERED("DELIVERED");

	private String status;

	public String getStatus() {
		return status;
	}

	CustomerOrderStatus(String s) {
		status = s;
	}
}
