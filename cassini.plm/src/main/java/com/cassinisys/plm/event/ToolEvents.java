package com.cassinisys.plm.event;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.plm.model.mes.MESObjectAttribute;
import com.cassinisys.plm.model.mes.MESTool;
import com.cassinisys.plm.model.plm.PLMFile;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class ToolEvents {

    @Data
    @AllArgsConstructor
    public static class ToolCreatedEvent {
        private MESTool tool;
    }


    @Data
    @AllArgsConstructor
    public static class ToolBasicInfoUpdatedEvent {
        private MESTool oldTool;
        private MESTool tool;
    }


    @Data
    @AllArgsConstructor
    public static class ToolAttributesUpdatedEvent {
        private MESTool tool;
        private MESObjectAttribute oldAttribute;
        private MESObjectAttribute newAttribute;
    }

    // Tool files event
    @Data
    @AllArgsConstructor
    public static class ToolFilesAddedEvent {
        private Integer tool;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class ToolFoldersAddedEvent {
        private Integer tool;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ToolFoldersDeletedEvent {
        private Integer tool;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ToolFileDeletedEvent {
        private Integer tool;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ToolFilesVersionedEvent {
        private Integer tool;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class ToolFileRenamedEvent {
        private Integer tool;
        private PLMFile oldFile;
        private PLMFile newFile;
        private String type;
    }

    @Data
    @AllArgsConstructor
    public static class ToolFileLockedEvent {
        private Integer tool;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ToolFileUnlockedEvent {
        private Integer tool;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ToolFileDownloadedEvent {
        private Integer tool;
        private PLMFile file;
    }

    // Comment event
    @Data
    @AllArgsConstructor
    public static class ToolCommentAddedEvent {
        private MESTool tool;
        private Comment comment;
    }
}
