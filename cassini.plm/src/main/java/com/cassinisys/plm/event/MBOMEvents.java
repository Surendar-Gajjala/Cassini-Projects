package com.cassinisys.plm.event;

import java.util.List;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.plm.model.mes.MESMBOM;
import com.cassinisys.plm.model.mes.MESBOMItem;
import com.cassinisys.plm.model.mes.MESMBOMFile;
import com.cassinisys.plm.model.mes.MESMBOMRevision;
import com.cassinisys.plm.model.mes.MESObjectAttribute;

import lombok.AllArgsConstructor;
import lombok.Data;

public final class MBOMEvents {

    public interface MbomBaseEvent {
        MESMBOMRevision getMbomRevision();
    }


    @Data
    @AllArgsConstructor
    public static class MbomCreatedEvent implements MbomBaseEvent {
        private MESMBOM mbom;
        private MESMBOMRevision mbomRevision;

    }
    @Data
    @AllArgsConstructor
    public static class MbomItemsCreatedEvent implements MbomBaseEvent {
        private List<MESBOMItem> mesMbomItems;
        private MESMBOMRevision mbomRevision;

    }
    @Data
    @AllArgsConstructor
    public static class MbomItemUpdatedEvent implements MbomBaseEvent {
        private MESBOMItem newMesMbomItem;
        private MESBOMItem oldMesMbomItem;
        private MESMBOMRevision mbomRevision;

    }
    @Data
    @AllArgsConstructor
    public static class MbomItemDeletedEvent implements MbomBaseEvent {
        private MESBOMItem mbomItem;
        private MESMBOMRevision mbomRevision;

    }


    @Data
    @AllArgsConstructor
    public static class MbomBasicInfoUpdatedEvent implements MbomBaseEvent {
        private MESMBOM oldMbom;
        private MESMBOM newMbom;
        private MESMBOMRevision mbomRevision;
    }

    @Data
    @AllArgsConstructor
    public static class MbomAttributesUpdatedEvent implements MbomBaseEvent {
        private String parentType;
        private Integer objectId;
        private Enum objectType;
        private MESMBOMRevision mbomRevision;
        private MESObjectAttribute oldAttribute;
        private MESObjectAttribute newAttribute;
    }

    // MBOM files event
    @Data
    @AllArgsConstructor
    public static class MbomFilesAddedEvent implements MbomBaseEvent {
        private MESMBOMRevision mbomRevision;
        private List<MESMBOMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class MbomFoldersAddedEvent implements MbomBaseEvent {
        private MESMBOMRevision mbomRevision;
        private MESMBOMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class MbomFoldersDeletedEvent implements MbomBaseEvent {
        private MESMBOMRevision mbomRevision;
        private MESMBOMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class MbomFileDeletedEvent implements MbomBaseEvent {
        private MESMBOMRevision mbomRevision;
        private MESMBOMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class MbomFilesVersionedEvent implements MbomBaseEvent {
        private MESMBOMRevision mbomRevision;
        private List<MESMBOMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class MbomFileRenamedEvent implements MbomBaseEvent {
        private MESMBOMRevision mbomRevision;
        private MESMBOMFile oldFile;
        private MESMBOMFile newFile;
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
    //     private MESMBOMFile file;
    // }

    @Data
    @AllArgsConstructor
    public static class MbomFileDownloadedEvent implements MbomBaseEvent {
        private MESMBOMRevision mbomRevision;
        private MESMBOMFile file;
    }

    // Comment event
    @Data
    @AllArgsConstructor
    public static class MbomCommentAddedEvent implements MbomBaseEvent {
        private MESMBOMRevision mbomRevision;
        private Comment comment;
    }
    
    
}
