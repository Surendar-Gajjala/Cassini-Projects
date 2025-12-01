package com.cassinisys.erp.model.hrm;

import java.util.List;

public class ERPEmployeeAttendanceDTO implements Comparable<ERPEmployeeAttendanceDTO>{
	
	private String empNumber;
	private String empName;
	private List<AttendanceDTO> attendance;

	public List<AttendanceDTO> getAttendance() {
		return attendance;
	}


	public void setAttendance(List<AttendanceDTO> attendance) {
		this.attendance = attendance;
	}


	public String getEmpNumber() {
		return empNumber;
	}


	public void setEmpNumber(String empNumber) {
		this.empNumber = empNumber;
	}


	public String getEmpName() {
		return empName;
	}


	public void setEmpName(String empName) {
		this.empName = empName;
	}


	@Override
	public int compareTo(ERPEmployeeAttendanceDTO o) {
		return this.getEmpNumber().compareTo(o.getEmpNumber());
	}
}
