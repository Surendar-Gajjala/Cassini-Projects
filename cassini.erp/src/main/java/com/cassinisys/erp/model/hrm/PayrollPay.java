package com.cassinisys.erp.model.hrm;

import java.util.List;

public class PayrollPay {

	private ERPEmployee emp;

	private ERPSalaryHistory empSalHist;

	private List<ERPAllowanceHistory> empAllowancesHist;

	private List<ERPDeductionHistory> empDeductionsHist;
	
	private List<ERPLoanPaymentHistory> empLoansPayHist;
	
	private double netAmtPay=0.0;
	
	private Integer attendence;
	
	
	public PayrollPay() {
	}

	public PayrollPay(ERPEmployee emp, ERPSalaryHistory empSalHist,
			List<ERPAllowanceHistory> empAllowancesHist,
			List<ERPDeductionHistory> empDeductionsHist,
			List<ERPLoanPaymentHistory> empLoansPayHist,Integer attendence) {
		this.emp = emp;
		this.empSalHist = empSalHist;
		this.empAllowancesHist = empAllowancesHist;
		this.empDeductionsHist = empDeductionsHist;
		this.empLoansPayHist=empLoansPayHist;
		this.attendence=attendence;
	}

	public ERPEmployee getEmp() {
		return emp;
	}

	public void setEmp(ERPEmployee emp) {
		this.emp = emp;
	}

	public ERPSalaryHistory getEmpSalHist() {
		return empSalHist;
	}

	public void setEmpSalHist(ERPSalaryHistory empSalHist) {
		this.empSalHist = empSalHist;
	}

	public List<ERPAllowanceHistory> getEmpAllowancesHist() {
		return empAllowancesHist;
	}

	public void setEmpAllowancesHist(List<ERPAllowanceHistory> empAllowancesHist) {
		this.empAllowancesHist = empAllowancesHist;
	}

	public List<ERPDeductionHistory> getEmpDeductionsHist() {
		return empDeductionsHist;
	}

	public void setEmpDeductionsHist(List<ERPDeductionHistory> empDeductionsHist) {
		this.empDeductionsHist = empDeductionsHist;
	}
		
	public List<ERPLoanPaymentHistory> getEmpLoansPayHist() {
		return empLoansPayHist;
	}

	public void setEmpLoansPayHist(List<ERPLoanPaymentHistory> empLoansPayHist) {
		this.empLoansPayHist = empLoansPayHist;
	}

	public double getNetAmtPay() {
		return netAmtPay;
	}

	public void setNetAmtPay(double netAmtPay) {
		this.netAmtPay = netAmtPay;
	}

	public Integer getAttendence() {
		return attendence;
	}

	public void setAttendence(Integer attendence) {
		this.attendence = attendence;
	}
	
	

}
