package com.cassinisys.plm.event;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class InspectionEvents {

    @Data
    @AllArgsConstructor
    public static class InspectionCreatedEvent {
        private PQMInspection inspection;
    }


    @Data
    @AllArgsConstructor
    public static class InspectionBasicInfoUpdatedEvent {
        private PQMInspection oldInspection;
        private PQMInspection inspection;
    }


    @Data
    @AllArgsConstructor
    public static class InspectionAttributesUpdatedEvent {
        private PQMInspection inspection;
        private PQMInspectionAttribute oldAttribute;
        private PQMInspectionAttribute newAttribute;
    }

    // Inspection files event
    @Data
    @AllArgsConstructor
    public static class InspectionFilesAddedEvent {
        private PQMInspection inspection;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class InspectionFoldersAddedEvent {
        private PQMInspection inspection;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class InspectionFoldersDeletedEvent {
        private PQMInspection inspection;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class InspectionFileDeletedEvent {
        private PQMInspection inspection;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class InspectionFilesVersionedEvent {
        private PQMInspection inspection;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class InspectionFileRenamedEvent {
        private PQMInspection inspection;
        private PLMFile oldFile;
        private PLMFile newFile;
        private String type;
    }

    @Data
    @AllArgsConstructor
    public static class InspectionFileLockedEvent {
        private PQMInspection inspection;
        private PLMFile inspectionFile;
    }

    @Data
    @AllArgsConstructor
    public static class InspectionFileUnlockedEvent {
        private PQMInspection inspection;
        private PLMFile inspectionFile;
    }

    @Data
    @AllArgsConstructor
    public static class InspectionFileDownloadedEvent {
        private PQMInspection inspection;
        private PLMFile inspectionFile;
    }

    // Inspection workflow events
    @Data
    @AllArgsConstructor
    public static class InspectionWorkflowStartedEvent {
        private PQMInspection inspection;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class InspectionWorkflowPromotedEvent {
        private PQMInspection inspection;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class InspectionWorkflowDemotedEvent {
        private PQMInspection inspection;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class InspectionWorkflowFinishedEvent {
        private PQMInspection inspection;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class InspectionWorkflowHoldEvent {
        private PQMInspection inspection;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class InspectionWorkflowUnholdEvent {
        private PQMInspection inspection;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class InspectionWorkflowChangeEvent {
        private PQMInspection inspection;
        private PLMWorkflow oldWorkflow;
        private PLMWorkflow workflow;
    }


    // Comment event
    @Data
    @AllArgsConstructor
    public static class InspectionCommentAddedEvent {
        private PQMInspection inspection;
        private Comment comment;
    }

    @Data
    @AllArgsConstructor
    public static class InspectionChecklistAssignedToEvent {
        private PQMInspection inspection;
        private PQMInspectionChecklist inspectionChecklist;

    }

    @Data
    @AllArgsConstructor
    public static class InspectionChecklistUpdatedEvent {
        private PQMInspection inspection;
        private PQMInspectionChecklist oldChecklist;
        private PQMInspectionChecklist inspectionChecklist;
    }

    @Data
    @AllArgsConstructor
    public static class InspectionChecklistParameterUpdatedEvent {
        private PQMInspection inspection;
        private PQMInspectionChecklist inspectionChecklist;
        private PQMParamActualValue paramActualValue;
    }

    @Data
    @AllArgsConstructor
    public static class ItemInspectionRelatedItemAddedEvent {
        private PQMInspection inspection;
        private List<PQMItemInspectionRelatedItem> relatedItems;
    }

    @Data
    @AllArgsConstructor
    public static class ItemInspectionRelatedItemDeletedEvent {
        private PQMInspection inspection;
        private PQMItemInspectionRelatedItem relatedItem;
    }

    @Data
    @AllArgsConstructor
    public static class MaterialInspectionRelatedItemAddedEvent {
        private PQMInspection inspection;
        private List<PQMMaterialInspectionRelatedItem> relatedItems;
    }

    @Data
    @AllArgsConstructor
    public static class MaterialInspectionRelatedItemDeletedEvent {
        private PQMInspection inspection;
        private PQMMaterialInspectionRelatedItem relatedItem;
    }
}
