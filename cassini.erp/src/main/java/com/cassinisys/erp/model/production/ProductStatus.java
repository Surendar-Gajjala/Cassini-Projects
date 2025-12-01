package com.cassinisys.erp.model.production;

public enum ProductStatus {

	ACTIVE("ACTIVE"), OUTOFSTOCK("OUTOFSTOCK"), DISCONTINUED("DISCONTINUED");

	private String status;

	public String getStatus() {
		return status;
	}

	ProductStatus(String s) {
		status = s;
	}

}
