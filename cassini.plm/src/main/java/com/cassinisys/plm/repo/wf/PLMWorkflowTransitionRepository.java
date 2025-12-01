package com.cassinisys.plm.repo.wf;

import com.cassinisys.plm.model.wf.PLMWorkflowTransition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyamreddy on 019 19-May -17.
 */
@Repository
public interface PLMWorkflowTransitionRepository extends JpaRepository<PLMWorkflowTransition, Integer> {
    List<PLMWorkflowTransition> findByWorkflow(Integer id);

    List<PLMWorkflowTransition> findByFromStatus(Integer id);

    List<PLMWorkflowTransition> findByToStatus(Integer id);

    List<PLMWorkflowTransition> findByFromStatusAndToStatus(Integer fromStatus, Integer id);

    List<PLMWorkflowTransition> findByIdIn(Iterable<Integer> varl);
}
