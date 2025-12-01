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
import com.cassinisys.erp.model.production.ERPProcess;
import com.cassinisys.erp.service.production.ProcessService;

@RestController
@RequestMapping("production/process")
@Api(name="work",description="work endpoint",group="PRODUCTION")
public class ProcessController extends BaseController {
	
	@Autowired
	private ProcessService ProcessService;
	@Autowired
	private PageRequestConverter pageRequestConverter;
	
	
	@RequestMapping(method = RequestMethod.POST)
	public ERPProcess createProcess(@RequestBody @Valid ERPProcess process,
			HttpServletRequest request, HttpServletResponse response) {

		return ProcessService.create(process);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ERPProcess getProcessById(
			@PathVariable("id") Integer id) {

		return ProcessService.get(id);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ERPProcess update(@PathVariable("id") Integer id,
			@RequestBody ERPProcess process) {

		process.setId(id);
		return ProcessService.update(process);
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<ERPProcess> getAllProcesss() {

		return ProcessService.getAll();

	}

	@RequestMapping(value = "/pagable", method = RequestMethod.GET)
	public Page<ERPProcess> getProcesss(ERPPageRequest pageRequest) {
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return ProcessService.findAll(pageable);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") Integer id) {

		ProcessService.delete(id);
	}

}
