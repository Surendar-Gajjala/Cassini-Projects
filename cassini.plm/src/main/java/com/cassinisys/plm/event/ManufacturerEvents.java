package com.cassinisys.plm.event;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.plm.model.mfr.PLMManufacturer;
import com.cassinisys.plm.model.mfr.PLMManufacturerAttribute;
import com.cassinisys.plm.model.mfr.PLMManufacturerFile;
import com.cassinisys.plm.model.mfr.PLMManufacturerPart;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class ManufacturerEvents {

    @Data
    @AllArgsConstructor
    public static class ManufacturerCreatedEvent {
        private PLMManufacturer manufacturer;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerBasicInfoUpdatedEvent {
        private PLMManufacturer oldManufacturer;
        private PLMManufacturer manufacturer;

    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerAttributesUpdatedEvent {
        private PLMManufacturer manufacturer;
        private PLMManufacturerAttribute oldAttribute;
        private PLMManufacturerAttribute newAttribute;
    }

    // Inspection files event
    @Data
    @AllArgsConstructor
    public static class ManufacturerFilesAddedEvent {
        private PLMManufacturer manufacturer;
        private List<PLMManufacturerFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerFoldersAddedEvent {
        private PLMManufacturer manufacturer;
        private PLMManufacturerFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerFoldersDeletedEvent {
        private PLMManufacturer manufacturer;
        private PLMManufacturerFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerFileDeletedEvent {
        private PLMManufacturer manufacturer;
        private PLMManufacturerFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerFilesVersionedEvent {
        private PLMManufacturer manufacturer;
        private List<PLMManufacturerFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerFileRenamedEvent {
        private PLMManufacturer manufacturer;
        private PLMManufacturerFile oldFile;
        private PLMManufacturerFile newFile;
        private String type;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerFileLockedEvent {
        private PLMManufacturer manufacturer;
        private PLMManufacturerFile manufacturerFile;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerFileUnlockedEvent {
        private PLMManufacturer manufacturer;
        private PLMManufacturerFile manufacturerFile;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerFileDownloadedEvent {
        private PLMManufacturer manufacturer;
        private PLMManufacturerFile manufacturerFile;
    }

    // Inspection workflow events
    @Data
    @AllArgsConstructor
    public static class ManufacturerWorkflowStartedEvent {
        private PLMManufacturer manufacturer;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerWorkflowPromotedEvent {
        private PLMManufacturer manufacturer;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerWorkflowDemotedEvent {
        private PLMManufacturer manufacturer;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerWorkflowFinishedEvent {
        private PLMManufacturer manufacturer;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerWorkflowHoldEvent {
        private PLMManufacturer manufacturer;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerWorkflowUnholdEvent {
        private PLMManufacturer manufacturer;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerWorkflowChangeEvent {
        private PLMManufacturer manufacturer;
        private PLMWorkflow oldWorkflow;
        private PLMWorkflow workflow;
    }

    // Comment event
    @Data
    @AllArgsConstructor
    public static class ManufacturerCommentAddedEvent {
        private PLMManufacturer manufacturer;
        private Comment comment;
    }

    @Data
    @AllArgsConstructor
    public static class MfrPartCreatedEvent {
        private PLMManufacturerPart part;
        private PLMManufacturer manufacturer;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerPromotedEvent {
        private PLMManufacturer manufacturer;
        private PLMLifeCyclePhase fromLifeCyclePhase;
        private PLMLifeCyclePhase toLifeCyclePhase;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerDemotedEvent {
        private PLMManufacturer manufacturer;
        private PLMLifeCyclePhase fromLifeCyclePhase;
        private PLMLifeCyclePhase toLifeCyclePhase;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerPartDeletedEvent {
        private PLMManufacturer manufacturer;
        private PLMManufacturerPart manufacturerPart;
    }
}
