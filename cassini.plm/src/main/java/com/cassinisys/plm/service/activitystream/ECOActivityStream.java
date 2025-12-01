package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.ECOEvents;
import com.cassinisys.plm.model.cm.*;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.repo.cm.ChangeTypeAttributeRepository;
import com.cassinisys.plm.repo.cm.ECORepository;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ECOActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PLMWorkflowRepository workFlowRepository;
    @Autowired
    private PLMWorkflowStatusRepository workflowStatusRepository;
    @Autowired
    private ECORepository ecoRepository;
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
    public void ecoCreated(ECOEvents.ECOCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEco().getId());
        object.setType("eco");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.eco.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void ecoBasicInfoUpdated(ECOEvents.ECOBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PLMECO oldEco = event.getOldEco();
        PLMECO eco = event.getEco();

        object.setObject(oldEco.getId());
        object.setType("eco");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.eco.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getEcoBasicInfoUpdatedJson(oldEco, eco));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void ecoAttributesUpdated(ECOEvents.ECOAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PLMECO eco = event.getEco();

        PLMChangeAttribute oldAtt = event.getOldAttribute();
        PLMChangeAttribute newAtt = event.getNewAttribute();

        object.setObject(eco.getId());
        object.setType("eco");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.eco.update.attributes");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getECOAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void ecoFilesAdded(ECOEvents.ECOFilesAddedEvent event) {
        List<PLMChangeFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.eco.files.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEco().getId());
        object.setType("eco");
        as.setObject(object);
        as.setData(getECOFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void ecoFoldersAdded(ECOEvents.ECOFoldersAddedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.eco.files.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEco().getId());
        object.setType("eco");
        as.setObject(object);
        as.setData(getECOFoldersAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void ecoFoldersDeleted(ECOEvents.ECOFoldersDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.eco.files.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEco().getId());
        object.setType("eco");
        as.setObject(object);
        as.setData(getECOFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void ecoFileDeleted(ECOEvents.ECOFileDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.eco.files.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEco().getId());
        object.setType("eco");
        as.setObject(object);
        as.setData(getECOFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void ecoFilesVersioned(ECOEvents.ECOFilesVersionedEvent event) {
        List<PLMChangeFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.eco.files.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEco().getId());
        object.setType("eco");
        as.setObject(object);
        as.setData(getECOFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void ecoFileRenamed(ECOEvents.ECOFileRenamedEvent event) {
        PLMFile oldFile = event.getOldFile();
        PLMFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals("Rename")) as.setActivity("plm.change.eco.files.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.change.eco.files.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEco().getId());
        object.setType("eco");
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
    public void ecoFileLocked(ECOEvents.ECOFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.eco.files.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEco().getId());
        object.setType("eco");
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
    public void ecoFileUnlocked(ECOEvents.ECOFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.eco.files.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEco().getId());
        object.setType("eco");
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
    public void ecoFileDownloaded(ECOEvents.ECOFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.eco.files.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEco().getId());
        object.setType("eco");
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
    public void ecoWorkflowStarted(ECOEvents.ECOWorkflowStartedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.eco.workflow.start");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEco().getId());
        object.setType("eco");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void ecoWorkflowFinished(ECOEvents.ECOWorkflowFinishedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.eco.workflow.finish");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEco().getId());
        object.setType("eco");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void ecoWorkflowPromoted(ECOEvents.ECOWorkflowPromotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.eco.workflow.promote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEco().getId());
        object.setType("eco");
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
    public void ecoWorkflowDemoted(ECOEvents.ECOWorkflowDemotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.eco.workflow.demote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEco().getId());
        object.setType("eco");
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
    public void ecoWorkflowHold(ECOEvents.ECOWorkflowHoldEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.eco.workflow.hold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEco().getId());
        object.setType("eco");
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
    public void ecoWorkflowUnhold(ECOEvents.ECOWorkflowUnholdEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.eco.workflow.unhold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEco().getId());
        object.setType("eco");
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
    public void ecoWorkflowChange(ECOEvents.ECOWorkflowChangeEvent event) {
        ActivityStream as = new ActivityStream();
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEco().getId());
        object.setType("eco");
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
            as.setActivity("plm.change.eco.workflow.change");
        } else {
            as.setActivity("plm.change.eco.workflow.add");
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
    public void ecoCommentAdded(ECOEvents.ECOCommentAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.eco.comment");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEco().getId());
        object.setType("eco");
        as.setObject(object);
        as.setData(event.getComment().getComment());

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void ecoAffectedItemsAdded(ECOEvents.ECOAffectedItemAddedEvent event) {
        List<PLMAffectedItem> affectedItems = event.getAffectedItems();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.eco.affectedItems.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEco().getId());
        object.setType("eco");
        as.setObject(object);
        as.setData(getECOAffectedItemsAddedJson(affectedItems));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void mcoProblemItemUpdated(ECOEvents.ECOAffectedItemUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        object.setObject(event.getEco().getId());
        object.setType("eco");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.eco.affectedItems.update");
        as.setObject(object);
        as.setConverter(getConverterKey());

        List<ASProblemItemUpdate> changes = new ArrayList<>();
        Date oldDate = event.getOldAffectedItem().getEffectiveDate();
        Date newDate = event.getAffectedItem().getEffectiveDate();

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(event.getAffectedItem().getItem());
        PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
        if (oldDate == null && newDate != null) {
            String newDateValue = dateFormat.format(newDate);
            changes.add(new ASProblemItemUpdate(item.getItemNumber(), "Effective Date", "", newDateValue));
        } else if (oldDate != null && newDate != null) {
            if (!oldDate.equals(newDate)) {
                String oldDateValue = dateFormat.format(oldDate);
                String newDateValue = dateFormat.format(newDate);
                changes.add(new ASProblemItemUpdate(item.getItemNumber(), "Effective Date", oldDateValue, newDateValue));
            }
        } else if (oldDate != null) {
            String oldDateValue = dateFormat.format(oldDate);
            changes.add(new ASProblemItemUpdate(item.getItemNumber(), "Effective Date", oldDateValue, ""));
        }

        String oldValue = event.getOldAffectedItem().getNotes();
        String newValue = event.getAffectedItem().getNotes();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!oldValue.equals(newValue)) {
            changes.add(new ASProblemItemUpdate(item.getItemNumber(), "Notes", oldValue, newValue));
        }
        try {
            if (changes.size() > 0) {
                as.setData(objectMapper.writeValueAsString(changes));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void ecoAffectedItemsDeleted(ECOEvents.ECOAffectedItemDeletedEvent event) {
        PLMAffectedItem affectedItem = event.getAffectedItem();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.eco.affectedItems.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEco().getId());
        object.setType("eco");
        as.setObject(object);
        as.setData(getECOAffectedItemsDeletedJson(affectedItem));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void ecoChangeRequestsAdded(ECOEvents.ECOChangeRequestAddedEvent event) {
        List<PLMECR> ecrList = event.getEcrList();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.eco.changeRequests.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEco().getId());
        object.setType("eco");
        as.setObject(object);
        as.setData(getECOChangeRequestsAddedJson(ecrList));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void ecoChangeRequestsDeleted(ECOEvents.ECOChangeRequestDeletedEvent event) {
        PLMECR ecr = event.getEcr();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.eco.changeRequests.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEco().getId());
        object.setType("eco");
        as.setObject(object);
        as.setData(getECOChangeRequestsDeletedJson(ecr));

        activityStreamService.create(as);
    }

    @Override
    public String getConverterKey() {
        return "plm.change.eco";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            PLMECO eco = ecoRepository.findOne(object.getObject());

            if (eco == null) return "";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.change.eco.create":
                    convertedString = getECOCreatedString(messageString, actor, eco);
                    break;
                case "plm.change.eco.revision":
                    convertedString = getECORevisionCreatedString(messageString, actor, eco);
                    break;
                case "plm.change.eco.update.basicinfo":
                    convertedString = getECOBasicInfoUpdatedString(messageString, actor, eco, as);
                    break;
                case "plm.change.eco.update.attributes":
                    convertedString = getECOAttributeUpdatedString(messageString, actor, eco, as);
                    break;
                case "plm.change.eco.files.add":
                    convertedString = getECOFilesAddedString(messageString, eco, as);
                    break;
                case "plm.change.eco.files.delete":
                    convertedString = getECOFileDeletedString(messageString, eco, as);
                    break;
                case "plm.change.eco.files.folders.add":
                    convertedString = getECOFoldersAddedString(messageString, eco, as);
                    break;
                case "plm.change.eco.files.folders.delete":
                    convertedString = getECOFoldersDeletedString(messageString, eco, as);
                    break;
                case "plm.change.eco.files.version":
                    convertedString = getECOFilesVersionedString(messageString, eco, as);
                    break;
                case "plm.change.eco.files.rename":
                    convertedString = getECOFileRenamedString(messageString, eco, as);
                    break;
                case "plm.change.eco.files.replace":
                    convertedString = getECOFileRenamedString(messageString, eco, as);
                    break;
                case "plm.change.eco.files.lock":
                    convertedString = getECOFileString(messageString, eco, as);
                    break;
                case "plm.change.eco.files.unlock":
                    convertedString = getECOFileString(messageString, eco, as);
                    break;
                case "plm.change.eco.files.download":
                    convertedString = getECOFileString(messageString, eco, as);
                    break;
                case "plm.change.eco.workflow.start":
                    convertedString = getECOWorkflowStartString(messageString, eco, as);
                    break;
                case "plm.change.eco.workflow.finish":
                    convertedString = getECOWorkflowFinishString(messageString, eco, as);
                    break;
                case "plm.change.eco.workflow.promote":
                    convertedString = getECOWorkflowPromoteDemoteString(messageString, eco, as);
                    break;
                case "plm.change.eco.workflow.demote":
                    convertedString = getECOWorkflowPromoteDemoteString(messageString, eco, as);
                    break;
                case "plm.change.eco.workflow.hold":
                    convertedString = getECOWorkflowHoldUnholdString(messageString, eco, as);
                    break;
                case "plm.change.eco.workflow.unhold":
                    convertedString = getECOWorkflowHoldUnholdString(messageString, eco, as);
                    break;
                case "plm.change.eco.workflow.add":
                    convertedString = getECOWorkflowAddedString(messageString, eco, as);
                    break;
                case "plm.change.eco.workflow.change":
                    convertedString = getECOWorkflowChangeString(messageString, eco, as);
                    break;
                case "plm.change.eco.comment":
                    convertedString = getECOCommentString(messageString, eco, as);
                    break;
                case "plm.change.eco.affectedItems.add":
                    convertedString = getECOAffectedItemsAddedString(messageString, eco, as);
                    break;
                case "plm.change.eco.affectedItems.update":
                    convertedString = getECOAffectedItemsUpdatedString(messageString, eco, as);
                    break;
                case "plm.change.eco.affectedItems.delete":
                    convertedString = getECOAffectedItemsDeletedString(messageString, eco, as);
                    break;
                case "plm.change.eco.changeRequests.add":
                    convertedString = getECOChangeRequestsAddedString(messageString, eco, as);
                    break;
                case "plm.change.eco.changeRequests.delete":
                    convertedString = getECOChangeRequestsDeletedString(messageString, eco, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getECOCreatedString(String messageString, Person actor, PLMECO eco) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), eco.getEcoNumber());
    }

    private String getECORevisionCreatedString(String messageString, Person actor, PLMECO eco) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), eco.getEcoNumber());
    }

    private String getECOBasicInfoUpdatedString(String messageString, Person actor, PLMECO eco, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), eco.getEcoNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.change.eco.update.basicinfo.property");

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

    private String getEcoBasicInfoUpdatedJson(PLMECO oldEco, PLMECO eco) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldEco.getTitle();
        String newValue = eco.getTitle();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Title", oldValue, newValue));
        }

        oldValue = oldEco.getDescription();
        newValue = eco.getDescription();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue, newValue));
        }

        oldValue = oldEco.getReasonForChange();
        newValue = eco.getReasonForChange();
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

    private String getECOAttributesUpdatedJson(PLMChangeAttribute oldAttribute, PLMChangeAttribute newAttribute) {
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

    private String getECOAttributeUpdatedString(String messageString, Person actor, PLMECO eco, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), eco.getEcoNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.change.eco.update.attributes.attribute");
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

    private String getECOFilesAddedString(String messageString, PLMECO eco, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), eco.getEcoNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.change.eco.files.add.file");

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

    private String getECOFoldersAddedString(String messageString, PLMECO eco, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), eco.getEcoNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getECOFoldersDeletedString(String messageString, PLMECO eco, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), eco.getEcoNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getECOFileDeletedString(String messageString, PLMECO eco, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), eco.getEcoNumber());
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

    private String getECOFilesVersionedString(String messageString, PLMECO eco, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), eco.getEcoNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.change.eco.files.version.file");

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

    private String getECOFileRenamedString(String messageString, PLMECO eco, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    eco.getEcoNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getECOFileString(String messageString, PLMECO eco, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    eco.getEcoNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getECOWorkflowStartString(String messageString, PLMECO eco, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                eco.getEcoNumber());
    }

    private String getECOWorkflowFinishString(String messageString, PLMECO eco, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                eco.getEcoNumber());
    }

    private String getECOWorkflowPromoteDemoteString(String messageString, PLMECO eco, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();
        ActivityStreamObject target = as.getTarget();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus fromActivity = workflowStatusRepository.findOne(source.getObject());
        PLMWorkflowStatus toActivity = workflowStatusRepository.findOne(target.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                eco.getEcoNumber(),
                highlightValue(fromActivity.getName()),
                highlightValue(toActivity.getName()));
    }

    private String getECOWorkflowHoldUnholdString(String messageString, PLMECO eco, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus plmWorkflowActivity = workflowStatusRepository.findOne(source.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflowActivity.getName()),
                highlightValue(plmWorkflow.getName()),
                eco.getEcoNumber());
    }

    private String getECOWorkflowAddedString(String messageString, PLMECO eco, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getNewValue()),
                    eco.getEcoNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getECOWorkflowChangeString(String messageString, PLMECO eco, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getOldValue()),
                    highlightValue(workflow.getNewValue()),
                    eco.getEcoNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }


    private String getECOCommentString(String messageString, PLMECO eco, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                eco.getEcoNumber());

        String s = highlightValue("Comment: ");
        s += as.getData();
        s = addMarginToMessage(s);

        message += s;

        return message;
    }

    private String getECOAffectedItemsAddedJson(List<PLMAffectedItem> affectedItems) {
        List<ASNewAffectedItem> asNewAffectedItems = new ArrayList<>();
        for (PLMAffectedItem affectedItem : affectedItems) {
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(affectedItem.getItem());
            PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
            ASNewAffectedItem asNewAffectedItem = new ASNewAffectedItem(affectedItem.getItem(), item.getItemName(), item.getItemNumber(), affectedItem.getFromRevision(), "");
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

    private String getECOAffectedItemsDeletedJson(PLMAffectedItem affectedItem) {
        String json = null;
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(affectedItem.getItem());
        PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());

        ASNewAffectedItem asNewAffectedItem = new ASNewAffectedItem(affectedItem.getItem(), item.getItemName(), item.getItemNumber(), affectedItem.getFromRevision(), "");

        List<ASNewAffectedItem> asNewAffectedItems = new ArrayList<>();
        asNewAffectedItems.add(asNewAffectedItem);
        try {
            json = objectMapper.writeValueAsString(asNewAffectedItems);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getECOChangeRequestsAddedJson(List<PLMECR> plmecrList) {
        List<ASNewChangeRequest> asNewChangeRequests = new ArrayList<>();
        for (PLMECR plmecr : plmecrList) {
            ASNewChangeRequest newChangeRequest = new ASNewChangeRequest(plmecr.getId(), plmecr.getCrNumber(), plmecr.getTitle());
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

    private String getECOChangeRequestsDeletedJson(PLMECR plmecr) {
        String json = null;

        ASNewChangeRequest asNewChangeRequest = new ASNewChangeRequest(plmecr.getId(), plmecr.getCrNumber(), plmecr.getTitle());

        List<ASNewChangeRequest> asNewChangeRequests = new ArrayList<>();
        asNewChangeRequests.add(asNewChangeRequest);
        try {
            json = objectMapper.writeValueAsString(asNewChangeRequests);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getECOAffectedItemsAddedString(String messageString, PLMECO eco, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), eco.getEcoNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.change.eco.affectedItems.add.item");

        String json = as.getData();
        try {
            List<ASNewAffectedItem> affectedItems = objectMapper.readValue(json, new TypeReference<List<ASNewAffectedItem>>() {
            });
            affectedItems.forEach(affectedItem -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(affectedItem.getItemName()), highlightValue(affectedItem.getRevision()),
                        highlightValue(affectedItem.getLifecyclePhase())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getECOAffectedItemsUpdatedString(String messageString, PLMECO eco, ActivityStream as) {
        StringBuffer sb = new StringBuffer();

        String updateString = activityStreamResourceBundle.getString("plm.change.eco.affectedItems.update.property");

        String json = as.getData();
        try {
            List<ASProblemItemUpdate> propChanges = objectMapper.readValue(json, new TypeReference<List<ASProblemItemUpdate>>() {
            });
            String activityString = MessageFormat.format(messageString, as.getActor().getFullName(), highlightValue(propChanges.get(0).getItemNumber()),
                    eco.getEcoNumber());
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

    private String getECOAffectedItemsDeletedString(String messageString, PLMECO eco, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewAffectedItem> asNewAffectedItems = objectMapper.readValue(json, new TypeReference<List<ASNewAffectedItem>>() {
            });
            ASNewAffectedItem asNewAffectedItem = asNewAffectedItems.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(asNewAffectedItem.getItemName()), highlightValue(asNewAffectedItem.getRevision()), highlightValue(asNewAffectedItem.getLifecyclePhase()), eco.getEcoNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getECOChangeRequestsAddedString(String messageString, PLMECO eco, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), eco.getEcoNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.change.eco.changeRequests.add.ecr");

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

    private String getECOChangeRequestsDeletedString(String messageString, PLMECO eco, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewChangeRequest> asNewChangeRequests = objectMapper.readValue(json, new TypeReference<List<ASNewChangeRequest>>() {
            });
            ASNewChangeRequest newChangeRequest = asNewChangeRequests.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(newChangeRequest.getCrNumber()), highlightValue(newChangeRequest.getTitle()), eco.getEcoNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }
}
