package com.cassinisys.platform.repo.common;

import com.cassinisys.platform.model.common.MobileDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by reddy on 10/24/15.
 */
@Repository
public interface MobileDeviceRepository extends JpaRepository<MobileDevice, String> {

    List<MobileDevice> findByDeviceIdIn(Iterable<String> ids);
}
