package com.cassinisys.erp.api.production;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.cassinisys.erp.converters.PageRequestConverter;
import com.cassinisys.erp.model.api.paging.ERPPageRequest;
import com.cassinisys.erp.model.production.*;
import com.cassinisys.erp.model.production.ERPProductionOrderItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.service.production.BOMService;

@RestController
@RequestMapping("production/boms")
public class BOMController extends BaseController {

	@Autowired
	BOMService bomService;

	@Autowired
	private PageRequestConverter pageRequestConverter;


	@RequestMapping(method = RequestMethod.POST)
	public ERPBom create(@RequestBody @Valid ERPBom bom,
						 HttpServletRequest request, HttpServletResponse response) {

		return bomService.createBOM(bom);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ERPBom getBOMById(@PathVariable("id") Integer id) {

		return bomService.getBOMById(id);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ERPBom update(@PathVariable("id") Integer id,
						 @RequestBody ERPBom bom) {
		bom.setBomId(id);
		return bomService.updateBOM(bom);
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<ERPBom> getAllBOMs() {

		return bomService.getAllBOMs();

	}

	@RequestMapping(value="/enums",method = RequestMethod.GET)
	public BomType[] getAllBOMEnums() {

		return BomType.values();

	}


	@RequestMapping(value = "/pagable", method = RequestMethod.GET)
	public Page<ERPBom> getBoms(ERPPageRequest pageRequest) {
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return bomService.findAll(pageable);

	}

	/*@RequestMapping(value = "/custom/{ids}", method = RequestMethod.GET)
	public ProductionItemsBomDTO getBoms(@PathVariable List<Integer> ids) {

		return bomService.getAllBomItems(ids);

	}
*/
	@RequestMapping(value = "/custom", method = RequestMethod.POST)
	public List<ERPProductionOrderItem> getPOItemBomDetails(@RequestBody @Valid List<ERPProductionOrderItem> orderItems) {

		return bomService.getAllProductionOrderBomItems(orderItems);

	}


	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") Integer id) {

		bomService.deleteBOM(id);
	}

}