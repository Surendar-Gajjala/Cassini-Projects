package com.cassinisys.erp.api.production;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.cassinisys.erp.model.api.criteria.CustomerCriteria;
import com.cassinisys.erp.model.api.criteria.EmpWorkShiftCriteria;
import com.cassinisys.erp.model.crm.ERPSalesRepFieldReport;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.converters.PageRequestConverter;
import com.cassinisys.erp.model.api.paging.ERPPageRequest;
import com.cassinisys.erp.model.production.ERPWorkShiftEmployee;
import com.cassinisys.erp.service.production.WorkShiftEmployeeService;


@RestController
@RequestMapping("production/workshiftemployee")
@Api(name="work",description="work endpoint",group="PRODUCTION")
public class WorkShiftEmployeeController extends BaseController {
	
	@Autowired
	private WorkShiftEmployeeService workShiftEmployeeService;
	@Autowired
	private PageRequestConverter pageRequestConverter;
	
	
	@RequestMapping(method = RequestMethod.POST)
	public List<ERPWorkShiftEmployee> createWorkShiftEmployee(@RequestBody @Valid List<ERPWorkShiftEmployee> workShiftEmployee,
			HttpServletRequest request, HttpServletResponse response) {

		return workShiftEmployeeService.createEmpWorkList(workShiftEmployee);

	}

	/*@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ERPWorkShiftEmployee getWorkShiftEmployeeById(
			@PathVariable("id") Integer id) {

		return workShiftEmployeeService.get(id);

	}*/

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ERPWorkShiftEmployee update(@PathVariable("id") Integer id,
			@RequestBody ERPWorkShiftEmployee shiftEmployee) {

		shiftEmployee.setRowId(id);
		return workShiftEmployeeService.update(shiftEmployee);
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<ERPWorkShiftEmployee> getAllWorkShiftEmployees() {

		return workShiftEmployeeService.getAll();

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public List<ERPWorkShiftEmployee> getWorkShiftEmployees(@PathVariable("id") Integer id) {

		return workShiftEmployeeService.findByShift(id);

	}

	@RequestMapping(value = "/allShiftEmps", method = RequestMethod.GET)
	public Page<ERPWorkShiftEmployee> getEmpWorkShiftsByShiftId(EmpWorkShiftCriteria criteria,
	ERPPageRequest pageRequest) {

		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return workShiftEmployeeService.find(criteria, pageable);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") Integer id) {

		workShiftEmployeeService.delete(id);
	}

}
