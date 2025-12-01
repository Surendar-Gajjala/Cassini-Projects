package com.cassinisys.drdo.repo.partTracking;

import com.cassinisys.drdo.model.partTracking.TrackValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Nageshreddy on 08-10-2018.
 */

@Repository
public interface TrackValueRepository extends JpaRepository<TrackValue, Integer> {

    List<TrackValue> findByPartTrackingInOrderByPartTrackingAsc(Iterable<Integer> partTrackings);

    List<TrackValue> findByPartTrackingInAndCheckedFalse(Iterable<Integer> partTrackings);
}
