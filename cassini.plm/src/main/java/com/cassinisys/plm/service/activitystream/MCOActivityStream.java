package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.MCOEvents;
import com.cassinisys.plm.model.cm.*;
import com.cassinisys.plm.model.mes.MESMBOM;
import com.cassinisys.plm.model.mes.MESMBOMRevision;
import com.cassinisys.plm.model.mes.MESObject;
import com.cassinisys.plm.model.mfr.PLMManufacturerPart;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.repo.cm.ChangeTypeAttributeRepository;
import com.cassinisys.plm.repo.cm.MCORepository;
import com.cassinisys.plm.repo.mes.MESMBOMRepository;
import com.cassinisys.plm.repo.mes.MESMBOMRevisionRepository;
import com.cassinisys.plm.repo.mes.MESObjectRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerPartRepository;
import com.cassinisys.plm.repo.plm.ItemRepository;
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
public class MCOActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PLMWorkflowRepository workFlowRepository;
    @Autowired
    private PLMWorkflowStatusRepository workflowStatusRepository;
    @Autowired
    private MCORepository mcoRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository workflowDefinitionRepository;
    @Autowired
    private ChangeTypeAttributeRepository changeTypeAttributeRepository;
    @Autowired
    private ManufacturerPartRepository manufacturerPartRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CommonActivityStream commonActivityStream;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private MESMBOMRevisionRepository mbomRevisionRepository;
    @Autowired
    private MESObjectRepository mesObjectRepository;
    @Autowired
    private MESMBOMRepository mbomRepository;

    @Async
    @EventListener
    public void mcoCreated(MCOEvents.MCOCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMco().getId());
        object.setType("mco");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.mco.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void problemReportBasicInfoUpdated(MCOEvents.MCOBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PLMMCO oldMco = event.getOldMco();
        PLMMCO mco = event.getMco();

        object.setObject(mco.getId());
        object.setType("mco");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.mco.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getMcoBasicInfoUpdatedJson(oldMco, mco));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void mcoAttributesUpdated(MCOEvents.MCOAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PLMMCO mco = event.getMco();

        PLMChangeAttribute oldAtt = event.getOldAttribute();
        PLMChangeAttribute newAtt = event.getNewAttribute();

        object.setObject(mco.getId());
        object.setType("mco");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.mco.update.attributes");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getMCOAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void mcoFilesAdded(MCOEvents.MCOFilesAddedEvent event) {
        List<PLMChangeFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.mco.files.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMco().getId());
        object.setType("mco");
        as.setObject(object);
        as.setData(getMCOFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void mcoFoldersAdded(MCOEvents.MCOFoldersAddedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.mco.files.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMco().getId());
        object.setType("mco");
        as.setObject(object);
        as.setData(getMCOFoldersAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void mcoFoldersDeleted(MCOEvents.MCOFoldersDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.mco.files.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMco().getId());
        object.setType("mco");
        as.setObject(object);
        as.setData(getMCOFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void mcoFileDeleted(MCOEvents.MCOFileDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.mco.files.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMco().getId());
        object.setType("mco");
        as.setObject(object);
        as.setData(getMCOFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void mcoFilesVersioned(MCOEvents.MCOFilesVersionedEvent event) {
        List<PLMChangeFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.mco.files.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMco().getId());
        object.setType("mco");
        as.setObject(object);
        as.setData(getMCOFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void mcoFileRenamed(MCOEvents.MCOFileRenamedEvent event) {
        PLMFile oldFile = event.getOldFile();
        PLMFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals("Rename")) as.setActivity("plm.change.mco.files.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.change.mco.files.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMco().getId());
        object.setType("mco");
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
    public void mcoFileLocked(MCOEvents.MCOFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.mco.files.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMco().getId());
        object.setType("mco");
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
    public void mcoFileUnlocked(MCOEvents.MCOFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.mco.files.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMco().getId());
        object.setType("mco");
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
    public void mcoFileDownloaded(MCOEvents.MCOFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.mco.files.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMco().getId());
        object.setType("mco");
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
    public void mcoWorkflowStarted(MCOEvents.MCOWorkflowStartedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.mco.workflow.start");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMco().getId());
        object.setType("mco");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void mcoWorkflowFinished(MCOEvents.MCOWorkflowFinishedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.mco.workflow.finish");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMco().getId());
        object.setType("mco");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void mcoWorkflowPromoted(MCOEvents.MCOWorkflowPromotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.mco.workflow.promote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMco().getId());
        object.setType("mco");
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
    public void mcoWorkflowDemoted(MCOEvents.MCOWorkflowDemotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.mco.workflow.demote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMco().getId());
        object.setType("mco");
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
    public void mcoWorkflowHold(MCOEvents.MCOWorkflowHoldEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.mco.workflow.hold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMco().getId());
        object.setType("mco");
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
    public void mcoWorkflowUnhold(MCOEvents.MCOWorkflowUnholdEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.mco.workflow.unhold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMco().getId());
        object.setType("mco");
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
    public void mcoWorkflowChange(MCOEvents.MCOWorkflowChangeEvent event) {
        ActivityStream as = new ActivityStream();
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMco().getId());
        object.setType("mco");
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
            as.setActivity("plm.change.mco.workflow.change");
        } else {
            as.setActivity("plm.change.mco.workflow.add");
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
    public void mcoCommentAdded(MCOEvents.MCOCommentAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.mco.comment");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMco().getId());
        object.setType("mco");
        as.setObject(object);
        as.setData(event.getComment().getComment());

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void mcoAffectedItemsAdded(MCOEvents.MCOAffectedItemAddedEvent event) {
        List<PLMMCOAffectedItem> affectedItems = event.getAffectedItems();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.mco.affectedItems.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMco().getId());
        object.setType("mco");
        as.setObject(object);
        as.setData(getMCOAffectedItemsAddedJson(affectedItems));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void mcoAffectedItemsDeleted(MCOEvents.MCOAffectedItemDeletedEvent event) {
        PLMMCOAffectedItem affectedItem = event.getAffectedItem();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.mco.affectedItems.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMco().getId());
        object.setType("mco");
        as.setObject(object);
        as.setData(getMCOAffectedItemsDeletedJson(affectedItem));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void mcoRelatedItemsAdded(MCOEvents.MCORelatedItemAddedEvent event) {
        List<PLMMCORelatedItem> relatedItems = event.getRelatedItems();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.mco.relatedItems.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMco().getId());
        object.setType("ncr");
        as.setObject(object);
        as.setData(getMCORelatedItemsAddedJson(relatedItems));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void mcoRelatedItemsDeleted(MCOEvents.MCORelatedItemDeletedEvent event) {
        PLMMCORelatedItem relatedItem = event.getRelatedItem();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.mco.relatedItems.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMco().getId());
        object.setType("ncr");
        as.setObject(object);
        as.setData(getMCORelatedItemsDeletedJson(relatedItem));

        activityStreamService.create(as);
    }


    @Async
    @EventListener
    public void mcoProblemItemUpdated(MCOEvents.MCOAffectedItemUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        object.setObject(event.getMco().getId());
        object.setType("mco");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.mco.affectedItems.update");
        as.setObject(object);
        as.setConverter(getConverterKey());

        List<ASProblemItemUpdate> changes = new ArrayList<>();
        String oldValue = event.getOldAffectedItem().getChangeType().toString();
        String newValue = event.getAffectedItem().getChangeType().toString();
        PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(event.getAffectedItem().getMaterial());
        if (!oldValue.equals(newValue)) {
            changes.add(new ASProblemItemUpdate(manufacturerPart.getPartNumber(), "Change Type", oldValue, newValue));
        }

        oldValue = event.getOldAffectedItem().getNotes();
        newValue = event.getAffectedItem().getNotes();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!oldValue.equals(newValue)) {
            changes.add(new ASProblemItemUpdate(manufacturerPart.getPartNumber(), "Notes", oldValue, newValue));
        }

        if (event.getAffectedItem().getChangeType().equals(MCOChangeType.REPLACED)) {
            Integer oldReplacement = event.getOldAffectedItem().getReplacement();
            Integer newReplacement = event.getAffectedItem().getReplacement();

            if (oldReplacement == null) {
                oldReplacement = 0;
            }
            if (!oldReplacement.equals(newReplacement)) {
                PLMManufacturerPart oldReplacementPart = manufacturerPartRepository.findOne(oldReplacement);
                PLMManufacturerPart newReplacementPart = manufacturerPartRepository.findOne(newReplacement);

                String oldPart = "";
                String newPart = "";
                if (oldReplacementPart != null) {
                    oldPart = oldReplacementPart.getPartNumber() + " - " + oldReplacementPart.getPartName();
                }
                if (newReplacementPart != null) {
                    newPart = newReplacementPart.getPartNumber() + " - " + newReplacementPart.getPartName();
                }
                changes.add(new ASProblemItemUpdate(manufacturerPart.getPartNumber(), "Replacement Part", oldPart, newPart));
            }
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
    public void mcoRelatedItemUpdated(MCOEvents.MCORelatedItemUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        object.setObject(event.getMco().getId());
        object.setType("mco");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.mco.relatedItems.update");
        as.setObject(object);
        as.setConverter(getConverterKey());
        List<ASProblemItemUpdate> changes = new ArrayList<>();
        String oldValue = event.getOldRelatedItem().getNotes();
        String newValue = event.getRelatedItem().getNotes();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!oldValue.equals(newValue)) {
            changes.add(new ASProblemItemUpdate(event.getRelatedItem().getPart().getPartNumber(), "Notes", oldValue, newValue));
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
    public void mcoProductAffectedItemsAdded(MCOEvents.MCOProductAffectedItemAddedEvent event) {
        List<PLMMCOProductAffectedItem> productAffectedItems = event.getProductAffectedItems();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.mco.productaAffectedItems.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMco().getId());
        object.setType("mco");
        as.setObject(object);
        as.setData(getMCOProductAffectedItemsAddedJson(productAffectedItems));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void mcoProductAffectedItemsDeleted(MCOEvents.MCOProductAffectedItemDeletedEvent event) {
        PLMMCOProductAffectedItem productAffectedItem = event.getProductAffectedItem();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.mco.productaAffectedItems.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMco().getId());
        object.setType("mco");
        as.setObject(object);
        as.setData(getMCOProductAffectedItemsDeletedJson(productAffectedItem));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void mcoProductAffectedItemUpdated(MCOEvents.MCOProductAffectedItemUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        object.setObject(event.getMco().getId());
        object.setType("mco");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.mco.productaAffectedItems.update");
        as.setObject(object);
        as.setConverter(getConverterKey());

        List<ASProblemItemUpdate> changes = new ArrayList<>();
        Date oldDate = event.getOldProductAffectedItem().getEffectiveDate();
        Date newDate = event.getProductAffectedItem().getEffectiveDate();

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        MESMBOMRevision mbomRevision = mbomRevisionRepository.findOne(event.getProductAffectedItem().getItem());
        MESMBOM mbom = mbomRepository.findOne(mbomRevision.getMaster());
        if (oldDate == null && newDate != null) {
            String newDateValue = dateFormat.format(newDate);
            changes.add(new ASProblemItemUpdate(mbom.getNumber(), "Effective Date", "", newDateValue));
        } else if (oldDate != null && newDate != null) {
            if (!oldDate.equals(newDate)) {
                String oldDateValue = dateFormat.format(oldDate);
                String newDateValue = dateFormat.format(newDate);
                changes.add(new ASProblemItemUpdate(mbom.getNumber(), "Effective Date", oldDateValue, newDateValue));
            }
        } else if (oldDate != null) {
            String oldDateValue = dateFormat.format(oldDate);
            changes.add(new ASProblemItemUpdate(mbom.getNumber(), "Effective Date", oldDateValue, ""));
        }

        String oldValue = event.getOldProductAffectedItem().getNotes();
        String newValue = event.getProductAffectedItem().getNotes();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!oldValue.equals(newValue)) {
            changes.add(new ASProblemItemUpdate(mbom.getNumber(), "Notes", oldValue, newValue));
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


    @Override
    public String getConverterKey() {
        return "plm.change.mco";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            PLMMCO mco = mcoRepository.findOne(object.getObject());

            if (mco == null) return "";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.change.mco.create":
                    convertedString = getMCOCreatedString(messageString, actor, mco);
                    break;
                case "plm.change.mco.revision":
                    convertedString = getMCORevisionCreatedString(messageString, actor, mco);
                    break;
                case "plm.change.mco.update.basicinfo":
                    convertedString = getMCOBasicInfoUpdatedString(messageString, actor, mco, as);
                    break;
                case "plm.change.mco.update.attributes":
                    convertedString = getMCOAttributeUpdatedString(messageString, actor, mco, as);
                    break;
                case "plm.change.mco.files.add":
                    convertedString = getMCOFilesAddedString(messageString, mco, as);
                    break;
                case "plm.change.mco.files.delete":
                    convertedString = getMCOFileDeletedString(messageString, mco, as);
                    break;
                case "plm.change.mco.files.folders.add":
                    convertedString = getMCOFoldersAddedString(messageString, mco, as);
                    break;
                case "plm.change.mco.files.folders.delete":
                    convertedString = getMCOFoldersDeletedString(messageString, mco, as);
                    break;
                case "plm.change.mco.files.version":
                    convertedString = getMCOFilesVersionedString(messageString, mco, as);
                    break;
                case "plm.change.mco.files.rename":
                    convertedString = getMCOFileRenamedString(messageString, mco, as);
                    break;
                case "plm.change.mco.files.replace":
                    convertedString = getMCOFileRenamedString(messageString, mco, as);
                    break;
                case "plm.change.mco.files.lock":
                    convertedString = getMCOFileString(messageString, mco, as);
                    break;
                case "plm.change.mco.files.unlock":
                    convertedString = getMCOFileString(messageString, mco, as);
                    break;
                case "plm.change.mco.files.download":
                    convertedString = getMCOFileString(messageString, mco, as);
                    break;
                case "plm.change.mco.workflow.start":
                    convertedString = getMCOWorkflowStartString(messageString, mco, as);
                    break;
                case "plm.change.mco.workflow.finish":
                    convertedString = getMCOWorkflowFinishString(messageString, mco, as);
                    break;
                case "plm.change.mco.workflow.promote":
                    convertedString = getMCOWorkflowPromoteDemoteString(messageString, mco, as);
                    break;
                case "plm.change.mco.workflow.demote":
                    convertedString = getMCOWorkflowPromoteDemoteString(messageString, mco, as);
                    break;
                case "plm.change.mco.workflow.hold":
                    convertedString = getMCOWorkflowHoldUnholdString(messageString, mco, as);
                    break;
                case "plm.change.mco.workflow.unhold":
                    convertedString = getMCOWorkflowHoldUnholdString(messageString, mco, as);
                    break;
                case "plm.change.mco.workflow.add":
                    convertedString = getMCOWorkflowAddedString(messageString, mco, as);
                    break;
                case "plm.change.mco.workflow.change":
                    convertedString = getMCOWorkflowChangeString(messageString, mco, as);
                    break;
                case "plm.change.mco.comment":
                    convertedString = getMCOCommentString(messageString, mco, as);
                    break;
                case "plm.change.mco.affectedItems.add":
                    convertedString = getMCOAffectedItemsAddedString(messageString, mco, as);
                    break;
                case "plm.change.mco.affectedItems.update":
                    convertedString = getAffectedItemsUpdatedString(messageString, mco, as);
                    break;
                case "plm.change.mco.affectedItems.delete":
                    convertedString = getMCOAffectedItemsDeletedString(messageString, mco, as);
                    break;
                case "plm.change.mco.relatedItems.add":
                    convertedString = getMCORelatedItemsAddedString(messageString, mco, as);
                    break;
                case "plm.change.mco.relatedItems.update":
                    convertedString = getRelatedItemsUpdatedString(messageString, mco, as);
                    break;
                case "plm.change.mco.relatedItems.delete":
                    convertedString = getMCORelatedItemsDeletedString(messageString, mco, as);
                    break;
                case "plm.change.mco.productaAffectedItems.add":
                    convertedString = getMCOProductAffectedItemsAddedString(messageString, mco, as);
                    break;  
                case "plm.change.mco.productaAffectedItems.delete":
                    convertedString = getMCOProductAffectedItemsDeletedString(messageString, mco, as);
                    break;
                case "plm.change.mco.productaAffectedItems.update":
                    convertedString = getProductAffectedItemsUpdatedString(messageString, mco, as);
                    break;          
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getMCOCreatedString(String messageString, Person actor, PLMMCO mco) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), mco.getMcoNumber());
    }

    private String getMCORevisionCreatedString(String messageString, Person actor, PLMMCO mco) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), mco.getMcoNumber());
    }

    private String getMCOBasicInfoUpdatedString(String messageString, Person actor, PLMMCO mco, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), mco.getMcoNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.change.mco.update.basicinfo.property");

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

    private String getMcoBasicInfoUpdatedJson(PLMMCO oldMco, PLMMCO mco) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldMco.getTitle();
        String newValue = mco.getTitle();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Title", oldValue, newValue));
        }

        oldValue = oldMco.getDescription();
        newValue = mco.getDescription();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Description of Change", oldValue, newValue));
        }

        oldValue = oldMco.getReasonForChange();
        newValue = mco.getReasonForChange();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Reason for Change", oldValue, newValue));
        }

        Integer oldValue1 = oldMco.getChangeAnalyst();
        Integer newValue1 = mco.getChangeAnalyst();

        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Change Analyst", personRepository.findOne(oldValue1).getFullName(), personRepository.findOne(newValue1).getFullName()));
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


    private String getMCOAttributesUpdatedJson(PLMChangeAttribute oldAttribute, PLMChangeAttribute newAttribute) {
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

    private String getMCOAttributeUpdatedString(String messageString, Person actor, PLMMCO mco, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), mco.getMcoNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.change.mco.update.attributes.attribute");
        String propertyUpdateString = activityStreamResourceBundle.getString("plm.change.mco.update.attributes.attribute").substring(0, 21);

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

    private String getMCOFilesAddedJson(List<PLMChangeFile> files) {
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

    private String getMCOFoldersAddedJson(PLMFile file) {
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

    private String getMCOFoldersDeletedJson(PLMFile file) {
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

    private String getMCOFilesAddedString(String messageString, PLMMCO mco, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), mco.getMcoNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.change.mco.files.add.file");

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

    private String getMCOFoldersAddedString(String messageString, PLMMCO mco, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), mco.getMcoNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getMCOFoldersDeletedString(String messageString, PLMMCO mco, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), mco.getMcoNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getMCOFileDeletedString(String messageString, PLMMCO mco, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), mco.getMcoNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getMCOFilesVersionedJson(List<PLMChangeFile> files) {
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

    private String getMCOFilesVersionedString(String messageString, PLMMCO mco, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), mco.getMcoNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.change.mco.files.version.file");

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

    private String getMCOFileRenamedString(String messageString, PLMMCO mco, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    mco.getMcoNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getMCOFileString(String messageString, PLMMCO mco, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    mco.getMcoNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getMCOWorkflowStartString(String messageString, PLMMCO mco, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                mco.getMcoNumber());
    }

    private String getMCOWorkflowFinishString(String messageString, PLMMCO mco, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                mco.getMcoNumber());
    }

    private String getMCOWorkflowPromoteDemoteString(String messageString, PLMMCO mco, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();
        ActivityStreamObject target = as.getTarget();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus fromActivity = workflowStatusRepository.findOne(source.getObject());
        PLMWorkflowStatus toActivity = workflowStatusRepository.findOne(target.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                mco.getMcoNumber(),
                highlightValue(fromActivity.getName()),
                highlightValue(toActivity.getName()));
    }

    private String getMCOWorkflowHoldUnholdString(String messageString, PLMMCO mco, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus plmWorkflowActivity = workflowStatusRepository.findOne(source.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflowActivity.getName()),
                highlightValue(plmWorkflow.getName()),
                mco.getMcoNumber());
    }

    private String getMCOWorkflowAddedString(String messageString, PLMMCO mco, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getNewValue()),
                    mco.getMcoNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getMCOWorkflowChangeString(String messageString, PLMMCO mco, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getOldValue()),
                    highlightValue(workflow.getNewValue()),
                    mco.getMcoNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getMCOCommentString(String messageString, PLMMCO mco, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                mco.getMcoNumber());

        String s = highlightValue("Comment: ");
        s += as.getData();
        s = addMarginToMessage(s);

        message += s;

        return message;
    }

    private String getMCOAffectedItemsAddedJson(List<PLMMCOAffectedItem> affectedItems) {
        List<ASNewNCRProblemItem> asNewAffectedItems = new ArrayList<>();
        for (PLMMCOAffectedItem affectedItem : affectedItems) {
            PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(affectedItem.getMaterial());
            ASNewNCRProblemItem asNewAffectedItem = new ASNewNCRProblemItem(affectedItem.getMaterial(), manufacturerPart.getPartName(), manufacturerPart.getPartNumber(), manufacturerPart.getMfrPartType().getName());
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

    private String getMCOAffectedItemsDeletedJson(PLMMCOAffectedItem affectedItem) {
        String json = null;
        PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(affectedItem.getMaterial());
        ASNewNCRProblemItem asNewAffectedItem = new ASNewNCRProblemItem(affectedItem.getMaterial(), manufacturerPart.getPartName(), manufacturerPart.getPartNumber(), manufacturerPart.getMfrPartType().getName());

        List<ASNewNCRProblemItem> asNewAffectedItems = new ArrayList<>();
        asNewAffectedItems.add(asNewAffectedItem);
        try {
            json = objectMapper.writeValueAsString(asNewAffectedItems);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getMCOAffectedItemsAddedString(String messageString, PLMMCO mco, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), mco.getMcoNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.change.mco.affectedItems.add.item");

        String json = as.getData();
        try {
            List<ASNewNCRProblemItem> affectedItems = objectMapper.readValue(json, new TypeReference<List<ASNewNCRProblemItem>>() {
            });
            affectedItems.forEach(affectedItem -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(affectedItem.getPartName()), highlightValue(affectedItem.getPartNumber()),
                        highlightValue(affectedItem.getPartType())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getAffectedItemsUpdatedString(String messageString, PLMMCO mco, ActivityStream as) {
        StringBuffer sb = new StringBuffer();

        String updateString = activityStreamResourceBundle.getString("plm.change.mco.affectedItems.update.property");

        String json = as.getData();
        try {
            List<ASProblemItemUpdate> propChanges = objectMapper.readValue(json, new TypeReference<List<ASProblemItemUpdate>>() {
            });
            String activityString = MessageFormat.format(messageString, as.getActor().getFullName(), highlightValue(propChanges.get(0).getItemNumber()),
                    mco.getMcoNumber());
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

    private String getRelatedItemsUpdatedString(String messageString, PLMMCO mco, ActivityStream as) {
        StringBuffer sb = new StringBuffer();

        String updateString = activityStreamResourceBundle.getString("plm.change.mco.relatedItems.update.property");

        String json = as.getData();
        try {
            List<ASProblemItemUpdate> propChanges = objectMapper.readValue(json, new TypeReference<List<ASProblemItemUpdate>>() {
            });
            String activityString = MessageFormat.format(messageString, as.getActor().getFullName(), highlightValue(propChanges.get(0).getItemNumber()),
                    mco.getMcoNumber());
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

    private String getMCOAffectedItemsDeletedString(String messageString, PLMMCO mco, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewNCRProblemItem> asNewAffectedItems = objectMapper.readValue(json, new TypeReference<List<ASNewNCRProblemItem>>() {
            });
            ASNewNCRProblemItem asNewAffectedItem = asNewAffectedItems.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(asNewAffectedItem.getPartName()), highlightValue(asNewAffectedItem.getPartNumber()), highlightValue(asNewAffectedItem.getPartType()), mco.getMcoNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getMCORelatedItemsAddedJson(List<PLMMCORelatedItem> relatedItems) {
        List<ASNewNCRProblemItem> asNewNCRProblemItems = new ArrayList<>();
        for (PLMMCORelatedItem relatedItem : relatedItems) {
            ASNewNCRProblemItem asNewNCRProblemItem = new ASNewNCRProblemItem(relatedItem.getPart().getId(), relatedItem.getPart().getPartName(),
                    relatedItem.getPart().getPartNumber(), relatedItem.getPart().getMfrPartType().getName());
            asNewNCRProblemItems.add(asNewNCRProblemItem);
        }
        String json = null;
        try {
            json = objectMapper.writeValueAsString(asNewNCRProblemItems);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getMCORelatedItemsDeletedJson(PLMMCORelatedItem relatedItem) {
        String json = null;
        ASNewNCRProblemItem asNewNCRProblemItem = new ASNewNCRProblemItem(relatedItem.getPart().getId(), relatedItem.getPart().getPartName(), relatedItem.getPart().getPartNumber(), relatedItem.getPart().getMfrPartType().getName());

        List<ASNewNCRProblemItem> asNewNCRProblemItems = new ArrayList<>();
        asNewNCRProblemItems.add(asNewNCRProblemItem);
        try {
            json = objectMapper.writeValueAsString(asNewNCRProblemItems);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getMCORelatedItemsAddedString(String messageString, PLMMCO plmmco, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), plmmco.getMcoNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.change.mco.relatedItems.add.item");

        String json = as.getData();
        try {
            List<ASNewNCRProblemItem> parts = objectMapper.readValue(json, new TypeReference<List<ASNewNCRProblemItem>>() {
            });
            parts.forEach(part -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(part.getPartName()), highlightValue(part.getPartNumber()), highlightValue(part.getPartType())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getMCORelatedItemsDeletedString(String messageString, PLMMCO plmmco, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewNCRProblemItem> asNewNCRProblemItems = objectMapper.readValue(json, new TypeReference<List<ASNewNCRProblemItem>>() {
            });
            ASNewNCRProblemItem asNewNCRProblemItem = asNewNCRProblemItems.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(asNewNCRProblemItem.getPartName()), highlightValue(asNewNCRProblemItem.getPartNumber()), highlightValue(asNewNCRProblemItem.getPartType()), plmmco.getMcoNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getMCOProductAffectedItemsAddedJson(List<PLMMCOProductAffectedItem> productAffectedItems) {
        List<ASNewNCRProblemItem> asNewProductAffectedItems = new ArrayList<>();
        for (PLMMCOProductAffectedItem productAffectedItem : productAffectedItems) {
            MESMBOMRevision mbomRevision = mbomRevisionRepository.findOne(productAffectedItem.getItem());
            MESObject item = mesObjectRepository.findOne(mbomRevision.getMaster());

            ASNewNCRProblemItem asNewProductAffectedItem = new ASNewNCRProblemItem(productAffectedItem.getItem(), item.getName(), item.getNumber(), mbomRevision.getRevision());
            asNewProductAffectedItems.add(asNewProductAffectedItem);
        }
        String json = null;
        try {
            json = objectMapper.writeValueAsString(asNewProductAffectedItems);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getMCOProductAffectedItemsAddedString(String messageString, PLMMCO mco, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), mco.getMcoNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.change.mco.productaAffectedItems.add.item");

        String json = as.getData();
        try {
            List<ASNewNCRProblemItem> productAffectedItems = objectMapper.readValue(json, new TypeReference<List<ASNewNCRProblemItem>>() {
            });
            productAffectedItems.forEach(productAffectedItem -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(productAffectedItem.getPartName()), highlightValue(productAffectedItem.getPartNumber()),
                        highlightValue(productAffectedItem.getPartType())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getMCOProductAffectedItemsDeletedJson(PLMMCOProductAffectedItem productAffectedItem) {
        String json = null;
        MESMBOMRevision mbomRevision = mbomRevisionRepository.findOne(productAffectedItem.getItem());
        MESObject item = mesObjectRepository.findOne(mbomRevision.getMaster());
        ASNewNCRProblemItem asNewProductAffectedItem = new ASNewNCRProblemItem(productAffectedItem.getItem(), item.getName(), item.getNumber(), mbomRevision.getRevision());

        List<ASNewNCRProblemItem> asNewProductAffectedItems = new ArrayList<>();
        asNewProductAffectedItems.add(asNewProductAffectedItem);
        try {
            json = objectMapper.writeValueAsString(asNewProductAffectedItems);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getMCOProductAffectedItemsDeletedString(String messageString, PLMMCO mco, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewNCRProblemItem> asNewProductAffectedItems = objectMapper.readValue(json, new TypeReference<List<ASNewNCRProblemItem>>() {
            });
            ASNewNCRProblemItem asNewProductAffectedItem = asNewProductAffectedItems.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(asNewProductAffectedItem.getPartName()), highlightValue(asNewProductAffectedItem.getPartNumber()), highlightValue(asNewProductAffectedItem.getPartType()), mco.getMcoNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getProductAffectedItemsUpdatedString(String messageString, PLMMCO mco, ActivityStream as) {
        StringBuffer sb = new StringBuffer();

        String updateString = activityStreamResourceBundle.getString("plm.change.mco.productaAffectedItems.update.property");

        String json = as.getData();
        try {
            List<ASProblemItemUpdate> propChanges = objectMapper.readValue(json, new TypeReference<List<ASProblemItemUpdate>>() {
            });
            String activityString = MessageFormat.format(messageString, as.getActor().getFullName(), highlightValue(propChanges.get(0).getItemNumber()),
                    mco.getMcoNumber());
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

}
