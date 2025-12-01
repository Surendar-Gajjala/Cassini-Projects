package com.cassinisys.plm.event;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.plm.model.mro.MROMeter;
import com.cassinisys.plm.model.mro.MROObject;
import com.cassinisys.plm.model.mro.MROObjectAttribute;
import com.cassinisys.plm.model.plm.PLMFile;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class MeterEvents {
    @Data
    @AllArgsConstructor
    public static class MeterCreatedEvent {
        private MROMeter meter;
    }

    @Data
    @AllArgsConstructor
    public static class MeterBasicInfoUpdatedEvent {
        private MROMeter oldMeter;
        private MROMeter newMeter;
    }
    @Data
    @AllArgsConstructor
    public static class MeterAttributesUpdatedEvent {
        private MROObject meter;
        private MROObjectAttribute oldAttribute;
        private MROObjectAttribute newAttribute;
    }

    // Meter files event
    @Data
    @AllArgsConstructor
    public static class MeterFilesAddedEvent {
        private Integer meter;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class MeterFoldersAddedEvent {
        private Integer meter;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class MeterFoldersDeletedEvent {
        private Integer meter;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class MeterFileDeletedEvent {
        private Integer meter;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class MeterFilesVersionedEvent {
        private Integer meter;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class MeterFileRenamedEvent {
        private Integer meter;
        private PLMFile oldFile;
        private PLMFile newFile;
        private String type;
    }

    @Data
    @AllArgsConstructor
    public static class MeterFileLockedEvent {
        private Integer meter;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class MeterFileUnlockedEvent {
        private Integer meter;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class MeterFileDownloadedEvent {
        private Integer meter;
        private PLMFile file;
    }

    // Comment event
    @Data
    @AllArgsConstructor
    public static class MeterCommentAddedEvent {
        private MROMeter meter;
        private Comment comment;
    }

}