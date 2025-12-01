package com.cassinisys.is.repo.workflow;

import com.cassinisys.is.model.workflow.ISWorkflowDefinitionTransition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by GSR on 19-05-2017.
 */
@Repository
public interface ISWorkflowDefinitionTransitionRepository extends JpaRepository<ISWorkflowDefinitionTransition, Integer> {
    List<ISWorkflowDefinitionTransition> findByWorkflow(Integer id);

    List<ISWorkflowDefinitionTransition> findByFromStatus(Integer id);

    List<ISWorkflowDefinitionTransition> findByToStatus(Integer id);

}
