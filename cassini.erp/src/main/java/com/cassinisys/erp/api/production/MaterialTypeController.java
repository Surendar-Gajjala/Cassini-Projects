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
import com.cassinisys.erp.model.production.ERPMaterialType;
import com.cassinisys.erp.service.production.MaterialTypeService;

@RestController
@RequestMapping("production/materialtypes")
@Api(name = "MaterialTypes", description = "MaterialTypes endpoint", group = "PRODUCTION")
public class MaterialTypeController extends BaseController {

	@Autowired
	MaterialTypeService materialTypeService;

	@RequestMapping(method = RequestMethod.POST)
	public ERPMaterialType createMaterialType(
			@RequestBody @Valid ERPMaterialType materialType,
			HttpServletRequest request, HttpServletResponse response) {

		return materialTypeService.createMaterialType(materialType);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ERPMaterialType getMaterialTypeById(@PathVariable("id") Integer id) {

		return materialTypeService.getMaterialType(id);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ERPMaterialType update(@PathVariable("id") Integer id,
			@RequestBody ERPMaterialType materialType) {
		materialType.setId(id);
		return materialTypeService.updateMaterialType(materialType);
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<ERPMaterialType> getAllMaterialTypes() {

		return materialTypeService.getAllMaterialTypes();

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteMaterialType(@PathVariable("id") Integer id) {

		materialTypeService.deleteMaterialType(id);
	}

}