package com.cassinisys.plm.event;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.plm.model.mes.MESMaterial;
import com.cassinisys.plm.model.mes.MESObjectAttribute;
import com.cassinisys.plm.model.plm.PLMFile;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class MaterialEvents {

    @Data
    @AllArgsConstructor
    public static class MaterialCreatedEvent {
        private MESMaterial material;
    }


    @Data
    @AllArgsConstructor
    public static class MaterialBasicInfoUpdatedEvent {
        private MESMaterial oldMaterial;
        private MESMaterial material;
    }


    @Data
    @AllArgsConstructor
    public static class MaterialAttributesUpdatedEvent {
        private MESMaterial material;
        private MESObjectAttribute oldAttribute;
        private MESObjectAttribute newAttribute;
    }

    // Material files event
    @Data
    @AllArgsConstructor
    public static class MaterialFilesAddedEvent {
        private Integer material;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class MaterialFoldersAddedEvent {
        private Integer material;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class MaterialFoldersDeletedEvent {
        private Integer material;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class MaterialFileDeletedEvent {
        private Integer material;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class MaterialFilesVersionedEvent {
        private Integer material;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class MaterialFileRenamedEvent {
        private Integer material;
        private PLMFile oldFile;
        private PLMFile newFile;
        private String type;
    }

    @Data
    @AllArgsConstructor
    public static class MaterialFileLockedEvent {
        private Integer material;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class MaterialFileUnlockedEvent {
        private Integer material;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class MaterialFileDownloadedEvent {
        private Integer material;
        private PLMFile file;
    }

    // Comment event
    @Data
    @AllArgsConstructor
    public static class MaterialCommentAddedEvent {
        private MESMaterial material;
        private Comment comment;
    }
}
