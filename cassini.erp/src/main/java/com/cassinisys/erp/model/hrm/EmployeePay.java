package com.cassinisys.erp.model.hrm;

import java.util.List;

/**
 * 
 * @author lakshmi
 *
 */
public class EmployeePay {

	private ERPEmployee emp;

	private ERPEmployeeSalary empSal;

	private List<ERPEmployeeAllowance> empAllowances;

	private List<ERPEmployeeDeduction> empDeductions;
	
	private List<ERPEmployeeLoan> empLoans;


	public EmployeePay() {
		super();
	}

	public EmployeePay(ERPEmployee emp, ERPEmployeeSalary empSal,
			List<ERPEmployeeAllowance> empAllowances,
			List<ERPEmployeeDeduction> empDeductions,
			List<ERPEmployeeLoan> empLoans) {
		super();
		this.emp = emp;
		this.empSal = empSal;
		this.empAllowances = empAllowances;
		this.empDeductions = empDeductions;
		this.empLoans=empLoans;
	}

	public ERPEmployee getEmp() {
		return emp;
	}

	public void setEmp(ERPEmployee emp) {
		this.emp = emp;
	}

	public ERPEmployeeSalary getEmpSal() {
		return empSal;
	}

	public void setEmpSal(ERPEmployeeSalary empSal) {
		this.empSal = empSal;
	}

	public List<ERPEmployeeAllowance> getEmpAllowances() {
		return empAllowances;
	}

	public void setEmpAllowances(List<ERPEmployeeAllowance> empAllowances) {
		this.empAllowances = empAllowances;
	}

	public List<ERPEmployeeDeduction> getEmpDeductions() {
		return empDeductions;
	}

	public void setEmpDeductions(List<ERPEmployeeDeduction> empDeductions) {
		this.empDeductions = empDeductions;
	}

	public List<ERPEmployeeLoan> getEmpLoans() {
		return empLoans;
	}

	public void setEmpLoans(List<ERPEmployeeLoan> empLoans) {
		this.empLoans = empLoans;
	}

		
}
