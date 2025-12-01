package com.cassinisys.erp.model.production;

public enum JobStatus {

	CREATED("CREATED"), SCHEDULED("SCHEDULED"),STARTED("STARTED"), FINISHED("FINISHED");
	
	private String jobStatus;

	public String getJobStatus() {
		return jobStatus;
	}

	private JobStatus(String s) {
		jobStatus = s;
	}
}
