package com.cassinisys.erp.service.production;

import com.cassinisys.erp.api.filtering.ProductPredicateBuilder;
import com.cassinisys.erp.config.AutowiredLogger;
import com.cassinisys.erp.model.api.criteria.ProductCriteria;
import com.cassinisys.erp.model.production.ERPProduct;
import com.cassinisys.erp.model.production.ERPProductCategory;
import com.cassinisys.erp.model.production.QERPProduct;
import com.cassinisys.erp.repo.production.ProductRepository;
import com.cassinisys.erp.service.paging.CrudService;
import com.cassinisys.erp.service.paging.PageableService;
import com.mysema.query.types.Predicate;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by reddy on 7/18/15.
 */
@Service
@Transactional
public class ProductService implements CrudService<ERPProduct, Integer>,
        PageableService<ERPProduct, Integer> {

    @AutowiredLogger
    private Logger LOGGER;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ProductPredicateBuilder predicateBuilder;

    @Autowired
    private ProductInventoryService productInventoryService;



    @Override
    public ERPProduct create(ERPProduct product) {
        product = productRepository.save(product);
        return product;
    }

    @Override
    public ERPProduct update(ERPProduct product) {
        return productRepository.save(product);
    }

    @Override
    public void delete(Integer id) {
        productRepository.delete(id);
    }

    @Override
    public ERPProduct get(Integer id) {
        return productRepository.findOne(id);
    }

    public ERPProduct getBySku(String sku) {
        return productRepository.findBySku(sku);
    }

    @Override
    public List<ERPProduct> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Page<ERPProduct> findAll(Pageable pageable) {
        return null;
    }

    public Page<ERPProduct> getByCategory(Integer catid, Pageable pageable) {
        ERPProductCategory cat = productCategoryService.get(catid);
        List<Integer> cats = productCategoryService.flattenCategoryPath(cat);
        if(cats.size() > 0) {
            return productRepository.findInCategories(cats, pageable);
        }
        else {
            return new PageImpl<ERPProduct>(new ArrayList<ERPProduct>(), pageable, 0);
        }
    }

    public Page<ERPProduct> findAll(ProductCriteria criteria, Pageable pageable) {
        Predicate predicate = predicateBuilder.build(criteria, QERPProduct.eRPProduct);
        return productRepository.findAll(predicate, pageable);
    }

}
