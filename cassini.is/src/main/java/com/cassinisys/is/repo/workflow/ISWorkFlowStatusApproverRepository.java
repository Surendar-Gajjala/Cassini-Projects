package com.cassinisys.is.repo.workflow;

import com.cassinisys.is.model.workflow.ISWorkFlowStatusApprover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Rajabrahmachary on 19-05-2017.
 */
@Repository
public interface ISWorkFlowStatusApproverRepository extends JpaRepository<ISWorkFlowStatusApprover, Integer> {
    List<ISWorkFlowStatusApprover> findByStatus(Integer statusId);
}
