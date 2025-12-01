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
import com.cassinisys.erp.model.production.ERPSparePartsInventory;
import com.cassinisys.erp.service.production.SparePartInventoryService;

@RestController
@RequestMapping("production/sparepartinventory")
@Api(name="work",description="work endpoint",group="PRODUCTION")
public class SparePartInventoryController  extends BaseController {
	
	@Autowired
	private SparePartInventoryService sparePartInventoryService;
	@Autowired
	private PageRequestConverter pageRequestConverter;
	
	
	@RequestMapping(method = RequestMethod.POST)
	public ERPSparePartsInventory createSparePartInventory(@RequestBody @Valid ERPSparePartsInventory SparePartInventory,
			HttpServletRequest request, HttpServletResponse response) {

		return sparePartInventoryService.create(SparePartInventory);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ERPSparePartsInventory getSparePartInventoryById(
			@PathVariable("id") Integer id) {

		return sparePartInventoryService.get(id);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ERPSparePartsInventory update(@PathVariable("id") Integer id,
			@RequestBody ERPSparePartsInventory sparePartInventory) {

		sparePartInventory.setPartId(id);
		return sparePartInventoryService.update(sparePartInventory);
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<ERPSparePartsInventory> getAllSparePartInventorys() {

		return sparePartInventoryService.getAll();

	}

	@RequestMapping(value = "/pagable", method = RequestMethod.GET)
	public Page<ERPSparePartsInventory> getSparePartInventorys(ERPPageRequest pageRequest) {
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return sparePartInventoryService.findAll(pageable);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") Integer id) {

		//SparePartInventoryService.delete(id);
	}

}
