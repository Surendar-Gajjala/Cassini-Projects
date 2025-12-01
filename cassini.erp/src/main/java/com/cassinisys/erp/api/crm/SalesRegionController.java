package com.cassinisys.erp.api.crm;

import javax.validation.Valid;

import com.cassinisys.erp.service.security.PermissionCheck;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.converters.PageRequestConverter;
import com.cassinisys.erp.model.api.criteria.SalesRegionCriteria;
import com.cassinisys.erp.model.api.paging.ERPPageRequest;
import com.cassinisys.erp.model.crm.ERPSalesRegion;
import com.cassinisys.erp.service.crm.SalesRegionService;

/**
 * Created by reddy on 8/22/15.
 */
@RestController
@RequestMapping("crm/salesregions")
@Api(name = "SalesRegions", description = "SalesRegions endpoint", group = "CRM")
public class SalesRegionController extends BaseController {
	@Autowired
	private PageRequestConverter pageRequestConverter;

	@Autowired
	private SalesRegionService salesRegionService;

	@RequestMapping(method = RequestMethod.GET)
	public Page<ERPSalesRegion> getAll(SalesRegionCriteria criteria,
			ERPPageRequest pageRequest) {
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return salesRegionService.find(criteria, pageable);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ERPSalesRegion saveSalesRegion(
			@RequestBody @Valid ERPSalesRegion region) {
		return salesRegionService.update(region);
	}
	@RequestMapping(value = "/{regionId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("regionId") Integer regionId) {
 		salesRegionService.delete(regionId);
	}
}
