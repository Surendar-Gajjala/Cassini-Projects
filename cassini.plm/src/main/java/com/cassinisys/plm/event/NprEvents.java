package com.cassinisys.plm.event;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.plm.model.plm.PLMNpr;
import com.cassinisys.plm.model.plm.PLMNprFile;
import com.cassinisys.plm.model.plm.PLMNprItem;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Created by GSR Cassini on 30-11-2020.
 */

public final class NprEvents {

    @Data
    @AllArgsConstructor
    public static class NprCreatedEvent {
        private PLMNpr npr;
    }


    @Data
    @AllArgsConstructor
    public static class NprBasicInfoUpdatedEvent {
        private PLMNpr oldNpr;
        private PLMNpr npr;
    }

    // Npr files event
    @Data
    @AllArgsConstructor
    public static class NprFilesAddedEvent {
        private PLMNpr npr;
        private List<PLMNprFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class NprFoldersAddedEvent {
        private PLMNpr npr;
        private PLMNprFile file;
    }

    @Data
    @AllArgsConstructor
    public static class NprFoldersDeletedEvent {
        private PLMNpr npr;
        private PLMNprFile file;
    }

    @Data
    @AllArgsConstructor
    public static class NprFileDeletedEvent {
        private PLMNpr npr;
        private PLMNprFile file;
    }

    @Data
    @AllArgsConstructor
    public static class NprFilesVersionedEvent {
        private PLMNpr npr;
        private List<PLMNprFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class NprFileRenamedEvent {
        private PLMNpr npr;
        private PLMNprFile oldFile;
        private PLMNprFile newFile;
        private String type;
    }

    @Data
    @AllArgsConstructor
    public static class NprFileLockedEvent {
        private PLMNpr npr;
        private PLMNprFile file;
    }

    @Data
    @AllArgsConstructor
    public static class NprFileUnlockedEvent {
        private PLMNpr npr;
        private PLMNprFile file;
    }

    @Data
    @AllArgsConstructor
    public static class NprFileDownloadedEvent {
        private PLMNpr npr;
        private PLMNprFile file;
    }

    // Comment event
    @Data
    @AllArgsConstructor
    public static class NprCommentAddedEvent {
        private PLMNpr npr;
        private Comment comment;
    }

    // Npr workflow events

    @Data
    @AllArgsConstructor
    public static class NprWorkflowStartedEvent {
        private PLMNpr npr;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class NprWorkflowPromotedEvent {
        private PLMNpr npr;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class NprWorkflowDemotedEvent {
        private PLMNpr npr;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class NprWorkflowFinishedEvent {
        private PLMNpr npr;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class NprWorkflowHoldEvent {
        private PLMNpr npr;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class NprWorkflowUnholdEvent {
        private PLMNpr npr;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class NprWorkflowChangeEvent {
        private PLMNpr npr;
        private PLMWorkflow oldWorkflow;
        private PLMWorkflow workflow;
    }

    @Data
    @AllArgsConstructor
    public static class NPRRequestedItemAddedEvent {
        private PLMNpr  npr;
        private List<PLMNprItem> plmNprItems;
    }


    @Data
    @AllArgsConstructor
    public static class NPRRequestedItemDeletedEvent {
        private PLMNpr npr;
        private PLMNprItem plmNprItem;
    }

    @Data
    @AllArgsConstructor
    public static class NPRRequestedItemUpdateEvent {
        private PLMNpr npr;
        private PLMNprItem oldPlmNprItem;
        private PLMNprItem newPlmNprItem;
    }
}
