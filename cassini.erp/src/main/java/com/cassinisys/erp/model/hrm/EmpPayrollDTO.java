package com.cassinisys.erp.model.hrm;

public class EmpPayrollDTO {
	
	private Integer month;
	
	private Integer year;
	
	private PayrollPay payrollPay;

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public PayrollPay getPayrollPay() {
		return payrollPay;
	}

	public void setPayrollPay(PayrollPay payrollPay) {
		this.payrollPay = payrollPay;
	}
	
	
}
