package com.cassinisys.platform.events;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.model.custom.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class CustomObjectEvents {

    @Data
    @AllArgsConstructor
    public static class CustomObjectTypeCreatedEvent {
        private ObjectType type;
        private Object object;
    }

    @Data
    @AllArgsConstructor
    public static class CustomObjectTypeDeletedEvent {
        private ObjectType type;
        private Object object;
    }

    @Data
    @AllArgsConstructor
    public static class CustomObjectTypeUpdatedEvent {
        private ObjectType type;
        private Object oldType;
        private Object newType;
    }

    @Data
    @AllArgsConstructor
    public static class CustomObjectTypeAttributeCreatedEvent {
        private ObjectType type;
        private Object object;
    }

    @Data
    @AllArgsConstructor
    public static class CustomObjectTypeAttributeUpdatedEvent {
        private ObjectType type;
        private Object oldValue;
        private Object newValue;
    }

    @Data
    @AllArgsConstructor
    public static class CustomObjectTypeAttributeDeletedEvent {
        private ObjectType type;
        private Object object;
    }


    @Data
    @AllArgsConstructor
    public static class CustomObjectCreatedEvent {
        private CustomObject customObject;
    }

    @Data
    @AllArgsConstructor
    public static class CustomObjectBasicInfoUpdatedEvent {
        private CustomObject oldCustomObject;
        private CustomObject newCustomObject;
    }

    @Data
    @AllArgsConstructor
    public static class CustomObjectAttributesUpdatedEvent {
        private Integer objectId;
        private Enum objectType;
        private CustomObjectAttribute oldAttribute;
        private CustomObjectAttribute newAttribute;
    }

    // CustomObject files event
    @Data
    @AllArgsConstructor
    public static class CustomObjectFilesAddedEvent {
        private Integer customObject;
        private List<CustomFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class CustomObjectFoldersAddedEvent {
        private Integer customObject;
        private CustomFile file;
    }

    @Data
    @AllArgsConstructor
    public static class CustomObjectFoldersDeletedEvent {
        private Integer customObject;
        private CustomFile file;
    }

    @Data
    @AllArgsConstructor
    public static class CustomObjectFileDeletedEvent {
        private Integer customObject;
        private CustomFile file;
    }

    @Data
    @AllArgsConstructor
    public static class CustomObjectFilesVersionedEvent {
        private Integer customObject;
        private List<CustomFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class CustomObjectFileRenamedEvent {
        private Integer customObject;
        private CustomFile oldFile;
        private CustomFile newFile;
        private String type;
    }

    @Data
    @AllArgsConstructor
    public static class CustomObjectFileLockedEvent {
        private Integer customObject;
        private CustomFile file;
    }

    @Data
    @AllArgsConstructor
    public static class CustomObjectFileUnlockedEvent {
        private Integer customObject;
        private CustomFile file;
    }

    @Data
    @AllArgsConstructor
    public static class CustomObjectFileDownloadedEvent {
        private Integer customObject;
        private CustomFile file;
    }

    // Comment event
    @Data
    @AllArgsConstructor
    public static class CustomObjectCommentAddedEvent {
        private CustomObject customObject;
        private Comment comment;
    }

    // BOM Events
    @Data
    @AllArgsConstructor
    public static class CustomObjectBomAddedEvent {
        private CustomObject customObject;
        private CustomObjectBom customObjectBom;
    }

    @Data
    @AllArgsConstructor
    public static class CustomObjectBomsAddedEvent {
        private CustomObject customObject;
        private List<CustomObjectBom> customObjectBoms;
    }

    @Data
    @AllArgsConstructor
    public static class CustomObjectBomDeletedEvent {
        private CustomObject customObject;
        private CustomObjectBom customObjectBom;
    }

    @Data
    @AllArgsConstructor
    public static class CustomObjectBomUpdatedEvent {
        private CustomObject customObject;
        private CustomObjectBom oldCustomObjectBom;
        private CustomObjectBom customObjectBom;
    }

    // Related Events
    @Data
    @AllArgsConstructor
    public static class CustomObjectRelatedAddedEvent {
        private CustomObject customObject;
        private CustomObjectRelated customObjectRelated;
    }

    @Data
    @AllArgsConstructor
    public static class CustomObjectRelatedsAddedEvent {
        private CustomObject customObject;
        private List<CustomObjectRelated> customObjectRelateds;
    }

    @Data
    @AllArgsConstructor
    public static class CustomObjectRelatedDeletedEvent {
        private CustomObject customObject;
        private CustomObjectRelated customObjectRelated;
    }

    @Data
    @AllArgsConstructor
    public static class CustomObjectRelatedUpdatedEvent {
        private CustomObject customObject;
        private CustomObjectRelated oldCustomObjectRelated;
        private CustomObjectRelated customObjectRelated;
    }


    // workflow events
    @Data
    @AllArgsConstructor
    public static class CustomObjectWorkflowStartedEvent {
        private CustomObject customObject;
        private Integer workflowId;
        private String workflowName;
    }

    @Data
    @AllArgsConstructor
    public static class CustomObjectWorkflowPromotedEvent {
        private CustomObject customObject;
        private Integer workflowId;
        private String workflowName;
        private Integer fromActivityId;
        private String fromActivityName;
        private Integer toActivityId;
        private String toActivityName;
    }

    @Data
    @AllArgsConstructor
    public static class CustomObjectWorkflowDemotedEvent {
        private CustomObject customObject;
        private Integer workflowId;
        private String workflowName;
        private Integer fromActivityId;
        private String fromActivityName;
        private Integer toActivityId;
        private String toActivityName;
    }

    @Data
    @AllArgsConstructor
    public static class CustomObjectWorkflowFinishedEvent {
        private CustomObject customObject;
        private Integer workflowId;
        private String workflowName;
    }

    @Data
    @AllArgsConstructor
    public static class CustomObjectWorkflowHoldEvent {
        private CustomObject customObject;
        private Integer workflowId;
        private String workflowName;
        private Integer activityId;
        private String activityName;
    }

    @Data
    @AllArgsConstructor
    public static class CustomObjectWorkflowUnholdEvent {
        private CustomObject customObject;
        private Integer workflowId;
        private String workflowName;
        private Integer activityId;
        private String activityName;
    }


}
