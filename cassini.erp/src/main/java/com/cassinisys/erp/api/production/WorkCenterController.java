package com.cassinisys.erp.api.production;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.converters.PageRequestConverter;
import com.cassinisys.erp.model.api.paging.ERPPageRequest;
import com.cassinisys.erp.model.production.ERPWorkCenter;
import com.cassinisys.erp.service.production.WorkCenterService;


@RestController
@RequestMapping("production/workcenter")
@Api(name="work",description="work endpoint",group="PRODUCTION")
public class WorkCenterController extends BaseController {
	
	@Autowired
	private WorkCenterService workCenterService;
	@Autowired
	private PageRequestConverter pageRequestConverter;
	
	
	@RequestMapping(method = RequestMethod.POST)
	public ERPWorkCenter createWorkCenter(@RequestBody @Valid ERPWorkCenter workCenter,
			HttpServletRequest request, HttpServletResponse response) {

		return workCenterService.create(workCenter);

	}

	@RequestMapping(value = "/{centerId}", method = RequestMethod.GET)
	public ERPWorkCenter getWorkCenterById(
			@PathVariable("centerId") Integer centerId) {

		return workCenterService.get(centerId);

	}

	@RequestMapping(value = "/{centerId}", method = RequestMethod.PUT)
	public ERPWorkCenter update(@PathVariable("centerId") Integer centerId,
			@RequestBody ERPWorkCenter center) {

		center.setId(centerId);
		return workCenterService.update(center);
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<ERPWorkCenter> getAllWorkCenters() {

		return workCenterService.getAll();

	}

	@RequestMapping(value = "/pagable", method = RequestMethod.GET)
	public Page<ERPWorkCenter> getWorkCenters(ERPPageRequest pageRequest) {
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return workCenterService.findAll(pageable);

	}

	@RequestMapping(value = "/{centerId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("centerId") Integer centerId) {

		workCenterService.delete(centerId);
	}

}
