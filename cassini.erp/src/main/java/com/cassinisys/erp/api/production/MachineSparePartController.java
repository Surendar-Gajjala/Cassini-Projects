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
import com.cassinisys.erp.model.production.ERPMachineSparePart;
import com.cassinisys.erp.service.production.MachineSparePartService;

@RestController
@RequestMapping("production/sparepart")
@Api(name="work",description="work endpoint",group="PRODUCTION")
public class MachineSparePartController extends BaseController {
	
	@Autowired
	private MachineSparePartService MachineSparePartService;
	@Autowired
	private PageRequestConverter pageRequestConverter;
	
	
	@RequestMapping(method = RequestMethod.POST)
	public ERPMachineSparePart createMachineSparePart(@RequestBody @Valid ERPMachineSparePart MachineSparePart,
			HttpServletRequest request, HttpServletResponse response) {

		return MachineSparePartService.create(MachineSparePart);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ERPMachineSparePart getMachineSparePartById(
			@PathVariable("id") Integer id) {

		return MachineSparePartService.get(id);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ERPMachineSparePart update(@PathVariable("id") Integer id,
			@RequestBody ERPMachineSparePart MachineSparePart) {

		MachineSparePart.setId(id);
		return MachineSparePartService.update(MachineSparePart);
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<ERPMachineSparePart> getAllMachineSpareParts() {

		return MachineSparePartService.getAll();

	}

	@RequestMapping(value = "/pagable", method = RequestMethod.GET)
	public Page<ERPMachineSparePart> getMachineSpareParts(ERPPageRequest pageRequest) {
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return MachineSparePartService.findAll(pageable);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") Integer id) {

		//MachineSparePartService.delete(id);
	}

}
