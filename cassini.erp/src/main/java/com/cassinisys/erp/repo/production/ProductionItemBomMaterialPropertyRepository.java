package com.cassinisys.erp.repo.production;

import com.cassinisys.erp.model.production.ERPMaterialProperty;
import com.cassinisys.erp.model.production.ERPProductionItemBomMaterialProperty;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by reddy on 27/02/16.
 */
public interface ProductionItemBomMaterialPropertyRepository extends JpaRepository<ERPProductionItemBomMaterialProperty, Integer> {
}
