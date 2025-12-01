package com.cassinisys.is.api.pm;
/**
 * The Class is for CustomerTypeController
 **/

import com.cassinisys.is.model.pm.ISCustomerType;
import com.cassinisys.is.service.pm.CustomerTypeService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Api(name = "Customer types", description = "Customer types endpoint")
@RestController
@RequestMapping("/customertypes")
public class CustomerTypeController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;
    @Autowired
    private CustomerTypeService customerTypeService;

    /**
     * The method used for creating the ISCustomerType
     **/
    @RequestMapping(method = RequestMethod.POST)
    public ISCustomerType create(@RequestBody ISCustomerType customerType) {
        customerType.setId(null);
        return customerTypeService.create(customerType);
    }

    /**
     * The method used for updating the ISCustomerType
     **/
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ISCustomerType update(@PathVariable("id") Integer id,
                                 @RequestBody ISCustomerType customerType) {
        customerType.setId(id);
        return customerTypeService.update(customerType);
    }

    /**
     * The method used for deleting the ISCustomerType
     **/
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        customerTypeService.delete(id);
    }

    /**
     * The method used to get the values of ISCustomerType
     **/
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ISCustomerType get(@PathVariable("id") Integer id) {
        return customerTypeService.get(id);
    }

    /**
     * The method used to getall the values of ISCustomerType
     **/
    @RequestMapping(method = RequestMethod.GET)
    public Page<ISCustomerType> getAll(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return customerTypeService.findAll(pageable);
    }

}
