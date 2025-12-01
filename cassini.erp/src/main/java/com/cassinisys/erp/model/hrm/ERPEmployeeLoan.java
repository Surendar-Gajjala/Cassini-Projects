package com.cassinisys.erp.model.hrm;

import com.cassinisys.erp.converters.CustomDateSerializer;
import com.cassinisys.erp.model.core.ERPObject;
import com.cassinisys.erp.model.core.ObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ERP_EMPLOYEELOAN")
@PrimaryKeyJoinColumn(name = "LOAN_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "HRM")
public class ERPEmployeeLoan extends ERPObject implements
		Comparable<ERPEmployeeLoan> {

	@ApiObjectField(required = true)
	private Integer employee;
	@ApiObjectField(required = true)
	private ERPLoanType type;
	@ApiObjectField(required = true)
	private double amount = 0;
	@ApiObjectField(required = true)
	private Date requestedDate;
	@ApiObjectField(required = true)
	private Integer approvedBy;
	@ApiObjectField(required = true)
	private Date approvedDate;
	@ApiObjectField
	private Date paidOffDate;
	@ApiObjectField
	private String reason;
	@ApiObjectField
	private Integer term = 1;
	@ApiObjectField
	private LoanStatus status = LoanStatus.NEW;
	@ApiObjectField(required = true)
	private double balance = 0;

	public ERPEmployeeLoan() {
		super.setObjectType(ObjectType.LOAN);
	}

	@Column(name = "EMPLOYEE_ID", nullable = false)
	public Integer getEmployee() {
		return employee;
	}

	public void setEmployee(Integer employee) {
		this.employee = employee;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "LOAN_TYPE")
	public ERPLoanType getType() {
		return type;
	}

	public void setType(ERPLoanType type) {
		this.type = type;
	}

	@Column(name = "AMOUNT", nullable = false)
	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Column(name = "REQUESTED_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = CustomDateSerializer.class)
	public Date getRequestedDate() {
		return requestedDate;
	}

	public void setRequestedDate(Date requestedDate) {
		this.requestedDate = requestedDate;
	}

	@Column(name = "APPROVED_BY", nullable = false)
	public Integer getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Integer approvedBy) {
		this.approvedBy = approvedBy;
	}

	@Column(name = "APPROVED_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = CustomDateSerializer.class)
	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	@Column(name = "PAIDOFF_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = CustomDateSerializer.class)
	public Date getPaidOffDate() {
		return paidOffDate;
	}

	public void setPaidOffDate(Date paidOffDate) {
		this.paidOffDate = paidOffDate;
	}

	@Column(name = "REASON")
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Column(name = "TERM")
	public Integer getTerm() {
		return term;
	}

	public void setTerm(Integer term) {
		this.term = term;
	}

	@Column(name = "STATUS")
	@Type(type = "com.cassinisys.erp.converters.EnumUserType", parameters = { @org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.erp.model.hrm.LoanStatus") })
	public LoanStatus getStatus() {
		return status;
	}

	public void setStatus(LoanStatus status) {
		this.status = status;
	}

	@Column(name = "BALANCE")
	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	@Override
	public int compareTo(ERPEmployeeLoan loan) {

		return this.getType().getId().compareTo(loan.getType().getId());
	}
}
