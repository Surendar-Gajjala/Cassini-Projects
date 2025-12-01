package com.cassinisys.plm.controller.pqm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.CustomerSupplierCriteria;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.pqm.PQMCustomer;
import com.cassinisys.plm.model.pqm.PQMSupplier;
import com.cassinisys.plm.model.pqm.dto.ProblemReportsDto;
import com.cassinisys.plm.service.pqm.CustomerSupplierService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by subramanyam on 04-06-2020.
 */
@RestController
@RequestMapping("/pqm")
@Api(tags = "PLM.PQM",description = "Quality Related")
public class CustomerSupplierController extends BaseController {

    @Autowired
    private CustomerSupplierService customerSupplierService;
    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(value = "/customers", method = RequestMethod.POST)
    public PQMCustomer create(@RequestBody PQMCustomer inspection) {
        return customerSupplierService.create(inspection);
    }

    @RequestMapping(value = "/customers/{id}", method = RequestMethod.PUT)
    public PQMCustomer update(@PathVariable("id") Integer id,
                              @RequestBody PQMCustomer customer) {
        return customerSupplierService.update(customer);
    }

    @RequestMapping(value = "/customers/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        customerSupplierService.delete(id);
    }

    @RequestMapping(value = "/customers/{id}", method = RequestMethod.GET)
    public PQMCustomer get(@PathVariable("id") Integer id) {
        return customerSupplierService.get(id);
    }

    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    public List<PQMCustomer> getAll() {
        return customerSupplierService.getAll();
    }

    @RequestMapping(value = "/customers/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PQMCustomer> getMultiple(@PathVariable Integer[] ids) {
        return customerSupplierService.findMultiple(Arrays.asList(ids));
    }

    @RequestMapping(value = "/customers/all", method = RequestMethod.GET)
    public Page<PQMCustomer> getAllCustomers(PageRequest pageRequest, CustomerSupplierCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return customerSupplierService.getAllCustomers(pageable, criteria);
    }

    @RequestMapping(value = "/customers/{id}/count", method = RequestMethod.GET)
    public ItemDetailsDto getCustomerTabCounts(@PathVariable("id") Integer id) {
        return customerSupplierService.getCustomerTabCounts(id);
    }

    /*--------------------------------  Suppliers --------------------------------------------------*/

    @RequestMapping(value = "/suppliers", method = RequestMethod.POST)
    public PQMSupplier createSupplier(@RequestBody PQMSupplier supplier) {
        return customerSupplierService.createSupplier(supplier);
    }

    @RequestMapping(value = "/suppliers/{id}", method = RequestMethod.PUT)
    public PQMSupplier updateSupplier(@PathVariable("id") Integer id,
                                      @RequestBody PQMSupplier supplier) {
        return customerSupplierService.updateSupplier(supplier);
    }

    @RequestMapping(value = "/suppliers/{id}", method = RequestMethod.DELETE)
    public void deleteSupplier(@PathVariable("id") Integer id) {
        customerSupplierService.deleteSupplier(id);
    }

    @RequestMapping(value = "/suppliers/{id}", method = RequestMethod.GET)
    public PQMSupplier getSupplier(@PathVariable("id") Integer id) {
        return customerSupplierService.getSupplier(id);
    }

    @RequestMapping(value = "/suppliers", method = RequestMethod.GET)
    public List<PQMSupplier> getAllSuppliers() {
        return customerSupplierService.getSuppliers();
    }

    @RequestMapping(value = "/suppliers/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PQMSupplier> getMultipleSuppliers(@PathVariable Integer[] ids) {
        return customerSupplierService.findMultipleSuppliers(Arrays.asList(ids));
    }

    @RequestMapping(value = "/suppliers/all", method = RequestMethod.GET)
    public Page<PQMSupplier> getAllSuppliers(PageRequest pageRequest, CustomerSupplierCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return customerSupplierService.getAllSuppliers(pageable, criteria);
    }

    @RequestMapping(value = "/customers/{customer}/problemreports", method = RequestMethod.GET)
    public List<ProblemReportsDto> getAllCustomerProblemReports(@PathVariable("customer") Integer customer) {
        return customerSupplierService.getAllCustomerProblemReports(customer);
    }
}
