package com.cassinisys.plm.repo.wf;

import com.cassinisys.plm.model.wf.PLMWorkFlowStatusObserver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Rajabrahmachary on 19-05-2017.
 */
@Repository
public interface PLMWorkFlowStatusObserverRepository extends JpaRepository<PLMWorkFlowStatusObserver, Integer> {
    List<PLMWorkFlowStatusObserver> findByStatus(Integer statusId);
}
