package com.cassinisys.erp.repo.crm;

import com.cassinisys.erp.model.crm.ERPShipper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by reddy on 10/13/15.
 */
@Repository
public interface ShipperRepository extends JpaRepository<ERPShipper, Integer> {
}
