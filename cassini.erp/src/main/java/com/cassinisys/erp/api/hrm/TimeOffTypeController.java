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
import com.cassinisys.erp.model.hrm.ERPTimeOffType;
import com.cassinisys.erp.service.hrm.TimeOffTypeService;

@RestController
@RequestMapping("hrm/timeofftype")
@Api(name="TimeOffTypes",description="TimeOffTypes endpoint",group="HRM")
public class TimeOffTypeController extends BaseController {

	@Autowired
	TimeOffTypeService timeOffTypeService;

	
	@RequestMapping(method = RequestMethod.POST)
	public ERPTimeOffType createTimeOffType(@RequestBody @Valid ERPTimeOffType timeOffType,
			HttpServletRequest request, HttpServletResponse response) {

		return timeOffTypeService.createTimeOffType(timeOffType);

	}

	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ERPTimeOffType getTimeOffTypeById(@PathVariable("id") Integer id) {

		return timeOffTypeService.getTimeOffTypeById(id);

	}

	
	@RequestMapping(value = "{id}", method = RequestMethod.PUT)
	public ERPTimeOffType updateTimeOffType(@PathVariable Integer id,
			@RequestBody ERPTimeOffType timeOffType) {
		timeOffType.setId(id);
		return timeOffTypeService.updateTimeOffType(timeOffType);
	}

	
	@RequestMapping(method = RequestMethod.GET)
	public List<ERPTimeOffType> getAllTimeOffTypes() {

		return timeOffTypeService.getAllTimeOffTypes();

	}

	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable Integer id) {

		timeOffTypeService.deleteTimeOffType(id);
	}

	
}
