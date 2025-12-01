package com.cassinisys.erp.service.hrm;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Date;
import java.util.List;

import com.cassinisys.erp.model.hrm.ERPLoanPaymentHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cassinisys.erp.repo.hrm.EmployeeLoanRepository;
import com.cassinisys.erp.repo.hrm.EmployeeRepository;
import com.cassinisys.erp.repo.hrm.LoanPaymentHistoryRepository;

@Service
@Transactional
public class LoanPaymentsService {

	@Autowired
	EmployeeLoanRepository employeeLoanRepository;

	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	LoanPaymentHistoryRepository loanPaymentHistoryRepository;
	

	/**
	 * 
	 * @param empLoan
	 * @return
	 */
	public ERPLoanPaymentHistory saveLoanPayment(ERPLoanPaymentHistory loanPayment) {
		checkNotNull(loanPayment);
		return loanPaymentHistoryRepository.save(loanPayment);

	}

	/**
	 * 
	 * @param loanPayment
	 * @return
	 */
	/*public ERPLoanPayments updateLoanPayments(ERPLoanPayments loanPayment) {

		checkNotNull(loanPayment);
		checkNotNull(loanPayment.getLoanId());
		if (loanPaymentsRepository.getLoanPaymentById(loanPayment.getLoanId()) == null) {
			throw new ERPException(HttpStatus.NOT_FOUND,
					ErrorCodes.RESOURCE_NOT_FOUND);
		}
		return loanPaymentsRepository.save(loanPayment);

	}*/

	/**
	 * 
	 * @param loanId
	 * @return
	 */
	public List<ERPLoanPaymentHistory> getLoanPaymentsById(Integer loanId) {
		checkNotNull(loanId);
		return loanPaymentHistoryRepository.findByLoanPaymentHistoryIdLoanId(loanId);

	}

	/**
	 * 
	 * @param loanId
	 */
	public void deleteLoanPayment(Integer loanId) {
		checkNotNull(loanId);
		//loanPaymentsRepository.delete(typeId);
	}

	
	/**
	 * 
	 * @param fromDate
	 * @param toDate
	 * @return
	 *//*
	public List<ERPLoanPaymentHistory> getLoanPaymentsByIdAndDateRange(Integer loanId, Date fromDate, Date toDate) {
				
		checkNotNull(fromDate);
		checkNotNull(toDate);
		return loanPaymentHistoryRepository.getLoanPaymentsByIdAndDateRange(loanId, fromDate, toDate);

	}
*/	
	/**
	 * 
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public List<ERPLoanPaymentHistory> getLoanPaymentsByDateRange(Date fromDate, Date toDate) {
				
		checkNotNull(fromDate);
		checkNotNull(toDate);
		return loanPaymentHistoryRepository.getLoanPaymentsByDateRange(fromDate, toDate);

	}
		

}
