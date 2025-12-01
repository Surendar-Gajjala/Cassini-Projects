package com.cassinisys.erp.service.hrm;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cassinisys.erp.api.exceptions.ERPException;
import com.cassinisys.erp.api.exceptions.ErrorCodes;
import com.cassinisys.erp.api.exceptions.ResourceNotFoundException;
import com.cassinisys.erp.model.hrm.ERPEmployeeLoan;
import com.cassinisys.erp.model.hrm.ERPEmployeeTimeOff;
import com.cassinisys.erp.model.hrm.LoanStatus;
import com.cassinisys.erp.repo.hrm.EmployeeLoanRepository;
import com.cassinisys.erp.repo.hrm.EmployeeRepository;
import com.cassinisys.erp.service.security.SessionWrapper;
import com.mysema.query.types.Predicate;

@Service
@Transactional
public class EmployeeLoanService {

	@Autowired
	EmployeeLoanRepository employeeLoanRepository;

	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
    SessionWrapper sessionWrapper;
	
	 @Autowired
	 EmployeeService employeeService;

	
	public ERPEmployeeLoan createEmployeeLoan(ERPEmployeeLoan empLoan) {
		
		empLoan.setBalance(empLoan.getAmount());
		empLoan.setRequestedDate(new Date());
		return employeeLoanRepository.save(empLoan);

	}
	
	 
	
	 public ERPEmployeeLoan approveLoan(Integer loanId) {
        checkNotNull(loanId);

        ERPEmployeeLoan loan = employeeLoanRepository.findOne(loanId);
        if (loan != null) {
            Integer id = sessionWrapper.getSession().getLogin().getPerson().getId();
            loan.setStatus(LoanStatus.APPROVED);
            loan.setApprovedBy(id);
            loan.setModifiedDate(new Date());
            loan = employeeLoanRepository.save(loan);
        } else {
            throw new ResourceNotFoundException("Loan " + loanId + " does not exist");
        }

        return loan;

    }

	
	public ERPEmployeeLoan updateEmployeeLoan(ERPEmployeeLoan employeeLoan) {

		checkNotNull(employeeLoan);
		checkNotNull(employeeLoan.getId());
		if (employeeLoanRepository.findOne(employeeLoan.getId()) == null) {
			throw new ERPException(HttpStatus.NOT_FOUND,
					ErrorCodes.RESOURCE_NOT_FOUND);
		}
		return employeeLoanRepository.save(employeeLoan);

	}

	/**
	 * 
	 * @param loanId
	 * @return
	 */
	public ERPEmployeeLoan getEmployeeLoanById(Integer loanId) {

		return employeeLoanRepository.findOne(loanId);

	}
	
	 public Page<ERPEmployeeLoan> findAll(Predicate predicate, Pageable pageable) {
	        return employeeLoanRepository.findAll(predicate, pageable);
	    }

	/**
	 * 
	 * @param loanId
	 */
	public void deleteEmployeeLoan(Integer loanId) {
		checkNotNull(loanId);
		// employeeTypeRepository.delete(typeId);
	}

	/**
	 * 
	 * @param employee
	 * @return
	 */
	public List<ERPEmployeeLoan> getEmployeeLoans(Integer employee) {

		List<ERPEmployeeLoan> employeeLoans = null;
		checkNotNull(employee);

		employeeLoans = employeeLoanRepository.findByEmployee(employee);

		return employeeLoans;

	}

	/**
	 * 
	 * @param employee
	 * @return
	 */
	public List<ERPEmployeeLoan> getLoansApprovedBy(Integer approvedBy) {

		List<ERPEmployeeLoan> employeeLoans = null;

		checkNotNull(approvedBy);

		employeeLoans = employeeLoanRepository.getLoansApprovedBy(approvedBy);

		return employeeLoans;

	}

	public List<ERPEmployeeLoan> getAllLoans() {

		return employeeLoanRepository.findAll();
	}

}
