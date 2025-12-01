package com.cassinisys.plm.event;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.plm.model.cm.*;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class DCOEvents {

    @Data
    @AllArgsConstructor
    public static class DCOCreatedEvent {
        private PLMDCO dco;
    }

    @Data
    @AllArgsConstructor
    public static class DCOBasicInfoUpdatedEvent {
        private PLMDCO olddco;
        private PLMDCO dco;

    }

    @Data
    @AllArgsConstructor
    public static class DCOAttributesUpdatedEvent {
        private PLMDCO dco;
        private PLMChangeAttribute oldAttribute;
        private PLMChangeAttribute newAttribute;
    }

    // Inspection files event
    @Data
    @AllArgsConstructor
    public static class DCOFilesAddedEvent {
        private PLMDCO dco;
        private List<PLMChangeFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class DCOFoldersAddedEvent {
        private PLMDCO dco;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class DCOFoldersDeletedEvent {
        private PLMDCO dco;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class DCOFileDeletedEvent {
        private PLMDCO dco;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class DCOFilesVersionedEvent {
        private PLMDCO dco;
        private List<PLMChangeFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class DCOFileRenamedEvent {
        private PLMDCO dco;
        private PLMFile oldFile;
        private PLMFile newFile;
        private String type;
    }

    @Data
    @AllArgsConstructor
    public static class DCOFileLockedEvent {
        private PLMDCO dco;
        private PLMFile changeFile;
    }

    @Data
    @AllArgsConstructor
    public static class DCOFileUnlockedEvent {
        private PLMDCO dco;
        private PLMFile changeFile;
    }

    @Data
    @AllArgsConstructor
    public static class DCOFileDownloadedEvent {
        private PLMDCO dco;
        private PLMFile changeFile;
    }

    // Inspection workflow events
    @Data
    @AllArgsConstructor
    public static class DCOWorkflowStartedEvent {
        private PLMDCO dco;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class DCOWorkflowPromotedEvent {
        private PLMDCO dco;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class DCOWorkflowDemotedEvent {
        private PLMDCO dco;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class DCOWorkflowFinishedEvent {
        private PLMDCO dco;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class DCOWorkflowHoldEvent {
        private PLMDCO dco;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class DCOWorkflowUnholdEvent {
        private PLMDCO dco;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class DCOWorkflowChangeEvent {
        private PLMDCO dco;
        private PLMWorkflow oldWorkflow;
        private PLMWorkflow workflow;
    }

    // Comment event
    @Data
    @AllArgsConstructor
    public static class DCOCommentAddedEvent {
        private PLMDCO dco;
        private Comment comment;
    }

    @Data
    @AllArgsConstructor
    public static class DCOAffectedItemAddedEvent {
        private PLMDCO dco;
        private PLMDCOAffectedItem item;
    }

    @Data
    @AllArgsConstructor
    public static class DCOAffectedItemDeletedEvent {
        private PLMDCO dco;
        private PLMDCOAffectedItem item;
    }

    @Data
    @AllArgsConstructor
    public static class DCORelatedItemAddedEvent {
        private PLMDCO dco;
        private List<PLMChangeRelatedItem> relatedItems;
    }

    @Data
    @AllArgsConstructor
    public static class DCORelatedItemDeletedEvent {
        private PLMDCO dco;
        private PLMChangeRelatedItem relatedItem;
    }

    @Data
    @AllArgsConstructor
    public static class DCOChangeRequestAddedEvent {
        private PLMDCO dco;
        private List<PLMDCR> dcrList;
    }

    @Data
    @AllArgsConstructor
    public static class DCOChangeRequestDeletedEvent {
        private PLMDCO dco;
        private PLMDCR dcr;
    }
}
