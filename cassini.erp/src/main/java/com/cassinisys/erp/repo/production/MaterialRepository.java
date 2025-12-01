package com.cassinisys.erp.repo.production;

import com.cassinisys.erp.model.production.ERPMaterial;
import com.cassinisys.erp.model.production.ERPMaterialCategory;
import com.cassinisys.erp.model.production.ERPMaterialType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<ERPMaterial, Integer>, QueryDslPredicateExecutor<ERPMaterial> {

    ERPMaterial findByName(String name);

    ERPMaterial findBySku(String sku);

    List<ERPMaterial> findByIdIn(Iterable<Integer> var1);

    List<ERPMaterial> findByCategory(Integer category, Pageable pageable);

    List<ERPMaterial> findByType(ERPMaterialType type);

    List<ERPMaterial> findByCategory(ERPMaterialCategory category);

    @Query(
            "SELECT m FROM ERPMaterial m WHERE m.category.id IN :categories"
    )
    Page<ERPMaterial> findInCategories(@Param("categories") List<Integer> categories, Pageable pageable);

    @Query(
            "SELECT m.id FROM ERPMaterial m WHERE LOWER(m.sku) LIKE lower(concat('%', :sku,'%'))"
    )
    List<Integer> findBySkuContatins(@Param("sku") String sku);


    @Query(
            "SELECT m.id FROM ERPMaterial m WHERE LOWER(m.name) LIKE lower(concat('%', :name,'%'))"
    )
    List<Integer> findByNameContatins(@Param("name") String name);
}


