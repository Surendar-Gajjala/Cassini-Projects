package com.cassinisys.erp.service.common;

import com.cassinisys.erp.model.hrm.ERPBusinessUnit;
import com.cassinisys.erp.repo.hrm.BusinessUnitRepository;
import com.cassinisys.erp.service.paging.CrudService;
import com.cassinisys.erp.service.paging.PageableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by reddy on 18/01/16.
 */
@Service
@Transactional
public class BusinessUnitService implements CrudService<ERPBusinessUnit, Integer>,
        PageableService<ERPBusinessUnit, Integer> {

    @Autowired
    private BusinessUnitRepository businessUnitRepository;

    @Override
    public ERPBusinessUnit create(ERPBusinessUnit erpBusinessUnit) {
        return businessUnitRepository.save(erpBusinessUnit);
    }

    @Override
    public ERPBusinessUnit update(ERPBusinessUnit erpBusinessUnit) {
        return businessUnitRepository.save(erpBusinessUnit);
    }

    @Override
    public void delete(Integer prId) {
        businessUnitRepository.delete(prId);
    }

    @Override
    public ERPBusinessUnit get(Integer prId) {
        return businessUnitRepository.findOne(prId);
    }

    @Override
    public List<ERPBusinessUnit> getAll() {
        return businessUnitRepository.findAll();
    }

    @Override
    public Page<ERPBusinessUnit> findAll(Pageable pageable) {
        return businessUnitRepository.findAll(pageable);
    }
}
