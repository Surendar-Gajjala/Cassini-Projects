package com.cassinisys.erp.api.hrm;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.converters.PageRequestConverter;
import com.cassinisys.erp.model.api.paging.ERPPageRequest;
import com.cassinisys.erp.model.hrm.ERPEmployeeAllowance;
import com.cassinisys.erp.service.hrm.EmployeeAllowanceService;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hrm/allowances")
@Api(name="EmployeeAllowances",description="EmployeeAllowances endpoint",group="HRM")
public class EmployeeAllowanceController extends BaseController {

	EmployeeAllowanceService employeeAllowanceService;

	private PageRequestConverter pageRequestConverter;

	@Autowired
	public EmployeeAllowanceController(
			EmployeeAllowanceService employeeallowanceservice,
			PageRequestConverter pageRequestConverter) {
		this.pageRequestConverter = pageRequestConverter;
		this.employeeAllowanceService = employeeallowanceservice;
	}

	@RequestMapping(method = RequestMethod.GET)
	public Page<ERPEmployeeAllowance> getAllEmployeesAllowances(
			ERPPageRequest pageReequest) {
		Pageable pageable = pageRequestConverter.convert(pageReequest);
		return employeeAllowanceService.getAllEmployeeAllowances(pageable);
	}



	@RequestMapping(method = RequestMethod.POST)
	public ERPEmployeeAllowance create(
			@RequestBody ERPEmployeeAllowance employeeAllowance) {
		return employeeAllowanceService.create(employeeAllowance);
	}


	@RequestMapping(value = "/multiple", method = RequestMethod.POST)
	public List<ERPEmployeeAllowance> createEmployeeAllowances(
			@RequestBody List<ERPEmployeeAllowance> employeeAllowances) {
		return employeeAllowanceService
				.createEmployeeAllowances(employeeAllowances);
	}
}
