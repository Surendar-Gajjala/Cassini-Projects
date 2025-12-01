package com.cassinisys.plm.event;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.plm.model.mro.MROObject;
import com.cassinisys.plm.model.mro.MROObjectAttribute;
import com.cassinisys.plm.model.mro.MROSparePart;
import com.cassinisys.plm.model.plm.PLMFile;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class SparePartsEvents {
    @Data
    @AllArgsConstructor
    public static class SparePartCreatedEvent {
        private MROSparePart sparePart;
    }

    @Data
    @AllArgsConstructor
    public static class SparePartBasicInfoUpdatedEvent {
        private MROSparePart oldSparePart;
        private MROSparePart newSparePart;
    }

    @Data
    @AllArgsConstructor
    public static class SparePartAttributesUpdatedEvent {
        private MROObject sparePart;
        private MROObjectAttribute oldAttribute;
        private MROObjectAttribute newAttribute;
    }

    // SparePart files event
    @Data
    @AllArgsConstructor
    public static class SparePartFilesAddedEvent {
        private Integer sparePart;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class SparePartFoldersAddedEvent {
        private Integer sparePart;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class SparePartFoldersDeletedEvent {
        private Integer sparePart;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class SparePartFileDeletedEvent {
        private Integer sparePart;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class SparePartFilesVersionedEvent {
        private Integer sparePart;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class SparePartFileRenamedEvent {
        private Integer sparePart;
        private PLMFile oldFile;
        private PLMFile newFile;
        private String type;
    }

    @Data
    @AllArgsConstructor
    public static class SparePartFileLockedEvent {
        private Integer sparePart;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class SparePartFileUnlockedEvent {
        private Integer sparePart;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class SparePartFileDownloadedEvent {
        private Integer sparePart;
        private PLMFile file;
    }

    // Comment event
    @Data
    @AllArgsConstructor
    public static class SparePartCommentAddedEvent {
        private MROSparePart sparePart;
        private Comment comment;
    }
}