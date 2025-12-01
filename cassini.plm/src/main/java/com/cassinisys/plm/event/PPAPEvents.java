package com.cassinisys.plm.event;

import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.pqm.PQMPPAP;
import com.cassinisys.plm.model.pqm.PQMPPAPAttribute;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class PPAPEvents {
    @Data
    @AllArgsConstructor
    public static class PPAPCreatedEvent {
        private PQMPPAP ppap;
    }

    @Data
    @AllArgsConstructor
    public static class PPAPBasicInfoUpdatedEvent {
        private PQMPPAP oldPpap;
        private PQMPPAP newPpap;
    }

    // PPAP Checklist event
    @Data
    @AllArgsConstructor
    public static class PPAPFilesAddedEvent {
        private Integer ppap;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class PPAPFoldersAddedEvent {
        private Integer ppap;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class PPAPFoldersDeletedEvent {
        private PQMPPAP ppap;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class PPAPFileDeletedEvent {
        private PQMPPAP ppap;
        private PLMFile file;
    }


    @Data
    @AllArgsConstructor
    public static class PPAPAttributesUpdatedEvent {
        private PQMPPAP ppap;
        private PQMPPAPAttribute oldAttribute;
        private PQMPPAPAttribute newAttribute;
    }

    @Data
    @AllArgsConstructor
    public static class PPAPFilesVersionedEvent {
        private Integer ppap;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class PPAPFileRenamedEvent {
        private PQMPPAP ppap;
        private PLMFile oldFile;
        private PLMFile newFile;
        private String type;
    }

    @Data
    @AllArgsConstructor
    public static class PPAPFileLockedEvent {
        private PQMPPAP ppap;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class PPAPFileUnlockedEvent {
        private PQMPPAP ppap;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class PPAPFileDownloadedEvent {
        private PQMPPAP ppap;
        private PLMFile file;
    }

}
