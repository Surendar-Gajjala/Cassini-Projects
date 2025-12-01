package com.cassinisys.erp.model.hrm;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import com.cassinisys.erp.converters.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "ERP_LOANPAYMENTHISTORY")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "HRM")
public class ERPLoanPaymentHistory implements Serializable {

	@EmbeddedId
	@ApiObjectField(required = true)
	private LoanPaymentHistoryId loanPaymentHistoryId;

	@ApiObjectField(required = true)
	@Column(name = "AMOUNT_PAID", nullable = false)
	private double amtPaid;
	
	@ApiObjectField(required = true)
	@Column(name = "PAYMENT_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = CustomDateSerializer.class)
	private Date paymentDate;

	public LoanPaymentHistoryId getLoanPaymentHistoryId() {
		return loanPaymentHistoryId;
	}

	public void setLoanPaymentHistoryId(
			LoanPaymentHistoryId loanPaymentHistoryId) {
		this.loanPaymentHistoryId = loanPaymentHistoryId;
	}

	public double getAmtPaid() {
		return amtPaid;
	}

	public void setAmtPaid(double amtPaid) {
		this.amtPaid = amtPaid;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	@Embeddable
	public static class LoanPaymentHistoryId implements Serializable {

		@ApiObjectField(required = true)
		@Column(name = "PAYROLL_ID", nullable = false)
		private Integer payrollId;

		@ApiObjectField(required = true)
		@Column(name = "EMPLOYEE_ID", nullable = false)
		private Integer employeeId;

		@ApiObjectField(required = true)
		@Column(name = "LOAN_ID", nullable = false)
		private Integer loanId;

		public LoanPaymentHistoryId() {
		}

		public LoanPaymentHistoryId(Integer payrollId, Integer employeeId,
				Integer loanId) {
			super();
			this.payrollId = payrollId;
			this.employeeId = employeeId;
			this.loanId = loanId;
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

		public Integer getLoanId() {
			return loanId;
		}

		public void setLoanId(Integer loanId) {
			this.loanId = loanId;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((employeeId == null) ? 0 : employeeId.hashCode());
			result = prime * result
					+ ((loanId == null) ? 0 : loanId.hashCode());
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
			LoanPaymentHistoryId other = (LoanPaymentHistoryId) obj;
			if (employeeId == null) {
				if (other.employeeId != null)
					return false;
			} else if (!employeeId.equals(other.employeeId))
				return false;
			if (loanId == null) {
				if (other.loanId != null)
					return false;
			} else if (!loanId.equals(other.loanId))
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
		return this.getLoanPaymentHistoryId().getEmployeeId();
	}

	public void setEmployeeId(Integer employeeId) {
		this.getLoanPaymentHistoryId().setEmployeeId(employeeId);
	}

	@Transient
	public Integer getPayrollId() {
		return this.getLoanPaymentHistoryId().getPayrollId();
	}

	public void setPayrollId(Integer payrollId) {
		this.getLoanPaymentHistoryId().setPayrollId(payrollId);
	}

	@Transient
	public Integer getLoanId() {
		return this.getLoanPaymentHistoryId().getLoanId();
	}

	public void setLoanId(Integer loanId) {
		this.getLoanPaymentHistoryId().setLoanId(loanId);
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

	public ERPLoanPaymentHistory() {
		this.loanPaymentHistoryId = new LoanPaymentHistoryId();
	}

}
