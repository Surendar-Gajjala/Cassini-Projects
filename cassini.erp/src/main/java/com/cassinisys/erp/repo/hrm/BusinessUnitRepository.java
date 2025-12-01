package com.cassinisys.erp.repo.hrm;

import com.cassinisys.erp.model.hrm.ERPBusinessUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by reddy on 10/26/15.
 */
@Repository
public interface BusinessUnitRepository extends JpaRepository<ERPBusinessUnit, Integer> {
    ERPBusinessUnit findByName(String name);
}
