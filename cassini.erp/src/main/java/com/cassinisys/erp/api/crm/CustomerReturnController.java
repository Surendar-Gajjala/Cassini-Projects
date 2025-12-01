package com.cassinisys.erp.api.crm;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.api.exceptions.ERPException;
import com.cassinisys.erp.api.exceptions.ErrorCodes;
import com.cassinisys.erp.converters.PageRequestConverter;
import com.cassinisys.erp.model.api.criteria.CustomerReturnCriteria;
import com.cassinisys.erp.model.api.paging.ERPPageRequest;
import com.cassinisys.erp.model.crm.ERPCustomerOrder;
import com.cassinisys.erp.model.crm.ERPCustomerReturn;
import com.cassinisys.erp.model.crm.ERPCustomerReturnDetails;
import com.cassinisys.erp.model.hrm.ERPEmployee;
import com.cassinisys.erp.service.crm.CustomerReturnService;
import com.cassinisys.erp.service.hrm.EmployeeService;
import com.cassinisys.erp.service.security.PermissionCheck;
import com.cassinisys.erp.service.security.SessionWrapper;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("crm/customerReturns")
@Api(name="CustomerReturns",description="CustomerReturns endpoint",group="CRM")
public class CustomerReturnController extends BaseController {

	private CustomerReturnService customerReturnService;
	private EmployeeService employeeService;
	private SessionWrapper sessionWrapper;
	private PageRequestConverter pageRequestConverter;

	@Autowired
	public CustomerReturnController(CustomerReturnService customerReturnService,EmployeeService employeeService,SessionWrapper sessionWrapper,
			PageRequestConverter pageRequestConverter) {
		this.pageRequestConverter = pageRequestConverter;
		this.customerReturnService = customerReturnService;
		this.employeeService = employeeService;
		this.sessionWrapper = sessionWrapper;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ERPCustomerReturn createCustomerReturn(@RequestBody @Valid ERPCustomerReturn customerReturn,
			HttpServletRequest request, HttpServletResponse response) {
		return customerReturnService.create(customerReturn);
	}

	@RequestMapping(value = "/{returnId}", method = RequestMethod.GET)
	public ERPCustomerReturn getCustomerReturnById(@PathVariable("returnId") Integer returnId) {
		return customerReturnService.get(returnId);
	}

	@RequestMapping(value = "/{returnId}", method = RequestMethod.PUT)
	public ERPCustomerReturn update(@PathVariable("returnId") Integer returnId,
			@RequestBody ERPCustomerReturn customerReturn) {
		customerReturn.setId(returnId);
		return customerReturnService.update(customerReturn);
	}

	@RequestMapping(method = RequestMethod.GET)
	public Page<ERPCustomerReturn> getAllReturns(CustomerReturnCriteria criteria,
												 ERPPageRequest pageRequest) {
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return customerReturnService.findAll(criteria, pageable);
	}

	@RequestMapping(value = "/new/approveAll", method = RequestMethod.GET)
	public List<ERPCustomerReturn> approveAllNewReturns() {
		return customerReturnService.approveAllNewReturns();
	}

	@RequestMapping(value = "/{returnId}/approve", method = RequestMethod.GET)
	public ERPCustomerReturn approveReturn(
			@PathVariable("returnId") Integer returnId) {
		Integer id = sessionWrapper.getSession().getLogin().getPerson().getId();
		ERPEmployee employee = employeeService.getEmployeeById(id);
		if (employee != null) {
			return customerReturnService.approveReturn(returnId);
		} else {
			throw new ERPException(HttpStatus.UNAUTHORIZED, ErrorCodes.GENERAL,
					"You are not authorized to approve returns");
		}
	}

	@RequestMapping(value = "/{returnId}/cancel", method = RequestMethod.GET)
	public ERPCustomerReturn cancelOrder(
			@PathVariable("returnId") Integer returnId) {
		Integer id = sessionWrapper.getSession().getLogin().getPerson().getId();
		ERPEmployee employee = employeeService.getEmployeeById(id);
		if (employee != null) {
			return customerReturnService.cancelReturn(returnId);
		} else {
			throw new ERPException(HttpStatus.UNAUTHORIZED, ErrorCodes.GENERAL,
					"You are not authorized to cancel orders");
		}
	}

	@RequestMapping(value = "/{returnId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("returnId") Integer returnId) {
		customerReturnService.delete(returnId);
	}

	@RequestMapping(value = "/{returnId}/details", method = RequestMethod.GET)
	public List<ERPCustomerReturnDetails> getReturnDetails(
			@PathVariable("returnId") Integer returnId) {
		return customerReturnService.getCustomerDetailsByReturnId(returnId);
	}
}
