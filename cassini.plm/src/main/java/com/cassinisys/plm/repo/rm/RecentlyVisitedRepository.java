package com.cassinisys.plm.repo.rm;

import com.cassinisys.plm.model.rm.RecentlyVisited;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam reddy on 21-09-2018.
 */
@Repository
public interface RecentlyVisitedRepository extends JpaRepository<RecentlyVisited, Integer> {

    Page<RecentlyVisited> findByPersonOrderByVisitedDateDesc(Integer person, Pageable pageable);

    List<RecentlyVisited> findByPersonOrderByVisitedDateAsc(Integer person);

    List<RecentlyVisited> findByObjectId(Integer objectId);

    RecentlyVisited findByObjectIdAndObjectTypeAndPerson(Integer objectId, String objectType, Integer person);
}
