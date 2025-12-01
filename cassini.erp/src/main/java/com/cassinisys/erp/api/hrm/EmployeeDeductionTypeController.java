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
import com.cassinisys.erp.model.hrm.ERPDeductionType;
import com.cassinisys.erp.service.hrm.DeductionTypeService;

@RestController
@RequestMapping("hrm/deductiontypes")
@Api(name = "EmployeeDeductionTypes", description = "EmployeeDeductionTypes endpoint", group = "HRM")
public class EmployeeDeductionTypeController extends BaseController {

	@Autowired
	DeductionTypeService deductionTypeService;

	@RequestMapping(method = RequestMethod.POST)
	public ERPDeductionType createDeductionType(
			@RequestBody @Valid ERPDeductionType deductionType,
			HttpServletRequest request, HttpServletResponse response) {

		return deductionTypeService.createDeductionType(deductionType);

	}

	@RequestMapping(value = "/{typeId}", method = RequestMethod.GET)
	public ERPDeductionType getDeductionTypeById(
			@PathVariable("typeId") Integer typeId) {

		return deductionTypeService.getDeductionTypeById(typeId);

	}

	@RequestMapping(value = "/{typeId}", method = RequestMethod.PUT)
	public ERPDeductionType updateDeductionType(@PathVariable Integer typeId,
			@RequestBody ERPDeductionType deductionType) {
		deductionType.setId(typeId);
		return deductionTypeService.updateDeductionType(deductionType);
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<ERPDeductionType> getDeductionTypes() {

		return deductionTypeService.getDeductionTypes();

	}

	@RequestMapping(value = "/{typeId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable Integer typeId) {

		deductionTypeService.deleteDeductionType(typeId);
	}

}
