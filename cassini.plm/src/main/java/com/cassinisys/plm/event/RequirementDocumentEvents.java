package com.cassinisys.plm.event;

import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.req.*;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class RequirementDocumentEvents {

    // RequirementDocument events
    public interface RequirementDocumentBaseEvent {
        PLMRequirementDocumentRevision getRequirementDocumentRevision();
    }

    @Data
    @AllArgsConstructor
    public static class RequirementDocumentCreatedEvent implements RequirementDocumentBaseEvent {
        private PLMRequirementDocument requirementDocument;
        private PLMRequirementDocumentRevision requirementDocumentRevision;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementDocumentRevisionCreatedEvent implements RequirementDocumentBaseEvent {
        private PLMRequirementDocument requirementDocument;
        private PLMRequirementDocumentRevision requirementDocumentRevision;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementDocumentBasicInfoUpdatedEvent implements RequirementDocumentBaseEvent {
        private PLMRequirementDocument plmOldRequirementDocument;
        private PLMRequirementDocument requirementDocument;
        private PLMRequirementDocumentRevision requirementDocumentRevision;
    }


    @Data
    @AllArgsConstructor
    public static class RequirementDocumentAttributesUpdatedEvent implements RequirementDocumentBaseEvent {
        private PLMRequirementDocument requirementDocument;
        private PLMRequirementDocumentRevision requirementDocumentRevision;
        private PLMRequirementObjectAttribute oldAttribute;
        private PLMRequirementObjectAttribute newAttribute;
    }

    // RequirementDocument files event
    @Data
    @AllArgsConstructor
    public static class RequirementDocumentFilesAddedEvent implements RequirementDocumentBaseEvent {
        private PLMRequirementDocumentRevision requirementDocumentRevision;
        private List<PLMRequirementDocumentFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementDocumentFileDeletedEvent implements RequirementDocumentBaseEvent {
        private PLMRequirementDocumentRevision requirementDocumentRevision;
        private PLMRequirementDocumentFile file;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementDocumentFilesVersionedEvent implements RequirementDocumentBaseEvent {
        private PLMRequirementDocumentRevision requirementDocumentRevision;
        private List<PLMRequirementDocumentFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementDocumentFileRenamedEvent implements RequirementDocumentBaseEvent {
        private PLMRequirementDocumentRevision requirementDocumentRevision;
        private PLMRequirementDocumentFile oldFile;
        private PLMRequirementDocumentFile newFile;
        private String type;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementDocumentFileLockedEvent implements RequirementDocumentBaseEvent {
        private PLMRequirementDocumentRevision requirementDocumentRevision;
        private PLMRequirementDocumentFile requirementDocumentFile;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementDocumentFileUnlockedEvent implements RequirementDocumentBaseEvent {
        private PLMRequirementDocumentRevision requirementDocumentRevision;
        private PLMRequirementDocumentFile requirementDocumentFile;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementDocumentFileDownloadedEvent implements RequirementDocumentBaseEvent {
        private PLMRequirementDocumentRevision requirementDocumentRevision;
        private PLMRequirementDocumentFile requirementDocumentFile;
    }


    @Data
    @AllArgsConstructor
    public static class RequirementDocumentFoldersAddedEvent implements RequirementDocumentBaseEvent {
        private PLMRequirementDocumentRevision requirementDocumentRevision;
        private PLMRequirementDocumentFile file;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementDocumentFoldersDeletedEvent implements RequirementDocumentBaseEvent {
        private PLMRequirementDocumentRevision requirementDocumentRevision;
        private PLMRequirementDocumentFile file;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementDocumentPromotedEvent {
        private PLMRequirementDocument requirementDocument;
        private PLMRequirementDocumentRevision requirementDocumentRevision;
        private PLMLifeCyclePhase fromLifeCyclePhase;
        private PLMLifeCyclePhase toLifeCyclePhase;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementDocumentDemotedEvent {
        private PLMRequirementDocument requirementDocument;
        private PLMRequirementDocumentRevision requirementDocumentRevision;
        private PLMLifeCyclePhase fromLifeCyclePhase;
        private PLMLifeCyclePhase toLifeCyclePhase;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementDocumentWorkflowStartedEvent {
        private PLMRequirementDocumentRevision requirementDocumentRevision;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementDocumentWorkflowPromotedEvent {
        private PLMRequirementDocumentRevision requirementDocumentRevision;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementDocumentWorkflowDemotedEvent {
        private PLMRequirementDocumentRevision requirementDocumentRevision;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementDocumentWorkflowFinishedEvent {
        private PLMRequirementDocumentRevision requirementDocumentRevision;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementDocumentWorkflowHoldEvent {
        private PLMRequirementDocumentRevision requirementDocumentRevision;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementDocumentWorkflowUnholdEvent {
        private PLMRequirementDocumentRevision requirementDocumentRevision;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementCreatedEvent {
        private PLMRequirement requirement;
        private PLMRequirementDocumentRevision requirementDocumentRevision;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementDocumentReviewerAddedEvent {
        private PLMRequirementDocumentRevision requirementDocumentRevision;
        private PLMRequirementDocumentReviewer reviewer;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementDocumentReviewerUpdateEvent {
        private PLMRequirementDocumentRevision requirementDocumentRevision;
        private PLMRequirementDocumentReviewer reviewer;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementDocumentReviewerDeletedEvent {
        private PLMRequirementDocumentRevision requirementDocumentRevision;
        private PLMRequirementDocumentReviewer reviewer;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementDocumentApprovedEvent {
        private PLMRequirementDocumentRevision requirementDocumentRevision;
        private PLMRequirementDocumentReviewer reviewer;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementsApprovedEvent {
        private PLMRequirementDocumentRevision requirementDocumentRevision;
    }

}
