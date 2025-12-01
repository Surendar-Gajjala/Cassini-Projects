package com.cassinisys.plm.event;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class QCREvents {

    @Data
    @AllArgsConstructor
    public static class QCRCreatedEvent {
        private PQMQCR qcr;
    }

    @Data
    @AllArgsConstructor
    public static class QCRBasicInfoUpdatedEvent {
        private PQMQCR oldQcr;
        private PQMQCR qcr;

    }

    @Data
    @AllArgsConstructor
    public static class QCRAttributesUpdatedEvent {
        private PQMQCR qcr;
        private PQMQCRAttribute oldAttribute;
        private PQMQCRAttribute newAttribute;
    }

    // QCR files event
    @Data
    @AllArgsConstructor
    public static class QCRFilesAddedEvent {
        private PQMQCR qcr;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class QCRFoldersAddedEvent {
        private PQMQCR qcr;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class QCRFoldersDeletedEvent {
        private PQMQCR qcr;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class QCRFileDeletedEvent {
        private PQMQCR qcr;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class QCRFilesVersionedEvent {
        private PQMQCR qcr;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class QCRFileRenamedEvent {
        private PQMQCR qcr;
        private PLMFile oldFile;
        private PLMFile newFile;
        private String type;
    }

    @Data
    @AllArgsConstructor
    public static class QCRFileLockedEvent {
        private PQMQCR qcr;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class QCRFileUnlockedEvent {
        private PQMQCR qcr;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class QCRFileDownloadedEvent {
        private PQMQCR qcr;
        private PLMFile file;
    }

    // QCR workflow events
    @Data
    @AllArgsConstructor
    public static class QCRWorkflowStartedEvent {
        private PQMQCR qcr;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class QCRWorkflowPromotedEvent {
        private PQMQCR qcr;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class QCRWorkflowDemotedEvent {
        private PQMQCR qcr;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class QCRWorkflowFinishedEvent {
        private PQMQCR qcr;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class QCRWorkflowHoldEvent {
        private PQMQCR qcr;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class QCRWorkflowUnHoldEvent {
        private PQMQCR qcr;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class QCRWorkflowChangeEvent {
        private PQMQCR qcr;
        private PLMWorkflow oldWorkflow;
        private PLMWorkflow workflow;
    }

    // Comment event
    @Data
    @AllArgsConstructor
    public static class QCRCommentAddedEvent {
        private PQMQCR qcr;
        private Comment comment;
    }

    @Data
    @AllArgsConstructor
    public static class QCRPrProblemSourceAddedEvent {
        private PQMQCR qcr;
        private List<PQMQCRAggregatePR> aggregatePRs;
    }

    @Data
    @AllArgsConstructor
    public static class QCRNCRProblemSourceAddedEvent {
        private PQMQCR qcr;
        private List<PQMQCRAggregateNCR> aggregateNCRs;
    }

    @Data
    @AllArgsConstructor
    public static class QCRPRProblemSourceDeletedEvent {
        private PQMQCR qcr;
        private PQMQCRAggregatePR aggregatePR;
    }

    @Data
    @AllArgsConstructor
    public static class QCRNCRProblemSourceDeletedEvent {
        private PQMQCR qcr;
        private PQMQCRAggregateNCR aggregateNCR;
    }

    @Data
    @AllArgsConstructor
    public static class QCRProblemItemAddedEvent {
        private PQMQCR qcr;
        private List<PQMQCRProblemItem> problemItems;
    }

    @Data
    @AllArgsConstructor
    public static class QCRProblemItemUpdateEvent {
        private PQMQCR qcr;
        private PQMQCRProblemItem oldProblemItem;
        private PQMQCRProblemItem problemItems;
    }

    @Data
    @AllArgsConstructor
    public static class QCRProblemItemDeletedEvent {
        private PQMQCR qcr;
        private PQMQCRProblemItem problemItem;
    }

    @Data
    @AllArgsConstructor
    public static class QCRProblemMaterialAddedEvent {
        private PQMQCR qcr;
        private List<PQMQCRProblemMaterial> problemMaterials;
    }

    @Data
    @AllArgsConstructor
    public static class QCRProblemMaterialUpdateEvent {
        private PQMQCR qcr;
        private PQMQCRProblemMaterial oldProblemItem;
        private PQMQCRProblemMaterial problemItems;
    }

    @Data
    @AllArgsConstructor
    public static class QCRProblemMaterialDeletedEvent {
        private PQMQCR qcr;
        private PQMQCRProblemMaterial problemMaterial;
    }

    @Data
    @AllArgsConstructor
    public static class QCRRelatedItemAddedEvent {
        private PQMQCR qcr;
        private List<PQMQCRRelatedItem> relatedItems;
    }

    @Data
    @AllArgsConstructor
    public static class QCRRelatedItemDeletedEvent {
        private PQMQCR qcr;
        private PQMQCRRelatedItem relatedItem;
    }

    @Data
    @AllArgsConstructor
    public static class QCRRelatedMaterialAddedEvent {
        private PQMQCR qcr;
        private List<PQMQCRRelatedMaterial> relatedMaterials;
    }

    @Data
    @AllArgsConstructor
    public static class QCRRelatedMaterialDeletedEvent {
        private PQMQCR qcr;
        private PQMQCRRelatedMaterial relatedMaterial;
    }

    @Data
    @AllArgsConstructor
    public static class QCRCAPAAddedEvent {
        private PQMQCR qcr;
        private PQMQCRCAPA capa;
    }

    @Data
    @AllArgsConstructor
    public static class QCRCAPAUpdatedEvent {
        private PQMQCR qcr;
        private PQMQCRCAPA oldCapa;
        private PQMQCRCAPA capa;
    }

    @Data
    @AllArgsConstructor
    public static class QCRCAPAAuditEvent {
        private PQMQCR qcr;
        private PQMQCRCAPA capa;
    }

    @Data
    @AllArgsConstructor
    public static class QCRCAPADeletedEvent {
        private PQMQCR qcr;
        private PQMQCRCAPA capa;
    }
}
