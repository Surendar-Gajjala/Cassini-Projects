package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.RequirementEvents;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.req.*;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.repo.req.PLMRequirementObjectTypeAttributeRepository;
import com.cassinisys.plm.repo.req.PLMRequirementRepository;
import com.cassinisys.plm.repo.req.PLMRequirementVersionRepository;
import com.cassinisys.plm.repo.req.RequirementDocumentChildrenRepository;
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
import java.util.LinkedList;
import java.util.List;

@Component
public class RequirementsActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PLMRequirementRepository requirementRepository;
    @Autowired
    private PLMRequirementVersionRepository requirementVersionRepository;
    @Autowired
    private PLMRequirementObjectTypeAttributeRepository requirementObjectTypeAttributeRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private RequirementDocumentChildrenRepository requirementDocumentChildrenRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private PLMWorkflowRepository workFlowRepository;
    @Autowired
    private PLMWorkflowStatusRepository workflowStatusRepository;

    @Async
    @EventListener
    public void requirementCreated(RequirementEvents.RequirementCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getRequirementDocumentChildren().getId());
        object.setType("requirement");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirements.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void requirementBasicInfoUpdated(RequirementEvents.RequirementBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PLMRequirementVersion oldRequirement = event.getOldVersion();
        PLMRequirementVersion newRequirement = event.getVersion();
        PLMRequirementDocumentChildren children = event.getRequirementDocumentChildren();

        object.setObject(children.getId());
        object.setType("requirement");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirements.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getRequirementBasicInfoUpdatedJson(oldRequirement, newRequirement));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void requirementAttributesUpdated(RequirementEvents.RequirementAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PLMRequirement plmRequirementVersion = event.getRequirement();

        PLMRequirementObjectAttribute oldAtt = event.getOldAttribute();
        PLMRequirementObjectAttribute newAtt = event.getNewAttribute();

        object.setObject(plmRequirementVersion.getId());
        object.setType("requirement");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirements.update.attributes");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getRequirementAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void requirementFilesAdded(RequirementEvents.RequirementFilesAddedEvent event) {
        List<PLMRequirementFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirements.files.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDocumentChildren().getId());
        object.setType("requirement");
        as.setObject(object);
        as.setData(getRequirementFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void requirementFileDeleted(RequirementEvents.RequirementFileDeletedEvent event) {
        PLMRequirementFile file = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirements.files.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDocumentChildren().getId());
        object.setType("requirement");
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
    public void requirementFilesVersioned(RequirementEvents.RequirementFilesVersionedEvent event) {
        List<PLMRequirementFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirements.files.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDocumentChildren().getId());
        object.setType("requirement");
        as.setObject(object);
        as.setData(getRequirementFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void requirementFileRenamed(RequirementEvents.RequirementFileRenamedEvent event) {
        PLMRequirementFile oldFile = event.getOldFile();
        PLMRequirementFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirements.files.rename");
        if (event.getType().equals("Rename")) as.setActivity("plm.pm.requirements.files.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.pm.requirements.files.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDocumentChildren().getId());
        object.setType("requirement");
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
    public void requirementFileLocked(RequirementEvents.RequirementFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirements.files.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        PLMRequirementDocumentChildren children = requirementDocumentChildrenRepository.findByRequirementVersion(event.getRequirementVersion());
        object.setObject(children.getId());
        object.setType("requirement");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getRequirementFile().getId(), event.getRequirementFile().getName(), FileUtils.byteCountToDisplaySize(event.getRequirementFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void requirementFileUnlocked(RequirementEvents.RequirementFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirements.files.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        PLMRequirementDocumentChildren children = requirementDocumentChildrenRepository.findByRequirementVersion(event.getRequirementVersion());
        object.setObject(children.getId());
        object.setType("requirement");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getRequirementFile().getId(), event.getRequirementFile().getName(), FileUtils.byteCountToDisplaySize(event.getRequirementFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void requirementFileDownloaded(RequirementEvents.RequirementFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirements.files.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        PLMRequirementDocumentChildren children = requirementDocumentChildrenRepository.findByRequirementVersion(event.getRequirementVersion());
        object.setObject(children.getId());
        object.setType("requirement");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getRequirementFile().getId(), event.getRequirementFile().getName(), FileUtils.byteCountToDisplaySize(event.getRequirementFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }
    @Async
    @EventListener
    public void requirementFoldersAdded(RequirementEvents.RequirementFoldersAddedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirements.files.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDocumentChildren().getId());
        object.setType("requirement");
        as.setObject(object);
        as.setData(getReqFoldersAddedJson(files));

        activityStreamService.create(as);       
    }                                      

    @Async
    @EventListener
    public void requirementFoldersDeleted(RequirementEvents.RequirementFoldersDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirements.files.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDocumentChildren().getId());
        object.setType("requirement");
        as.setObject(object);
        as.setData(getReqFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void requirementdded(RequirementEvents.RequirementItemAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirements.items.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getRequirementDocumentChildren().getId());
        object.setType("requirement");
        as.setObject(object);
        as.setData(getRequirementItemAddedJson(event.getRequirementItems()));

        activityStreamService.create(as);
    }


    @Async
    @EventListener
    public void projectReqDocumentDeleted(RequirementEvents.RequirementItemDeletedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirements.items.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getRequirementDocumentChildren().getId());
        object.setType("requirement");
        as.setObject(object);
        String json = null;
        PLMItem item = itemRepository.findOne(event.getRequirementItem().getItem().getItemMaster());
        ASReqItemDto dto = new ASReqItemDto(event.getRequirementItem().getItem().getId(), item.getItemNumber(), item.getItemName(),
                event.getRequirementItem().getItem().getRevision(), event.getRequirementItem().getItem().getLifeCyclePhase().getPhase());
        try {
            json = objectMapper.writeValueAsString(dto);
            as.setData(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }


    @Async
    @EventListener
    public void requirementReviewersAdded(RequirementEvents.RequirementReviewerAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirements.reviewer.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getRequirementDocumentChildren().getId());
        object.setType("requirement");
        as.setObject(object);
        as.setData(getReqReviewerAddedJson(event.getRequirementVersion(), event.getReviewer()));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void requirementReviewersUpdate(RequirementEvents.RequirementReviewerUpdateEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirements.reviewer.change");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getRequirementDocumentChildren().getId());
        object.setType("requirement");
        as.setObject(object);
        as.setData(getReqReviewerUpdateJson(event.getRequirementVersion(), event.getReviewer()));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void requirementReviewersDeleted(RequirementEvents.RequirementReviewerDeletedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirements.reviewer.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getRequirementDocumentChildren().getId());
        object.setType("requirement");
        as.setObject(object);
        Person person = personRepository.findOne(event.getReviewer().getReviewer());
        try {
            as.setData(objectMapper.writeValueAsString(new ASNewReviewerDTO(event.getReviewer().getId(), person.getFullName(), "reviewer", event.getRequirementVersion().getMaster().getNumber())));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void requirementReviewerApproved(RequirementEvents.RequirementReviewerApprovedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirements.reviewer.approved");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getRequirementDocumentChildren().getId());
        object.setType("requirement");
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
            as.setData(objectMapper.writeValueAsString(new ASNewReviewerDTO(event.getReviewer().getId(), person.getFullName(), type, event.getRequirementVersion().getMaster().getNumber())));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void requirementWorkflowStarted(RequirementEvents.RequirementWorkflowStartedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirements.workflow.start");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getRequirementDocumentChildren().getId());
        object.setType("requirement");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void requirementWorkflowFinished(RequirementEvents.RequirementWorkflowFinishedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirements.workflow.finish");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getRequirementDocumentChildren().getId());
        object.setType("requirement");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void requirementWorkflowPromoted(RequirementEvents.RequirementWorkflowPromotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirements.workflow.promote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getRequirementDocumentChildren().getId());
        object.setType("requirement");
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
    public void requirementWorkflowDemoted(RequirementEvents.RequirementWorkflowDemotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirements.workflow.demote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getRequirementDocumentChildren().getId());
        object.setType("requirement");
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
    public void requirementWorkflowHold(RequirementEvents.RequirementWorkflowHoldEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirements.workflow.hold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getRequirementDocumentChildren().getId());
        object.setType("requirement");
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
    public void requirementWorkflowUnhold(RequirementEvents.RequirementWorkflowUnholdEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pm.requirements.workflow.unhold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getRequirementDocumentChildren().getId());
        object.setType("requirement");
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

    @Override
    public String getConverterKey() {
        return "plm.pm.requirements";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            PLMRequirementDocumentChildren children = requirementDocumentChildrenRepository.findOne(object.getObject());
            PLMRequirementVersion requirementVersion = children.getRequirementVersion();

            if (requirementVersion == null || requirementVersion.getMaster() == null) return "";

            PLMRequirement requirement = requirementRepository.findOne(requirementVersion.getMaster().getId());

            if (requirement == null) return "";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.pm.requirements.create":
                    convertedString = getRequirementCreatedString(messageString, actor, requirement);
                    break;
                case "plm.pm.requirements.revision":
                    convertedString = getRequirementVersionCreatedString(messageString, actor, requirement, requirementVersion);
                    break;
                case "plm.pm.requirements.update.basicinfo":
                    convertedString = getRequirementBasicInfoUpdatedString(messageString, actor, requirement, requirementVersion, as);
                    break;
                case "plm.pm.requirements.update.attributes":
                    convertedString = getRequirementAttributeUpdatedString(messageString, actor, requirement, requirementVersion, as);
                    break;
                case "plm.pm.requirements.files.add":
                    convertedString = getRequirementFilesAddedString(messageString, requirement, requirementVersion, as);
                    break;
                case "plm.pm.requirements.files.delete":
                    convertedString = getRequirementFilesDeletedString(messageString, requirement, requirementVersion, as);
                    break;
                case "plm.pm.requirements.files.version":
                    convertedString = getRequirementFilesVersionedString(messageString, requirement, requirementVersion, as);
                    break;
                case "plm.pm.requirements.files.rename":
                    convertedString = getRequirementFileRenamedString(messageString, requirement, requirementVersion, as);
                    break;
                case "plm.pm.requirements.files.replace":
                    convertedString = getRequirementFileRenamedString(messageString, requirement, requirementVersion, as);
                    break;
                case "plm.pm.requirements.files.lock":
                    convertedString = getRequirementFileString(messageString, requirement, requirementVersion, as);
                    break;
                case "plm.pm.requirements.files.unlock":
                    convertedString = getRequirementFileString(messageString, requirement, requirementVersion, as);
                    break;
                case "plm.pm.requirements.files.folders.add":
                    convertedString = getRequirementFoldersAddedOrDeletedString(messageString, requirement, requirementVersion, as);
                    break;
                case "plm.pm.requirements.files.folders.delete":
                    convertedString = getRequirementFoldersAddedOrDeletedString(messageString, requirement, requirementVersion, as);
                    break;
                case "plm.pm.requirements.files.download":
                    convertedString = getRequirementFileString(messageString, requirement, requirementVersion, as);
                    break;
                case "plm.pm.requirements.reviewer.add":
                    convertedString = getReqReviewerAddedString(messageString, requirementVersion, as);
                    break;
                case "plm.pm.requirements.reviewer.change":
                    convertedString = getReqReviewerUpdateString(messageString, requirementVersion, as);
                    break;
                case "plm.pm.requirements.reviewer.delete":
                    convertedString = getReqReviewerDeletedString(messageString, requirementVersion, as);
                    break;
                case "plm.pm.requirements.reviewer.approved":
                    convertedString = getReqReviewerApprovedString(messageString, requirementVersion, as);
                    break;
                case "plm.pm.requirements.items.add":
                    convertedString = getRequirementItemAddedString(messageString, actor, requirementVersion, as);
                    break;
                case "plm.pm.requirements.items.delete":
                    convertedString = getRequirementItemDeletedString(messageString, actor, requirementVersion, as);
                    break;
                case "plm.pm.requirements.workflow.start":
                    convertedString = getRequirementWorkflowStartString(messageString, requirement,requirementVersion, as);
                    break;
                case "plm.pm.requirements.workflow.finish":
                    convertedString = getRequirementWorkflowFinishString(messageString, requirement,requirementVersion, as);
                    break;
                case "plm.pm.requirements.workflow.promote":
                    convertedString = getRequirementWorkflowPromoteDemoteString(messageString, requirement,requirementVersion, as);
                    break;
                case "plm.pm.requirements.workflow.demote":
                    convertedString = getRequirementWorkflowPromoteDemoteString(messageString, requirement,requirementVersion, as);
                    break;
                case "plm.pm.requirements.workflow.hold":
                    convertedString = getRequirementWorkflowHoldUnholdString(messageString, requirement,requirementVersion, as);
                    break;
                case "plm.pm.requirements.workflow.unhold":
                    convertedString = getRequirementWorkflowHoldUnholdString(messageString, requirement,requirementVersion, as);
                    break;
                case "plm.pm.requirements.workflow.add":
                    convertedString = getRequirementWorkflowAddedString(messageString, requirement,requirementVersion, as);
                    break;
                case "plm.pm.requirements.workflow.change":
                    convertedString = getRequirementWorkflowChangeString(messageString, requirement,requirementVersion, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getRequirementCreatedString(String messageString, Person actor, PLMRequirement requirement) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), requirement.getNumber());
    }

    private String getRequirementVersionCreatedString(String messageString, Person actor, PLMRequirement requirement, PLMRequirementVersion requirementVersion) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), requirement.getNumber(),
                requirementVersion.getVersion(), requirementVersion.getLifeCyclePhase().getPhase());
    }

    private String getRequirementBasicInfoUpdatedJson(PLMRequirementVersion plmOldRequirement, PLMRequirementVersion plmRequirement) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = plmOldRequirement.getName();
        String newValue = plmRequirement.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        oldValue = plmOldRequirement.getDescription();
        newValue = plmRequirement.getDescription();
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

    private String getRequirementBasicInfoUpdatedString(String messageString, Person actor, PLMRequirement plmRequirement, PLMRequirementVersion plmRequirementVersion, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), plmRequirement.getNumber(),
                plmRequirementVersion.getVersion(), plmRequirementVersion.getLifeCyclePhase().getPhase());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.pm.requirements.update.basicinfo.property");

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

    private String getRequirementAttributesUpdatedJson(PLMRequirementObjectAttribute oldAttribute, PLMRequirementObjectAttribute newAttribute) {
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

    private String getRequirementAttributeUpdatedString(String messageString, Person actor, PLMRequirement plmRequirement, PLMRequirementVersion plmRequirementVersion, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), plmRequirement.getNumber(),
                plmRequirementVersion.getVersion(), plmRequirementVersion.getLifeCyclePhase().getPhase());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.pm.requirements.update.attributes.attribute");

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
    private String getReqFoldersAddedJson(PLMFile file) {
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

    private String getRequirementCopiedString(String messageString, ActivityStream as) {
        ActivityStreamObject object = as.getObject();
        ActivityStreamObject source = as.getSource();

        PLMRequirementVersion plmRequirementVersion = requirementVersionRepository.findOne(object.getObject());
        PLMRequirementVersion sourcePlmRequirementVersion = requirementVersionRepository.findOne(source.getObject());

        PLMRequirement plmRequirement = requirementRepository.findOne(plmRequirementVersion.getMaster().getId());
        PLMRequirement sourcePlmRequirement = requirementRepository.findOne(sourcePlmRequirementVersion.getMaster().getId());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(), plmRequirement.getNumber(), highlightValue(sourcePlmRequirement.getNumber()));
    }

    private String getRequirementFilesAddedJson(List<PLMRequirementFile> files) {
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

    private String getRequirementFilesAddedString(String messageString, PLMRequirement plmRequirement, PLMRequirementVersion plmRequirementVersion, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), plmRequirement.getNumber(),
                plmRequirementVersion.getVersion(), plmRequirementVersion.getLifeCyclePhase().getPhase());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.pm.requirements.files.add.file");

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

    private String getRequirementFilesDeletedString(String messageString, PLMRequirement plmRequirement, PLMRequirementVersion plmRequirementVersion, ActivityStream as) {
        String activityString = "";

        String json = as.getData();
        try {
            ASNewFileDTO asNewFileDTO = objectMapper.readValue(json, new TypeReference<ASNewFileDTO>() {
            });
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(asNewFileDTO.getName()),
                    plmRequirement.getNumber(), plmRequirementVersion.getVersion(), plmRequirementVersion.getLifeCyclePhase().getPhase());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return activityString;
    }


    private String getRequirementFilesVersionedJson(List<PLMRequirementFile> files) {
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

    private String getRequirementFilesVersionedString(String messageString, PLMRequirement plmRequirement, PLMRequirementVersion plmRequirementVersion, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), plmRequirement.getNumber(),
                plmRequirementVersion.getVersion(), plmRequirementVersion.getLifeCyclePhase().getPhase());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.pm.requirements.files.version.file");

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

    private String getRequirementFileRenamedString(String messageString, PLMRequirement plmRequirement, PLMRequirementVersion plmRequirementVersion, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    plmRequirement.getNumber(),
                    plmRequirementVersion.getVersion(),
                    plmRequirementVersion.getLifeCyclePhase().getPhase());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getRequirementFileString(String messageString, PLMRequirement plmRequirement, PLMRequirementVersion plmRequirementVersion, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    plmRequirement.getNumber(),
                    plmRequirementVersion.getVersion(),
                    plmRequirementVersion.getLifeCyclePhase().getPhase());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getReqFoldersDeletedJson(PLMFile file) {
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

    private String getRequirementFoldersAddedOrDeletedString(String messageString, PLMRequirement plmRequirement, PLMRequirementVersion plmRequirementVersion, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), plmRequirement.getNumber(),
                    plmRequirementVersion.getVersion(), plmRequirementVersion.getLifeCyclePhase().getPhase());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getReqReviewerAddedJson(PLMRequirementVersion version, PLMRequirementReviewer reviewer) {
        List<ASNewReviewerDTO> asNewReviewerDTOs = new ArrayList<>();
        String type = "reviewer";
        Person person = personRepository.findOne(reviewer.getReviewer());
        ASNewReviewerDTO asNewReviewerDTO = new ASNewReviewerDTO(person.getId(), person.getFullName(), type, version.getName());
        asNewReviewerDTOs.add(asNewReviewerDTO);
        String json = null;
        try {
            json = objectMapper.writeValueAsString(asNewReviewerDTOs);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getReqReviewerAddedString(String messageString, PLMRequirementVersion version, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), version.getMaster().getNumber());

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


    private String getReqReviewerUpdateJson(PLMRequirementVersion version, PLMRequirementReviewer reviewer) {
        List<ASNewReviewerDTO> asNewReviewerDTOs = new ArrayList<>();
        String type = null;
        if (!reviewer.getApprover()) {
            type = "reviewer";
        } else {
            type = "approver";
        }
        Person person = personRepository.findOne(reviewer.getReviewer());
        ASNewReviewerDTO asNewReviewerDTO = new ASNewReviewerDTO(person.getId(), person.getFullName(), type, version.getName());
        asNewReviewerDTOs.add(asNewReviewerDTO);
        String json = null;
        try {
            json = objectMapper.writeValueAsString(asNewReviewerDTOs);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }


    private String getRequirementItemAddedJson(List<PLMRequirementItem> requirementItems) {
        String json = null;
        List<ASReqItemDto> dtos = new LinkedList<>();
        requirementItems.forEach(requirementItem -> {
            PLMItem item = itemRepository.findOne(requirementItem.getItem().getItemMaster());
            dtos.add(new ASReqItemDto(requirementItem.getItem().getId(), item.getItemNumber(), item.getItemName(), requirementItem.getItem().getRevision(), requirementItem.getItem().getLifeCyclePhase().getPhase()));
        });
        try {
            json = objectMapper.writeValueAsString(dtos);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }


    private String getReqReviewerUpdateString(String messageString, PLMRequirementVersion version, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), version.getMaster().getNumber());

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

    private String getRequirementItemAddedString(String messageString, Person actor, PLMRequirementVersion version, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), highlightValue(version.getMaster().getName()));

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.pm.requirements.items.add.item");

        String json = as.getData();
        try {
            List<ASReqItemDto> reqItemDtos = objectMapper.readValue(json, new TypeReference<List<ASReqItemDto>>() {
            });
            reqItemDtos.forEach(f -> {
                String s = addMarginToMessage(MessageFormat.format(updateString, highlightValue(f.getItemNumber()), highlightValue(f.getRevision()), highlightValue(f.getPhase())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }


    private String getRequirementItemDeletedString(String messageString, Person actor, PLMRequirementVersion version, ActivityStream as) {
        String json = as.getData();
        String s = null;
        try {
            ASReqItemDto reqItemDto = objectMapper.readValue(json, new TypeReference<ASReqItemDto>() {
            });
            s = MessageFormat.format(messageString, highlightValue(actor.getFullName().trim()), highlightValue(reqItemDto.getItemNumber()),
                    highlightValue(reqItemDto.getRevision()), highlightValue(reqItemDto.getPhase()), highlightValue(version.getMaster().getName()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return s;
    }

    private String getReqReviewerDeletedString(String messageString, PLMRequirementVersion version, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            ASNewReviewerDTO reviewer = objectMapper.readValue(json, new TypeReference<ASNewReviewerDTO>() {
            });
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(reviewer.getType()),
                    highlightValue(reviewer.getFullName()), highlightValue(version.getMaster().getNumber()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getReqReviewerApprovedString(String messageString, PLMRequirementVersion version, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            ASNewReviewerDTO reviewer = objectMapper.readValue(json, new TypeReference<ASNewReviewerDTO>() {
            });
            activityString = MessageFormat.format(messageString, highlightValue(reviewer.getFullName()), highlightValue(reviewer.getType()),
                    highlightValue(version.getMaster().getNumber()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getRequirementWorkflowStartString(String messageString, PLMRequirement plmRequirement, PLMRequirementVersion plmRequirementVersion, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                plmRequirement.getNumber());
    }

    private String getRequirementWorkflowFinishString(String messageString, PLMRequirement plmRequirement, PLMRequirementVersion plmRequirementVersion, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                plmRequirement.getNumber());
    }

    private String getRequirementWorkflowPromoteDemoteString(String messageString, PLMRequirement plmRequirement, PLMRequirementVersion plmRequirementVersion, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();
        ActivityStreamObject target = as.getTarget();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus fromActivity = workflowStatusRepository.findOne(source.getObject());
        PLMWorkflowStatus toActivity = workflowStatusRepository.findOne(target.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                plmRequirement.getNumber(),
                highlightValue(fromActivity.getName()),
                highlightValue(toActivity.getName()));
    }

    private String getRequirementWorkflowHoldUnholdString(String messageString, PLMRequirement plmRequirement, PLMRequirementVersion plmRequirementVersion, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus plmWorkflowActivity = workflowStatusRepository.findOne(source.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflowActivity.getName()),
                highlightValue(plmWorkflow.getName()),
                plmRequirement.getNumber());
    }

    private String getRequirementWorkflowAddedString(String messageString, PLMRequirement plmRequirement, PLMRequirementVersion plmRequirementVersion, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getNewValue()),
                    plmRequirement.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getRequirementWorkflowChangeString(String messageString, PLMRequirement plmRequirement, PLMRequirementVersion plmRequirementVersion, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getOldValue()),
                    highlightValue(workflow.getNewValue()),
                    plmRequirementVersion.getVersion());  } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }
}
