package com.cassinisys.is.api.procm;
/**
 * The Class is for SupplierController
 **/

import com.cassinisys.is.filtering.SupplierCriteria;
import com.cassinisys.is.model.procm.ISSupplier;
import com.cassinisys.is.model.procm.ISSupplierAddress;
import com.cassinisys.is.service.procm.SupplierService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.common.Address;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Api(name = "Supplier", description = "Supplier endpoint")
@RestController
@RequestMapping("/suppliers")
public class SupplierController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;
    @Autowired
    private SupplierService supplierService;

    /**
     * The method used for creating the ISSupplier
     **/
    @RequestMapping(method = RequestMethod.POST)
    public ISSupplier create(@RequestBody ISSupplier supplier) {
        return supplierService.create(supplier);
    }

    /**
     * The method used for updating the ISSupplier
     **/
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ISSupplier update(@PathVariable("id") Integer id,
                             @RequestBody ISSupplier supplier) {
        supplier.setId(id);
        return supplierService.update(supplier);
    }

    /**
     * The method used for deleting the ISSupplier
     **/
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        supplierService.delete(id);
    }

    /**
     * The method used to get value of the ISSupplier
     **/
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ISSupplier get(@PathVariable("id") Integer id) {
        return supplierService.get(id);
    }

    /**
     * The method used to getall the values of  ISSupplier
     **/
    @RequestMapping(method = RequestMethod.GET)
    public Page<ISSupplier> getAll(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return supplierService.getAll(pageable);
    }

    @RequestMapping(value = "/freesearch", method = RequestMethod.GET)
    public Page<ISSupplier> freeTextSearch(PageRequest pageRequest,
                                           SupplierCriteria criteria) {
        criteria.setFreeTextSearch(true);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<ISSupplier> suppliers = supplierService.freeTextSearch(pageable, criteria);
        return suppliers;
    }

    /**
     * The method used to createAddress for ISSupplierAddress
     **/
    @RequestMapping(value = "/{id}/addresses", method = RequestMethod.POST)
    public ISSupplierAddress createAddress(@PathVariable("id") Integer id,
                                           @RequestBody Address address) {
        return supplierService.createAddress(id, address);
    }

    /**
     * The method used to addAddress for ISSupplierAddress
     **/
    @RequestMapping(value = "/{id}/addresses/{addressId}",
            method = RequestMethod.PUT)
    public ISSupplierAddress addAddress(@PathVariable("id") Integer id,
                                        @PathVariable("addressId") Integer addressId) {
        return supplierService.addAddress(id, addressId);
    }

    /**
     * The method used to deleteAddress of ISSupplierAddress
     **/
    @RequestMapping(value = "/{id}/addresses/{addressId}",
            method = RequestMethod.DELETE)
    public void deleteAddress(@PathVariable("id") Integer id,
                              @PathVariable("addressId") Integer addressId) {
        supplierService.deleteAddress(id, addressId);
    }

    /**
     * The method used to getAddresses of Address
     **/
    @RequestMapping(value = "/{id}/addresses", method = RequestMethod.GET)
    public Page<Address> getAddresses(@PathVariable("id") Integer id,
                                      PageRequest page) {
        Pageable pageable = pageRequestConverter.convert(page);
        return supplierService.getAddresses(id, pageable);
    }

    /**
     * The method used to getMultiples from the List of ISSupplier
     **/
    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<ISSupplier> getMultiple(@PathVariable Integer[] ids) {
        return supplierService.findMultiple(Arrays.asList(ids));
    }

}
