package com.cassinisys.drdo.controller.procurement;

import com.cassinisys.drdo.filtering.ProcurementCriteria;
import com.cassinisys.drdo.model.DRDOUpdates;
import com.cassinisys.drdo.model.procurement.Manufacturer;
import com.cassinisys.drdo.model.procurement.Supplier;
import com.cassinisys.drdo.service.DRDOUpdatesService;
import com.cassinisys.drdo.service.procurement.ProcurementService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by subra on 10-12-2018.
 */
@RestController
@RequestMapping("drdo/procurement")
public class ProcurementController extends BaseController {

    @Autowired
    private ProcurementService procurementService;

    @Autowired
    private DRDOUpdatesService drdoUpdatesService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    /*--------------------------------------  Supplier Methods  -----------------------------------------------*/

    @RequestMapping(value = "/suppliers", method = RequestMethod.POST)
    public Supplier createSupplier(@RequestBody Supplier supplier) {
        return procurementService.createSupplier(supplier);
    }

    @RequestMapping(value = "/suppliers/{id}", method = RequestMethod.PUT)
    public Supplier updateSupplier(@PathVariable("id") Integer id,
                                   @RequestBody Supplier supplier) {
        return procurementService.updateSupplier(supplier);
    }

    @RequestMapping(value = "/suppliers/{id}", method = RequestMethod.DELETE)
    public void deleteSupplier(@PathVariable("id") Integer id) {
        procurementService.deleteSupplier(id);
    }

    @RequestMapping(value = "/suppliers/{id}", method = RequestMethod.GET)
    public Supplier getSupplier(@PathVariable("id") Integer id) {
        return procurementService.getSupplier(id);
    }

    @RequestMapping(value = "/suppliers", method = RequestMethod.GET)
    public List<Supplier> getSuppliers() {
        return procurementService.getSuppliers();
    }

    @RequestMapping(value = "/suppliers/all", method = RequestMethod.GET)
    public Page<Supplier> getAllSuppliers(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return procurementService.getAllSuppliers(pageable);
    }

    @RequestMapping(value = "/suppliers/filter", method = RequestMethod.GET)
    public Page<Supplier> getFilterSuppliers(PageRequest pageRequest, ProcurementCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return procurementService.gerFiltersuppliers(pageable, criteria);
    }

    @RequestMapping(value = "/suppliers/byCode/{code}", method = RequestMethod.GET)
    public Supplier getSupplierByCode(@PathVariable("code") String code) {
        return procurementService.getSupplierByCode(code);
    }

    /*--------------------------------------  Manufacturer Methods  -----------------------------------------------*/

    @RequestMapping(value = "/manufacturers", method = RequestMethod.POST)
    public Manufacturer createManufacturer(@RequestBody Manufacturer supplier) {
        return procurementService.createManufacturer(supplier);
    }

    @RequestMapping(value = "/manufacturers/{id}", method = RequestMethod.PUT)
    public Manufacturer updateSupplier(@PathVariable("id") Integer id,
                                       @RequestBody Manufacturer supplier) {
        return procurementService.updateManufacturer(supplier);
    }

    @RequestMapping(value = "/manufacturers/{id}", method = RequestMethod.DELETE)
    public void deleteManufacturer(@PathVariable("id") Integer id) {
        procurementService.deleteManufacturer(id);
    }

    @RequestMapping(value = "/manufacturers/{id}", method = RequestMethod.GET)
    public Manufacturer getManufacturer(@PathVariable("id") Integer id) {
        return procurementService.getManufacturer(id);
    }

    @RequestMapping(value = "/manufacturers", method = RequestMethod.GET)
    public List<Manufacturer> getManufacturers() {
        return procurementService.getManufacturers();
    }

    @RequestMapping(value = "/manufacturers/all", method = RequestMethod.GET)
    public Page<Manufacturer> getAllManufacturers(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return procurementService.getAllManufacturers(pageable);
    }

    @RequestMapping(value = "/manufacturers/filter", method = RequestMethod.GET)
    public Page<Manufacturer> getFilterManufacturers(PageRequest pageRequest, ProcurementCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return procurementService.gerFilterManufactures(pageable, criteria);
    }

    @RequestMapping(value = "/manufacturers/byCode/{code}", method = RequestMethod.GET)
    public Manufacturer getManufacturerByCode(@PathVariable("code") String code) {
        return procurementService.getManufacturerByCode(code);
    }

    @RequestMapping(value = "/updates/{personId}", method = RequestMethod.GET)
    public List<DRDOUpdates> getUpdatesByPerson(@PathVariable("personId") Integer personId) {
        return drdoUpdatesService.getUpdatesByPerson(personId);
    }

    @RequestMapping(value = "/updates/{updateId}", method = RequestMethod.PUT)
    public DRDOUpdates readMessage(@PathVariable("updateId") Integer updateId, @RequestBody DRDOUpdates drdoUpdates) {
        return drdoUpdatesService.readMessage(drdoUpdates);
    }

    @RequestMapping(value = "/updates/{personId}/delete", method = RequestMethod.DELETE)
    public void deleteUpdatesByPerson(@PathVariable("personId") Integer personId) {
        drdoUpdatesService.deleteUpdatesByPerson(personId);
    }

    @RequestMapping(value = "/updates/{personId}/update", method = RequestMethod.GET)
    public void updateMessageByPerson(@PathVariable("personId") Integer personId) {
        drdoUpdatesService.updateMessageByPerson(personId);
    }
}
