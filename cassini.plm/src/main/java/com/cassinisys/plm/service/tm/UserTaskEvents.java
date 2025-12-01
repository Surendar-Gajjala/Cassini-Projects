package com.cassinisys.plm.service.tm;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.plm.model.plm.PLMDocumentReviewer;
import com.cassinisys.plm.model.pm.PLMActivity;
import com.cassinisys.plm.model.pm.PLMProject;
import com.cassinisys.plm.model.pm.PLMTask;
import com.cassinisys.plm.model.pqm.PQMSupplierAuditReviewer;
import com.cassinisys.plm.model.req.PLMRequirementReviewer;
import com.cassinisys.plm.model.req.PLMRequirementVersion;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

public final class UserTaskEvents {

    @Data
    @AllArgsConstructor
    public static class ProjectActivityAssignedEvent {
        private PLMProject project;
        private PLMActivity activity;
    }

    @Data
    @AllArgsConstructor
    public static class ProjectActivityFinishedEvent {
        private PLMProject project;
        private PLMActivity activity;
    }

    @Data
    @AllArgsConstructor
    public static class ProjectActivityDeletedEvent {
        private PLMProject project;
        private PLMActivity activity;
    }

    @Data
    @AllArgsConstructor
    public static class ProjectTaskAssignedEvent {
        private PLMProject project;
        private PLMTask task;
    }

    @Data
    @AllArgsConstructor
    public static class ProjectTaskFinishedEvent {
        private PLMProject project;
        private PLMTask task;
    }

    @Data
    @AllArgsConstructor
    public static class ProjectTaskDeletedEvent {
        private PLMProject project;
        private PLMTask task;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementAssignedEvent {
        private PLMRequirementVersion requirementVersion;
        private PLMRequirementReviewer requirementReviewer;
    }

    @Data
    @AllArgsConstructor
    public static class DocumentAssignedEvent {
        private Integer document;
        private PLMDocumentReviewer documentReviewer;
    }

    @Data
    @AllArgsConstructor
    public static class SupplierAuditPlanAssignedEvent {
        private Integer supplierAuditPlan;
        private PQMSupplierAuditReviewer supplierAuditReviewer;
    }

    @Data
    @AllArgsConstructor
    public static class SupplierAuditPlanDeletedEvent {
        private Integer supplierAuditPlan;
        private PQMSupplierAuditReviewer supplierAuditReviewer;
    }

    @Data
    @AllArgsConstructor
    public static class SupplierAuditPlanSubmittedEvent {
        private Integer supplierAuditPlan;
        private PQMSupplierAuditReviewer supplierAuditReviewer;
    }


    @Data
    @AllArgsConstructor
    public static class DocumentSubmittedEvent {
        private Integer document;
        private PLMDocumentReviewer reviewer;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementFinishedEvent {
        private Integer requirement;
        private PLMRequirementReviewer requirementReviewer;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementDeletedEvent {
        private Integer requirement;
        private PLMRequirementReviewer requirementReviewer;
    }

    @Data
    @AllArgsConstructor
    public static class DocumentDeletedEvent {
        private Integer document;
        private PLMDocumentReviewer documentReviewer;
    }

    @Data
    @AllArgsConstructor
    public static class WorkflowTaskAssignedEvent {
        private Person assignedTo;
        private PLMWorkflow workflow;
        private PLMWorkflowStatus activity;
    }

    @Data
    @AllArgsConstructor
    public static class WorkflowTaskFinishedEvent {
        private Person assignedTo;
        private PLMWorkflow workflow;
        private PLMWorkflowStatus activity;
    }

    @Data
    @AllArgsConstructor
    public static class WorkflowTaskDeletedEvent {
        private Person assignedTo;
        private PLMWorkflow workflow;
        private PLMWorkflowStatus activity;
    }
}
