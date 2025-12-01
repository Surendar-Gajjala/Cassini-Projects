package com.cassinisys.erp.api.production;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.model.production.ERPProductBom;
import com.cassinisys.erp.service.production.ProductBOMService;

@RestController
@RequestMapping("production/productboms")
public class ProductBOMController extends BaseController {

	@Autowired
	ProductBOMService productBOMService;

	@RequestMapping(method = RequestMethod.POST)
	public ERPProductBom createMaterialInv(@RequestBody @Valid ERPProductBom prodBOM,
			HttpServletRequest request, HttpServletResponse response) {

		return productBOMService.createProductBOM(prodBOM);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ERPProductBom getProductBOMById(@PathVariable("id") Integer id) {

		return productBOMService.getProductBOMById(id);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ERPProductBom update(@PathVariable("id") Integer id,
			@RequestBody ERPProductBom productBOM) {
		productBOM.setRowId(id);
		return productBOMService.updateProductBOM(productBOM);
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<ERPProductBom> getAllProductBOMs() {

		return productBOMService.getAllProductBOMs();

	}
	
	@RequestMapping(value = "/product/{id}", method = RequestMethod.GET)
	public List<ERPProductBom> findByProduct(@PathVariable("id") Integer id) {

		return productBOMService.findByProduct(id);

	}
	
	@RequestMapping(value = "/material/{id}", method = RequestMethod.GET)
	public List<ERPProductBom> findByMaterial(@PathVariable("id") Integer id) {

		return productBOMService.findByMaterial(id);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") Integer id) {

		productBOMService.deleteProductBOM(id);
	}

}