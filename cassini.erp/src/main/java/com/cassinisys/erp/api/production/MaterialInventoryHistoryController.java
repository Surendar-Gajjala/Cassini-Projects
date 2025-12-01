package com.cassinisys.erp.api.production;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.model.production.ERPMaterialInventoryHistory;
import com.cassinisys.erp.service.production.MaterialInventoryHistoryService;

@RestController
@RequestMapping("production/materialinvhistory")
public class MaterialInventoryHistoryController extends BaseController {

	@Autowired
	MaterialInventoryHistoryService materialInvHistoryService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ERPMaterialInventoryHistory getMaterialInvHistoryById(
			@PathVariable("id") Integer id) {

		return materialInvHistoryService.getMaterialInvHistory(id);

	}

	@RequestMapping(method = RequestMethod.GET)
	public List<ERPMaterialInventoryHistory> getAllMaterialInvHistorys() {

		return materialInvHistoryService.getAllMaterialInvHistorys();

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") Integer id) {

		materialInvHistoryService.delete(id);
	}

}