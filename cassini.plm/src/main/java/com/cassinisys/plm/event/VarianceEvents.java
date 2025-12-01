package com.cassinisys.plm.event;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.plm.model.cm.*;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class VarianceEvents {

    @Data
    @AllArgsConstructor
    public static class VarianceCreatedEvent {
        private PLMVariance variance;
    }

    @Data
    @AllArgsConstructor
    public static class VarianceBasicInfoUpdatedEvent {
        private PLMVariance oldVariance;
        private PLMVariance newVariance;

    }

    @Data
    @AllArgsConstructor
    public static class VarianceAttributesUpdatedEvent {
        private PLMVariance variance;
        private PLMChangeAttribute oldAttribute;
        private PLMChangeAttribute newAttribute;
    }

    // Variance files event
    @Data
    @AllArgsConstructor
    public static class VarianceFilesAddedEvent {
        private PLMVariance variance;
        private List<PLMChangeFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class VarianceFoldersAddedEvent {
        private PLMVariance variance;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class VarianceFoldersDeletedEvent {
        private PLMVariance variance;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class VarianceFileDeletedEvent {
        private PLMVariance variance;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class VarianceFilesVersionedEvent {
        private PLMVariance variance;
        private List<PLMChangeFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class VarianceFileRenamedEvent {
        private PLMVariance variance;
        private PLMFile oldFile;
        private PLMFile newFile;
        private String type;
    }

    @Data
    @AllArgsConstructor
    public static class VarianceFileLockedEvent {
        private PLMVariance variance;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class VarianceFileUnlockedEvent {
        private PLMVariance variance;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class VarianceFileDownloadedEvent {
        private PLMVariance variance;
        private PLMFile file;
    }

    // Variance workflow events
    @Data
    @AllArgsConstructor
    public static class VarianceWorkflowStartedEvent {
        private PLMVariance variance;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class VarianceWorkflowChangedEvent {
        private PLMVariance variance;
        private String plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class VarianceWorkflowPromotedEvent {
        private PLMVariance variance;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class VarianceWorkflowDemotedEvent {
        private PLMVariance variance;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class VarianceWorkflowFinishedEvent {
        private PLMVariance variance;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class VarianceWorkflowHoldEvent {
        private PLMVariance variance;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class VarianceWorkflowUnholdEvent {
        private PLMVariance variance;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class VarianceWorkflowChangeEvent {
        private PLMVariance variance;
        private PLMWorkflow oldWorkflow;
        private PLMWorkflow workflow;
    }

    // Comment event
    @Data
    @AllArgsConstructor
    public static class VarianceCommentAddedEvent {
        private PLMVariance variance;
        private Comment comment;
    }

    @Data
    @AllArgsConstructor
    public static class VarianceAffectedItemAddedEvent {
        private PLMVariance variance;
        private List<PLMVarianceAffectedItem> affectedItems;
    }

    @Data
    @AllArgsConstructor
    public static class VarianceAffectedItemUpdatedEvent {
        private PLMVariance variance;
        private PLMVarianceAffectedItem oldaffectedItem;
        private PLMVarianceAffectedItem newaffectedItem;
    }

    @Data
    @AllArgsConstructor
    public static class VarianceAffectedItemDeletedEvent {
        private PLMVariance variance;
        private PLMVarianceAffectedItem affectedItem;
    }

    @Data
    @AllArgsConstructor
    public static class VarianceAffectedPartAddedEvent {
        private PLMVariance variance;
        private List<PLMVarianceAffectedMaterial> affectedMaterials;
    }

    @Data
    @AllArgsConstructor
    public static class VarianceAffectedPartUpdatedEvent {
        private PLMVariance variance;
        private PLMVarianceAffectedMaterial oldaffectedMaterial;
        private PLMVarianceAffectedMaterial newaffectedMaterial;
    }

    @Data
    @AllArgsConstructor
    public static class VarianceAffectedPartDeletedEvent {
        private PLMVariance variance;
        private PLMVarianceAffectedMaterial affectedMaterial;
    }

    @Data
    @AllArgsConstructor
    public static class VarianceRelatedItemsAddedEvent {
        private PLMVariance variance;
        private List<PLMChangeRelatedItem> relatedItems;
    }

    @Data
    @AllArgsConstructor
    public static class VarianceChangeRelatedDeletedEvent {
        private PLMVariance variance;
        private PLMChangeRelatedItem relatedItem;
    }
}
