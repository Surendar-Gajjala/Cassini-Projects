package com.cassinisys.erp.model.common;

public class AttendenceHelperDTO {
	
	private Integer numOfSundays;
	
	private Integer numOfDaysInMonth;

	public AttendenceHelperDTO(){
		
	}
	
	public AttendenceHelperDTO(Integer numOfSundays, Integer numOfDaysInMonth) {
		super();
		this.numOfSundays = numOfSundays;
		this.numOfDaysInMonth = numOfDaysInMonth;
	}

	public Integer getNumOfSundays() {
		return numOfSundays;
	}

	public void setNumOfSundays(Integer numOfSundays) {
		this.numOfSundays = numOfSundays;
	}

	public Integer getNumOfDaysInMonth() {
		return numOfDaysInMonth;
	}

	public void setNumOfDaysInMonth(Integer numOfDaysInMonth) {
		this.numOfDaysInMonth = numOfDaysInMonth;
	}
	
	

}
