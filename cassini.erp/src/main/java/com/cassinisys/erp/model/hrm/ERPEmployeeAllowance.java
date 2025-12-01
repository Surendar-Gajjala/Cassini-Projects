package com.cassinisys.erp.model.hrm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "ERP_EMPLOYEEALLOWANCE")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "HRM")
public class ERPEmployeeAllowance implements Serializable,
		Comparable<ERPEmployeeAllowance> {

	@EmbeddedId
	@ApiObjectField(required = true)
	private EmployeeAllowanceId employeeAllowanceId;

	@ApiObjectField(required = true)
	@Column(name = "ALLOWANCE_VALUE", nullable = false)
	private double allowanceValue;

	public EmployeeAllowanceId getEmployeeAllowanceId() {
		return employeeAllowanceId;
	}

	public void setEmployeeAllowanceId(EmployeeAllowanceId employeeAllowanceId) {
		this.employeeAllowanceId = employeeAllowanceId;
	}

	public double getAllowanceValue() {
		return allowanceValue;
	}

	public void setAllowanceValue(double allowanceValue) {
		this.allowanceValue = allowanceValue;
	}

	@Embeddable
	public static class EmployeeAllowanceId implements Serializable {
		@ApiObjectField(required = true)
		@Column(name = "EMPLOYEE_ID", nullable = false)
		private Integer employee;
		@ApiObjectField(required = true)
		@Column(name = "ALLOWANCE_TYPE", nullable = false)
		private Integer allowanceType;

		public EmployeeAllowanceId() {
		}

		public EmployeeAllowanceId(Integer employee, Integer allowanceType) {
			this.employee = employee;
			this.allowanceType = allowanceType;
		}

		public Integer getEmployee() {
			return employee;
		}

		public void setEmployee(Integer employee) {
			this.employee = employee;
		}

		public Integer getAllowanceType() {
			return allowanceType;
		}

		public void setAllowanceType(Integer allowanceType) {
			this.allowanceType = allowanceType;
		}

	}

	@Override
	public int compareTo(ERPEmployeeAllowance allowanceObj) {

		return this.employeeAllowanceId.allowanceType.compareTo(allowanceObj
				.getEmployeeAllowanceId().allowanceType);

	}

}
