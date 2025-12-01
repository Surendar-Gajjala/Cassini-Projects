package com.cassinisys.erp.service.crm;

import com.cassinisys.erp.api.filtering.SalesRegionPredicateBuilder;
import com.cassinisys.erp.model.api.criteria.SalesRegionCriteria;
import com.cassinisys.erp.model.crm.ERPSalesRegion;
import com.cassinisys.erp.model.crm.QERPSalesRegion;
import com.cassinisys.erp.repo.crm.SalesRegionRepository;
import com.cassinisys.erp.service.paging.CrudService;
import com.cassinisys.erp.service.paging.PageableService;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by reddy on 8/22/15.
 */
@Service
@Transactional
public class SalesRegionService implements CrudService<ERPSalesRegion, Integer>,
        PageableService<ERPSalesRegion, Integer> {

    @Autowired
    private SalesRegionRepository repository;

    @Autowired
    private SalesRegionPredicateBuilder predicateBuilder;

    @Override
    public ERPSalesRegion create(ERPSalesRegion erpSalesRegion) {
        checkNotNull(erpSalesRegion);
        erpSalesRegion.setId(null);
        return repository.save(erpSalesRegion);
    }

    @Override
    public ERPSalesRegion update(ERPSalesRegion erpSalesRegion) {
        checkNotNull(erpSalesRegion);
        return repository.save(erpSalesRegion);
    }

    @Override
    public void delete(Integer id) {
        checkNotNull(id);
        repository.delete(id);
    }

    @Override
    public ERPSalesRegion get(Integer id) {
        checkNotNull(id);
        return repository.findOne(id);
    }

    @Override
    public List<ERPSalesRegion> getAll() {
        return repository.findAll();
    }

    @Override
    public Page<ERPSalesRegion> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<ERPSalesRegion> find(SalesRegionCriteria criteria, Pageable pageable) {
        Predicate predicate = predicateBuilder.build(criteria, QERPSalesRegion.eRPSalesRegion);
        return repository.findAll(predicate, pageable);
    }
}
