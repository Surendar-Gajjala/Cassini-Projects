package com.cassinisys.plm.event;

import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.req.*;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class RequirementEvents {

    @Data
    @AllArgsConstructor
    public static class RequirementCreatedEvent {
        private PLMRequirement requirement;
        private PLMRequirementDocumentChildren requirementDocumentChildren;
        private PLMRequirementVersion requirementVersion;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementBasicInfoUpdatedEvent {
        private PLMRequirement plmOldRequirement;
        private PLMRequirement requirement;
        private PLMRequirementDocumentChildren requirementDocumentChildren;
        private PLMRequirementVersion oldVersion;
        private PLMRequirementVersion version;
    }


    @Data
    @AllArgsConstructor
    public static class RequirementAttributesUpdatedEvent {
        private PLMRequirement requirement;
        private PLMRequirementObjectAttribute oldAttribute;
        private PLMRequirementObjectAttribute newAttribute;
    }

    // Requirement files event
    @Data
    @AllArgsConstructor
    public static class RequirementFilesAddedEvent {
        private PLMRequirementVersion requirementVersion;
        private  PLMRequirementDocumentChildren documentChildren;
        private List<PLMRequirementFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementFileDeletedEvent {
        private PLMRequirementVersion requirementVersion;
        private PLMRequirementDocumentChildren documentChildren;
        private PLMRequirementFile file;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementFilesVersionedEvent {
        private PLMRequirementVersion requirementVersion;
        private PLMRequirementDocumentChildren documentChildren;
        private List<PLMRequirementFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementFileRenamedEvent {
        private PLMRequirementVersion requirementVersion;
        private PLMRequirementDocumentChildren documentChildren;
        private PLMRequirementFile oldFile;
        private PLMRequirementFile newFile;
        private String type;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementFileLockedEvent {
        private PLMRequirementVersion requirementVersion;
        private PLMRequirementFile requirementFile;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementFileUnlockedEvent {
        private PLMRequirementVersion requirementVersion;
        private PLMRequirementFile requirementFile;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementFileDownloadedEvent {
        private PLMRequirementVersion requirementVersion;
        private PLMRequirementFile requirementFile;
    }


    @Data
    @AllArgsConstructor
    public static class RequirementFoldersAddedEvent {
        private PLMRequirementVersion requirementVersion;
        private PLMRequirementDocumentChildren documentChildren;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementFoldersDeletedEvent {
        private PLMRequirementVersion requirementVersion;
        private PLMRequirementDocumentChildren documentChildren;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementReviewerAddedEvent {
        private PLMRequirementVersion requirementVersion;
        private PLMRequirementDocumentChildren requirementDocumentChildren;
        private PLMRequirementReviewer reviewer;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementReviewerUpdateEvent {
        private PLMRequirementVersion requirementVersion;
        private PLMRequirementDocumentChildren requirementDocumentChildren;
        private PLMRequirementReviewer reviewer;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementReviewerDeletedEvent {
        private PLMRequirementVersion requirementVersion;
        private PLMRequirementDocumentChildren requirementDocumentChildren;
        private PLMRequirementReviewer reviewer;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementReviewerApprovedEvent {
        private PLMRequirementVersion requirementVersion;
        private PLMRequirementDocumentChildren requirementDocumentChildren;
        private PLMRequirementReviewer reviewer;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementItemAddedEvent {
        private PLMRequirementVersion requirementVersion;
        private PLMRequirementDocumentChildren requirementDocumentChildren;
        private List<PLMRequirementItem> requirementItems;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementItemDeletedEvent {
        private PLMRequirementDocumentChildren requirementDocumentChildren;
        private PLMRequirementVersion requirementVersion;
        private PLMRequirementItem requirementItem;
    }
    @Data
    @AllArgsConstructor
    public static class RequirementWorkflowStartedEvent {
        private PLMRequirementDocumentChildren requirementDocumentChildren;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementWorkflowPromotedEvent {
        private PLMRequirementDocumentChildren requirementDocumentChildren;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementWorkflowDemotedEvent {
        private PLMRequirementDocumentChildren requirementDocumentChildren;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementWorkflowFinishedEvent {
        private PLMRequirementDocumentChildren requirementDocumentChildren;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementWorkflowHoldEvent {
        private PLMRequirementDocumentChildren requirementDocumentChildren;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class RequirementWorkflowUnholdEvent {
        private PLMRequirementDocumentChildren requirementDocumentChildren;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }
}
