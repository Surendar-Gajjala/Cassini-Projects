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
import com.cassinisys.erp.model.production.ERPProductDiscount;
import com.cassinisys.erp.service.production.ProductDiscountService;

@RestController
@RequestMapping("production/discounts")
@Api(name="ProductDiscounts",description="ProductDiscounts endpoint",group="PRODUCTION")
public class ProductDiscountController extends BaseController {

	@Autowired
	ProductDiscountService productDiscountService;

	@RequestMapping(method = RequestMethod.POST)
	public ERPProductDiscount createMaterialInv(@RequestBody @Valid ERPProductDiscount prodDiscount,
			HttpServletRequest request, HttpServletResponse response) {

		return productDiscountService.createProductDiscount(prodDiscount);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ERPProductDiscount getProductDiscountById(@PathVariable("id") Integer id) {

		return productDiscountService.getProductDiscountById(id);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ERPProductDiscount update(@PathVariable("id") Integer id,
			@RequestBody ERPProductDiscount productDiscount) {
		productDiscount.setRowId(id);
		return productDiscountService.updateProductDiscount(productDiscount);
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<ERPProductDiscount> getAllMaterialInventories() {

		return productDiscountService.getAllProductDiscounts();

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") Integer id) {

		productDiscountService.deleteProductDiscount(id);
	}

}