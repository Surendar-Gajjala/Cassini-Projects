package com.cassinisys.erp.api.production;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.converters.PageRequestConverter;
import com.cassinisys.erp.model.api.paging.ERPPageRequest;
import com.cassinisys.erp.model.production.ERPMaterial;
import com.cassinisys.erp.model.production.ERPSupplier;
import com.cassinisys.erp.service.production.SupplierService;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("production/suppliers")
@Api(name = "Suppliers", description = "Suppliers endpoint", group = "PRODUCTION")
public class SupplierController extends BaseController {

	@Autowired
	SupplierService supplierService;

	@Autowired
	private PageRequestConverter pageRequestConverter;

	@RequestMapping(method = RequestMethod.POST)
	public ERPSupplier createMaterialSupplier(
			@RequestBody @Valid ERPSupplier materialSupplier,
			HttpServletRequest request, HttpServletResponse response) {

		return supplierService.create(materialSupplier);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ERPSupplier getMaterialSupplierById(
			@PathVariable("id") Integer id) {

		return supplierService.get(id);

	}

	@RequestMapping(
			value = {"/multiple/[{ids}]"},
			method = {RequestMethod.GET}
	)
	public List<ERPSupplier> getMultiple(@PathVariable Integer[] ids) {
		return supplierService.findMultiple(Arrays.asList(ids));
	}

	@RequestMapping(value = "/{name}/{officePhone}", method = RequestMethod.GET)
	public List<ERPSupplier> getMaterialSupplierByNameAndPhone(
			@PathVariable("name") String name, @PathVariable("officePhone") String officePhone) {

		return supplierService.getByNameAndOfficePhone(name, officePhone);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ERPSupplier update(@PathVariable("id") Integer id,
			@RequestBody ERPSupplier materialSupplier) {
		materialSupplier.setId(id);
		return supplierService.update(materialSupplier);
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<ERPSupplier> getAllMaterialSuppliers() {

		return supplierService.getAll();

	}

	@RequestMapping(value="/pagable",method = RequestMethod.GET)
	public Page<ERPSupplier> getAllMaterialSuppliers(ERPPageRequest pageRequest) {
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return supplierService.findAll(pageable);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") Integer id) {

		supplierService.delete(id);
	}

}