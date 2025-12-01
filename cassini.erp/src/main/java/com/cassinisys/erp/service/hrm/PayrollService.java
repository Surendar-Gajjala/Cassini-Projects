package com.cassinisys.erp.service.hrm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cassinisys.erp.model.common.AttendenceHelperDTO;
import com.cassinisys.erp.model.hrm.ERPAllowanceHistory;
import com.cassinisys.erp.model.hrm.ERPDeductionHistory;
import com.cassinisys.erp.model.hrm.ERPEmployeeAllowance;
import com.cassinisys.erp.model.hrm.ERPEmployeeDeduction;
import com.cassinisys.erp.model.hrm.ERPEmployeeLoan;
import com.cassinisys.erp.model.hrm.ERPLoanPaymentHistory;
import com.cassinisys.erp.model.hrm.ERPPayroll;
import com.cassinisys.erp.model.hrm.ERPPayrollHistory;
import com.cassinisys.erp.model.hrm.ERPSalaryHistory;
import com.cassinisys.erp.model.hrm.EmpPayrollDTO;
import com.cassinisys.erp.model.hrm.EmployeePay;
import com.cassinisys.erp.model.hrm.LoanStatus;
import com.cassinisys.erp.model.hrm.PayrollDTO;
import com.cassinisys.erp.model.hrm.PayrollPay;
import com.cassinisys.erp.model.hrm.TimeOffStatus;
import com.cassinisys.erp.repo.hrm.AllowanceHistoryRepository;
import com.cassinisys.erp.repo.hrm.DeductionHistoryRepository;
import com.cassinisys.erp.repo.hrm.EmployeeAttendanceRepository;
import com.cassinisys.erp.repo.hrm.EmployeeLoanRepository;
import com.cassinisys.erp.repo.hrm.EmployeeTimeOffRepository;
import com.cassinisys.erp.repo.hrm.LoanPaymentHistoryRepository;
import com.cassinisys.erp.repo.hrm.PayrollHistoryRepository;
import com.cassinisys.erp.repo.hrm.PayrollRepository;
import com.cassinisys.erp.repo.hrm.SalaryHistoryRepository;
import com.cassinisys.erp.service.common.AttendenceHelperService;

@Service
@Transactional
public class PayrollService {

	@Autowired
	EmployeeService empService;

	@Autowired
	private PayrollRepository payrollRepository;

	@Autowired
	private EmployeeAttendanceRepository attendenceRepository;

	@Autowired
	private SalaryHistoryRepository salHistoryRepository;

	@Autowired
	private AllowanceHistoryRepository allowanceHistRepository;

	@Autowired
	private LoanPaymentHistoryRepository loanPayHistRepository;

	@Autowired
	private DeductionHistoryRepository deductionHistRepository;

	@Autowired
	private HolidayService holidayService;
	
	@Autowired
	private PayrollHistoryRepository payrollHistRepository;

	@Autowired
	private EmployeeTimeOffRepository empTimeOffRepository;

	@Autowired
	AttendenceHelperService attendenceHelperService;

	@Autowired
	private EmployeeLoanRepository empLoanRepository;

	public List<ERPPayroll> getAllPayroll(Integer year) {
		// getAll will get for current year

		if (year == null) {
			Calendar cal = Calendar.getInstance();
			year = cal.get(Calendar.YEAR);
		}
		return payrollRepository.findByYear(year);
	}

	public PayrollDTO getAllPayrollByMonthAndYear(Integer year, Integer month) {

		PayrollDTO payrollDTO = new PayrollDTO();

		List<ERPPayroll> erpPayrolls = payrollRepository.findByYearAndMonth(
				year, month);

		for (ERPPayroll erpPayroll : erpPayrolls) {

			payrollDTO.setId(erpPayroll.getId());
			payrollDTO.setMonth(erpPayroll.getMonth());
			payrollDTO.setYear(erpPayroll.getYear());
			payrollDTO.setTotalAmount(erpPayroll.getTotalAmount());

			payrollDTO.setEmpPayrollPayLst(empService
					.getAllEmployeesPayrollDetails(payrollDTO));

			break;
		}

		return payrollDTO;

	}

	public ERPPayroll getAllPayrollById(Integer payrollId) {
		return payrollRepository.findOne(payrollId);
	}

