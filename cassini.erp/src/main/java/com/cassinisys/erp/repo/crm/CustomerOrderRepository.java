package com.cassinisys.erp.repo.crm;

import com.cassinisys.erp.model.crm.CustomerOrderStatus;
import com.cassinisys.erp.model.crm.ERPCustomer;
import com.cassinisys.erp.model.crm.ERPCustomerOrder;
import com.cassinisys.erp.scripting.dto.KeyAndNumber;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CustomerOrderRepository extends JpaRepository<ERPCustomerOrder, Integer>,
        QueryDslPredicateExecutor<ERPCustomerOrder>{

        ERPCustomerOrder findByOrderNumber(String orderNumber);

        @Query (
                "SELECT o FROM ERPCustomerOrder o WHERE o.status= :status"
        )
        List<ERPCustomerOrder> getNewOrders(@Param("status") CustomerOrderStatus status);

        @Query (
                "SELECT o FROM ERPCustomerOrder o WHERE o.customer= :customer ORDER BY o.orderedDate DESC"
        )
        List<ERPCustomerOrder> getCustomerOrders(@Param("customer") ERPCustomer customer);



        @Query (
                "SELECT o FROM ERPCustomerOrder o WHERE o.status= 'APPROVED' AND DAY(CURRENT_TIMESTAMP - o.modifiedDate) >= 1"
        )
        Page<ERPCustomerOrder> getLateApprovedOrders(Pageable pageable);

         @Query (
                 "SELECT o FROM ERPCustomerOrder o WHERE o.status= 'PROCESSED' AND DAY(CURRENT_TIMESTAMP - o.modifiedDate) >= 2"
         )
         Page<ERPCustomerOrder> getLateShippedOrders(Pageable pageable);

        @Query (
                "SELECT o FROM ERPCustomerOrder o WHERE o.status= 'PROCESSING' AND o.modifiedDate <= :time"
        )
        Page<ERPCustomerOrder> getLateProcessedOrders(@Param("time")Date time, Pageable pageable);
}
