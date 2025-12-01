package com.cassinisys.plm.event;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.plm.model.cm.*;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class MCOEvents {

    @Data
    @AllArgsConstructor
    public static class MCOCreatedEvent {
        private PLMMCO mco;
    }

    @Data
    @AllArgsConstructor
    public static class MCOBasicInfoUpdatedEvent {
        private PLMMCO oldMco;
        private PLMMCO mco;

    }

    @Data
    @AllArgsConstructor
    public static class MCOAttributesUpdatedEvent {
        private PLMMCO mco;
        private PLMChangeAttribute oldAttribute;
        private PLMChangeAttribute newAttribute;
    }

    // NCR files event
    @Data
    @AllArgsConstructor
    public static class MCOFilesAddedEvent {
        private PLMMCO mco;
        private List<PLMChangeFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class MCOFoldersAddedEvent {
        private PLMMCO mco;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class MCOFoldersDeletedEvent {
        private PLMMCO mco;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class MCOFileDeletedEvent {
        private PLMMCO mco;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class MCOFilesVersionedEvent {
        private PLMMCO mco;
        private List<PLMChangeFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class MCOFileRenamedEvent {
        private PLMMCO mco;
        private PLMFile oldFile;
        private PLMFile newFile;
        private String type;
    }

    @Data
    @AllArgsConstructor
    public static class MCOFileLockedEvent {
        private PLMMCO mco;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class MCOFileUnlockedEvent {
        private PLMMCO mco;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class MCOFileDownloadedEvent {
        private PLMMCO mco;
        private PLMFile file;
    }

    // NCR workflow events
    @Data
    @AllArgsConstructor
    public static class MCOWorkflowStartedEvent {
        private PLMMCO mco;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class MCOWorkflowPromotedEvent {
        private PLMMCO mco;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class MCOWorkflowDemotedEvent {
        private PLMMCO mco;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class MCOWorkflowFinishedEvent {
        private PLMMCO mco;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class MCOWorkflowHoldEvent {
        private PLMMCO mco;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class MCOWorkflowUnholdEvent {
        private PLMMCO mco;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class MCOWorkflowChangeEvent {
        private PLMMCO mco;
        private PLMWorkflow oldWorkflow;
        private PLMWorkflow workflow;
    }

    // Comment event
    @Data
    @AllArgsConstructor
    public static class MCOCommentAddedEvent {
        private PLMMCO mco;
        private Comment comment;
    }

    @Data
    @AllArgsConstructor
    public static class MCOAffectedItemAddedEvent {
        private PLMMCO mco;
        private List<PLMMCOAffectedItem> affectedItems;
    }

    @Data
    @AllArgsConstructor
    public static class MCOAffectedItemDeletedEvent {
        private PLMMCO mco;
        private PLMMCOAffectedItem affectedItem;
    }

    @Data
    @AllArgsConstructor
    public static class MCORelatedItemAddedEvent {
        private PLMMCO mco;
        private List<PLMMCORelatedItem> relatedItems;
    }

    @Data
    @AllArgsConstructor
    public static class MCORelatedItemDeletedEvent {
        private PLMMCO mco;
        private PLMMCORelatedItem relatedItem;
    }

    @Data
    @AllArgsConstructor
    public static class MCOAffectedItemUpdatedEvent {
        private PLMMCO mco;
        private PLMMCOAffectedItem oldAffectedItem;
        private PLMMCOAffectedItem affectedItem;
    }

    @Data
    @AllArgsConstructor
    public static class MCORelatedItemUpdatedEvent {
        private PLMMCO mco;
        private PLMMCORelatedItem oldRelatedItem;
        private PLMMCORelatedItem relatedItem;
    }

    @Data
    @AllArgsConstructor
    public static class MCOProductAffectedItemAddedEvent {
        private PLMMCO mco;
        private List<PLMMCOProductAffectedItem> productAffectedItems;
    }

    @Data
    @AllArgsConstructor
    public static class MCOProductAffectedItemDeletedEvent {
        private PLMMCO mco;
        private PLMMCOProductAffectedItem productAffectedItem;
    }

    @Data
    @AllArgsConstructor
    public static class MCOProductAffectedItemUpdatedEvent {
        private PLMMCO mco;
        private PLMMCOProductAffectedItem oldProductAffectedItem;
        private PLMMCOProductAffectedItem productAffectedItem;
    }
}
