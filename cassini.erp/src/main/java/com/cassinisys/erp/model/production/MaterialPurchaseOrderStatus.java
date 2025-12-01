package com.cassinisys.erp.model.production;

public enum MaterialPurchaseOrderStatus {

	CREATED("CREATED"), DELIVERED("DELIVERED"),APPROVED("APPROVED"), CANCELLED("CANCELLED");

	private String status;

	public String getStatus() {
		return status;
	}

	MaterialPurchaseOrderStatus(String s) {
		status = s;
	}

}
