package com.cassinisys.plm.event;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.plm.model.cm.PLMChange;
import com.cassinisys.plm.model.mfr.PLMItemManufacturerPart;
import com.cassinisys.plm.model.pgc.PGCItemSpecification;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.service.activitystream.dto.ASBOMConfigItemInclusion;
import com.cassinisys.plm.service.activitystream.dto.ASBomConfigValues;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class ItemEvents {

    // Item events
    public interface ItemBaseEvent {
        PLMItemRevision getItemRevision();
    }

    @Data
    @AllArgsConstructor
    public static class ItemCreatedEvent implements ItemBaseEvent {
        private PLMItem item;
        private PLMItemRevision itemRevision;
    }

    @Data
    @AllArgsConstructor
    public static class ItemRevisionCreatedEvent implements ItemBaseEvent {
        private PLMItem item;
        private PLMItemRevision itemRevision;
    }

    @Data
    @AllArgsConstructor
    public static class ItemBasicInfoUpdatedEvent implements ItemBaseEvent {
        private PLMItem plmOldItem;
        private PLMItem item;
        private PLMItemRevision itemRevision;
    }

    @Data
    @AllArgsConstructor
    public static class ItemEffectiveDatesUpdatedEvent implements ItemBaseEvent {
        private PLMItem item;
        private PLMItemRevision oldRevision;
        private PLMItemRevision itemRevision;
    }

    @Data
    @AllArgsConstructor
    public static class ItemIncorporateUpdatedEvent implements ItemBaseEvent {
        private PLMItem item;
        private PLMItemRevision oldRevision;
        private PLMItemRevision itemRevision;
    }

    @Data
    @AllArgsConstructor
    public static class ItemAttributesUpdatedEvent implements ItemBaseEvent {
        private PLMItem item;
        private PLMItemRevision itemRevision;
        private PLMItemAttribute oldAttribute;
        private PLMItemAttribute newAttribute;
    }

    @Data
    @AllArgsConstructor
    public static class ItemRevisionAttributesUpdatedEvent implements ItemBaseEvent {
        private PLMItem item;
        private PLMItemRevision itemRevision;
        private PLMItemRevisionAttribute oldAttribute;
        private PLMItemRevisionAttribute newAttribute;
    }

    @Data
    @AllArgsConstructor
    public static class ItemCopiedEvent implements ItemBaseEvent {
        private PLMItemRevision itemRevision;
        private PLMItemRevision fromPlmItemRevision;
    }

    @Data
    @AllArgsConstructor
    public static class ItemReleasedEvent implements ItemBaseEvent {
        private PLMItem item;
        private PLMItemRevision itemRevision;
        private PLMChange changeOrder;
    }

    @Data
    @AllArgsConstructor
    public static class ItemStatusChangedEvent implements ItemBaseEvent {
        private PLMItem item;
        private PLMItemRevision itemRevision;
        private PLMChange changeOrder;
        private PLMLifeCyclePhase oldStatus;
        private PLMLifeCyclePhase newStatus;
    }

    // Item files event
    @Data
    @AllArgsConstructor
    public static class ItemFilesAddedEvent implements ItemBaseEvent {
        private PLMItemRevision itemRevision;
        private List<PLMItemFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class ItemFileDeletedEvent implements ItemBaseEvent {
        private PLMItemRevision itemRevision;
        private PLMItemFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ItemFilesVersionedEvent implements ItemBaseEvent {
        private PLMItemRevision itemRevision;
        private List<PLMItemFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class ItemFileRenamedEvent implements ItemBaseEvent {
        private PLMItemRevision itemRevision;
        private PLMItemFile oldFile;
        private PLMItemFile newFile;
        private String type;
    }

    @Data
    @AllArgsConstructor
    public static class ItemFileLockedEvent implements ItemBaseEvent {
        private PLMItemRevision itemRevision;
        private PLMItemFile itemFile;
    }

    @Data
    @AllArgsConstructor
    public static class ItemSubscribeEvent implements ItemBaseEvent {
        private PLMItemRevision itemRevision;
        private PLMItem item;
    }

    @Data
    @AllArgsConstructor
    public static class ItemUnSubscribeEvent implements ItemBaseEvent {
        private PLMItemRevision itemRevision;
        private PLMItem item;
    }

    @Data
    @AllArgsConstructor
    public static class ItemFileUnlockedEvent implements ItemBaseEvent {
        private PLMItemRevision itemRevision;
        private PLMItemFile itemFile;
    }

    @Data
    @AllArgsConstructor
    public static class ItemFileDownloadedEvent implements ItemBaseEvent {
        private PLMItemRevision itemRevision;
        private PLMItemFile itemFile;
    }


    @Data
    @AllArgsConstructor
    public static class ItemFoldersAddedEvent implements ItemBaseEvent {
        private PLMItemRevision itemRevision;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ItemFoldersDeletedEvent implements ItemBaseEvent {
        private PLMItemRevision itemRevision;
        private PLMFile file;
    }

    // Item workflow events
    @Data
    @AllArgsConstructor
    public static class ItemWorkflowStartedEvent implements ItemBaseEvent {
        private PLMItemRevision itemRevision;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class ItemWorkflowPromotedEvent implements ItemBaseEvent {
        private PLMItemRevision itemRevision;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class ItemWorkflowDemotedEvent implements ItemBaseEvent {
        private PLMItemRevision itemRevision;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class ItemWorkflowFinishedEvent implements ItemBaseEvent {
        private PLMItemRevision itemRevision;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class ItemWorkflowHoldEvent implements ItemBaseEvent {
        private PLMItemRevision itemRevision;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class ItemWorkflowUnholdEvent implements ItemBaseEvent {
        private PLMItemRevision itemRevision;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class ItemWorkflowChangeEvent implements ItemBaseEvent {
        private PLMItemRevision itemRevision;
        private PLMWorkflow oldWorkflow;
        private PLMWorkflow workflow;
    }

    // Comment event
    @Data
    @AllArgsConstructor
    public static class ItemCommentAddedEvent implements ItemBaseEvent {
        private PLMItemRevision itemRevision;
        private Comment comment;
    }

    // BOM Events
    @Data
    @AllArgsConstructor
    public static class ItemBomItemAddedEvent implements ItemBaseEvent {
        private PLMItemRevision itemRevision;
        private PLMBom bomItem;
    }

    @Data
    @AllArgsConstructor
    public static class ItemBomItemsAddedEvent implements ItemBaseEvent {
        private PLMItemRevision itemRevision;
        private List<PLMBom> bomItems;
    }

    @Data
    @AllArgsConstructor
    public static class ItemBomItemDeletedEvent implements ItemBaseEvent {
        private PLMItemRevision itemRevision;
        private PLMBom bomItem;
    }

    @Data
    @AllArgsConstructor
    public static class ItemBomItemsDeletedEvent implements ItemBaseEvent {
        private PLMItemRevision itemRevision;
        private List<PLMBom> bomItems;
    }

    @Data
    @AllArgsConstructor
    public static class ItemBomItemUpdatedEvent implements ItemBaseEvent {
        private PLMItemRevision itemRevision;
        private PLMBom oldBomItem;
        private PLMBom bomItem;
    }

    @Data
    @AllArgsConstructor
    public static class ItemBomItemsUpdatedEvent implements ItemBaseEvent {
        private PLMItemRevision itemRevision;
        private List<PLMBom> bomItems;
    }

    @Data
    @AllArgsConstructor
    public static class ItemMfrPartsAddedEvent implements ItemBaseEvent {
        private PLMItemRevision itemRevision;
        private List<PLMItemManufacturerPart> itemManufacturerParts;
    }

    @Data
    @AllArgsConstructor
    public static class ItemMfrPartDeletedEvent implements ItemBaseEvent {
        private PLMItemRevision itemRevision;
        private PLMItemManufacturerPart itemManufacturerPart;
    }

    @Data
    @AllArgsConstructor
    public static class ItemRelatedItemsAddedEvent implements ItemBaseEvent {
        private PLMItemRevision itemRevision;
        private List<PLMRelatedItem> relatedItems;
    }

    @Data
    @AllArgsConstructor
    public static class ItemRelatedItemDeletedEvent implements ItemBaseEvent {
        private PLMItemRevision itemRevision;
        private PLMRelatedItem relatedItem;
    }

    @Data
    @AllArgsConstructor
    public static class ItemAlternatePartsAddedEvent implements ItemBaseEvent {
        private PLMItemRevision itemRevision;
        private List<PLMAlternatePart> parts;
    }

    @Data
    @AllArgsConstructor
    public static class ItemAlternatePartsDeletedEvent implements ItemBaseEvent {
        private PLMItemRevision itemRevision;
        private List<PLMAlternatePart> parts;    }


    @Data
    @AllArgsConstructor
    public static class ItemAlternatePartUpdatedEvent {
        private PLMItemRevision itemRevision;
        private PLMAlternatePart oldAlternatePart;
        private PLMAlternatePart newAlternatePart;
    }

    @Data
    @AllArgsConstructor
    public static class ItemInstanceAddedEvent implements ItemBaseEvent {
        private PLMItemRevision itemRevision;
        private List<PLMItem> instances;
    }

    @Data
    @AllArgsConstructor
    public static class ItemInstanceDeletedEvent implements ItemBaseEvent {
        private PLMItemRevision itemRevision;
        private PLMItem instance;
    }

    @Data
    @AllArgsConstructor
    public static class BomConfigItemInclusionsEvent implements ItemBaseEvent {
        private PLMItemRevision itemRevision;
        private List<ASBOMConfigItemInclusion> configItemInclusions;
    }

    @Data
    @AllArgsConstructor
    public static class BomNonConfigItemInclusionsEvent implements ItemBaseEvent {
        private PLMItemRevision itemRevision;
        private List<ASBOMConfigItemInclusion> configItemInclusions;
    }

    @Data
    @AllArgsConstructor
    public static class BomConfigAttributeExclusionsEvent implements ItemBaseEvent {
        private PLMItemRevision itemRevision;
        private List<ASBOMConfigItemInclusion> configItemInclusions;
    }

    @Data
    @AllArgsConstructor
    public static class BomItemResolvedEvent implements ItemBaseEvent {
        private PLMItem item;
        private PLMItemRevision itemRevision;
        private PLMItem fromItem;
        private PLMItem toItem;
    }

    @Data
    @AllArgsConstructor
    public static class BomConfigurationAddedEvent implements ItemBaseEvent {
        private PLMItemRevision itemRevision;
        private BOMConfiguration bomConfiguration;
        private BOMConfiguration existBomConfig;
        private List<ASBomConfigValues> bomConfigValues;
    }

    @Data
    @AllArgsConstructor
    public static class BomConfigurationUpdatedEvent implements ItemBaseEvent {
        private PLMItemRevision itemRevision;
        private BOMConfiguration bomConfiguration;
        private BOMConfiguration existBomConfig;
        private List<ASBomConfigValues> bomConfigValues;
    }

    @Data
    @AllArgsConstructor
    public static class ItemPromotedEvent {
        private PLMItem item;
        private PLMItemRevision itemRevision;
        private PLMLifeCyclePhase fromLifeCyclePhase;
        private PLMLifeCyclePhase toLifeCyclePhase;
    }

    @Data
    @AllArgsConstructor
    public static class ItemDemotedEvent {
        private PLMItem item;
        private PLMItemRevision itemRevision;
        private PLMLifeCyclePhase fromLifeCyclePhase;
        private PLMLifeCyclePhase toLifeCyclePhase;
    }

    //Item Compliance Add event
    @Data
    @AllArgsConstructor
    public static class ItemComplianceAddEvent {
        private Integer item;
        private List<PGCItemSpecification> specifications;
    }

    @Data
    @AllArgsConstructor
    public static class ItemComplianceDeleteEvent {
        private Integer item;
        private PGCItemSpecification itemSpecification;
    }

}
