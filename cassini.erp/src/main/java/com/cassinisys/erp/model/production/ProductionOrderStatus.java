package com.cassinisys.erp.model.production;

public enum ProductionOrderStatus {

	CREATED("CREATED"),
	INPRODUCTION("INPRODUCTION"),
	COMPLETED("COMPLETED"),
	APPROVED("APPROVED"),
	REJECTED("REJECTED"),
	WAITINGFORMATERIAL("WAITINGFORMATERIAL"),
	CANCELLED("CANCELLED");

	private String status;

	public String getStatus() {
		return status;
	}

	ProductionOrderStatus(String s) {
		status = s;
	}

}
