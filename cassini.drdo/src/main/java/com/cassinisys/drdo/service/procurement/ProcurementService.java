package com.cassinisys.drdo.service.procurement;

import com.cassinisys.drdo.filtering.ManufacturerPredicateBuilder;
import com.cassinisys.drdo.filtering.ProcurementCriteria;
import com.cassinisys.drdo.filtering.SupplierPredicateBuilder;
import com.cassinisys.drdo.model.procurement.Manufacturer;
import com.cassinisys.drdo.model.procurement.QManufacturer;
import com.cassinisys.drdo.model.procurement.QSupplier;
import com.cassinisys.drdo.model.procurement.Supplier;
import com.cassinisys.drdo.repo.DRDOUpdateRepository;
import com.cassinisys.drdo.repo.procurement.ManufacturerRepository;
import com.cassinisys.drdo.repo.procurement.SupplierRepository;
import com.cassinisys.platform.model.common.Address;
import com.cassinisys.platform.repo.common.AddressRepository;
import com.cassinisys.platform.repo.common.StateRepository;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by subra on 10-12-2018.
 */
@Service
public class ProcurementService {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private DRDOUpdateRepository drdoUpdateRepository;

    @Autowired
    private SupplierPredicateBuilder supplierPredicateBuilder;
    @Autowired
    private ManufacturerPredicateBuilder manufacturerPredicateBuilder;

    /*--------------------------------------  Supplier Methods  -----------------------------------------------*/

    @Transactional(readOnly = false)
    public Supplier createSupplier(Supplier supplier) {

        Address address = addressRepository.save(supplier.getAddress());
        supplier.setAddress(address);
        return supplierRepository.save(supplier);
    }

    @Transactional(readOnly = false)
    public Supplier updateSupplier(Supplier supplier) {

        Address address = addressRepository.save(supplier.getAddress());
        supplier.setAddress(address);
        return supplierRepository.save(supplier);
    }

    @Transactional(readOnly = false)
    public void deleteSupplier(Integer supplierId) {
        supplierRepository.delete(supplierId);
    }

    @Transactional(readOnly = true)
    public Supplier getSupplier(Integer supplierId) {
        return supplierRepository.findOne(supplierId);
    }


    @Transactional(readOnly = true)
    public List<Supplier> getSuppliers() {
        return supplierRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Supplier> getAllSuppliers(Pageable pageable) {
        Page<Supplier> suppliers = supplierRepository.findAll(pageable);
        suppliers.getContent().forEach(supplier -> {
            supplier.setState(stateRepository.findOne(supplier.getAddress().getState()));
        });
        return suppliers;
    }

    @Transactional(readOnly = true)
    public Page<Supplier> gerFiltersuppliers(Pageable pageable, ProcurementCriteria criteria) {
        if (criteria.getSearchQuery() != null) {
            criteria.setFreeTextSearch(true);
        }
        Predicate predicate = supplierPredicateBuilder.build(criteria, QSupplier.supplier);

        Page<Supplier> supplierPage = supplierRepository.findAll(predicate, pageable);
        return supplierPage;
    }

    @Transactional(readOnly = true)
    public Supplier getSupplierByCode(String code) {
        return supplierRepository.findBySupplierCodeIgnoreCase(code);
    }

    /*--------------------------------------  Manufacturer Methods  -----------------------------------------------*/

    @Transactional(readOnly = false)
    public Manufacturer createManufacturer(Manufacturer manufacturer) {
        return manufacturerRepository.save(manufacturer);
    }

    @Transactional(readOnly = false)
    public Manufacturer updateManufacturer(Manufacturer manufacturer) {
        return manufacturerRepository.save(manufacturer);
    }

    @Transactional(readOnly = false)
    public void deleteManufacturer(Integer manufacturerId) {
        manufacturerRepository.delete(manufacturerId);
    }

    @Transactional(readOnly = true)
    public Manufacturer getManufacturer(Integer manufacturerId) {
        return manufacturerRepository.findOne(manufacturerId);
    }


    @Transactional(readOnly = true)
    public List<Manufacturer> getManufacturers() {
        return manufacturerRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Manufacturer> getAllManufacturers(Pageable pageable) {
        return manufacturerRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Manufacturer> gerFilterManufactures(Pageable pageable, ProcurementCriteria criteria) {
        if (criteria.getSearchQuery() != null) {
            criteria.setFreeTextSearch(true);
        }
        Predicate predicate = manufacturerPredicateBuilder.build(criteria, QManufacturer.manufacturer);

        Page<Manufacturer> manufacturerPage = manufacturerRepository.findAll(predicate, pageable);
        return manufacturerPage;
    }

    @Transactional(readOnly = true)
    public Manufacturer getManufacturerByCode(String code) {
        return manufacturerRepository.findByMfrCodeIgnoreCase(code);
    }
}

