package com.cassinisys.plm.event;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.plm.model.mro.MROMaintenanceOperation;
import com.cassinisys.plm.model.mro.MROMaintenancePlan;
import com.cassinisys.plm.model.mro.MROObject;
import com.cassinisys.plm.model.mro.MROObjectAttribute;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class MaintenancePlanEvents {
    @Data
    @AllArgsConstructor
    public static class MaintenancePlanCreatedEvent {
        private MROMaintenancePlan maintenancePlan;
    }

    @Data
    @AllArgsConstructor
    public static class MaintenancePlanBasicInfoUpdatedEvent {
        private MROMaintenancePlan oldMaintenancePlan;
        private MROMaintenancePlan newMaintenancePlan;
    }

    @Data
    @AllArgsConstructor
    public static class MaintenancePlanAttributesUpdatedEvent {
        private MROObject maintenancePlan;
        private MROObjectAttribute oldAttribute;
        private MROObjectAttribute newAttribute;
    }

    @Data
    @AllArgsConstructor
    public static class MaintenancePlanOperationCreatedEvent {
        private Integer maintenancePlan;
        private List<MROMaintenanceOperation> maintenanceOperations;
    }

    @Data
    @AllArgsConstructor
    public static class MaintenancePlanOperationDeletedEvent {
        private Integer maintenancePlan;
        private MROMaintenanceOperation maintenanceOperation;
    }

    @Data
    @AllArgsConstructor
    public static class MaintenancePlanOperationUpdatedEvent {
        private Integer maintenancePlan;
        private MROMaintenanceOperation oldMaintenanceOperation;
        private MROMaintenanceOperation maintenanceOperation;
    }

    // Comment event
    @Data
    @AllArgsConstructor
    public static class MaintenancePlanCommentAddedEvent {
        private MROMaintenancePlan maintenancePlan;
        private Comment comment;
    }


    // maintenancePlan workflow events
    @Data
    @AllArgsConstructor
    public static class MaintenancePlanWorkflowStartedEvent {
        private MROMaintenancePlan maintenancePlan;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class MaintenancePlanWorkflowPromotedEvent {
        private MROMaintenancePlan maintenancePlan;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class MaintenancePlanWorkflowDemotedEvent {
        private MROMaintenancePlan maintenancePlan;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class MaintenancePlanWorkflowFinishedEvent {
        private MROMaintenancePlan maintenancePlan;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class MaintenancePlanWorkflowHoldEvent {
        private MROMaintenancePlan maintenancePlan;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class MaintenancePlanWorkflowUnholdEvent {
        private MROMaintenancePlan maintenancePlan;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }
}