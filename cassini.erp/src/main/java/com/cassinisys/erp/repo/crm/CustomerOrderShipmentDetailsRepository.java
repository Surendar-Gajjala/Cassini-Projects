package com.cassinisys.erp.repo.crm;

import com.cassinisys.erp.model.crm.ERPCustomerOrderShipmentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by reddy on 05/03/16.
 */
@Repository
public interface CustomerOrderShipmentDetailsRepository extends JpaRepository<ERPCustomerOrderShipmentDetails, Integer> {
    @Modifying
    @Query("DELETE FROM ERPCustomerOrderShipmentDetails item where item.id= :itemId")
    void deleteItem(@Param("itemId") Integer itemId);


    @Query("SELECT shipment FROM ERPCustomerOrderShipmentDetails shipment WHERE shipment.id= :shipmentId")
    List<ERPCustomerOrderShipmentDetails> findByShipmentId(@Param("shipmentId") Integer shipmentId);


}
