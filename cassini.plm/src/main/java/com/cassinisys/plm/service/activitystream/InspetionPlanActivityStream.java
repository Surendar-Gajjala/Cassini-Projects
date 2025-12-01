package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.DataType;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.InspectionPlanEvents;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.repo.pqm.InspectionPlanRepository;
import com.cassinisys.plm.repo.pqm.InspectionPlanRevisionRepository;
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
public class InspetionPlanActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PLMWorkflowRepository workFlowRepository;
    @Autowired
    private PLMWorkflowStatusRepository workflowStatusRepository;
    @Autowired
    private InspectionPlanRepository inspectionPlanRepository;
    @Autowired
    private InspectionPlanRevisionRepository inspectionPlanRevisionRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository workflowDefinitionRepository;
    @Autowired
    private QualityTypeAttributeRepository qualityTypeAttributeRepository;
    @Autowired
    private CommonActivityStream commonActivityStream;

    @Async
    @EventListener
    public void planCreated(InspectionPlanEvents.PlanCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspectionPlanRevision().getId());
        object.setType("inspectionPlan");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.plan.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void planRevisionCreated(InspectionPlanEvents.PlanRevisionCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspectionPlanRevision().getId());
        object.setType("inspectionPlan");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.plan.revision");
        as.setObject(object);
        as.setConverter(getConverterKey());
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void planBasicInfoUpdated(InspectionPlanEvents.PlanBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PQMInspectionPlan oldPlan = event.getOldInspectionPlan();
        PQMInspectionPlan newPlan = event.getInspectionPlan();
        PQMInspectionPlanRevision inspectionPlanRevision = event.getInspectionPlanRevision();

        object.setObject(inspectionPlanRevision.getId());
        object.setType("inspectionPlan");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.plan.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getPlanBasicInfoUpdatedJson(oldPlan, newPlan));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void planAttributesUpdated(InspectionPlanEvents.PlanAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PQMInspectionPlanRevision inspectionPlanRevision = event.getInspectionPlanRevision();

        PQMInspectionPlanAttribute oldAtt = event.getOldAttribute();
        PQMInspectionPlanAttribute newAtt = event.getNewAttribute();

        object.setObject(inspectionPlanRevision.getId());
        object.setType("inspectionPlan");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.plan.update.attributes");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getPlanAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void planRevisionAttributesUpdated(InspectionPlanEvents.PlanRevisionAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PQMInspectionPlanRevision inspectionPlanRevision = event.getInspectionPlanRevision();

        PQMInspectionPlanRevisionAttribute oldAtt = event.getOldAttribute();
        PQMInspectionPlanRevisionAttribute newAtt = event.getNewAttribute();

        object.setObject(inspectionPlanRevision.getId());
        object.setType("inspectionPlan");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.plan.update.attributes");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getPlanRevisionAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void planFilesAdded(InspectionPlanEvents.PlanFilesAddedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.plan.files.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspectionPlanRevision().getId());
        object.setType("inspectionPlan");
        as.setObject(object);
        as.setData(getPlanFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void planFoldersAdded(InspectionPlanEvents.PlanFoldersAddedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.plan.files.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspectionPlanRevision().getId());
        object.setType("inspectionPlan");
        as.setObject(object);
        as.setData(getPlanFoldersAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void planFoldersDeleted(InspectionPlanEvents.PlanFoldersDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.plan.files.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspectionPlanRevision().getId());
        object.setType("inspectionPlan");
        as.setObject(object);
        as.setData(getPlanFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void planFileDeleted(InspectionPlanEvents.PlanFileDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.plan.files.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspectionPlanRevision().getId());
        object.setType("inspectionPlan");
        as.setObject(object);
        as.setData(getPlanFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void planFilesVersioned(InspectionPlanEvents.PlanFilesVersionedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.plan.files.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspectionPlanRevision().getId());
        object.setType("inspectionPlan");
        as.setObject(object);
        as.setData(getPlanFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void planFileRenamed(InspectionPlanEvents.PlanFileRenamedEvent event) {
        PLMFile oldFile = event.getOldFile();
        PLMFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals("Rename")) as.setActivity("plm.quality.plan.files.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.quality.plan.files.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspectionPlanRevision().getId());
        object.setType("inspectionPlan");
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
    public void planFileLocked(InspectionPlanEvents.PlanFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.plan.files.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspectionPlanRevision().getId());
        object.setType("inspectionPlan");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getInspectionPlanFile().getId(), event.getInspectionPlanFile().getName(), FileUtils.byteCountToDisplaySize(event.getInspectionPlanFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void planFileUnlocked(InspectionPlanEvents.PlanFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.plan.files.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspectionPlanRevision().getId());
        object.setType("inspectionPlan");
        as.setObject(object);
        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getInspectionPlanFile().getId(), event.getInspectionPlanFile().getName(), FileUtils.byteCountToDisplaySize(event.getInspectionPlanFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void planFileDownloaded(InspectionPlanEvents.PlanFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.plan.files.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspectionPlanRevision().getId());
        object.setType("inspectionPlan");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getInspectionPlanFile().getId(), event.getInspectionPlanFile().getName(), FileUtils.byteCountToDisplaySize(event.getInspectionPlanFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }


    @Async
    @EventListener
    public void planWorkflowStarted(InspectionPlanEvents.PlanWorkflowStartedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.plan.workflow.start");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspectionPlanRevision().getId());
        object.setType("inspectionPlan");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void planWorkflowFinished(InspectionPlanEvents.PlanWorkflowFinishedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.plan.workflow.finish");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspectionPlanRevision().getId());
        object.setType("inspectionPlan");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void planWorkflowPromoted(InspectionPlanEvents.PlanWorkflowPromotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.plan.workflow.promote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspectionPlanRevision().getId());
        object.setType("inspectionPlan");
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
    public void planWorkflowDemoted(InspectionPlanEvents.PlanWorkflowDemotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.plan.workflow.demote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspectionPlanRevision().getId());
        object.setType("inspectionPlan");
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
    public void planWorkflowHold(InspectionPlanEvents.PlanWorkflowHoldEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.plan.workflow.hold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspectionPlanRevision().getId());
        object.setType("inspectionPlan");
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
    public void planWorkflowUnhold(InspectionPlanEvents.PlanWorkflowUnholdEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.plan.workflow.unhold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspectionPlanRevision().getId());
        object.setType("inspectionPlan");
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
    public void planWorkflowChange(InspectionPlanEvents.PlanWorkflowChangeEvent event) {
        ActivityStream as = new ActivityStream();
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspectionPlanRevision().getId());
        object.setType("inspectionPlan");
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
            as.setActivity("plm.quality.plan.workflow.change");
        } else {
            as.setActivity("plm.quality.plan.workflow.add");
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
    public void planCommentAdded(InspectionPlanEvents.PlanCommentAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.plan.comment");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspectionPlanRevision().getId());
        object.setType("inspectionPlan");
        as.setObject(object);
        as.setData(event.getComment().getComment());

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void planChecklistAdd(InspectionPlanEvents.PlanChecklistAddEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.plan.checklist.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspectionPlanRevision().getId());
        object.setType("inspectionPlan");
        as.setObject(object);

        try {
            as.setData(objectMapper.writeValueAsString(new ASPlanChecklist(event.getInspectionPlanChecklist().getTitle(),
                    event.getInspectionPlanChecklist().getProcedure(), event.getInspectionPlanChecklist().getSummary())));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void planChecklistDelete(InspectionPlanEvents.PlanChecklistDeleteEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.plan.checklist.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspectionPlanRevision().getId());
        object.setType("inspectionPlan");
        as.setObject(object);

        try {
            as.setData(objectMapper.writeValueAsString(new ASPlanChecklist(event.getInspectionPlanChecklist().getTitle(),
                    event.getInspectionPlanChecklist().getProcedure(), event.getInspectionPlanChecklist().getSummary())));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void planChecklistParameterAdd(InspectionPlanEvents.PlanChecklistParameterAddEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.plan.checklist.parameters.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspectionPlanRevision().getId());
        object.setType("inspectionPlan");
        as.setObject(object);

        try {
            as.setData(objectMapper.writeValueAsString(new ASPlanChecklistParameter(event.getChecklistParameter().getId(),
                    event.getChecklistParameter().getName(), event.getChecklistParameter().getPassCriteria(), event.getChecklistParameter().getExpectedValueType(), event.getChecklistParameter().getExpectedValue(), event.getInspectionPlanChecklist().getTitle())));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void planChecklistParameterDelete(InspectionPlanEvents.PlanChecklistParameterDeleteEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.plan.checklist.parameters.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspectionPlanRevision().getId());
        object.setType("inspectionPlan");
        as.setObject(object);

        try {
            as.setData(objectMapper.writeValueAsString(new ASPlanChecklistParameter(event.getChecklistParameter().getId(),
                    event.getChecklistParameter().getName(), event.getChecklistParameter().getPassCriteria(), event.getChecklistParameter().getExpectedValueType(), event.getChecklistParameter().getExpectedValue(), event.getInspectionPlanChecklist().getTitle())));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async

    @EventListener

    public void inspectionPlanChecklistUpdated(InspectionPlanEvents.InspectionPlanChecklistUpdatedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.plan.checklist.update");
        as.setConverter(getConverterKey());
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInspectionPlanRevision().getId());
        object.setType("inspectionPlan");
        as.setObject(object);
        as.setData(getInspectionPlanChecklistUpdatedJson(event.getOldPlanChecklist(), event.getInspectionPlanChecklist()));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Override
    public String getConverterKey() {
        return "plm.quality.plan";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(object.getObject());
            PQMInspectionPlan inspectionPlan = inspectionPlanRepository.findOne(inspectionPlanRevision.getPlan().getId());

            if (inspectionPlan == null) return "";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.quality.plan.create":
                    convertedString = getPlanCreatedString(messageString, actor, inspectionPlan);
                    break;
                case "plm.quality.plan.revision":
                    convertedString = getPlanRevisionCreatedString(messageString, actor, inspectionPlan, inspectionPlanRevision);
                    break;
                case "plm.quality.plan.update.basicinfo":
                    convertedString = getPlanBasicInfoUpdatedString(messageString, actor, inspectionPlan, inspectionPlanRevision, as);
                    break;
                case "plm.quality.plan.update.attributes":
                    convertedString = getPlanAttributeUpdatedString(messageString, actor, inspectionPlan, inspectionPlanRevision, as);
                    break;
                case "plm.quality.plan.files.add":
                    convertedString = getPlanFilesAddedString(messageString, inspectionPlan, inspectionPlanRevision, as);
                    break;
                case "plm.quality.plan.files.delete":
                    convertedString = getPlanFileDeletedString(messageString, inspectionPlan, inspectionPlanRevision, as);
                    break;
                case "plm.quality.plan.files.folders.add":
                    convertedString = getPlanFoldersAddedString(messageString, inspectionPlan, inspectionPlanRevision, as);
                    break;
                case "plm.quality.plan.files.folders.delete":
                    convertedString = getPlanFoldersDeletedString(messageString, inspectionPlan, inspectionPlanRevision, as);
                    break;
                case "plm.quality.plan.files.version":
                    convertedString = getPlanFilesVersionedString(messageString, inspectionPlan, inspectionPlanRevision, as);
                    break;
                case "plm.quality.plan.files.rename":
                    convertedString = getPlanFileRenamedString(messageString, inspectionPlan, inspectionPlanRevision, as);
                    break;
                case "plm.quality.plan.files.replace":
                    convertedString = getPlanFileRenamedString(messageString, inspectionPlan, inspectionPlanRevision, as);
                    break;
                case "plm.quality.plan.files.lock":
                    convertedString = getPlanFileString(messageString, inspectionPlan, inspectionPlanRevision, as);
                    break;
                case "plm.quality.plan.files.unlock":
                    convertedString = getPlanFileString(messageString, inspectionPlan, inspectionPlanRevision, as);
                    break;
                case "plm.quality.plan.files.download":
                    convertedString = getPlanFileString(messageString, inspectionPlan, inspectionPlanRevision, as);
                    break;
                case "plm.quality.plan.workflow.start":
                    convertedString = getPlanWorkflowStartString(messageString, inspectionPlan, inspectionPlanRevision, as);
                    break;
                case "plm.quality.plan.workflow.finish":
                    convertedString = getPlanWorkflowFinishString(messageString, inspectionPlan, inspectionPlanRevision, as);
                    break;
                case "plm.quality.plan.workflow.promote":
                    convertedString = getPlanWorkflowPromoteDemoteString(messageString, inspectionPlan, inspectionPlanRevision, as);
                    break;
                case "plm.quality.plan.workflow.demote":
                    convertedString = getPlanWorkflowPromoteDemoteString(messageString, inspectionPlan, inspectionPlanRevision, as);
                    break;
                case "plm.quality.plan.workflow.hold":
                    convertedString = getPlanWorkflowHoldUnholdString(messageString, inspectionPlan, inspectionPlanRevision, as);
                    break;
                case "plm.quality.plan.workflow.unhold":
                    convertedString = getPlanWorkflowHoldUnholdString(messageString, inspectionPlan, inspectionPlanRevision, as);
                    break;
                case "plm.quality.plan.workflow.add":
                    convertedString = getPlanWorkflowAddedString(messageString, inspectionPlan, inspectionPlanRevision, as);
                    break;
                case "plm.quality.plan.workflow.change":
                    convertedString = getPlanWorkflowChangeString(messageString, inspectionPlan, inspectionPlanRevision, as);
                    break;
                case "plm.quality.plan.comment":
                    convertedString = getPlanCommentString(messageString, inspectionPlan, inspectionPlanRevision, as);
                    break;
                case "plm.quality.plan.checklist.add":
                    convertedString = getPlanChecklistAddedOrDeletedString(messageString, inspectionPlan, inspectionPlanRevision, as);
                    break;
                case "plm.quality.plan.checklist.delete":
                    convertedString = getPlanChecklistAddedOrDeletedString(messageString, inspectionPlan, inspectionPlanRevision, as);
                    break;
                case "plm.quality.plan.checklist.parameters.add":
                    convertedString = getPlanChecklistParameterAddedString(messageString, inspectionPlan, inspectionPlanRevision, as);
                    break;
                case "plm.quality.plan.checklist.parameters.delete":
                    convertedString = getPlanChecklistParameterDeletedString(messageString, inspectionPlan, inspectionPlanRevision, as);
                    break;
                case "plm.quality.plan.checklist.update":
                    convertedString = getAssignedToChecklistUpdatedString(messageString, inspectionPlan, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getPlanCreatedString(String messageString, Person actor, PQMInspectionPlan inspectionPlan) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), inspectionPlan.getNumber());
    }

    private String getPlanRevisionCreatedString(String messageString, Person actor, PQMInspectionPlan inspectionPlan, PQMInspectionPlanRevision inspectionPlanRevision) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), inspectionPlan.getNumber(),
                inspectionPlanRevision.getRevision(), inspectionPlanRevision.getLifeCyclePhase().getPhase());
    }

    private String getPlanBasicInfoUpdatedJson(PQMInspectionPlan oldPlan, PQMInspectionPlan inspectionPlan) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldPlan.getName();
        String newValue = inspectionPlan.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        oldValue = oldPlan.getDescription();
        newValue = inspectionPlan.getDescription();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue, newValue));
        }

        oldValue = oldPlan.getNotes();
        newValue = inspectionPlan.getNotes();
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

    private String getPlanBasicInfoUpdatedString(String messageString, Person actor, PQMInspectionPlan inspectionPlan, PQMInspectionPlanRevision inspectionPlanRevision, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), inspectionPlan.getNumber(),
                inspectionPlanRevision.getRevision(), inspectionPlanRevision.getLifeCyclePhase().getPhase());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.quality.plan.update.basicinfo.property");

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

    private String getPlanAttributesUpdatedJson(PQMInspectionPlanAttribute oldAttribute, PQMInspectionPlanAttribute newAttribute) {
        PQMQualityTypeAttribute attDef = qualityTypeAttributeRepository.findOne(newAttribute.getId().getAttributeDef());
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

    private String getPlanRevisionAttributesUpdatedJson(PQMInspectionPlanRevisionAttribute oldAttribute, PQMInspectionPlanRevisionAttribute newAttribute) {
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

    private String getPlanAttributeUpdatedString(String messageString, Person actor, PQMInspectionPlan inspectionPlan, PQMInspectionPlanRevision inspectionPlanRevision, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), inspectionPlan.getNumber(),
                inspectionPlanRevision.getRevision(), inspectionPlanRevision.getLifeCyclePhase().getPhase());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.quality.plan.update.attributes.attribute");
        String propertyUpdateString = activityStreamResourceBundle.getString("plm.quality.plan.update.attributes.attribute").substring(0, 21);


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

    private String getPlanFilesAddedJson(List<PLMFile> files) {
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

    private String getPlanFoldersAddedJson(PLMFile file) {
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

    private String getPlanFoldersDeletedJson(PLMFile file) {
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

    private String getPlanFilesAddedString(String messageString, PQMInspectionPlan inspectionPlan, PQMInspectionPlanRevision inspectionPlanRevision, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), inspectionPlan.getNumber(),
                inspectionPlanRevision.getRevision(), inspectionPlanRevision.getLifeCyclePhase().getPhase());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.quality.plan.files.add.file");

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

    private String getPlanFoldersAddedString(String messageString, PQMInspectionPlan inspectionPlan, PQMInspectionPlanRevision inspectionPlanRevision, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), inspectionPlan.getNumber(),
                    inspectionPlanRevision.getRevision(), inspectionPlanRevision.getLifeCyclePhase().getPhase());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getPlanFoldersDeletedString(String messageString, PQMInspectionPlan inspectionPlan, PQMInspectionPlanRevision inspectionPlanRevision, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), inspectionPlan.getNumber(),
                    inspectionPlanRevision.getRevision(), inspectionPlanRevision.getLifeCyclePhase().getPhase());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getPlanFileDeletedString(String messageString, PQMInspectionPlan inspectionPlan, PQMInspectionPlanRevision inspectionPlanRevision, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), inspectionPlan.getNumber(),
                    inspectionPlanRevision.getRevision(), inspectionPlanRevision.getLifeCyclePhase().getPhase());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getPlanChecklistAddedOrDeletedString(String messageString, PQMInspectionPlan inspectionPlan, PQMInspectionPlanRevision inspectionPlanRevision, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            ASPlanChecklist planChecklist = objectMapper.readValue(json, new TypeReference<ASPlanChecklist>() {
            });
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(planChecklist.getTitle()), inspectionPlan.getNumber(),
                    inspectionPlanRevision.getRevision(), inspectionPlanRevision.getLifeCyclePhase().getPhase());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getPlanChecklistParameterAddedString(String messageString, PQMInspectionPlan inspectionPlan, PQMInspectionPlanRevision inspectionPlanRevision, ActivityStream as) {
        String activityString = "";
        StringBuffer sb = new StringBuffer();
        try {
            String json = as.getData();
            ASPlanChecklistParameter planChecklist = objectMapper.readValue(json, new TypeReference<ASPlanChecklistParameter>() {
            });
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(planChecklist.getChecklistName()), inspectionPlan.getNumber(),
                    inspectionPlanRevision.getRevision(), inspectionPlanRevision.getLifeCyclePhase().getPhase());

            sb.append(activityString);

            String fileString = activityStreamResourceBundle.getString("plm.quality.plan.checklist.parameters.add.parameter");
            String expectedValue = "";
            if (planChecklist.getDataType().equals(DataType.TEXT)) {
                expectedValue = planChecklist.getExpectedValue().getTextValue();
            } else if (planChecklist.getDataType().equals(DataType.DOUBLE)) {
                expectedValue = "" + planChecklist.getExpectedValue().getDoubleValue();
            } else if (planChecklist.getDataType().equals(DataType.INTEGER)) {
                expectedValue = "" + planChecklist.getExpectedValue().getIntegerValue();
            } else if (planChecklist.getDataType().equals(DataType.DATE)) {
                expectedValue = "" + planChecklist.getExpectedValue().getDateValue();
            } else if (planChecklist.getDataType().equals(DataType.BOOLEAN)) {
                expectedValue = "" + planChecklist.getExpectedValue().getBooleanValue();
            }
            String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(planChecklist.getName()), highlightValue(planChecklist.getPassCriteria()), highlightValue(expectedValue)));
            sb.append(s);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getPlanChecklistParameterDeletedString(String messageString, PQMInspectionPlan inspectionPlan, PQMInspectionPlanRevision inspectionPlanRevision, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            ASPlanChecklistParameter planChecklist = objectMapper.readValue(json, new TypeReference<ASPlanChecklistParameter>() {
            });
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(planChecklist.getName()), highlightValue(planChecklist.getChecklistName()), inspectionPlan.getNumber(),
                    inspectionPlanRevision.getRevision(), inspectionPlanRevision.getLifeCyclePhase().getPhase());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getPlanFilesVersionedJson(List<PLMFile> files) {
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

    private String getPlanFilesVersionedString(String messageString, PQMInspectionPlan inspectionPlan, PQMInspectionPlanRevision inspectionPlanRevision, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), inspectionPlan.getNumber(),
                inspectionPlanRevision.getRevision(), inspectionPlanRevision.getLifeCyclePhase().getPhase());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.quality.plan.files.version.file");

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

    private String getPlanFileRenamedString(String messageString, PQMInspectionPlan inspectionPlan, PQMInspectionPlanRevision inspectionPlanRevision, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    inspectionPlan.getNumber(),
                    inspectionPlanRevision.getRevision(),
                    inspectionPlanRevision.getLifeCyclePhase().getPhase());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getPlanFileString(String messageString, PQMInspectionPlan inspectionPlan, PQMInspectionPlanRevision inspectionPlanRevision, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    inspectionPlan.getNumber(),
                    inspectionPlanRevision.getRevision(),
                    inspectionPlanRevision.getLifeCyclePhase().getPhase());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getPlanWorkflowStartString(String messageString, PQMInspectionPlan inspectionPlan, PQMInspectionPlanRevision inspectionPlanRevision, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                inspectionPlan.getNumber(),
                inspectionPlanRevision.getRevision(),
                inspectionPlanRevision.getLifeCyclePhase().getPhase());
    }

    private String getPlanWorkflowFinishString(String messageString, PQMInspectionPlan inspectionPlan, PQMInspectionPlanRevision inspectionPlanRevision, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                inspectionPlan.getNumber(),
                inspectionPlanRevision.getRevision(),
                inspectionPlanRevision.getLifeCyclePhase().getPhase());
    }

    private String getPlanWorkflowPromoteDemoteString(String messageString, PQMInspectionPlan inspectionPlan, PQMInspectionPlanRevision inspectionPlanRevision, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();
        ActivityStreamObject target = as.getTarget();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus fromActivity = workflowStatusRepository.findOne(source.getObject());
        PLMWorkflowStatus toActivity = workflowStatusRepository.findOne(target.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                inspectionPlan.getNumber(),
                inspectionPlanRevision.getRevision(),
                inspectionPlanRevision.getLifeCyclePhase().getPhase(),
                highlightValue(fromActivity.getName()),
                highlightValue(toActivity.getName()));
    }

    private String getPlanWorkflowHoldUnholdString(String messageString, PQMInspectionPlan inspectionPlan, PQMInspectionPlanRevision inspectionPlanRevision, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus plmWorkflowActivity = workflowStatusRepository.findOne(source.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflowActivity.getName()),
                highlightValue(plmWorkflow.getName()),
                inspectionPlan.getNumber(),
                inspectionPlanRevision.getRevision(),
                inspectionPlanRevision.getLifeCyclePhase().getPhase());
    }

    private String getPlanWorkflowAddedString(String messageString, PQMInspectionPlan inspectionPlan, PQMInspectionPlanRevision inspectionPlanRevision, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getNewValue()),
                    inspectionPlan.getNumber(),
                    inspectionPlanRevision.getRevision(),
                    inspectionPlanRevision.getLifeCyclePhase().getPhase());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getPlanWorkflowChangeString(String messageString, PQMInspectionPlan inspectionPlan, PQMInspectionPlanRevision inspectionPlanRevision, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getOldValue()),
                    highlightValue(workflow.getNewValue()),
                    inspectionPlan.getNumber(),
                    inspectionPlanRevision.getRevision(),
                    inspectionPlanRevision.getLifeCyclePhase().getPhase());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getPlanCommentString(String messageString, PQMInspectionPlan inspectionPlan, PQMInspectionPlanRevision inspectionPlanRevision, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                inspectionPlan.getNumber(),
                inspectionPlanRevision.getRevision(),
                inspectionPlanRevision.getLifeCyclePhase().getPhase());

        String s = highlightValue("Comment: ");
        s += as.getData();
        s = addMarginToMessage(s);

        message += s;

        return message;
    }

    private String getInspectionPlanChecklistUpdatedJson(PQMInspectionPlanChecklist oldPlanChecklist, PQMInspectionPlanChecklist inspectionPlanChecklist) {
        List<ASInspectionChecklistUpdate> changes = new ArrayList<>();
        String oldValue = oldPlanChecklist.getTitle();
        String newValue = inspectionPlanChecklist.getTitle();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASInspectionChecklistUpdate(inspectionPlanChecklist.getTitle(), "Title", oldValue, newValue));
        }
        oldValue = oldPlanChecklist.getSummary();
        newValue = inspectionPlanChecklist.getSummary();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASInspectionChecklistUpdate(inspectionPlanChecklist.getSummary(), "Summary", oldValue, newValue));
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
    private String getAssignedToChecklistUpdatedString(String messageString, PQMInspectionPlan inspectionPlan, ActivityStream as) {
        StringBuffer sb = new StringBuffer();
        String updateString = activityStreamResourceBundle.getString("plm.quality.plan.checklist.update.property");
        String json = as.getData();
        try {
            List<ASInspectionChecklistUpdate> propChanges = objectMapper.readValue(json, new TypeReference<List<ASInspectionChecklistUpdate>>() {
            });
            String activityString = MessageFormat.format(messageString,highlightValue(as.getActor().getFullName().trim()),highlightValue(propChanges.get(0).getChecklistName()), inspectionPlan.getNumber());
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
