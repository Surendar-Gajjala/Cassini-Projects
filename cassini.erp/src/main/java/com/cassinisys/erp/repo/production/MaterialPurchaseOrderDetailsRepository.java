package com.cassinisys.erp.repo.production;

import com.cassinisys.erp.model.production.ERPMaterialPurchaseOrder;
import com.cassinisys.erp.model.production.ERPMaterialPurchaseOrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialPurchaseOrderDetailsRepository extends
        JpaRepository<ERPMaterialPurchaseOrderDetails, Integer> {

    List<ERPMaterialPurchaseOrderDetails> findBymaterialPurchaseOrder(
            @Param("order") ERPMaterialPurchaseOrder order);

    List<ERPMaterialPurchaseOrderDetails> findByMaterialIdAndIssuedFalse(Integer id);

    List<ERPMaterialPurchaseOrderDetails> findByMaterialIdAndIssuedTrue(Integer id);

    ERPMaterialPurchaseOrderDetails findByMaterialIdAndMaterialPurchaseOrderId(Integer materialId, Integer poId);
}
