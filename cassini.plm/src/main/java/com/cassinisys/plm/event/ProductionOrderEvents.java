package com.cassinisys.plm.event;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.plm.model.mes.MESProductionOrder;
import com.cassinisys.plm.model.plm.PLMFile;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class ProductionOrderEvents {

    // ProductionOrder Events
    

    @Data
    @AllArgsConstructor
    public static class ProductionOrderCreatedEvent {
        private MESProductionOrder productionOrder;
    }
    @Data
    @AllArgsConstructor
    public static class ProductionOrderBasicInfoUpdatedEvent {
        private MESProductionOrder oldProductionOrder;
        private MESProductionOrder productionOrder;
        
    }

    // ProductionOrder files event
    @Data
    @AllArgsConstructor
    public static class ProductionOrderFilesAddedEvent  {
        private Integer productionOrder;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class ProductionOrderFileDeletedEvent  {
        private Integer productionOrder;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ProductionOrderFilesVersionedEvent {
        private Integer productionOrder;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class ProductionOrderFileRenamedEvent  {
        private Integer productionOrder;
        private PLMFile oldFile;
        private PLMFile newFile;
        private String type;
    }

    @Data
    @AllArgsConstructor
    public static class ProductionOrderFileLockedEvent  {
        private Integer productionOrder;
        private PLMFile file;
    }
    
    @Data
    @AllArgsConstructor
    public static class ProductionOrderFileUnlockedEvent   {
        private Integer productionOrder;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ProductionOrderFileDownloadedEvent  {
        private Integer productionOrder;
        private PLMFile file;
    }


    @Data
    @AllArgsConstructor
    public static class ProductionOrderFoldersAddedEvent  {
        private Integer productionOrder;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ProductionOrderFoldersDeletedEvent  {
        private Integer productionOrder;
        private PLMFile file;
    }

    // Comment event
    @Data
    @AllArgsConstructor
    public static class ProductionOrderCommentAddedEvent {
        private MESProductionOrder productionOrder;
        private Comment comment;
    }

}
