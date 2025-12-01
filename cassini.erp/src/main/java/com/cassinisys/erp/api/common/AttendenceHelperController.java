package com.cassinisys.erp.api.common;

import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cassinisys.erp.model.common.AttendenceHelperDTO;
import com.cassinisys.erp.service.common.AttendenceHelperService;

@RestController
@RequestMapping("common/attendencehelper")
@Api(name = "AttendenceHelper", description = "Attendence Help Details", group = "COMMON")
public class AttendenceHelperController {

	@Autowired
	AttendenceHelperService attendenceHelperService;

	@RequestMapping(method = RequestMethod.GET)
	public AttendenceHelperDTO getCurrentMonthDetails() {

		return attendenceHelperService.getMonthDetails();
	}

}
