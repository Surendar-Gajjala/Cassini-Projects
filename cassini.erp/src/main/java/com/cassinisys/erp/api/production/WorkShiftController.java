package com.cassinisys.erp.api.production;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.converters.PageRequestConverter;
import com.cassinisys.erp.model.api.paging.ERPPageRequest;
import com.cassinisys.erp.model.production.ERPWorkShift;
import com.cassinisys.erp.service.production.WorkShiftService;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("production/workshift")
@Api(name="work",description="work endpoint",group="PRODUCTION")
public class WorkShiftController extends BaseController {

	@Autowired
	private WorkShiftService workShiftService;
	@Autowired
	private PageRequestConverter pageRequestConverter;
	
	
	@RequestMapping(method = RequestMethod.POST)
	public ERPWorkShift createWorkShift(@RequestBody @Valid ERPWorkShift workShift,
			HttpServletRequest request, HttpServletResponse response) {

		return workShiftService.create(workShift);

	}

	@RequestMapping(value = "/{shiftId}", method = RequestMethod.GET)
	public ERPWorkShift getWorkShiftById(
			@PathVariable("shiftId") Integer shiftId) {

		return workShiftService.get(shiftId);

	}

	@RequestMapping(value = "/{shiftId}", method = RequestMethod.PUT)
	public ERPWorkShift update(@PathVariable("shiftId") Integer shiftId,
			@RequestBody ERPWorkShift shift) {

		shift.setShiftId(shiftId);
		return workShiftService.update(shift);
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<ERPWorkShift> getAllWorkShifts() {

		return workShiftService.getAll();

	}

	@RequestMapping(value = "/pagable", method = RequestMethod.GET)
	public Page<ERPWorkShift> getWorkShifts(ERPPageRequest pageRequest) {
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return workShiftService.findAll(pageable);

	}

	@RequestMapping(value = "/{shiftId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("shiftId") Integer shiftId) {

		//workShiftService.delete(shiftId);
	}
}
