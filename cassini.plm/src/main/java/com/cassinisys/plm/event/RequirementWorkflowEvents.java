package com.cassinisys.plm.event;

import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import lombok.AllArgsConstructor;
import com.cassinisys.plm.model.req.PLMRequirementDocumentChildren;
import lombok.Data;

/**
 * Created by sgajjala on 08-06-2022.
 */
public final class RequirementWorkflowEvents {
    @Data
    @AllArgsConstructor
    public static class ReqWorkflowStartedEvent {
        private Enum attachedToType;
        private PLMRequirementDocumentChildren attachedToObject;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class ReqWorkflowPromotedEvent {
        private Enum attachedToType;
        private PLMRequirementDocumentChildren attachedToObject;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromStatus;
        private PLMWorkflowStatus toStatus;
    }

    @Data
    @AllArgsConstructor
    public static class ReqWorkflowDemotedEvent {
        private Enum attachedToType;
        private PLMRequirementDocumentChildren attachedToObject;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromStatus;
        private PLMWorkflowStatus toStatus;
    }

    @Data
    @AllArgsConstructor
    public static class ReqWorkflowFinishedEvent {
        private Enum attachedToType;
        private PLMRequirementDocumentChildren attachedToObject;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromStatus;
    }

    @Data
    @AllArgsConstructor
    public static class ReqWorkflowHoldEvent {
        private Enum attachedToType;
        private PLMRequirementDocumentChildren attachedToObject;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus currentStatus;
    }

    @Data
    @AllArgsConstructor
    public static class ReqWorkflowUnHoldEvent {
        private Enum attachedToType;
        private PLMRequirementDocumentChildren attachedToObject;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus currentStatus;
    }
}


