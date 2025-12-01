package com.cassinisys.erp.api.hrm;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.api.filtering.EmployeePredicateBuilder;
import com.cassinisys.erp.converters.PageRequestConverter;
import com.cassinisys.erp.model.api.criteria.EmployeeCriteria;
import com.cassinisys.erp.model.api.paging.ERPPageRequest;
import com.cassinisys.erp.model.hrm.ERPEmployee;
import com.cassinisys.erp.model.hrm.EmployeePay;
import com.cassinisys.erp.model.hrm.QERPEmployee;
import com.cassinisys.erp.service.hrm.EmployeeService;
import com.mysema.query.types.Predicate;
import org.apache.commons.io.IOUtils;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by reddy on 7/18/15.
 */
@RestController
@RequestMapping("hrm/employees")
@Api(name = "Employees", description = "Employees endpoint", group = "HRM")
public class EmployeeController extends BaseController {

	private EmployeeService employeeService;

	private PageRequestConverter pageRequestConverter;

	private EmployeePredicateBuilder predicateBuilder;

	@Autowired
	public EmployeeController(EmployeeService employeeService,
			PageRequestConverter pageRequestConverter,
			EmployeePredicateBuilder predicateBuilder) {
		this.employeeService = employeeService;
		this.pageRequestConverter = pageRequestConverter;
		this.predicateBuilder = predicateBuilder;
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public List<ERPEmployee> getAllEmployees() {
		return employeeService.getAllEmployees();
	}

	/*
	 * @RequestMapping(method = RequestMethod.GET) public Page<ERPEmployee>
	 * getAllEmployees(ERPPageRequest pageRequest) { Pageable pageable =
	 * pageRequestConverter.convert(pageRequest); return
	 * employeeService.getAllEmployees(pageable); }
	 */

	@RequestMapping(method = RequestMethod.GET)
	public Page<ERPEmployee> getAllEmployees(ERPPageRequest pageRequest,
			EmployeeCriteria criteria) {
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		if(criteria != null) {
			Predicate predicate = predicateBuilder.build(criteria,
					QERPEmployee.eRPEmployee);
			return employeeService.getAllEmployees(predicate, pageable);
		}
		else {
			return employeeService.getAllEmployees(pageable);
		}

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ERPEmployee getEmployeeById(@PathVariable("id") int id) {
		return employeeService.getEmployeeById(id);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ERPEmployee saveEmployee(@RequestBody @Valid ERPEmployee employee) {
		return employeeService.create(employee);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ERPEmployee updateEmployee(@PathVariable("id") int id,
			@RequestBody @Valid ERPEmployee employee) {
		employee.setId(id);
		return employeeService.update(employee);
	}

	@RequestMapping(value = "/emppay", method = RequestMethod.GET)
	public List<EmployeePay> getAllEmpPays(ERPPageRequest pageRequest) {
		pageRequest.setSize(500);
		Pageable pageable = pageRequestConverter.convert(pageRequest);

		return employeeService.getAllEmployeesPayDetails(pageable);
	}

	@RequestMapping(value = "/emppay", method = RequestMethod.PUT)
	public EmployeePay updateEmployee(@RequestBody @Valid EmployeePay empPay) {

		return employeeService.updateEmployeePayDetails(empPay);
	}


	@RequestMapping(value = "/{id}/picture", method = RequestMethod.POST)
	public ERPEmployee updatePicture(@PathVariable ("id") Integer employeeId,
									 MultipartHttpServletRequest request) {
		ERPEmployee emp = employeeService.getEmployeeById(employeeId);

		Collection<MultipartFile> files = request.getFileMap().values();
		List<MultipartFile> list = new ArrayList<>(files);

		if(list.size() > 0) {
			MultipartFile file = list.get(0);

			try {
				byte[] bytes = IOUtils.toByteArray(file.getInputStream());
				emp.setPicture(bytes);
				return employeeService.update(emp);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return emp;
	}


}
