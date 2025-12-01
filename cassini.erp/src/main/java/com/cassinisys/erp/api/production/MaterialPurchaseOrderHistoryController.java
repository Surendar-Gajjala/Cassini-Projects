package com.cassinisys.erp.api.production;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.model.production.ERPMaterialPurchaseOrderHistory;
import com.cassinisys.erp.service.production.MaterialPurchaseOrderHistoryService;

@RestController
@RequestMapping("production/materialpurchaseorderhistory")
public class MaterialPurchaseOrderHistoryController extends BaseController {

	@Autowired
	MaterialPurchaseOrderHistoryService mPurchaseOrderHistoryService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ERPMaterialPurchaseOrderHistory getMaterialPurchaseOrderHistoryById(
			@PathVariable("id") Integer id) {

		return mPurchaseOrderHistoryService.getMaterialPurchaseOrderHistory(id);

	}

	@RequestMapping(method = RequestMethod.GET)
	public List<ERPMaterialPurchaseOrderHistory> getAllMaterialPurchaseOrderHistorys() {

		return mPurchaseOrderHistoryService
				.getAllMaterialPurchaseOrderHistorys();

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") Integer id) {

		mPurchaseOrderHistoryService.delete(id);
	}

}