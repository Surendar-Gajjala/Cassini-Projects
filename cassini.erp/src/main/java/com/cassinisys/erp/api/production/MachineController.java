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
import com.cassinisys.erp.model.production.ERPMachine;
import com.cassinisys.erp.service.production.MachineService;

@RestController
@RequestMapping("production/machine")
@Api(name="work",description="work endpoint",group="PRODUCTION")
public class MachineController extends BaseController {
	
	@Autowired
	private MachineService MachineService;
	@Autowired
	private PageRequestConverter pageRequestConverter;
	
	
	@RequestMapping(method = RequestMethod.POST)
	public ERPMachine createMachine(@RequestBody @Valid ERPMachine Machine,
			HttpServletRequest request, HttpServletResponse response) {

		return MachineService.create(Machine);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ERPMachine getMachineById(
			@PathVariable("id") Integer id) {

		return MachineService.get(id);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ERPMachine update(@PathVariable("id") Integer id,
			@RequestBody ERPMachine machine) {

		machine.setId(id);
		return MachineService.update(machine);
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<ERPMachine> getAllMachines() {

		return MachineService.getAll();

	}

	@RequestMapping(value = "/pagable", method = RequestMethod.GET)
	public Page<ERPMachine> getMachines(ERPPageRequest pageRequest) {
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return MachineService.findAll(pageable);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") Integer id) {

		MachineService.delete(id);
	}

}
