package com.cassinisys.erp.api.common;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.model.common.ERPAutoNumber;
import com.cassinisys.erp.service.common.AutoNumberService;

import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.util.List;

/**
 * Created by reddy on 7/2/15.
 */
@RestController
@RequestMapping("common/autonumbers")
@Api(name = "Autonumbers", description = "Autonumbers endpoint", group = "COMMON")
public class AutonumberController extends BaseController {

	@Autowired
	private AutoNumberService autoNumberService;

	@RequestMapping(method = RequestMethod.GET)
	public List<ERPAutoNumber> getAutonumbers(HttpServletRequest request,
			HttpServletResponse response) {
		return autoNumberService.getAllAutonumbers();
	}

	@RequestMapping(method = RequestMethod.POST)
	public List<ERPAutoNumber> saveAutonumbers(
			@RequestBody @Valid List<ERPAutoNumber> autonumbers,
			HttpServletRequest request, HttpServletResponse response) {
		return autoNumberService.save(autonumbers);
	}

	@RequestMapping(value = "/{autonumberId}/next", method = RequestMethod.GET)
	public String getNextNumber(@PathVariable Integer autonumberId,
			HttpServletRequest request, HttpServletResponse response) {
		return autoNumberService.getNextNumber(autonumberId);
	}

}
