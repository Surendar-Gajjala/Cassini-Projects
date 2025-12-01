package com.cassinisys.is.repo.store;

import com.cassinisys.is.model.store.CustomPurchaseOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Rajabrahmachary on 14-09-2018.
 */
@Repository
public interface CustomPurchaseOrderItemRepository extends JpaRepository<CustomPurchaseOrderItem, Integer>, QueryDslPredicateExecutor<CustomPurchaseOrderItem> {
    @Query("SELECT i FROM CustomPurchaseOrderItem i WHERE i.customPurchaseOrder.id =:purchaseOrderId ORDER BY i.id ASC")
    List<CustomPurchaseOrderItem> findByCustomPurchaseOrderId(@Param("purchaseOrderId") Integer purchaseOrderId);

    List<CustomPurchaseOrderItem> findByIdIn(List<Integer> purchaseOrderIds);
}
