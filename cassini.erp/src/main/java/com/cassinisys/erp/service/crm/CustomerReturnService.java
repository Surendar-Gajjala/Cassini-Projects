package com.cassinisys.erp.service.crm;

import com.cassinisys.erp.api.exceptions.ERPException;
import com.cassinisys.erp.api.exceptions.ErrorCodes;
import com.cassinisys.erp.api.exceptions.ResourceNotFoundException;
import com.cassinisys.erp.api.filtering.CustomerReturnPredicateBuilder;
import com.cassinisys.erp.model.api.criteria.CustomerReturnCriteria;
import com.cassinisys.erp.model.crm.*;
import com.cassinisys.erp.model.hrm.ERPEmployee;
import com.cassinisys.erp.repo.crm.CustomerReturnDetailsRepository;
import com.cassinisys.erp.repo.crm.CustomerReturnRepository;
import com.cassinisys.erp.service.hrm.EmployeeService;
import com.cassinisys.erp.service.paging.CrudService;
import com.cassinisys.erp.service.paging.PageableService;
import com.cassinisys.erp.service.production.ProductInventoryService;
import com.cassinisys.erp.service.security.SessionWrapper;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@Transactional
public class CustomerReturnService implements CrudService<ERPCustomerReturn, Integer>, PageableService<ERPCustomerReturn, Integer> {

    @Autowired
    SessionWrapper sessionWrapper;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    CustomerReturnRepository custReturnRepository;
    @Autowired
    CustomerReturnDetailsRepository custReturnDetailsRepository;
    @Autowired
    CustomerReturnPredicateBuilder predicateBuilder;
    @Autowired
    ProductInventoryService productInventoryService;

    @Override
    public ERPCustomerReturn create(ERPCustomerReturn customerReturn) {
        customerReturn.setReturnDate(new Date());
        customerReturn = custReturnRepository.save(customerReturn);

        List<ERPCustomerReturnDetails> details = customerReturn.getDetails();
        for (ERPCustomerReturnDetails detail : details) {
            productInventoryService.stockIn(detail.getProduct().getId(), detail.getQuantity());
        }

        return customerReturn;
    }

    @Override
    public Page<ERPCustomerReturn> findAll(Pageable pageable) {
        return custReturnRepository.findAll(pageable);
    }

    public Page<ERPCustomerReturn> findAll(CustomerReturnCriteria criteria, Pageable pageable) {
        Predicate predicate = predicateBuilder.build(criteria, QERPCustomerReturn.eRPCustomerReturn);
        return custReturnRepository.findAll(predicate, pageable);
    }

    public List<ERPCustomerReturn> getNewReturns() {
        return custReturnRepository.getNewReturns(ReturnStatus.PENDING);
    }

    @Override
    public ERPCustomerReturn update(ERPCustomerReturn customerReturn) {
        checkNotNull(customerReturn);
        checkNotNull(customerReturn.getId());
        if (custReturnRepository.findOne(customerReturn.getId()) == null) {
            throw new ERPException(HttpStatus.NOT_FOUND,
                    ErrorCodes.RESOURCE_NOT_FOUND);
        }
        return custReturnRepository.save(customerReturn);
    }

    @Override
    public void delete(Integer returnId) {
        checkNotNull(returnId);
        custReturnRepository.delete(returnId);
    }

    @Override
    public ERPCustomerReturn get(Integer returnId) {
        return custReturnRepository.findOne(returnId);
    }

    @Override
    public List<ERPCustomerReturn> getAll() {
        return custReturnRepository.findAll();
    }



    public List<ERPCustomerReturn> getCustomerReturnsByCustomer(Integer customerId) {
        return custReturnRepository.findByCustomerId(customerId);
    }

    public List<ERPCustomerReturnDetails> getCustomerDetailsByReturnId(Integer returnId) {
        ERPCustomerReturn erpCustomerReturn = custReturnRepository.findByReturnId(returnId);
        return erpCustomerReturn.getDetails();
    }

    public List<ERPCustomerReturn> approveAllNewReturns() {

        List<ERPCustomerReturn> unApprovedReturns = new ArrayList<>();

        List<ERPCustomerReturn> newReturns = getNewReturns();
        for (ERPCustomerReturn returns : newReturns) {

            if ((returns != null)) {
                approveReturn(returns.getId());
            } else {
                unApprovedReturns.add(returns);
            }
        }
        return unApprovedReturns;
    }

    public ERPCustomerReturn approveReturn(Integer returnId) {
        checkNotNull(returnId);
        ERPCustomerReturn returns = custReturnRepository.findOne(returnId);
        if (returns != null) {
            returns.setStatus(ReturnStatus.APPROVED);
            returns = custReturnRepository.save(returns);
        } else {
            throw new ResourceNotFoundException("Return " + returnId + " does not exist");
        }

        return returns;

    }

    public List<ERPCustomerReturnDetails> getAllReturnDetailsByReturnId(
            Integer returnId) {
        return custReturnDetailsRepository.findByReturnId(returnId);
    }

    private void cancelPendingItems(ERPCustomerReturn Return) {
        List<ERPCustomerReturnDetails> details = getAllReturnDetailsByReturnId(Return.getId());
        List<ERPCustomerReturnDetails> itemsToRemove = new ArrayList<>();
        for (ERPCustomerReturnDetails detail : details) {
            itemsToRemove.add(detail);}
        custReturnDetailsRepository.delete(itemsToRemove);
        }

    public ERPCustomerReturn cancelReturn(Integer returnId) {
        checkNotNull(returnId);
        ERPCustomerReturn Return = custReturnRepository.findOne(returnId);
        if (Return != null) {
            Integer id = sessionWrapper.getSession().getLogin().getPerson().getId();
            ERPEmployee employee = employeeService.getEmployeeById(id);

            if (Return.getStatus() == ReturnStatus.PENDING) {
                cancelPendingItems(Return);
            }
            Return.setStatus(ReturnStatus.CANCELLED);
            Return = custReturnRepository.save(Return);
        } else {
            throw new ResourceNotFoundException("Return " + returnId + " does not exist");
        }
        return Return;
    }
}




