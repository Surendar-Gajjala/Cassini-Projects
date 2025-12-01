package com.cassinisys.plm.event;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.plm.model.cm.*;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class DCREvents {

    @Data
    @AllArgsConstructor
    public static class DCRCreatedEvent {
        private PLMDCR dcr;
    }

    @Data
    @AllArgsConstructor
    public static class DCRBasicInfoUpdatedEvent {
        private PLMDCR olddcr;
        private PLMDCR dcr;

    }

    @Data
    @AllArgsConstructor
    public static class DCRAttributesUpdatedEvent {
        private PLMDCR dcr;
        private PLMChangeAttribute oldAttribute;
        private PLMChangeAttribute newAttribute;
    }

    // Inspection files event
    @Data
    @AllArgsConstructor
    public static class DCRFilesAddedEvent {
        private PLMDCR dcr;
        private List<PLMChangeFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class DCRFoldersAddedEvent {
        private PLMDCR dcr;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class DCRFoldersDeletedEvent {
        private PLMDCR dcr;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class DCRFileDeletedEvent {
        private PLMDCR dcr;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class DCRFilesVersionedEvent {
        private PLMDCR dcr;
        private List<PLMChangeFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class DCRFileRenamedEvent {
        private PLMDCR dcr;
        private PLMFile oldFile;
        private PLMFile newFile;
        private String type;
    }

    @Data
    @AllArgsConstructor
    public static class DCRFileLockedEvent {
        private PLMDCR dcr;
        private PLMFile dcrFile;
    }

    @Data
    @AllArgsConstructor
    public static class DCRFileUnlockedEvent {
        private PLMDCR dcr;
        private PLMFile changeFile;
    }

    @Data
    @AllArgsConstructor
    public static class DCRFileDownloadedEvent {
        private PLMDCR dcr;
        private PLMFile changeFile;
    }

    // Inspection workflow events
    @Data
    @AllArgsConstructor
    public static class DCRWorkflowStartedEvent {
        private PLMDCR dcr;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class DCRWorkflowPromotedEvent {
        private PLMDCR dcr;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class DCRWorkflowDemotedEvent {
        private PLMDCR dcr;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class DCRWorkflowFinishedEvent {
        private PLMDCR dcr;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class DCRWorkflowHoldEvent {
        private PLMDCR dcr;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class DCRWorkflowUnholdEvent {
        private PLMDCR dcr;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class DCRWorkflowChangeEvent {
        private PLMDCR dcr;
        private PLMWorkflow oldWorkflow;
        private PLMWorkflow workflow;
    }

    // Comment event
    @Data
    @AllArgsConstructor
    public static class DCRCommentAddedEvent {
        private PLMDCR dcr;
        private Comment comment;
    }

    @Data
    @AllArgsConstructor
    public static class DCRAffectedItemAddedEvent {
        private PLMDCR dcr;
        private List<PLMDCRAffectedItem> items;
    }

    @Data
    @AllArgsConstructor
    public static class DCRAffectedItemDeletedEvent {
        private PLMDCR dcr;
        private PLMDCRAffectedItem item;
    }

    @Data
    @AllArgsConstructor
    public static class DCRRelatedItemAddedEvent {
        private PLMDCR dcr;
        private List<PLMChangeRelatedItem> relatedItems;
    }

    @Data
    @AllArgsConstructor
    public static class DCRRelatedItemDeletedEvent {
        private PLMDCR dcr;
        private PLMChangeRelatedItem relatedItem;
    }
}
