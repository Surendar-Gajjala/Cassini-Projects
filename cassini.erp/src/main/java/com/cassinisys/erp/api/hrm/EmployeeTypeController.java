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
import com.cassinisys.erp.model.hrm.ERPEmployeeType;
import com.cassinisys.erp.service.hrm.EmployeeTypeService;

@RestController
@RequestMapping("hrm/employeetype")
@Api(name="EmployeeTypes",description="EmployeeTypes endpoint",group="HRM")
public class EmployeeTypeController extends BaseController {

	@Autowired
	EmployeeTypeService employeeTypeService;

	
	@RequestMapping(method = RequestMethod.POST)
	public ERPEmployeeType createEmployeeType(@RequestBody @Valid ERPEmployeeType empType,
			HttpServletRequest request, HttpServletResponse response) {

		return employeeTypeService.createEmployeeType(empType);

	}

	
	@RequestMapping(value = "/{typeId}", method = RequestMethod.GET)
	public ERPEmployeeType getEmployeeTypeById(@PathVariable("typeId") Integer typeId) {

		return employeeTypeService.getEmployeeTypeById(typeId);

	}

	
	@RequestMapping(value = "/{typeId}", method = RequestMethod.PUT)
	public ERPEmployeeType updateEmployeeType(@PathVariable Integer typeId,
			@RequestBody ERPEmployeeType employeeType) {
		employeeType.setId(typeId);
		return employeeTypeService.updateEmployeeType(employeeType);
	}

	
	@RequestMapping(method = RequestMethod.GET)
	public List<ERPEmployeeType> getAllEmployeeTypes() {

		return employeeTypeService.getAllEmployeeTypes();

	}

	
	@RequestMapping(value = "/{typeId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable Integer typeId) {

		employeeTypeService.deleteEmployeeType(typeId);
	}


}
