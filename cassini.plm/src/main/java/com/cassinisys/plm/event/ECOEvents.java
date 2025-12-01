package com.cassinisys.plm.event;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.plm.model.cm.*;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class ECOEvents {

    @Data
    @AllArgsConstructor
    public static class ECOCreatedEvent {
        private PLMECO eco;
    }

    @Data
    @AllArgsConstructor
    public static class ECOBasicInfoUpdatedEvent {
        private PLMECO oldEco;
        private PLMECO eco;

    }

    @Data
    @AllArgsConstructor
    public static class ECOAttributesUpdatedEvent {
        private PLMECO eco;
        private PLMChangeAttribute oldAttribute;
        private PLMChangeAttribute newAttribute;
    }

    // NCR files event
    @Data
    @AllArgsConstructor
    public static class ECOFilesAddedEvent {
        private PLMECO eco;
        private List<PLMChangeFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class ECOFoldersAddedEvent {
        private PLMECO eco;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ECOFoldersDeletedEvent {
        private PLMECO eco;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ECOFileDeletedEvent {
        private PLMECO eco;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ECOFilesVersionedEvent {
        private PLMECO eco;
        private List<PLMChangeFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class ECOFileRenamedEvent {
        private PLMECO eco;
        private PLMFile oldFile;
        private PLMFile newFile;
        private String type;
    }

    @Data
    @AllArgsConstructor
    public static class ECOFileLockedEvent {
        private PLMECO eco;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ECOFileUnlockedEvent {
        private PLMECO eco;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ECOFileDownloadedEvent {
        private PLMECO eco;
        private PLMFile file;
    }

    // NCR workflow events
    @Data
    @AllArgsConstructor
    public static class ECOWorkflowStartedEvent {
        private PLMECO eco;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class ECOWorkflowPromotedEvent {
        private PLMECO eco;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class ECOWorkflowDemotedEvent {
        private PLMECO eco;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class ECOWorkflowFinishedEvent {
        private PLMECO eco;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class ECOWorkflowHoldEvent {
        private PLMECO eco;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class ECOWorkflowUnholdEvent {
        private PLMECO eco;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class ECOWorkflowChangeEvent {
        private PLMECO eco;
        private PLMWorkflow oldWorkflow;
        private PLMWorkflow workflow;
    }

    // Comment event
    @Data
    @AllArgsConstructor
    public static class ECOCommentAddedEvent {
        private PLMECO eco;
        private Comment comment;
    }

    @Data
    @AllArgsConstructor
    public static class ECOAffectedItemAddedEvent {
        private PLMECO eco;
        private List<PLMAffectedItem> affectedItems;
    }

    @Data
    @AllArgsConstructor
    public static class ECOAffectedItemUpdatedEvent {
        private PLMECO eco;
        private PLMAffectedItem oldAffectedItem;
        private PLMAffectedItem affectedItem;
    }

    @Data
    @AllArgsConstructor
    public static class ECOAffectedItemDeletedEvent {
        private PLMECO eco;
        private PLMAffectedItem affectedItem;
    }

    @Data
    @AllArgsConstructor
    public static class ECOChangeRequestAddedEvent {
        private PLMECO eco;
        private List<PLMECR> ecrList;
    }

    @Data
    @AllArgsConstructor
    public static class ECOChangeRequestDeletedEvent {
        private PLMECO eco;
        private PLMECR ecr;
    }
}
