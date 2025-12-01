package com.cassinisys.erp.api.common;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.model.hrm.ERPBusinessUnit;
import com.cassinisys.erp.service.common.BusinessUnitService;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by reddy on 18/01/16.
 */
@RestController
@RequestMapping("common/businessunits")
@Api(name="Business Units",description="Business Units endpoint",group="COMMON")
public class BusinessUnitController extends BaseController {

    @Autowired
    private BusinessUnitService buService;

    @RequestMapping(method = RequestMethod.GET)
    public List<ERPBusinessUnit> getAll() {
        return buService.getAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ERPBusinessUnit create(@RequestBody ERPBusinessUnit bu) {
        return buService.create(bu);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ERPBusinessUnit get(@PathVariable("id") Integer id) {
        return buService.get(id);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ERPBusinessUnit update(@PathVariable("id") Integer id,
                                  @RequestBody ERPBusinessUnit bu) {
        return buService.update(bu);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        buService.delete(id);
    }
}
