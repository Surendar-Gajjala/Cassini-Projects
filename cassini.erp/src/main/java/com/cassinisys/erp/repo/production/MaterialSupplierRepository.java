package com.cassinisys.erp.repo.production;

import com.cassinisys.erp.model.hrm.ERPAllowanceHistory;
import com.cassinisys.erp.model.production.ERPMaterialSupplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lakshmi on 2/17/2016.
 */

@Repository
public interface MaterialSupplierRepository extends
        JpaRepository<ERPMaterialSupplier, ERPMaterialSupplier.MaterialSupplierId> {

        //For EmbeddedIds
        List<ERPMaterialSupplier> findByMaterialSupplierIdMaterialId(Integer materialId);
        List<ERPMaterialSupplier> findByMaterialSupplierIdSupplierId(Integer supplierId);

}

