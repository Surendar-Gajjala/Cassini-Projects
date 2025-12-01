package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.RequirementDocumentEvents;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.req.*;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.repo.plm.LifeCyclePhaseRepository;
import com.cassinisys.plm.repo.req.PLMRequirementDocumentRepository;
import com.cassinisys.plm.repo.req.PLMRequirementDocumentRevisionRepository;
import com.cassinisys.plm.repo.req.PLMRequirementObjectTypeAttributeRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowStatusRepository;
import com.cassinisys.plm.service.activitystream.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class RequirementDocumentsActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PLMRequirementDocumentRepository requirementDocumentRepository;
    @Autowired
    private PLMRequirementDocumentRevisionRepository requirementDocumentRevisionRepository;
    @Autowired
    private PLMRequirementObjectTypeAttributeRepository requirementObjectTypeAttributeRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private LifeCyclePhaseRepository lifeCyclePhaseRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private PLMWorkflowRepository workFlowRepository;
    @Autowired
    private PLMWorkflowStatusRepository workflowStatusRepository;

    @Async
    @EventListener
    public void requirementDocumentCreated(RequirementDocumentEvents.RequirementDocumentCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getRequirementDocumentRevision().getId());
        object.setType("requirementDocument");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirementDocuments.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void requirementDocumentRevisionCreated(RequirementDocumentEvents.RequirementDocumentRevisionCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getRequirementDocumentRevision().getId());
        object.setType("requirementDocument");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirementDocuments.revision");
        as.setObject(object);
        as.setConverter(getConverterKey());
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void requirementDocumentBasicInfoUpdated(RequirementDocumentEvents.RequirementDocumentBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PLMRequirementDocument oldRequirementDocument = event.getPlmOldRequirementDocument();
        PLMRequirementDocument newRequirementDocument = event.getRequirementDocument();
        PLMRequirementDocumentRevision requirementDocumentRevision = event.getRequirementDocumentRevision();

        object.setObject(requirementDocumentRevision.getId());
        object.setType("requirementDocument");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirementDocuments.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getRequirementDocumentBasicInfoUpdatedJson(oldRequirementDocument, newRequirementDocument));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void requirementDocumentAttributesUpdated(RequirementDocumentEvents.RequirementDocumentAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PLMRequirementDocumentRevision plmRequirementDocumentRevision = event.getRequirementDocumentRevision();

        PLMRequirementObjectAttribute oldAtt = event.getOldAttribute();
        PLMRequirementObjectAttribute newAtt = event.getNewAttribute();

        object.setObject(plmRequirementDocumentRevision.getId());
        object.setType("requirementDocument");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirementDocuments.update.attributes");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getRequirementDocumentAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void requirementDocumentFilesAdded(RequirementDocumentEvents.RequirementDocumentFilesAddedEvent event) {
        List<PLMRequirementDocumentFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirementDocuments.files.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getRequirementDocumentRevision().getId());
        object.setType("requirementDocument");
        as.setObject(object);
        as.setData(getRequirementDocumentFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void requirementDocumentFileDeleted(RequirementDocumentEvents.RequirementDocumentFileDeletedEvent event) {
        PLMRequirementDocumentFile file = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirementDocuments.files.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getRequirementDocumentRevision().getId());
        object.setType("requirementDocument");
        as.setObject(object);
        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(file.getId(), file.getName(), FileUtils.byteCountToDisplaySize(file.getSize()));
        String json = "";
        try {
            json = objectMapper.writeValueAsString(asNewFileDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        as.setData(json);
        activityStreamService.create(as);
    }


    @Async
    @EventListener
    public void requirementDocumentFilesVersioned(RequirementDocumentEvents.RequirementDocumentFilesVersionedEvent event) {
        List<PLMRequirementDocumentFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirementDocuments.files.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getRequirementDocumentRevision().getId());
        object.setType("requirementDocument");
        as.setObject(object);
        as.setData(getRequirementDocumentFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void requirementDocumentFileRenamed(RequirementDocumentEvents.RequirementDocumentFileRenamedEvent event) {
        PLMRequirementDocumentFile oldFile = event.getOldFile();
        PLMRequirementDocumentFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirementDocuments.files.rename");
        if (event.getType().equals("Rename")) as.setActivity("plm.pm.requirementDocuments.files.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.pm.requirementDocuments.files.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getRequirementDocumentRevision().getId());
        object.setType("requirementDocument");
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
    public void requirementDocumentFoldersAdded(RequirementDocumentEvents.RequirementDocumentFoldersAddedEvent event) {
        PLMRequirementDocumentFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirementDocuments.files.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getRequirementDocumentRevision().getId());
        object.setType("requirementDocument");
        as.setObject(object);
        as.setData(getRequirementDocumentFoldersAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void requirementDocumentFoldersDeleted(RequirementDocumentEvents.RequirementDocumentFoldersDeletedEvent event) {
        PLMRequirementDocumentFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirementDocuments.files.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getRequirementDocumentRevision().getId());
        object.setType("requirementDocument");
        as.setObject(object);
        as.setData(getRequirementDocumentFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void requirementDocumentFileLocked(RequirementDocumentEvents.RequirementDocumentFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirementDocuments.files.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getRequirementDocumentRevision().getId());
        object.setType("requirementDocument");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getRequirementDocumentFile().getId(), event.getRequirementDocumentFile().getName(), FileUtils.byteCountToDisplaySize(event.getRequirementDocumentFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void requirementDocumentFileUnlocked(RequirementDocumentEvents.RequirementDocumentFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirementDocuments.files.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getRequirementDocumentRevision().getId());
        object.setType("requirementDocument");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getRequirementDocumentFile().getId(), event.getRequirementDocumentFile().getName(), FileUtils.byteCountToDisplaySize(event.getRequirementDocumentFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void requirementDocumentFileDownloaded(RequirementDocumentEvents.RequirementDocumentFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirementDocuments.files.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getRequirementDocumentRevision().getId());
        object.setType("requirementDocument");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getRequirementDocumentFile().getId(), event.getRequirementDocumentFile().getName(), FileUtils.byteCountToDisplaySize(event.getRequirementDocumentFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void requirementDocumentWorkflowStarted(RequirementDocumentEvents.RequirementDocumentWorkflowStartedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirementDocuments.workflow.start");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getRequirementDocumentRevision().getId());
        object.setType("requirementDocument");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void requirementDocumentWorkflowPromoted(RequirementDocumentEvents.RequirementDocumentWorkflowPromotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirementDocuments.workflow.promote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getRequirementDocumentRevision().getId());
        object.setType("requirementDocument");
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
    public void requirementDocumentWorkflowDemoted(RequirementDocumentEvents.RequirementDocumentWorkflowDemotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirementDocuments.workflow.demote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getRequirementDocumentRevision().getId());
        object.setType("requirementDocument");
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
    public void requirementDocumentWorkflowFinished(RequirementDocumentEvents.RequirementDocumentWorkflowFinishedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirementDocuments.workflow.finish");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getRequirementDocumentRevision().getId());
        object.setType("requirementDocument");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void requirementDocumentWorkflowHold(RequirementDocumentEvents.RequirementDocumentWorkflowHoldEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirementDocuments.workflow.hold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getRequirementDocumentRevision().getId());
        object.setType("requirementDocument");
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
    public void requirementDocumentWorkflowUnhold(RequirementDocumentEvents.RequirementDocumentWorkflowUnholdEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirementDocuments.workflow.unhold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getRequirementDocumentRevision().getId());
        object.setType("requirementDocument");
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
    public void requirementAddedEvent(RequirementDocumentEvents.RequirementCreatedEvent event) {
        PLMRequirement asNewMfrPartDTO = event.getRequirement();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirementDocuments.requirement.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getRequirementDocumentRevision().getId());
        object.setType("requirementDocument");
        as.setObject(object);
        as.setData(getRequirementAddedJson(asNewMfrPartDTO));

        activityStreamService.create(as);
    }


    @Async
    @EventListener
    public void requirementDocumentReviewerAdded(RequirementDocumentEvents.RequirementDocumentReviewerAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirementDocuments.reviewer.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getRequirementDocumentRevision().getId());
        object.setType("requirementDocument");
        as.setObject(object);
        as.setData(getReqDocumentReviewerAddedJson(event.getRequirementDocumentRevision(), event.getReviewer()));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void requirementDocumentReviewerUpdate(RequirementDocumentEvents.RequirementDocumentReviewerUpdateEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirementDocuments.reviewer.change");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getRequirementDocumentRevision().getId());
        object.setType("requirementDocument");
        as.setObject(object);
        as.setData(getReqDocumentReviewerUpdateJson(event.getRequirementDocumentRevision(), event.getReviewer()));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void requirementDocumentReviewerDeleted(RequirementDocumentEvents.RequirementDocumentReviewerDeletedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirementDocuments.reviewer.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getRequirementDocumentRevision().getId());
        object.setType("requirementDocument");
        as.setObject(object);
        Person person = personRepository.findOne(event.getReviewer().getReviewer());
        try {
            as.setData(objectMapper.writeValueAsString(new ASNewReviewerDTO(event.getReviewer().getId(), person.getFullName(), "reviewer", event.getRequirementDocumentRevision().getMaster().getNumber())));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void requirementDocumentReviewerApproved(RequirementDocumentEvents.RequirementDocumentApprovedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirementDocuments.reviewer.approved");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getRequirementDocumentRevision().getId());
        object.setType("requirementDocument");
        as.setObject(object);
        Person person = personRepository.findOne(event.getReviewer().getReviewer());
        String type = null;
        if (event.getReviewer().getStatus().equals(RequirementApprovalStatus.APPROVED)) {
            type = "approved";
        } else if (event.getReviewer().getStatus().equals(RequirementApprovalStatus.REJECTED)) {
            type = "rejected";
        } else if (event.getReviewer().getStatus().equals(RequirementApprovalStatus.REVIEWED)) {
            type = "reviewed";
        }
        try {
            as.setData(objectMapper.writeValueAsString(new ASNewReviewerDTO(event.getReviewer().getId(), person.getFullName(), type, event.getRequirementDocumentRevision().getMaster().getNumber())));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void allRequirementsApproved(RequirementDocumentEvents.RequirementsApprovedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirementDocuments.requirement.approved");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getRequirementDocumentRevision().getId());
        object.setType("requirementDocument");
        as.setObject(object);
        try {
            as.setData(objectMapper.writeValueAsString(new ASNewReviewerDTO(null, null, "approved all requirements", event.getRequirementDocumentRevision().getMaster().getNumber())));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        activityStreamService.create(as);
    }

    @Override
    public String getConverterKey() {
        return "plm.pm.requirementDocuments";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            PLMRequirementDocumentRevision requirementDocumentRevision = requirementDocumentRevisionRepository.findOne(object.getObject());
            PLMRequirementDocument requirementDocument = requirementDocumentRepository.findOne(requirementDocumentRevision.getMaster().getId());

            if (requirementDocument == null) return "";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.pm.requirementDocuments.create":
                    convertedString = getRequirementDocumentCreatedString(messageString, actor, requirementDocument);
                    break;
                case "plm.pm.requirementDocuments.revision":
                    convertedString = getRequirementDocumentRevisionCreatedString(messageString, actor, requirementDocument, requirementDocumentRevision);
                    break;
                case "plm.pm.requirementDocuments.update.basicinfo":
                    convertedString = getRequirementDocumentBasicInfoUpdatedString(messageString, actor, requirementDocument, requirementDocumentRevision, as);
                    break;
                case "plm.pm.requirementDocuments.update.attributes":
                    convertedString = getRequirementDocumentAttributeUpdatedString(messageString, actor, requirementDocument, requirementDocumentRevision, as);
                    break;
                case "plm.pm.requirementDocuments.files.add":
                    convertedString = getRequirementDocumentFilesAddedString(messageString, requirementDocument, requirementDocumentRevision, as);
                    break;
                case "plm.pm.requirementDocuments.files.delete":
                    convertedString = getRequirementDocumentFilesDeletedString(messageString, requirementDocument, requirementDocumentRevision, as);
                    break;
                case "plm.pm.requirementDocuments.files.version":
                    convertedString = getRequirementDocumentFilesVersionedString(messageString, requirementDocument, requirementDocumentRevision, as);
                    break;
                case "plm.pm.requirementDocuments.files.rename":
                    convertedString = getRequirementDocumentFileRenamedString(messageString, requirementDocument, requirementDocumentRevision, as);
                    break;
                case "plm.pm.requirementDocuments.files.replace":
                    convertedString = getRequirementDocumentFileRenamedString(messageString, requirementDocument, requirementDocumentRevision, as);
                    break;
                case "plm.pm.requirementDocuments.files.lock":
                    convertedString = getRequirementDocumentFileString(messageString, requirementDocument, requirementDocumentRevision, as);
                    break;
                case "plm.pm.requirementDocuments.files.unlock":
                    convertedString = getRequirementDocumentFileString(messageString, requirementDocument, requirementDocumentRevision, as);
                    break;
                case "plm.pm.requirementDocuments.files.folders.add":
                    convertedString = getRequirementDocumentFoldersAddedOrDeletedString(messageString, requirementDocument, requirementDocumentRevision, as);
                    break;
                case "plm.pm.requirementDocuments.files.folders.delete":
                    convertedString = getRequirementDocumentFoldersAddedOrDeletedString(messageString, requirementDocument, requirementDocumentRevision, as);
                    break;
                case "plm.pm.requirementDocuments.files.download":
                    convertedString = getRequirementDocumentFileString(messageString, requirementDocument, requirementDocumentRevision, as);
                    break;
                case "plm.pm.requirementDocuments.workflow.start":
                    convertedString = getRequirementDocumentWorkflowStartString(messageString, requirementDocument,requirementDocumentRevision, as);
                    break;   
                case "plm.pm.requirementDocuments.workflow.promote":
                    convertedString = getRequirementDocumentWorkflowPromoteDemoteString(messageString, requirementDocument,requirementDocumentRevision, as);
                    break;
                case "plm.pm.requirementDocuments.workflow.demote":
                    convertedString = getRequirementDocumentWorkflowPromoteDemoteString(messageString, requirementDocument,requirementDocumentRevision, as);
                    break; 
                case "plm.pm.requirementDocuments.workflow.finish":
                    convertedString = getRequirementDocumentWorkflowFinishString(messageString, requirementDocument,requirementDocumentRevision, as);
                    break;  
                case "plm.pm.requirementDocuments.workflow.hold":
                    convertedString = getRequirementDocumentWorkflowHoldUnholdString(messageString, requirementDocument,requirementDocumentRevision, as);
                    break; 
                case "plm.pm.requirementDocuments.workflow.unhold":
                    convertedString = getRequirementDocumentWorkflowHoldUnholdString(messageString, requirementDocument,requirementDocumentRevision, as);
                    break;                  
                case "plm.pm.requirementDocuments.lifeCyclePhase.promote":
                    convertedString = getRequirementDocumentPromoteDemoteString(messageString, requirementDocument, requirementDocumentRevision, as);
                    break;
                case "plm.pm.requirementDocuments.lifeCyclePhase.demote":
                    convertedString = getRequirementDocumentPromoteDemoteString(messageString, requirementDocument, requirementDocumentRevision, as);
                    break;
                case "plm.pm.requirementDocuments.requirement.add":
                    convertedString = getRequirementAddedString(messageString, requirementDocumentRevision, as);
                    break;
                case "plm.pm.requirementDocuments.reviewer.add":
                    convertedString = getReqDocumentReviewerAddedString(messageString, requirementDocumentRevision, as);
                    break;
                case "plm.pm.requirementDocuments.reviewer.change":
                    convertedString = getReqDocumentReviewerUpdateString(messageString, requirementDocumentRevision, as);
                    break;
                case "plm.pm.requirementDocuments.reviewer.delete":
                    convertedString = getReqDocumentReviewerDeletedString(messageString, requirementDocumentRevision, as);
                    break;
                case "plm.pm.requirementDocuments.reviewer.approved":
                    convertedString = getReqDocumentReviewerApprovedString(messageString, requirementDocumentRevision, as);
                    break;
                case "plm.pm.requirementDocuments.requirement.approved":
                    convertedString = getReqDocumentRequirementsApprovedString(messageString, requirementDocumentRevision, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getRequirementDocumentCreatedString(String messageString, Person actor, PLMRequirementDocument requirementDocument) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), requirementDocument.getNumber());
    }

    private String getRequirementDocumentRevisionCreatedString(String messageString, Person actor, PLMRequirementDocument requirementDocument, PLMRequirementDocumentRevision requirementDocumentRevision) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), requirementDocument.getNumber(),
                requirementDocumentRevision.getRevision(), requirementDocumentRevision.getLifeCyclePhase().getPhase());
    }

    private String getRequirementDocumentBasicInfoUpdatedJson(PLMRequirementDocument plmOldRequirementDocument, PLMRequirementDocument plmRequirementDocument) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = plmOldRequirementDocument.getName();
        String newValue = plmRequirementDocument.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        oldValue = plmOldRequirementDocument.getDescription();
        newValue = plmRequirementDocument.getDescription();
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
        if (changes.size() > 0) {
            try {
                json = objectMapper.writeValueAsString(changes);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        return json;
    }

    private String getRequirementDocumentBasicInfoUpdatedString(String messageString, Person actor, PLMRequirementDocument plmRequirementDocument, PLMRequirementDocumentRevision plmRequirementDocumentRevision, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), plmRequirementDocument.getNumber(),
                plmRequirementDocumentRevision.getRevision(), plmRequirementDocumentRevision.getLifeCyclePhase().getPhase());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.pm.requirementDocuments.update.basicinfo.property");

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

    private String getRequirementDocumentAttributesUpdatedJson(PLMRequirementObjectAttribute oldAttribute, PLMRequirementObjectAttribute newAttribute) {
        List<ASAttributeChangeDTO> changes = new ArrayList<>();

        String oldValue = "";

        if (oldAttribute != null) {
            oldValue = oldAttribute.getValueAsString();
        }
        String newValue = newAttribute.getValueAsString();

        PLMRequirementObjectTypeAttribute attDef = requirementObjectTypeAttributeRepository.findOne(newAttribute.getId().getAttributeDef());
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

    private String getRequirementDocumentAttributeUpdatedString(String messageString, Person actor, PLMRequirementDocument plmRequirementDocument, PLMRequirementDocumentRevision plmRequirementDocumentRevision, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), plmRequirementDocument.getNumber(),
                plmRequirementDocumentRevision.getRevision(), plmRequirementDocumentRevision.getLifeCyclePhase().getPhase());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.pm.requirementDocuments.update.attributes.attribute");

        String json = as.getData();
        try {
            List<ASAttributeChangeDTO> propChanges = objectMapper.readValue(json, new TypeReference<List<ASAttributeChangeDTO>>() {
            });
            propChanges.forEach(p -> {
                String s = addMarginToMessage(MessageFormat.format(updateString,
                        highlightValue(p.getAttribute()),
                        highlightValue(p.getOldValue()),
                        highlightValue(p.getNewValue())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getRequirementDocumentCopiedString(String messageString, ActivityStream as) {
        ActivityStreamObject object = as.getObject();
        ActivityStreamObject source = as.getSource();

        PLMRequirementDocumentRevision plmRequirementDocumentRevision = requirementDocumentRevisionRepository.findOne(object.getObject());
        PLMRequirementDocumentRevision sourcePlmRequirementDocumentRevision = requirementDocumentRevisionRepository.findOne(source.getObject());

        PLMRequirementDocument plmRequirementDocument = requirementDocumentRepository.findOne(plmRequirementDocumentRevision.getMaster().getId());
        PLMRequirementDocument sourcePlmRequirementDocument = requirementDocumentRepository.findOne(sourcePlmRequirementDocumentRevision.getMaster().getId());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(), plmRequirementDocument.getNumber(), highlightValue(sourcePlmRequirementDocument.getNumber()));
    }

    private String getRequirementDocumentFilesAddedJson(List<PLMRequirementDocumentFile> files) {
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
    private String getRequirementDocumentFoldersAddedJson(PLMRequirementDocumentFile file) {
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

    private String getRequirementDocumentFoldersDeletedJson(PLMRequirementDocumentFile file) {
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



    private String getRequirementDocumentFilesAddedString(String messageString, PLMRequirementDocument plmRequirementDocument, PLMRequirementDocumentRevision plmRequirementDocumentRevision, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), plmRequirementDocument.getNumber(),
                plmRequirementDocumentRevision.getRevision(), plmRequirementDocumentRevision.getLifeCyclePhase().getPhase());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.pm.requirementDocuments.files.add.file");

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

    private String getRequirementDocumentFilesDeletedString(String messageString, PLMRequirementDocument plmRequirementDocument, PLMRequirementDocumentRevision plmRequirementDocumentRevision, ActivityStream as) {
        String activityString = "";

        String json = as.getData();
        try {
            ASNewFileDTO asNewFileDTO = objectMapper.readValue(json, new TypeReference<ASNewFileDTO>() {
            });
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(asNewFileDTO.getName()),
                    plmRequirementDocument.getNumber(), plmRequirementDocumentRevision.getRevision(), plmRequirementDocumentRevision.getLifeCyclePhase().getPhase());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return activityString;
    }


    private String getRequirementDocumentFilesVersionedJson(List<PLMRequirementDocumentFile> files) {
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

    private String getRequirementDocumentFilesVersionedString(String messageString, PLMRequirementDocument plmRequirementDocument, PLMRequirementDocumentRevision plmRequirementDocumentRevision, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), plmRequirementDocument.getNumber(),
                plmRequirementDocumentRevision.getRevision(), plmRequirementDocumentRevision.getLifeCyclePhase().getPhase());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.pm.requirementDocuments.files.version.file");

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

    private String getRequirementDocumentFileRenamedString(String messageString, PLMRequirementDocument plmRequirementDocument, PLMRequirementDocumentRevision plmRequirementDocumentRevision, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    plmRequirementDocument.getNumber(),
                    plmRequirementDocumentRevision.getRevision(),
                    plmRequirementDocumentRevision.getLifeCyclePhase().getPhase());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getRequirementDocumentFileString(String messageString, PLMRequirementDocument plmRequirementDocument, PLMRequirementDocumentRevision plmRequirementDocumentRevision, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    plmRequirementDocument.getNumber(),
                    plmRequirementDocumentRevision.getRevision(),
                    plmRequirementDocumentRevision.getLifeCyclePhase().getPhase());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }


    private String getRequirementDocumentFoldersAddedOrDeletedString(String messageString, PLMRequirementDocument plmRequirementDocument, PLMRequirementDocumentRevision plmRequirementDocumentRevision, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), plmRequirementDocument.getNumber(),
                    plmRequirementDocumentRevision.getRevision(), plmRequirementDocumentRevision.getLifeCyclePhase().getPhase());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getRequirementDocumentPromoteDemoteString(String messageString, PLMRequirementDocument requirementDocument, PLMRequirementDocumentRevision requirementDocumentRevision, ActivityStream as) {
        ActivityStreamObject source = as.getSource();
        ActivityStreamObject target = as.getTarget();

        PLMLifeCyclePhase fromLifeCyclePhase = lifeCyclePhaseRepository.findOne(source.getObject());
        PLMLifeCyclePhase toLifeCyclePhase = lifeCyclePhaseRepository.findOne(target.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(requirementDocument.getNumber()),
                highlightValue(requirementDocumentRevision.getRevision()),
                highlightValue(fromLifeCyclePhase.getPhase()),
                highlightValue(toLifeCyclePhase.getPhase()));
    }

    private String getRequirementAddedJson(PLMRequirement requirement) {
        List<ASNewMfrPartDTO> asNewMfrPartDTOs = new ArrayList<>();
        //PLMRequirementDocumentRevision revision = requirement.getRequirementDocumentRevision();
        ASNewMfrPartDTO asNewMfrPartDTO = new ASNewMfrPartDTO(requirement.getId(), requirement.getNumber(), requirement.getName(), null/*, revision.getName()*/);
        asNewMfrPartDTOs.add(asNewMfrPartDTO);
        String json = null;
        try {
            json = objectMapper.writeValueAsString(asNewMfrPartDTOs);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getRequirementAddedString(String messageString, PLMRequirementDocumentRevision revision, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), revision.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.pm.requirementDocuments.requirement.add.requirement");

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

    private String getRequirementDocumentWorkflowStartString(String messageString, PLMRequirementDocument requirementDocument, PLMRequirementDocumentRevision requirementDocumentRevision, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                requirementDocument.getNumber());
    }

    private String getRequirementDocumentWorkflowPromoteDemoteString(String messageString, PLMRequirementDocument requirementDocument, PLMRequirementDocumentRevision requirementDocumentRevision, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();
        ActivityStreamObject target = as.getTarget();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus fromActivity = workflowStatusRepository.findOne(source.getObject());
        PLMWorkflowStatus toActivity = workflowStatusRepository.findOne(target.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                requirementDocument.getNumber(),
                highlightValue(fromActivity.getName()),
                highlightValue(toActivity.getName()));
    }

    private String getRequirementDocumentWorkflowFinishString(String messageString, PLMRequirementDocument requirementDocument, PLMRequirementDocumentRevision requirementDocumentRevision, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                requirementDocument.getNumber());
    }

    private String getRequirementDocumentWorkflowHoldUnholdString(String messageString, PLMRequirementDocument requirementDocument, PLMRequirementDocumentRevision requirementDocumentRevision, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus plmWorkflowActivity = workflowStatusRepository.findOne(source.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflowActivity.getName()),
                highlightValue(plmWorkflow.getName()),
                requirementDocument.getNumber());
    }


    private String getReqDocumentReviewerAddedJson(PLMRequirementDocumentRevision revision, PLMRequirementDocumentReviewer reviewer) {
        List<ASNewReviewerDTO> asNewReviewerDTOs = new ArrayList<>();
        String type = "reviewer";
        Person person = personRepository.findOne(reviewer.getReviewer());
        ASNewReviewerDTO asNewReviewerDTO = new ASNewReviewerDTO(person.getId(), person.getFullName(), type, revision.getName());
        asNewReviewerDTOs.add(asNewReviewerDTO);
        String json = null;
        try {
            json = objectMapper.writeValueAsString(asNewReviewerDTOs);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getReqDocumentReviewerAddedString(String messageString, PLMRequirementDocumentRevision revision, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), revision.getMaster().getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.pm.requirementDocuments.reviewer.add.reviewer");

        String json = as.getData();
        try {
            List<ASNewReviewerDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewReviewerDTO>>() {
            });
            files.forEach(f -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(f.getFullName()), highlightValue(f.getType())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return sb.toString();

    }


    private String getReqDocumentReviewerUpdateJson(PLMRequirementDocumentRevision revision, PLMRequirementDocumentReviewer reviewer) {
        List<ASNewReviewerDTO> asNewReviewerDTOs = new ArrayList<>();
        String type = null;
        if (!reviewer.getApprover()) {
            type = "reviewer";
        } else {
            type = "approver";
        }
        Person person = personRepository.findOne(reviewer.getReviewer());
        ASNewReviewerDTO asNewReviewerDTO = new ASNewReviewerDTO(person.getId(), person.getFullName(), type, revision.getName());
        asNewReviewerDTOs.add(asNewReviewerDTO);
        String json = null;
        try {
            json = objectMapper.writeValueAsString(asNewReviewerDTOs);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getReqDocumentReviewerUpdateString(String messageString, PLMRequirementDocumentRevision revision, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), revision.getMaster().getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.pm.requirementDocuments.reviewer.change.reviewer");

        String json = as.getData();
        try {
            List<ASNewReviewerDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewReviewerDTO>>() {
            });
            files.forEach(f -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(f.getFullName()), highlightValue(f.getType())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private String getReqDocumentReviewerDeletedString(String messageString, PLMRequirementDocumentRevision revision, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            ASNewReviewerDTO reviewer = objectMapper.readValue(json, new TypeReference<ASNewReviewerDTO>() {
            });
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(reviewer.getType()),
                    highlightValue(reviewer.getFullName()), highlightValue(revision.getMaster().getNumber()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getReqDocumentReviewerApprovedString(String messageString, PLMRequirementDocumentRevision revision, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            ASNewReviewerDTO reviewer = objectMapper.readValue(json, new TypeReference<ASNewReviewerDTO>() {
            });
            activityString = MessageFormat.format(messageString, highlightValue(reviewer.getFullName()), highlightValue(reviewer.getType()),
                    highlightValue(revision.getMaster().getNumber()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getReqDocumentRequirementsApprovedString(String messageString, PLMRequirementDocumentRevision revision, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            ASNewReviewerDTO reviewer = objectMapper.readValue(json, new TypeReference<ASNewReviewerDTO>() {
            });
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(reviewer.getType()),
                    highlightValue(revision.getMaster().getNumber()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }



}
