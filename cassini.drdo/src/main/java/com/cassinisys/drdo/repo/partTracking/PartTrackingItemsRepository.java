package com.cassinisys.drdo.repo.partTracking;

import com.cassinisys.drdo.model.partTracking.PartTrackingItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Nageshreddy on 08-10-2018.
 */
@Repository
public interface PartTrackingItemsRepository extends JpaRepository<PartTrackingItems, Integer> {

    List<PartTrackingItems> findByItemOrderBySerialNo(Integer integer);

    @Query("SELECT p.id from PartTrackingItems p where p.item = :integer")
    List<Integer> getIdsByItem(@Param("integer") Integer integer);

}
