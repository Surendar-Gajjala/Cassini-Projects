package com.cassinisys.erp.api.production;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.model.production.ERPMaterialPriceChangeHistory;
import com.cassinisys.erp.service.production.MaterialPriceChangeHistoryService;

@RestController
@RequestMapping("production/materialpricechghistory")
@Api(name = "MaterialPriceChangeHistory", description = "MaterialPriceChangeHistory endpoint", group = "PRODUCTION")
public class MaterialPriceChangeHistoryController extends BaseController {

	@Autowired
	MaterialPriceChangeHistoryService materialPriceChgHistoryService;

	@RequestMapping(method = RequestMethod.POST)
	public ERPMaterialPriceChangeHistory createMaterialPriceChgHistory(
			@RequestBody @Valid ERPMaterialPriceChangeHistory materialPriceChgHistory,
			HttpServletRequest request, HttpServletResponse response) {

		return materialPriceChgHistoryService
				.createMaterialPriceChgHistory(materialPriceChgHistory);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ERPMaterialPriceChangeHistory getMaterialPriceChgHistoryById(
			@PathVariable("id") Integer id) {

		return materialPriceChgHistoryService.getMaterialPriceChangeHistory(id);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ERPMaterialPriceChangeHistory update(@PathVariable("id") Integer id,
			@RequestBody ERPMaterialPriceChangeHistory materialPriceChgHistory) {
		materialPriceChgHistory.setRowId(id);
		return materialPriceChgHistoryService
				.updateMaterialPriceChgHistory(materialPriceChgHistory);
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<ERPMaterialPriceChangeHistory> getAllMaterialPriceChgHistory() {

		return materialPriceChgHistoryService
				.getAllMaterialPriceChangeHistorys();

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteMaterialPriceChgHistory(@PathVariable("id") Integer id) {

		materialPriceChgHistoryService.deleteMaterialPriceChangeHistory(id);
	}

}