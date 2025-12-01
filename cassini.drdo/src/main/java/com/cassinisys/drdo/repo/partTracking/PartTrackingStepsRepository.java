package com.cassinisys.drdo.repo.partTracking;

import com.cassinisys.drdo.model.partTracking.PartTrackingSteps;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Nageshreddy on 08-10-2018.
 */
@Repository
public interface PartTrackingStepsRepository extends
        JpaRepository<PartTrackingSteps, Integer> {

    List<PartTrackingSteps> findByPartTrackingOrderBySerialNo(Integer id);

    @Modifying
    @Transactional
    @Query("delete from PartTrackingSteps list where list.partTracking = :partTracking")
    void deleteByPartTracking(@Param("partTracking") Integer partTracking);

    List<PartTrackingSteps> findByIdIn(Iterable<Integer> ids);
}
