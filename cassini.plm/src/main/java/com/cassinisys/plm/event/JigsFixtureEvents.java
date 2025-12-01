package com.cassinisys.plm.event;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.plm.model.mes.MESJigsFixture;
import com.cassinisys.plm.model.mes.MESObjectAttribute;
import com.cassinisys.plm.model.plm.PLMFile;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class JigsFixtureEvents {

    @Data
    @AllArgsConstructor
    public static class JigFixtureCreatedEvent {
        private MESJigsFixture jigsFixture;
    }


    @Data
    @AllArgsConstructor
    public static class JigFixtureBasicInfoUpdatedEvent {
        private MESJigsFixture oldJigFixture;
        private MESJigsFixture jigsFixture;
    }


    @Data
    @AllArgsConstructor
    public static class JigFixtureAttributesUpdatedEvent {
        private MESJigsFixture jigsFixture;
        private MESObjectAttribute oldAttribute;
        private MESObjectAttribute newAttribute;
    }

    // JigFixture files event
    @Data
    @AllArgsConstructor
    public static class JigFixtureFilesAddedEvent {
        private Integer jigsFixture;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class JigFixtureFoldersAddedEvent {
        private Integer jigsFixture;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class JigFixtureFoldersDeletedEvent {
        private Integer jigsFixture;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class JigFixtureFileDeletedEvent {
        private Integer jigsFixture;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class JigFixtureFilesVersionedEvent {
        private Integer jigsFixture;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class JigFixtureFileRenamedEvent {
        private Integer jigsFixture;
        private PLMFile oldFile;
        private PLMFile newFile;
        private String type;
    }

    @Data
    @AllArgsConstructor
    public static class JigFixtureFileLockedEvent {
        private Integer jigsFixture;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class JigFixtureFileUnlockedEvent {
        private Integer jigsFixture;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class JigFixtureFileDownloadedEvent {
        private Integer jigsFixture;
        private PLMFile file;
    }

    // Comment event
    @Data
    @AllArgsConstructor
    public static class JigFixtureCommentAddedEvent {
        private MESJigsFixture jigsFixture;
        private Comment comment;
    }
}
