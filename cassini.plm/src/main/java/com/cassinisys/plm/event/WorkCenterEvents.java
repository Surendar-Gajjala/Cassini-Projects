package com.cassinisys.plm.event;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.plm.model.mes.MESObjectAttribute;
import com.cassinisys.plm.model.mes.MESWorkCenter;
import com.cassinisys.plm.model.plm.PLMFile;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class WorkCenterEvents {

    @Data
    @AllArgsConstructor
    public static class WorkCenterCreatedEvent {
        private MESWorkCenter workCenter;
    }


    @Data
    @AllArgsConstructor
    public static class WorkCenterBasicInfoUpdatedEvent {
        private MESWorkCenter oldWorkCenter;
        private MESWorkCenter workCenter;
    }


    @Data
    @AllArgsConstructor
    public static class WorkCenterAttributesUpdatedEvent {
        private MESWorkCenter workCenter;
        private MESObjectAttribute oldAttribute;
        private MESObjectAttribute newAttribute;
    }

    // WorkCenter files event
    @Data
    @AllArgsConstructor
    public static class WorkCenterFilesAddedEvent {
        private String parentType;
        private Integer workCenter;
        private List<PLMFile> files;
        private Enum objectType;
    }

    @Data
    @AllArgsConstructor
    public static class WorkCenterFoldersAddedEvent {
        private String parentType;
        private Integer workCenter;
        private PLMFile file;
        private Enum objectType;
    }

    @Data
    @AllArgsConstructor
    public static class WorkCenterFoldersDeletedEvent {
        private String parentType;
        private Integer workCenter;
        private PLMFile file;
        private Enum objectType;
    }

    @Data
    @AllArgsConstructor
    public static class WorkCenterFileDeletedEvent {
        private String parentType;
        private Integer workCenter;
        private PLMFile file;
        private Enum objectType;
    }

    @Data
    @AllArgsConstructor
    public static class WorkCenterFilesVersionedEvent {
        private String parentType;
        private Integer workCenter;
        private List<PLMFile> files;
        private Enum objectType;
    }

    @Data
    @AllArgsConstructor
    public static class WorkCenterFileRenamedEvent {
        private String parentType;
        private Integer workCenter;
        private PLMFile oldFile;
        private PLMFile newFile;
        private String type;
        private Enum objectType;
    }

    @Data
    @AllArgsConstructor
    public static class WorkCenterFileLockedEvent {
        private String parentType;
        private Integer workCenter;
        private PLMFile file;
        private Enum objectType;
    }

    @Data
    @AllArgsConstructor
    public static class WorkCenterFileUnlockedEvent {
        private String parentType;
        private Integer workCenter;
        private PLMFile file;
        private Enum objectType;
    }

    @Data
    @AllArgsConstructor
    public static class WorkCenterFileDownloadedEvent {
        private String parentType;
        private Integer workCenter;
        private PLMFile file;
        private Enum objectType;
    }

    // Comment event
    @Data
    @AllArgsConstructor
    public static class WorkCenterCommentAddedEvent {
        private MESWorkCenter workCenter;
        private Comment comment;
    }
}
