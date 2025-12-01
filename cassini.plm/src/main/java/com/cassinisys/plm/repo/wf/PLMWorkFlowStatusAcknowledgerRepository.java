package com.cassinisys.plm.repo.wf;

import com.cassinisys.plm.model.wf.PLMWorkFlowStatusAcknowledger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Rajabrahmachary on 19-05-2017.
 */
@Repository
public interface PLMWorkFlowStatusAcknowledgerRepository extends JpaRepository<PLMWorkFlowStatusAcknowledger, Integer> {
    List<PLMWorkFlowStatusAcknowledger> findByStatus(Integer statusId);
    PLMWorkFlowStatusAcknowledger findByStatusAndPersonAndAcknowledgedIsFalse(Integer statusId, Integer personId);
}
