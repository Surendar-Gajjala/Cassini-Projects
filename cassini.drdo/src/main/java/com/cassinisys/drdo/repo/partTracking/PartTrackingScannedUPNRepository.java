package com.cassinisys.drdo.repo.partTracking;

import com.cassinisys.drdo.model.partTracking.PartTrackingScannedUPN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Nageshreddy on 08-10-2018.
 */
@Repository
public interface PartTrackingScannedUPNRepository extends
        JpaRepository<PartTrackingScannedUPN, Integer> {

    List<PartTrackingScannedUPN> findByPartTrackingId(Integer integer);

    List<PartTrackingScannedUPN> findByUpn(String upn);
}
