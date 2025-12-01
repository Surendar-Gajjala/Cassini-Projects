package com.cassinisys.is.service.pm;

import com.cassinisys.is.model.pm.ISCustomer;
import com.cassinisys.is.repo.pm.CustomerRepository;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The class is for CustomerService
 */
@Service
@Transactional
public class CustomerService implements CrudService<ISCustomer, Integer>,
        PageableService<ISCustomer, Integer> {

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * The method used to create  ISCustomer
     **/
    @Transactional(readOnly = false)
    @Override
    public ISCustomer create(ISCustomer customer) {
        checkNotNull(customer);
        customer.setId(null);
        customer = customerRepository.save(customer);
        return customer;
    }

    /**
     * The method used to update  ISCustomer
     **/
    @Transactional(readOnly = false)
    @Override
    public ISCustomer update(ISCustomer customer) {
        checkNotNull(customer);
        checkNotNull(customer.getId());
        customer = customerRepository.save(customer);
        return customer;
    }

    /**
     * The method used to delete customer of ISCustomer
     **/
    @Transactional(readOnly = false)
    @Override
    public void delete(Integer id) {
        checkNotNull(id);
        ISCustomer customer = customerRepository.findOne(id);
        if (customer == null) {
            throw new ResourceNotFoundException();
        }
        customerRepository.delete(customer);

    }

    /**
     * The method used to get  ISCustomer
     **/
    @Transactional(readOnly = true)
    @Override
    public ISCustomer get(Integer id) {
        checkNotNull(id);
        ISCustomer customer = customerRepository.findOne(id);
        if (customer == null) {
            throw new ResourceNotFoundException();
        }
        return customer;
    }

    /**
     * The method used to getAll for the list of ISCustomer
     **/
    @Transactional(readOnly = true)
    @Override
    public List<ISCustomer> getAll() {
        return customerRepository.findAll();
    }

    /**
     * The method used to findAll for the Page of ISCustomer
     **/
    @Transactional(readOnly = true)
    @Override
    public Page<ISCustomer> findAll(Pageable pageable) {
        checkNotNull(pageable);
        return customerRepository.findAll(pageable);
    }

    /**
     * The method used to findMultiple for the list of ISCustomer
     **/
    @Transactional(readOnly = true)
    public List<ISCustomer> findMultiple(List<Integer> ids) {
        return customerRepository.findByIdIn(ids);
    }

}
