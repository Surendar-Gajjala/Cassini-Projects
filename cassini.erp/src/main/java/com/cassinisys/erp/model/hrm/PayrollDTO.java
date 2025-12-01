package com.cassinisys.erp.model.hrm;

import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

import com.cassinisys.erp.converters.CustomDateDeserializer;
import com.cassinisys.erp.converters.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class PayrollDTO {
	
	private Integer id;

	private Integer year;

	private Integer month;

	private double totalAmount;

	private List<PayrollPay> empPayrollPayLst;
	
	private Date currentDate=new Date();
	
	@Transient
	@JsonSerialize(using = CustomDateSerializer.class)
	@JsonDeserialize(using = CustomDateDeserializer.class)
	public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public List<PayrollPay> getEmpPayrollPayLst() {
		return empPayrollPayLst;
	}

	public void setEmpPayrollPayLst(List<PayrollPay> empPayrollPayLst) {
		this.empPayrollPayLst = empPayrollPayLst;
	}

	
}
