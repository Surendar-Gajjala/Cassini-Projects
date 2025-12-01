package com.cassinisys.plm.repo.wf;

import com.cassinisys.plm.model.wf.PLMWorkflowDefinitionEvent;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinitionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by GSR on 24-06-2021.
 */
@Repository
public interface PLMWorkflowDefinitionEventRepository extends JpaRepository<PLMWorkflowDefinitionEvent, Integer> {

    List<PLMWorkflowDefinitionEvent> findByWorkflowOrderByIdAsc(Integer workflow);

    List<PLMWorkflowDefinitionEvent> findByWorkflowAndEventTypeOrderByIdAsc(Integer workflow, String eventType);

    List<PLMWorkflowDefinitionEvent> findByWorkflowAndActivityAndEventTypeOrderByIdAsc(Integer workflow, PLMWorkflowDefinitionStatus workflowStatus, String eventType);

    PLMWorkflowDefinitionEvent findByWorkflowAndActivityAndEventTypeAndActionType(Integer workflow, PLMWorkflowDefinitionStatus workflowStatus, String eventType, String actionType);

    PLMWorkflowDefinitionEvent findByWorkflowAndEventTypeAndActionType(Integer workflow, String eventType, String actionType);
}
