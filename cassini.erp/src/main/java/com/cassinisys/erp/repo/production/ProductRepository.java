package com.cassinisys.erp.repo.production;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.production.ERPProduct;

@Repository
public interface ProductRepository extends JpaRepository<ERPProduct, Integer>,
        QueryDslPredicateExecutor<ERPProduct>{
    ERPProduct findByName(String name);
    ERPProduct findBySku(String sku);
    List<ERPProduct> findByCategory(Integer category, Pageable pageable);

    @Query (
        "SELECT p FROM ERPProduct p WHERE p.category.id IN :categories"
    )
    Page<ERPProduct> findInCategories(@Param("categories") List<Integer> categories, Pageable pageable);
}
