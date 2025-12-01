package com.cassinisys.erp.repo.production;

import com.cassinisys.erp.model.production.ERPProductInventoryHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductInventoryHistoryRepository extends JpaRepository<ERPProductInventoryHistory, Integer>,
        QueryDslPredicateExecutor<ERPProductInventoryHistory> {

    @Query(
            "SELECT h FROM ERPProductInventoryHistory h WHERE h.product = :productId"
    )
    Page<ERPProductInventoryHistory> getProductInventoryHistory(@Param("productId") Integer productId, Pageable pageable);

}
