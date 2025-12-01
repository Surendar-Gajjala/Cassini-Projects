package com.cassinisys.plm.event;

import com.cassinisys.plm.model.mes.MESBOPInstance;
import com.cassinisys.plm.model.mes.MESBOPInstanceOperationFile;
import com.cassinisys.plm.model.mes.MESBOPOperationPart;
import com.cassinisys.plm.model.mes.MESBOPOperationResource;
import com.cassinisys.plm.model.plm.PLMFile;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class BOPInstanceOperationEvents {
    // BOP Operation files event
    @Data
    @AllArgsConstructor
    public static class BOPOperationFilesAddedEvent {
        private Integer operation;
        private List<MESBOPInstanceOperationFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class BOPOperationFileDeletedEvent {
        private Integer operation;
        private MESBOPInstanceOperationFile file;
    }

    @Data
    @AllArgsConstructor
    public static class BOPOperationFilesVersionedEvent {
        private Integer operation;
        private List<MESBOPInstanceOperationFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class BOPOperationFileRenamedEvent {
        private Integer operation;
        private MESBOPInstanceOperationFile oldFile;
        private MESBOPInstanceOperationFile newFile;
        private String type;
    }

    @Data
    @AllArgsConstructor
    public static class BOPOperationFileLockedEvent {
        private Integer operation;
        private MESBOPInstanceOperationFile bopFile;
    }

    @Data
    @AllArgsConstructor
    public static class BOPOperationFileUnlockedEvent {
        private Integer operation;
        private MESBOPInstanceOperationFile bopFile;
    }

    @Data
    @AllArgsConstructor
    public static class BOPOperationFileDownloadedEvent {
        private Integer operation;
        private MESBOPInstanceOperationFile bopFile;
    }

    @Data
    @AllArgsConstructor
    public static class BOPOperationFoldersAddedEvent {
        private Integer operation;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class BOPOperationFoldersDeletedEvent {
        private Integer operation;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class BOPOperationPartsAddedEvent {
        private Integer operation;
        private List<MESBOPOperationPart> operationParts;
    }

    @Data
    @AllArgsConstructor
    public static class BOPOperationPartDeletedEvent {
        private Integer operation;
        private MESBOPOperationPart operationPart;
    }

    @Data
    @AllArgsConstructor
    public static class BOPOperationPartUpdatedEvent {
        private Integer operation;
        private MESBOPOperationPart oldOperationPart;
        private MESBOPOperationPart operationPart;
    }

    @Data
    @AllArgsConstructor
    public static class BOPOperationResourceUpdatedEvent {
        private Integer operation;
        private MESBOPOperationResource oldOperationResource;
        private MESBOPOperationResource operationResource;
    }

    @Data
    @AllArgsConstructor
    public static class BOPOperationResourcesAddedEvent {
        private Integer operation;
        private List<MESBOPOperationResource> operationResources;
    }

    @Data
    @AllArgsConstructor
    public static class BOPOperationResourceDeletedEvent {
        private Integer operation;
        private MESBOPOperationResource operationResource;
    }
}
