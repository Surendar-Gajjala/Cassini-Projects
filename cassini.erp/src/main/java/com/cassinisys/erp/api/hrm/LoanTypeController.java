package com.cassinisys.erp.api.hrm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.model.hrm.ERPLoanType;
import com.cassinisys.erp.service.hrm.LoanTypeService;

@RestController
@RequestMapping("hrm/loantypes")
@Api(name = "LoanTypes", description = "LoanTypes endpoint", group = "HRM")
public class LoanTypeController extends BaseController {

	@Autowired
	LoanTypeService loanTypeService;

	@RequestMapping(method = RequestMethod.POST)
	public ERPLoanType createLoanType(@RequestBody @Valid ERPLoanType loanType,
			HttpServletRequest request, HttpServletResponse response) {

		return loanTypeService.createLoanType(loanType);

	}

	@RequestMapping(value = "/{typeId}", method = RequestMethod.GET)
	public ERPLoanType getLoanTypeById(@PathVariable("typeId") Integer typeId) {

		return loanTypeService.getLoanTypeById(typeId);

	}

	@RequestMapping(value = "/{typeId}", method = RequestMethod.PUT)
	public ERPLoanType updateLoanType(@PathVariable Integer typeId,
			@RequestBody ERPLoanType loanType) {
		loanType.setId(typeId);
		return loanTypeService.updateLoanType(loanType);
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<ERPLoanType> getLoanTypes() {

		return loanTypeService.getAllLoanTypes();

	}

	@RequestMapping(value = "/{typeId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable Integer typeId) {

		loanTypeService.deleteLoanType(typeId);
	}

}
