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
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.model.hrm.ERPEmployeeAttendance;
import com.cassinisys.erp.model.hrm.ERPEmployeeAttendanceDTO;
import com.cassinisys.erp.service.hrm.EmployeeAttendanceService;

@RestController
@RequestMapping("hrm/employeeattendace")
@Api(name = "EmployeeAttendance", description = "EmployeeAttendance endpoint", group = "HRM")
public class EmployeeAttendanceController extends BaseController {

	@Autowired
	EmployeeAttendanceService employeeAttendanceService;

	@RequestMapping(method = RequestMethod.POST)
	public ERPEmployeeAttendance createEmployeeAttendance(
			@RequestBody @Valid ERPEmployeeAttendance empAttendance,
			HttpServletRequest request, HttpServletResponse response) {

		return employeeAttendanceService.saveEmployeeAttendance(empAttendance);

	}

	@RequestMapping(value = "/{employeeId}/{month}/{year}", method = RequestMethod.GET)
	public ERPEmployeeAttendanceDTO getEmployeeAttendance(
			@PathVariable("employeeId") Integer employeeId,@PathVariable("month") Integer month,@PathVariable("year") Integer year) {

		return employeeAttendanceService.getEmployeeAttendance(employeeId,month,year);

	}
	
	
	@RequestMapping(value = "/{month}/{year}", method = RequestMethod.GET)
	public List<ERPEmployeeAttendanceDTO> getEmployeesAttendance(@PathVariable("month") Integer month,@PathVariable("year") Integer year) {

		return employeeAttendanceService.getEmployeesAttendance(month,year);

	}

	@RequestMapping(value = "/{month}/{year}/count", method = RequestMethod.GET)
	public Integer getEmployeesAttendanceCount(@PathVariable("month") Integer month,@PathVariable("year") Integer year) {

		return employeeAttendanceService.getAttendenceCountByMonth(month,year);

	}


	@RequestMapping(value = "/attachment",method = RequestMethod.POST)
	public String uploadAttendenceFile( MultipartHttpServletRequest request) {
		return employeeAttendanceService.uploadAttendence(request.getFileMap());

	}


	@RequestMapping(value = "/{date}", method = RequestMethod.GET)
	public List<ERPEmployeeAttendance> getEmployeesAttendanceByDate(
			@PathVariable("date") String date) {

		return employeeAttendanceService.getAttendenceDetailsByDate(date);

	}

}
