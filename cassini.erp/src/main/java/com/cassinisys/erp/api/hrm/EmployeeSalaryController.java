package com.cassinisys.erp.api.hrm;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.model.hrm.ERPEmployeeSalary;
import com.cassinisys.erp.service.hrm.EmployeeSalaryService;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/hrm/salaries")
@Api(name="EmployeePay",description="EmployeePay endpoint",group="HRM")
public class EmployeeSalaryController extends BaseController {

	@Autowired
	EmployeeSalaryService employeeSalaryService;

	@RequestMapping(method = RequestMethod.GET)
	public List<ERPEmployeeSalary> getAllEmployeeSalaries() {
		return employeeSalaryService.getAllEmployeeSalaries();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ERPEmployeeSalary getEmployeeSalaryById(@PathVariable @Valid Integer id) {
		return employeeSalaryService.getEmployeeSalaryById(id);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ERPEmployeeSalary create(@RequestBody ERPEmployeeSalary employeepay) {
		return employeeSalaryService.create(employeepay);
	}
	
	@RequestMapping(value = "/multiple", method = RequestMethod.POST)
	public List<ERPEmployeeSalary> createEmployeeSalaries(
			@RequestBody List<ERPEmployeeSalary> employeePays) {
		return employeeSalaryService.createEmployeeSalaries(employeePays);
	}

}
