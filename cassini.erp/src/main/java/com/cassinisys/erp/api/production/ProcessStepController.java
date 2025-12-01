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
import com.cassinisys.erp.model.production.ERPProcessStep;
import com.cassinisys.erp.service.production.ProcessStepService;

@RestController
@RequestMapping("production/processstep")
@Api(name="work",description="work endpoint",group="PRODUCTION")
public class ProcessStepController extends BaseController {
	
	@Autowired
	private ProcessStepService processStepService;
	@Autowired
	private PageRequestConverter pageRequestConverter;
	
	
	@RequestMapping(method = RequestMethod.POST)
	public ERPProcessStep createProcessStep(@RequestBody @Valid ERPProcessStep ProcessStep,
			HttpServletRequest request, HttpServletResponse response) {

		return processStepService.create(ProcessStep);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ERPProcessStep getProcessStepById(
			@PathVariable("id") Integer id) {

		return processStepService.get(id);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ERPProcessStep update(@PathVariable("id") Integer id,
			@RequestBody ERPProcessStep ProcessStep) {

		ProcessStep.setId(id);
		return processStepService.update(ProcessStep);
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<ERPProcessStep> getAllProcessSteps() {

		return processStepService.getAll();

	}

	@RequestMapping(value = "/pagable", method = RequestMethod.GET)
	public Page<ERPProcessStep> getProcessSteps(ERPPageRequest pageRequest) {
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return processStepService.findAll(pageable);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") Integer id) {

		processStepService.delete(id);
	}

}
