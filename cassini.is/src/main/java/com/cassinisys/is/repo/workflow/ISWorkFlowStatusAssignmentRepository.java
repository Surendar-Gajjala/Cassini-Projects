package com.cassinisys.is.repo.workflow;

import com.cassinisys.is.model.workflow.ISWorkFlowStatusAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Rajabrahmachary on 19-05-2017.
 */
@Repository
public interface ISWorkFlowStatusAssignmentRepository extends JpaRepository<ISWorkFlowStatusAssignment, Integer> {
}
