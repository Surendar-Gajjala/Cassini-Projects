package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.DataType;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.InspectionEvents;
import com.cassinisys.plm.model.mfr.PLMManufacturerPart;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.repo.mfr.ManufacturerPartRepository;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.repo.plm.ItemRevisionRepository;
import com.cassinisys.plm.repo.pqm.InspectionRepository;
import com.cassinisys.plm.repo.pqm.QualityTypeAttributeRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowDefinitionRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowStatusRepository;
import com.cassinisys.plm.service.activitystream.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class InspectionActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PLMWorkflowRepository workFlowRepository;
    @Autowired
    private PLMWorkflowStatusRepository workflowStatusRepository;
    @Autowired
    private InspectionRepository inspectionRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private ManufacturerPartRepository manufacturerPartRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private QualityTypeAttributeRepository qualityTypeAttributeRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository workflowDefinitionRepository;

    @Async
    @EventListener
    public void inspectionCreated(InspectionEvents.InspectionCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspection().getId());
        object.setType("inspection");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.inspection.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void inspectionBasicInfoUpdated(InspectionEvents.InspectionBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PQMInspection oldPlan = event.getOldInspection();
        PQMInspection newPlan = event.getInspection();

        object.setObject(newPlan.getId());
        object.setType("inspection");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.inspection.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getInspectionBasicInfoUpdatedJson(oldPlan, newPlan));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void inspectionAttributesUpdated(InspectionEvents.InspectionAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PQMInspection inspection = event.getInspection();

        PQMInspectionAttribute oldAtt = event.getOldAttribute();
        PQMInspectionAttribute newAtt = event.getNewAttribute();

        object.setObject(inspection.getId());
        object.setType("inspection");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.inspection.update.attributes");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getInspectionAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void inspectionFilesAdded(InspectionEvents.InspectionFilesAddedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.inspection.files.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspection().getId());
        object.setType("inspection");
        as.setObject(object);
        as.setData(getInspectionFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void inspectionFoldersAdded(InspectionEvents.InspectionFoldersAddedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.inspection.files.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspection().getId());
        object.setType("inspection");
        as.setObject(object);
        as.setData(getInspectionFoldersAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void inspectionFoldersDeleted(InspectionEvents.InspectionFoldersDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.inspection.files.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspection().getId());
        object.setType("inspection");
        as.setObject(object);
        as.setData(getInspectionFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void inspectionFileDeleted(InspectionEvents.InspectionFileDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.inspection.files.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspection().getId());
        object.setType("inspection");
        as.setObject(object);
        as.setData(getInspectionFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void inspectionFilesVersioned(InspectionEvents.InspectionFilesVersionedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.inspection.files.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspection().getId());
        object.setType("inspection");
        as.setObject(object);
        as.setData(getInspectionFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void inspectionFileRenamed(InspectionEvents.InspectionFileRenamedEvent event) {
        PLMFile oldFile = event.getOldFile();
        PLMFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals("Rename")) as.setActivity("plm.quality.inspection.files.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.quality.inspection.files.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspection().getId());
        object.setType("inspection");
        as.setObject(object);

        ASFileReplaceDto asFileReplaceDto = new ASFileReplaceDto(oldFile.getId(), oldFile.getName(), newFile.getId(), newFile.getName());
        try {
            as.setData(objectMapper.writeValueAsString(asFileReplaceDto));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void inspectionFileLocked(InspectionEvents.InspectionFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.inspection.files.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspection().getId());
        object.setType("inspection");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getInspectionFile().getId(), event.getInspectionFile().getName(), FileUtils.byteCountToDisplaySize(event.getInspectionFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void inspectionFileUnlocked(InspectionEvents.InspectionFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.inspection.files.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspection().getId());
        object.setType("inspection");
        as.setObject(object);
        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getInspectionFile().getId(), event.getInspectionFile().getName(), FileUtils.byteCountToDisplaySize(event.getInspectionFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void inspectionFileDownloaded(InspectionEvents.InspectionFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.inspection.files.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspection().getId());
        object.setType("inspection");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getInspectionFile().getId(), event.getInspectionFile().getName(), FileUtils.byteCountToDisplaySize(event.getInspectionFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        activityStreamService.create(as);
    }


    @Async
    @EventListener
    public void inspectionWorkflowStarted(InspectionEvents.InspectionWorkflowStartedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.inspection.workflow.start");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspection().getId());
        object.setType("inspection");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void inspectionWorkflowFinished(InspectionEvents.InspectionWorkflowFinishedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.inspection.workflow.finish");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspection().getId());
        object.setType("inspection");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void inspectionWorkflowPromoted(InspectionEvents.InspectionWorkflowPromotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.inspection.workflow.promote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspection().getId());
        object.setType("inspection");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        ActivityStreamObject source = new ActivityStreamObject();
        source.setObject(event.getFromActivity().getId());
        source.setType("workflowactivity");
        as.setSource(source);

        ActivityStreamObject target = new ActivityStreamObject();
        target.setObject(event.getToActivity().getId());
        target.setType("workflowactivity");
        as.setTarget(target);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void inspectionWorkflowDemoted(InspectionEvents.InspectionWorkflowDemotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.inspection.workflow.demote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspection().getId());
        object.setType("inspection");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        ActivityStreamObject source = new ActivityStreamObject();
        source.setObject(event.getFromActivity().getId());
        source.setType("workflowactivity");
        as.setSource(source);

        ActivityStreamObject target = new ActivityStreamObject();
        target.setObject(event.getToActivity().getId());
        target.setType("workflowactivity");
        as.setTarget(target);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void inspectionWorkflowHold(InspectionEvents.InspectionWorkflowHoldEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.inspection.workflow.hold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspection().getId());
        object.setType("inspection");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        ActivityStreamObject source = new ActivityStreamObject();
        source.setObject(event.getWorkflowActivity().getId());
        source.setType("workflowstatus");
        as.setSource(source);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void inspectionWorkflowUnhold(InspectionEvents.InspectionWorkflowUnholdEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.inspection.workflow.unhold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspection().getId());
        object.setType("inspection");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        ActivityStreamObject source = new ActivityStreamObject();
        source.setObject(event.getWorkflowActivity().getId());
        source.setType("workflowstatus");
        as.setSource(source);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void inspectionWorkflowChange(InspectionEvents.InspectionWorkflowChangeEvent event) {
        ActivityStream as = new ActivityStream();
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspection().getId());
        object.setType("inspection");
        as.setObject(object);

        String oldWorkflowName = "";
        if (event.getOldWorkflow() != null) {
            String revision = "";
            if (event.getOldWorkflow().getWorkflowRevision() != null) {
                PLMWorkflowDefinition workflowDefinition = workflowDefinitionRepository.findOne(event.getOldWorkflow().getWorkflowRevision());
                if (workflowDefinition != null) {
                    revision = " ( " + workflowDefinition.getRevision() + " )";
                }
            }
            oldWorkflowName = event.getOldWorkflow().getName() + "" + revision;
            as.setActivity("plm.quality.inspection.workflow.change");
        } else {
            as.setActivity("plm.quality.inspection.workflow.add");
        }
        String newWorkflowName = "";
        String revision = "";
        if (event.getWorkflow().getWorkflowRevision() != null) {
            PLMWorkflowDefinition workflowDefinition = workflowDefinitionRepository.findOne(event.getWorkflow().getWorkflowRevision());
            if (workflowDefinition != null) {
                revision = " ( " + workflowDefinition.getRevision() + " )";
            }
        }
        newWorkflowName = event.getWorkflow().getName() + "" + revision;

        ASPropertyChangeDTO asPropertyChangeDTO = new ASPropertyChangeDTO("Workflow", oldWorkflowName, newWorkflowName);

        try {
            as.setData(objectMapper.writeValueAsString(asPropertyChangeDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        activityStreamService.create(as);
    }


    @Async
    @EventListener
    public void inspectionCommentAdded(InspectionEvents.InspectionCommentAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.inspection.comment");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspection().getId());
        object.setType("inspection");
        as.setObject(object);
        as.setData(event.getComment().getComment());

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void planChecklistAssignedToAdd(InspectionEvents.InspectionChecklistAssignedToEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.inspection.checklist.assignedTo");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspection().getId());
        object.setType("inspection");
        as.setObject(object);
        Person person = personRepository.findOne(event.getInspectionChecklist().getAssignedTo());
        try {
            as.setData(objectMapper.writeValueAsString(new ASAssignedToChecklist(person.getFullName(), event.getInspectionChecklist().getPlanChecklist().getTitle())));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void inspectionChecklistUpdated(InspectionEvents.InspectionChecklistParameterUpdatedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.inspection.checklist.parameters");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspection().getId());
        object.setType("inspection");
        as.setObject(object);
        try {
            as.setData(objectMapper.writeValueAsString(new ASInspectionChecklistParamUpdate(event.getInspectionChecklist().getPlanChecklist().getTitle(), event.getParamActualValue().getResult(), event.getParamActualValue())));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void planChecklistUpdated(InspectionEvents.InspectionChecklistUpdatedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.inspection.checklist.update");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspection().getId());
        object.setType("inspection");
        as.setObject(object);
        as.setData(getInspectionChecklistUpdatedJson(event.getOldChecklist(), event.getInspectionChecklist()));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void inspectionRelatedItemsAdded(InspectionEvents.ItemInspectionRelatedItemAddedEvent event) {
        List<PQMItemInspectionRelatedItem> relatedItems = event.getRelatedItems();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.inspection.relatedItems.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspection().getId());
        object.setType("inspection");
        as.setObject(object);
        as.setData(getInspectionRelatedItemsAddedJson(relatedItems));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemInspectionRelatedItemsDeleted(InspectionEvents.ItemInspectionRelatedItemDeletedEvent event) {
        PQMItemInspectionRelatedItem relatedItem = event.getRelatedItem();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.inspection.relatedItems.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspection().getId());
        object.setType("inspection");
        as.setObject(object);
        as.setData(getInspectionRelatedItemsDeletedJson(relatedItem));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void materialInspectionRelatedItemsAdded(InspectionEvents.MaterialInspectionRelatedItemAddedEvent event) {
        List<PQMMaterialInspectionRelatedItem> relatedItems = event.getRelatedItems();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.inspection.relatedItems.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspection().getId());
        object.setType("inspection");
        as.setObject(object);
        as.setData(getMaterialInspectionRelatedItemsAddedJson(relatedItems));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void materialInspectionRelatedItemsDeleted(InspectionEvents.MaterialInspectionRelatedItemDeletedEvent event) {
        PQMMaterialInspectionRelatedItem relatedItem = event.getRelatedItem();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.inspection.relatedItems.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspection().getId());
        object.setType("inspection");
        as.setObject(object);
        as.setData(getMaterialInspectionRelatedItemsDeletedJson(relatedItem));

        activityStreamService.create(as);
    }

    @Override
    public String getConverterKey() {
        return "plm.quality.inspection";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            PQMInspection inspection = inspectionRepository.findOne(object.getObject());
            String inspectionType = "";
            if (inspection == null) return "";

            if (inspection.getObjectType().equals(ObjectType.valueOf(PLMObjectType.ITEMINSPECTION.toString()))) {
                inspectionType = "item";
            } else {
                inspectionType = "material";
            }

            String activity = as.getActivity();
            switch (activity) {
                case "plm.quality.inspection.create":
                    convertedString = getInspectionCreatedString(messageString, actor, inspectionType, inspection);
                    break;
                case "plm.quality.inspection.update.basicinfo":
                    convertedString = getInspectionBasicInfoUpdatedString(messageString, actor, inspection, inspectionType, as);
                    break;
                case "plm.quality.inspection.update.attributes":
                    convertedString = getInspectionAttributeUpdatedString(messageString, actor, inspection, as);
                    break;
                case "plm.quality.inspection.files.add":
                    convertedString = getInspectionFilesAddedString(messageString, inspection, as);
                    break;
                case "plm.quality.inspection.files.delete":
                    convertedString = getInspectionFileDeletedString(messageString, inspection, as);
                    break;
                case "plm.quality.inspection.files.folders.add":
                    convertedString = getInspectionFoldersAddedString(messageString, inspection, as);
                    break;
                case "plm.quality.inspection.files.folders.delete":
                    convertedString = getInspectionFoldersDeletedString(messageString, inspection, as);
                    break;
                case "plm.quality.inspection.files.version":
                    convertedString = getInspectionFilesVersionedString(messageString, inspection, as);
                    break;
                case "plm.quality.inspection.files.rename":
                    convertedString = getInspectionFileRenamedString(messageString, inspection, as);
                    break;
                case "plm.quality.inspection.files.replace":
                    convertedString = getInspectionFileRenamedString(messageString, inspection, as);
                    break;
                case "plm.quality.inspection.files.lock":
                    convertedString = getInspectionFileString(messageString, inspection, as);
                    break;
                case "plm.quality.inspection.files.unlock":
                    convertedString = getInspectionFileString(messageString, inspection, as);
                    break;
                case "plm.quality.inspection.files.download":
                    convertedString = getInspectionFileString(messageString, inspection, as);
                    break;
                case "plm.quality.inspection.workflow.start":
                    convertedString = getInspectionWorkflowStartString(messageString, inspection, as);
                    break;
                case "plm.quality.inspection.workflow.finish":
                    convertedString = getInspectionWorkflowFinishString(messageString, inspection, as);
                    break;
                case "plm.quality.inspection.workflow.promote":
                    convertedString = getInspectionWorkflowPromoteDemoteString(messageString, inspection, as);
                    break;
                case "plm.quality.inspection.workflow.demote":
                    convertedString = getInspectionWorkflowPromoteDemoteString(messageString, inspection, as);
                    break;
                case "plm.quality.inspection.workflow.hold":
                    convertedString = getInspectionWorkflowHoldUnholdString(messageString, inspection, as);
                    break;
                case "plm.quality.inspection.workflow.unhold":
                    convertedString = getInspectionWorkflowHoldUnholdString(messageString, inspection, as);
                    break;
                case "plm.quality.inspection.workflow.add":
                    convertedString = getInspectionWorkflowAddedString(messageString, inspection, as);
                    break;
                case "plm.quality.inspection.workflow.change":
                    convertedString = getInspectionWorkflowChangeString(messageString, inspection, as);
                    break;
                case "plm.quality.inspection.comment":
                    convertedString = getInspectionCommentString(messageString, inspection, as);
                    break;
                case "plm.quality.inspection.checklist.assignedTo":
                    convertedString = getAssignedToChecklistAddedString(messageString, inspection, as);
                    break;
                case "plm.quality.inspection.checklist.update":
                    convertedString = getAssignedToChecklistUpdatedString(messageString, actor, inspection, as);
                    break;
                case "plm.quality.inspection.checklist.parameters":
                    convertedString = getInspectionChecklistUpdatedString(messageString, inspection, as);
                    break;
                case "plm.quality.inspection.relatedItems.add":
                    convertedString = getInspectionRelatedItemsAddedString(messageString, inspection, as);
                    break;
                case "plm.quality.inspection.relatedItems.delete":
                    convertedString = getInspectionRelatedItemsDeletedString(messageString, inspection, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getInspectionCreatedString(String messageString, Person actor, String type, PQMInspection inspection) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), type, inspection.getInspectionNumber());
    }

    private String getInspectionRevisionCreatedString(String messageString, Person actor, PQMInspection inspection) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), inspection.getInspectionNumber());
    }

    private String getInspectionBasicInfoUpdatedString(String messageString, Person actor, PQMInspection inspection, String type, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), type, inspection.getInspectionNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.quality.inspection.update.basicinfo.property");

        String json = as.getData();
        try {
            List<ASPropertyChangeDTO> propChanges = objectMapper.readValue(json, new TypeReference<List<ASPropertyChangeDTO>>() {
            });
            propChanges.forEach(p -> {
                String s = addMarginToMessage(MessageFormat.format(updateString,
                        highlightValue(p.getProperty()),
                        highlightValue(p.getOldValue()),
                        highlightValue(p.getNewValue())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getInspectionBasicInfoUpdatedJson(PQMInspection oldInspection, PQMInspection inspection) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldInspection.getDeviationSummary();
        String newValue = inspection.getDeviationSummary();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Deviation Summary", oldValue, newValue));
        }

        oldValue = oldInspection.getNotes();
        newValue = inspection.getNotes();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Notes", oldValue, newValue));
        }

        String json = null;
        try {
            if (changes.size() > 0) {
                json = objectMapper.writeValueAsString(changes);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }


    private String getInspectionAttributesUpdatedJson(PQMInspectionAttribute oldAttribute, PQMInspectionAttribute newAttribute) {
        List<ASAttributeChangeDTO> changes = new ArrayList<>();

        String oldValue = "";
        if (oldAttribute != null) {
            oldValue = oldAttribute.getValueAsString();
        }
        String newValue = newAttribute.getValueAsString();

        PQMQualityTypeAttribute attDef = qualityTypeAttributeRepository.findOne(newAttribute.getId().getAttributeDef());
        if (!newValue.equals(oldValue)) {
            changes.add(new ASAttributeChangeDTO(attDef.getName(), oldValue, newValue));
        }

        String json = null;
        try {
            if (changes.size() > 0) {
                json = objectMapper.writeValueAsString(changes);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getInspectionAttributeUpdatedString(String messageString, Person actor, PQMInspection inspection, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), inspection.getInspectionNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.quality.inspection.update.attributes.attribute");

        String json = as.getData();
        try {
            List<ASAttributeChangeDTO> propChanges = objectMapper.readValue(json, new TypeReference<List<ASAttributeChangeDTO>>() {
            });
            propChanges.forEach(p -> {
                String s = addMarginToMessage(MessageFormat.format(updateString,
                        highlightValue(p.getAttribute()),
                        highlightValue(p.getOldValue()),
                        highlightValue(p.getNewValue())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getInspectionFilesAddedJson(List<PLMFile> files) {
        String json = null;

        List<ASNewFileDTO> ASNewFileDtos = new ArrayList<>();
        files.forEach(f -> ASNewFileDtos.add(new ASNewFileDTO(f.getId(), f.getName(), FileUtils.byteCountToDisplaySize(f.getSize()))));

        try {
            json = objectMapper.writeValueAsString(ASNewFileDtos);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getInspectionFoldersAddedJson(PLMFile file) {
        String json = null;

        List<ASNewFileDTO> ASNewFileDtos = new ArrayList<>();
        ASNewFileDtos.add(new ASNewFileDTO(file.getId(), file.getName(), FileUtils.byteCountToDisplaySize(file.getSize())));
        try {
            json = objectMapper.writeValueAsString(ASNewFileDtos);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getInspectionFoldersDeletedJson(PLMFile file) {
        String json = null;

        List<ASNewFileDTO> ASNewFileDtos = new ArrayList<>();
        ASNewFileDtos.add(new ASNewFileDTO(file.getId(), file.getName(), FileUtils.byteCountToDisplaySize(file.getSize())));
        try {
            json = objectMapper.writeValueAsString(ASNewFileDtos);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getInspectionFilesAddedString(String messageString, PQMInspection inspection, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), inspection.getInspectionNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.quality.inspection.files.add.file");

        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            files.forEach(f -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(f.getName()), f.getSize()));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getInspectionFoldersAddedString(String messageString, PQMInspection inspection, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), inspection.getInspectionNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getInspectionFoldersDeletedString(String messageString, PQMInspection inspection, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), inspection.getInspectionNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getInspectionFileDeletedString(String messageString, PQMInspection inspection, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), inspection.getInspectionNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getInspectionFilesVersionedJson(List<PLMFile> files) {
        String json = null;

        List<ASVersionedFileDTO> ASVersionedFileDtos = new ArrayList<>();
        files.forEach(f -> ASVersionedFileDtos.add(new ASVersionedFileDTO(f.getId(), f.getName(), f.getVersion() - 1, f.getVersion())));

        try {
            json = objectMapper.writeValueAsString(ASVersionedFileDtos);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getInspectionFilesVersionedString(String messageString, PQMInspection inspection, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), inspection.getInspectionNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.quality.inspection.files.version.file");

        String json = as.getData();
        try {
            List<ASVersionedFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASVersionedFileDTO>>() {
            });
            files.forEach(f -> {
                String s = addMarginToMessage(MessageFormat.format(fileString,
                        highlightValue(f.getName()),
                        highlightValue("" + f.getOldVersion()),
                        highlightValue("" + f.getNewVersion())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getInspectionFileRenamedString(String messageString, PQMInspection inspection, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    inspection.getInspectionNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getInspectionFileString(String messageString, PQMInspection inspection, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    inspection.getInspectionNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getInspectionWorkflowStartString(String messageString, PQMInspection inspection, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                inspection.getInspectionNumber());
    }

    private String getInspectionWorkflowFinishString(String messageString, PQMInspection inspection, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                inspection.getInspectionNumber());
    }

    private String getInspectionWorkflowPromoteDemoteString(String messageString, PQMInspection inspection, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();
        ActivityStreamObject target = as.getTarget();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus fromActivity = workflowStatusRepository.findOne(source.getObject());
        PLMWorkflowStatus toActivity = workflowStatusRepository.findOne(target.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                inspection.getInspectionNumber(),
                highlightValue(fromActivity.getName()),
                highlightValue(toActivity.getName()));
    }

    private String getInspectionWorkflowHoldUnholdString(String messageString, PQMInspection inspection, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus plmWorkflowActivity = workflowStatusRepository.findOne(source.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflowActivity.getName()),
                highlightValue(plmWorkflow.getName()),
                inspection.getInspectionNumber());
    }

    private String getInspectionWorkflowAddedString(String messageString, PQMInspection inspection, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getNewValue()),
                    inspection.getInspectionNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getInspectionWorkflowChangeString(String messageString, PQMInspection inspection, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getOldValue()),
                    highlightValue(workflow.getNewValue()),
                    inspection.getInspectionNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }


    private String getInspectionCommentString(String messageString, PQMInspection inspection, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                inspection.getInspectionNumber());

        String s = highlightValue("Comment: ");
        s += as.getData();
        s = addMarginToMessage(s);

        message += s;

        return message;
    }

    private String getAssignedToChecklistAddedString(String messageString, PQMInspection inspection, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            ASAssignedToChecklist planChecklist = objectMapper.readValue(json, new TypeReference<ASAssignedToChecklist>() {
            });
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(planChecklist.getFullName()), highlightValue(planChecklist.getChecklistName()), inspection.getInspectionNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getAssignedToChecklistUpdatedString(String messageString, Person actor, PQMInspection inspection, ActivityStream as) {
        StringBuffer sb = new StringBuffer();

        String updateString = activityStreamResourceBundle.getString("plm.quality.inspection.checklist.update.property");

        String json = as.getData();
        try {
            List<ASInspectionChecklistUpdate> propChanges = objectMapper.readValue(json, new TypeReference<List<ASInspectionChecklistUpdate>>() {
            });
            String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), highlightValue(propChanges.get(0).getChecklistName()), inspection.getInspectionNumber());
            sb.append(activityString);
            propChanges.forEach(p -> {
                String s = addMarginToMessage(MessageFormat.format(updateString,
                        highlightValue(p.getProperty()),
                        highlightValue(p.getOldValue()),
                        highlightValue(p.getNewValue())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getInspectionChecklistUpdatedString(String messageString, PQMInspection inspection, ActivityStream as) {

        String activityString = "";
        StringBuffer sb = new StringBuffer();
        try {
            String json = as.getData();
            ASInspectionChecklistParamUpdate planChecklist = objectMapper.readValue(json, new TypeReference<ASInspectionChecklistParamUpdate>() {
            });
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(planChecklist.getChecklistName()), inspection.getInspectionNumber());

            sb.append(activityString);

            String fileString = activityStreamResourceBundle.getString("plm.quality.inspection.checklist.parameters.parameter");
            String actualValue = "";
            String expectedValue = "";
            if (planChecklist.getParamActualValue().getParam().getExpectedValueType().equals(DataType.TEXT)) {
                actualValue = planChecklist.getParamActualValue().getTextValue();
                expectedValue = planChecklist.getParamActualValue().getParam().getExpectedValue().getTextValue();
            } else if (planChecklist.getParamActualValue().getParam().getExpectedValueType().equals(DataType.DOUBLE)) {
                actualValue = "" + planChecklist.getParamActualValue().getDoubleValue();
                expectedValue = "" + planChecklist.getParamActualValue().getParam().getExpectedValue().getDoubleValue();
            } else if (planChecklist.getParamActualValue().getParam().getExpectedValueType().equals(DataType.INTEGER)) {
                actualValue = "" + planChecklist.getParamActualValue().getIntegerValue();
                expectedValue = "" + planChecklist.getParamActualValue().getParam().getExpectedValue().getIntegerValue();
            } else if (planChecklist.getParamActualValue().getParam().getExpectedValueType().equals(DataType.DATE)) {
                actualValue = "" + planChecklist.getParamActualValue().getDateValue();
                expectedValue = "" + planChecklist.getParamActualValue().getParam().getExpectedValue().getDateValue();
            } else if (planChecklist.getParamActualValue().getParam().getExpectedValueType().equals(DataType.BOOLEAN)) {
                actualValue = "" + planChecklist.getParamActualValue().getBooleanValue();
                expectedValue = "" + planChecklist.getParamActualValue().getParam().getExpectedValue().getBooleanValue();
            }
            String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(expectedValue), highlightValue(actualValue), highlightValue(planChecklist.getResult().toString())));
            sb.append(s);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getInspectionChecklistUpdatedJson(PQMInspectionChecklist oldChecklist, PQMInspectionChecklist inspectionChecklist) {
        List<ASInspectionChecklistUpdate> changes = new ArrayList<>();

        Integer oldPerson = oldChecklist.getAssignedTo();
        Integer newPerson = inspectionChecklist.getAssignedTo();
        if (oldPerson == null) {
            oldPerson = 0;
        }
        if (newPerson == null) {
            newPerson = 0;
        }
        if (!oldPerson.equals(newPerson)) {
            Person person = personRepository.findOne(oldPerson);
            Person newPersonObject = personRepository.findOne(newPerson);
            if (person == null && newPersonObject != null) {
                changes.add(new ASInspectionChecklistUpdate(inspectionChecklist.getPlanChecklist().getTitle(), "Assigned To", "", newPersonObject.getFullName()));
            } else if (person != null && newPersonObject != null) {
                changes.add(new ASInspectionChecklistUpdate(inspectionChecklist.getPlanChecklist().getTitle(), "Assigned To", person.getFullName(), newPersonObject.getFullName()));
            }
        }

        String oldValue = oldChecklist.getNotes();
        String newValue = inspectionChecklist.getNotes();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASInspectionChecklistUpdate(inspectionChecklist.getPlanChecklist().getTitle(), "Notes", oldValue, newValue));
        }

        oldValue = oldChecklist.getResult().toString();
        newValue = inspectionChecklist.getResult().toString();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASInspectionChecklistUpdate(inspectionChecklist.getPlanChecklist().getTitle(), "Result", oldValue, newValue));
        }

        String json = null;
        try {
            if (changes.size() > 0) {
                json = objectMapper.writeValueAsString(changes);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getInspectionRelatedItemsAddedJson(List<PQMItemInspectionRelatedItem> relatedItems) {
        List<ASNewPRProblemItem> asNewPRProblemItems = new ArrayList<>();
        for (PQMItemInspectionRelatedItem problemItem : relatedItems) {
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(problemItem.getItem());
            PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
            ASNewPRProblemItem asNewPRProblemItem = new ASNewPRProblemItem(itemRevision.getId(), item.getItemName(), item.getItemNumber(), itemRevision.getRevision(), itemRevision.getLifeCyclePhase().getPhase());
            asNewPRProblemItems.add(asNewPRProblemItem);
        }
        String json = null;
        try {
            json = objectMapper.writeValueAsString(asNewPRProblemItems);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getInspectionRelatedItemsDeletedJson(PQMItemInspectionRelatedItem relatedItem) {
        String json = null;
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(relatedItem.getItem());
        PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
        ASNewPRProblemItem asNewPRProblemItem = new ASNewPRProblemItem(itemRevision.getId(), item.getItemName(), item.getItemNumber(), itemRevision.getRevision(), itemRevision.getLifeCyclePhase().getPhase());

        List<ASNewPRProblemItem> asNewPRProblemItems = new ArrayList<>();
        asNewPRProblemItems.add(asNewPRProblemItem);
        try {
            json = objectMapper.writeValueAsString(asNewPRProblemItems);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getMaterialInspectionRelatedItemsAddedJson(List<PQMMaterialInspectionRelatedItem> relatedItems) {
        List<ASNewNCRProblemItem> asNewPRProblemItems = new ArrayList<>();
        for (PQMMaterialInspectionRelatedItem problemItem : relatedItems) {
            PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(problemItem.getMaterial());
            ASNewNCRProblemItem asNewPRProblemItem = new ASNewNCRProblemItem(manufacturerPart.getId(), manufacturerPart.getPartName(), manufacturerPart.getPartNumber(),
                    manufacturerPart.getMfrPartType().getName());
            asNewPRProblemItems.add(asNewPRProblemItem);
        }
        String json = null;
        try {
            json = objectMapper.writeValueAsString(asNewPRProblemItems);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getMaterialInspectionRelatedItemsDeletedJson(PQMMaterialInspectionRelatedItem relatedItem) {
        String json = null;
        PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(relatedItem.getMaterial());
        ASNewNCRProblemItem asNewPRProblemItem = new ASNewNCRProblemItem(manufacturerPart.getId(), manufacturerPart.getPartName(), manufacturerPart.getPartNumber(),
                manufacturerPart.getMfrPartType().getName());

        List<ASNewNCRProblemItem> asNewPRProblemItems = new ArrayList<>();
        asNewPRProblemItems.add(asNewPRProblemItem);
        try {
            json = objectMapper.writeValueAsString(asNewPRProblemItems);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }


    private String getInspectionRelatedItemsAddedString(String messageString, PQMInspection inspection, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), inspection.getInspectionNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = "";
        if (inspection.getObjectType().equals(ObjectType.valueOf(PLMObjectType.ITEMINSPECTION.toString()))) {
            fileString = activityStreamResourceBundle.getString("plm.quality.inspection.relatedItems.add.item");
        } else {
            fileString = activityStreamResourceBundle.getString("plm.quality.inspection.relatedItems.add.part");
        }

        String json = as.getData();
        try {
            if (inspection.getObjectType().equals(ObjectType.valueOf(PLMObjectType.ITEMINSPECTION.toString()))) {
                List<ASNewPRProblemItem> itemList = objectMapper.readValue(json, new TypeReference<List<ASNewPRProblemItem>>() {
                });
                for (ASNewPRProblemItem item : itemList) {
                    String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(item.getItemName()), highlightValue(item.getRevision()),
                            highlightValue(item.getLifecyclePhase())));
                    sb.append(s);
                }
            } else {
                List<ASNewNCRProblemItem> partList = objectMapper.readValue(json, new TypeReference<List<ASNewNCRProblemItem>>() {
                });
                for (ASNewNCRProblemItem part : partList) {
                    String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(part.getPartName()), highlightValue(part.getPartNumber()),
                            highlightValue(part.getPartType())));
                    sb.append(s);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getInspectionRelatedItemsDeletedString(String messageString, PQMInspection inspection, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            if (inspection.getObjectType().equals(ObjectType.valueOf(PLMObjectType.ITEMINSPECTION.toString()))) {
                List<ASNewPRProblemItem> asNewPRProblemItems = objectMapper.readValue(json, new TypeReference<List<ASNewPRProblemItem>>() {
                });
                ASNewPRProblemItem asNewPRProblemItem = asNewPRProblemItems.get(0);
                activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(asNewPRProblemItem.getItemName()),
                        highlightValue(asNewPRProblemItem.getRevision()), highlightValue(asNewPRProblemItem.getLifecyclePhase()), inspection.getInspectionNumber());
            } else {
                List<ASNewNCRProblemItem> asNewPRProblemItems = objectMapper.readValue(json, new TypeReference<List<ASNewNCRProblemItem>>() {
                });
                ASNewNCRProblemItem asNewPRProblemItem = asNewPRProblemItems.get(0);
                activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(asNewPRProblemItem.getPartNumber()),
                        inspection.getInspectionNumber());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }
}
