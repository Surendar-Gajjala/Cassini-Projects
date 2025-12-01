package com.cassinisys.erp.repo.crm;

import com.cassinisys.erp.model.crm.ERPCustomerOrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerOrderDetailsRepository extends JpaRepository<ERPCustomerOrderDetails, Integer> {

	@Query(
		"SELECT item FROM ERPCustomerOrderDetails item WHERE item.orderId= :orderId ORDER BY item.serialNumber"
	)
	List<ERPCustomerOrderDetails> findByOrderId(@Param("orderId") Integer orderId);

}
