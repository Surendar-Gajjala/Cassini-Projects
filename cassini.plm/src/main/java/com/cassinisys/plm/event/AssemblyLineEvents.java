package com.cassinisys.plm.event;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.plm.model.mes.MESObjectAttribute;
import com.cassinisys.plm.model.mes.MESAssemblyLine;
import com.cassinisys.plm.model.mes.MESWorkCenter;
import com.cassinisys.plm.model.plm.PLMFile;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class AssemblyLineEvents {
    @Data
    @AllArgsConstructor
    public static class AssemblyLineCreatedEvent {
        private MESAssemblyLine assemblyLine;
    }

    @Data
    @AllArgsConstructor
    public static class AssemblyLineBasicInfoUpdatedEvent {
        private MESAssemblyLine oldAssemblyLine;
        private MESAssemblyLine newAssemblyLine;
    }

    @Data
    @AllArgsConstructor
    public static class AssemblyLineAttributesUpdatedEvent {
        private String parentType;
        private Integer objectId;
        private Enum objectType;
        private MESObjectAttribute oldAttribute;
        private MESObjectAttribute newAttribute;
    }

    // AssemblyLine files event
    @Data
    @AllArgsConstructor
    public static class AssemblyLineFilesAddedEvent {
        private Integer assemblyLine;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class AssemblyLineFoldersAddedEvent {
        private Integer assemblyLine;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class AssemblyLineFoldersDeletedEvent {
        private Integer assemblyLine;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class AssemblyLineFileDeletedEvent {
        private Integer assemblyLine;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class AssemblyLineFilesVersionedEvent {
        private Integer assemblyLine;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class AssemblyLineFileRenamedEvent {
        private Integer assemblyLine;
        private PLMFile oldFile;
        private PLMFile newFile;
        private String type;
    }

    @Data
    @AllArgsConstructor
    public static class AssemblyLineFileLockedEvent {
        private Integer assemblyLine;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class AssemblyLineFileUnlockedEvent {
        private Integer assemblyLine;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class AssemblyLineFileDownloadedEvent {
        private Integer assemblyLine;
        private PLMFile file;
    }

    // Comment event
    @Data
    @AllArgsConstructor
    public static class AssemblyLineCommentAddedEvent {
        private MESAssemblyLine assemblyLine;
        private Comment comment;
    }


    @Data
    @AllArgsConstructor
    public static class AssemblyLineWorkCentersAddedEvent {
        private MESAssemblyLine assemblyLine;
        private List<MESWorkCenter> workCenters;
    }

    @Data
    @AllArgsConstructor
    public static class AssemblyLineWorkCenterDeletedEvent {
        private MESAssemblyLine assemblyLine;
        private MESWorkCenter workCenter;
    }
}