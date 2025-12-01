package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.DCREvents;
import com.cassinisys.plm.model.cm.*;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.repo.cm.ChangeTypeAttributeRepository;
import com.cassinisys.plm.repo.cm.DCRRepository;
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
public class DCRActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PLMWorkflowRepository workFlowRepository;
    @Autowired
    private PLMWorkflowStatusRepository workflowStatusRepository;
    @Autowired
    private DCRRepository dcrRepository;
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
    public void dcrCreated(DCREvents.DCRCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDcr().getId());
        object.setType("dcr");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dcr.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void dcrBasicInfoUpdated(DCREvents.DCRBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PLMDCR olddcr = event.getOlddcr();
        PLMDCR newDcr = event.getDcr();

        object.setObject(newDcr.getId());
        object.setType("dcr");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dcr.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getDCRBasicInfoUpdatedJson(olddcr, newDcr));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void dcrAttributesUpdated(DCREvents.DCRAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PLMDCR dcr = event.getDcr();

        PLMChangeAttribute oldAtt = event.getOldAttribute();
        PLMChangeAttribute newAtt = event.getNewAttribute();

        object.setObject(dcr.getId());
        object.setType("dcr");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dcr.update.attributes");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getDCRAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void dcrFilesAdded(DCREvents.DCRFilesAddedEvent event) {
        List<PLMChangeFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dcr.files.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDcr().getId());
        object.setType("dcr");
        as.setObject(object);
        as.setData(getDCRFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void dcrFoldersAdded(DCREvents.DCRFoldersAddedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dcr.files.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDcr().getId());
        object.setType("dcr");
        as.setObject(object);
        as.setData(getDCRFoldersAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void dcrFoldersDeleted(DCREvents.DCRFoldersDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dcr.files.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDcr().getId());
        object.setType("dcr");
        as.setObject(object);
        as.setData(getDCRFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void dcrFileDeleted(DCREvents.DCRFileDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dcr.files.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDcr().getId());
        object.setType("dcr");
        as.setObject(object);
        as.setData(getDCRFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void dcrFilesVersioned(DCREvents.DCRFilesVersionedEvent event) {
        List<PLMChangeFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dcr.files.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDcr().getId());
        object.setType("dcr");
        as.setObject(object);
        as.setData(getDCRFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void dcrFileRenamed(DCREvents.DCRFileRenamedEvent event) {
        PLMFile oldFile = event.getOldFile();
        PLMFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals("Rename")) as.setActivity("plm.change.dcr.files.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.change.dcr.files.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDcr().getId());
        object.setType("dcr");
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
    public void dcrFileLocked(DCREvents.DCRFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dcr.files.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDcr().getId());
        object.setType("dcr");
        as.setObject(object);
        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getDcrFile().getId(), event.getDcrFile().getName(), FileUtils.byteCountToDisplaySize(event.getDcrFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void dcrFileUnlocked(DCREvents.DCRFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dcr.files.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDcr().getId());
        object.setType("dcr");
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
    public void dcrFileDownloaded(DCREvents.DCRFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dcr.files.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDcr().getId());
        object.setType("dcr");
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
    public void dcrWorkflowStarted(DCREvents.DCRWorkflowStartedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dcr.workflow.start");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDcr().getId());
        object.setType("dcr");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void dcrWorkflowFinished(DCREvents.DCRWorkflowFinishedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dcr.workflow.finish");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDcr().getId());
        object.setType("dcr");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void dcrWorkflowPromoted(DCREvents.DCRWorkflowPromotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dcr.workflow.promote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDcr().getId());
        object.setType("dcr");
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
    public void dcrWorkflowDemoted(DCREvents.DCRWorkflowDemotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dcr.workflow.demote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDcr().getId());
        object.setType("dcr");
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
    public void dcrWorkflowHold(DCREvents.DCRWorkflowHoldEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dcr.workflow.hold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDcr().getId());
        object.setType("dcr");
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
    public void dcrWorkflowUnhold(DCREvents.DCRWorkflowUnholdEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dcr.workflow.unhold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDcr().getId());
        object.setType("dcr");
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
    public void dcrWorkflowChange(DCREvents.DCRWorkflowChangeEvent event) {
        ActivityStream as = new ActivityStream();
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDcr().getId());
        object.setType("dcr");
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
            as.setActivity("plm.change.dcr.workflow.change");
        } else {
            as.setActivity("plm.change.dcr.workflow.add");
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
    public void dcrCommentAdded(DCREvents.DCRCommentAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dcr.comment");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDcr().getId());
        object.setType("dcr");
        as.setObject(object);
        as.setData(event.getComment().getComment());

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void dcrAffectedItemsAdded(DCREvents.DCRAffectedItemAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dcr.affectedItems.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDcr().getId());
        object.setType("dcr");
        as.setObject(object);
        as.setData(getDcrAffectedItemsAddedJson(event.getItems()));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void dcrAffectedItemsDeleted(DCREvents.DCRAffectedItemDeletedEvent event) {
        PLMDCRAffectedItem asNewDcrItem = event.getItem();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dcr.affectedItems.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDcr().getId());
        object.setType("dcr");
        as.setObject(object);
        as.setData(getDcrAffectedItemsDeletedJson(asNewDcrItem));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void dcrRelatedItemsAdded(DCREvents.DCRRelatedItemAddedEvent event) {
        List<PLMChangeRelatedItem> relatedItems = event.getRelatedItems();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dcr.relatedItems.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDcr().getId());
        object.setType("dcr");
        as.setObject(object);
        as.setData(getDcrRelatedItemsAddedJson(relatedItems));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void dcrRelatedItemsDeleted(DCREvents.DCRRelatedItemDeletedEvent event) {
        PLMChangeRelatedItem relatedItem = event.getRelatedItem();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.dcr.relatedItems.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDcr().getId());
        object.setType("dcr");
        as.setObject(object);
        as.setData(getDcrRelatedItemsDeletedJson(relatedItem));

        activityStreamService.create(as);
    }

    @Override
    public String getConverterKey() {
        return "plm.change.dcr";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            PLMDCR dcr = dcrRepository.findOne(object.getObject());

            if (dcr == null) return "";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.change.dcr.create":
                    convertedString = getDCRCreatedString(messageString, actor, dcr);
                    break;
                case "plm.change.dcr.update.basicinfo":
                    convertedString = getDCRBasicInfoUpdatedString(messageString, actor, dcr, as);
                    break;
                case "plm.change.dcr.update.attributes":
                    convertedString = getDCRAttributeUpdatedString(messageString, actor, dcr, as);
                    break;
                case "plm.change.dcr.files.add":
                    convertedString = getDCRFilesAddedString(messageString, dcr, as);
                    break;
                case "plm.change.dcr.files.delete":
                    convertedString = getDCRFileDeletedString(messageString, dcr, as);
                    break;
                case "plm.change.dcr.files.folders.add":
                    convertedString = getDCRFoldersAddedString(messageString, dcr, as);
                    break;
                case "plm.change.dcr.files.folders.delete":
                    convertedString = getDCRFoldersDeletedString(messageString, dcr, as);
                    break;
                case "plm.change.dcr.files.version":
                    convertedString = getDCRFilesVersionedString(messageString, dcr, as);
                    break;
                case "plm.change.dcr.files.rename":
                    convertedString = getDCRFileRenamedString(messageString, dcr, as);
                    break;
                case "plm.change.dcr.files.replace":
                    convertedString = getDCRFileRenamedString(messageString, dcr, as);
                    break;
                case "plm.change.dcr.files.lock":
                    convertedString = getDCRFileString(messageString, dcr, as);
                    break;
                case "plm.change.dcr.files.unlock":
                    convertedString = getDCRFileString(messageString, dcr, as);
                    break;
                case "plm.change.dcr.files.download":
                    convertedString = getDCRFileString(messageString, dcr, as);
                    break;
                case "plm.change.dcr.workflow.start":
                    convertedString = getDCRWorkflowStartString(messageString, dcr, as);
                    break;
                case "plm.change.dcr.workflow.finish":
                    convertedString = getDCRWorkflowFinishString(messageString, dcr, as);
                    break;
                case "plm.change.dcr.workflow.promote":
                    convertedString = getDCRWorkflowPromoteDemoteString(messageString, dcr, as);
                    break;
                case "plm.change.dcr.workflow.demote":
                    convertedString = getDCRWorkflowPromoteDemoteString(messageString, dcr, as);
                    break;
                case "plm.change.dcr.workflow.hold":
                    convertedString = getDCRWorkflowHoldUnholdString(messageString, dcr, as);
                    break;
                case "plm.change.dcr.workflow.unhold":
                    convertedString = getDCRWorkflowHoldUnholdString(messageString, dcr, as);
                    break;
                case "plm.change.dcr.workflow.add":
                    convertedString = getDCRWorkflowAddedString(messageString, dcr, as);
                    break;
                case "plm.change.dcr.workflow.change":
                    convertedString = getDCRWorkflowChangeString(messageString, dcr, as);
                    break;
                case "plm.change.dcr.comment":
                    convertedString = getDCRCommentString(messageString, dcr, as);
                    break;
                case "plm.change.dcr.affectedItems.add":
                    convertedString = getDcrAffectedItemsAddedString(messageString, dcr, as);
                    break;
                case "plm.change.dcr.affectedItems.delete":
                    convertedString = getDcrAffectedItemsDeletedString(messageString, dcr, as);
                    break;
                case "plm.change.dcr.relatedItems.add":
                    convertedString = getDcrRelatedItemsAddedString(messageString, dcr, as);
                    break;
                case "plm.change.dcr.relatedItems.delete":
                    convertedString = getDcrRelatedItemsDeletedString(messageString, dcr, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getDCRCreatedString(String messageString, Person actor, PLMDCR dcr) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), dcr.getCrNumber());
    }

    private String getDCRBasicInfoUpdatedString(String messageString, Person actor, PLMDCR dcr, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), dcr.getCrNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.change.dcr.update.basicinfo.property");

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

    private String getDCRAttributesUpdatedJson(PLMChangeAttribute oldAttribute, PLMChangeAttribute newAttribute) {
        PLMChangeTypeAttribute attDef = changeTypeAttributeRepository.findOne(newAttribute.getId().getAttributeDef());
        List<ASAttributeChangeDTO> changes = commonActivityStream.getAttributeUpdateJsonData(oldAttribute,newAttribute,attDef);

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

    private String getDCRAttributeUpdatedString(String messageString, Person actor, PLMDCR dcr, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), dcr.getCrNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.change.dcr.update.attributes.attribute");
        String propertyUpdateString = activityStreamResourceBundle.getString("plm.change.dcr.update.attributes.attribute").substring(0, 21);

        String json = as.getData();
        try {
            List<ASAttributeChangeDTO> propChanges = objectMapper.readValue(json, new TypeReference<List<ASAttributeChangeDTO>>() {
            });
            propChanges.forEach(p ->
            {
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

    private String getDCRFilesAddedJson(List<PLMChangeFile> files) {
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

    private String getDCRFoldersAddedJson(PLMFile file) {
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

    private String getDCRFoldersDeletedJson(PLMFile file) {
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

    private String getDCRFilesAddedString(String messageString, PLMDCR dcr, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), dcr.getCrNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.change.dcr.files.add.file");

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

    private String getDCRFoldersAddedString(String messageString, PLMDCR dcr, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), dcr.getCrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getDCRFoldersDeletedString(String messageString, PLMDCR dcr, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), dcr.getCrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getDCRFileDeletedString(String messageString, PLMDCR dcr, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), dcr.getCrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getDCRFilesVersionedJson(List<PLMChangeFile> files) {
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

    private String getDCRFilesVersionedString(String messageString, PLMDCR dcr, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), dcr.getCrNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.change.dcr.files.version.file");

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

    private String getDCRFileRenamedString(String messageString, PLMDCR dcr, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    dcr.getCrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getDCRFileString(String messageString, PLMDCR dcr, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    dcr.getCrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getDCRWorkflowStartString(String messageString, PLMDCR dcr, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                dcr.getCrNumber());
    }

    private String getDCRWorkflowFinishString(String messageString, PLMDCR dcr, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                dcr.getCrNumber());
    }

    private String getDCRWorkflowPromoteDemoteString(String messageString, PLMDCR dcr, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();
        ActivityStreamObject target = as.getTarget();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus fromActivity = workflowStatusRepository.findOne(source.getObject());
        PLMWorkflowStatus toActivity = workflowStatusRepository.findOne(target.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                dcr.getCrNumber(),
                highlightValue(fromActivity.getName()),
                highlightValue(toActivity.getName()));
    }

    private String getDCRWorkflowHoldUnholdString(String messageString, PLMDCR dcr, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus plmWorkflowActivity = workflowStatusRepository.findOne(source.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflowActivity.getName()),
                highlightValue(plmWorkflow.getName()),
                dcr.getCrNumber());
    }


    private String getDCRWorkflowAddedString(String messageString, PLMDCR dcr, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getNewValue()),
                    dcr.getCrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getDCRWorkflowChangeString(String messageString, PLMDCR dcr, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getOldValue()),
                    highlightValue(workflow.getNewValue()),
                    dcr.getCrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getDCRCommentString(String messageString, PLMDCR dcr, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                dcr.getCrNumber());

        String s = highlightValue("Comment: ");
        s += as.getData();
        s = addMarginToMessage(s);

        message += s;

        return message;
    }

    private String getDCRBasicInfoUpdatedJson(PLMDCR olddcr, PLMDCR dcr) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = olddcr.getCrNumber();
        String newValue = dcr.getCrNumber();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Number", oldValue, newValue));
        }

        oldValue = olddcr.getDescriptionOfChange();
        newValue = dcr.getDescriptionOfChange();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Description of Change", oldValue, newValue));
        }

        oldValue = olddcr.getTitle();
        newValue = dcr.getTitle();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Title", oldValue, newValue));
        }

        oldValue = olddcr.getReasonForChange();
        newValue = dcr.getReasonForChange();
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

    private String getDcrAffectedItemsAddedJson(List<PLMDCRAffectedItem> affectedItems) {
        List<ASNewDcrItem> asNewDcrItems = new ArrayList<>();
        affectedItems.forEach(affectedItem -> {
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(affectedItem.getItem());
            PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
            ASNewDcrItem asNewDcrItem = new ASNewDcrItem(itemRevision.getId(), item.getItemName(), item.getItemNumber(), itemRevision.getRevision(), itemRevision.getLifeCyclePhase().getPhase());
            asNewDcrItems.add(asNewDcrItem);
        });
        String json = null;
        try {
            json = objectMapper.writeValueAsString(asNewDcrItems);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getDcrAffectedItemsDeletedJson(PLMDCRAffectedItem affectedItem) {
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

    private String getDcrAffectedItemsAddedString(String messageString, PLMDCR dcr, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), dcr.getCrNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.change.dcr.affectedItems.add.item");

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

    private String getDcrAffectedItemsDeletedString(String messageString, PLMDCR dcr, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewDcrItem> asNewDcrItems = objectMapper.readValue(json, new TypeReference<List<ASNewDcrItem>>() {
            });
            ASNewDcrItem asNewDcrItem = asNewDcrItems.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(asNewDcrItem.getItemNumber()), highlightValue(asNewDcrItem.getItemName()), highlightValue(asNewDcrItem.getRevision()), highlightValue(asNewDcrItem.getLifecyclePhase()), dcr.getCrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getDcrRelatedItemsAddedJson(List<PLMChangeRelatedItem> relatedItems) {
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

    private String getDcrRelatedItemsDeletedJson(PLMChangeRelatedItem relatedItem) {
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

    private String getDcrRelatedItemsAddedString(String messageString, PLMDCR dcr, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), dcr.getCrNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.change.dcr.relatedItems.add.item");

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

    private String getDcrRelatedItemsDeletedString(String messageString, PLMDCR dcr, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewDcrItem> asNewDcrItems = objectMapper.readValue(json, new TypeReference<List<ASNewDcrItem>>() {
            });
            ASNewDcrItem asNewDcrItem = asNewDcrItems.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(asNewDcrItem.getItemNumber()), highlightValue(asNewDcrItem.getItemName()), highlightValue(asNewDcrItem.getRevision()), highlightValue(asNewDcrItem.getLifecyclePhase()), dcr.getCrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }
}
