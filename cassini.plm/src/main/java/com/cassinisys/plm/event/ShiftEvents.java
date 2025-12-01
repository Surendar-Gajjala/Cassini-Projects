package com.cassinisys.plm.event;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.plm.model.mes.MESShift;
import com.cassinisys.plm.model.plm.PLMFile;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class ShiftEvents {
    @Data
    @AllArgsConstructor
    public static class ShiftCreatedEvent {
        private MESShift shift;
    }

    @Data
    @AllArgsConstructor
    public static class ShiftBasicInfoUpdatedEvent {
        private MESShift oldShift;
        private MESShift newShift;
    }

    // Shift files event
    @Data
    @AllArgsConstructor
    public static class ShiftFilesAddedEvent {
        private Integer shift;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class ShiftFoldersAddedEvent {
        private Integer shift;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ShiftFoldersDeletedEvent {
        private Integer shift;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ShiftFileDeletedEvent {
        private Integer shift;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ShiftFilesVersionedEvent {
        private Integer shift;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class ShiftFileRenamedEvent {
        private Integer shift;
        private PLMFile oldFile;
        private PLMFile newFile;
        private String type;
    }

    @Data
    @AllArgsConstructor
    public static class ShiftFileLockedEvent {
        private Integer shift;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ShiftFileUnlockedEvent {
        private Integer shift;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ShiftFileDownloadedEvent {
        private Integer shift;
        private PLMFile file;
    }

    // Comment event
    @Data
    @AllArgsConstructor
    public static class ShiftCommentAddedEvent {
        private MESShift shift;
        private Comment comment;
    }

}