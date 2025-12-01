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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "ERP_PAYROLL_HISTORY")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "HRM")
public class ERPPayrollHistory implements Serializable {

	@ApiObjectField(required = true)
	private PayrollHistoryId payrollHistoryId;

	@ApiObjectField(required = true)
	private Double salNetPay;

	@EmbeddedId
	public PayrollHistoryId getPayrollHistoryId() {
		return payrollHistoryId;
	}

	public void setPayrollHistoryId(PayrollHistoryId payrollHistoryId) {
		this.payrollHistoryId = payrollHistoryId;
	}

	@Column(name = "SAL_NET_PAY")
	public Double getSalNetPay() {
		return salNetPay;
	}

	public void setSalNetPay(Double salNetPay) {
		this.salNetPay = salNetPay;
	}

	@Embeddable
	public static class PayrollHistoryId implements Serializable {

		@ApiObjectField(required = true)
		private Integer payrollId;

		@ApiObjectField(required = true)
		private Integer employeeId;

		public PayrollHistoryId() {

		}

		public PayrollHistoryId(Integer payrollId, Integer employeeId) {
			super();
			this.payrollId = payrollId;
			this.employeeId = employeeId;
		}

		@Column(name = "PAYROLL_ID", nullable = false)
		public Integer getPayrollId() {
			return payrollId;
		}

		public void setPayrollId(Integer payrollId) {
			this.payrollId = payrollId;
		}

		@Column(name = "EMPLOYEE_ID", nullable = false)
		public Integer getEmployeeId() {
			return employeeId;
		}

		public void setEmployeeId(Integer employeeId) {
			this.employeeId = employeeId;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
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
			PayrollHistoryId other = (PayrollHistoryId) obj;
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
	public Integer getPayrollId() {
		return this.payrollHistoryId.getPayrollId();
	}

	public void setPayrollId(Integer payrollId) {
		this.payrollHistoryId.setPayrollId(payrollId);
	}

	@Transient
	public Integer getEmployeeId() {
		return this.payrollHistoryId.getEmployeeId();
	}

	public void setEmployeeId(Integer employeeId) {
		this.payrollHistoryId.setEmployeeId(employeeId);
	}

	private ERPPayroll payroll;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PAYROLL_ID", referencedColumnName = "PAYROLL_ID", insertable = false, updatable = false)
	public ERPPayroll getPayroll() {
		return payroll;
	}

	public void setPayroll(ERPPayroll payroll) {
		this.payroll = payroll;

	}

	public ERPPayrollHistory() {
		this.payrollHistoryId = new PayrollHistoryId();

	}

}
