package com.cassinisys.plm.controller.mfr;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.model.mfr.PLMSupplierType;
import com.cassinisys.plm.model.mfr.PLMSupplierTypeAttribute;
import com.cassinisys.plm.service.classification.PLMSupplierTypeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plm/suppliertypes")
@Api(tags = "PLM.MES", description = "MES Related")
public class SupplierTypeController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;
    @Autowired
    private PLMSupplierTypeService supplierTypeService;


    @RequestMapping(method = RequestMethod.POST)
    public PLMSupplierType create(@RequestBody PLMSupplierType supplierType) {
        return supplierTypeService.create(supplierType);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PLMSupplierType update(@PathVariable("id") Integer id,
                                  @RequestBody PLMSupplierType supplierType) {
        return supplierTypeService.update(supplierType);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        supplierTypeService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PLMSupplierType get(@PathVariable("id") Integer id) {
        return supplierTypeService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PLMSupplierType> getAll() {
        return supplierTypeService.getRootTypes();
    }

    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public List<PLMSupplierType> getSupplierTypeTree() {
        return supplierTypeService.getClassificationTree();
    }

    /*
  * Get All   Hierarchical Attributes By Type
  * */
    @RequestMapping(value = "/type/{typeId}/attributes", method = RequestMethod.GET)
    public List<PLMSupplierTypeAttribute> getHierarchicalTypeAttributes(@PathVariable("typeId") Integer typeId,
                                                                        @RequestParam(value = "hierarchy", required = false) Boolean hierarchy) {
        if (hierarchy == null) {
            hierarchy = Boolean.FALSE;
        }
        return supplierTypeService.getAttributes(typeId, hierarchy);
    }

    @RequestMapping(value = "/{id}/count", method = RequestMethod.GET)
    public Integer getObjectsByType(@PathVariable("id") Integer id) {
        return supplierTypeService.getObjectsByType(id);
    }

}
