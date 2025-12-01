package com.cassinisys.erp.repo.common;

import com.cassinisys.erp.model.common.ERPMobileDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by reddy on 10/24/15.
 */
@Repository
public interface MobileDeviceRepository extends JpaRepository<ERPMobileDevice, String> {
}
