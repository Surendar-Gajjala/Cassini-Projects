package com.cassinisys.erp.service.crm;

import com.cassinisys.erp.api.exceptions.ERPException;
import com.cassinisys.erp.api.exceptions.ErrorCodes;
import com.cassinisys.erp.api.filtering.CustomerPredicateBuilder;
import com.cassinisys.erp.model.api.criteria.CustomerCriteria;
import com.cassinisys.erp.model.crm.*;
import com.cassinisys.erp.repo.common.ObjectGeoLocationRepository;
import com.cassinisys.erp.repo.crm.CustomerRepository;
import com.cassinisys.erp.repo.crm.CustomerTypeRepository;
import com.cassinisys.erp.repo.crm.SalesRegionRepository;
import com.cassinisys.erp.repo.crm.SalesRepRepository;
import com.cassinisys.erp.service.paging.CrudService;
import com.cassinisys.erp.service.paging.PageableService;
import com.cassinisys.erp.service.security.SessionWrapper;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by reddy on 7/18/15.
 */
@Service
@Transactional
public class CustomerService implements CrudService<ERPCustomer, Integer>,
        PageableService<ERPCustomer, Integer> {

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    CustomerTypeRepository customerTypeRepository;
    @Autowired
    SalesRegionRepository salesRegionRepository;
    @Autowired
    SalesRepRepository salesRepRepository;
    @Autowired
    SessionWrapper sessionWrapper;
    @Autowired
    CustomerPredicateBuilder predicateBuilder;
    @Autowired
    private ObjectGeoLocationRepository objectGeoLocationRepository;

    @Override
    public ERPCustomer create(ERPCustomer customer) {
        if (customer.getSalesRep() != null) {
            ERPSalesRep rep = salesRepRepository.findOne(customer.getSalesRep().getId());
            customer.setSalesRep(rep);
        }
        if (customer.getSalesRegion() != null) {
            ERPSalesRegion region = salesRegionRepository.findOne(customer.getSalesRegion().getId());
            customer.setSalesRegion(region);
        }
        return customerRepository.save(customer);
    }

    @Override
    public ERPCustomer get(Integer custId) {
        return customerRepository.findOne(custId);
    }


    @Override
    public ERPCustomer update(ERPCustomer customer) {
        checkNotNull(customer);
        checkNotNull(customer.getId());
        if (customerRepository.findOne(customer.getId()) == null) {
            throw new ERPException(HttpStatus.NOT_FOUND,
                    ErrorCodes.RESOURCE_NOT_FOUND);
        }
        return customerRepository.save(customer);
    }

    public Boolean getCustomerByNameAndRegion(String name, String salesRegion) {
        Boolean valid = true;
        List<ERPCustomer> erpCustomers = customerRepository.findByNameAndRegion(name, salesRegion);
        if (erpCustomers.size() == 0 || erpCustomers == null) {
            valid = false;
        }
        return valid;
    }

    @Override
    public List<ERPCustomer> getAll() {
        return customerRepository.findAll();
    }


    @Override
    public void delete(Integer custId) {
        checkNotNull(custId);
        customerRepository.delete(custId);
    }

    @Override
    public Page<ERPCustomer> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    public List<ERPCustomerType> getCustomerTypes() {
        return customerTypeRepository.findAll();
    }

    public ERPCustomerType saveCustomerType(ERPCustomerType customerType) {
        return customerTypeRepository.save(customerType);
    }

    public Page<ERPSalesRegion> getSalesRegions(Pageable pageable) {
        return salesRegionRepository.findAll(pageable);
    }


    public ERPSalesRegion saveSalesRegion(ERPSalesRegion region) {
        return salesRegionRepository.save(region);
    }

    public Page<ERPCustomer> find(CustomerCriteria criteria, Pageable pageable) {
        Predicate predicate = predicateBuilder.build(criteria, QERPCustomer.eRPCustomer);
        return customerRepository.findAll(predicate, pageable);
    }

}
