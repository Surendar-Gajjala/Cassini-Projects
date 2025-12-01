package com.cassinisys.erp.model.production;

public enum InventoryRecordType {

	STOCKIN("STOCKIN"), STOCKOUT("STOCKOUT");

	private String recordType;

	public String getRecordtype() {
		return recordType;
	}

	private InventoryRecordType(String s) {
		recordType = s;
	}
}
