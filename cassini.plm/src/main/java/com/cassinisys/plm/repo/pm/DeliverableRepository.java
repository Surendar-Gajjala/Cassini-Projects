package com.cassinisys.plm.repo.pm;

import com.cassinisys.plm.model.pm.PLMDeliverable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by swapna on 12/31/17.
 */

@Repository
public interface DeliverableRepository extends JpaRepository<PLMDeliverable, Integer> {

    List<PLMDeliverable> findByItemRevision(Integer item);

    List<PLMDeliverable> findByIdIn(Iterable<Integer> ids);

}
