package com.cassinisys.plm.event;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.plm.model.mes.MESMBOMInstance;
import com.cassinisys.plm.model.mes.MESMBOMInstanceFile;
import com.cassinisys.plm.model.mes.MESObjectAttribute;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class MBOMInstanceEvents {

    @Data
    @AllArgsConstructor
    public static class MbomInstanceCreatedEvent {
        private MESMBOMInstance mbomInstance;

    }

    @Data
    @AllArgsConstructor
    public static class MbomInstanceBasicInfoUpdatedEvent {
        private MESMBOMInstance oldMbomInstance;
        private MESMBOMInstance newMbomInstance;
    }

    @Data
    @AllArgsConstructor
    public static class MbomAttributesUpdatedEvent {
        private String parentType;
        private Integer objectId;
        private Enum objectType;
        private Integer mbomInstance;
        private MESObjectAttribute oldAttribute;
        private MESObjectAttribute newAttribute;
    }

    // MBOM files event
    @Data
    @AllArgsConstructor
    public static class MbomInstanceFilesAddedEvent {
        private Integer mbomInstance;
        private List<MESMBOMInstanceFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class MbomInstanceFoldersAddedEvent {
        private Integer mbomInstance;
        private MESMBOMInstanceFile file;
    }

    @Data
    @AllArgsConstructor
    public static class MbomInstanceFoldersDeletedEvent {
        private Integer mbomInstance;
        private MESMBOMInstanceFile file;
    }

    @Data
    @AllArgsConstructor
    public static class MbomInstanceFileDeletedEvent {
        private Integer mbomInstance;
        private MESMBOMInstanceFile file;
    }

    @Data
    @AllArgsConstructor
    public static class MbomInstanceFilesVersionedEvent {
        private Integer mbomInstance;
        private List<MESMBOMInstanceFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class MbomInstanceFileRenamedEvent {
        private Integer mbomInstance;
        private MESMBOMInstanceFile oldFile;
        private MESMBOMInstanceFile newFile;
        private String type;
    }

    // @Data
    // @AllArgsConstructor
    // public static class MbomFileLockedEvent {
    //     private Integer mbom;
    //     private File file;
    // }

    // @Data
    // @AllArgsConstructor
    // public static class MbomFileUnlockedEvent {
    //     private Integer mbom;
    //     private MESMBOMInstanceFile file;
    // }

    @Data
    @AllArgsConstructor
    public static class MbomInstanceFileDownloadedEvent {
        private Integer mbomInstance;
        private MESMBOMInstanceFile file;
    }

    // Comment event
    @Data
    @AllArgsConstructor
    public static class MbomCommentAddedEvent {
        private Integer mbomInstance;
        private Comment comment;
    }


}
