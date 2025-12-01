package com.cassinisys.erp.repo.production;

import com.cassinisys.erp.model.production.ERPProduct;
import com.cassinisys.erp.model.production.ERPProductInventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductInventoryRepository extends JpaRepository<ERPProductInventory, Integer> {

    @Query (
            "SELECT p FROM ERPProductInventory p WHERE p.product.id = :productId"
    )
    ERPProductInventory getProductInventoryByProductId(@Param("productId") Integer productId);

    @Query (
            "SELECT p FROM ERPProductInventory p WHERE p.product.id IN (:productIds)"
    )
    List<ERPProductInventory> getProductsInventory(@Param("productIds") List<Integer> productIds);

    @Query ("SELECT p FROM ERPProductInventory p WHERE p.inventory < p.threshold")
    Page<ERPProductInventory> getLowInventoryItems(Pageable  pageable);
}
