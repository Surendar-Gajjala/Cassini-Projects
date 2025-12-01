package com.cassinisys.erp.repo.crm;

import com.cassinisys.erp.model.crm.ERPCustomerOrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerOrderStatusHistoryRepository extends JpaRepository<ERPCustomerOrderStatusHistory, Integer> {

    @Query (
            "SELECT h FROM ERPCustomerOrderStatusHistory h WHERE h.order.id= :orderId ORDER BY h.modifiedDate DESC"
    )
    List<ERPCustomerOrderStatusHistory> findOrdersByOrderId(@Param("orderId") Integer orderId);

}
