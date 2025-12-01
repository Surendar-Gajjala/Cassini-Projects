package com.cassinisys.plm.event;

import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.pqm.PQMSupplierAudit;
import com.cassinisys.plm.model.pqm.PQMSupplierAuditAttribute;
import com.cassinisys.plm.model.pqm.PQMSupplierAuditPlan;
import com.cassinisys.plm.model.pqm.PQMSupplierAuditReviewer;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class SupplierAuditEvents {
    @Data
    @AllArgsConstructor
    public static class SupplierAuditCreatedEvent {
        private PQMSupplierAudit supplierAudit;
    }

    @Data
    @AllArgsConstructor
    public static class SupplierAuditBasicInfoUpdatedEvent {
        private PQMSupplierAudit oldSupplierAudit;
        private PQMSupplierAudit newSupplierAudit;
    }

    @Data
    @AllArgsConstructor
    public static class SupplieAuditAttributesUpdatedEvent {
        private PQMSupplierAudit supplierAudit;
        private PQMSupplierAuditAttribute oldAttribute;
        private PQMSupplierAuditAttribute newAttribute;
    }

    // SupplierAudit files event
    @Data
    @AllArgsConstructor
    public static class SupplierAuditFilesAddedEvent {
        private Integer supplierAudit;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class SupplierAuditPlansAddedEvent {
        private Integer supplierAudit;
        private List<PQMSupplierAuditPlan> auditPlans;
    }

    @Data
    @AllArgsConstructor
    public static class SupplierAuditPlanUpdatedEvent {
        private Integer supplierAudit;
        private PQMSupplierAuditPlan oldAuditPlan;
        private PQMSupplierAuditPlan auditPlan;
    }

    @Data
    @AllArgsConstructor
    public static class SupplierAuditPlanDeletedEvent {
        private Integer supplierAudit;
        private PQMSupplierAuditPlan auditPlan;
    }

    @Data
    @AllArgsConstructor
    public static class SupplierAuditFoldersAddedEvent {
        private Integer supplierAudit;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class SupplierAuditFoldersDeletedEvent {
        private Integer supplierAudit;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class SupplierAuditFileDeletedEvent {
        private Integer supplierAudit;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class SupplierAuditFilesVersionedEvent {
        private Integer supplierAudit;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class SupplierAuditFileRenamedEvent {
        private Integer supplierAudit;
        private PLMFile oldFile;
        private PLMFile newFile;
        private String type;
    }

    @Data
    @AllArgsConstructor
    public static class SupplierAuditFileLockedEvent {
        private Integer supplierAudit;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class SupplierAuditFileUnlockedEvent {
        private Integer supplierAudit;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class SupplierAuditFileDownloadedEvent {
        private Integer supplierAudit;
        private PLMFile file;
    }

    // SupplierAudit workflow events

    @Data
    @AllArgsConstructor
    public static class SupplierAuditWorkflowStartedEvent {
        private PQMSupplierAudit supplierAudit;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class SupplierAuditWorkflowPromotedEvent {
        private PQMSupplierAudit supplierAudit;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class SupplierAuditWorkflowDemotedEvent {
        private PQMSupplierAudit supplierAudit;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class SupplierAuditWorkflowFinishedEvent {
        private PQMSupplierAudit supplierAudit;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class SupplierAuditWorkflowHoldEvent {
        private PQMSupplierAudit supplierAudit;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class SupplierAuditWorkflowUnholdEvent {
        private PQMSupplierAudit supplierAudit;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class SupplierAuditWorkflowChangeEvent {
        private PQMSupplierAudit supplierAudit;
        private PLMWorkflow oldWorkflow;
        private PLMWorkflow workflow;
    }

    @Data
    @AllArgsConstructor
    public static class SupplierAuditPlanReviewerAddedEvent {
        private PQMSupplierAuditPlan auditPlan;
        private PQMSupplierAuditReviewer reviewer;
    }

    @Data
    @AllArgsConstructor
    public static class SupplierAuditPlanReviewerUpdateEvent {
        private PQMSupplierAuditPlan auditPlan;
        private PQMSupplierAuditReviewer reviewer;
    }

    @Data
    @AllArgsConstructor
    public static class SupplierAuditPlanReviewerDeletedEvent {
        private PQMSupplierAuditPlan auditPlan;
        private PQMSupplierAuditReviewer reviewer;
    }

    @Data
    @AllArgsConstructor
    public static class SupplierAuditPlanReviewerSubmittedEvent {
        private PQMSupplierAuditPlan auditPlan;
        private PQMSupplierAuditReviewer reviewer;
    }


}
