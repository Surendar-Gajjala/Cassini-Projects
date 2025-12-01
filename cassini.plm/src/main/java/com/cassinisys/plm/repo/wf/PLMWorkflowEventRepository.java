package com.cassinisys.plm.repo.wf;

import com.cassinisys.plm.model.wf.PLMWorkflowEvent;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by GSR on 24-06-2021.
 */
@Repository
public interface PLMWorkflowEventRepository extends JpaRepository<PLMWorkflowEvent, Integer> {

    List<PLMWorkflowEvent> findByWorkflowOrderByIdAsc(Integer workflow);

    List<PLMWorkflowEvent> findByWorkflowAndEventTypeOrderByIdAsc(Integer workflow, String eventType);

    List<PLMWorkflowEvent> findByWorkflowAndActivityAndEventTypeOrderByIdAsc(Integer workflow, PLMWorkflowStatus workflowStatus, String eventType);

    PLMWorkflowEvent findByWorkflowAndActivityAndEventTypeAndActionType(Integer workflow, PLMWorkflowStatus workflowStatus, String eventType, String actionType);

    PLMWorkflowEvent findByWorkflowAndEventTypeAndActionType(Integer workflow, String eventType, String actionType);
}
