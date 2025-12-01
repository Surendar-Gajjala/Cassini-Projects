package com.cassinisys.plm.model.wf.events;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

public final class WorkflowEvents {

    @Data
    @AllArgsConstructor
    public static class WorkflowStartedEvent {
        private Enum attachedToType;
        private CassiniObject attachedToObject;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class WorkflowPromotedEvent {
        private Enum attachedToType;
        private CassiniObject attachedToObject;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromStatus;
        private PLMWorkflowStatus toStatus;
    }

    @Data
    @AllArgsConstructor
    public static class WorkflowDemotedEvent {
        private Enum attachedToType;
        private CassiniObject attachedToObject;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromStatus;
        private PLMWorkflowStatus toStatus;
    }

    @Data
    @AllArgsConstructor
    public static class WorkflowFinishedEvent {
        private Enum attachedToType;
        private CassiniObject attachedToObject;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromStatus;
    }

    @Data
    @AllArgsConstructor
    public static class WorkflowHoldEvent {
        private Enum attachedToType;
        private CassiniObject attachedToObject;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus currentStatus;
    }

    @Data
    @AllArgsConstructor
    public static class WorkflowUnHoldEvent {
        private Enum attachedToType;
        private CassiniObject attachedToObject;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus currentStatus;
    }

    @Data
    @AllArgsConstructor
    public static class WorkflowActivityStartEvent {
        private Enum attachedToType;
        private CassiniObject attachedToObject;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus currentStatus;
    }

    @Data
    @AllArgsConstructor
    public static class WorkflowActivityFinishEvent {
        private Enum attachedToType;
        private CassiniObject attachedToObject;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus currentStatus;
    }

    @Data
    @AllArgsConstructor
    public static class AttachedToObjectDeletedEvent {
        private Integer attachedTo;
    }

    @Data
    @AllArgsConstructor
    public static class PromotePluginEvent {
        private String name;
        private Integer id;
        private PLMWorkflowStatus currentStatus;
    }

    @Data
    @AllArgsConstructor
    public static class ProjectActivityTaskWorkflowDeletedEvent {
        private Integer project;
    }

    @Data
    @AllArgsConstructor
    public static class ProgramProjectActivityTaskWorkflowDeletedEvent {
        private Integer program;
    }
}
