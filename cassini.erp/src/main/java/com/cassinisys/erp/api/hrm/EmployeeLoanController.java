package com.cassinisys.erp.api.hrm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.api.exceptions.ERPException;
import com.cassinisys.erp.api.exceptions.ErrorCodes;
import com.cassinisys.erp.api.filtering.EmployeeLoanPredicateBuilder;
import com.cassinisys.erp.converters.PageRequestConverter;
import com.cassinisys.erp.model.api.criteria.EmployeeLoanCriteria;
import com.cassinisys.erp.model.api.paging.ERPPageRequest;
import com.cassinisys.erp.model.hrm.ERPEmployee;
import com.cassinisys.erp.model.hrm.ERPEmployeeLoan;
import com.cassinisys.erp.model.hrm.QERPEmployeeLoan;
import com.cassinisys.erp.service.hrm.EmployeeLoanService;
import com.cassinisys.erp.service.hrm.EmployeeService;
import com.cassinisys.erp.service.security.SessionWrapper;
import com.mysema.query.types.Predicate;


@RestController
@RequestMapping("hrm/employeeloans")
@Api(name = "EmployeeLoans", description = "EmployeeLoans endpoint", group = "HRM")
public class EmployeeLoanController extends BaseController {

	@Autowired
	EmployeeLoanService employeeLoanService;
	@Autowired
	EmployeeService employeeService;
	@Autowired
	SessionWrapper sessionWrapper;
	
	@Autowired
	EmployeeLoanPredicateBuilder predicateBuilder;
	
	@Autowired
	PageRequestConverter pageRequestConverter;
	
	
	@RequestMapping(method = RequestMethod.POST)
	public ERPEmployeeLoan createEmployeeLoan(
			@RequestBody @Valid ERPEmployeeLoan empLoan,
			HttpServletRequest request, HttpServletResponse response) {

		return employeeLoanService.createEmployeeLoan(empLoan);

	}

	@RequestMapping(value = "/{loanId}", method = RequestMethod.GET)
	public ERPEmployeeLoan getEmployeeLoanById(
			@PathVariable("loanId") Integer loanId) {

		return employeeLoanService.getEmployeeLoanById(loanId);

	}

	@RequestMapping(value = "{loanId}", method = RequestMethod.PUT)
	public ERPEmployeeLoan updateEmployeeLoan(@PathVariable Integer loanId,
			@RequestBody ERPEmployeeLoan employeeLoan) {
		employeeLoan.setId(loanId);
		return employeeLoanService.updateEmployeeLoan(employeeLoan);
	}
	
	@RequestMapping(value = "/loan/{loanId}/", method = RequestMethod.PUT)
	public ERPEmployeeLoan approveEmployeeLoan(@PathVariable Integer loanId) {
				
		Integer id = sessionWrapper.getSession().getLogin().getPerson().getId();
        ERPEmployee employee = employeeService.getEmployeeById(id);
        if (employee != null) {
            return employeeLoanService.approveLoan(loanId);
        } else {
            throw new ERPException(HttpStatus.UNAUTHORIZED, ErrorCodes.GENERAL,
                    "You are not authorized to approve loans");
        }		
		
	}

	@RequestMapping(value = "/employee/{employee}", method = RequestMethod.GET)
	public List<ERPEmployeeLoan> getEmployeeLoans(@PathVariable Integer employee) {

		return employeeLoanService.getEmployeeLoans(employee);
	}

	@RequestMapping(value = "/{loanId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable Integer loanId) {

		employeeLoanService.deleteEmployeeLoan(loanId);
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<ERPEmployeeLoan> getAllLoans() {

		return employeeLoanService.getAllLoans();
	}

	
	@RequestMapping(value = "/pageable", method = RequestMethod.GET)
	public Page<ERPEmployeeLoan> getEmployeeTimeOff(EmployeeLoanCriteria criteria,
			ERPPageRequest pageRequest) {
		
		Predicate predicate = predicateBuilder.build(criteria,
				QERPEmployeeLoan.eRPEmployeeLoan);
		Pageable pageable = pageRequestConverter.convert(pageRequest);

		return employeeLoanService.findAll(predicate, pageable);
		
	

	}	
	
}