	/**
	 * 
	 * @return PayrollDTO
	 */
	public PayrollDTO createPayrollGet(Integer month ,Pageable pagable,
			boolean paginationRequired) {

		PayrollDTO payrol = new PayrollDTO();
		payrol.setCurrentDate(new Date());

		List<PayrollPay> payrollPayLst = new ArrayList<PayrollPay>();

		PayrollPay payrollPay = null;

		List<ERPAllowanceHistory> empAllowanceHistorys = null;

		List<ERPDeductionHistory> empDeductionHistorys = null;

		List<ERPLoanPaymentHistory> empLoanPaymentHistorys = null;

		ERPSalaryHistory empSalHist = null;

		ERPAllowanceHistory empAllownceHist = null;

		ERPDeductionHistory empDeductionHist = null;

		ERPLoanPaymentHistory empLoanPymtHist = null;

		List<EmployeePay> empPays = null;

		if (paginationRequired) {
			empPays = empService.getAllEmployeesPayDetails(pagable);
		} else {
			// without pagination
			empPays = empService.getAllEmployeesPayDetails();
		}

		if (empPays != null && empPays.size() > 0) {

			for (EmployeePay empPay : empPays) {

				if (empPay != null) {

					payrollPay = new PayrollPay();

					payrollPay.setEmp(empPay.getEmp());

					// add emp sal hsitory
					if (empPay.getEmpSal() != null) {

						empSalHist = new ERPSalaryHistory();

						empSalHist.setBonus(empPay.getEmpSal().getBonus());
						empSalHist.setSalary(getEligibleSal(month,empPay));
						empSalHist.setEmployeeId(empPay.getEmpSal()
								.getEmployeeId());

						payrollPay.setEmpSalHist(empSalHist);
					}

					// set the Allowance historys

					if (empPay.getEmpAllowances() != null
							&& empPay.getEmpAllowances().size() > 0) {

						empAllowanceHistorys = new ArrayList<ERPAllowanceHistory>();

						for (ERPEmployeeAllowance empAllowance : empPay
								.getEmpAllowances()) {

							if (empAllowance != null) {

								empAllownceHist = new ERPAllowanceHistory();

								empAllownceHist.setAllowancePaid(empAllowance
										.getAllowanceValue());
								empAllownceHist.setAllowanceType(empAllowance
										.getEmployeeAllowanceId()
										.getAllowanceType());
								empAllownceHist
										.setEmployeeId(empAllowance
												.getEmployeeAllowanceId()
												.getEmployee());

								empAllowanceHistorys.add(empAllownceHist);
							}

						}

						payrollPay.setEmpAllowancesHist(empAllowanceHistorys);
					}

					if (empPay.getEmpDeductions() != null
							&& empPay.getEmpDeductions().size() > 0) {

						empDeductionHistorys = new ArrayList<ERPDeductionHistory>();

						for (ERPEmployeeDeduction empDeduction : empPay
								.getEmpDeductions()) {

							if (empDeduction != null) {

								empDeductionHist = new ERPDeductionHistory();

								empDeductionHist.setDeductionAmt(empDeduction
										.getDeductionValue());
								empDeductionHist.setDeductionType(empDeduction
										.getEmployeeDeductionId()
										.getDeductionType());
								empDeductionHist
										.setEmployeeId(empDeduction
												.getEmployeeDeductionId()
												.getEmployee());

								empDeductionHistorys.add(empDeductionHist);
							}

						}

						payrollPay.setEmpDeductionsHist(empDeductionHistorys);

					}

					if (empPay.getEmpLoans() != null
							&& empPay.getEmpLoans().size() > 0) {

						empLoanPaymentHistorys = new ArrayList<ERPLoanPaymentHistory>();

						for (ERPEmployeeLoan empLoan : empPay.getEmpLoans()) {

							if (empLoan != null) {

								if (empLoan.getBalance() > 0) {
									empLoanPymtHist = new ERPLoanPaymentHistory();
									double termAmtPay = empLoan.getAmount()
											/ empLoan.getTerm();
									empLoanPymtHist.setLoanId(empLoan.getId());
									empLoanPymtHist.setEmployeeId(empLoan
											.getEmployee());
									empLoanPymtHist.setPaymentDate(new Date());
									empLoanPymtHist.setAmtPaid(termAmtPay);

									empLoanPaymentHistorys.add(empLoanPymtHist);
								}

							}
						}

						payrollPay.setEmpLoansPayHist(empLoanPaymentHistorys);
						;

					}
					// add attendence
					if(empPay.getEmp()!=null && empPay.getEmp().getEmployeeNumber()!=null) {
						payrollPay.setAttendence(getAttendenceDetails(month, Integer.parseInt(empPay
								.getEmp().getEmployeeNumber())));
					}

					payrollPayLst.add(payrollPay);

				}

			}

		}

		payrol.setEmpPayrollPayLst(payrollPayLst);

		return payrol;
	}

