package com.cassinisys.erp.repo.production;

import java.util.List;

import com.cassinisys.erp.model.production.ERPProductionOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.production.ERPProductionOrder;

@Repository
public interface ProductionOrderDetailsRepository extends JpaRepository<ERPProductionOrderItem, Integer> {
	List<ERPProductionOrderItem> findByProductionOrder(@Param("order") ERPProductionOrder order);

}
