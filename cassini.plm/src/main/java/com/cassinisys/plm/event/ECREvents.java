package com.cassinisys.plm.event;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.plm.model.cm.*;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class ECREvents {

    @Data
    @AllArgsConstructor
    public static class ECRCreatedEvent {
        private PLMECR ecr;
    }

    @Data
    @AllArgsConstructor
    public static class ECRBasicInfoUpdatedEvent {
        private PLMECR oldEcr;
        private PLMECR ecr;

    }

    @Data
    @AllArgsConstructor
    public static class ECRAttributesUpdatedEvent {
        private PLMECR ecr;
        private PLMChangeAttribute oldAttribute;
        private PLMChangeAttribute newAttribute;
    }

    // NCR files event
    @Data
    @AllArgsConstructor
    public static class ECRFilesAddedEvent {
        private PLMECR ecr;
        private List<PLMChangeFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class ECRFoldersAddedEvent {
        private PLMECR ecr;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ECRFoldersDeletedEvent {
        private PLMECR ecr;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ECRFileDeletedEvent {
        private PLMECR ecr;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ECRFilesVersionedEvent {
        private PLMECR ecr;
        private List<PLMChangeFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class ECRFileRenamedEvent {
        private PLMECR ecr;
        private PLMFile oldFile;
        private PLMFile newFile;
        private String type;
    }

    @Data
    @AllArgsConstructor
    public static class ECRFileLockedEvent {
        private PLMECR ecr;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ECRFileUnlockedEvent {
        private PLMECR ecr;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ECRFileDownloadedEvent {
        private PLMECR ecr;
        private PLMFile file;
    }

    // NCR workflow events
    @Data
    @AllArgsConstructor
    public static class ECRWorkflowStartedEvent {
        private PLMECR ecr;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class ECRWorkflowPromotedEvent {
        private PLMECR ecr;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class ECRWorkflowDemotedEvent {
        private PLMECR ecr;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class ECRWorkflowFinishedEvent {
        private PLMECR ecr;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class ECRWorkflowHoldEvent {
        private PLMECR ecr;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class ECRWorkflowUnholdEvent {
        private PLMECR ecr;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class ECRWorkflowChangeEvent {
        private PLMECR ecr;
        private PLMWorkflow oldWorkflow;
        private PLMWorkflow workflow;
    }

    // Comment event
    @Data
    @AllArgsConstructor
    public static class ECRCommentAddedEvent {
        private PLMECR ecr;
        private Comment comment;
    }

    @Data
    @AllArgsConstructor
    public static class ECRAffectedItemAddedEvent {
        private PLMECR ecr;
        private List<PLMECRAffectedItem> affectedItems;
    }

    @Data
    @AllArgsConstructor
    public static class ECRAffectedItemDeletedEvent {
        private PLMECR ecr;
        private PLMECRAffectedItem affectedItem;
    }

    @Data
    @AllArgsConstructor
    public static class ECRRelatedItemsAddedEvent {
        private PLMECR ecr;
        private List<PLMChangeRelatedItem> changeRelatedItems;
    }

    @Data
    @AllArgsConstructor
    public static class ECRRelatedItemsDeletedEvent {
        private PLMECR ecr;
        private PLMChangeRelatedItem changeRelatedItem;
    }

    // ECR Problem Report Events

    @Data
    @AllArgsConstructor
    public static class ECRProblemReportAddedEvent {
        private PLMECR ecr;
        private List<PLMECRPR> problemReports;
    }

    @Data
    @AllArgsConstructor
    public static class ECRProblemReportDeletedEvent {
        private PLMECR ecr;
        private PLMECRPR problemReport;
    }
}
