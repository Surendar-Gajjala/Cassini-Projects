package com.cassinisys.erp.model.hrm;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "ERP_ALLOWANCEHISTORY")
@ApiObject(group = "HRM")
public class ERPAllowanceHistory implements Serializable,
		Comparable<ERPAllowanceHistory> {

	@EmbeddedId
	@ApiObjectField(required = true)
	private AllowanceHistoryId allowanceHistoryId;

	@ApiObjectField(required = true)
	@Column(name = "ALLOWANCE_PAID", nullable = false)
	private double allowancePaid;

	
	public AllowanceHistoryId getAllowanceHistoryId() {
		return allowanceHistoryId;
	}

	public void setAllowanceHistoryId(AllowanceHistoryId allowanceHistoryId) {
		this.allowanceHistoryId = allowanceHistoryId;
	}

	
	public double getAllowancePaid() {
		return allowancePaid;
	}

	public void setAllowancePaid(double allowancePaid) {
		this.allowancePaid = allowancePaid;
	}

	@Embeddable
	public static class AllowanceHistoryId implements Serializable {

		@ApiObjectField(required = true)
		@Column(name = "PAYROLL_ID", nullable = false)
		private Integer payrollId;

		@ApiObjectField(required = true)
		@Column(name = "EMPLOYEE_ID", nullable = false)
		private Integer employeeId;

		@ApiObjectField(required = true)
		@Column(name = "ALLOWANCE_TYPE", nullable = false)
		private Integer allowanceType;

		public AllowanceHistoryId() {

		}

		public AllowanceHistoryId(Integer payrollId, Integer employeeId,
				Integer allowanceType) {
			super();
			this.payrollId = payrollId;
			this.employeeId = employeeId;
			this.allowanceType = allowanceType;
		}

		
		public Integer getEmployeeId() {
			return employeeId;
		}

		public void setEmployeeId(Integer employeeId) {
			this.employeeId = employeeId;
		}

		
		public Integer getPayrollId() {
			return payrollId;
		}

		public void setPayrollId(Integer payrollId) {
			this.payrollId = payrollId;
		}

		
		public Integer getAllowanceType() {
			return allowanceType;
		}

		public void setAllowanceType(Integer allowanceType) {
			this.allowanceType = allowanceType;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((allowanceType == null) ? 0 : allowanceType.hashCode());
			result = prime * result
					+ ((employeeId == null) ? 0 : employeeId.hashCode());
			result = prime * result
					+ ((payrollId == null) ? 0 : payrollId.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			AllowanceHistoryId other = (AllowanceHistoryId) obj;
			if (allowanceType == null) {
				if (other.allowanceType != null)
					return false;
			} else if (!allowanceType.equals(other.allowanceType))
				return false;
			if (employeeId == null) {
				if (other.employeeId != null)
					return false;
			} else if (!employeeId.equals(other.employeeId))
				return false;
			if (payrollId == null) {
				if (other.payrollId != null)
					return false;
			} else if (!payrollId.equals(other.payrollId))
				return false;
			return true;
		}

	}

	@Transient
	public Integer getEmployeeId() {
		return this.getAllowanceHistoryId().getEmployeeId();
	}

	public void setEmployeeId(Integer employeeId) {
		this.getAllowanceHistoryId().setEmployeeId(employeeId);
	}

	@Transient
	public Integer getPayrollId() {
		return this.getAllowanceHistoryId().getPayrollId();
	}

	public void setPayrollId(Integer payrollId) {
		this.getAllowanceHistoryId().setPayrollId(payrollId);
	}

	@Transient
	public Integer getAllowanceType() {
		return this.getAllowanceHistoryId().getAllowanceType();
	}

	public void setAllowanceType(Integer allowanceType) {
		this.getAllowanceHistoryId().setAllowanceType(allowanceType);
	}
	
	@JsonBackReference
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PAYROLL_ID", referencedColumnName = "PAYROLL_ID", insertable = false, updatable = false)
	private ERPPayroll payroll;

	
	public ERPPayroll getPayroll() {
		return payroll;
	}

	public void setPayroll(ERPPayroll payroll) {
		this.payroll = payroll;

	}

	public ERPAllowanceHistory() {
		this.allowanceHistoryId = new AllowanceHistoryId();
	}

	@Override
	public int compareTo(ERPAllowanceHistory allowanceHistObj) {

		return this.allowanceHistoryId.allowanceType.compareTo(allowanceHistObj
				.getAllowanceHistoryId().allowanceType);
	}

}
