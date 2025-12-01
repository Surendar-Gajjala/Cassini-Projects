package com.cassinisys.erp.service.production;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cassinisys.erp.config.AutowiredLogger;
import com.cassinisys.erp.model.production.ERPProductType;
import com.cassinisys.erp.repo.production.ProductTypeRepository;
import com.cassinisys.erp.service.paging.CrudService;
import com.cassinisys.erp.service.paging.PageableService;

@Service
@Transactional
public class ProductTypeService implements CrudService<ERPProductType, Integer>,
        PageableService<ERPProductType, Integer> {

    @AutowiredLogger
    private Logger LOGGER;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ProductTypeRepository productTypeRepository;

   
    @Override
    public ERPProductType create(ERPProductType productType) {
        return productTypeRepository.save(productType);
    }

    @Override
    public ERPProductType update(ERPProductType productType) {
        return productTypeRepository.save(productType);
    }

    @Override
    public void delete(Integer id) {
        productTypeRepository.delete(id);
    }

    @Override
    public ERPProductType get(Integer id) {
        return productTypeRepository.findOne(id);
    }

    @Override
    public List<ERPProductType> getAll() {
        return productTypeRepository.findAll();
    }

    @Override
    public Page<ERPProductType> findAll(Pageable pageable) {
        return null;
    }

   
}
