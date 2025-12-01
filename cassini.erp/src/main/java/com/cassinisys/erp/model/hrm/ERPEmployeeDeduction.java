package com.cassinisys.erp.model.hrm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "ERP_EMPLOYEEDEDUCTION")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "HRM")
public class ERPEmployeeDeduction implements Serializable ,Comparable<ERPEmployeeDeduction>{

	@EmbeddedId
	@ApiObjectField(required = true)
	private EmployeeDeductionId employeeDeductionId;

	@ApiObjectField(required = true)
	@Column(name = "DEDUCTION_VALUE", nullable = false)
	private double deductionValue;

	public EmployeeDeductionId getEmployeeDeductionId() {
		return employeeDeductionId;
	}

	public void setEmployeeDeductionId(EmployeeDeductionId employeeDeductionId) {
		this.employeeDeductionId = employeeDeductionId;
	}

	public double getDeductionValue() {
		return deductionValue;
	}

	public void setDeductionValue(double deductionValue) {
		this.deductionValue = deductionValue;
	}

	@Embeddable
	public static class EmployeeDeductionId implements Serializable {

		@ApiObjectField(required = true)
		@Column(name = "EMPLOYEE_ID", nullable = false)
		private Integer employee;

		@ApiObjectField(required = true)
		@Column(name = "DEDUCTION_TYPE", nullable = false)
		private Integer deductionType;

		public EmployeeDeductionId() {

		}

		public EmployeeDeductionId(Integer employee, Integer deductionType) {
			this.employee = employee;
			this.deductionType = deductionType;
		}

		public Integer getEmployee() {
			return employee;
		}

		public void setEmployee(Integer employee) {
			this.employee = employee;
		}

		public Integer getDeductionType() {
			return deductionType;
		}

		public void setDeductionType(Integer deductionType) {
			this.deductionType = deductionType;
		}
	}

	@Override
	public int compareTo(ERPEmployeeDeduction deductionObj) {
		return this.getEmployeeDeductionId().deductionType.compareTo(deductionObj.getEmployeeDeductionId().deductionType);
	}

}
