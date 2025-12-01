package com.cassinisys.erp.model.production;

public enum InventoryType {

	STOCKIN("STOCKIN"), STOCKOUT("STOCKOUT");

	private String type;

	public String getType() {
		return type;
	}

	InventoryType(String s) {
		type = s;
	}

}
