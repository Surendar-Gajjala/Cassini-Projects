package com.cassinisys.plm.event;

import com.cassinisys.plm.model.plm.PLMDocument;
import com.cassinisys.plm.model.plm.PLMDocumentReviewer;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class DocumentEvents {

    @Data
    @AllArgsConstructor
    public static class DocumentFilesAddedEvent {
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class DocumentFoldersAddedEvent {
        private PLMDocument file;
    }

    @Data
    @AllArgsConstructor
    public static class DocumentFoldersDeletedEvent {
        private PLMDocument file;
    }

    @Data
    @AllArgsConstructor
    public static class DocumentFileDeletedEvent {
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class DocumentFilesVersionedEvent {
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class DocumentFileRenamedEvent {
        private PLMFile oldFile;
        private PLMFile newFile;
        private String type;
    }

    @Data
    @AllArgsConstructor
    public static class DocumentFileLockedEvent {
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class DocumentFileUnlockedEvent {
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class DocumentFileDownloadedEvent {
        private PLMFile file;
    }


    @Data
    @AllArgsConstructor
    public static class DocumentPromotedEvent {
        private Integer id;
        private Integer file;
        private PLMLifeCyclePhase fromLifeCyclePhase;
        private PLMLifeCyclePhase toLifeCyclePhase;
        private Enum type;
    }

    @Data
    @AllArgsConstructor
    public static class DocumentRevisedEvent {
        private Integer id;
        private Integer file;
        private PLMFile oldDocument;
        private PLMFile document;
        private Enum type;
    }

    @Data
    @AllArgsConstructor
    public static class DocumentDemotedEvent {
        private Integer id;
        private Integer file;
        private PLMLifeCyclePhase fromLifeCyclePhase;
        private PLMLifeCyclePhase toLifeCyclePhase;
        private Enum type;
    }


    @Data
    @AllArgsConstructor
    public static class DocumentReviewerAddedEvent {
        private Integer id;
        private PLMFile document;
        private PLMDocumentReviewer reviewer;
        private Enum type;
    }

    @Data
    @AllArgsConstructor
    public static class DocumentReviewerUpdateEvent {
        Integer id;
        private PLMFile document;
        private PLMDocumentReviewer reviewer;
        private Enum type;
    }

    @Data
    @AllArgsConstructor
    public static class DocumentReviewerDeletedEvent {
        private Integer id;
        private PLMFile document;
        private PLMDocumentReviewer reviewer;
        private Enum type;
    }

    @Data
    @AllArgsConstructor
    public static class DocumentReviewerSubmittedEvent {
        private Integer id;
        private PLMFile document;
        private PLMDocumentReviewer reviewer;
        private Enum type;
    }

}
