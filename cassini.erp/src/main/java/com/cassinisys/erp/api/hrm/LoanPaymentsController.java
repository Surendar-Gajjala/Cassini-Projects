package com.cassinisys.erp.api.hrm;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.cassinisys.erp.model.hrm.ERPLoanPaymentHistory;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.service.hrm.LoanPaymentsService;

@RestController
@RequestMapping("hrm/employeeloanpayments")
@Api(name="LoanPayments",description="LoanPayments endpoint",group="HRM")
public class LoanPaymentsController extends BaseController {

	@Autowired
	LoanPaymentsService loanPaymentsService;

	
	@RequestMapping(method = RequestMethod.POST)
	public ERPLoanPaymentHistory createLoanPayments(@RequestBody @Valid ERPLoanPaymentHistory loanPayment,
			HttpServletRequest request, HttpServletResponse response) {

		return loanPaymentsService.saveLoanPayment(loanPayment);

	}

	
	@RequestMapping(value = "/{loanId}", method = RequestMethod.GET)
	public List<ERPLoanPaymentHistory> getLoanPaymentsById(@PathVariable("loanId") Integer loanId) {

		return loanPaymentsService.getLoanPaymentsById(loanId);

	}

	
	/*@RequestMapping(value = "{loanId}", method = RequestMethod.PUT)
	public ERPLoanPayments updateLoanPaymentsByDateRange(@PathVariable Integer loanId,
			@RequestBody ERPLoanPayments loanPayments) {
		loanPayments.setLoanId(loanId);
		return loanPaymentsService.updateLoanPayments(loanPayments);
	}*/

	
	@RequestMapping(value = "/{loanId}/{fromDate}/{toDate}", method = RequestMethod.GET)
	public List<ERPLoanPaymentHistory> getLoanPaymentsByDateRange(@PathVariable Integer loanId, @PathVariable Date  fromDate, @PathVariable Date toDate ) {

		return loanPaymentsService.getLoanPaymentsByDateRange(fromDate, toDate);

	}

	

}
