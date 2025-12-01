package com.cassinisys.erp.repo.crm;

import com.cassinisys.erp.model.crm.ERPConsignment;
import com.cassinisys.erp.model.crm.ERPCustomerOrder;
import com.cassinisys.erp.model.crm.ERPCustomerOrderShipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerOrderShipmentRepository extends JpaRepository<ERPCustomerOrderShipment, Integer>,
        QueryDslPredicateExecutor<ERPCustomerOrderShipment>{

    @Modifying
    @Query("DELETE FROM ERPCustomerOrderShipment shipmentId where shipmentId.id= :shipmentId")
    void deleteItem(@Param("shipmentId") Integer shipmentId);

    ERPCustomerOrderShipment findFirstByInvoiceNumber(String invoiceNumber);
    List<ERPCustomerOrderShipment> findByOrder(ERPCustomerOrder order);
    List<ERPCustomerOrderShipment> findByConsignment(ERPConsignment consignment);
}
