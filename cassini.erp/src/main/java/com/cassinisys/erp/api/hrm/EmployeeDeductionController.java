package com.cassinisys.erp.api.hrm;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.model.hrm.ERPEmployeeDeduction;
import com.cassinisys.erp.service.hrm.EmployeeDeductionService;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hrm/deductions")
@Api(name = "EmployeeDeductions", description = "EmployeeDeductions endpoint", group = "HRM")
public class EmployeeDeductionController extends BaseController {

    @Autowired
    EmployeeDeductionService employeeDeductionService;

    @RequestMapping(method = RequestMethod.GET)
    public List<ERPEmployeeDeduction> getAllEmployeesDeductions() {
        return employeeDeductionService.getAllEmployeeDeductions();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ERPEmployeeDeduction create(
            @RequestBody ERPEmployeeDeduction employeeDeduction) {
        return employeeDeductionService.create(employeeDeduction);
    }

    @RequestMapping(value = "/createall", method = RequestMethod.POST)
    public List<ERPEmployeeDeduction> createEmployeeDeductions(
            @RequestBody List<ERPEmployeeDeduction> employeeDeductions) {
        return employeeDeductionService
                .createEmployeeDeductions(employeeDeductions);
    }
}
