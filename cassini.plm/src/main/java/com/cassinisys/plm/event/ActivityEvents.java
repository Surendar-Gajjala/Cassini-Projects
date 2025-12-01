package com.cassinisys.plm.event;


import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.pm.PLMActivity;
import com.cassinisys.plm.model.pm.PLMActivityFile;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.service.activitystream.dto.ASNewMemberDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


public final class ActivityEvents {

    @Data
    @AllArgsConstructor
    public static class ActivityCreatedEvent {
        private PLMActivity activity;
    }

    @Data
    @AllArgsConstructor
    public static class ActivityBasicInfoUpdatedEvent {
        private PLMActivity oldActivity;
        private PLMActivity newActivity;

    }

    // Activity deliverables events

    @Data
    @AllArgsConstructor
    public static class ActivityDeliverablesAddedEvent {
        private PLMActivity activity;
        private List<PLMItem> activityDeliverables;
    }

    @Data
    @AllArgsConstructor
    public static class ActivityDeliverableDeletedEvent {
        private PLMActivity activity;
        private PLMItem activityDeliverable;
    }

    @Data
    @AllArgsConstructor
    public static class ActivityDeliverableFinishedEvent {
        private PLMActivity activity;
        private PLMItem activityDeliverable;
    }


    @Data
    @AllArgsConstructor
    public static class ActivityReferenceItemsAddedEvent {
        private PLMActivity activity;
        private List<ASNewMemberDTO> activityItemReferences;
    }

    @Data
    @AllArgsConstructor
    public static class ActivityReferenceItemDeletedEvent {
        private PLMActivity activity;
        private PLMItem activityItemReference;
    }

    // Activity file events
    @Data
    @AllArgsConstructor
    public static class ActivityFileDeletedEvent {
        private PLMActivity activity;
        private PLMActivityFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ActivityFilesVersionedEvent {
        private PLMActivity activity;
        private List<PLMActivityFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class ActivityFileRenamedEvent {
        private PLMActivity activity;
        private String type;
        private PLMActivityFile oldFile;
        private PLMActivityFile newFile;
    }

    @Data
    @AllArgsConstructor
    public static class ActivityFileLockedEvent {
        private PLMActivity activity;
        private PLMActivityFile activityFile;
    }

    @Data
    @AllArgsConstructor
    public static class ActivityFileUnlockedEvent {
        private PLMActivity activity;
        private PLMActivityFile activityFile;
    }

    @Data
    @AllArgsConstructor
    public static class ActivityFileDownloadedEvent {
        private PLMActivity Activity;
        private PLMActivityFile activityFile;
    }

    @Data
    @AllArgsConstructor
    public static class ActivityFilesAddedEvent {
        private PLMActivity activity;
        private List<PLMActivityFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class ActivityFoldersAddedEvent {
        private PLMActivity activity;
        private PLMActivityFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ActivityFoldersDeletedEvent {
        private PLMActivity activity;
        private PLMFile file;
    }

    // Activity workflow events

    @Data
    @AllArgsConstructor
    public static class ActivityWorkflowStartedEvent {
        private PLMActivity Activity;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class ActivityWorkflowPromotedEvent {
        private PLMActivity Activity;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class ActivityWorkflowDemotedEvent {
        private PLMActivity Activity;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class ActivityWorkflowFinishedEvent {
        private PLMActivity Activity;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class ActivityWorkflowHoldEvent {
        private PLMActivity Activity;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class ActivityWorkflowUnholdEvent {
        private PLMActivity Activity;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class ActivityWorkflowChangeEvent {
        private PLMActivity activity;
        private PLMWorkflow oldWorkflow;
        private PLMWorkflow workflow;
    }
    

  
}
