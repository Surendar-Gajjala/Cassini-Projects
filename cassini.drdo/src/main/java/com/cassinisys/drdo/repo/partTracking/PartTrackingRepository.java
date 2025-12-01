package com.cassinisys.drdo.repo.partTracking;

import com.cassinisys.drdo.model.partTracking.PartTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Nageshreddy on 08-10-2018.
 */

@Repository
public interface PartTrackingRepository extends
        JpaRepository<PartTracking, Integer> {

}
