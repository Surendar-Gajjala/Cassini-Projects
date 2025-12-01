package com.cassinisys.erp.api.production;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.cassinisys.erp.model.production.BomItemType;
import com.cassinisys.erp.model.production.BomType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.model.production.ERPBomItem;
import com.cassinisys.erp.service.production.BOMItemService;

@RestController
@RequestMapping("production/bomitems")
public class BOMItemController extends BaseController {

	@Autowired
	BOMItemService bomItemService;

	@RequestMapping(method = RequestMethod.POST)
	public ERPBomItem create(@RequestBody @Valid ERPBomItem bomItem,
			HttpServletRequest request, HttpServletResponse response) {

		return bomItemService.createBOMItem(bomItem);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ERPBomItem getBOMItemById(@PathVariable("id") Integer id) {

		return bomItemService.getBOMItemById(id);

	}

	@RequestMapping(value="/enums",method = RequestMethod.GET)
	public BomItemType[] getAllBOMItemEnums() {

		return BomItemType.values();

	}

		@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ERPBomItem update(@PathVariable("id") Integer id,
			@RequestBody ERPBomItem bomItem) {
		bomItem.setRowId(id);
		return bomItemService.updateBOMItem(bomItem);
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<ERPBomItem> getAllBOMItems() {

		return bomItemService.getAllBOMItems();

	}

	@RequestMapping(value = "/bom/{bomId}",method = RequestMethod.GET)
	public List<ERPBomItem> getAllBOMItems(@PathVariable("bomId") Integer bomId) {

		return bomItemService.getAllBOMItemsByBom(bomId);

	}



	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") Integer id) {

		bomItemService.deleteBOMItem(id);
	}

}