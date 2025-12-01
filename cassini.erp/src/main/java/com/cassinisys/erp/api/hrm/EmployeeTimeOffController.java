package com.cassinisys.erp.api.hrm;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.api.filtering.EmployeeTimeOffPredicateBuilder;
import com.cassinisys.erp.converters.PageRequestConverter;
import com.cassinisys.erp.model.api.criteria.EmployeeTimeOffCriteria;
import com.cassinisys.erp.model.api.paging.ERPPageRequest;
import com.cassinisys.erp.model.hrm.ERPEmployeeTimeOff;
import com.cassinisys.erp.model.hrm.QERPEmployeeTimeOff;
import com.cassinisys.erp.service.hrm.EmployeeTimeOffService;
import com.mysema.query.types.Predicate;

@RestController
@RequestMapping("hrm/employeetimeoff")
@Api(name="EmployeeTimeOff's",description="EmployeeTimeOff's endpoint",group="HRM")
public class EmployeeTimeOffController extends BaseController {

	@Autowired
	EmployeeTimeOffService employeeTimeOffService;
	
	@Autowired
	EmployeeTimeOffPredicateBuilder predicateBuilder;
	
	@Autowired
	PageRequestConverter pageRequestConverter;
	

	
	@RequestMapping(method = RequestMethod.POST)
	public ERPEmployeeTimeOff createEmployeeTimeOff(@RequestBody @Valid ERPEmployeeTimeOff empTimeOff,
			HttpServletRequest request, HttpServletResponse response) {

		return employeeTimeOffService.saveEmployeeTimeOff(empTimeOff);

	}

	
	@RequestMapping(value = "/{employeeId}", method = RequestMethod.GET)
	public List<ERPEmployeeTimeOff> getEmployeeTimeOff(@PathVariable("employeeId") Integer employeeId) {

		return employeeTimeOffService.getEmployeeTimeOff(employeeId);

	}

	
	/*@RequestMapping(value = "{employeeId}", method = RequestMethod.PUT)
	public ERPEmployeeTimeOff updateEmployeeTimeOff(@PathVariable Integer employeeId,
			@RequestBody ERPEmployeeTimeOff empTimeOff) {
		loanPayments.setLoanId(loanId);
		return employeeTimeOffService.updateEmployeeTimeOff(empTimeOff);
	}*/


	@RequestMapping(value = "/byMonth/{employeeId}/{month}/{year}", method = RequestMethod.GET)
	public List<ERPEmployeeTimeOff> getEmployeeTimeOffByMonthAndYear(@PathVariable Integer employeeId, @PathVariable  Integer month,@PathVariable Integer year) {

		return employeeTimeOffService.getEmployeeTimeOffsByMonthAndYear(employeeId, month, year);

	}



	@RequestMapping(value = "/{employeeId}/{fromDate}/{toDate}", method = RequestMethod.GET)
	public List<ERPEmployeeTimeOff> getEmployeeTimeOff(@PathVariable Integer employeeId, @PathVariable  @DateTimeFormat(pattern="DD-MM-YYYY") Date fromDate, @PathVariable  @DateTimeFormat(pattern="DD-MM-YYYY") Date toDate ) {

		return employeeTimeOffService.getEmployeeTimeOffsIdAndDateRange(employeeId, fromDate, toDate);

	}
	
	@RequestMapping(value = "/timeoffcount/{fromDate}/{toDate}", method = RequestMethod.GET)
	public Integer getEmployeeTimeOff(@PathVariable  @DateTimeFormat(pattern="DD-MM-YYYY") Date fromDate, @PathVariable @DateTimeFormat(pattern="DD-MM-YYYY") Date toDate ) {

		return employeeTimeOffService.getValidTimeOffCountDaysBetweenDates(fromDate, toDate);

	}

	
	@RequestMapping(value = "/approve/{id}", method = RequestMethod.GET)
	public ERPEmployeeTimeOff approveTimeOff(@PathVariable("id")Integer id   ) {

		return employeeTimeOffService.approveTimeOff(id);

	}

	
	@RequestMapping(value = "/pageable", method = RequestMethod.GET)
	public Page<ERPEmployeeTimeOff> getEmployeeTimeOff(EmployeeTimeOffCriteria criteria,
			ERPPageRequest pageRequest) {
		
		Predicate predicate = predicateBuilder.build(criteria,
				QERPEmployeeTimeOff.eRPEmployeeTimeOff);
		Pageable pageable = pageRequestConverter.convert(pageRequest);

		return employeeTimeOffService.findAll(predicate, pageable);

	}	
	
	
	
	
}
