package com.cassinisys.is.repo.workflow;

import com.cassinisys.is.model.workflow.ISWorkflowTerminate;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by reddy on 5/22/17.
 */
public interface ISWorkflowTerminateRepository extends JpaRepository<ISWorkflowTerminate, Integer> {
}
