package com.cassinisys.erp.repo.production;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.production.ERPMaterialPurchaseOrder;

@Repository
public interface MaterialPurchaseOrderRepository extends
		JpaRepository<ERPMaterialPurchaseOrder, Integer> {

}
