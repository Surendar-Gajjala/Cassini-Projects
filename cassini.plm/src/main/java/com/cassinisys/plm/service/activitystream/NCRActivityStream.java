package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.NCREvents;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.repo.pqm.NCRFileRepository;
import com.cassinisys.plm.repo.pqm.NCRRepository;
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
public class NCRActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PLMWorkflowRepository workFlowRepository;
    @Autowired
    private PLMWorkflowStatusRepository workflowStatusRepository;
    @Autowired
    private NCRRepository ncrRepository;
    @Autowired
    private NCRFileRepository ncrFileRepository;
    @Autowired
    private QualityTypeAttributeRepository qualityTypeAttributeRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository workflowDefinitionRepository;
    @Autowired
    private CommonActivityStream commonActivityStream;

    @Async
    @EventListener
    public void ncrCreated(NCREvents.NCRCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNcr().getId());
        object.setType("ncr");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.ncr.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void problemReportBasicInfoUpdated(NCREvents.NCRBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PQMNCR oldNcr = event.getOldNcr();
        PQMNCR ncr = event.getNcr();

        object.setObject(ncr.getId());
        object.setType("ncr");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.ncr.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getNcrBasicInfoUpdatedJson(oldNcr, ncr));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void ncrAttributesUpdated(NCREvents.NCRAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PQMNCR ncr = event.getNcr();

        PQMNCRAttribute oldAtt = event.getOldAttribute();
        PQMNCRAttribute newAtt = event.getNewAttribute();

        object.setObject(ncr.getId());
        object.setType("ncr");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.ncr.update.attributes");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getNCRAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void ncrFilesAdded(NCREvents.NCRFilesAddedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.ncr.files.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNcr().getId());
        object.setType("ncr");
        as.setObject(object);
        as.setData(getNCRFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void ncrFoldersAdded(NCREvents.NCRFoldersAddedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.ncr.files.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNcr().getId());
        object.setType("ncr");
        as.setObject(object);
        as.setData(getNCRFoldersAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void ncrFoldersDeleted(NCREvents.NCRFoldersDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.ncr.files.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNcr().getId());
        object.setType("ncr");
        as.setObject(object);
        as.setData(getNCRFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void ncrFileDeleted(NCREvents.NCRFileDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.ncr.files.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNcr().getId());
        object.setType("ncr");
        as.setObject(object);
        as.setData(getNCRFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void ncrFilesVersioned(NCREvents.NCRFilesVersionedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.ncr.files.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNcr().getId());
        object.setType("ncr");
        as.setObject(object);
        as.setData(getNCRFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void ncrFileRenamed(NCREvents.NCRFileRenamedEvent event) {
        PLMFile oldFile = event.getOldFile();
        PLMFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals("Rename")) as.setActivity("plm.quality.ncr.files.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.quality.ncr.files.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNcr().getId());
        object.setType("ncr");
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
    public void ncrFileLocked(NCREvents.NCRFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.ncr.files.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNcr().getId());
        object.setType("ncr");
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
    public void ncrFileUnlocked(NCREvents.NCRFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.ncr.files.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNcr().getId());
        object.setType("ncr");
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
    public void ncrFileDownloaded(NCREvents.NCRFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.ncr.files.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNcr().getId());
        object.setType("ncr");
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
    public void ncrWorkflowStarted(NCREvents.NCRWorkflowStartedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.ncr.workflow.start");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNcr().getId());
        object.setType("ncr");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void ncrWorkflowFinished(NCREvents.NCRWorkflowFinishedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.ncr.workflow.finish");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNcr().getId());
        object.setType("ncr");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void ncrWorkflowPromoted(NCREvents.NCRWorkflowPromotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.ncr.workflow.promote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNcr().getId());
        object.setType("ncr");
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
    public void ncrWorkflowDemoted(NCREvents.NCRWorkflowDemotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.ncr.workflow.demote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNcr().getId());
        object.setType("ncr");
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
    public void ncrWorkflowHold(NCREvents.NCRWorkflowHoldEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.ncr.workflow.hold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNcr().getId());
        object.setType("ncr");
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
    public void ncrWorkflowUnhold(NCREvents.NCRWorkflowUnholdEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.ncr.workflow.unhold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNcr().getId());
        object.setType("ncr");
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
    public void ncrWorkflowChange(NCREvents.NCRWorkflowChangeEvent event) {
        ActivityStream as = new ActivityStream();
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNcr().getId());
        object.setType("ncr");
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
            as.setActivity("plm.quality.ncr.workflow.change");
        } else {
            as.setActivity("plm.quality.ncr.workflow.add");
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
    public void ncrCommentAdded(NCREvents.NCRCommentAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.ncr.comment");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNcr().getId());
        object.setType("ncr");
        as.setObject(object);
        as.setData(event.getComment().getComment());

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void prProblemItemsAdded(NCREvents.NCRProblemItemAddedEvent event) {
        List<PQMNCRProblemItem> asNewNCRProblemItems = event.getProblemItems();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.ncr.problemItems.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNcr().getId());
        object.setType("ncr");
        as.setObject(object);
        as.setData(getNCRProblemItemsAddedJson(asNewNCRProblemItems));

        activityStreamService.create(as);
    }


    @Async
    @EventListener
    public void ncrProblemItemUpdated(NCREvents.NCRProblemItemUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        object.setObject(event.getNcr().getId());
        object.setType("ncr");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.ncr.problemItems.update");
        as.setObject(object);
        as.setConverter(getConverterKey());

        List<ASProblemItemUpdate> changes = new ArrayList<>();
        Integer oldQty = event.getOldProblemItem().getReceivedQty();
        Integer newQty = event.getProblemItem().getReceivedQty();

        if (!oldQty.equals(newQty)) {
            changes.add(new ASProblemItemUpdate(event.getProblemItem().getMaterial().getPartNumber(), "Received Qty", oldQty.toString(), newQty.toString()));
        }

        oldQty = event.getOldProblemItem().getInspectedQty();
        newQty = event.getProblemItem().getInspectedQty();

        if (!oldQty.equals(newQty)) {
            changes.add(new ASProblemItemUpdate(event.getProblemItem().getMaterial().getPartNumber(), "Inspected Qty", oldQty.toString(), newQty.toString()));
        }

        oldQty = event.getOldProblemItem().getDefectiveQty();
        newQty = event.getProblemItem().getDefectiveQty();

        if (!oldQty.equals(newQty)) {
            changes.add(new ASProblemItemUpdate(event.getProblemItem().getMaterial().getPartNumber(), "Defective Qty", oldQty.toString(), newQty.toString()));
        }

        String oldValue = event.getOldProblemItem().getNotes();
        String newValue = event.getProblemItem().getNotes();
        if (oldValue == null) {
            oldValue = "";
        }

        if (newValue == null) {
            newValue = "";
        }

        if (!oldValue.equals(newValue)) {
            changes.add(new ASProblemItemUpdate(event.getProblemItem().getMaterial().getPartNumber(), "Notes", oldValue, newValue));
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
    public void ncrRelatedItemUpdated(NCREvents.NCRRelatedItemUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        object.setObject(event.getNcr().getId());
        object.setType("ncr");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.ncr.relatedItems.update");
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
            changes.add(new ASProblemItemUpdate(event.getRelatedItem().getMaterial().getPartNumber(), "Notes", oldValue, newValue));
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
    public void prProblemItemsDeleted(NCREvents.NCRProblemItemDeletedEvent event) {
        PQMNCRProblemItem asNewNCRProblemItem = event.getProblemItem();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.ncr.problemItems.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNcr().getId());
        object.setType("ncr");
        as.setObject(object);
        as.setData(getNCRProblemItemsDeletedJson(asNewNCRProblemItem));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void ncrRelatedItemsAdded(NCREvents.NCRRelatedItemAddedEvent event) {
        List<PQMNCRRelatedItem> relatedItems = event.getRelatedItems();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.ncr.relatedItems.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNcr().getId());
        object.setType("ncr");
        as.setObject(object);
        as.setData(getNCRRelatedItemsAddedJson(relatedItems));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void ncrRelatedItemsDeleted(NCREvents.NCRRelatedItemDeletedEvent event) {
        PQMNCRRelatedItem relatedItem = event.getRelatedItem();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.ncr.relatedItems.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNcr().getId());
        object.setType("ncr");
        as.setObject(object);
        as.setData(getNCRRelatedItemsDeletedJson(relatedItem));

        activityStreamService.create(as);
    }

    @Override
    public String getConverterKey() {
        return "plm.quality.ncr";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            PQMNCR ncr = ncrRepository.findOne(object.getObject());

            if (ncr == null) return "";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.quality.ncr.create":
                    convertedString = getNCRCreatedString(messageString, actor, ncr);
                    break;
                case "plm.quality.ncr.revision":
                    convertedString = getNCRRevisionCreatedString(messageString, actor, ncr);
                    break;
                case "plm.quality.ncr.update.basicinfo":
                    convertedString = getNCRBasicInfoUpdatedString(messageString, actor, ncr, as);
                    break;
                case "plm.quality.ncr.update.attributes":
                    convertedString = getNCRAttributeUpdatedString(messageString, actor, ncr, as);
                    break;
                case "plm.quality.ncr.files.add":
                    convertedString = getNCRFilesAddedString(messageString, ncr, as);
                    break;
                case "plm.quality.ncr.files.delete":
                    convertedString = getNCRFileDeletedString(messageString, ncr, as);
                    break;
                case "plm.quality.ncr.files.folders.add":
                    convertedString = getNCRFoldersAddedString(messageString, ncr, as);
                    break;
                case "plm.quality.ncr.files.folders.delete":
                    convertedString = getNCRFoldersDeletedString(messageString, ncr, as);
                    break;
                case "plm.quality.ncr.files.version":
                    convertedString = getNCRFilesVersionedString(messageString, ncr, as);
                    break;
                case "plm.quality.ncr.files.rename":
                    convertedString = getNCRFileRenamedString(messageString, ncr, as);
                    break;
                case "plm.quality.ncr.files.replace":
                    convertedString = getNCRFileRenamedString(messageString, ncr, as);
                    break;
                case "plm.quality.ncr.files.lock":
                    convertedString = getNCRFileString(messageString, ncr, as);
                    break;
                case "plm.quality.ncr.files.unlock":
                    convertedString = getNCRFileString(messageString, ncr, as);
                    break;
                case "plm.quality.ncr.files.download":
                    convertedString = getNCRFileString(messageString, ncr, as);
                    break;
                case "plm.quality.ncr.workflow.start":
                    convertedString = getNCRWorkflowStartString(messageString, ncr, as);
                    break;
                case "plm.quality.ncr.workflow.finish":
                    convertedString = getNCRWorkflowFinishString(messageString, ncr, as);
                    break;
                case "plm.quality.ncr.workflow.promote":
                    convertedString = getNCRWorkflowPromoteDemoteString(messageString, ncr, as);
                    break;
                case "plm.quality.ncr.workflow.demote":
                    convertedString = getNCRWorkflowPromoteDemoteString(messageString, ncr, as);
                    break;
                case "plm.quality.ncr.workflow.hold":
                    convertedString = getNCRWorkflowHoldUnholdString(messageString, ncr, as);
                    break;
                case "plm.quality.ncr.workflow.unhold":
                    convertedString = getNCRWorkflowHoldUnholdString(messageString, ncr, as);
                    break;
                case "plm.quality.ncr.workflow.add":
                    convertedString = getNCRWorkflowAddedString(messageString, ncr, as);
                    break;
                case "plm.quality.ncr.workflow.change":
                    convertedString = getNCRWorkflowChangeString(messageString, ncr, as);
                    break;
                case "plm.quality.ncr.comment":
                    convertedString = getNCRCommentString(messageString, ncr, as);
                    break;
                case "plm.quality.ncr.problemItems.add":
                    convertedString = getNCRProblemItemsAddedString(messageString, ncr, as);
                    break;
                case "plm.quality.ncr.problemItems.update":
                    convertedString = getProblemItemUpdatedString(messageString, ncr, as);
                    break;
                case "plm.quality.ncr.problemItems.delete":
                    convertedString = getNCRProblemItemsDeletedString(messageString, ncr, as);
                    break;
                case "plm.quality.ncr.relatedItems.add":
                    convertedString = getNCRRelatedItemsAddedString(messageString, ncr, as);
                    break;
                case "plm.quality.ncr.relatedItems.update":
                    convertedString = getRelatedItemUpdatedString(messageString, ncr, as);
                    break;
                case "plm.quality.ncr.relatedItems.delete":
                    convertedString = getNCRRelatedItemsDeletedString(messageString, ncr, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getNCRCreatedString(String messageString, Person actor, PQMNCR ncr) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), ncr.getNcrNumber());
    }

    private String getNCRRevisionCreatedString(String messageString, Person actor, PQMNCR ncr) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), ncr.getNcrNumber());
    }

    private String getNCRBasicInfoUpdatedString(String messageString, Person actor, PQMNCR ncr, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), ncr.getNcrNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.quality.ncr.update.basicinfo.property");

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

    private String getNcrBasicInfoUpdatedJson(PQMNCR oldNcr, PQMNCR ncr) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldNcr.getTitle();
        String newValue = ncr.getTitle();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Title", oldValue, newValue));
        }

        oldValue = oldNcr.getDescription();
        newValue = ncr.getDescription();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue, newValue));
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

    private String getNCRAttributesUpdatedJson(PQMNCRAttribute oldAttribute, PQMNCRAttribute newAttribute) {
        PQMQualityTypeAttribute attDef = qualityTypeAttributeRepository.findOne(newAttribute.getId().getAttributeDef());
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

    private String getNCRAttributeUpdatedString(String messageString, Person actor, PQMNCR ncr, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), ncr.getNcrNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.quality.ncr.update.attributes.attribute");
        String propertyUpdateString = activityStreamResourceBundle.getString("plm.quality.ncr.update.attributes.attribute").substring(0, 21);

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

    private String getNCRFilesAddedJson(List<PLMFile> files) {
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

    private String getNCRFoldersAddedJson(PLMFile file) {
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

    private String getNCRFoldersDeletedJson(PLMFile file) {
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

    private String getNCRFilesAddedString(String messageString, PQMNCR ncr, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), ncr.getNcrNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.quality.ncr.files.add.file");

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

    private String getNCRFoldersAddedString(String messageString, PQMNCR ncr, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), ncr.getNcrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getNCRFoldersDeletedString(String messageString, PQMNCR ncr, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), ncr.getNcrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getNCRFileDeletedString(String messageString, PQMNCR ncr, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), ncr.getNcrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getNCRFilesVersionedJson(List<PLMFile> files) {
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

    private String getNCRFilesVersionedString(String messageString, PQMNCR ncr, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), ncr.getNcrNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.quality.ncr.files.version.file");

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

    private String getNCRFileRenamedString(String messageString, PQMNCR ncr, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    ncr.getNcrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getNCRFileString(String messageString, PQMNCR ncr, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    ncr.getNcrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getNCRWorkflowStartString(String messageString, PQMNCR ncr, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                ncr.getNcrNumber());
    }

    private String getNCRWorkflowFinishString(String messageString, PQMNCR ncr, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                ncr.getNcrNumber());
    }

    private String getNCRWorkflowPromoteDemoteString(String messageString, PQMNCR ncr, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();
        ActivityStreamObject target = as.getTarget();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus fromActivity = workflowStatusRepository.findOne(source.getObject());
        PLMWorkflowStatus toActivity = workflowStatusRepository.findOne(target.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                ncr.getNcrNumber(),
                highlightValue(fromActivity.getName()),
                highlightValue(toActivity.getName()));
    }

    private String getNCRWorkflowHoldUnholdString(String messageString, PQMNCR ncr, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus plmWorkflowActivity = workflowStatusRepository.findOne(source.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflowActivity.getName()),
                highlightValue(plmWorkflow.getName()),
                ncr.getNcrNumber());
    }

    private String getNCRCommentString(String messageString, PQMNCR ncr, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                ncr.getNcrNumber());

        String s = highlightValue("Comment: ");
        s += as.getData();
        s = addMarginToMessage(s);

        message += s;

        return message;
    }

    private String getNCRWorkflowAddedString(String messageString, PQMNCR ncr, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getNewValue()),
                    ncr.getNcrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getNCRWorkflowChangeString(String messageString, PQMNCR ncr, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getOldValue()),
                    highlightValue(workflow.getNewValue()),
                    ncr.getNcrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getNCRProblemItemsAddedJson(List<PQMNCRProblemItem> problemItems) {
        List<ASNewNCRProblemItem> asNewNCRProblemItems = new ArrayList<>();
        for (PQMNCRProblemItem problemItem : problemItems) {
            ASNewNCRProblemItem asNewNCRProblemItem = new ASNewNCRProblemItem(problemItem.getMaterial().getId(), problemItem.getMaterial().getPartName(), problemItem.getMaterial().getPartNumber(), problemItem.getMaterial().getMfrPartType().getName());
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

    private String getNCRProblemItemsDeletedJson(PQMNCRProblemItem problemItem) {
        String json = null;
        ASNewNCRProblemItem asNewNCRProblemItem = new ASNewNCRProblemItem(problemItem.getMaterial().getId(), problemItem.getMaterial().getPartName(), problemItem.getMaterial().getPartNumber(), problemItem.getMaterial().getMfrPartType().getName());

        List<ASNewNCRProblemItem> asNewNCRProblemItems = new ArrayList<>();
        asNewNCRProblemItems.add(asNewNCRProblemItem);
        try {
            json = objectMapper.writeValueAsString(asNewNCRProblemItems);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getNCRProblemItemsAddedString(String messageString, PQMNCR ncr, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), ncr.getNcrNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.quality.ncr.problemItems.add.item");

        String json = as.getData();
        try {
            List<ASNewNCRProblemItem> files = objectMapper.readValue(json, new TypeReference<List<ASNewNCRProblemItem>>() {
            });
            files.forEach(f -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(f.getPartName()), highlightValue(f.getPartNumber()), highlightValue(f.getPartType())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getProblemItemUpdatedString(String messageString, PQMNCR pqmncr, ActivityStream as) {
        StringBuffer sb = new StringBuffer();

        String updateString = activityStreamResourceBundle.getString("plm.quality.ncr.problemItems.update.property");

        String json = as.getData();
        try {
            List<ASProblemItemUpdate> propChanges = objectMapper.readValue(json, new TypeReference<List<ASProblemItemUpdate>>() {
            });
            String activityString = MessageFormat.format(messageString, as.getActor().getFullName(), highlightValue(propChanges.get(0).getItemNumber()),
                    pqmncr.getNcrNumber());
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

    private String getRelatedItemUpdatedString(String messageString, PQMNCR pqmncr, ActivityStream as) {
        StringBuffer sb = new StringBuffer();

        String updateString = activityStreamResourceBundle.getString("plm.quality.ncr.relatedItems.update.property");

        String json = as.getData();
        try {
            List<ASProblemItemUpdate> propChanges = objectMapper.readValue(json, new TypeReference<List<ASProblemItemUpdate>>() {
            });
            String activityString = MessageFormat.format(messageString, as.getActor().getFullName(), highlightValue(propChanges.get(0).getItemNumber()),
                    pqmncr.getNcrNumber());
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


    private String getNCRProblemItemsDeletedString(String messageString, PQMNCR ncr, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewNCRProblemItem> asNewNCRProblemItems = objectMapper.readValue(json, new TypeReference<List<ASNewNCRProblemItem>>() {
            });
            ASNewNCRProblemItem asNewNCRProblemItem = asNewNCRProblemItems.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(asNewNCRProblemItem.getPartName()), highlightValue(asNewNCRProblemItem.getPartNumber()), highlightValue(asNewNCRProblemItem.getPartType()), ncr.getNcrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getNCRRelatedItemsAddedJson(List<PQMNCRRelatedItem> relatedItems) {
        List<ASNewNCRProblemItem> asNewNCRProblemItems = new ArrayList<>();
        for (PQMNCRRelatedItem relatedItem : relatedItems) {
            ASNewNCRProblemItem asNewNCRProblemItem = new ASNewNCRProblemItem(relatedItem.getMaterial().getId(), relatedItem.getMaterial().getPartName(),
                    relatedItem.getMaterial().getPartNumber(), relatedItem.getMaterial().getMfrPartType().getName());
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

    private String getNCRRelatedItemsDeletedJson(PQMNCRRelatedItem relatedItem) {
        String json = null;
        ASNewNCRProblemItem asNewNCRProblemItem = new ASNewNCRProblemItem(relatedItem.getMaterial().getId(), relatedItem.getMaterial().getPartName(), relatedItem.getMaterial().getPartNumber(), relatedItem.getMaterial().getMfrPartType().getName());

        List<ASNewNCRProblemItem> asNewNCRProblemItems = new ArrayList<>();
        asNewNCRProblemItems.add(asNewNCRProblemItem);
        try {
            json = objectMapper.writeValueAsString(asNewNCRProblemItems);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getNCRRelatedItemsAddedString(String messageString, PQMNCR ncr, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), ncr.getNcrNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.quality.ncr.relatedItems.add.item");

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

    private String getNCRRelatedItemsDeletedString(String messageString, PQMNCR ncr, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewNCRProblemItem> asNewNCRProblemItems = objectMapper.readValue(json, new TypeReference<List<ASNewNCRProblemItem>>() {
            });
            ASNewNCRProblemItem asNewNCRProblemItem = asNewNCRProblemItems.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(asNewNCRProblemItem.getPartName()), highlightValue(asNewNCRProblemItem.getPartNumber()), highlightValue(asNewNCRProblemItem.getPartType()), ncr.getNcrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }
}
