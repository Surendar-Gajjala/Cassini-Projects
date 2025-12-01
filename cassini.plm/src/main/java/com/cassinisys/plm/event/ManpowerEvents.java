package com.cassinisys.plm.event;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.plm.model.mes.MESManpower;
import com.cassinisys.plm.model.mes.MESManpowerContact;
import com.cassinisys.plm.model.mes.MESObjectAttribute;
import com.cassinisys.plm.model.plm.PLMFile;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class ManpowerEvents {

    @Data
    @AllArgsConstructor
    public static class ManpowerCreatedEvent {
        private MESManpower manpower;
    }

    @Data
    @AllArgsConstructor
    public static class ManpowerPersonCreatedEvent {
        private MESManpower manpower;
        private Person person;
    }

    @Data
    @AllArgsConstructor
    public static class ManpowerPersonDeletedEvent {
        private MESManpower manpower;
        private Person person;
    }

    @Data
    @AllArgsConstructor
    public static class ManpowerPersonUpdatedEvent {
        private MESManpowerContact oldContact;
        private MESManpowerContact newContact;
    }


    @Data
    @AllArgsConstructor
    public static class ManpowerBasicInfoUpdatedEvent {
        private MESManpower oldManpower;
        private MESManpower manpower;
    }


    @Data
    @AllArgsConstructor
    public static class ManpowerAttributesUpdatedEvent {
        private MESManpower manpower;
        private MESObjectAttribute oldAttribute;
        private MESObjectAttribute newAttribute;
    }

    // Manpower files event
    @Data
    @AllArgsConstructor
    public static class ManpowerFilesAddedEvent {
        private Integer manpower;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class ManpowerFoldersAddedEvent {
        private Integer manpower;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ManpowerFoldersDeletedEvent {
        private Integer manpower;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ManpowerFileDeletedEvent {
        private Integer manpower;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ManpowerFilesVersionedEvent {
        private Integer manpower;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class ManpowerFileRenamedEvent {
        private Integer manpower;
        private PLMFile oldFile;
        private PLMFile newFile;
        private String type;
    }

    @Data
    @AllArgsConstructor
    public static class ManpowerFileLockedEvent {
        private Integer manpower;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ManpowerFileUnlockedEvent {
        private Integer manpower;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ManpowerFileDownloadedEvent {
        private Integer manpower;
        private PLMFile file;
    }

    // Comment event
    @Data
    @AllArgsConstructor
    public static class ManpowerCommentAddedEvent {
        private MESManpower manpower;
        private Comment comment;
    }
}
