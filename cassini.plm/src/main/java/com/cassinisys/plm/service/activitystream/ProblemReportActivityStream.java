package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.ProblemReportEvents;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.repo.plm.ItemRevisionRepository;
import com.cassinisys.plm.repo.pqm.ProblemReportRepository;
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
public class ProblemReportActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PLMWorkflowRepository workFlowRepository;
    @Autowired
    private PLMWorkflowStatusRepository workflowStatusRepository;
    @Autowired
    private ProblemReportRepository problemReportRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository workflowDefinitionRepository;
    @Autowired
    private QualityTypeAttributeRepository qualityTypeAttributeRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CommonActivityStream commonActivityStream;
    @Autowired
    private PersonRepository personRepository;

    @Async
    @EventListener
    public void problemReportCreated(ProblemReportEvents.ProblemReportCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProblemReport().getId());
        object.setType("problemReport");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.problemReport.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void problemReportImplemented(ProblemReportEvents.ProblemReportImplementedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProblemReport().getId());
        object.setType("problemReport");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.problemReport.implemented");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void problemReportBasicInfoUpdated(ProblemReportEvents.ProblemReportBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PQMProblemReport oldProblemReport = event.getOldProblemReport();
        PQMProblemReport problemReport = event.getProblemReport();

        object.setObject(problemReport.getId());
        object.setType("problemReport");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.problemReport.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getProblemReportBasicInfoUpdatedJson(oldProblemReport, problemReport));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void problemReportAttributesUpdated(ProblemReportEvents.ProblemReportAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PQMProblemReport problemReport = event.getProblemReport();

        PQMProblemReportAttribute oldAtt = event.getOldAttribute();
        PQMProblemReportAttribute newAtt = event.getNewAttribute();

        object.setObject(problemReport.getId());
        object.setType("problemReport");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.problemReport.update.attributes");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getProblemReportAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void problemReportFilesAdded(ProblemReportEvents.ProblemReportFilesAddedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.problemReport.files.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProblemReport().getId());
        object.setType("problemReport");
        as.setObject(object);
        as.setData(getProblemReportFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void problemReportFoldersAdded(ProblemReportEvents.ProblemReportFoldersAddedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.problemReport.files.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProblemReport().getId());
        object.setType("problemReport");
        as.setObject(object);
        as.setData(getProblemReportFoldersAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void problemReportFoldersDeleted(ProblemReportEvents.ProblemReportFoldersDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.problemReport.files.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProblemReport().getId());
        object.setType("problemReport");
        as.setObject(object);
        as.setData(getProblemReportFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void problemReportFileDeleted(ProblemReportEvents.ProblemReportFileDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.problemReport.files.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProblemReport().getId());
        object.setType("problemReport");
        as.setObject(object);
        as.setData(getProblemReportFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void problemReportFilesVersioned(ProblemReportEvents.ProblemReportFilesVersionedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.problemReport.files.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProblemReport().getId());
        object.setType("problemReport");
        as.setObject(object);
        as.setData(getProblemReportFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void prProblemItemsAdded(ProblemReportEvents.ProblemReportProblemItemAddedEvent event) {
        List<PQMPRProblemItem> asNewPRProblemItems = event.getProblemItems();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.problemReport.problemItems.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProblemReport().getId());
        object.setType("problemReport");
        as.setObject(object);
        as.setData(getPRProblemItemsAddedJson(asNewPRProblemItems));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void prProblemItemsDeleted(ProblemReportEvents.ProblemReportProblemItemDeletedEvent event) {
        PQMPRProblemItem asNewPRProblemItem = event.getProblemItem();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.problemReport.problemItems.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProblemReport().getId());
        object.setType("problemReport");
        as.setObject(object);
        as.setData(getPRProblemItemsDeletedJson(asNewPRProblemItem));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void prRelatedItemsAdded(ProblemReportEvents.ProblemReportRelatedItemAddedEvent event) {
        List<PQMPRRelatedItem> relatedItems = event.getRelatedItems();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.problemReport.relatedItems.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProblemReport().getId());
        object.setType("problemReport");
        as.setObject(object);
        as.setData(getPRRelatedItemsAddedJson(relatedItems));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void prRelatedItemsDeleted(ProblemReportEvents.ProblemReportRelatedItemDeletedEvent event) {
        PQMPRRelatedItem relatedItem = event.getRelatedItem();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.problemReport.relatedItems.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProblemReport().getId());
        object.setType("problemReport");
        as.setObject(object);
        as.setData(getPRRelatedItemsDeletedJson(relatedItem));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void problemReportFileRenamed(ProblemReportEvents.ProblemReportFileRenamedEvent event) {
        PLMFile oldFile = event.getOldFile();
        PLMFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals("Rename")) as.setActivity("plm.quality.problemReport.files.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.quality.problemReport.files.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProblemReport().getId());
        object.setType("problemReport");
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
    public void problemReportFileLocked(ProblemReportEvents.ProblemReportFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.problemReport.files.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProblemReport().getId());
        object.setType("problemReport");
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
    public void problemReportFileUnlocked(ProblemReportEvents.ProblemReportFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.problemReport.files.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProblemReport().getId());
        object.setType("problemReport");
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
    public void problemReportFileDownloaded(ProblemReportEvents.ProblemReportFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.problemReport.files.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProblemReport().getId());
        object.setType("problemReport");
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
    public void problemReportWorkflowStarted(ProblemReportEvents.ProblemReportWorkflowStartedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.problemReport.workflow.start");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProblemReport().getId());
        object.setType("problemReport");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void problemReportWorkflowFinished(ProblemReportEvents.ProblemReportWorkflowFinishedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.problemReport.workflow.finish");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProblemReport().getId());
        object.setType("problemReport");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void problemReportWorkflowPromoted(ProblemReportEvents.ProblemReportWorkflowPromotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.problemReport.workflow.promote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProblemReport().getId());
        object.setType("problemReport");
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
    public void problemReportWorkflowDemoted(ProblemReportEvents.ProblemReportWorkflowDemotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.problemReport.workflow.demote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProblemReport().getId());
        object.setType("problemReport");
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
    public void problemReportWorkflowHold(ProblemReportEvents.ProblemReportWorkflowHoldEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.problemReport.workflow.hold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProblemReport().getId());
        object.setType("problemReport");
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
    public void problemReportWorkflowUnhold(ProblemReportEvents.ProblemReportWorkflowUnholdEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.problemReport.workflow.unhold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProblemReport().getId());
        object.setType("problemReport");
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
    public void problemReportWorkflowChange(ProblemReportEvents.ProblemReportWorkflowChangeEvent event) {
        ActivityStream as = new ActivityStream();
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProblemReport().getId());
        object.setType("problemReport");
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
            as.setActivity("plm.quality.problemReport.workflow.change");
        } else {
            as.setActivity("plm.quality.problemReport.workflow.add");
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
    public void problemReportCommentAdded(ProblemReportEvents.ProblemReportCommentAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.problemReport.comment");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProblemReport().getId());
        object.setType("problemReport");
        as.setObject(object);
        as.setData(event.getComment().getComment());

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void inspectionBasicInfoUpdated(ProblemReportEvents.ProblemReportProblemItemUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        object.setObject(event.getProblemReport().getId());
        object.setType("problemReport");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.problemReport.problemItems.update");
        as.setObject(object);
        as.setConverter(getConverterKey());
        String oldValue = event.getOldProblemItem().getNotes();
        String newValue = event.getProblemItem().getNotes();
        if (oldValue == null) {
            oldValue = "";
        }

        if (newValue == null) {
            newValue = "";
        }
        List<ASProblemItemUpdate> changes = new ArrayList<>();
        if (!oldValue.equals(newValue)) {
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(event.getProblemItem().getItem());
            PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
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

    @Override
    public String getConverterKey() {
        return "plm.quality.problemReport";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            PQMProblemReport problemReport = problemReportRepository.findOne(object.getObject());

            if (problemReport == null) return "";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.quality.problemReport.create":
                    convertedString = getProblemReportCreatedString(messageString, actor, problemReport);
                    break;
                case "plm.quality.problemReport.implemented":
                    convertedString = getProblemReportCreatedString(messageString, actor, problemReport);
                    break;
                case "plm.quality.problemReport.revision":
                    convertedString = getProblemReportRevisionCreatedString(messageString, actor, problemReport);
                    break;
                case "plm.quality.problemReport.update.basicinfo":
                    convertedString = getProblemReportBasicInfoUpdatedString(messageString, actor, problemReport, as);
                    break;
                case "plm.quality.problemReport.update.attributes":
                    convertedString = getProblemReportAttributeUpdatedString(messageString, actor, problemReport, as);
                    break;
                case "plm.quality.problemReport.files.add":
                    convertedString = getProblemReportFilesAddedString(messageString, problemReport, as);
                    break;
                case "plm.quality.problemReport.files.delete":
                    convertedString = getProblemReportFileDeletedString(messageString, problemReport, as);
                    break;
                case "plm.quality.problemReport.files.folders.add":
                    convertedString = getProblemReportFoldersAddedString(messageString, problemReport, as);
                    break;
                case "plm.quality.problemReport.files.folders.delete":
                    convertedString = getProblemReportFoldersDeletedString(messageString, problemReport, as);
                    break;
                case "plm.quality.problemReport.files.version":
                    convertedString = getProblemReportFilesVersionedString(messageString, problemReport, as);
                    break;
                case "plm.quality.problemReport.files.rename":
                    convertedString = getProblemReportFileRenamedString(messageString, problemReport, as);
                    break;
                case "plm.quality.problemReport.files.replace":
                    convertedString = getProblemReportFileRenamedString(messageString, problemReport, as);
                    break;
                case "plm.quality.problemReport.files.lock":
                    convertedString = getProblemReportFileString(messageString, problemReport, as);
                    break;
                case "plm.quality.problemReport.files.unlock":
                    convertedString = getProblemReportFileString(messageString, problemReport, as);
                    break;
                case "plm.quality.problemReport.files.download":
                    convertedString = getProblemReportFileString(messageString, problemReport, as);
                    break;
                case "plm.quality.problemReport.workflow.start":
                    convertedString = getProblemReportWorkflowStartString(messageString, problemReport, as);
                    break;
                case "plm.quality.problemReport.workflow.finish":
                    convertedString = getProblemReportWorkflowFinishString(messageString, problemReport, as);
                    break;
                case "plm.quality.problemReport.workflow.promote":
                    convertedString = getProblemReportWorkflowPromoteDemoteString(messageString, problemReport, as);
                    break;
                case "plm.quality.problemReport.workflow.demote":
                    convertedString = getProblemReportWorkflowPromoteDemoteString(messageString, problemReport, as);
                    break;
                case "plm.quality.problemReport.workflow.hold":
                    convertedString = getProblemReportWorkflowHoldUnholdString(messageString, problemReport, as);
                    break;
                case "plm.quality.problemReport.workflow.unhold":
                    convertedString = getProblemReportWorkflowHoldUnholdString(messageString, problemReport, as);
                    break;
                case "plm.quality.problemReport.workflow.add":
                    convertedString = getProblemReportWorkflowAddedString(messageString, problemReport, as);
                    break;
                case "plm.quality.problemReport.workflow.change":
                    convertedString = getProblemReportWorkflowChangeString(messageString, problemReport, as);
                    break;
                case "plm.quality.problemReport.comment":
                    convertedString = getProblemReportCommentString(messageString, problemReport, as);
                    break;
                case "plm.quality.problemReport.problemItems.add":
                    convertedString = getPRProblemItemsAddedString(messageString, problemReport, as);
                    break;
                case "plm.quality.problemReport.problemItems.update":
                    convertedString = getProblemItemUpdatedString(messageString, problemReport, as);
                    break;
                case "plm.quality.problemReport.problemItems.delete":
                    convertedString = getPRProblemItemsDeletedString(messageString, problemReport, as);
                    break;
                case "plm.quality.problemReport.relatedItems.add":
                    convertedString = getPRRelatedItemsAddedString(messageString, problemReport, as);
                    break;
                case "plm.quality.problemReport.relatedItems.delete":
                    convertedString = getPRRelatedItemsDeletedString(messageString, problemReport, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getProblemReportCreatedString(String messageString, Person actor, PQMProblemReport problemReport) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), problemReport.getPrNumber());
    }

    private String getProblemReportRevisionCreatedString(String messageString, Person actor, PQMProblemReport problemReport) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), problemReport.getPrNumber());
    }

    private String getProblemReportBasicInfoUpdatedString(String messageString, Person actor, PQMProblemReport problemReport, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), problemReport.getPrNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.quality.problemReport.update.basicinfo.property");

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

    private String getProblemReportBasicInfoUpdatedJson(PQMProblemReport oldProblemReport, PQMProblemReport problemReport) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldProblemReport.getProblem();
        String newValue = problemReport.getProblem();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Problem", oldValue, newValue));
        }

        oldValue = oldProblemReport.getDescription();
        newValue = problemReport.getDescription();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue, newValue));
        }

        oldValue = oldProblemReport.getStepsToReproduce();
        newValue = problemReport.getStepsToReproduce();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Steps To Reproduce", oldValue, newValue));
        }

        Integer oldValue1 = oldProblemReport.getQualityAnalyst();
        Integer newValue1 = problemReport.getQualityAnalyst();

        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Quality Analyst", personRepository.findOne(oldValue1).getFullName(), personRepository.findOne(newValue1).getFullName()));
        }

        oldValue = oldProblemReport.getFailureType();
        newValue = problemReport.getFailureType();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Defect Type", oldValue, newValue));
        }

        oldValue = oldProblemReport.getSeverity();
        newValue = problemReport.getSeverity();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Severity", oldValue, newValue));
        }

        oldValue = oldProblemReport.getDisposition();
        newValue = problemReport.getDisposition();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Disposition", oldValue, newValue));
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


    private String getProblemReportAttributesUpdatedJson(PQMProblemReportAttribute oldAttribute, PQMProblemReportAttribute newAttribute) {
        PQMQualityTypeAttribute attDef = qualityTypeAttributeRepository.findOne(newAttribute.getId().getAttributeDef());
        List<ASAttributeChangeDTO> changes =commonActivityStream.getAttributeUpdateJsonData(oldAttribute,newAttribute,attDef);

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

    private String getProblemReportAttributeUpdatedString(String messageString, Person actor, PQMProblemReport problemReport, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), problemReport.getPrNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.quality.problemReport.update.attributes.attribute");
        String propertyUpdateString = activityStreamResourceBundle.getString("plm.quality.problemReport.update.attributes.attribute").substring(0, 21);

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

    private String getProblemReportFilesAddedJson(List<PLMFile> files) {
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

    private String getProblemReportFoldersAddedJson(PLMFile file) {
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

    private String getProblemReportFoldersDeletedJson(PLMFile file) {
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

    private String getProblemReportFilesAddedString(String messageString, PQMProblemReport problemReport, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), problemReport.getPrNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.quality.problemReport.files.add.file");

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

    private String getProblemReportFoldersAddedString(String messageString, PQMProblemReport problemReport, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), problemReport.getPrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getProblemReportFoldersDeletedString(String messageString, PQMProblemReport problemReport, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), problemReport.getPrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getProblemReportFileDeletedString(String messageString, PQMProblemReport problemReport, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), problemReport.getPrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getProblemReportFilesVersionedJson(List<PLMFile> files) {
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

    private String getProblemReportFilesVersionedString(String messageString, PQMProblemReport problemReport, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), problemReport.getPrNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.quality.problemReport.files.version.file");

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

    private String getProblemReportFileRenamedString(String messageString, PQMProblemReport problemReport, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    problemReport.getPrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getProblemReportFileString(String messageString, PQMProblemReport problemReport, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    problemReport.getPrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getProblemReportWorkflowStartString(String messageString, PQMProblemReport problemReport, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                problemReport.getPrNumber());
    }

    private String getProblemReportWorkflowFinishString(String messageString, PQMProblemReport problemReport, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                problemReport.getPrNumber());
    }

    private String getProblemReportWorkflowPromoteDemoteString(String messageString, PQMProblemReport problemReport, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();
        ActivityStreamObject target = as.getTarget();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus fromActivity = workflowStatusRepository.findOne(source.getObject());
        PLMWorkflowStatus toActivity = workflowStatusRepository.findOne(target.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                problemReport.getPrNumber(),
                highlightValue(fromActivity.getName()),
                highlightValue(toActivity.getName()));
    }

    private String getProblemReportWorkflowHoldUnholdString(String messageString, PQMProblemReport problemReport, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus plmWorkflowActivity = workflowStatusRepository.findOne(source.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflowActivity.getName()),
                highlightValue(plmWorkflow.getName()),
                problemReport.getPrNumber());
    }

    private String getProblemReportWorkflowAddedString(String messageString, PQMProblemReport problemReport, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getNewValue()),
                    problemReport.getPrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getProblemReportWorkflowChangeString(String messageString, PQMProblemReport problemReport, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getOldValue()),
                    highlightValue(workflow.getNewValue()),
                    problemReport.getPrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getProblemReportCommentString(String messageString, PQMProblemReport problemReport, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                problemReport.getPrNumber());

        String s = highlightValue("Comment: ");
        s += as.getData();
        s = addMarginToMessage(s);

        message += s;

        return message;
    }

    private String getPRProblemItemsAddedJson(List<PQMPRProblemItem> problemItems) {
        List<ASNewPRProblemItem> asNewPRProblemItems = new ArrayList<>();
        for (PQMPRProblemItem problemItem : problemItems) {
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

    private String getPRProblemItemsDeletedJson(PQMPRProblemItem problemItem) {
        String json = null;
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(problemItem.getItem());
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

    private String getPRProblemItemsAddedString(String messageString, PQMProblemReport problemReport, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), problemReport.getPrNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.quality.problemReport.problemItems.add.item");

        String json = as.getData();
        try {
            List<ASNewPRProblemItem> files = objectMapper.readValue(json, new TypeReference<List<ASNewPRProblemItem>>() {
            });
            files.forEach(f -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(f.getItemName()), highlightValue(f.getRevision()), highlightValue(f.getLifecyclePhase())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getProblemItemUpdatedString(String messageString, PQMProblemReport problemReport, ActivityStream as) {
        StringBuffer sb = new StringBuffer();

        String updateString = activityStreamResourceBundle.getString("plm.quality.problemReport.problemItems.update.property");

        String json = as.getData();
        try {
            List<ASProblemItemUpdate> propChanges = objectMapper.readValue(json, new TypeReference<List<ASProblemItemUpdate>>() {
            });
            String activityString = MessageFormat.format(messageString, as.getActor().getFullName(), highlightValue(propChanges.get(0).getItemNumber()),
                    problemReport.getPrNumber());
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


    private String getPRProblemItemsDeletedString(String messageString, PQMProblemReport problemReport, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewPRProblemItem> asNewPRProblemItems = objectMapper.readValue(json, new TypeReference<List<ASNewPRProblemItem>>() {
            });
            ASNewPRProblemItem asNewPRProblemItem = asNewPRProblemItems.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(asNewPRProblemItem.getItemName()), highlightValue(asNewPRProblemItem.getRevision()), highlightValue(asNewPRProblemItem.getLifecyclePhase()), problemReport.getPrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getPRRelatedItemsAddedJson(List<PQMPRRelatedItem> relatedItems) {
        List<ASNewPRProblemItem> asNewPRProblemItems = new ArrayList<>();
        for (PQMPRRelatedItem problemItem : relatedItems) {
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

    private String getPRRelatedItemsDeletedJson(PQMPRRelatedItem relatedItem) {
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

    private String getPRRelatedItemsAddedString(String messageString, PQMProblemReport problemReport, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), problemReport.getPrNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.quality.problemReport.relatedItems.add.item");

        String json = as.getData();
        try {
            List<ASNewPRProblemItem> files = objectMapper.readValue(json, new TypeReference<List<ASNewPRProblemItem>>() {
            });
            files.forEach(f -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(f.getItemName()), highlightValue(f.getRevision()), highlightValue(f.getLifecyclePhase())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getPRRelatedItemsDeletedString(String messageString, PQMProblemReport problemReport, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewPRProblemItem> asNewPRProblemItems = objectMapper.readValue(json, new TypeReference<List<ASNewPRProblemItem>>() {
            });
            ASNewPRProblemItem asNewPRProblemItem = asNewPRProblemItems.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(asNewPRProblemItem.getItemName()), highlightValue(asNewPRProblemItem.getRevision()), highlightValue(asNewPRProblemItem.getLifecyclePhase()), problemReport.getPrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }
}
