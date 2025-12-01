package com.cassinisys.erp.repo.production;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.production.ERPProductCategory;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ERPProductCategory, Integer>{
    ERPProductCategory findByName(String name);
    ERPProductCategory findByCode(String code);
    List<ERPProductCategory> findByParent(Integer parent);
}
