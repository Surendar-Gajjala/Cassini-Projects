package com.cassinisys.is.service.procm;

import com.cassinisys.is.filtering.ContractorCriteria;
import com.cassinisys.is.filtering.ContractorPredicateBuilder;
import com.cassinisys.is.model.procm.ISContractor;
import com.cassinisys.is.model.procm.ISWorkOrder;
import com.cassinisys.is.model.procm.QISContractor;
import com.cassinisys.is.repo.procm.ContractorRepository;
import com.cassinisys.is.repo.procm.WorkOrderRepository;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by swapna on 21/01/19.
 */
@Service
public class ContractorService {

    @Autowired
    private ContractorRepository contractorRepository;
    @Autowired
    private WorkOrderRepository workOrderRepository;
    @Autowired
    private ContractorPredicateBuilder contractorPredicateBuilder;

    public ISContractor create(ISContractor contractor) {
        return contractorRepository.save(contractor);
    }

    public List<ISContractor> getAll() {
        return contractorRepository.findAll();
    }

    public List<ISContractor> getByIds(List<Integer> ids) {
        return contractorRepository.findByIdIn(ids);
    }

    public Page<ISContractor> getPageableContractors(Pageable pageable) {
        List<ISContractor> contractors = contractorRepository.findAll();
        for (ISContractor contractor : contractors) {
            List<ISWorkOrder> workOrders = workOrderRepository.findByContractor(contractor.getId());
            contractor.setWorkOrders(workOrders);
        }
        int start = pageable.getOffset();
        int end = (start + pageable.getPageSize()) > contractors.size() ? contractors.size() : (start + pageable.getPageSize());
        return new PageImpl<ISContractor>(contractors.subList(start, end), pageable, contractors.size());
    }

    public List<ISContractor> getActiveContractors() {
        return contractorRepository.findByActive(Boolean.TRUE);
    }

    public ISContractor get(Integer contractorId) {
        return contractorRepository.findOne(contractorId);
    }

    public ISContractor update(ISContractor contractor) {
        return contractorRepository.save(contractor);
    }

    public void delete(Integer contractorId) {
        contractorRepository.delete(contractorId);
    }

    public List<ISContractor> findByContactPerson(Integer contactPerson) {
        return contractorRepository.findByContact(contactPerson);
    }

    public Page<ISContractor> contractorsFreeTextSearch(Pageable pageable, ContractorCriteria criteria) {
        Predicate predicate = contractorPredicateBuilder.build(criteria, QISContractor.iSContractor);
        return contractorRepository.findAll(predicate, pageable);
    }
}
