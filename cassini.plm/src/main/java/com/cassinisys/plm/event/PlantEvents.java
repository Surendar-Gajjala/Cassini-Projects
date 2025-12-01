package com.cassinisys.plm.event;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.plm.model.mes.MESObjectAttribute;
import com.cassinisys.plm.model.mes.MESPlant;
import com.cassinisys.plm.model.plm.PLMFile;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class PlantEvents {
    @Data
    @AllArgsConstructor
    public static class PlantCreatedEvent {
        private MESPlant plant;
    }

    @Data
    @AllArgsConstructor
    public static class PlantBasicInfoUpdatedEvent {
        private MESPlant oldPlant;
        private MESPlant newPlant;
    }

    @Data
    @AllArgsConstructor
    public static class PlantAttributesUpdatedEvent {
        private String parentType;
        private Integer objectId;
        private Enum objectType;
        private MESObjectAttribute oldAttribute;
        private MESObjectAttribute newAttribute;
    }
    // Plant files event
    @Data
    @AllArgsConstructor
    public static class PlantFilesAddedEvent {
        private Integer plant;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class PlantFoldersAddedEvent {
        private Integer plant;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class PlantFoldersDeletedEvent {
        private Integer plant;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class PlantFileDeletedEvent {
        private Integer plant;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class PlantFilesVersionedEvent {
        private Integer plant;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class PlantFileRenamedEvent {
        private Integer plant;
        private PLMFile oldFile;
        private PLMFile newFile;
        private String type;
    }

    @Data
    @AllArgsConstructor
    public static class PlantFileLockedEvent {
        private Integer plant;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class PlantFileUnlockedEvent {
        private Integer plant;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class PlantFileDownloadedEvent {
        private Integer plant;
        private PLMFile file;
    }

    // Comment event
    @Data
    @AllArgsConstructor
    public static class PlantCommentAddedEvent {
        private MESPlant plant;
        private Comment comment;
    }
}