package com.cassinisys.plm.event;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.plm.model.cm.PLMChange;
import com.cassinisys.plm.model.mes.MESBOP;
import com.cassinisys.plm.model.mes.MESBOPFile;
import com.cassinisys.plm.model.mes.MESBOPRouteOperation;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMItemAttribute;
import com.cassinisys.plm.model.plm.PLMItemRevisionAttribute;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class BOPEvents {

    // BOP events
    public interface BOPBaseEvent {
        Integer getBopRevision();
    }

    @Data
    @AllArgsConstructor
    public static class BOPCreatedEvent implements BOPBaseEvent {
        private MESBOP bop;
        private Integer bopRevision;
    }

    @Data
    @AllArgsConstructor
    public static class BOPRevisionCreatedEvent implements BOPBaseEvent {
        private MESBOP bop;
        private Integer bopRevision;
    }

    @Data
    @AllArgsConstructor
    public static class BOPBasicInfoUpdatedEvent implements BOPBaseEvent {
        private MESBOP plmOldBOP;
        private MESBOP bop;
        private Integer bopRevision;
    }

    @Data
    @AllArgsConstructor
    public static class BOPAttributesUpdatedEvent implements BOPBaseEvent {
        private MESBOP bop;
        private Integer bopRevision;
        private PLMItemAttribute oldAttribute;
        private PLMItemAttribute newAttribute;
    }

    @Data
    @AllArgsConstructor
    public static class BOPRevisionAttributesUpdatedEvent implements BOPBaseEvent {
        private MESBOP bop;
        private Integer bopRevision;
        private PLMItemRevisionAttribute oldAttribute;
        private PLMItemRevisionAttribute newAttribute;
    }

    @Data
    @AllArgsConstructor
    public static class BOPReleasedEvent implements BOPBaseEvent {
        private MESBOP bop;
        private Integer bopRevision;
        private PLMChange changeOrder;
    }

    @Data
    @AllArgsConstructor
    public static class BOPStatusChangedEvent implements BOPBaseEvent {
        private MESBOP bop;
        private Integer bopRevision;
        private PLMChange changeOrder;
        private PLMLifeCyclePhase oldStatus;
        private PLMLifeCyclePhase newStatus;
    }

    // BOP files event
    @Data
    @AllArgsConstructor
    public static class BOPFilesAddedEvent implements BOPBaseEvent {
        private Integer bopRevision;
        private List<MESBOPFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class BOPFileDeletedEvent implements BOPBaseEvent {
        private Integer bopRevision;
        private MESBOPFile file;
    }

    @Data
    @AllArgsConstructor
    public static class BOPFilesVersionedEvent implements BOPBaseEvent {
        private Integer bopRevision;
        private List<MESBOPFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class BOPFileRenamedEvent implements BOPBaseEvent {
        private Integer bopRevision;
        private MESBOPFile oldFile;
        private MESBOPFile newFile;
        private String type;
    }

    @Data
    @AllArgsConstructor
    public static class BOPFileLockedEvent implements BOPBaseEvent {
        private Integer bopRevision;
        private MESBOPFile bopFile;
    }

    @Data
    @AllArgsConstructor
    public static class BOPSubscribeEvent implements BOPBaseEvent {
        private Integer bopRevision;
        private MESBOP bop;
    }

    @Data
    @AllArgsConstructor
    public static class BOPUnSubscribeEvent implements BOPBaseEvent {
        private Integer bopRevision;
        private MESBOP bop;
    }

    @Data
    @AllArgsConstructor
    public static class BOPFileUnlockedEvent implements BOPBaseEvent {
        private Integer bopRevision;
        private MESBOPFile bopFile;
    }

    @Data
    @AllArgsConstructor
    public static class BOPFileDownloadedEvent implements BOPBaseEvent {
        private Integer bopRevision;
        private MESBOPFile bopFile;
    }


    @Data
    @AllArgsConstructor
    public static class BOPFoldersAddedEvent implements BOPBaseEvent {
        private Integer bopRevision;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class BOPFoldersDeletedEvent implements BOPBaseEvent {
        private Integer bopRevision;
        private PLMFile file;
    }

    // BOP workflow events
    @Data
    @AllArgsConstructor
    public static class BOPWorkflowStartedEvent implements BOPBaseEvent {
        private Integer bopRevision;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class BOPWorkflowPromotedEvent implements BOPBaseEvent {
        private Integer bopRevision;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class BOPWorkflowDemotedEvent implements BOPBaseEvent {
        private Integer bopRevision;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class BOPWorkflowFinishedEvent implements BOPBaseEvent {
        private Integer bopRevision;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class BOPWorkflowHoldEvent implements BOPBaseEvent {
        private Integer bopRevision;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class BOPWorkflowUnholdEvent implements BOPBaseEvent {
        private Integer bopRevision;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class BOPWorkflowChangeEvent implements BOPBaseEvent {
        private Integer bopRevision;
        private PLMWorkflow oldWorkflow;
        private PLMWorkflow workflow;
    }

    // Comment event
    @Data
    @AllArgsConstructor
    public static class BOPCommentAddedEvent implements BOPBaseEvent {
        private Integer bopRevision;
        private Comment comment;
    }

    @Data
    @AllArgsConstructor
    public static class BOPRouteOperationsAddedEvent implements BOPBaseEvent {
        private Integer bopRevision;
        private List<MESBOPRouteOperation> routeItems;
    }

    @Data
    @AllArgsConstructor
    public static class BOPRouteItemUpdatedEvent implements BOPBaseEvent {
        private Integer bopRevision;
        private MESBOPRouteOperation oldRouteItem;
        private MESBOPRouteOperation routeItem;
    }

    @Data
    @AllArgsConstructor
    public static class BOPRouteItemDeletedEvent implements BOPBaseEvent {
        private Integer bopRevision;
        private MESBOPRouteOperation routeItem;
    }
}
