package com.cassinisys.is.service.pm;

import com.cassinisys.is.model.pm.ISCustomerType;
import com.cassinisys.is.repo.pm.CustomerTypeRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The class is for CustomerTypeService
 */
@Service
@Transactional
public class CustomerTypeService implements
        CrudService<ISCustomerType, Integer>,
        PageableService<ISCustomerType, Integer> {

    @Autowired
    private CustomerTypeRepository customerTypeRepository;

    /**
     * The method used to create  ISCustomerType
     **/
    @Transactional(readOnly = false)
    @Override
    public ISCustomerType create(ISCustomerType customerType) {
        checkNotNull(customerType);
        customerType.setId(null);
        return customerTypeRepository.save(customerType);
    }

    /**
     * The method used to update  ISCustomerType
     **/
    @Transactional(readOnly = false)
    @Override
    public ISCustomerType update(ISCustomerType customerType) {
        checkNotNull(customerType);
        checkNotNull(customerType.getId());
        return customerTypeRepository.save(customerType);
    }

    /**
     * The method used to delete
     **/
    @Transactional(readOnly = false)
    @Override
    public void delete(Integer id) {
        checkNotNull(id);
        customerTypeRepository.delete(id);
    }

    /**
     * The method used to get  ISCustomerType
     **/
    @Transactional(readOnly = true)
    @Override
    public ISCustomerType get(Integer id) {
        checkNotNull(id);
        ISCustomerType customerType = customerTypeRepository.findOne(id);
        if (customerType == null) {
            throw new ResourceNotFoundException();
        }
        return customerType;
    }

    /**
     * The method used to getAll  for the list of ISCustomerType
     **/
    @Transactional(readOnly = true)
    @Override
    public List<ISCustomerType> getAll() {
        return customerTypeRepository.findAll();
    }

    /**
     * The method used to findAll for  the page of ISCustomerType
     **/
    @Transactional(readOnly = true)
    @Override
    public Page<ISCustomerType> findAll(Pageable pageable) {
        checkNotNull(pageable);
        return customerTypeRepository.findAll(pageable);
    }

}
