package com.cassinisys.plm.event;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.plm.model.pgc.PGCObjectAttribute;
import com.cassinisys.plm.model.pgc.PGCSubstance;
import com.cassinisys.plm.model.plm.PLMFile;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Created by Suresh Cassini on 30-11-2020.
 */

public final class SubstanceEvents {

    @Data
    @AllArgsConstructor
    public static class SubstanceCreatedEvent {
        private PGCSubstance substance;
    }


    @Data
    @AllArgsConstructor
    public static class SubstanceBasicInfoUpdatedEvent {
        private PGCSubstance oldSubstance;
        private PGCSubstance substance;
    }


    @Data
    @AllArgsConstructor
    public static class SubstanceAttributesUpdatedEvent {
        private String parentType;
        private Integer objectId;
        private Enum objectType;
        private PGCObjectAttribute oldAttribute;
        private PGCObjectAttribute newAttribute;
    }

    // Substance files event
    @Data
    @AllArgsConstructor
    public static class SubstanceFilesAddedEvent {
        private String parentType;
        private Integer substance;
        private List<PLMFile> files;
        private Enum objectType;
    }

    @Data
    @AllArgsConstructor
    public static class SubstanceFoldersAddedEvent {
        private String parentType;
        private Integer substance;
        private PLMFile file;
        private Enum objectType;
    }

    @Data
    @AllArgsConstructor
    public static class SubstanceFoldersDeletedEvent {
        private String parentType;
        private Integer substance;
        private PLMFile file;
        private Enum objectType;
    }

    @Data
    @AllArgsConstructor
    public static class SubstanceFileDeletedEvent {
        private String parentType;
        private Integer substance;
        private PLMFile file;
        private Enum objectType;
    }

    @Data
    @AllArgsConstructor
    public static class SubstanceFilesVersionedEvent {
        private String parentType;
        private Integer substance;
        private List<PLMFile> files;
        private Enum objectType;
    }

    @Data
    @AllArgsConstructor
    public static class SubstanceFileRenamedEvent {
        private String parentType;
        private Integer substance;
        private PLMFile oldFile;
        private PLMFile newFile;
        private String type;
        private Enum objectType;
    }

    @Data
    @AllArgsConstructor
    public static class SubstanceFileLockedEvent {
        private String parentType;
        private Integer substance;
        private PLMFile file;
        private Enum objectType;
    }

    @Data
    @AllArgsConstructor
    public static class SubstanceFileUnlockedEvent {
        private String parentType;
        private Integer substance;
        private PLMFile file;
        private Enum objectType;
    }

    @Data
    @AllArgsConstructor
    public static class SubstanceFileDownloadedEvent {
        private String parentType;
        private Integer substance;
        private PLMFile file;
        private Enum objectType;
    }

    // Comment event
    @Data
    @AllArgsConstructor
    public static class SubstanceCommentAddedEvent {
        private PGCSubstance substance;
        private Comment comment;
    }
}
