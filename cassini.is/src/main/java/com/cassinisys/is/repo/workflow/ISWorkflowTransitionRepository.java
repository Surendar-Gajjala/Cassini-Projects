package com.cassinisys.is.repo.workflow;

import com.cassinisys.is.model.workflow.ISWorkflowTransition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyamreddy on 019 19-May -17.
 */
@Repository
public interface ISWorkflowTransitionRepository extends JpaRepository<ISWorkflowTransition, Integer> {
    List<ISWorkflowTransition> findByWorkflow(Integer id);

    List<ISWorkflowTransition> findByFromStatus(Integer id);

    List<ISWorkflowTransition> findByToStatus(Integer id);

    List<ISWorkflowTransition> findByFromStatusAndToStatus(Integer fromStatus, Integer id);

    List<ISWorkflowTransition> findByIdIn(Iterable<Integer> varl);
}
