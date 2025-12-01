package com.cassinisys.is.repo.workflow;

import com.cassinisys.is.model.workflow.ISWorkflowDefinitionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by GSR on 19-05-2017.
 */
@Repository
public interface ISWorkflowDefinitionStatusRepository extends JpaRepository<ISWorkflowDefinitionStatus, Integer> {
    List<ISWorkflowDefinitionStatus> findByWorkflow(Integer id);
}
