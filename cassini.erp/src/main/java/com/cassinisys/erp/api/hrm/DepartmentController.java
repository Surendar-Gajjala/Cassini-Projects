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
import com.cassinisys.erp.converters.PageRequestConverter;
import com.cassinisys.erp.model.hrm.ERPDepartment;
import com.cassinisys.erp.service.hrm.DepartmentService;


@RestController
@RequestMapping("hrm/departments")
@Api(name="Departments",description="Departments endpoint",group="HRM")
public class DepartmentController extends BaseController {

	
	private DepartmentService departmentService;
	
	private PageRequestConverter pageRequestConverter;

	
	@Autowired
	public DepartmentController(DepartmentService departmentService,
			PageRequestConverter pageRequestConverter) {
		this.pageRequestConverter = pageRequestConverter;
		this.departmentService = departmentService;
	}



	/**
	 * 
	 * @param department
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ERPDepartment createDepartment(@RequestBody @Valid ERPDepartment department,
			HttpServletRequest request, HttpServletResponse response) {

		return departmentService.create(department);

	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/{id}" ,method = RequestMethod.GET)
	public ERPDepartment getDepartmentById(@PathVariable("id") Integer id) {

		return departmentService.get(id);

	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.PUT)
	public ERPDepartment update(@PathVariable Integer id,
			@RequestBody ERPDepartment department) {
		department.setId(id);
		return departmentService.update(department);
	}

	
	/**
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public List<ERPDepartment> getAllDepartments(){
		
		return departmentService.getAll();
		
	}
	
	/**
	 * 	
	 * @param id
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable Integer id){

		departmentService.delete(id);
	}
}
