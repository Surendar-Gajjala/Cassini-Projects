package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.ManufacturerEvents;
import com.cassinisys.plm.model.mfr.*;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.repo.mfr.ManufacturerRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerTypeAttributeRepository;
import com.cassinisys.plm.repo.plm.LifeCyclePhaseRepository;
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
public class ManufacturerActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PLMWorkflowRepository workFlowRepository;
    @Autowired
    private PLMWorkflowStatusRepository workflowStatusRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository workflowDefinitionRepository;
    @Autowired
    private ManufacturerTypeAttributeRepository manufacturerTypeAttributeRepository;
    @Autowired
    private LifeCyclePhaseRepository lifeCyclePhaseRepository;
    @Autowired
    private CommonActivityStream commonActivityStream;

    @Async
    @EventListener
    public void manufacturerCreated(ManufacturerEvents.ManufacturerCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getManufacturer().getId());
        object.setType("manufacturer");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturer.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void manufacturerBasicInfoUpdated(ManufacturerEvents.ManufacturerBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PLMManufacturer oldmfr = event.getOldManufacturer();
        PLMManufacturer newmfr = event.getManufacturer();

        object.setObject(newmfr.getId());
        object.setType("manufacturer");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturer.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getManufacturerBasicInfoUpdatedJson(oldmfr, newmfr));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void manufacturerAttributesUpdated(ManufacturerEvents.ManufacturerAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PLMManufacturer manufacturer = event.getManufacturer();

        PLMManufacturerAttribute oldAtt = event.getOldAttribute();
        PLMManufacturerAttribute newAtt = event.getNewAttribute();

        object.setObject(manufacturer.getId());
        object.setType("manufacturer");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturer.update.attributes");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getManufacturerAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void manufacturerFilesAdded(ManufacturerEvents.ManufacturerFilesAddedEvent event) {
        List<PLMManufacturerFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturer.files.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getManufacturer().getId());
        object.setType("manufacturer");
        as.setObject(object);
        as.setData(getManufacturerFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void manufacturerFoldersAdded(ManufacturerEvents.ManufacturerFoldersAddedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturer.files.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getManufacturer().getId());
        object.setType("manufacturer");
        as.setObject(object);
        as.setData(getManufacturerFoldersAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void manufacturerFoldersDeleted(ManufacturerEvents.ManufacturerFoldersDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturer.files.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getManufacturer().getId());
        object.setType("manufacturer");
        as.setObject(object);
        as.setData(getManufacturerFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void manufacturerFileDeleted(ManufacturerEvents.ManufacturerFileDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturer.files.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getManufacturer().getId());
        object.setType("manufacturer");
        as.setObject(object);
        as.setData(getManufacturerFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void manufacturerFilesVersioned(ManufacturerEvents.ManufacturerFilesVersionedEvent event) {
        List<PLMManufacturerFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturer.files.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getManufacturer().getId());
        object.setType("manufacturer");
        as.setObject(object);
        as.setData(getManufacturerFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void manufacturerFileRenamed(ManufacturerEvents.ManufacturerFileRenamedEvent event) {
        PLMFile oldFile = event.getOldFile();
        PLMFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals("Rename")) as.setActivity("plm.oem.manufacturer.files.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.oem.manufacturer.files.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getManufacturer().getId());
        object.setType("manufacturer");
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
    public void manufacturerFileLocked(ManufacturerEvents.ManufacturerFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturer.files.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getManufacturer().getId());
        object.setType("manufacturer");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getManufacturerFile().getId(), event.getManufacturerFile().getName(), FileUtils.byteCountToDisplaySize(event.getManufacturerFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void manufacturerFileUnlocked(ManufacturerEvents.ManufacturerFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturer.files.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getManufacturer().getId());
        object.setType("manufacturer");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getManufacturerFile().getId(), event.getManufacturerFile().getName(), FileUtils.byteCountToDisplaySize(event.getManufacturerFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void manufacturerFileDownloaded(ManufacturerEvents.ManufacturerFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturer.files.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getManufacturer().getId());
        object.setType("manufacturer");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getManufacturerFile().getId(), event.getManufacturerFile().getName(), FileUtils.byteCountToDisplaySize(event.getManufacturerFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }


    @Async
    @EventListener
    public void manufacturerWorkflowStarted(ManufacturerEvents.ManufacturerWorkflowStartedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturer.workflow.start");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getManufacturer().getId());
        object.setType("manufacturer");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void manufacturerWorkflowFinished(ManufacturerEvents.ManufacturerWorkflowFinishedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturer.workflow.finish");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getManufacturer().getId());
        object.setType("manufacturer");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void manufacturerWorkflowPromoted(ManufacturerEvents.ManufacturerWorkflowPromotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturer.workflow.promote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getManufacturer().getId());
        object.setType("manufacturer");
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
    public void manufacturerWorkflowDemoted(ManufacturerEvents.ManufacturerWorkflowDemotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturer.workflow.demote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getManufacturer().getId());
        object.setType("manufacturer");
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
    public void manufacturerWorkflowHold(ManufacturerEvents.ManufacturerWorkflowHoldEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturer.workflow.hold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getManufacturer().getId());
        object.setType("manufacturer");
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
    public void manufacturerWorkflowUnhold(ManufacturerEvents.ManufacturerWorkflowUnholdEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturer.workflow.unhold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getManufacturer().getId());
        object.setType("manufacturer");
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
    public void manufacturerWorkflowChange(ManufacturerEvents.ManufacturerWorkflowChangeEvent event) {
        ActivityStream as = new ActivityStream();
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getManufacturer().getId());
        object.setType("manufacturer");
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
            as.setActivity("plm.oem.manufacturer.workflow.change");
        } else {
            as.setActivity("plm.oem.manufacturer.workflow.add");
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
    public void manufacturerCommentAdded(ManufacturerEvents.ManufacturerCommentAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturer.comment");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getManufacturer().getId());
        object.setType("manufacturer");
        as.setObject(object);
        as.setData(event.getComment().getComment());

        activityStreamService.create(as);
    }


    @Async
    @EventListener
    public void manufacturerPartAddedEvent(ManufacturerEvents.MfrPartCreatedEvent event) {
        PLMManufacturerPart asNewMfrPartDTO = event.getPart();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturer.part.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getManufacturer().getId());
        object.setType("manufacturer");
        as.setObject(object);
        as.setData(getMfrPartAddedJson(asNewMfrPartDTO));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void manufacturerPromoted(ManufacturerEvents.ManufacturerPromotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturer.lifeCyclePhase.promote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getManufacturer().getId());
        object.setType("manufacturer");
        as.setObject(object);

        ActivityStreamObject source = new ActivityStreamObject();
        source.setObject(event.getFromLifeCyclePhase().getId());
        source.setType("manufacturer");
        as.setSource(source);

        ActivityStreamObject target = new ActivityStreamObject();
        target.setObject(event.getToLifeCyclePhase().getId());
        target.setType("manufacturer");
        as.setTarget(target);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void manufacturerDemoted(ManufacturerEvents.ManufacturerDemotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturer.lifeCyclePhase.demote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getManufacturer().getId());
        object.setType("manufacturer");
        as.setObject(object);

        ActivityStreamObject source = new ActivityStreamObject();
        source.setObject(event.getFromLifeCyclePhase().getId());
        source.setType("manufacturer");
        as.setSource(source);

        ActivityStreamObject target = new ActivityStreamObject();
        target.setObject(event.getToLifeCyclePhase().getId());
        target.setType("manufacturer");
        as.setTarget(target);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void manufacturerPartDeleted(ManufacturerEvents.ManufacturerPartDeletedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturer.part.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getManufacturer().getId());
        object.setType("manufacturer");
        as.setObject(object);
        ASMfrPartDelete asMfrPartDelete = new ASMfrPartDelete(event.getManufacturer().getName(),
                event.getManufacturerPart().getPartNumber());
        try {
            as.setData(objectMapper.writeValueAsString(asMfrPartDelete));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Override
    public String getConverterKey() {
        return "plm.oem.manufacturer";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            PLMManufacturer manufacturer = manufacturerRepository.findOne(object.getObject());

            if (manufacturer == null) return "";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.oem.manufacturer.create":
                    convertedString = getManufacturerCreatedString(messageString, actor, manufacturer);
                    break;
                case "plm.oem.manufacturer.update.basicinfo":
                    convertedString = getManufacturerBasicInfoUpdatedString(messageString, actor, manufacturer, as);
                    break;
                case "plm.oem.manufacturer.update.attributes":
                    convertedString = getManufacturerAttributeUpdatedString(messageString, actor, manufacturer, as);
                    break;
                case "plm.oem.manufacturer.files.add":
                    convertedString = getManufacturerFilesAddedString(messageString, manufacturer, as);
                    break;
                case "plm.oem.manufacturer.files.delete":
                    convertedString = getManufacturerFileDeletedString(messageString, manufacturer, as);
                    break;
                case "plm.oem.manufacturer.files.folders.add":
                    convertedString = getManufacturerFoldersAddedString(messageString, manufacturer, as);
                    break;
                case "plm.oem.manufacturer.files.folders.delete":
                    convertedString = getManufacturerFoldersDeletedString(messageString, manufacturer, as);
                    break;
                case "plm.oem.manufacturer.files.version":
                    convertedString = getManufacturerFilesVersionedString(messageString, manufacturer, as);
                    break;
                case "plm.oem.manufacturer.files.rename":
                    convertedString = getManufacturerFileRenamedString(messageString, manufacturer, as);
                    break;
                case "plm.oem.manufacturer.files.replace":
                    convertedString = getManufacturerFileRenamedString(messageString, manufacturer, as);
                    break;
                case "plm.oem.manufacturer.files.lock":
                    convertedString = getManufacturerFileString(messageString, manufacturer, as);
                    break;
                case "plm.oem.manufacturer.files.unlock":
                    convertedString = getManufacturerFileString(messageString, manufacturer, as);
                    break;
                case "plm.oem.manufacturer.files.download":
                    convertedString = getManufacturerFileString(messageString, manufacturer, as);
                    break;
                case "plm.oem.manufacturer.workflow.start":
                    convertedString = getManufacturerWorkflowStartString(messageString, manufacturer, as);
                    break;
                case "plm.oem.manufacturer.workflow.finish":
                    convertedString = getManufacturerWorkflowFinishString(messageString, manufacturer, as);
                    break;
                case "plm.oem.manufacturer.workflow.promote":
                    convertedString = getManufacturerWorkflowPromoteDemoteString(messageString, manufacturer, as);
                    break;
                case "plm.oem.manufacturer.workflow.demote":
                    convertedString = getManufacturerWorkflowPromoteDemoteString(messageString, manufacturer, as);
                    break;
                case "plm.oem.manufacturer.workflow.hold":
                    convertedString = getManufacturerWorkflowHoldUnholdString(messageString, manufacturer, as);
                    break;
                case "plm.oem.manufacturer.workflow.unhold":
                    convertedString = getManufacturerWorkflowHoldUnholdString(messageString, manufacturer, as);
                    break;
                case "plm.oem.manufacturer.workflow.add":
                    convertedString = getManufacturerWorkflowAddedString(messageString, manufacturer, as);
                    break;
                case "plm.oem.manufacturer.workflow.change":
                    convertedString = getManufacturerWorkflowChangeString(messageString, manufacturer, as);
                    break;
                case "plm.oem.manufacturer.comment":
                    convertedString = getManufacturerCommentString(messageString, manufacturer, as);
                    break;
                case "plm.oem.manufacturer.part.add":
                    convertedString = getMfrPartAddedString(messageString, manufacturer, as);
                    break;
                case "plm.oem.manufacturer.part.delete":
                    convertedString = getMfrPartDeletedString(messageString, manufacturer, as);
                    break;
                case "plm.oem.manufacturer.lifeCyclePhase.promote":
                    convertedString = getManufacturerPromoteDemoteString(messageString, manufacturer, as);
                    break;
                case "plm.oem.manufacturer.lifeCyclePhase.demote":
                    convertedString = getManufacturerPromoteDemoteString(messageString, manufacturer, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getManufacturerCreatedString(String messageString, Person actor, PLMManufacturer manufacturer) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), manufacturer.getName());
    }

    private String getManufacturerBasicInfoUpdatedString(String messageString, Person actor, PLMManufacturer manufacturer, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), manufacturer.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.oem.manufacturer.update.basicinfo.property");

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

    private String getManufacturerAttributesUpdatedJson(PLMManufacturerAttribute oldAttribute, PLMManufacturerAttribute newAttribute) {
        PLMManufacturerTypeAttribute attDef= manufacturerTypeAttributeRepository.findOne(newAttribute.getId().getAttributeDef());
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

    private String getManufacturerAttributeUpdatedString(String messageString, Person actor, PLMManufacturer manufacturer, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), manufacturer.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.oem.manufacturer.update.attributes.attribute");
        String propertyUpdateString = activityStreamResourceBundle.getString("plm.oem.manufacturer.update.attributes.attribute").substring(0, 21);

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

    private String getManufacturerFilesAddedJson(List<PLMManufacturerFile> files) {
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

    private String getManufacturerFoldersAddedJson(PLMFile file) {
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

    private String getManufacturerFoldersDeletedJson(PLMFile file) {
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

    private String getManufacturerFilesAddedString(String messageString, PLMManufacturer manufacturer, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), manufacturer.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.oem.manufacturer.files.add.file");

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

    private String getManufacturerFoldersAddedString(String messageString, PLMManufacturer manufacturer, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), manufacturer.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getManufacturerFoldersDeletedString(String messageString, PLMManufacturer manufacturer, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), manufacturer.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getManufacturerFileDeletedString(String messageString, PLMManufacturer manufacturer, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), manufacturer.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getManufacturerFilesVersionedJson(List<PLMManufacturerFile> files) {
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

    private String getManufacturerFilesVersionedString(String messageString, PLMManufacturer manufacturer, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), manufacturer.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.oem.manufacturer.files.version.file");

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

    private String getManufacturerFileRenamedString(String messageString, PLMManufacturer manufacturer, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    manufacturer.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getManufacturerFileString(String messageString, PLMManufacturer manufacturer, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    manufacturer.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getManufacturerWorkflowStartString(String messageString, PLMManufacturer manufacturer, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                manufacturer.getName());
    }

    private String getManufacturerWorkflowFinishString(String messageString, PLMManufacturer manufacturer, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                manufacturer.getName());
    }

    private String getManufacturerWorkflowPromoteDemoteString(String messageString, PLMManufacturer manufacturer, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();
        ActivityStreamObject target = as.getTarget();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus fromActivity = workflowStatusRepository.findOne(source.getObject());
        PLMWorkflowStatus toActivity = workflowStatusRepository.findOne(target.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                manufacturer.getName(),
                highlightValue(fromActivity.getName()),
                highlightValue(toActivity.getName()));
    }

    private String getManufacturerWorkflowHoldUnholdString(String messageString, PLMManufacturer manufacturer, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus plmWorkflowActivity = workflowStatusRepository.findOne(source.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflowActivity.getName()),
                highlightValue(plmWorkflow.getName()),
                manufacturer.getName());
    }

    private String getManufacturerWorkflowAddedString(String messageString, PLMManufacturer manufacturer, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getNewValue()),
                    manufacturer.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getManufacturerWorkflowChangeString(String messageString, PLMManufacturer manufacturer, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getOldValue()),
                    highlightValue(workflow.getNewValue()),
                    manufacturer.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getManufacturerCommentString(String messageString, PLMManufacturer manufacturer, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                manufacturer.getName());

        String s = highlightValue("Comment: ");
        s += as.getData();
        s = addMarginToMessage(s);

        message += s;

        return message;
    }

    private String getManufacturerBasicInfoUpdatedJson(PLMManufacturer oldmfr, PLMManufacturer mfr) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldmfr.getName();
        String newValue = mfr.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        oldValue = oldmfr.getDescription();
        newValue = mfr.getDescription();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue, newValue));
        }

        oldValue = oldmfr.getContactPerson();
        newValue = mfr.getContactPerson();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Contact Person", oldValue, newValue));
        }

        oldValue = oldmfr.getPhoneNumber();
        newValue = mfr.getPhoneNumber();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("PhoneNumber", oldValue, newValue));
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

    private String getMfrPartAddedJson(PLMManufacturerPart part) {
        List<ASNewMfrPartDTO> asNewMfrPartDTOs = new ArrayList<>();
        PLMManufacturer manufacturer = manufacturerRepository.findOne(part.getManufacturer());
        ASNewMfrPartDTO asNewMfrPartDTO = new ASNewMfrPartDTO(part.getId(), part.getPartNumber(), part.getPartName(), manufacturer.getName());
        asNewMfrPartDTOs.add(asNewMfrPartDTO);
        String json = null;
        try {
            json = objectMapper.writeValueAsString(asNewMfrPartDTOs);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getMfrPartAddedString(String messageString, PLMManufacturer mfr, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), mfr.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.oem.manufacturer.part.add.part");

        String json = as.getData();
        try {
            List<ASNewMfrPartDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewMfrPartDTO>>() {
            });
            files.forEach(f -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(f.getPartNumber()), highlightValue(f.getPartName())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getManufacturerPromoteDemoteString(String messageString, PLMManufacturer manufacturer, ActivityStream as) {
        ActivityStreamObject source = as.getSource();
        ActivityStreamObject target = as.getTarget();

        PLMLifeCyclePhase fromLifeCyclePhase = lifeCyclePhaseRepository.findOne(source.getObject());
        PLMLifeCyclePhase toLifeCyclePhase = lifeCyclePhaseRepository.findOne(target.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(manufacturer.getName()),
                highlightValue(fromLifeCyclePhase.getPhase()),
                highlightValue(toLifeCyclePhase.getPhase()));
    }

    private String getMfrPartDeletedString(String messageString, PLMManufacturer manufacturer, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            ASMfrPartDelete partDelete = objectMapper.readValue(json, new TypeReference<ASMfrPartDelete>() {
            });
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(partDelete.getPartNumber()), highlightValue(manufacturer.getName()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }
}
