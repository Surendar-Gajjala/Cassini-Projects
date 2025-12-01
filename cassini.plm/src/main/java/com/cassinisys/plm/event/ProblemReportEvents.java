package com.cassinisys.plm.event;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.pqm.PQMPRProblemItem;
import com.cassinisys.plm.model.pqm.PQMPRRelatedItem;
import com.cassinisys.plm.model.pqm.PQMProblemReport;
import com.cassinisys.plm.model.pqm.PQMProblemReportAttribute;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class ProblemReportEvents {

    @Data
    @AllArgsConstructor
    public static class ProblemReportCreatedEvent {
        private PQMProblemReport problemReport;
    }

    @Data
    @AllArgsConstructor
    public static class ProblemReportImplementedEvent {
        private PQMProblemReport problemReport;
    }

    @Data
    @AllArgsConstructor
    public static class ProblemReportBasicInfoUpdatedEvent {
        private PQMProblemReport oldProblemReport;
        private PQMProblemReport problemReport;
    }

    @Data
    @AllArgsConstructor
    public static class ProblemReportAttributesUpdatedEvent {
        private PQMProblemReport problemReport;
        private PQMProblemReportAttribute oldAttribute;
        private PQMProblemReportAttribute newAttribute;
    }

    // ProblemReport files event
    @Data
    @AllArgsConstructor
    public static class ProblemReportFilesAddedEvent {
        private PQMProblemReport problemReport;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class ProblemReportFoldersAddedEvent {
        private PQMProblemReport problemReport;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ProblemReportFoldersDeletedEvent {
        private PQMProblemReport problemReport;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ProblemReportFileDeletedEvent {
        private PQMProblemReport problemReport;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ProblemReportFilesVersionedEvent {
        private PQMProblemReport problemReport;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class ProblemReportFileRenamedEvent {
        private PQMProblemReport problemReport;
        private PLMFile oldFile;
        private PLMFile newFile;
        private String type;
    }

    @Data
    @AllArgsConstructor
    public static class ProblemReportFileLockedEvent {
        private PQMProblemReport problemReport;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ProblemReportFileUnlockedEvent {
        private PQMProblemReport problemReport;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ProblemReportFileDownloadedEvent {
        private PQMProblemReport problemReport;
        private PLMFile file;
    }

    // ProblemReport workflow events
    @Data
    @AllArgsConstructor
    public static class ProblemReportWorkflowStartedEvent {
        private PQMProblemReport problemReport;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class ProblemReportWorkflowPromotedEvent {
        private PQMProblemReport problemReport;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class ProblemReportWorkflowDemotedEvent {
        private PQMProblemReport problemReport;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class ProblemReportWorkflowFinishedEvent {
        private PQMProblemReport problemReport;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class ProblemReportWorkflowHoldEvent {
        private PQMProblemReport problemReport;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class ProblemReportWorkflowUnholdEvent {
        private PQMProblemReport problemReport;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class ProblemReportWorkflowChangeEvent {
        private PQMProblemReport problemReport;
        private PLMWorkflow oldWorkflow;
        private PLMWorkflow workflow;
    }


    // Comment event
    @Data
    @AllArgsConstructor
    public static class ProblemReportCommentAddedEvent {
        private PQMProblemReport problemReport;
        private Comment comment;
    }

    @Data
    @AllArgsConstructor
    public static class ProblemReportProblemItemAddedEvent {
        private PQMProblemReport problemReport;
        private List<PQMPRProblemItem> problemItems;
    }

    @Data
    @AllArgsConstructor
    public static class ProblemReportProblemItemUpdatedEvent {
        private PQMProblemReport problemReport;
        private PQMPRProblemItem oldProblemItem;
        private PQMPRProblemItem problemItem;
    }

    @Data
    @AllArgsConstructor
    public static class ProblemReportProblemItemDeletedEvent {
        private PQMProblemReport problemReport;
        private PQMPRProblemItem problemItem;
    }

    @Data
    @AllArgsConstructor
    public static class ProblemReportRelatedItemAddedEvent {
        private PQMProblemReport problemReport;
        private List<PQMPRRelatedItem> relatedItems;
    }

    @Data
    @AllArgsConstructor
    public static class ProblemReportRelatedItemDeletedEvent {
        private PQMProblemReport problemReport;
        private PQMPRRelatedItem relatedItem;
    }
}
