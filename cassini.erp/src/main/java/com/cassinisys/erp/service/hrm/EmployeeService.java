package com.cassinisys.erp.service.hrm;

import com.cassinisys.erp.model.hrm.*;
import com.cassinisys.erp.repo.hrm.*;
import com.mysema.query.types.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by reddy on 7/18/15.
 */

@Service
@Transactional
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private EmployeeSalaryRepository empSalRepository;
	
	@Autowired
	private EmployeeAttendanceRepository attendenceRepository;


	@Autowired
	private EmployeeAllowanceRepository empAllowanceRepository;

	@Autowired
	private EmployeeDeductionRepository empDeductionRepository;
	
	@Autowired
	private EmployeeLoanRepository empLoanRepository;
	
	@Autowired
	private LoanPaymentHistoryRepository loanPymtHistRepository;
	
	@Autowired
	private AllowanceTypeRepository allowanceTypeRepository;

	@Autowired
	private DeductionTypeRepository deductionTypeRepository;

	@Autowired
	private SalaryHistoryRepository salHistRepository;
	
	@Autowired 
	private PayrollHistoryRepository payrollHistRepository;

	@Autowired
	private AllowanceHistoryRepository allowanceHistRepository;

	@Autowired
	private DeductionHistoryRepository deductionHistRepository;

	public Page<ERPEmployee> getAllEmployees(Pageable pageable) {
		return employeeRepository.findAll(pageable);
	}

	public List<ERPEmployee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	public ERPEmployee create(ERPEmployee employee) {
		return employeeRepository.save(employee);
	}

	public ERPEmployee update(ERPEmployee employee) {
		return employeeRepository.save(employee);
	}

	public void deleteEmployee(Integer employeeId) {
		employeeRepository.delete(employeeId);
	}

	public ERPEmployee getEmployeeById(int id) {

		ERPEmployee emp=employeeRepository.findOne(id);

		if(emp.getManager() != null) {

			ERPEmployee empManager = employeeRepository.findOne(emp.getManager());

			StringBuilder fullName = new StringBuilder();

			if (empManager != null) {
				fullName.append(empManager.getFirstName()).append(" ").append(empManager.getLastName());
				emp.setManagerName(fullName.toString());
			}
		}

		return emp;
	}

	public Page<ERPEmployee> getAllEmployees(Predicate predicate,
			Pageable pageable) {
		return employeeRepository.findAll(predicate, pageable);
	}

	/**
	 * 
	 * @return List<EmployeePay>
	 */
	public List<EmployeePay> getAllEmployeesPayDetails() {

		List<EmployeePay> empPayList = new ArrayList<EmployeePay>();

		// get the allowance types

		EmployeePay empPay = null;
		// get All emps

		List<ERPEmployee> emps = employeeRepository.findAll();

		if (emps != null) {

			for (ERPEmployee emp : emps) {

				empPay = new EmployeePay();

				if (emp != null) {

					empPay.setEmp(emp);

					// get the EmpSalary

					empPay.setEmpSal(empSalRepository.findOne(emp.getId()));

					List<ERPEmployeeAllowance> empAllowances = empAllowanceRepository
							.findByEmployeeAllowanceIdEmployee(emp.getId());

					addDefaultEmpAllowances(empAllowances, emp.getId());
					Collections.sort(empAllowances);
					empPay.setEmpAllowances(empAllowances);

					List<ERPEmployeeDeduction> empDeductions = empDeductionRepository
							.findByEmployeeDeductionIdEmployee(emp.getId());
					addDefaultEmpDeductions(empDeductions, emp.getId());
					Collections.sort(empDeductions);
					empPay.setEmpDeductions(empDeductions);
					
					List<ERPEmployeeLoan> empLoans= empLoanRepository.findByEmployeeAndStatus(emp.getId(), LoanStatus.APPROVED);
					Collections.sort(empLoans);
					empPay.setEmpLoans(empLoans);
					empPayList.add(empPay);

				}

			}

		}

		return empPayList;

	}

	/**
	 * 
	 * @return List<EmployeePay>
	 */
	public List<EmployeePay> getAllEmployeesPayDetails(Pageable pageable) {

		List<EmployeePay> empPayList = new ArrayList<EmployeePay>();

		// get the allowance types

		EmployeePay empPay = null;
		// get All emps

		Page<ERPEmployee> emps = employeeRepository.findAll(pageable);

		if (emps != null) {

			for (ERPEmployee emp : emps) {

				empPay = new EmployeePay();

				if (emp != null) {

					empPay.setEmp(emp);

					// get the EmpSalary

					empPay.setEmpSal(empSalRepository.findOne(emp.getId()));

					List<ERPEmployeeAllowance> empAllowances = empAllowanceRepository
							.findByEmployeeAllowanceIdEmployee(emp.getId());

					addDefaultEmpAllowances(empAllowances, emp.getId());
					Collections.sort(empAllowances);
					empPay.setEmpAllowances(empAllowances);

					List<ERPEmployeeDeduction> empDeductions = empDeductionRepository
							.findByEmployeeDeductionIdEmployee(emp.getId());
					addDefaultEmpDeductions(empDeductions, emp.getId());
					Collections.sort(empDeductions);
					empPay.setEmpDeductions(empDeductions);
					
					List<ERPEmployeeLoan> empLoans= empLoanRepository.findByEmployeeAndStatus(emp.getId(), LoanStatus.APPROVED);
					Collections.sort(empLoans);
					empPay.setEmpLoans(empLoans);

					empPayList.add(empPay);

				}

			}

		}

		return empPayList;

	}

	/**
	 * 
	 * @return List<EmployeePay>
	 */
	public List<PayrollPay> getAllEmployeesPayrollDetails(PayrollDTO payrollDTO) {

		List<PayrollPay> empPayrollList = new ArrayList<PayrollPay>();

		// get the allowance types
		Integer payrollId= payrollDTO.getId();

		PayrollPay payrollPay = null;
		// get All emps

		List<ERPEmployee> emps = employeeRepository.findAll();

		if (emps != null) {

			for (ERPEmployee emp : emps) {

				payrollPay = new PayrollPay();

				if (emp != null) {

					payrollPay.setEmp(emp);

					// get the EmpSalary

					ERPSalaryHistory ob =new ERPSalaryHistory();
				/*	ob.setSalaryHistoryId(new ERPSalaryHistory.SalaryHistoryId(
							payrollId, emp.getId()));
*/					ob=salHistRepository.findOne(new ERPSalaryHistory.SalaryHistoryId(
							payrollId, emp.getId()));
					payrollPay.setEmpSalHist(ob);

					List<ERPAllowanceHistory> empHistAllowances = allowanceHistRepository
							.findByAllowanceHistoryIdPayrollIdAndAllowanceHistoryIdEmployeeId(
									payrollId, emp.getId());

					Collections.sort(empHistAllowances);
					payrollPay.setEmpAllowancesHist(empHistAllowances);

					List<ERPDeductionHistory> empHistDeductions = deductionHistRepository
							.findByDeductionHistoryIdPayrollIdAndDeductionHistoryIdEmployeeId(
									payrollId, emp.getId());

					Collections.sort(empHistDeductions);
					payrollPay.setEmpDeductionsHist(empHistDeductions);
					
					List<ERPLoanPaymentHistory> loanPymtHist=loanPymtHistRepository.findByLoanPaymentHistoryIdPayrollIdAndLoanPaymentHistoryIdEmployeeId(payrollId, emp.getId());
										payrollPay.setEmpLoansPayHist(loanPymtHist);
					
					ERPPayrollHistory payrolHist=payrollHistRepository.findOne( new ERPPayrollHistory.PayrollHistoryId(payrollId, emp.getId()));
					
					payrollPay.setNetAmtPay(payrolHist.getSalNetPay());
					
					//add the attendence details
					payrollPay.setAttendence(getAttendenceDetails(emp.getId(),payrollDTO.getYear(),payrollDTO.getMonth()));

					empPayrollList.add(payrollPay);

				}

			}

		}

		return empPayrollList;

	}
	
	
	public Integer getAttendenceDetails(Integer empNum,Integer year,Integer month){
		
		int attendedDays=0;
		
		Date fromDate;
		Date toDate;
		
		Calendar calendar = Calendar.getInstance(); 
		calendar.set(year, month, 1);
		
		calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR, 00);
		calendar.set(Calendar.MINUTE, 00);
		calendar.set(Calendar.SECOND, 00);
		calendar.set(Calendar.AM_PM, Calendar.AM);


		fromDate = calendar.getTime();
		//add one month
		calendar.add(Calendar.MONTH, 1);
		
			
		calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		
		calendar.set(Calendar.HOUR, 00);
		calendar.set(Calendar.MINUTE, 00);
		calendar.set(Calendar.SECOND, 00);
		calendar.set(Calendar.AM_PM, Calendar.AM);

		toDate = calendar.getTime();
		
		
		attendedDays=attendenceRepository.getAttendenceCountByMonthWiseforEmp(fromDate,toDate,empNum+"");
		
		return attendedDays;	
			
		}


	public void addDefaultEmpAllowances(
			List<ERPEmployeeAllowance> empAllowances, Integer empId) {

		List<ERPAllowanceType> empAllownceTypes = null;
		empAllownceTypes = allowanceTypeRepository.findAll();

		boolean allowanceExists = false;

		for (ERPAllowanceType allowanceType : empAllownceTypes) {
			allowanceExists = false;
			for (ERPEmployeeAllowance empAllowance : empAllowances) {

				if (empAllowance.getEmployeeAllowanceId().getAllowanceType() == allowanceType
						.getId()) {

					allowanceExists = true;
					break;
				}
			}

			if (!allowanceExists) {

				ERPEmployeeAllowance empAllowanceNew = new ERPEmployeeAllowance();
				empAllowanceNew.setAllowanceValue(0);
				ERPEmployeeAllowance.EmployeeAllowanceId alowanceInnerObj = new ERPEmployeeAllowance.EmployeeAllowanceId(
						empId, allowanceType.getId());
				empAllowanceNew.setEmployeeAllowanceId(alowanceInnerObj);
				empAllowances.add(empAllowanceNew);

			}

		}

	}

	public void addDefaultEmpDeductions(
			List<ERPEmployeeDeduction> empDeductions, Integer empId) {

		List<ERPDeductionType> empDeductionTypes = null;

		empDeductionTypes = deductionTypeRepository.findAll();

		boolean deductionExists = false;

		for (ERPDeductionType deductionType : empDeductionTypes) {
			deductionExists = false;
			for (ERPEmployeeDeduction empDeduction : empDeductions) {

				if (empDeduction.getEmployeeDeductionId().getDeductionType() == deductionType
						.getId()) {

					deductionExists = true;
					break;
				}
			}

			if (!deductionExists) {

				ERPEmployeeDeduction empDeductionNew = new ERPEmployeeDeduction();
				empDeductionNew.setDeductionValue(0);
				ERPEmployeeDeduction.EmployeeDeductionId deductionInnerObj = new ERPEmployeeDeduction.EmployeeDeductionId(
						empId, deductionType.getId());
				empDeductionNew.setEmployeeDeductionId(deductionInnerObj);
				empDeductions.add(empDeductionNew);

			}

		}

	}

	public EmployeePay updateEmployeePayDetails(EmployeePay empPay) {

		if (empPay != null) {

			if (empPay.getEmpSal() != null) {
				ERPEmployeeSalary salary = empPay.getEmpSal();
				salary.setEmployeeId(empPay.getEmp().getId());
				empSalRepository.save(salary);

			}

			if (empPay.getEmpAllowances() != null
					&& empPay.getEmpAllowances().size() > 0) {

				empAllowanceRepository.save(empPay.getEmpAllowances());

			}

			if (empPay.getEmpDeductions() != null
					&& empPay.getEmpDeductions().size() > 0) {
				empDeductionRepository.save(empPay.getEmpDeductions());
			}

		}

		return empPay;
	}

}