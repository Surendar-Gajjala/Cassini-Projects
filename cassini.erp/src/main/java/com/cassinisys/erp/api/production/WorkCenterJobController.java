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
import com.cassinisys.erp.model.production.ERPWorkCenterJob;
import com.cassinisys.erp.service.production.WorkCenterJobService;

@RestController
@RequestMapping("production/workscenterjob")
@Api(name="work",description="work endpoint",group="PRODUCTION")
public class WorkCenterJobController extends BaseController {
	
	@Autowired
	private WorkCenterJobService workCenterJobService;
	@Autowired
	private PageRequestConverter pageRequestConverter;
	
	
	@RequestMapping(method = RequestMethod.POST)
	public ERPWorkCenterJob createWorkCenterJob(@RequestBody @Valid ERPWorkCenterJob workCenterJob,
			HttpServletRequest request, HttpServletResponse response) {

		return workCenterJobService.create(workCenterJob);

	}

	@RequestMapping(value = "/{centerJobId}", method = RequestMethod.GET)
	public ERPWorkCenterJob getWorkCenterJobById(
			@PathVariable("centerJobId") Integer centerJobId) {

		return workCenterJobService.get(centerJobId);

	}

	@RequestMapping(value = "/{centerJobId}", method = RequestMethod.PUT)
	public ERPWorkCenterJob update(@PathVariable("centerJobId") Integer CenterJobId,
			@RequestBody ERPWorkCenterJob CenterJob) {

		CenterJob.setId(CenterJobId);
		return workCenterJobService.update(CenterJob);
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<ERPWorkCenterJob> getAllWorkCenterJobs() {

		return workCenterJobService.getAll();

	}

	@RequestMapping(value = "/pagable", method = RequestMethod.GET)
	public Page<ERPWorkCenterJob> getWorkCenterJobs(ERPPageRequest pageRequest) {
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return workCenterJobService.findAll(pageable);

	}

	@RequestMapping(value = "/{centerJobId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("centerJobId") Integer centerJobId) {

		//workCenterJobService.delete(centerJobId);
	}

}
