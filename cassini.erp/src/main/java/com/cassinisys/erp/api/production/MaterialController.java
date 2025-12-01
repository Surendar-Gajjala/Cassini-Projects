package com.cassinisys.erp.api.production;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.converters.PageRequestConverter;
import com.cassinisys.erp.model.api.criteria.MaterialCriteria;
import com.cassinisys.erp.model.api.criteria.ProductCriteria;
import com.cassinisys.erp.model.api.paging.ERPPageRequest;
import com.cassinisys.erp.model.production.ERPMaterial;
import com.cassinisys.erp.model.production.ERPMaterialSupplier;
import com.cassinisys.erp.model.production.ERPProduct;
import com.cassinisys.erp.service.production.MaterialService;
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
@RequestMapping("production/materials")
@Api(name = "Materials", description = "Materials endpoint", group = "PRODUCTION")
public class MaterialController extends BaseController {

    private MaterialService materialService;

    private PageRequestConverter pageRequestConverter;

    @Autowired
    public MaterialController(MaterialService materialService,
                              PageRequestConverter pageRequestConverter) {
        this.pageRequestConverter = pageRequestConverter;
        this.materialService = materialService;
    }

    @RequestMapping(value = "/category/{id}", method = RequestMethod.GET)
    public
    
    Page<ERPMaterial> getMaterialsByCategory(@PathVariable("id") Integer id, ERPPageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return materialService.getByCategory(id, pageable);
    }


    @RequestMapping(method = RequestMethod.POST)
    public ERPMaterial createMaterial(@RequestBody @Valid ERPMaterial material,
                                      HttpServletRequest request, HttpServletResponse response) {
        return materialService.create(material);
    }

    @RequestMapping(value = "/materialSuppliers", method = RequestMethod.POST)
    public List<ERPMaterialSupplier> createMaterialSuppliers(@RequestBody List<ERPMaterialSupplier> materialSuppliers,
                                                             HttpServletRequest request, HttpServletResponse response) {
        return materialService.saveMaterialSuppliers(materialSuppliers);

    }

    @RequestMapping(value = "/{materialId}", method = RequestMethod.GET)
    public ERPMaterial getMaterialById(
            @PathVariable("materialId") Integer materialId) {

        return materialService.get(materialId);
    }

    @RequestMapping(
            value = {"/multiple/[{ids}]"},
            method = {RequestMethod.GET}
    )
    public List<ERPMaterial> getMultiple(@PathVariable Integer[] ids) {
        return materialService.findMultiple(Arrays.asList(ids));
    }

    @RequestMapping(value = "/{materialId}", method = RequestMethod.PUT)
    public ERPMaterial update(@PathVariable("materialId") Integer materialId,
                              @RequestBody ERPMaterial material) {

        material.setId(materialId);
        return materialService.update(material);
    }

    @RequestMapping(value = "/materialSuppliers/byMaterial/{materialId}", method = RequestMethod.GET)
    public List<ERPMaterialSupplier> getMaterialSuppliersByMaterialId(@PathVariable("materialId") Integer materialId) {
        return materialService.getMaterialSuppliersByMaterialId(materialId);
    }

    @RequestMapping(value = "/materialSuppliers/bySupplier/{supplierId}", method = RequestMethod.GET)
    public List<ERPMaterialSupplier> getMaterialSuppliersBySupplierId(@PathVariable("supplierId") Integer supplierId) {
        return materialService.getMaterialSuppliersBySupplierId(supplierId);
    }

    @RequestMapping(value = "/materialSuppliers/{materialId}/{supplierId}", method = RequestMethod.DELETE)
    public void deleterMaterialSupplier(@PathVariable("materialId")Integer materialId, @PathVariable("supplierId")Integer supplierId) {
         materialService.deleterMaterialSupplier(materialId, supplierId);
    }

    @RequestMapping(value = "/pagable", method = RequestMethod.GET)
    public Page<ERPMaterial> getAllMaterials(ERPPageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return materialService.findAll(pageable);
    }

    @RequestMapping(value = "/{materialId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("materialId") Integer materialId) {

        materialService.delete(materialId);
    }


    @RequestMapping(value = "/sku/{sku}", method = RequestMethod.GET)
    public
    
    ERPMaterial getMaterialBySku(@PathVariable("sku") String sku) {
        return materialService.getBySku(sku);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public
    
    Page<ERPMaterial> textSearch(MaterialCriteria criteria, ERPPageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return materialService.findAll(criteria, pageable);
    }

    @RequestMapping(method = RequestMethod.GET)
    public
    
    Page<ERPMaterial> getMaterials(MaterialCriteria criteria, ERPPageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return materialService.findAll(criteria, pageable);
    }
}
