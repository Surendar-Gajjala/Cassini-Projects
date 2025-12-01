package com.cassinisys.plm.event;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.plm.model.mro.*;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class WorkOrderEvents {
    @Data
    @AllArgsConstructor
    public static class WorkOrderCreatedEvent {
        private MROWorkOrder workOrder;
    }

    @Data
    @AllArgsConstructor
    public static class WorkOrderBasicInfoUpdatedEvent {
        private MROWorkOrder oldWorkOrder;
        private MROWorkOrder newWorkOrder;
    }

    @Data
    @AllArgsConstructor
    public static class WorkOrderAttributesUpdatedEvent {
        private MROObject workOrder;
        private MROObjectAttribute oldAttribute;
        private MROObjectAttribute newAttribute;
    }

    @Data
    @AllArgsConstructor
    public static class WorkOrderOperationUpdatedEvent {
        private Integer maintenancePlan;
        private MROWorkOrderOperation oldWorkOrderOperation;
        private MROWorkOrderOperation workOrderOperation;
    }

    @Data
    @AllArgsConstructor
    public static class WorkOrderSparePartCreatedEvent {
        private Integer maintenancePlan;
        private List<MROWorkOrderPart> workOrderParts;
    }

    @Data
    @AllArgsConstructor
    public static class WorkOrderSparePartDeletedEvent {
        private Integer maintenancePlan;
        private MROWorkOrderPart workOrderPart;
    }

    @Data
    @AllArgsConstructor
    public static class WorkOrderSparePartUpdatedEvent {
        private Integer maintenancePlan;
        private MROWorkOrderPart oldWorkOrderPart;
        private MROWorkOrderPart workOrderPart;
    }

    // Comment event
    @Data
    @AllArgsConstructor
    public static class WorkOrderCommentAddedEvent {
        private MROWorkOrder workOrder;
        private Comment comment;
    }

    @Data
    @AllArgsConstructor
    public static class WorkOrderStartedEvent {
        private MROWorkOrder workOrder;
    }

    @Data
    @AllArgsConstructor
    public static class WorkOrderHoldEvent {
        private MROWorkOrder workOrder;
    }

    @Data
    @AllArgsConstructor
    public static class WorkOrderUnholdEvent {
        private MROWorkOrder workOrder;
    }

    @Data
    @AllArgsConstructor
    public static class WorkOrderFinishedEvent {
        private MROWorkOrder workOrder;
    }

    // workOrder workflow events
    @Data
    @AllArgsConstructor
    public static class WorkOrderWorkflowStartedEvent {
        private MROWorkOrder workOrder;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class WorkOrderWorkflowPromotedEvent {
        private MROWorkOrder workOrder;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class WorkOrderWorkflowDemotedEvent {
        private MROWorkOrder workOrder;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class WorkOrderWorkflowFinishedEvent {
        private MROWorkOrder workOrder;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class WorkOrderWorkflowHoldEvent {
        private MROWorkOrder workOrder;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class WorkOrderWorkflowUnholdEvent {
        private MROWorkOrder workOrder;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }
}