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
import com.cassinisys.erp.model.hrm.ERPAllowanceType;
import com.cassinisys.erp.service.hrm.AllowanceTypeService;

@RestController
@RequestMapping("hrm/allowancetypes")
@Api(name = "EmployeeAllowanceTypes", description = "EmployeeAllowanceTypes endpoint", group = "HRM")
public class EmployeeAllowanceTypeController extends BaseController {

	@Autowired
	AllowanceTypeService allowanceTypeTypeService;

	@RequestMapping(method = RequestMethod.POST)
	public ERPAllowanceType createAllowanceType(
			@RequestBody @Valid ERPAllowanceType allowanceType,
			HttpServletRequest request, HttpServletResponse response) {

		return allowanceTypeTypeService.createAllowanceType(allowanceType);

	}

	@RequestMapping(value = "/{typeId}", method = RequestMethod.GET)
	public ERPAllowanceType getAllowanceTypeById(
			@PathVariable("typeId") Integer typeId) {

		return allowanceTypeTypeService.getAllowanceTypeById(typeId);

	}

	@RequestMapping(value = "/{typeId}", method = RequestMethod.PUT)
	public ERPAllowanceType updateAllowanceType(@PathVariable Integer typeId,
			@RequestBody ERPAllowanceType allowanceType) {
		allowanceType.setId(typeId);
		return allowanceTypeTypeService.updateAllowanceType(allowanceType);
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<ERPAllowanceType> getAllEmployeeTypes() {

		return allowanceTypeTypeService.getAllowanceTypes();

	}

	@RequestMapping(value = "/{typeId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable Integer typeId) {

		allowanceTypeTypeService.deleteAllowanceType(typeId);
	}

}
