package com.cassinisys.plm.repo.wf;

import com.cassinisys.plm.model.wf.PLMWorkflowDefinitionTransition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by GSR on 19-05-2017.
 */
@Repository
public interface PLMWorkflowDefinitionTransitionRepository extends JpaRepository<PLMWorkflowDefinitionTransition, Integer> {
    List<PLMWorkflowDefinitionTransition> findByWorkflow(Integer id);

    List<PLMWorkflowDefinitionTransition> findByFromStatus(Integer id);

    List<PLMWorkflowDefinitionTransition> findByToStatus(Integer id);

}
