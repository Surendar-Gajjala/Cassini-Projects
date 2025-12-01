package com.cassinisys.plm.event;


import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.pm.PLMTask;
import com.cassinisys.plm.model.pm.PLMTaskFile;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.service.activitystream.dto.ASNewMemberDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


public final class TaskEvents {

    @Data
    @AllArgsConstructor
    public static class TaskCreatedEvent {
        private PLMTask task;
    }

    @Data
    @AllArgsConstructor
    public static class TaskBasicInfoUpdatedEvent {
        private PLMTask oldTask;
        private PLMTask newTask;

    }

    @Data
    @AllArgsConstructor
    public static class TaskDeliverablesAddedEvent {
        private PLMTask task;
        private List<PLMItem> taskDeliverables;
    }

    @Data
    @AllArgsConstructor
    public static class TaskDeliverableDeletedEvent {
        private PLMTask task;
        private PLMItem taskDeliverable;
    }

    @Data
    @AllArgsConstructor
    public static class TaskDeliverableFinishedEvent {
        private PLMTask task;
        private PLMItem taskDeliverable;
    }

    @Data
    @AllArgsConstructor
    public static class TaskReferenceItemsAddedEvent {
        private PLMTask task;
        private List<ASNewMemberDTO> taskItemReferences;
    }

    @Data
    @AllArgsConstructor
    public static class TaskReferenceItemDeletedEvent {
        private PLMTask task;
        private PLMItem taskItemReference;
    }

    // Task file events
    @Data
    @AllArgsConstructor
    public static class TaskFileDeletedEvent {
        private PLMTask task;
        private PLMTaskFile file;
    }

    @Data
    @AllArgsConstructor
    public static class TaskFilesVersionedEvent {
        private PLMTask task;
        private List<PLMTaskFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class TaskFileRenamedEvent {
        private PLMTask task;
        private String type;
        private PLMTaskFile oldFile;
        private PLMTaskFile newFile;
    }

    @Data
    @AllArgsConstructor
    public static class TaskFileLockedEvent {
        private PLMTask task;
        private PLMTaskFile taskFile;
    }

    @Data
    @AllArgsConstructor
    public static class TaskFileUnlockedEvent {
        private PLMTask task;
        private PLMTaskFile taskFile;
    }

    @Data
    @AllArgsConstructor
    public static class TaskFileDownloadedEvent {
        private PLMTask Task;
        private PLMTaskFile taskFile;
    }

    @Data
    @AllArgsConstructor
    public static class TaskFilesAddedEvent {
        private PLMTask task;
        private List<PLMTaskFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class TaskFoldersAddedEvent {
        private PLMTask task;
        private PLMTaskFile file;
    }

    @Data
    @AllArgsConstructor
    public static class TaskFoldersDeletedEvent {
        private PLMTask task;
        private PLMFile file;
    }

    // Task workflow events

    @Data
    @AllArgsConstructor
    public static class TaskWorkflowStartedEvent {
        private PLMTask Task;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class TaskWorkflowPromotedEvent {
        private PLMTask Task;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromTask;
        private PLMWorkflowStatus toTask;
    }

    @Data
    @AllArgsConstructor
    public static class TaskWorkflowDemotedEvent {
        private PLMTask Task;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromTask;
        private PLMWorkflowStatus toTask;
    }

    @Data
    @AllArgsConstructor
    public static class TaskWorkflowFinishedEvent {
        private PLMTask Task;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class TaskWorkflowHoldEvent {
        private PLMTask Task;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowTask;
    }

    @Data
    @AllArgsConstructor
    public static class TaskWorkflowUnholdEvent {
        private PLMTask Task;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowTask;
    }

    @Data
    @AllArgsConstructor
    public static class TaskWorkflowChangeEvent {
        private PLMTask task;
        private PLMWorkflow oldWorkflow;
        private PLMWorkflow workflow;
    }
    

  
}
