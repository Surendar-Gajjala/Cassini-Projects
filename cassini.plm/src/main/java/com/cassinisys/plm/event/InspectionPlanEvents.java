package com.cassinisys.plm.event;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class InspectionPlanEvents {

    // Plan events
    public interface InspectionPlanBaseEvent {
        PQMInspectionPlanRevision getInspectionPlanRevision();
    }

    @Data
    @AllArgsConstructor
    public static class PlanCreatedEvent implements InspectionPlanBaseEvent {
        private PQMInspectionPlan inspectionPlan;
        private PQMInspectionPlanRevision inspectionPlanRevision;
    }

    @Data
    @AllArgsConstructor
    public static class PlanRevisionCreatedEvent implements InspectionPlanBaseEvent {
        private PQMInspectionPlan inspectionPlan;
        private PQMInspectionPlanRevision inspectionPlanRevision;
    }

    @Data
    @AllArgsConstructor
    public static class PlanBasicInfoUpdatedEvent implements InspectionPlanBaseEvent {
        private PQMInspectionPlan oldInspectionPlan;
        private PQMInspectionPlan inspectionPlan;
        private PQMInspectionPlanRevision inspectionPlanRevision;
    }

    @Data
    @AllArgsConstructor
    public static class PlanAttributesUpdatedEvent implements InspectionPlanBaseEvent {
        private PQMInspectionPlan inspectionPlan;
        private PQMInspectionPlanRevision inspectionPlanRevision;
        private PQMInspectionPlanAttribute oldAttribute;
        private PQMInspectionPlanAttribute newAttribute;
    }

    @Data
    @AllArgsConstructor
    public static class PlanRevisionAttributesUpdatedEvent implements InspectionPlanBaseEvent {
        private PQMInspectionPlan inspectionPlan;
        private PQMInspectionPlanRevision inspectionPlanRevision;
        private PQMInspectionPlanRevisionAttribute oldAttribute;
        private PQMInspectionPlanRevisionAttribute newAttribute;
    }

    // Plan files event
    @Data
    @AllArgsConstructor
    public static class PlanFilesAddedEvent implements InspectionPlanBaseEvent {
        private PQMInspectionPlanRevision inspectionPlanRevision;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class PlanFoldersAddedEvent implements InspectionPlanBaseEvent {
        private PQMInspectionPlanRevision inspectionPlanRevision;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class PlanFoldersDeletedEvent implements InspectionPlanBaseEvent {
        private PQMInspectionPlanRevision inspectionPlanRevision;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class PlanFileDeletedEvent implements InspectionPlanBaseEvent {
        private PQMInspectionPlanRevision inspectionPlanRevision;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class PlanFilesVersionedEvent implements InspectionPlanBaseEvent {
        private PQMInspectionPlanRevision inspectionPlanRevision;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class PlanFileRenamedEvent implements InspectionPlanBaseEvent {
        private PQMInspectionPlanRevision inspectionPlanRevision;
        private PLMFile oldFile;
        private PLMFile newFile;
        private String type;
    }

    @Data
    @AllArgsConstructor
    public static class PlanFileLockedEvent implements InspectionPlanBaseEvent {
        private PQMInspectionPlanRevision inspectionPlanRevision;
        private PLMFile inspectionPlanFile;
    }

    @Data
    @AllArgsConstructor
    public static class PlanFileUnlockedEvent implements InspectionPlanBaseEvent {
        private PQMInspectionPlanRevision inspectionPlanRevision;
        private PLMFile inspectionPlanFile;
    }

    @Data
    @AllArgsConstructor
    public static class PlanFileDownloadedEvent implements InspectionPlanBaseEvent {
        private PQMInspectionPlanRevision inspectionPlanRevision;
        private PLMFile inspectionPlanFile;
    }

    // Plan workflow events
    @Data
    @AllArgsConstructor
    public static class PlanWorkflowStartedEvent implements InspectionPlanBaseEvent {
        private PQMInspectionPlanRevision inspectionPlanRevision;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class PlanWorkflowPromotedEvent implements InspectionPlanBaseEvent {
        private PQMInspectionPlanRevision inspectionPlanRevision;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class PlanWorkflowDemotedEvent implements InspectionPlanBaseEvent {
        private PQMInspectionPlanRevision inspectionPlanRevision;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class PlanWorkflowFinishedEvent implements InspectionPlanBaseEvent {
        private PQMInspectionPlanRevision inspectionPlanRevision;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class PlanWorkflowHoldEvent implements InspectionPlanBaseEvent {
        private PQMInspectionPlanRevision inspectionPlanRevision;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class PlanWorkflowUnholdEvent implements InspectionPlanBaseEvent {
        private PQMInspectionPlanRevision inspectionPlanRevision;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class PlanWorkflowChangeEvent implements InspectionPlanBaseEvent {
        private PQMInspectionPlanRevision inspectionPlanRevision;
        private PLMWorkflow oldWorkflow;
        private PLMWorkflow workflow;
    }


    // Comment event
    @Data
    @AllArgsConstructor
    public static class PlanCommentAddedEvent implements InspectionPlanBaseEvent {
        private PQMInspectionPlanRevision inspectionPlanRevision;
        private Comment comment;
    }

    // Checklist event
    @Data
    @AllArgsConstructor
    public static class PlanChecklistAddEvent implements InspectionPlanBaseEvent {
        private PQMInspectionPlanRevision inspectionPlanRevision;
        private PQMInspectionPlanChecklist inspectionPlanChecklist;

    }

    @Data
    @AllArgsConstructor
    public static class PlanChecklistDeleteEvent implements InspectionPlanBaseEvent {
        private PQMInspectionPlanRevision inspectionPlanRevision;
        private PQMInspectionPlanChecklist inspectionPlanChecklist;
    }

    @Data
    @AllArgsConstructor
    public static class PlanChecklistParameterAddEvent implements InspectionPlanBaseEvent {
        private PQMInspectionPlanRevision inspectionPlanRevision;
        private PQMInspectionPlanChecklist inspectionPlanChecklist;
        private PQMInspectionPlanChecklistParameter checklistParameter;

    }

    @Data
    @AllArgsConstructor
    public static class PlanChecklistParameterDeleteEvent implements InspectionPlanBaseEvent {
        private PQMInspectionPlanRevision inspectionPlanRevision;
        private PQMInspectionPlanChecklist inspectionPlanChecklist;
        private PQMInspectionPlanChecklistParameter checklistParameter;
    }

    @Data
    @AllArgsConstructor
    public static class InspectionPlanChecklistUpdatedEvent {
        private PQMInspectionPlanRevision inspectionPlanRevision;
        private PQMInspectionPlanChecklist oldPlanChecklist;
        private PQMInspectionPlanChecklist inspectionPlanChecklist;
    }
}
