package com.cassinisys.plm.repo.wf;

import com.cassinisys.plm.model.wf.PLMWorkFlowStatusApprover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Rajabrahmachary on 19-05-2017.
 */
@Repository
public interface PLMWorkFlowStatusApproverRepository extends JpaRepository<PLMWorkFlowStatusApprover, Integer> {
    List<PLMWorkFlowStatusApprover> findByStatus(Integer statusId);
    PLMWorkFlowStatusApprover findByStatusAndPersonAndVoteIsNull(Integer statusId, Integer personId);

    List<PLMWorkFlowStatusApprover> findByStatusAndPersonOrderByIdDesc(Integer status, Integer person);
}

