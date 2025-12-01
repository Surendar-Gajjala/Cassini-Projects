package com.cassinisys.erp.api.production;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.model.production.ERPProductCategory;
import com.cassinisys.erp.service.production.ProductCategoryService;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Created by reddy on 8/4/15.
 */
@RestController
@RequestMapping("production/productcategories")
@Api(name="ProductCategories",description="ProductCategories endpoint",group="PRODUCTION")
public class ProductCategoryController extends BaseController {
    @Autowired
    private ProductCategoryService productCategoryService;

    @RequestMapping (method = RequestMethod.GET)
    public 
    List<ERPProductCategory> getCategoryTree() {
        return productCategoryService.getCategoryTree();
    }

    @RequestMapping(value ="/{id}", method = RequestMethod.GET)
    public 
    ERPProductCategory getCategory(@PathVariable("id") Integer id) {
        return productCategoryService.get(id);
    }

    @RequestMapping(value ="/{id}/children", method = RequestMethod.GET)
    public 
    List<ERPProductCategory> getChildCategories(@PathVariable("id") Integer id) {
        return productCategoryService.getChildren(id);
    }
    
    
    @RequestMapping(method = RequestMethod.POST)
	public ERPProductCategory create(@RequestBody @Valid ERPProductCategory prodCateory,
			HttpServletRequest request, HttpServletResponse response) {

		return productCategoryService.create(prodCateory);

	}
}
