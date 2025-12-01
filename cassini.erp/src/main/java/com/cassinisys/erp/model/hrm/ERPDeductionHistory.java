package com.cassinisys.erp.model.hrm;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ERP_DEDUCTIONHISTORY")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "HRM")
public class ERPDeductionHistory implements Serializable,
		Comparable<ERPDeductionHistory> {

	@EmbeddedId
	@ApiObjectField(required = true)
	private DeductionHistoryId deductionHistoryId;

	@ApiObjectField(required = true)
	@Column(name = "DEDUCTION_AMOUNT", nullable = false)
	private double deductionAmt;

	
	public DeductionHistoryId getDeductionHistoryId() {
		return deductionHistoryId;
	}

	public void setDeductionHistoryId(DeductionHistoryId deductionHistoryId) {
		this.deductionHistoryId = deductionHistoryId;
	}

	
	public double getDeductionAmt() {
		return deductionAmt;
	}

	public void setDeductionAmt(double deductionAmt) {
		this.deductionAmt = deductionAmt;
	}

	@Embeddable
	public static class DeductionHistoryId implements Serializable {

		@ApiObjectField(required = true)
		@Column(name = "PAYROLL_ID", nullable = false)
		private Integer payrollId;

		@ApiObjectField(required = true)
		@Column(name = "EMPLOYEE_ID", nullable = false)
		private Integer employeeId;

		@ApiObjectField(required = true)
		@Column(name = "DEDUCTION_TYPE", nullable = false)
		private Integer deductionType;

		public DeductionHistoryId() {
		}

		public DeductionHistoryId(Integer payrollId, Integer employeeId,
				Integer deductionType) {
			super();
			this.payrollId = payrollId;
			this.employeeId = employeeId;
			this.deductionType = deductionType;
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

		
		public Integer getDeductionType() {
			return deductionType;
		}

		public void setDeductionType(Integer deductionType) {
			this.deductionType = deductionType;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((deductionType == null) ? 0 : deductionType.hashCode());
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
			DeductionHistoryId other = (DeductionHistoryId) obj;
			if (deductionType == null) {
				if (other.deductionType != null)
					return false;
			} else if (!deductionType.equals(other.deductionType))
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
		return this.getDeductionHistoryId().getEmployeeId();
	}

	public void setEmployeeId(Integer employeeId) {
		this.getDeductionHistoryId().setEmployeeId(employeeId);
	}

	@Transient
	public Integer getPayrollId() {
		return this.getDeductionHistoryId().getPayrollId();
	}

	public void setPayrollId(Integer payrollId) {
		this.getDeductionHistoryId().setPayrollId(payrollId);
	}

	@Transient
	public Integer getDeductionType() {
		return this.getDeductionHistoryId().getDeductionType();
	}

	public void setDeductionType(Integer deductionType) {
		this.getDeductionHistoryId().setDeductionType(deductionType);
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

	public ERPDeductionHistory() {
		this.deductionHistoryId = new DeductionHistoryId();
	}

	@Override
	public int compareTo(ERPDeductionHistory deductionHistObj) {

		return this.deductionHistoryId.deductionType.compareTo(deductionHistObj
				.getDeductionHistoryId().deductionType);
	}

}
