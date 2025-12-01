package com.cassinisys.is.service.procm;

import com.cassinisys.is.filtering.SupplierCriteria;
import com.cassinisys.is.filtering.SupplierPredicateBuilder;
import com.cassinisys.is.model.procm.ISSupplier;
import com.cassinisys.is.model.procm.ISSupplierAddress;
import com.cassinisys.is.model.procm.QISSupplier;
import com.cassinisys.is.repo.procm.SupplierAddressRepository;
import com.cassinisys.is.repo.procm.SupplierRepository;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.common.Address;
import com.cassinisys.platform.repo.common.AddressRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The class is for  SupplierService
 **/
@Service
public class SupplierService implements CrudService<ISSupplier, Integer> {

    @Autowired
    private SessionWrapper sessionWrapper;

    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private SupplierAddressRepository supplierAddressRepository;
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private SupplierPredicateBuilder supplierPredicateBuilder;

    @Override
    @Transactional(readOnly = true)
    public List<ISSupplier> getAll() {
        return supplierRepository.findAll();
    }

    /**
     * The Method is used to create ISSupplier
     **/
    @Override
    @Transactional(readOnly = false)
    public ISSupplier create(ISSupplier supplier) {
        checkNotNull(supplier);
        supplier.setId(null);
        supplier = supplierRepository.save(supplier);
        return supplier;
    }

    /**
     * The Method is used to update ISSupplier
     **/
    @Override
    @Transactional(readOnly = false)
    public ISSupplier update(ISSupplier supplier) {
        checkNotNull(supplier);
        checkNotNull(supplier.getId());
        supplier = supplierRepository.save(supplier);
        return supplier;
    }

    /**
     * The Method is used to delete  supplier
     **/
    @Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        checkNotNull(id);
        ISSupplier supplier = supplierRepository.findOne(id);
        if (supplier == null) {
            throw new ResourceNotFoundException();
        }
        supplierRepository.delete(supplier);

    }

    /**
     * The Method is used to get ISSupplier
     **/
    @Transactional(readOnly = true)
    @Override
    public ISSupplier get(Integer id) {
        checkNotNull(id);
        ISSupplier supplier = supplierRepository.findOne(id);
        if (supplier == null) {
            throw new ResourceNotFoundException();
        }
        return supplier;
    }

    /**
     * The Method is used to getAll the list of ISSupplier
     **/
    @Transactional(readOnly = true)
    public Page<ISSupplier> getAll(Pageable pageable) {
        return supplierRepository.findAll(pageable);
    }

    /**
     * The Method is used to getAddresses for the page of Address
     **/
    @Transactional(readOnly = true)
    public Page<Address> getAddresses(Integer supplierId, Pageable pageable) {
        checkNotNull(supplierId);
        checkNotNull(pageable);
        if (supplierRepository.findOne(supplierId) == null) {
            throw new ResourceNotFoundException();
        }
        Page<Integer> addressesIdPage = supplierAddressRepository
                .findBySupplierId(supplierId, pageable);
        List<Address> addresses = addressRepository.findAll(addressesIdPage
                .getContent());
        return new PageImpl<Address>(addresses, pageable,
                addressesIdPage.getTotalElements());
    }

    /**
     * The Method is used to createAddress for ISSupplierAddress
     **/
    @Transactional(readOnly = false)
    public ISSupplierAddress createAddress(Integer supplierId, Address address) {
        checkNotNull(supplierId);
        checkNotNull(address);
        ISSupplier supplier = supplierRepository.findOne(supplierId);
        if (supplier == null) {
            throw new ResourceNotFoundException();
        }
        address.setId(null);
        address = addressRepository.save(address);
        ISSupplierAddress supplierAddress = new ISSupplierAddress(supplierId,
                address.getId());
        supplierAddress = supplierAddressRepository.save(supplierAddress);
        return supplierAddress;
    }

    /**
     * The Method is used to addAddress for ISSupplierAddress
     **/
    public ISSupplierAddress addAddress(Integer supplierId, Integer addressId) {
        checkNotNull(supplierId);
        checkNotNull(addressId);
        ISSupplier supplier = supplierRepository.findOne(supplierId);
        if (supplier == null) {
            throw new ResourceNotFoundException();
        }
        Address address = addressRepository.findOne(addressId);
        if (address == null) {
            throw new ResourceNotFoundException();
        }
        ISSupplierAddress supplierAddress = new ISSupplierAddress(supplierId,
                addressId);
        supplierAddress = supplierAddressRepository.save(supplierAddress);
        return supplierAddress;
    }

    /**
     * The Method is used to deleteAddress of ISSupplierAddress
     **/
    @Transactional(readOnly = false)
    public void deleteAddress(Integer supplierId, Integer addressId) {
        checkNotNull(supplierId);
        checkNotNull(addressId);
        ISSupplier supplier = supplierRepository.findOne(supplierId);
        if (supplier == null) {
            throw new ResourceNotFoundException();
        }
        supplierAddressRepository.delete(new ISSupplierAddress(supplierId,
                addressId));

    }

    /**
     * The Method is used to findMultiple for the list of ISSupplier
     **/
    @Transactional(readOnly = true)
    public List<ISSupplier> findMultiple(List<Integer> ids) {
        return supplierRepository.findByIdIn(ids);
    }

    /**
     * The method used for the freeTextSearch for the page of  ISProjectSite
     **/
    @Transactional(readOnly = true)
    public Page<ISSupplier> freeTextSearch(Pageable pageable, SupplierCriteria supplierCriteria) {
        Predicate predicate = supplierPredicateBuilder.build(supplierCriteria, QISSupplier.iSSupplier);
        return supplierRepository.findAll(predicate, pageable);
    }

}
