package com.cassinisys.is.api.pm;
/**
 * The Class is for CustomerController
 **/

import com.cassinisys.is.model.pm.ISCustomer;
import com.cassinisys.is.service.pm.CustomerService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Api(name = "Customers", description = "Customers endpoint")
@RestController
@RequestMapping("/customers")
public class CustomerController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;
    @Autowired
    private CustomerService customerService;

    /**
     * The method used for creating the ISCustomer
     **/
    @RequestMapping(method = RequestMethod.POST)
    public ISCustomer create(@RequestBody ISCustomer customer) {
        return customerService.create(customer);
    }

    /**
     * The method used for updating the ISCustomer
     **/
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ISCustomer update(@PathVariable("id") Integer id,
                             @RequestBody ISCustomer customer) {
        customer.setId(id);
        return customerService.update(customer);
    }

    /**
     * The method used for deleting the ISCustomer
     **/
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        customerService.delete(id);
    }

    /**
     * The method used to get the value of ISCustomer
     **/
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ISCustomer get(@PathVariable("id") Integer id) {
        return customerService.get(id);
    }

    /**
     * The method used to getall the values of ISCustomer
     **/
    @RequestMapping(method = RequestMethod.GET)
    public Page<ISCustomer> getAll(PageRequest page) {
        Pageable pageable = pageRequestConverter.convert(page);
        return customerService.findAll(pageable);
    }

    /**
     * The method used to getMultiples  of ISCustomer
     **/
    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<ISCustomer> getMultiple(@PathVariable Integer[] ids) {
        return customerService.findMultiple(Arrays.asList(ids));
    }

}
