package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.VarianceEvents;
import com.cassinisys.plm.model.cm.*;
import com.cassinisys.plm.model.mfr.PLMManufacturerPart;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.repo.cm.ChangeTypeAttributeRepository;
import com.cassinisys.plm.repo.cm.VarianceRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerPartRepository;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.repo.plm.ItemRevisionRepository;
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

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class VarianceActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PLMWorkflowRepository workFlowRepository;
    @Autowired
    private PLMWorkflowStatusRepository workflowStatusRepository;
    @Autowired
    private VarianceRepository varianceRepository;
    @Autowired
    private ChangeTypeAttributeRepository changeTypeAttributeRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ManufacturerPartRepository manufacturerPartRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository workflowDefinitionRepository;
    @Autowired
    private CommonActivityStream commonActivityStream;

    @Async
    @EventListener
    public void varianceCreated(VarianceEvents.VarianceCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getVariance().getId());
        object.setType("variance");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.variance.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void varianceBasicInfoUpdated(VarianceEvents.VarianceBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PLMVariance olddco = event.getOldVariance();
        PLMVariance newdco = event.getNewVariance();

        object.setObject(newdco.getId());
        object.setType("variance");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.variance.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getVarianceBasicInfoUpdatedJson(olddco, newdco));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void varianceAttributesUpdated(VarianceEvents.VarianceAttributesUpdatedEvent event) throws ParseException {
        ActivityStreamObject object = new ActivityStreamObject();

        PLMVariance eco = event.getVariance();

        PLMChangeAttribute oldAtt = event.getOldAttribute();
        PLMChangeAttribute newAtt = event.getNewAttribute();

        object.setObject(eco.getId());
        object.setType("variance");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.variance.update.attributes");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getECOAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void varianceFilesAdded(VarianceEvents.VarianceFilesAddedEvent event) {
        List<PLMChangeFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.variance.files.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getVariance().getId());
        object.setType("variance");
        as.setObject(object);
        as.setData(getECOFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void varianceFoldersAdded(VarianceEvents.VarianceFoldersAddedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.variance.files.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getVariance().getId());
        object.setType("variance");
        as.setObject(object);
        as.setData(getECOFoldersAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void varianceFoldersDeleted(VarianceEvents.VarianceFoldersDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.variance.files.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getVariance().getId());
        object.setType("variance");
        as.setObject(object);
        as.setData(getECOFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void varianceFileDeleted(VarianceEvents.VarianceFileDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.variance.files.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getVariance().getId());
        object.setType("variance");
        as.setObject(object);
        as.setData(getECOFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void varianceFilesVersioned(VarianceEvents.VarianceFilesVersionedEvent event) {
        List<PLMChangeFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.variance.files.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getVariance().getId());
        object.setType("variance");
        as.setObject(object);
        as.setData(getECOFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void ecoFileRenamed(VarianceEvents.VarianceFileRenamedEvent event) {
        PLMFile oldFile = event.getOldFile();
        PLMFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals("Rename")) as.setActivity("plm.change.variance.files.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.change.variance.files.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getVariance().getId());
        object.setType("variance");
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
    public void varianceFileLocked(VarianceEvents.VarianceFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.variance.files.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getVariance().getId());
        object.setType("variance");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getFile().getId(), event.getFile().getName(), FileUtils.byteCountToDisplaySize(event.getFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void varianceFileUnlocked(VarianceEvents.VarianceFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.variance.files.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getVariance().getId());
        object.setType("variance");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getFile().getId(), event.getFile().getName(), FileUtils.byteCountToDisplaySize(event.getFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void varianceFileDownloaded(VarianceEvents.VarianceFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.variance.files.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getVariance().getId());
        object.setType("variance");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getFile().getId(), event.getFile().getName(), FileUtils.byteCountToDisplaySize(event.getFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }


    @Async
    @EventListener
    public void varianceWorkflowStarted(VarianceEvents.VarianceWorkflowStartedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.variance.workflow.start");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getVariance().getId());
        object.setType("variance");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void varianceWorkflowChanged(VarianceEvents.VarianceWorkflowChangedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.variance.workflow.change");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getVariance().getId());
        as.setData(event.getPlmWorkflow());
        object.setType("variance");
        as.setObject(object);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void varianceWorkflowFinished(VarianceEvents.VarianceWorkflowFinishedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.variance.workflow.finish");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getVariance().getId());
        object.setType("variance");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void varianceWorkflowPromoted(VarianceEvents.VarianceWorkflowPromotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.variance.workflow.promote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getVariance().getId());
        object.setType("variance");
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
    public void varianceWorkflowDemoted(VarianceEvents.VarianceWorkflowDemotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.variance.workflow.demote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getVariance().getId());
        object.setType("variance");
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
    public void varianceWorkflowHold(VarianceEvents.VarianceWorkflowHoldEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.variance.workflow.hold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getVariance().getId());
        object.setType("variance");
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
    public void varianceWorkflowUnhold(VarianceEvents.VarianceWorkflowUnholdEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.variance.workflow.unhold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getVariance().getId());
        object.setType("variance");
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
    public void mcoWorkflowChange(VarianceEvents.VarianceWorkflowChangeEvent event) {
        ActivityStream as = new ActivityStream();
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getVariance().getId());
        object.setType("variance");
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
            as.setActivity("plm.change.variance.workflow.change");
        } else {
            as.setActivity("plm.change.variance.workflow.add");
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
    public void varianceCommentAdded(VarianceEvents.VarianceCommentAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.variance.comment");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getVariance().getId());
        object.setType("variance");
        as.setObject(object);
        as.setData(event.getComment().getComment());

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void varianceAffectedItemsAdded(VarianceEvents.VarianceAffectedItemAddedEvent event) {
        List<PLMVarianceAffectedItem> affectedItems = event.getAffectedItems();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.variance.affectedItems.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getVariance().getId());
        object.setType("variance");
        as.setObject(object);
        as.setData(getECOAffectedItemsAddedJson(affectedItems));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void varianceAffectedItemUpdated(VarianceEvents.VarianceAffectedItemUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PLMVarianceAffectedItem olddco = event.getOldaffectedItem();
        PLMVarianceAffectedItem newdco = event.getNewaffectedItem();

        object.setObject(newdco.getVariance());
        object.setType("variance");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.variance.update.affectedItems");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getVarianceAffectedUpdatedJson(olddco, newdco));
        if (!as.getData().equals("[]")) activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void varianceAffectedItemsDeleted(VarianceEvents.VarianceAffectedItemDeletedEvent event) {
        PLMVarianceAffectedItem affectedItem = event.getAffectedItem();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.variance.affectedItems.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getVariance().getId());
        object.setType("variance");
        as.setObject(object);
        as.setData(getECOAffectedItemsDeletedJson(affectedItem));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void varianceAffectedPartsAdded(VarianceEvents.VarianceAffectedPartAddedEvent event) {
        List<PLMVarianceAffectedMaterial> affectedItems = event.getAffectedMaterials();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.variance.affectedParts.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getVariance().getId());
        object.setType("variance");
        as.setObject(object);
        as.setData(getECOAffectedPartsAddedJson(affectedItems));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void varianceAffectedPartUpdated(VarianceEvents.VarianceAffectedPartUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PLMVarianceAffectedMaterial olddco = event.getOldaffectedMaterial();
        PLMVarianceAffectedMaterial newdco = event.getNewaffectedMaterial();

        object.setObject(newdco.getVariance());
        object.setType("variance");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.variance.update.affectedParts");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getVarianceAffectedUpdatedJson(olddco, newdco));
        if (!as.getData().equals("[]")) activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void varianceAffectedPartsDeleted(VarianceEvents.VarianceAffectedPartDeletedEvent event) {
        PLMVarianceAffectedMaterial affectedItem = event.getAffectedMaterial();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.variance.affectedParts.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getVariance().getId());
        object.setType("variance");
        as.setObject(object);
        as.setData(getECOAffectedPartsDeletedJson(affectedItem));

        activityStreamService.create(as);
    }

    @Override
    public String getConverterKey() {
        return "plm.change.variance";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            PLMVariance eco = varianceRepository.findOne(object.getObject());

            if (eco == null) return "";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.change.variance.create":
                    convertedString = getECOCreatedString(messageString, actor, eco);
                    break;
                case "plm.change.variance.update.basicinfo":
                    convertedString = getECOBasicInfoUpdatedString(messageString, actor, eco, as);
                    break;
                case "plm.change.variance.update.attributes":
                    convertedString = getECOAttributeUpdatedString(messageString, actor, eco, as);
                    break;
                case "plm.change.variance.files.add":
                    convertedString = getECOFilesAddedString(messageString, eco, as);
                    break;
                case "plm.change.variance.files.delete":
                    convertedString = getECOFileDeletedString(messageString, eco, as);
                    break;
                case "plm.change.variance.files.folders.add":
                    convertedString = getECOFoldersAddedString(messageString, eco, as);
                    break;
                case "plm.change.variance.files.folders.delete":
                    convertedString = getECOFoldersDeletedString(messageString, eco, as);
                    break;
                case "plm.change.variance.files.version":
                    convertedString = getECOFilesVersionedString(messageString, eco, as);
                    break;
                case "plm.change.variance.files.rename":
                    convertedString = getECOFileRenamedString(messageString, eco, as);
                    break;
                case "plm.change.variance.files.replace":
                    convertedString = getECOFileRenamedString(messageString, eco, as);
                    break;
                case "plm.change.variance.files.lock":
                    convertedString = getECOFileString(messageString, eco, as);
                    break;
                case "plm.change.variance.files.unlock":
                    convertedString = getECOFileString(messageString, eco, as);
                    break;
                case "plm.change.variance.files.download":
                    convertedString = getECOFileString(messageString, eco, as);
                    break;
                case "plm.change.variance.workflow.start":
                    convertedString = getECOWorkflowStartString(messageString, eco, as);
                    break;
                case "plm.change.variance.workflow.finish":
                    convertedString = getECOWorkflowFinishString(messageString, eco, as);
                    break;
                case "plm.change.variance.workflow.promote":
                    convertedString = getECOWorkflowPromoteDemoteString(messageString, eco, as);
                    break;
                case "plm.change.variance.workflow.demote":
                    convertedString = getECOWorkflowPromoteDemoteString(messageString, eco, as);
                    break;
                case "plm.change.variance.workflow.hold":
                    convertedString = getECOWorkflowHoldUnholdString(messageString, eco, as);
                    break;
                case "plm.change.variance.workflow.unhold":
                    convertedString = getECOWorkflowHoldUnholdString(messageString, eco, as);
                    break;
                case "plm.change.variance.workflow.add":
                    convertedString = getECOWorkflowAddedString(messageString, eco, as);
                    break;
                case "plm.change.variance.workflow.change":
                    convertedString = getECOWorkflowChangeString(messageString, eco, as);
                    break;
                case "plm.change.variance.comment":
                    convertedString = getECOCommentString(messageString, eco, as);
                    break;
                case "plm.change.variance.affectedItems.add":
                    convertedString = getECOAffectedItemsAddedString(messageString, eco, as);
                    break;
                case "plm.change.variance.update.affectedItems":
                    convertedString = getVarianceAffectedItemUpdatedString(messageString, actor, eco, as);
                    break;
                case "plm.change.variance.affectedItems.delete":
                    convertedString = getECOAffectedItemsDeletedString(messageString, eco, as);
                    break;
                case "plm.change.variance.affectedParts.add":
                    convertedString = getECOAffectedPartsAddedString(messageString, eco, as);
                    break;
                case "plm.change.variance.update.affectedParts":
                    convertedString = getVarianceAffectedPartUpdatedString(messageString, actor, eco, as);
                    break;
                case "plm.change.variance.affectedParts.delete":
                    convertedString = getECOAffectedPartsDeletedString(messageString, eco, as);
                    break;
                case "plm.change.variance.relatedItems.add":
                    convertedString = getDcoRelatedItemsAddedString(messageString, eco, as);
                    break;
                case "plm.change.variance.relatedItems.delete":
                    convertedString = getDcoRelatedItemsDeletedString(messageString, eco, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getECOCreatedString(String messageString, Person actor, PLMVariance eco) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), eco.getVarianceType().toString().toLowerCase(), eco.getVarianceNumber());
    }

    private String getECOBasicInfoUpdatedString(String messageString, Person actor, PLMVariance eco, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), eco.getVarianceType().toString().toLowerCase(), eco.getVarianceNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.change.variance.update.basicinfo.property");

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

    private String getECOAttributesUpdatedJson(PLMChangeAttribute oldAttribute, PLMChangeAttribute newAttribute) throws ParseException {
        PLMChangeTypeAttribute attDef = changeTypeAttributeRepository.findOne(newAttribute.getId().getAttributeDef());
        List<ASAttributeChangeDTO> changes = commonActivityStream.getAttributeUpdateJsonData(oldAttribute, newAttribute, attDef);
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

    private String getECOAttributeUpdatedString(String messageString, Person actor, PLMVariance eco, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), eco.getVarianceType().toString().toLowerCase(), eco.getVarianceNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.change.variance.update.attributes.attribute");
        String propertyUpdateString = activityStreamResourceBundle.getString("plm.change.variance.update.attributes.attribute").substring(0, 21);

        String json = as.getData();
        try {
            List<ASAttributeChangeDTO> propChanges = objectMapper.readValue(json, new TypeReference<List<ASAttributeChangeDTO>>() {
            });
            propChanges.forEach(p -> {
                String s = null;
                if (p.getAttribute().equals("MULTIPLELIST") || p.getAttribute().equals("OBJECT") || p.getAttribute().equals("IMAGE") || p.getAttribute().equals("ATTACHMENT")) {
                    s = addMarginToMessage(MessageFormat.format(propertyUpdateString, highlightValue(p.getAttribute().toLowerCase())));
                    sb.append(s);
                } else {
                    s = addMarginToMessage(MessageFormat.format(updateString,
                            highlightValue(p.getAttribute()),
                            highlightValue(p.getOldValue()),
                            highlightValue(p.getNewValue())));
                    sb.append(s);
                }
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getECOFilesAddedJson(List<PLMChangeFile> files) {
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

    private String getECOFoldersAddedJson(PLMFile file) {
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

    private String getECOFoldersDeletedJson(PLMFile file) {
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

    private String getECOFilesAddedString(String messageString, PLMVariance eco, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), eco.getVarianceType().toString().toLowerCase(), eco.getVarianceNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.change.variance.files.add.file");

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

    private String getECOFoldersAddedString(String messageString, PLMVariance eco, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), eco.getVarianceType().toString().toLowerCase(), highlightValue(fileDTO.getName()), eco.getVarianceNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getECOFoldersDeletedString(String messageString, PLMVariance eco, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), eco.getVarianceType().toString().toLowerCase(), highlightValue(fileDTO.getName()), eco.getVarianceNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getECOFileDeletedString(String messageString, PLMVariance eco, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), eco.getVarianceType().toString().toLowerCase(), highlightValue(fileDTO.getName()), eco.getVarianceNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getECOFilesVersionedJson(List<PLMChangeFile> files) {
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

    private String getECOFilesVersionedString(String messageString, PLMVariance eco, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), eco.getVarianceType().toString().toLowerCase(), eco.getVarianceNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.change.variance.files.version.file");

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

    private String getECOFileRenamedString(String messageString, PLMVariance eco, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    eco.getVarianceType().toString().toLowerCase(),
                    eco.getVarianceNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getECOFileString(String messageString, PLMVariance eco, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    eco.getVarianceType().toString().toLowerCase(),
                    eco.getVarianceNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getECOWorkflowStartString(String messageString, PLMVariance eco, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                eco.getVarianceType().toString().toLowerCase(),
                eco.getVarianceNumber());
    }

    private String getECOWorkflowFinishString(String messageString, PLMVariance eco, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                eco.getVarianceType().toString().toLowerCase(),
                eco.getVarianceNumber());
    }

    private String getECOWorkflowPromoteDemoteString(String messageString, PLMVariance eco, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();
        ActivityStreamObject target = as.getTarget();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus fromActivity = workflowStatusRepository.findOne(source.getObject());
        PLMWorkflowStatus toActivity = workflowStatusRepository.findOne(target.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                eco.getVarianceType().toString().toLowerCase(),
                eco.getVarianceNumber(),
                highlightValue(fromActivity.getName()),
                highlightValue(toActivity.getName()));
    }

    private String getECOWorkflowHoldUnholdString(String messageString, PLMVariance eco, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus plmWorkflowActivity = workflowStatusRepository.findOne(source.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflowActivity.getName()),
                highlightValue(plmWorkflow.getName()),
                eco.getVarianceType().toString().toLowerCase(),
                eco.getVarianceNumber());
    }

    private String getECOWorkflowAddedString(String messageString, PLMVariance variance, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getNewValue()),
                    variance.getVarianceType().toString().toLowerCase(),
                    variance.getVarianceNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getECOWorkflowChangeString(String messageString, PLMVariance variance, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getOldValue()),
                    highlightValue(workflow.getNewValue()),
                    variance.getVarianceType().toString().toLowerCase(),
                    variance.getVarianceNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getECOCommentString(String messageString, PLMVariance eco, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                eco.getVarianceNumber());

        String s = highlightValue("Comment: ");
        s += as.getData();
        s = addMarginToMessage(s);

        message += s;

        return message;
    }

    private String getECOAffectedItemsAddedJson(List<PLMVarianceAffectedItem> affectedItems) {
        List<ASNewFileDTO> asNewAffectedItems = new ArrayList<>();
        for (PLMVarianceAffectedItem affectedItem : affectedItems) {
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(affectedItem.getItem());
            PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
            ASNewFileDTO asNewAffectedItem = new ASNewFileDTO(affectedItem.getItem(), item.getItemName(), item.getItemNumber());
            asNewAffectedItems.add(asNewAffectedItem);
        }
        String json = null;
        try {
            json = objectMapper.writeValueAsString(asNewAffectedItems);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getECOAffectedItemsDeletedJson(PLMVarianceAffectedItem affectedItem) {
        String json = null;
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(affectedItem.getItem());
        PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());

        ASNewFileDTO asNewAffectedItem = new ASNewFileDTO(affectedItem.getItem(), item.getItemName(), item.getItemNumber());

        List<ASNewFileDTO> asNewAffectedItems = new ArrayList<>();
        asNewAffectedItems.add(asNewAffectedItem);
        try {
            json = objectMapper.writeValueAsString(asNewAffectedItems);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getECOAffectedPartsAddedJson(List<PLMVarianceAffectedMaterial> affectedItems) {
        List<ASNewFileDTO> asNewAffectedItems = new ArrayList<>();
        for (PLMVarianceAffectedMaterial affectedItem : affectedItems) {
            PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(affectedItem.getMaterial());
            ASNewFileDTO asNewAffectedItem = new ASNewFileDTO(affectedItem.getMaterial(), manufacturerPart.getPartName(), manufacturerPart.getPartNumber());
            asNewAffectedItems.add(asNewAffectedItem);
        }
        String json = null;
        try {
            json = objectMapper.writeValueAsString(asNewAffectedItems);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getECOAffectedPartsDeletedJson(PLMVarianceAffectedMaterial affectedItem) {
        String json = null;
        PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(affectedItem.getMaterial());

        ASNewFileDTO asNewAffectedItem = new ASNewFileDTO(affectedItem.getMaterial(), manufacturerPart.getPartName(), manufacturerPart.getPartNumber());

        List<ASNewFileDTO> asNewAffectedItems = new ArrayList<>();
        asNewAffectedItems.add(asNewAffectedItem);
        try {
            json = objectMapper.writeValueAsString(asNewAffectedItems);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getECOAffectedItemsAddedString(String messageString, PLMVariance eco, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), eco.getVarianceType().toString().toLowerCase(), eco.getVarianceNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.change.variance.affectedItems.add.item");

        String json = as.getData();
        try {
            List<ASNewFileDTO> affectedItems = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            affectedItems.forEach(affectedItem -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(affectedItem.getName()),
                        highlightValue(affectedItem.getSize())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getVarianceAffectedItemUpdatedString(String messageString, Person actor, PLMVariance eco, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), eco.getVarianceType().toString().toLowerCase(), eco.getVarianceNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);
        String updateString = activityStreamResourceBundle.getString("plm.change.variance.update.affectedItems.affectedItem");

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

    private String getVarianceAffectedPartUpdatedString(String messageString, Person actor, PLMVariance eco, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), eco.getVarianceType().toString().toLowerCase(), eco.getVarianceNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);
        String updateString = activityStreamResourceBundle.getString("plm.change.variance.update.affectedParts.affectedPart");

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

    private String getECOAffectedItemsDeletedString(String messageString, PLMVariance eco, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> asNewAffectedItems = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO asNewAffectedItem = asNewAffectedItems.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), eco.getVarianceType().toString().toLowerCase(), highlightValue(asNewAffectedItem.getId().toString()), highlightValue(asNewAffectedItem.getName()), highlightValue(asNewAffectedItem.getSize()), eco.getVarianceNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getECOAffectedPartsAddedString(String messageString, PLMVariance eco, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), eco.getVarianceType().toString().toLowerCase(), eco.getVarianceNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.change.variance.affectedParts.add.item");

        String json = as.getData();
        try {
            List<ASNewFileDTO> affectedItems = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            affectedItems.forEach(affectedItem -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(affectedItem.getName()),
                        highlightValue(affectedItem.getSize())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getECOAffectedPartsDeletedString(String messageString, PLMVariance eco, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> asNewAffectedItems = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO asNewAffectedItem = asNewAffectedItems.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(asNewAffectedItem.getId().toString()), highlightValue(asNewAffectedItem.getName()), highlightValue(asNewAffectedItem.getSize()), eco.getVarianceType().toString().toLowerCase(), eco.getVarianceNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    @Async
    @EventListener
    public void varianceRelatedItemsAdded(VarianceEvents.VarianceRelatedItemsAddedEvent event) {
        List<PLMChangeRelatedItem> relatedItems = event.getRelatedItems();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.variance.relatedItems.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getVariance().getId());
        object.setType("variance");
        as.setObject(object);
        as.setData(getDcoRelatedItemsAddedJson(relatedItems));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void varianceRelatedItemsDeleted(VarianceEvents.VarianceChangeRelatedDeletedEvent event) {
        PLMChangeRelatedItem relatedItem = event.getRelatedItem();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.variance.relatedItems.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getVariance().getId());
        object.setType("variance");
        as.setObject(object);
        as.setData(getDcoRelatedItemsDeletedJson(relatedItem));

        activityStreamService.create(as);
    }

    private String getDcoRelatedItemsAddedJson(List<PLMChangeRelatedItem> relatedItems) {
        List<ASNewDcrItem> asNewDcrItems = new ArrayList<>();
        for (PLMChangeRelatedItem relatedItem : relatedItems) {
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(relatedItem.getItem());
            PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
            ASNewDcrItem asNewDcrItem = new ASNewDcrItem(itemRevision.getId(), item.getItemName(), item.getItemNumber(), itemRevision.getRevision(), itemRevision.getLifeCyclePhase().getPhase());
            asNewDcrItems.add(asNewDcrItem);
        }
        String json = null;
        try {
            json = objectMapper.writeValueAsString(asNewDcrItems);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getDcoRelatedItemsDeletedJson(PLMChangeRelatedItem relatedItem) {
        String json = null;
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(relatedItem.getItem());
        PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
        ASNewDcrItem asNewDcrItem = new ASNewDcrItem(itemRevision.getId(), item.getItemName(), item.getItemNumber(), itemRevision.getRevision(), itemRevision.getLifeCyclePhase().getPhase());

        List<ASNewDcrItem> asNewDcrItems = new ArrayList<>();
        asNewDcrItems.add(asNewDcrItem);
        try {
            json = objectMapper.writeValueAsString(asNewDcrItems);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getDcoRelatedItemsAddedString(String messageString, PLMVariance dco, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), dco.getVarianceType().toString().toLowerCase(), dco.getVarianceNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.change.variance.relatedItems.add.item");

        String json = as.getData();
        try {
            List<ASNewDcrItem> files = objectMapper.readValue(json, new TypeReference<List<ASNewDcrItem>>() {
            });
            files.forEach(f -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(f.getItemNumber()), highlightValue(f.getItemName()), highlightValue(f.getRevision()), highlightValue(f.getLifecyclePhase())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getDcoRelatedItemsDeletedString(String messageString, PLMVariance dco, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewDcrItem> asNewDcrItems = objectMapper.readValue(json, new TypeReference<List<ASNewDcrItem>>() {
            });
            ASNewDcrItem asNewDcrItem = asNewDcrItems.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(asNewDcrItem.getItemNumber()), highlightValue(asNewDcrItem.getItemName()), highlightValue(asNewDcrItem.getRevision()), highlightValue(asNewDcrItem.getLifecyclePhase()), dco.getVarianceType().toString().toLowerCase(), dco.getVarianceNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getVarianceBasicInfoUpdatedJson(PLMVariance olddco, PLMVariance dco) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String oldValue = olddco.getTitle();
        String newValue = dco.getTitle();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Title", oldValue, newValue));
        }

        oldValue = olddco.getDescription();
        newValue = dco.getDescription();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue, newValue));
        }

        oldValue = olddco.getReasonForVariance();
        newValue = dco.getReasonForVariance();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("ReasonChange", oldValue, newValue));
        }

        oldValue = olddco.getCurrentRequirement();
        newValue = dco.getCurrentRequirement();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Current Requirement", oldValue, newValue));
        }

        oldValue = olddco.getRequirementDeviation();
        newValue = dco.getRequirementDeviation();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Requirement Deviation", oldValue, newValue));
        }

        Date oldDate1 = olddco.getEffectiveFrom();
        Date newDate1 = dco.getEffectiveFrom();
        String newDate;
        String oldDate;
        if (oldDate1 == null) {
            oldDate = "";
        } else {
            oldDate = dateFormat.format(olddco.getEffectiveFrom());
        }
        if (newDate1 == null) {
            newDate = "";
        } else {
            newDate = dateFormat.format(dco.getEffectiveFrom());
        }
        if (!newDate.equals(oldDate)) {
            changes.add(new ASPropertyChangeDTO("Effective From", oldDate, newDate));
        }

        Date oldDate2 = olddco.getEffectiveTo();
        Date newDate2 = dco.getEffectiveTo();
        String newDate3;
        String oldDate3;
        if (oldDate2 == null) {
            oldDate3 = "";
        } else {
            oldDate3 = dateFormat.format(olddco.getEffectiveTo());
        }
        if (newDate2 == null) {
            newDate3 = "";
        } else {
            newDate3 = dateFormat.format(dco.getEffectiveTo());
        }
        if (!newDate.equals(oldDate)) {
            changes.add(new ASPropertyChangeDTO("Effective To", oldDate3, newDate3));
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

    private String getVarianceAffectedUpdatedJson(PLMVarianceAffectedObject olddco, PLMVarianceAffectedObject dco) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = olddco.getQuantity().toString();
        String newValue = dco.getQuantity().toString();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Quantity", oldValue, newValue));
        }

        oldValue = olddco.getSerialsOrLots();
        newValue = dco.getSerialsOrLots();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Serials or Lots", oldValue, newValue));
        }

        oldValue = olddco.getNotes();
        newValue = dco.getNotes();
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
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }
}
