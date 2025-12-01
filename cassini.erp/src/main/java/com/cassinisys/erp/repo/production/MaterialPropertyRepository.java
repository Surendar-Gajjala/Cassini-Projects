package com.cassinisys.erp.repo.production;

import com.cassinisys.erp.model.production.ERPMaterialProperty;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by reddy on 27/02/16.
 */
public interface MaterialPropertyRepository extends JpaRepository<ERPMaterialProperty, Integer> {
}
