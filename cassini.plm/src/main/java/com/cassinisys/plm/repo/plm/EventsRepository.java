package com.cassinisys.plm.repo.plm;

import com.cassinisys.plm.model.plm.PLMEvents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by swapna on 12/31/17.
 */
@Repository
public interface EventsRepository extends JpaRepository<PLMEvents, Integer> {

    PLMEvents findById(Integer id);

    List<PLMEvents> findByEvent(Integer event);

    List<PLMEvents> findByIdIn(Iterable<Integer> ids);

}
