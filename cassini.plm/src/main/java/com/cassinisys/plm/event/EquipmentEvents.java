package com.cassinisys.plm.event;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.plm.model.mes.MESEquipment;
import com.cassinisys.plm.model.mes.MESObjectAttribute;
import com.cassinisys.plm.model.plm.PLMFile;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class EquipmentEvents {

    @Data
    @AllArgsConstructor
    public static class EquipmentCreatedEvent {
        private MESEquipment equipment;
    }


    @Data
    @AllArgsConstructor
    public static class EquipmentBasicInfoUpdatedEvent {
        private MESEquipment oldEquipment;
        private MESEquipment equipment;
    }


    @Data
    @AllArgsConstructor
    public static class EquipmentAttributesUpdatedEvent {
        private MESEquipment equipment;
        private MESObjectAttribute oldAttribute;
        private MESObjectAttribute newAttribute;
    }

    // Equipment files event
    @Data
    @AllArgsConstructor
    public static class EquipmentFilesAddedEvent {
        private Integer equipment;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class EquipmentFoldersAddedEvent {
        private Integer equipment;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class EquipmentFoldersDeletedEvent {
        private Integer equipment;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class EquipmentFileDeletedEvent {
        private Integer equipment;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class EquipmentFilesVersionedEvent {
        private Integer equipment;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class EquipmentFileRenamedEvent {
        private Integer equipment;
        private PLMFile oldFile;
        private PLMFile newFile;
        private String type;
    }

    @Data
    @AllArgsConstructor
    public static class EquipmentFileLockedEvent {
        private Integer equipment;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class EquipmentFileUnlockedEvent {
        private Integer equipment;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class EquipmentFileDownloadedEvent {
        private Integer equipment;
        private PLMFile file;
    }

    // Comment event
    @Data
    @AllArgsConstructor
    public static class EquipmentCommentAddedEvent {
        private MESEquipment equipment;
        private Comment comment;
    }
}
