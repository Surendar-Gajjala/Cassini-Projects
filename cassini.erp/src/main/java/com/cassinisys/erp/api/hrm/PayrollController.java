package com.cassinisys.erp.api.hrm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.converters.PageRequestConverter;
import com.cassinisys.erp.model.api.paging.ERPPageRequest;
import com.cassinisys.erp.model.hrm.ERPPayroll;
import com.cassinisys.erp.model.hrm.EmpPayrollDTO;
import com.cassinisys.erp.model.hrm.PayrollDTO;
import com.cassinisys.erp.service.hrm.PayrollService;

@RestController
@RequestMapping("/hrm/payroll")
@Api(name = "Payrolls", description = "Payroll endpoint", group = "HRM")
public class PayrollController extends BaseController {

	private PayrollService payrollService;

	private PageRequestConverter pageRequestConverter;

	@Autowired
	public PayrollController(PayrollService payrollService,
			PageRequestConverter pageRequestConverter) {
		this.payrollService = payrollService;
		this.pageRequestConverter = pageRequestConverter;
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<ERPPayroll> getAllPayroll() {
		return payrollService.getAllPayroll(null);
	}

	@RequestMapping(value = "/year/{year}", method = RequestMethod.GET)
	public List<ERPPayroll> getAllPayrollForYear(
			@PathVariable("year") Integer year) {
		return payrollService.getAllPayroll(year);
	}

	@RequestMapping(value = "/year/{year}/month/{month}", method = RequestMethod.GET)
	public PayrollDTO getAllPayrollForYear(
			@PathVariable("year") Integer year,
			@PathVariable("month") Integer month) {
		return payrollService.getAllPayrollByMonthAndYear(year, month);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ERPPayroll getAllPayrollById(@PathVariable("id") int id) {
		return payrollService.getAllPayrollById(id);
	}

	@RequestMapping(value = "/new/{month}", method = RequestMethod.GET)
	public PayrollDTO createNewPayroll(@PathVariable("month") int month,ERPPageRequest pageRequest) {
		pageRequest.setSize(500);
		Pageable pageable = pageRequestConverter.convert(pageRequest);

		return payrollService.createPayrollGet(month,pageable,true);
	}

	@RequestMapping(method = RequestMethod.POST)
	public PayrollDTO create(@RequestBody @Valid PayrollDTO payroll,
			HttpServletRequest request, HttpServletResponse response) {
		return payrollService.create(payroll);
	}
	
	@RequestMapping(value = "/profilepay/{empid}/{year}", method = RequestMethod.GET)
	public List<EmpPayrollDTO> getEmpProfilePayByYear(@PathVariable("empid")Integer empid,
			@PathVariable("year") Integer year) {
		return payrollService.getEmployeePayDetailsForCurrentYear(empid, year);
	}


}
