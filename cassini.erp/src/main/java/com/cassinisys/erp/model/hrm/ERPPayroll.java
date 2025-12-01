package com.cassinisys.erp.model.hrm;

import com.cassinisys.erp.converters.CustomDateDeserializer;
import com.cassinisys.erp.converters.CustomDateSerializer;
import com.cassinisys.erp.model.core.ERPObject;
import com.cassinisys.erp.model.core.ObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ERP_PAYROLL")
@JsonIgnoreProperties(ignoreUnknown = true)
@PrimaryKeyJoinColumn(name = "PAYROLL_ID")
@ApiObject(group = "HRM")
public class ERPPayroll extends ERPObject implements Serializable {

	@ApiObjectField(required = true)
	private Integer year;
	@ApiObjectField(required = true)
	private Integer month;
	@ApiObjectField(required = true)
	private double totalAmount;
	@ApiObjectField(required = false)
	private List<ERPSalaryHistory> salaries = new ArrayList<ERPSalaryHistory>();
	@ApiObjectField(required = false)
	private List<ERPAllowanceHistory> allowances = new ArrayList<ERPAllowanceHistory>();
	@ApiObjectField(required = false)
	private List<ERPDeductionHistory> deductions = new ArrayList<ERPDeductionHistory>();
	
	@ApiObjectField(required = false)
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

	public ERPPayroll() {
		super.setObjectType(ObjectType.PAYROLL);
	}

	@Column(name = "YEAR")
	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	@Column(name = "MONTH")
	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	@Column(name = "TOTAL_AMOUNT")
	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	@JsonManagedReference
	@OneToMany(mappedBy = "payroll", fetch = FetchType.EAGER, targetEntity = ERPAllowanceHistory.class, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<ERPAllowanceHistory> getAllowances() {
		return allowances;
	}

	public void setAllowances(List<ERPAllowanceHistory> allowances) {
		this.allowances = allowances;
	}

	@JsonManagedReference
	@OneToMany(mappedBy = "payroll", fetch = FetchType.EAGER, targetEntity = ERPSalaryHistory.class, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<ERPSalaryHistory> getSalaries() {
		return salaries;
	}

	public void setSalaries(List<ERPSalaryHistory> salaries) {
		this.salaries = salaries;
	}

	@JsonManagedReference
	@OneToMany(mappedBy = "payroll", fetch = FetchType.EAGER, targetEntity = ERPDeductionHistory.class, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<ERPDeductionHistory> getDeductions() {
		return deductions;
	}

	public void setDeductions(List<ERPDeductionHistory> deductions) {
		this.deductions = deductions;
	}
}
