package com.cassinisys.plm.event;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.pqm.PQMNCR;
import com.cassinisys.plm.model.pqm.PQMNCRAttribute;
import com.cassinisys.plm.model.pqm.PQMNCRProblemItem;
import com.cassinisys.plm.model.pqm.PQMNCRRelatedItem;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class NCREvents {

    @Data
    @AllArgsConstructor
    public static class NCRCreatedEvent {
        private PQMNCR ncr;
    }

    @Data
    @AllArgsConstructor
    public static class NCRBasicInfoUpdatedEvent {
        private PQMNCR oldNcr;
        private PQMNCR ncr;

    }

    @Data
    @AllArgsConstructor
    public static class NCRAttributesUpdatedEvent {
        private PQMNCR ncr;
        private PQMNCRAttribute oldAttribute;
        private PQMNCRAttribute newAttribute;
    }

    // NCR files event
    @Data
    @AllArgsConstructor
    public static class NCRFilesAddedEvent {
        private PQMNCR ncr;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class NCRFoldersAddedEvent {
        private PQMNCR ncr;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class NCRFoldersDeletedEvent {
        private PQMNCR ncr;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class NCRFileDeletedEvent {
        private PQMNCR ncr;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class NCRFilesVersionedEvent {
        private PQMNCR ncr;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class NCRFileRenamedEvent {
        private PQMNCR ncr;
        private PLMFile oldFile;
        private PLMFile newFile;
        private String type;
    }

    @Data
    @AllArgsConstructor
    public static class NCRFileLockedEvent {
        private PQMNCR ncr;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class NCRFileUnlockedEvent {
        private PQMNCR ncr;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class NCRFileDownloadedEvent {
        private PQMNCR ncr;
        private PLMFile file;
    }

    // NCR workflow events
    @Data
    @AllArgsConstructor
    public static class NCRWorkflowStartedEvent {
        private PQMNCR ncr;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class NCRWorkflowPromotedEvent {
        private PQMNCR ncr;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class NCRWorkflowDemotedEvent {
        private PQMNCR ncr;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class NCRWorkflowFinishedEvent {
        private PQMNCR ncr;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class NCRWorkflowHoldEvent {
        private PQMNCR ncr;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class NCRWorkflowUnholdEvent {
        private PQMNCR ncr;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class NCRWorkflowChangeEvent {
        private PQMNCR ncr;
        private PLMWorkflow oldWorkflow;
        private PLMWorkflow workflow;
    }

    // Comment event
    @Data
    @AllArgsConstructor
    public static class NCRCommentAddedEvent {
        private PQMNCR ncr;
        private Comment comment;
    }

    @Data
    @AllArgsConstructor
    public static class NCRProblemItemAddedEvent {
        private PQMNCR ncr;
        private List<PQMNCRProblemItem> problemItems;
    }

    @Data
    @AllArgsConstructor
    public static class NCRProblemItemUpdatedEvent {
        private PQMNCR ncr;
        private PQMNCRProblemItem oldProblemItem;
        private PQMNCRProblemItem problemItem;
    }

    @Data
    @AllArgsConstructor
    public static class NCRProblemItemDeletedEvent {
        private PQMNCR ncr;
        private PQMNCRProblemItem problemItem;
    }

    @Data
    @AllArgsConstructor
    public static class NCRRelatedItemAddedEvent {
        private PQMNCR ncr;
        private List<PQMNCRRelatedItem> relatedItems;
    }

    @Data
    @AllArgsConstructor
    public static class NCRRelatedItemUpdatedEvent {
        private PQMNCR ncr;
        private PQMNCRRelatedItem oldRelatedItem;
        private PQMNCRRelatedItem relatedItem;
    }

    @Data
    @AllArgsConstructor
    public static class NCRRelatedItemDeletedEvent {
        private PQMNCR ncr;
        private PQMNCRRelatedItem relatedItem;
    }
}