	public double getEligibleSal(Integer month,EmployeePay empPay) {

		double sal = empPay.getEmpSal().getSalary();

		double eligibleSal = 0.0;

		AttendenceHelperDTO attendanceHelperDTO = attendenceHelperService
				.getMonthDetails();

		int numOfHolidays = holidayService.getNumOfHolidaysInCurrentMonth(month);

		// apply formula
		int numofSundays = attendanceHelperDTO.getNumOfSundays();
		int totNumOfDays = attendanceHelperDTO.getNumOfDaysInMonth();
		int attendedDays = getAttendenceDetails(month,Integer.parseInt(empPay.getEmp().getEmployeeNumber()));

		int workingDays = totNumOfDays - (numofSundays + 2 + numOfHolidays);
		double perDaySal = sal / workingDays;
		Date fromDate;
		Date toDate;

		Calendar calendar = Calendar.getInstance();
		month=month-1;
		calendar.set(Calendar.MONTH,month);
		calendar.set(Calendar.DATE,
				calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR, 00);
		calendar.set(Calendar.MINUTE, 00);
		calendar.set(Calendar.SECOND, 00);
		calendar.set(Calendar.AM_PM, Calendar.AM);

		fromDate = calendar.getTime();
		// add one month
		calendar.add(Calendar.MONTH, 1);

		calendar.set(Calendar.DATE,
				calendar.getActualMinimum(Calendar.DAY_OF_MONTH));

		calendar.set(Calendar.HOUR, 00);
		calendar.set(Calendar.MINUTE, 00);
		calendar.set(Calendar.SECOND, 00);
		calendar.set(Calendar.AM_PM, Calendar.AM);

		toDate = calendar.getTime();

		int timeOffDays = empTimeOffRepository
				.getTimeOffCountByMonthWiseforEmp(fromDate, toDate, empPay
						.getEmpSal().getEmployeeId(), TimeOffStatus.APPROVED);

		int unpaidDays = workingDays - (attendedDays + timeOffDays);
		eligibleSal = sal - (unpaidDays * perDaySal);

		return eligibleSal;
	}

	public Integer getAttendenceDetails(Integer month,Integer empNum) {

		int attendedDays = 0;

		Date fromDate;
		Date toDate;

		Calendar calendar = Calendar.getInstance();
		month=month-1;
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DATE,
				calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR, 00);
		calendar.set(Calendar.MINUTE, 00);
		calendar.set(Calendar.SECOND, 00);
		calendar.set(Calendar.AM_PM, Calendar.AM);

		fromDate = calendar.getTime();
		// add one month
		calendar.add(Calendar.MONTH, 1);

		calendar.set(Calendar.DATE,
				calendar.getActualMinimum(Calendar.DAY_OF_MONTH));

		calendar.set(Calendar.HOUR, 00);
		calendar.set(Calendar.MINUTE, 00);
		calendar.set(Calendar.SECOND, 00);
		calendar.set(Calendar.AM_PM, Calendar.AM);

		toDate = calendar.getTime();

		attendedDays = attendenceRepository
				.getAttendenceCountByMonthWiseforEmp(fromDate, toDate, empNum+"");

