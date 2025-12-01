package com.cassinisys.erp.api.production;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.model.production.ERPMaterialCategory;
import com.cassinisys.erp.service.production.MaterialCategoryService;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("production/materialcategories")
@Api(name = "MaterialCategories", description = "MaterialCategories endpoint", group = "PRODUCTION")
public class MaterialCategoryController extends BaseController {

    @Autowired
    MaterialCategoryService materialCategoryService;

    @RequestMapping(method = RequestMethod.POST)
    public ERPMaterialCategory create(
            @RequestBody @Valid ERPMaterialCategory materialCategory,
            HttpServletRequest request, HttpServletResponse response) {

        return materialCategoryService.createMaterialCategory(materialCategory);

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ERPMaterialCategory getMaterialCategoryById(
            @PathVariable("id") Integer id) {

        return materialCategoryService.getMaterialCategory(id);

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ERPMaterialCategory update(@PathVariable("id") Integer id,
                                      @RequestBody ERPMaterialCategory materialCategory) {
        materialCategory.setId(id);
        return materialCategoryService.updateMaterialCategory(materialCategory);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ERPMaterialCategory> getCategoryTree() {
        return materialCategoryService.getCategoryTree();
    }

    @RequestMapping(value = "/{id}/children", method = RequestMethod.GET)
    public List<ERPMaterialCategory> getChildCategories(@PathVariable("id") Integer id) {
        return materialCategoryService.getChildren(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteMaterialCategory(@PathVariable("id") Integer id) {

        materialCategoryService.deleteMaterialCategory(id);
    }

}