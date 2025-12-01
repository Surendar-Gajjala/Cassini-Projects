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
import com.cassinisys.erp.model.production.ERPProductType;
import com.cassinisys.erp.service.production.ProductTypeService;

@RestController
@RequestMapping("production/producttypes")
public class ProductTypeController extends BaseController {

	@Autowired
	ProductTypeService productTypeService;

	@RequestMapping(method = RequestMethod.POST)
	public ERPProductType create(@RequestBody @Valid ERPProductType productType,
			HttpServletRequest request, HttpServletResponse response) {

		return productTypeService.create(productType);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ERPProductType get(@PathVariable("id") Integer id) {

		return productTypeService.get(id);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ERPProductType update(@PathVariable("id") Integer id,
			@RequestBody ERPProductType productType) {
		productType.setId(id);
		return productTypeService.update(productType);
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<ERPProductType> getAll() {

		return productTypeService.getAll();

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") Integer id) {

		productTypeService.delete(id);
	}

}