		return attendedDays;

	}

	public PayrollDTO create(PayrollDTO uiPayroll) {

		ERPPayroll payrol = new ERPPayroll();
		payrol.setMonth(uiPayroll.getMonth());
		payrol.setYear(uiPayroll.getYear());
		payrol.setTotalAmount(uiPayroll.getTotalAmount());

		payrol = payrollRepository.save(payrol);

		// to merge the modified objects with db objects get the db data

		PayrollDTO dbPayroll = createPayrollGet(uiPayroll.getMonth(),null, false);

		uiPayroll.setId(payrol.getId());
		
		ERPPayrollHistory payrollHist=null;
		
		double totalAmt=0.0;

		if (dbPayroll.getEmpPayrollPayLst() != null
				&& dbPayroll.getEmpPayrollPayLst().size() > 0) {

			for (PayrollPay payrolPay : dbPayroll.getEmpPayrollPayLst()) {

				if (payrolPay != null) {
					
					
					payrollHist= new ERPPayrollHistory();
					payrollHist.setPayrollId(payrol.getId());
					payrollHist.setEmployeeId(payrolPay.getEmp().getId());
					double sal=0.0;
					double bonus=0.0;
					double allowance=0.0;
					double deduction=0.0;
					double loanAmt=0.0;

					// merge and save salHistObj
					if (payrolPay.getEmpSalHist() != null) {

						ERPSalaryHistory salhistory = payrolPay.getEmpSalHist();

						for (PayrollPay uiPayrolPay : uiPayroll
								.getEmpPayrollPayLst()) {

							if (uiPayrolPay != null
									&& uiPayrolPay.getEmpSalHist() != null) {

								if (uiPayrolPay.getEmpSalHist().getEmployeeId() == salhistory
										.getEmployeeId()) {

									salhistory.setBonus(uiPayrolPay
											.getEmpSalHist().getBonus());
									salhistory.setSalary(uiPayrolPay
											.getEmpSalHist().getSalary());
									break;
								}

							}
						}

						ERPSalaryHistory salHistNew = new ERPSalaryHistory();

						salHistNew.setBonus(salhistory.getBonus());
						salHistNew.setEmployeeId(salhistory.getEmployeeId());
						salHistNew.setSalary(salhistory.getSalary());
						salHistNew.setPayrollId(payrol.getId());

						// --save the salary with total days,attendence,working
						// days,sundays...

						salHistoryRepository.save(salHistNew);
						
						sal=salHistNew.getSalary();
						bonus=salHistNew.getBonus();
						payrollHist.setEmployeeId(salhistory.getEmployeeId());

					}

					// merge and save allowanceHisoryObj

					if (payrolPay.getEmpAllowancesHist() != null
							&& payrolPay.getEmpAllowancesHist().size() > 0) {

						for (ERPAllowanceHistory allowanceHistory : payrolPay
								.getEmpAllowancesHist()) {

							if (allowanceHistory != null) {

								ERPAllowanceHistory allowHistNew = new ERPAllowanceHistory();

								for (PayrollPay uiPayrolPay : uiPayroll
										.getEmpPayrollPayLst()) {

									boolean exists = false;

									for (ERPAllowanceHistory uiAllowanceHistory : uiPayrolPay
											.getEmpAllowancesHist()) {

										if (uiAllowanceHistory.getEmployeeId() == allowanceHistory
												.getEmployeeId()) {

											allowanceHistory
													.setAllowancePaid(uiAllowanceHistory
															.getAllowancePaid());
											exists = true;
											break;

										}
									}

									if (exists)
										break;

								}

								allowHistNew.setAllowancePaid(allowanceHistory
										.getAllowancePaid());
								allowHistNew.setAllowanceType(allowanceHistory
										.getAllowanceType());
								allowHistNew.setEmployeeId(allowanceHistory
										.getEmployeeId());
								allowHistNew.setPayrollId(payrol.getId());

								allowanceHistRepository.save(allowHistNew);
								
								allowance =allowance+allowHistNew.getAllowancePaid();

							}

						}

					}

					// merge and save DeductionHisoryObj

					if (payrolPay.getEmpDeductionsHist() != null
							&& payrolPay.getEmpDeductionsHist().size() > 0) {

						for (ERPDeductionHistory deductionHistory : payrolPay
								.getEmpDeductionsHist()) {

							if (deductionHistory != null) {

								ERPDeductionHistory deductionHistNew = new ERPDeductionHistory();

								for (PayrollPay uiPayrolPay : uiPayroll
										.getEmpPayrollPayLst()) {

									boolean exists = false;

									for (ERPDeductionHistory uiDeductionHistory : uiPayrolPay
											.getEmpDeductionsHist()) {

										if (uiDeductionHistory.getEmployeeId() == deductionHistory
												.getEmployeeId()) {

											deductionHistory
													.setDeductionAmt(uiDeductionHistory
															.getDeductionAmt());
											exists = true;
											break;

										}
									}

									if (exists)
										break;

								}

								deductionHistNew
										.setDeductionAmt(deductionHistory
												.getDeductionAmt());
								deductionHistNew
										.setDeductionType(deductionHistory
												.getDeductionType());
								deductionHistNew.setEmployeeId(deductionHistory
										.getEmployeeId());
								deductionHistNew.setPayrollId(payrol.getId());

								deductionHistRepository.save(deductionHistNew);
								
								deduction=deduction+deductionHistNew.getDeductionAmt();

							}

						}

					}

					// save the emp loan history
					if (payrolPay.getEmpLoansPayHist() != null
							&& payrolPay.getEmpLoansPayHist().size() > 0) {

						for (ERPLoanPaymentHistory loanPayHist : payrolPay
								.getEmpLoansPayHist()) {

							if (loanPayHist != null) {

								loanPayHist.setPayrollId(payrol.getId());

								loanPayHistRepository.save(loanPayHist);
								
								loanAmt=loanAmt+loanPayHist.getAmtPaid();

								// also update the Emp Loan
								ERPEmployeeLoan empLoan = empLoanRepository
										.findOne(loanPayHist.getLoanId());

								empLoan.setBalance(empLoan.getBalance()
										- loanPayHist.getAmtPaid());

								if (empLoan.getBalance() == 0) {
									empLoan.setStatus(LoanStatus.PAID);
								}

								empLoanRepository.save(empLoan);
							}

						}

					}
					double netAmtPay=(sal+bonus+allowance)-(deduction+loanAmt);
					
					payrollHist.setSalNetPay(netAmtPay);
					payrollHistRepository.save(payrollHist);
					totalAmt=totalAmt+netAmtPay;
		
				}
			}

		}
		if(totalAmt>0){
			//update total amt 
			ERPPayroll payrolUpdate=payrollRepository.findOne(payrol.getId());
			payrolUpdate.setTotalAmount(totalAmt );
			payrollRepository.save(payrolUpdate);
		}

		return uiPayroll;

	}

	public List<EmpPayrollDTO> getEmployeePayDetailsForCurrentYear(
			Integer empId, Integer year) {

		List<EmpPayrollDTO> empPayrolLst = new ArrayList<EmpPayrollDTO>();

		// get the current month

		Calendar cal = Calendar.getInstance();

		int currYear = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		if (year > 0) {
			month = 12;
		} else {
			year = currYear;
		}

		for (int i = 1; i <= month; i++) {

			List<ERPPayroll> payrolls = payrollRepository.findByYearAndMonth(
					year, i);

			if (payrolls != null && payrolls.size() > 0) {

				EmpPayrollDTO empPayDTO = new EmpPayrollDTO();
				empPayDTO.setMonth(i);
				empPayDTO.setYear(year);

				ERPPayroll payroll = payrolls.get(0);

				PayrollPay payrollPay = new PayrollPay();

				ERPSalaryHistory salHist = salHistoryRepository
						.findOne(new ERPSalaryHistory.SalaryHistoryId(payroll
								.getId(), empId));

				List<ERPAllowanceHistory> allowHist = allowanceHistRepository
						.findByAllowanceHistoryIdPayrollIdAndAllowanceHistoryIdEmployeeId(
								payroll.getId(), empId);

				List<ERPDeductionHistory> dedHist = deductionHistRepository
						.findByDeductionHistoryIdPayrollIdAndDeductionHistoryIdEmployeeId(
								payroll.getId(), empId);

				List<ERPLoanPaymentHistory> loanPayHist = loanPayHistRepository
						.findByLoanPaymentHistoryIdPayrollIdAndLoanPaymentHistoryIdEmployeeId(
								payroll.getId(), empId);

				payrollPay.setEmpSalHist(salHist);
				payrollPay.setEmpAllowancesHist(allowHist);
				payrollPay.setEmpDeductionsHist(dedHist);
				payrollPay.setEmpLoansPayHist(loanPayHist);
				
				ERPPayrollHistory payrollHist=	payrollHistRepository.findOne(new ERPPayrollHistory.PayrollHistoryId(payroll.getId(), empId));
				if(payrollHist!=null)
				payrollPay.setNetAmtPay(payrollHist.getSalNetPay());
			
				empPayDTO.setPayrollPay(payrollPay);
				empPayrolLst.add(empPayDTO);
			}

		}

		return empPayrolLst;
	}

}
