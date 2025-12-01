package com.cassinisys.plm.event;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.plm.model.mfr.PLMManufacturerPart;
import com.cassinisys.plm.model.mfr.PLMManufacturerPartAttribute;
import com.cassinisys.plm.model.mfr.PLMManufacturerPartFile;
import com.cassinisys.plm.model.mfr.PLMMfrPartInspectionReport;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class ManufacturerPartEvents {

    @Data
    @AllArgsConstructor
    public static class ManufacturerPartCreatedEvent {
        private PLMManufacturerPart part;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerPartBasicInfoUpdatedEvent {
        private PLMManufacturerPart oldPart;
        private PLMManufacturerPart part;

    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerPartAttributesUpdatedEvent {
        private PLMManufacturerPart part;
        private PLMManufacturerPartAttribute oldAttribute;
        private PLMManufacturerPartAttribute newAttribute;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerPartFilesAddedEvent {
        private PLMManufacturerPart part;
        private List<PLMManufacturerPartFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerPartFoldersAddedEvent {
        private PLMManufacturerPart part;
        private PLMManufacturerPartFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerPartFoldersDeletedEvent {
        private PLMManufacturerPart part;
        private PLMManufacturerPartFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerPartFileDeletedEvent {
        private PLMManufacturerPart part;
        private PLMManufacturerPartFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerPartFilesVersionedEvent {
        private PLMManufacturerPart part;
        private List<PLMManufacturerPartFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerPartFileRenamedEvent {
        private PLMManufacturerPart part;
        private PLMManufacturerPartFile oldFile;
        private PLMManufacturerPartFile newFile;
        private String type;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerPartFileLockedEvent {
        private PLMManufacturerPart part;
        private PLMManufacturerPartFile partFile;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerPartFileUnlockedEvent {
        private PLMManufacturerPart part;
        private PLMManufacturerPartFile partFile;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerPartFileDownloadedEvent {
        private PLMManufacturerPart part;
        private PLMManufacturerPartFile partFile;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerPartReportAddedEvent {
        private Integer part;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerPartReportFoldersAddedEvent {
        private Integer part;
        private PLMMfrPartInspectionReport file;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerPartReportFoldersDeletedEvent {
        private Integer part;
        private PLMMfrPartInspectionReport file;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerPartReportDeletedEvent {
        private Integer part;
        private PLMMfrPartInspectionReport file;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerPartReportVersionedEvent {
        private Integer part;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerPartReportRenamedEvent {
        private Integer part;
        private PLMMfrPartInspectionReport oldFile;
        private PLMMfrPartInspectionReport newFile;
        private String type;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerPartReportLockedEvent {
        private Integer part;
        private PLMMfrPartInspectionReport partFile;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerPartReportUnlockedEvent {
        private Integer part;
        private PLMMfrPartInspectionReport partFile;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerPartReportDownloadedEvent {
        private Integer part;
        private PLMFile partFile;
    }

    // ManufacturerPart workflow events
    @Data
    @AllArgsConstructor
    public static class ManufacturerPartWorkflowStartedEvent {
        private PLMManufacturerPart part;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerPartWorkflowPromotedEvent {
        private PLMManufacturerPart part;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerPartWorkflowDemotedEvent {
        private PLMManufacturerPart part;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerPartWorkflowFinishedEvent {
        private PLMManufacturerPart part;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerPartWorkflowHoldEvent {
        private PLMManufacturerPart part;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerPartWorkflowUnholdEvent {
        private PLMManufacturerPart part;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerPartWorkflowChangeEvent {
        private PLMManufacturerPart part;
        private PLMWorkflow oldWorkflow;
        private PLMWorkflow workflow;
    }

    // Comment event
    @Data
    @AllArgsConstructor
    public static class ManufacturerPartCommentAddedEvent {
        private PLMManufacturerPart part;
        private Comment comment;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerPartPromotedEvent {
        private PLMManufacturerPart manufacturerPart;
        private PLMLifeCyclePhase fromLifeCyclePhase;
        private PLMLifeCyclePhase toLifeCyclePhase;
    }

    @Data
    @AllArgsConstructor
    public static class ManufacturerPartDemotedEvent {
        private PLMManufacturerPart manufacturerPart;
        private PLMLifeCyclePhase fromLifeCyclePhase;
        private PLMLifeCyclePhase toLifeCyclePhase;
    }
}
