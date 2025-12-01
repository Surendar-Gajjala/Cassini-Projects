package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.DCOEvents;
import com.cassinisys.plm.model.cm.*;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.repo.cm.ChangeTypeAttributeRepository;
import com.cassinisys.plm.repo.cm.DCORepository;
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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class DCOActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PLMWorkflowRepository workFlowRepository;
    @Autowired
    private PLMWorkflowStatusRepository workflowStatusRepository;
    @Autowired
    private DCORepository dcoRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository workflowDefinitionRepository;
    @Autowired
    private ChangeTypeAttributeRepository changeTypeAttributeRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CommonActivityStream commonActivityStream;


    @Async
    @EventListener
    public void dcoCreated(DCOEvents.DCOCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDco().getId());
        object.setType("dco");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dco.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }


    @Async
    @EventListener
    public void dcoBasicInfoUpdated(DCOEvents.DCOBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PLMDCO olddco = event.getOlddco();
        PLMDCO newdco = event.getDco();

        object.setObject(newdco.getId());
        object.setType("dco");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dco.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getDCOBasicInfoUpdatedJson(olddco, newdco));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void dcoAttributesUpdated(DCOEvents.DCOAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PLMDCO dco = event.getDco();

        PLMChangeAttribute oldAtt = event.getOldAttribute();
        PLMChangeAttribute newAtt = event.getNewAttribute();

        object.setObject(dco.getId());
        object.setType("dco");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dco.update.attributes");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getDCOAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void dcoFilesAdded(DCOEvents.DCOFilesAddedEvent event) {
        List<PLMChangeFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dco.files.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDco().getId());
        object.setType("dco");
        as.setObject(object);
        as.setData(getDCOFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void dcoFoldersAdded(DCOEvents.DCOFoldersAddedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dco.files.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDco().getId());
        object.setType("dco");
        as.setObject(object);
        as.setData(getDCOFoldersAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void dcoFoldersDeleted(DCOEvents.DCOFoldersDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dco.files.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDco().getId());
        object.setType("dco");
        as.setObject(object);
        as.setData(getDCOFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void dcoFileDeleted(DCOEvents.DCOFileDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dco.files.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDco().getId());
        object.setType("dco");
        as.setObject(object);
        as.setData(getDCOFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void dcoFilesVersioned(DCOEvents.DCOFilesVersionedEvent event) {
        List<PLMChangeFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dco.files.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDco().getId());
        object.setType("dco");
        as.setObject(object);
        as.setData(getDCOFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void dcoFileRenamed(DCOEvents.DCOFileRenamedEvent event) {
        PLMFile oldFile = event.getOldFile();
        PLMFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals("Rename")) as.setActivity("plm.change.dco.files.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.change.dco.files.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDco().getId());
        object.setType("dco");
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
    public void dcoFileLocked(DCOEvents.DCOFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dco.files.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDco().getId());
        object.setType("dco");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getChangeFile().getId(), event.getChangeFile().getName(), FileUtils.byteCountToDisplaySize(event.getChangeFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void dcoFileUnlocked(DCOEvents.DCOFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dco.files.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDco().getId());
        object.setType("dco");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getChangeFile().getId(), event.getChangeFile().getName(), FileUtils.byteCountToDisplaySize(event.getChangeFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void dcoFileDownloaded(DCOEvents.DCOFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dco.files.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDco().getId());
        object.setType("dco");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getChangeFile().getId(), event.getChangeFile().getName(), FileUtils.byteCountToDisplaySize(event.getChangeFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }


    @Async
    @EventListener
    public void dcoWorkflowStarted(DCOEvents.DCOWorkflowStartedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dco.workflow.start");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDco().getId());
        object.setType("dco");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void dcoWorkflowFinished(DCOEvents.DCOWorkflowFinishedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dco.workflow.finish");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDco().getId());
        object.setType("dco");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void dcoWorkflowPromoted(DCOEvents.DCOWorkflowPromotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dco.workflow.promote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDco().getId());
        object.setType("dco");
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
    public void dcoWorkflowDemoted(DCOEvents.DCOWorkflowDemotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dco.workflow.demote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDco().getId());
        object.setType("dco");
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
    public void dcoWorkflowHold(DCOEvents.DCOWorkflowHoldEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dco.workflow.hold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDco().getId());
        object.setType("dco");
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
    public void dcoWorkflowUnhold(DCOEvents.DCOWorkflowUnholdEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dco.workflow.unhold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDco().getId());
        object.setType("dco");
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
    public void dcoWorkflowChange(DCOEvents.DCOWorkflowChangeEvent event) {
        ActivityStream as = new ActivityStream();
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDco().getId());
        object.setType("dco");
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
            as.setActivity("plm.change.dco.workflow.change");
        } else {
            as.setActivity("plm.change.dco.workflow.add");
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
    public void dcoCommentAdded(DCOEvents.DCOCommentAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dco.comment");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDco().getId());
        object.setType("dco");
        as.setObject(object);
        as.setData(event.getComment().getComment());

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void dcoAffectedItemsAdded(DCOEvents.DCOAffectedItemAddedEvent event) {
        PLMDCOAffectedItem asNewDcrItem = event.getItem();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dco.affectedItems.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDco().getId());
        object.setType("dco");
        as.setObject(object);
        as.setData(getDcoAffectedItemsAddedJson(asNewDcrItem));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void dcoAffectedItemsDeleted(DCOEvents.DCOAffectedItemDeletedEvent event) {
        PLMDCOAffectedItem asNewDcrItem = event.getItem();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dco.affectedItems.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDco().getId());
        object.setType("dco");
        as.setObject(object);
        as.setData(getDcoAffectedItemsDeletedJson(asNewDcrItem));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void dcoRelatedItemsAdded(DCOEvents.DCORelatedItemAddedEvent event) {
        List<PLMChangeRelatedItem> relatedItems = event.getRelatedItems();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dco.relatedItems.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDco().getId());
        object.setType("dco");
        as.setObject(object);
        as.setData(getDcoRelatedItemsAddedJson(relatedItems));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void dcoRelatedItemsDeleted(DCOEvents.DCORelatedItemDeletedEvent event) {
        PLMChangeRelatedItem relatedItem = event.getRelatedItem();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dco.relatedItems.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDco().getId());
        object.setType("dco");
        as.setObject(object);
        as.setData(getDcoRelatedItemsDeletedJson(relatedItem));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void dcoChangeRequestsAdded(DCOEvents.DCOChangeRequestAddedEvent event) {
        List<PLMDCR> dcrList = event.getDcrList();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dco.changeRequests.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDco().getId());
        object.setType("dco");
        as.setObject(object);
        as.setData(getDCOChangeRequestsAddedJson(dcrList));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void dcoChangeRequestsDeleted(DCOEvents.DCOChangeRequestDeletedEvent event) {
        PLMDCR dcr = event.getDcr();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dco.changeRequests.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDco().getId());
        object.setType("dco");
        as.setObject(object);
        as.setData(getDCOChangeRequestsDeletedJson(dcr));

        activityStreamService.create(as);
    }

    @Override
    public String getConverterKey() {
        return "plm.change.dco";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            PLMDCO dco = dcoRepository.findOne(object.getObject());

            if (dco == null) return "";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.change.dco.create":
                    convertedString = getDCOCreatedString(messageString, actor, dco);
                    break;
                case "plm.change.dco.update.basicinfo":
                    convertedString = getDCOBasicInfoUpdatedString(messageString, actor, dco, as);
                    break;
                case "plm.change.dco.update.attributes":
                    convertedString = getDCOAttributeUpdatedString(messageString, actor, dco, as);
                    break;
                case "plm.change.dco.files.add":
                    convertedString = getDCOFilesAddedString(messageString, dco, as);
                    break;
                case "plm.change.dco.files.delete":
                    convertedString = getDCOFileDeletedString(messageString, dco, as);
                    break;
                case "plm.change.dco.files.folders.add":
                    convertedString = getDCOFoldersAddedString(messageString, dco, as);
                    break;
                case "plm.change.dco.files.folders.delete":
                    convertedString = getDCOFoldersDeletedString(messageString, dco, as);
                    break;
                case "plm.change.dco.files.version":
                    convertedString = getDCOFilesVersionedString(messageString, dco, as);
                    break;
                case "plm.change.dco.files.rename":
                    convertedString = getDCOFileRenamedString(messageString, dco, as);
                    break;
                case "plm.change.dco.files.replace":
                    convertedString = getDCOFileRenamedString(messageString, dco, as);
                    break;
                case "plm.change.dco.files.lock":
                    convertedString = getDCOFileString(messageString, dco, as);
                    break;
                case "plm.change.dco.files.unlock":
                    convertedString = getDCOFileString(messageString, dco, as);
                    break;
                case "plm.change.dco.files.download":
                    convertedString = getDCOFileString(messageString, dco, as);
                    break;
                case "plm.change.dco.workflow.start":
                    convertedString = getDCOWorkflowStartString(messageString, dco, as);
                    break;
                case "plm.change.dco.workflow.finish":
                    convertedString = getDCOWorkflowFinishString(messageString, dco, as);
                    break;
                case "plm.change.dco.workflow.promote":
                    convertedString = getDCOWorkflowPromoteDemoteString(messageString, dco, as);
                    break;
                case "plm.change.dco.workflow.demote":
                    convertedString = getDCOWorkflowPromoteDemoteString(messageString, dco, as);
                    break;
                case "plm.change.dco.workflow.hold":
                    convertedString = getDCOWorkflowHoldUnholdString(messageString, dco, as);
                    break;
                case "plm.change.dco.workflow.unhold":
                    convertedString = getDCOWorkflowHoldUnholdString(messageString, dco, as);
                    break;
                case "plm.change.dco.workflow.add":
                    convertedString = getDCOWorkflowAddedString(messageString, dco, as);
                    break;
                case "plm.change.dco.workflow.change":
                    convertedString = getDCOWorkflowChangeString(messageString, dco, as);
                    break;
                case "plm.change.dco.comment":
                    convertedString = getDCOCommentString(messageString, dco, as);
                    break;
                case "plm.change.dco.affectedItems.add":
                    convertedString = getDcoAffectedItemsAddedString(messageString, dco, as);
                    break;
                case "plm.change.dco.affectedItems.delete":
                    convertedString = getDcoAffectedItemsDeletedString(messageString, dco, as);
                    break;
                case "plm.change.dco.relatedItems.add":
                    convertedString = getDcoRelatedItemsAddedString(messageString, dco, as);
                    break;
                case "plm.change.dco.relatedItems.delete":
                    convertedString = getDcoRelatedItemsDeletedString(messageString, dco, as);
                    break;
                case "plm.change.dco.changeRequests.add":
                    convertedString = getDCOChangeRequestsAddedString(messageString, dco, as);
                    break;
                case "plm.change.dco.changeRequests.delete":
                    convertedString = getDCOChangeRequestsDeletedString(messageString, dco, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getDCOCreatedString(String messageString, Person actor, PLMDCO dco) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), dco.getDcoNumber());
    }

    private String getDCOBasicInfoUpdatedString(String messageString, Person actor, PLMDCO dco, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), dco.getDcoNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.change.dco.update.basicinfo.property");

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

    private String getDCOAttributesUpdatedJson(PLMChangeAttribute oldAttribute, PLMChangeAttribute newAttribute) {
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

    private String getDCOAttributeUpdatedString(String messageString, Person actor, PLMDCO dco, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), dco.getDcoNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.change.dco.update.attributes.attribute");
        String propertyUpdateString = activityStreamResourceBundle.getString("plm.change.dco.update.attributes.attribute").substring(0, 21);

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

    private String getDCOFilesAddedJson(List<PLMChangeFile> files) {
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

    private String getDCOFoldersAddedJson(PLMFile file) {
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

    private String getDCOFoldersDeletedJson(PLMFile file) {
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

    private String getDCOFilesAddedString(String messageString, PLMDCO dco, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), dco.getDcoNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.change.dco.files.add.file");

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

    private String getDCOFoldersAddedString(String messageString, PLMDCO dco, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), dco.getDcoNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getDCOFoldersDeletedString(String messageString, PLMDCO dco, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), dco.getDcoNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getDCOFileDeletedString(String messageString, PLMDCO dco, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), dco.getDcoNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getDCOFilesVersionedJson(List<PLMChangeFile> files) {
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

    private String getDCOFilesVersionedString(String messageString, PLMDCO dco, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), dco.getDcoNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.change.dco.files.version.file");

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

    private String getDCOFileRenamedString(String messageString, PLMDCO dco, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    dco.getDcoNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getDCOFileString(String messageString, PLMDCO dco, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    dco.getDcoNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getDCOWorkflowStartString(String messageString, PLMDCO dco, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                dco.getDcoNumber());
    }

    private String getDCOWorkflowFinishString(String messageString, PLMDCO dco, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                dco.getDcoNumber());
    }

    private String getDCOWorkflowPromoteDemoteString(String messageString, PLMDCO dco, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();
        ActivityStreamObject target = as.getTarget();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus fromActivity = workflowStatusRepository.findOne(source.getObject());
        PLMWorkflowStatus toActivity = workflowStatusRepository.findOne(target.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                dco.getDcoNumber(),
                highlightValue(fromActivity.getName()),
                highlightValue(toActivity.getName()));
    }

    private String getDCOWorkflowHoldUnholdString(String messageString, PLMDCO dco, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus plmWorkflowActivity = workflowStatusRepository.findOne(source.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflowActivity.getName()),
                highlightValue(plmWorkflow.getName()),
                dco.getDcoNumber());
    }

    private String getDCOWorkflowAddedString(String messageString, PLMDCO dco, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getNewValue()),
                    dco.getDcoNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getDCOWorkflowChangeString(String messageString, PLMDCO dco, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getOldValue()),
                    highlightValue(workflow.getNewValue()),
                    dco.getDcoNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }


    private String getDCOCommentString(String messageString, PLMDCO dco, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                dco.getDcoNumber());

        String s = highlightValue("Comment: ");
        s += as.getData();
        s = addMarginToMessage(s);

        message += s;

        return message;
    }

    private String getDCOBasicInfoUpdatedJson(PLMDCO olddco, PLMDCO dco) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

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
            changes.add(new ASPropertyChangeDTO("Description of Change", oldValue, newValue));
        }

        oldValue = olddco.getReasonForChange();
        newValue = dco.getReasonForChange();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Reason for Change", oldValue, newValue));
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

    private String getDcoAffectedItemsAddedJson(PLMDCOAffectedItem affectedItem) {
        List<ASNewDcrItem> asNewDcrItems = new ArrayList<>();
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(affectedItem.getItem());
        PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
        ASNewDcrItem asNewDcrItem = new ASNewDcrItem(itemRevision.getId(), item.getItemName(), item.getItemNumber(), itemRevision.getRevision(), itemRevision.getLifeCyclePhase().getPhase());
        asNewDcrItems.add(asNewDcrItem);
        String json = null;
        try {
            json = objectMapper.writeValueAsString(asNewDcrItems);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getDcoAffectedItemsDeletedJson(PLMDCOAffectedItem affectedItem) {
        String json = null;
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(affectedItem.getItem());
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

    private String getDcoAffectedItemsAddedString(String messageString, PLMDCO dco, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), dco.getDcoNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.change.dco.affectedItems.add.item");

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

    private String getDcoAffectedItemsDeletedString(String messageString, PLMDCO dco, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewDcrItem> asNewDcrItems = objectMapper.readValue(json, new TypeReference<List<ASNewDcrItem>>() {
            });
            ASNewDcrItem asNewDcrItem = asNewDcrItems.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(asNewDcrItem.getItemNumber()), highlightValue(asNewDcrItem.getItemName()), highlightValue(asNewDcrItem.getRevision()), highlightValue(asNewDcrItem.getLifecyclePhase()), dco.getDcoNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
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

    private String getDcoRelatedItemsAddedString(String messageString, PLMDCO dco, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), dco.getDcoNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.change.dco.relatedItems.add.item");

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

    private String getDcoRelatedItemsDeletedString(String messageString, PLMDCO dco, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewDcrItem> asNewDcrItems = objectMapper.readValue(json, new TypeReference<List<ASNewDcrItem>>() {
            });
            ASNewDcrItem asNewDcrItem = asNewDcrItems.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(asNewDcrItem.getItemNumber()), highlightValue(asNewDcrItem.getItemName()), highlightValue(asNewDcrItem.getRevision()), highlightValue(asNewDcrItem.getLifecyclePhase()), dco.getDcoNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getDCOChangeRequestsAddedJson(List<PLMDCR> plmdcrList) {
        List<ASNewChangeRequest> asNewChangeRequests = new ArrayList<>();
        for (PLMDCR plmdcr : plmdcrList) {
            ASNewChangeRequest newChangeRequest = new ASNewChangeRequest(plmdcr.getId(), plmdcr.getCrNumber(), plmdcr.getTitle());
            asNewChangeRequests.add(newChangeRequest);
        }
        String json = null;
        try {
            json = objectMapper.writeValueAsString(asNewChangeRequests);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getDCOChangeRequestsDeletedJson(PLMDCR plmdcr) {
        String json = null;

        ASNewChangeRequest asNewChangeRequest = new ASNewChangeRequest(plmdcr.getId(), plmdcr.getCrNumber(), plmdcr.getTitle());

        List<ASNewChangeRequest> asNewChangeRequests = new ArrayList<>();
        asNewChangeRequests.add(asNewChangeRequest);
        try {
            json = objectMapper.writeValueAsString(asNewChangeRequests);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getDCOChangeRequestsAddedString(String messageString, PLMDCO dco, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), dco.getDcoNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.change.dco.changeRequests.add.dcr");

        String json = as.getData();
        try {
            List<ASNewChangeRequest> asNewChangeRequests = objectMapper.readValue(json, new TypeReference<List<ASNewChangeRequest>>() {
            });
            asNewChangeRequests.forEach(newChangeRequest -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(newChangeRequest.getCrNumber()), highlightValue(newChangeRequest.getTitle())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getDCOChangeRequestsDeletedString(String messageString, PLMDCO dco, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewChangeRequest> asNewChangeRequests = objectMapper.readValue(json, new TypeReference<List<ASNewChangeRequest>>() {
            });
            ASNewChangeRequest newChangeRequest = asNewChangeRequests.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(newChangeRequest.getCrNumber()), highlightValue(newChangeRequest.getTitle()), dco.getDcoNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }
}
