package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.ECREvents;
import com.cassinisys.plm.model.cm.*;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.cassinisys.plm.model.pqm.PQMProblemReport;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.repo.cm.ChangeTypeAttributeRepository;
import com.cassinisys.plm.repo.cm.ECRRepository;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.repo.plm.ItemRevisionRepository;
import com.cassinisys.plm.repo.pqm.ProblemReportRepository;
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
public class ECRActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PLMWorkflowRepository workFlowRepository;
    @Autowired
    private PLMWorkflowStatusRepository workflowStatusRepository;
    @Autowired
    private ECRRepository ecrRepository;
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
    @Autowired
    private ProblemReportRepository problemReportRepository;

    @Async
    @EventListener
    public void ecrCreated(ECREvents.ECRCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEcr().getId());
        object.setType("ecr");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.ecr.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void problemReportBasicInfoUpdated(ECREvents.ECRBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PLMECR oldEcr = event.getOldEcr();
        PLMECR ecr = event.getEcr();

        object.setObject(ecr.getId());
        object.setType("ecr");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.ecr.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getEcrBasicInfoUpdatedJson(oldEcr, ecr));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void ecrAttributesUpdated(ECREvents.ECRAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PLMECR ecr = event.getEcr();

        PLMChangeAttribute oldAtt = event.getOldAttribute();
        PLMChangeAttribute newAtt = event.getNewAttribute();

        object.setObject(ecr.getId());
        object.setType("ecr");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.ecr.update.attributes");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getECRAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void ecrFilesAdded(ECREvents.ECRFilesAddedEvent event) {
        List<PLMChangeFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.ecr.files.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEcr().getId());
        object.setType("ecr");
        as.setObject(object);
        as.setData(getECRFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void ecrFoldersAdded(ECREvents.ECRFoldersAddedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.ecr.files.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEcr().getId());
        object.setType("ecr");
        as.setObject(object);
        as.setData(getECRFoldersAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void ecrFoldersDeleted(ECREvents.ECRFoldersDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.ecr.files.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEcr().getId());
        object.setType("ecr");
        as.setObject(object);
        as.setData(getECRFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void ecrFileDeleted(ECREvents.ECRFileDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.ecr.files.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEcr().getId());
        object.setType("ecr");
        as.setObject(object);
        as.setData(getECRFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void ecrFilesVersioned(ECREvents.ECRFilesVersionedEvent event) {
        List<PLMChangeFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.ecr.files.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEcr().getId());
        object.setType("ecr");
        as.setObject(object);
        as.setData(getECRFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void ecrFileRenamed(ECREvents.ECRFileRenamedEvent event) {
        PLMFile oldFile = event.getOldFile();
        PLMFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals("Rename")) as.setActivity("plm.change.ecr.files.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.change.ecr.files.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEcr().getId());
        object.setType("ecr");
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
    public void ecrFileLocked(ECREvents.ECRFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.ecr.files.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEcr().getId());
        object.setType("ecr");
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
    public void ecrFileUnlocked(ECREvents.ECRFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.ecr.files.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEcr().getId());
        object.setType("ecr");
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
    public void ecrFileDownloaded(ECREvents.ECRFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.ecr.files.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEcr().getId());
        object.setType("ecr");
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
    public void ecrWorkflowStarted(ECREvents.ECRWorkflowStartedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.ecr.workflow.start");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEcr().getId());
        object.setType("ecr");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void ecrWorkflowFinished(ECREvents.ECRWorkflowFinishedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.ecr.workflow.finish");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEcr().getId());
        object.setType("ecr");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void ecrWorkflowPromoted(ECREvents.ECRWorkflowPromotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.ecr.workflow.promote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEcr().getId());
        object.setType("ecr");
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
    public void ecrWorkflowDemoted(ECREvents.ECRWorkflowDemotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.ecr.workflow.demote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEcr().getId());
        object.setType("ecr");
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
    public void ecrWorkflowHold(ECREvents.ECRWorkflowHoldEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.ecr.workflow.hold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEcr().getId());
        object.setType("ecr");
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
    public void ecrWorkflowUnhold(ECREvents.ECRWorkflowUnholdEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.ecr.workflow.unhold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEcr().getId());
        object.setType("ecr");
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
    public void ecrWorkflowChange(ECREvents.ECRWorkflowChangeEvent event) {
        ActivityStream as = new ActivityStream();
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEcr().getId());
        object.setType("ecr");
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
            as.setActivity("plm.change.ecr.workflow.change");
        } else {
            as.setActivity("plm.change.ecr.workflow.add");
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
    public void ecrCommentAdded(ECREvents.ECRCommentAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.ecr.comment");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEcr().getId());
        object.setType("ecr");
        as.setObject(object);
        as.setData(event.getComment().getComment());

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void ecrAffectedItemsAdded(ECREvents.ECRAffectedItemAddedEvent event) {
        List<PLMECRAffectedItem> affectedItems = event.getAffectedItems();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.ecr.affectedItems.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEcr().getId());
        object.setType("ecr");
        as.setObject(object);
        as.setData(getECRAffectedItemsAddedJson(affectedItems));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void ecrAffectedItemsDeleted(ECREvents.ECRAffectedItemDeletedEvent event) {
        PLMECRAffectedItem affectedItem = event.getAffectedItem();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.ecr.affectedItems.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEcr().getId());
        object.setType("ecr");
        as.setObject(object);
        as.setData(getECRAffectedItemsDeletedJson(affectedItem));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void ecrRelatedItemsAdded(ECREvents.ECRRelatedItemsAddedEvent event) {
        List<PLMChangeRelatedItem> relatedItems = event.getChangeRelatedItems();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.ecr.relatedItems.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEcr().getId());
        object.setType("ecr");
        as.setObject(object);
        as.setData(getECRRelatedItemsAddedJson(relatedItems));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void ecrRelatedItemsDeleted(ECREvents.ECRRelatedItemsDeletedEvent event) {
        PLMChangeRelatedItem relatedItem = event.getChangeRelatedItem();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.ecr.relatedItems.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEcr().getId());
        object.setType("ecr");
        as.setObject(object);
        as.setData(getECRRelatedItemsDeletedJson(relatedItem));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void ecrProblemReportAdded(ECREvents.ECRProblemReportAddedEvent event) {
        List<PLMECRPR> problemReports = event.getProblemReports();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.ecr.problemReports.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEcr().getId());
        object.setType("ecr");
        as.setObject(object);
        as.setData(getECRProblemReportsAddedJson(problemReports));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void ecrProblemReportDeleted(ECREvents.ECRProblemReportDeletedEvent event) {
        PLMECRPR problemReport = event.getProblemReport();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.change.ecr.problemReports.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEcr().getId());
        object.setType("ecr");
        as.setObject(object);
        as.setData(getECRProblemReportDeletedJson(problemReport));
        activityStreamService.create(as);
    }

    @Override
    public String getConverterKey() {
        return "plm.change.ecr";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            PLMECR ecr = ecrRepository.findOne(object.getObject());

            if (ecr == null) return "";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.change.ecr.create":
                    convertedString = getECRCreatedString(messageString, actor, ecr);
                    break;
                case "plm.change.ecr.revision":
                    convertedString = getECRRevisionCreatedString(messageString, actor, ecr);
                    break;
                case "plm.change.ecr.update.basicinfo":
                    convertedString = getECRBasicInfoUpdatedString(messageString, actor, ecr, as);
                    break;
                case "plm.change.ecr.update.attributes":
                    convertedString = getECRAttributeUpdatedString(messageString, actor, ecr, as);
                    break;
                case "plm.change.ecr.files.add":
                    convertedString = getECRFilesAddedString(messageString, ecr, as);
                    break;
                case "plm.change.ecr.files.delete":
                    convertedString = getECRFileDeletedString(messageString, ecr, as);
                    break;
                case "plm.change.ecr.files.folders.add":
                    convertedString = getECRFoldersAddedString(messageString, ecr, as);
                    break;
                case "plm.change.ecr.files.folders.delete":
                    convertedString = getECRFoldersDeletedString(messageString, ecr, as);
                    break;
                case "plm.change.ecr.files.version":
                    convertedString = getECRFilesVersionedString(messageString, ecr, as);
                    break;
                case "plm.change.ecr.files.rename":
                    convertedString = getECRFileRenamedString(messageString, ecr, as);
                    break;
                case "plm.change.ecr.files.replace":
                    convertedString = getECRFileRenamedString(messageString, ecr, as);
                    break;
                case "plm.change.ecr.files.lock":
                    convertedString = getItemFileString(messageString, ecr, as);
                    break;
                case "plm.change.ecr.files.unlock":
                    convertedString = getItemFileString(messageString, ecr, as);
                    break;
                case "plm.change.ecr.files.download":
                    convertedString = getItemFileString(messageString, ecr, as);
                    break;
                case "plm.change.ecr.workflow.start":
                    convertedString = getECRWorkflowStartString(messageString, ecr, as);
                    break;
                case "plm.change.ecr.workflow.finish":
                    convertedString = getECRWorkflowFinishString(messageString, ecr, as);
                    break;
                case "plm.change.ecr.workflow.promote":
                    convertedString = getECRWorkflowPromoteDemoteString(messageString, ecr, as);
                    break;
                case "plm.change.ecr.workflow.demote":
                    convertedString = getECRWorkflowPromoteDemoteString(messageString, ecr, as);
                    break;
                case "plm.change.ecr.workflow.hold":
                    convertedString = getECRWorkflowHoldUnholdString(messageString, ecr, as);
                    break;
                case "plm.change.ecr.workflow.unhold":
                    convertedString = getECRWorkflowHoldUnholdString(messageString, ecr, as);
                    break;
                case "plm.change.ecr.workflow.add":
                    convertedString = getECRWorkflowAddedString(messageString, ecr, as);
                    break;
                case "plm.change.ecr.workflow.change":
                    convertedString = getECRWorkflowChangeString(messageString, ecr, as);
                    break;
                case "plm.change.ecr.comment":
                    convertedString = getECRCommentString(messageString, ecr, as);
                    break;
                case "plm.change.ecr.affectedItems.add":
                    convertedString = getECRAffectedItemsAddedString(messageString, ecr, as);
                    break;
                case "plm.change.ecr.affectedItems.delete":
                    convertedString = getECRAffectedItemsDeletedString(messageString, ecr, as);
                    break;
                case "plm.change.ecr.relatedItems.add":
                    convertedString = getECRRelatedItemsAddedString(messageString, ecr, as);
                    break;
                case "plm.change.ecr.relatedItems.delete":
                    convertedString = getECRRelatedItemsDeletedString(messageString, ecr, as);
                    break;
                case "plm.change.ecr.problemReports.add":
                    convertedString = getECRProblemReportAddedString(messageString, ecr, as);
                    break;
                case "plm.change.ecr.problemReports.delete":
                    convertedString = getECRProblemReportDeletedString(messageString, ecr, as);
                    break;                       
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getECRCreatedString(String messageString, Person actor, PLMECR ecr) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), ecr.getCrNumber());
    }

    private String getECRRevisionCreatedString(String messageString, Person actor, PLMECR ecr) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), ecr.getCrNumber());
    }

    private String getECRBasicInfoUpdatedString(String messageString, Person actor, PLMECR ecr, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), ecr.getCrNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.change.ecr.update.basicinfo.property");

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

    private String getEcrBasicInfoUpdatedJson(PLMECR oldEcr, PLMECR ecr) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldEcr.getTitle();
        String newValue = ecr.getTitle();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Title", oldValue, newValue));
        }

        oldValue = oldEcr.getDescriptionOfChange();
        newValue = ecr.getDescriptionOfChange();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Description of Change", oldValue, newValue));
        }

        oldValue = oldEcr.getReasonForChange();
        newValue = ecr.getReasonForChange();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Reason for Change", oldValue, newValue));
        }

        oldValue = oldEcr.getProposedChanges();
        newValue = ecr.getProposedChanges();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Proposed Changes", oldValue, newValue));
        }

        oldValue = oldEcr.getImpactAnalysis();
        newValue = ecr.getImpactAnalysis();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Impact Analysis", oldValue, newValue));
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

    private String getECRAttributesUpdatedJson(PLMChangeAttribute oldAttribute, PLMChangeAttribute newAttribute) {
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

    private String getECRAttributeUpdatedString(String messageString, Person actor, PLMECR ecr, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), ecr.getCrNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.change.ecr.update.attributes.attribute");
        String propertyUpdateString = activityStreamResourceBundle.getString("plm.change.ecr.update.attributes.attribute").substring(0, 21);

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

    private String getECRFilesAddedJson(List<PLMChangeFile> files) {
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

    private String getECRFoldersAddedJson(PLMFile file) {
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

    private String getECRFoldersDeletedJson(PLMFile file) {
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

    private String getECRFilesAddedString(String messageString, PLMECR ecr, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), ecr.getCrNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.change.ecr.files.add.file");

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

    private String getECRFoldersAddedString(String messageString, PLMECR ecr, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), ecr.getCrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getECRFoldersDeletedString(String messageString, PLMECR ecr, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), ecr.getCrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getECRFileDeletedString(String messageString, PLMECR ecr, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), ecr.getCrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getECRFilesVersionedJson(List<PLMChangeFile> files) {
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

    private String getECRFilesVersionedString(String messageString, PLMECR ecr, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), ecr.getCrNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.change.ecr.files.version.file");

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

    private String getECRFileRenamedString(String messageString, PLMECR ecr, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    ecr.getCrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getItemFileString(String messageString, PLMECR ecr, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    ecr.getCrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getECRWorkflowStartString(String messageString, PLMECR ecr, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                ecr.getCrNumber());
    }

    private String getECRWorkflowFinishString(String messageString, PLMECR ecr, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                ecr.getCrNumber());
    }

    private String getECRWorkflowPromoteDemoteString(String messageString, PLMECR ecr, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();
        ActivityStreamObject target = as.getTarget();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus fromActivity = workflowStatusRepository.findOne(source.getObject());
        PLMWorkflowStatus toActivity = workflowStatusRepository.findOne(target.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                ecr.getCrNumber(),
                highlightValue(fromActivity.getName()),
                highlightValue(toActivity.getName()));
    }

    private String getECRWorkflowHoldUnholdString(String messageString, PLMECR ecr, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus plmWorkflowActivity = workflowStatusRepository.findOne(source.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflowActivity.getName()),
                highlightValue(plmWorkflow.getName()),
                ecr.getCrNumber());
    }

    private String getECRWorkflowAddedString(String messageString, PLMECR ecr, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getNewValue()),
                    ecr.getCrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getECRWorkflowChangeString(String messageString, PLMECR ecr, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getOldValue()),
                    highlightValue(workflow.getNewValue()),
                    ecr.getCrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getECRCommentString(String messageString, PLMECR ecr, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                ecr.getCrNumber());

        String s = highlightValue("Comment: ");
        s += as.getData();
        s = addMarginToMessage(s);

        message += s;

        return message;
    }

    private String getECRAffectedItemsAddedJson(List<PLMECRAffectedItem> affectedItems) {
        List<ASNewAffectedItem> asNewAffectedItems = new ArrayList<>();
        for (PLMECRAffectedItem affectedItem : affectedItems) {
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(affectedItem.getItem());
            PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
            ASNewAffectedItem asNewAffectedItem = new ASNewAffectedItem(affectedItem.getItem(), item.getItemName(), item.getItemNumber(), itemRevision.getRevision(), itemRevision.getLifeCyclePhase().getPhase());
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

    private String getECRAffectedItemsDeletedJson(PLMECRAffectedItem affectedItem) {
        String json = null;
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(affectedItem.getItem());
        PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());

        ASNewAffectedItem asNewAffectedItem = new ASNewAffectedItem(affectedItem.getItem(), item.getItemName(), item.getItemNumber(), itemRevision.getRevision(), itemRevision.getLifeCyclePhase().getPhase());

        List<ASNewAffectedItem> asNewAffectedItems = new ArrayList<>();
        asNewAffectedItems.add(asNewAffectedItem);
        try {
            json = objectMapper.writeValueAsString(asNewAffectedItems);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getECRAffectedItemsAddedString(String messageString, PLMECR ecr, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), ecr.getCrNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.change.ecr.affectedItems.add.item");

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

    private String getECRAffectedItemsDeletedString(String messageString, PLMECR ecr, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewAffectedItem> asNewAffectedItems = objectMapper.readValue(json, new TypeReference<List<ASNewAffectedItem>>() {
            });
            ASNewAffectedItem asNewAffectedItem = asNewAffectedItems.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(asNewAffectedItem.getItemName()), highlightValue(asNewAffectedItem.getRevision()), highlightValue(asNewAffectedItem.getLifecyclePhase()), ecr.getCrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getECRRelatedItemsAddedJson(List<PLMChangeRelatedItem> relatedItems) {
        List<ASNewAffectedItem> asNewAffectedItems = new ArrayList<>();
        for (PLMChangeRelatedItem relatedItem : relatedItems) {
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(relatedItem.getItem());
            PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
            ASNewAffectedItem asNewAffectedItem = new ASNewAffectedItem(relatedItem.getItem(), item.getItemName(), item.getItemNumber(), itemRevision.getRevision(), itemRevision.getLifeCyclePhase().getPhase());
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

    private String getECRRelatedItemsDeletedJson(PLMChangeRelatedItem relatedItem) {
        String json = null;
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(relatedItem.getItem());
        PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());

        ASNewAffectedItem asNewAffectedItem = new ASNewAffectedItem(relatedItem.getItem(), item.getItemName(), item.getItemNumber(), itemRevision.getRevision(), itemRevision.getLifeCyclePhase().getPhase());

        List<ASNewAffectedItem> asNewAffectedItems = new ArrayList<>();
        asNewAffectedItems.add(asNewAffectedItem);
        try {
            json = objectMapper.writeValueAsString(asNewAffectedItems);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }


    private String getECRRelatedItemsAddedString(String messageString, PLMECR ecr, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), ecr.getCrNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.change.ecr.relatedItems.add.item");

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

    private String getECRRelatedItemsDeletedString(String messageString, PLMECR ecr, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewAffectedItem> asNewAffectedItems = objectMapper.readValue(json, new TypeReference<List<ASNewAffectedItem>>() {
            });
            ASNewAffectedItem asNewAffectedItem = asNewAffectedItems.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(asNewAffectedItem.getItemName()), highlightValue(asNewAffectedItem.getRevision()), highlightValue(asNewAffectedItem.getLifecyclePhase()), ecr.getCrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getECRProblemReportsAddedJson(List<PLMECRPR> problemReports) {
        List<AsEcrProblemReport> asEcrProblemReports = new ArrayList<>();
        for (PLMECRPR problemReport : problemReports) {
            PQMProblemReport pr = problemReportRepository.findOne(problemReport.getProblemReport());
            AsEcrProblemReport asEcrProblemReport = new AsEcrProblemReport(pr.getPrNumber().toString());
            asEcrProblemReports.add(asEcrProblemReport);
        }
        String json = null;
        try {
            json = objectMapper.writeValueAsString(asEcrProblemReports);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getECRProblemReportAddedString(String messageString, PLMECR ecr, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), ecr.getCrNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.change.ecr.problemReports.add.report");

        String json = as.getData();
        try {
            List<AsEcrProblemReport> problemReports = objectMapper.readValue(json, new TypeReference<List<AsEcrProblemReport>>() {
            });
            problemReports.forEach(problemReport -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, 
                highlightValue(problemReport.getPrNumber())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getECRProblemReportDeletedJson(PLMECRPR problemReport) {
        String json = null;
        PQMProblemReport pr = problemReportRepository.findOne(problemReport.getProblemReport());

        AsEcrProblemReport asEcrProblemReport = new AsEcrProblemReport((pr.getPrNumber().toString()));

        List<AsEcrProblemReport> asEcrProblemReports = new ArrayList<>();
        asEcrProblemReports.add(asEcrProblemReport);
        try {
            json = objectMapper.writeValueAsString(asEcrProblemReports);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getECRProblemReportDeletedString(String messageString, PLMECR ecr, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<AsEcrProblemReport> asEcrProblemReports = objectMapper.readValue(json, new TypeReference<List<AsEcrProblemReport>>() {
            });
            AsEcrProblemReport asEcrProblemReport = asEcrProblemReports.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(asEcrProblemReport.getPrNumber()), ecr.getCrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

}
