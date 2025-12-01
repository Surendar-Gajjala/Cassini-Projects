package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.ActivityEvents;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.pm.PLMActivity;
import com.cassinisys.plm.model.pm.PLMActivityFile;
import com.cassinisys.plm.model.pm.PLMWbsElement;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.repo.pm.PLMActivityRepository;
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
import java.util.LinkedList;
import java.util.List;

@Component
public class ProjectActivityActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PLMWorkflowRepository workFlowRepository;
    @Autowired
    private PLMWorkflowStatusRepository workflowStatusRepository;
    @Autowired
    private PLMActivityRepository activityRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository workflowDefinitionRepository;

    @Async
    @EventListener
    public void activityCreated(ActivityEvents.ActivityCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getActivity().getId());
        object.setType("activity");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.activity.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void activityBasicInfoUpdated(ActivityEvents.ActivityBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PLMActivity oldActivity = event.getOldActivity();
        PLMActivity newActivity = event.getNewActivity();

        object.setObject(newActivity.getId());
        object.setType("activity");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.activity.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getActivityBasicInfoUpdatedJson(oldActivity, newActivity));
        if (as.getData() != null) activityStreamService.create(as);
    }


    @Async
    @EventListener
    public void activityFilesAdded(ActivityEvents.ActivityFilesAddedEvent event) {
        List<PLMActivityFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.activity.files.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getActivity().getId());
        object.setType("activity");
        as.setObject(object);
        as.setData(getActivityFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void activityFoldersAdded(ActivityEvents.ActivityFoldersAddedEvent event) {
        PLMActivityFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.activity.files.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getActivity().getId());
        object.setType("activity");
        as.setObject(object);
        as.setData(getActivityFoldersAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void activityFoldersDeleted(ActivityEvents.ActivityFoldersDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.activity.files.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getActivity().getId());
        object.setType("activity");
        as.setObject(object);
        as.setData(getActivityFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void activityFileDeleted(ActivityEvents.ActivityFileDeletedEvent event) {
        PLMActivityFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.activity.files.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getActivity().getId());
        object.setType("activity");
        as.setObject(object);
        as.setData(getActivityFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void activityFilesVersioned(ActivityEvents.ActivityFilesVersionedEvent event) {
        List<PLMActivityFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.activity.files.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getActivity().getId());
        object.setType("activity");
        as.setObject(object);
        as.setData(getActivityFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void activityFileRenamed(ActivityEvents.ActivityFileRenamedEvent event) {
        PLMActivityFile oldFile = event.getOldFile();
        PLMActivityFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals("Rename")) as.setActivity("plm.activity.files.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.activity.files.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getActivity().getId());
        object.setType("activity");
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
    public void activityFileLocked(ActivityEvents.ActivityFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.activity.files.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getActivity().getId());
        object.setType("activity");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getActivityFile().getId(), event.getActivityFile().getName(), FileUtils.byteCountToDisplaySize(event.getActivityFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void activityFileUnlocked(ActivityEvents.ActivityFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.activity.files.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getActivity().getId());
        object.setType("activity");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getActivityFile().getId(), event.getActivityFile().getName(), FileUtils.byteCountToDisplaySize(event.getActivityFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void activityFileDownloaded(ActivityEvents.ActivityFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.activity.files.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getActivity().getId());
        object.setType("activity");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getActivityFile().getId(), event.getActivityFile().getName(), FileUtils.byteCountToDisplaySize(event.getActivityFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void activityWorkflowStarted(ActivityEvents.ActivityWorkflowStartedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.activity.workflow.start");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getActivity().getId());
        object.setType("activity");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void activityWorkflowFinished(ActivityEvents.ActivityWorkflowFinishedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.activity.workflow.finish");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getActivity().getId());
        object.setType("activity");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void activityWorkflowPromoted(ActivityEvents.ActivityWorkflowPromotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.activity.workflow.promote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getActivity().getId());
        object.setType("activity");
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
    public void activityWorkflowDemoted(ActivityEvents.ActivityWorkflowDemotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.activity.workflow.demote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getActivity().getId());
        object.setType("activity");
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
    public void activityWorkflowHold(ActivityEvents.ActivityWorkflowHoldEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.activity.workflow.hold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getActivity().getId());
        object.setType("activity");
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
    public void activityWorkflowUnhold(ActivityEvents.ActivityWorkflowUnholdEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.activity.workflow.unhold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getActivity().getId());
        object.setType("activity");
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
    public void manufacturerPartWorkflowChange(ActivityEvents.ActivityWorkflowChangeEvent event) {
        ActivityStream as = new ActivityStream();
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getActivity().getId());
        object.setType("activity");
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
            as.setActivity("plm.activity.workflow.change");
        } else {
            as.setActivity("plm.activity.workflow.add");
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
    public void activityDeliverablesAdded(ActivityEvents.ActivityDeliverablesAddedEvent event) {
        List<PLMItem> deliverables = event.getActivityDeliverables();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.activity.deliverables.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getActivity().getId());
        object.setType("activity");
        as.setObject(object);
        as.setData(getActivityDeliverablesAddedJson(deliverables));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void activityDeliverableDeleted(ActivityEvents.ActivityDeliverableDeletedEvent event) {
        PLMItem deliverable = event.getActivityDeliverable();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.activity.deliverables.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getActivity().getId());
        object.setType("activity");
        as.setObject(object);
        as.setData(getActivityDeliverableDeletedJson(deliverable));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void activityDeliverableFinished(ActivityEvents.ActivityDeliverableFinishedEvent event) {
        PLMItem deliverable = event.getActivityDeliverable();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.activity.deliverables.finish");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getActivity().getId());
        object.setType("activity");
        as.setObject(object);
        as.setData(getActivityDeliverableDeletedJson(deliverable));

        activityStreamService.create(as);
    }


    @Async
    @EventListener
    public void activityReferenceItemsAdded(ActivityEvents.ActivityReferenceItemsAddedEvent event) {
        List<ASNewMemberDTO> itemReferences = event.getActivityItemReferences();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.activity.referenceitems.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getActivity().getId());
        object.setType("activity");
        as.setObject(object);
        as.setData(getActivityReferenceItemsAddedJson(itemReferences));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void activityReferenceItemDeleted(ActivityEvents.ActivityReferenceItemDeletedEvent event) {
        PLMItem itemReference = event.getActivityItemReference();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.activity.referenceitems.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getActivity().getId());
        object.setType("activity");
        as.setObject(object);
        as.setData(getActivityReferenceItemDeletedJson(itemReference));

        activityStreamService.create(as);
    }


    @Override
    public String getConverterKey() {
        return "plm.activity";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            PLMActivity plmActivity = activityRepository.findOne(object.getObject());

            if (plmActivity == null) return "";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.activity.create":
                    convertedString = getActivityCreatedString(messageString, actor, plmActivity);
                    break;
                case "plm.activity.update.basicinfo":
                    convertedString = getActivityBasicInfoUpdatedString(messageString, actor, plmActivity, as);
                    break;
                case "plm.activity.files.add":
                    convertedString = getActivityFilesAddedString(messageString, plmActivity, as);
                    break;
                case "plm.activity.files.delete":
                    convertedString = getActivityFileDeletedString(messageString, plmActivity, as);
                    break;
                case "plm.activity.files.folders.add":
                    convertedString = getActivityFoldersAddedString(messageString, plmActivity, as);
                    break;
                case "plm.activity.files.folders.delete":
                    convertedString = getActivityFoldersDeletedString(messageString, plmActivity, as);
                    break;
                case "plm.activity.files.version":
                    convertedString = getActivityFilesVersionedString(messageString, plmActivity, as);
                    break;
                case "plm.activity.files.rename":
                    convertedString = getActivityFileRenamedString(messageString, plmActivity, as);
                    break;
                case "plm.activity.files.replace":
                    convertedString = getActivityFileRenamedString(messageString, plmActivity, as);
                    break;
                case "plm.activity.files.lock":
                    convertedString = getActivityFileString(messageString, plmActivity, as);
                    break;
                case "plm.activity.files.unlock":
                    convertedString = getActivityFileString(messageString, plmActivity, as);
                    break;
                case "plm.activity.files.download":
                    convertedString = getActivityFileString(messageString, plmActivity, as);
                    break;
                case "plm.activity.workflow.start":
                    convertedString = getActivityWorkflowStartString(messageString, plmActivity, as);
                    break;
                case "plm.activity.workflow.finish":
                    convertedString = getActivityWorkflowFinishString(messageString, plmActivity, as);
                    break;
                case "plm.activity.workflow.promote":
                    convertedString = getActivityWorkflowPromoteDemoteString(messageString, plmActivity, as);
                    break;
                case "plm.activity.workflow.demote":
                    convertedString = getActivityWorkflowPromoteDemoteString(messageString, plmActivity, as);
                    break;
                case "plm.activity.workflow.hold":
                    convertedString = getActivityWorkflowHoldUnholdString(messageString, plmActivity, as);
                    break;
                case "plm.activity.workflow.unhold":
                    convertedString = getActivityWorkflowHoldUnholdString(messageString, plmActivity, as);
                    break;
                case "plm.activity.workflow.add":
                    convertedString = getActivityWorkflowAddedString(messageString, plmActivity, as);
                    break;
                case "plm.activity.workflow.change":
                    convertedString = getActivityWorkflowChangeString(messageString, plmActivity, as);
                    break;
                case "plm.activity.activities.workflow.add":
                    convertedString = getActivityActivityWorkflowAddedString(messageString, plmActivity, as);
                    break;
                case "plm.activity.activities.workflow.change":
                    convertedString = getActivityActivityWorkflowChangeString(messageString, plmActivity, as);
                    break;
                case "plm.activity.deliverables.add":
                    convertedString = getProjectDeliverablesAddedString(messageString, plmActivity, as);
                    break;
                case "plm.activity.deliverables.delete":
                    convertedString = getActivityMemberDeletedString(messageString, plmActivity, as);
                    break;
                case "plm.activity.deliverables.finish":
                    convertedString = getProjectDeliverablesFinishString(messageString, plmActivity, as);
                    break;
                case "plm.activity.referenceitems.add":
                    convertedString = getActivityReferenceItemsAddedString(messageString, plmActivity, as);
                    break;
                case "plm.activity.referenceitems.delete":
                    convertedString = getActivityMemberDeletedString(messageString, plmActivity, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getActivityCreatedString(String messageString, Person actor, PLMActivity activity) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), activity.getName());
    }


    private String getActivityBasicInfoUpdatedString(String messageString, Person actor, PLMActivity activity, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), activity.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.activity.update.basicinfo.property");

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

    public String getActivityFilesAddedJson(List<PLMActivityFile> files) {
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

    public String getActivityDeliverablesAddedJson(List<PLMItem> activityDeliverables) {
        String json = null;

        List<ASNewMemberDTO> AsNewMemberDtos = new ArrayList<>();
        activityDeliverables.forEach(f -> AsNewMemberDtos.add(new ASNewMemberDTO(f.getId(), f.getItemName())));

        try {
            json = objectMapper.writeValueAsString(AsNewMemberDtos);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getActivityReferenceItemsAddedJson(List<ASNewMemberDTO> activityItemReferences) {
        String json = null;

        try {
            json = objectMapper.writeValueAsString(activityItemReferences);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getActivityFoldersAddedJson(PLMActivityFile file) {
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

    private String getActivityFoldersDeletedJson(PLMFile file) {
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

    private String getActivityFilesAddedString(String messageString, PLMActivity activity, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), activity.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.activity.files.add.file");

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

    private String getActivityFoldersAddedString(String messageString, PLMActivity activity, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), activity.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getActivityFoldersDeletedString(String messageString, PLMActivity activity, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), activity.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getActivityFileDeletedString(String messageString, PLMActivity activity, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), activity.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    public String getActivityFilesVersionedJson(List<PLMActivityFile> files) {
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

    private String getActivityFilesVersionedString(String messageString, PLMActivity activity, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), activity.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.activity.files.version.file");

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

    private String getActivityFileRenamedString(String messageString, PLMActivity activity, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    activity.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getActivityFileString(String messageString, PLMActivity activity, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    activity.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getActivityWorkflowStartString(String messageString, PLMActivity activity, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                activity.getName());
    }

    private String getActivityWorkflowFinishString(String messageString, PLMActivity activity, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                activity.getName());
    }

    private String getActivityWorkflowPromoteDemoteString(String messageString, PLMActivity activity, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();
        ActivityStreamObject target = as.getTarget();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus fromActivity = workflowStatusRepository.findOne(source.getObject());
        PLMWorkflowStatus toActivity = workflowStatusRepository.findOne(target.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                activity.getName(),
                highlightValue(fromActivity.getName()),
                highlightValue(toActivity.getName()));
    }

    private String getActivityWorkflowHoldUnholdString(String messageString, PLMActivity activity, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus plmWorkflowActivity = workflowStatusRepository.findOne(source.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflowActivity.getName()),
                highlightValue(plmWorkflow.getName()),
                activity.getName());
    }

    private String getActivityWorkflowAddedString(String messageString, PLMActivity activity, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getNewValue()),
                    activity.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    public String getActivityWorkflowChangeString(String messageString, PLMActivity activity, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getOldValue()),
                    highlightValue(workflow.getNewValue()),
                    activity.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    public String getActivityActivityWorkflowAddedString(String messageString, PLMActivity activity, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getNewValue()),
                    highlightValue(workflow.getProperty()),
                    activity.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    public String getActivityActivityWorkflowChangeString(String messageString, PLMActivity activity, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getOldValue()),
                    highlightValue(workflow.getNewValue()),
                    highlightValue(workflow.getProperty()),
                    activity.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }


    private String getActivityMemberDeletedString(String messageString, PLMActivity activity, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewMemberDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewMemberDTO>>() {
            });
            ASNewMemberDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), activity.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getActivityReferenceItemsAddedString(String messageString, PLMActivity activity, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), activity.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.activity.referenceitems.add");

        String json = as.getData();
        try {
            List<ASNewMemberDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewMemberDTO>>() {
            });
            files.forEach(f -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(f.getName()), activity.getName()));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }


    public String getActivityDeliverableDeletedJson(PLMItem deliverable) {
        String json = null;

        List<ASNewMemberDTO> ASNewMemberDtos = new ArrayList<>();
        ASNewMemberDtos.add(new ASNewMemberDTO(deliverable.getId(), deliverable.getItemName()));
        try {
            json = objectMapper.writeValueAsString(ASNewMemberDtos);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getActivityReferenceItemDeletedJson(PLMItem itemReference) {
        String json = null;

        List<ASNewMemberDTO> ASNewMemberDtos = new ArrayList<>();
        ASNewMemberDtos.add(new ASNewMemberDTO(itemReference.getId(), itemReference.getItemName()));
        try {
            json = objectMapper.writeValueAsString(ASNewMemberDtos);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    public String getActivityBasicInfoUpdatedJson(PLMActivity oldActivity, PLMActivity newActivity) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String oldValue = oldActivity.getName();
        String newValue = newActivity.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        oldValue = oldActivity.getDescription();
        newValue = newActivity.getDescription();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue, newValue));
        }

        Integer oldValue1 = oldActivity.getAssignedTo();
        Integer newValue1 = newActivity.getAssignedTo();
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("AssignedTo", personRepository.findOne(oldValue1).getFullName(), personRepository.findOne(newValue1).getFullName()));
        }
        Date oldDate = null;
        Date newDate = null;
        if (oldActivity.getPlannedStartDate() != null) {
            oldDate = oldActivity.getPlannedStartDate();
        }

        if (newActivity.getPlannedStartDate() != null) {
            newDate = newActivity.getPlannedStartDate();
        }

        if (oldDate == null && newDate != null) {
            String oldDateValue = "";
            String newDateValue = dateFormat.format(newDate);
            changes.add(new ASPropertyChangeDTO("Planned Start Date", oldDateValue, newDateValue));
        } else if (oldDate != null && newDate != null && oldDate.compareTo(newDate) != 0) {
            String oldDateValue = dateFormat.format(oldDate);
            String newDateValue = dateFormat.format(newDate);
            changes.add(new ASPropertyChangeDTO("Planned Start Date", oldDateValue, newDateValue));
        }
        if (oldActivity.getPlannedFinishDate() != null) {
            oldDate = oldActivity.getPlannedFinishDate();
        }

        if (newActivity.getPlannedFinishDate() != null) {
            newDate = newActivity.getPlannedFinishDate();
        }
        if (oldDate == null && newDate != null) {
            String oldDateValue = "";
            String newDateValue = dateFormat.format(newDate);
            changes.add(new ASPropertyChangeDTO("Planned Finished Date", oldDateValue, newDateValue));
        } else if (oldDate != null && newDate != null && oldDate.compareTo(newDate) != 0) {
            String oldDateValue = dateFormat.format(oldDate);
            String newDateValue = dateFormat.format(newDate);
            changes.add(new ASPropertyChangeDTO("Planned Finished Date", oldDateValue, newDateValue));
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

    private String getProjectDeliverablesAddedString(String messageString, PLMActivity activity, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), activity.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.activity.deliverables.add");

        String json = as.getData();
        try {
            List<ASNewMemberDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewMemberDTO>>() {
            });
            files.forEach(f -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(f.getName()), activity.getName()));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getProjectDeliverablesFinishString(String messageString, PLMActivity activity, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), activity.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.activity.deliverables.finish");

        String json = as.getData();
        try {
            List<ASNewMemberDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewMemberDTO>>() {
            });
            files.forEach(f -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(f.getName()), activity.getName()));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }


}
