package com.cassinisys.plm.event;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.plm.model.mro.MROObject;
import com.cassinisys.plm.model.mro.MROObjectAttribute;
import com.cassinisys.plm.model.mro.MROWorkRequest;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class WorkRequestEvents {
    @Data
    @AllArgsConstructor
    public static class WorkRequestCreatedEvent {
        private MROWorkRequest workRequest;
    }

    @Data
    @AllArgsConstructor
    public static class WorkRequestBasicInfoUpdatedEvent {
        private MROWorkRequest oldWorkRequest;
        private MROWorkRequest newWorkRequest;
    }

    @Data
    @AllArgsConstructor
    public static class WorkRequestAttributesUpdatedEvent {
        private MROObject workRequest;
        private MROObjectAttribute oldAttribute;
        private MROObjectAttribute newAttribute;
    }

    // WorkRequest files event
    @Data
    @AllArgsConstructor
    public static class WorkRequestFilesAddedEvent {
        private Integer workRequest;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class WorkRequestFoldersAddedEvent {
        private Integer workRequest;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class WorkRequestFoldersDeletedEvent {
        private Integer workRequest;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class WorkRequestFileDeletedEvent {
        private Integer workRequest;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class WorkRequestFilesVersionedEvent {
        private Integer workRequest;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class WorkRequestFileRenamedEvent {
        private Integer workRequest;
        private PLMFile oldFile;
        private PLMFile newFile;
        private String type;
    }

    @Data
    @AllArgsConstructor
    public static class WorkRequestFileLockedEvent {
        private Integer workRequest;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class WorkRequestFileUnlockedEvent {
        private Integer workRequest;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class WorkRequestFileDownloadedEvent {
        private Integer workRequest;
        private PLMFile file;
    }

    // Comment event
    @Data
    @AllArgsConstructor
    public static class WorkRequestCommentAddedEvent {
        private MROWorkRequest workRequest;
        private Comment comment;
    }

    // workflow events
    @Data
    @AllArgsConstructor
    public static class WorkRequestWorkflowStartedEvent {
        private MROWorkRequest workRequest;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class WorkRequestWorkflowPromotedEvent {
        private MROWorkRequest workRequest;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class WorkRequestWorkflowDemotedEvent {
        private MROWorkRequest workRequest;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class WorkRequestWorkflowFinishedEvent {
        private MROWorkRequest workRequest;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class WorkRequestWorkflowHoldEvent {
        private MROWorkRequest workRequest;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class WorkRequestWorkflowUnholdEvent {
        private MROWorkRequest workRequest;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class WorkRequestWorkflowChangeEvent {
        private MROWorkRequest workRequest;
        private PLMWorkflow oldWorkflow;
        private PLMWorkflow workflow;
    }
}