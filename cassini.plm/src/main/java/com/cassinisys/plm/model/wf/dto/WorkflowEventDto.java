package com.cassinisys.plm.model.wf.dto;

import com.cassinisys.plm.model.wf.PLMWorkflowDefinitionEvent;
import com.cassinisys.plm.model.wf.PLMWorkflowEvent;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam on 26-06-2021.
 */
@Data
public class WorkflowEventDto {
    private String eventType;
    private List<PLMWorkflowEvent> workflowEvents = new ArrayList<>();
    private List<PLMWorkflowDefinitionEvent> workflowDefinitionEvents = new ArrayList<>();
}
