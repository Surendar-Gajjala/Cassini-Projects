package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.TaskEvents;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.pm.PLMTask;
import com.cassinisys.plm.model.pm.PLMTaskFile;
import com.cassinisys.plm.model.pm.PLMWbsElement;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.repo.pm.TaskRepository;
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
public class ProjectTaskActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PLMWorkflowRepository workFlowRepository;
    @Autowired
    private PLMWorkflowStatusRepository workflowStatusRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository workflowDefinitionRepository;

    @Async
    @EventListener
    public void taskCreated(TaskEvents.TaskCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getTask().getId());
        object.setType("task");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.task.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void taskBasicInfoUpdated(TaskEvents.TaskBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PLMTask oldTask = event.getOldTask();
        PLMTask newTask = event.getNewTask();

        object.setObject(newTask.getId());
        object.setType("task");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.task.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getTaskBasicInfoUpdatedJson(oldTask, newTask));
        if (as.getData() != null) activityStreamService.create(as);
    }


    @Async
    @EventListener
    public void taskFilesAdded(TaskEvents.TaskFilesAddedEvent event) {
        List<PLMTaskFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.task.files.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getTask().getId());
        object.setType("task");
        as.setObject(object);
        as.setData(getTaskFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void taskFoldersAdded(TaskEvents.TaskFoldersAddedEvent event) {
        PLMTaskFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.task.files.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getTask().getId());
        object.setType("task");
        as.setObject(object);
        as.setData(getTaskFoldersAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void taskFoldersDeleted(TaskEvents.TaskFoldersDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.task.files.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getTask().getId());
        object.setType("task");
        as.setObject(object);
        as.setData(getTaskFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void taskFileDeleted(TaskEvents.TaskFileDeletedEvent event) {
        PLMTaskFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.task.files.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getTask().getId());
        object.setType("task");
        as.setObject(object);
        as.setData(getTaskFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void taskFilesVersioned(TaskEvents.TaskFilesVersionedEvent event) {
        List<PLMTaskFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.task.files.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getTask().getId());
        object.setType("task");
        as.setObject(object);
        as.setData(getTaskFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void taskFileRenamed(TaskEvents.TaskFileRenamedEvent event) {
        PLMTaskFile oldFile = event.getOldFile();
        PLMTaskFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals("Rename")) as.setActivity("plm.task.files.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.task.files.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getTask().getId());
        object.setType("task");
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
    public void taskFileLocked(TaskEvents.TaskFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.task.files.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getTask().getId());
        object.setType("task");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getTaskFile().getId(), event.getTaskFile().getName(), FileUtils.byteCountToDisplaySize(event.getTaskFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void taskFileUnlocked(TaskEvents.TaskFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.task.files.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getTask().getId());
        object.setType("task");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getTaskFile().getId(), event.getTaskFile().getName(), FileUtils.byteCountToDisplaySize(event.getTaskFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void taskFileDownloaded(TaskEvents.TaskFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.task.files.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getTask().getId());
        object.setType("task");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getTaskFile().getId(), event.getTaskFile().getName(), FileUtils.byteCountToDisplaySize(event.getTaskFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void taskWorkflowStarted(TaskEvents.TaskWorkflowStartedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.task.workflow.start");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getTask().getId());
        object.setType("task");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void taskWorkflowFinished(TaskEvents.TaskWorkflowFinishedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.task.workflow.finish");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getTask().getId());
        object.setType("task");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void taskWorkflowPromoted(TaskEvents.TaskWorkflowPromotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.task.workflow.promote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getTask().getId());
        object.setType("task");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        ActivityStreamObject source = new ActivityStreamObject();
        source.setObject(event.getFromTask().getId());
        source.setType("workflowactivity");
        as.setSource(source);

        ActivityStreamObject target = new ActivityStreamObject();
        target.setObject(event.getToTask().getId());
        target.setType("workflowactivity");
        as.setTarget(target);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void taskWorkflowDemoted(TaskEvents.TaskWorkflowDemotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.task.workflow.demote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getTask().getId());
        object.setType("task");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        ActivityStreamObject source = new ActivityStreamObject();
        source.setObject(event.getFromTask().getId());
        source.setType("workflowactivity");
        as.setSource(source);

        ActivityStreamObject target = new ActivityStreamObject();
        target.setObject(event.getToTask().getId());
        target.setType("workflowactivity");
        as.setTarget(target);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void taskWorkflowHold(TaskEvents.TaskWorkflowHoldEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.task.workflow.hold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getTask().getId());
        object.setType("task");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        ActivityStreamObject source = new ActivityStreamObject();
        source.setObject(event.getWorkflowTask().getId());
        source.setType("workflowstatus");
        as.setSource(source);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void taskWorkflowUnhold(TaskEvents.TaskWorkflowUnholdEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.task.workflow.unhold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getTask().getId());
        object.setType("task");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        ActivityStreamObject source = new ActivityStreamObject();
        source.setObject(event.getWorkflowTask().getId());
        source.setType("workflowstatus");
        as.setSource(source);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void taskPartWorkflowChange(TaskEvents.TaskWorkflowChangeEvent event) {
        ActivityStream as = new ActivityStream();
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getTask().getId());
        object.setType("task");
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
            as.setActivity("plm.task.workflow.change");
        } else {
            as.setActivity("plm.task.workflow.add");
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
    public void taskDeliverablesAdded(TaskEvents.TaskDeliverablesAddedEvent event) {
        List<PLMItem> deliverables = event.getTaskDeliverables();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.task.deliverables.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getTask().getId());
        object.setType("task");
        as.setObject(object);
        as.setData(getTaskDeliverablesAddedJson(deliverables));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void TaskDeliverableDeleted(TaskEvents.TaskDeliverableDeletedEvent event) {
        PLMItem deliverable = event.getTaskDeliverable();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.task.deliverables.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getTask().getId());
        object.setType("task");
        as.setObject(object);
        as.setData(getTaskDeliverableDeletedJson(deliverable));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void taskDeliverableFinished(TaskEvents.TaskDeliverableFinishedEvent event) {
        PLMItem deliverable = event.getTaskDeliverable();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.task.deliverables.finish");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getTask().getId());
        object.setType("task");
        as.setObject(object);
        as.setData(getActivityDeliverableDeletedJson(deliverable));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void taskReferenceItemsAdded(TaskEvents.TaskReferenceItemsAddedEvent event) {
        List<ASNewMemberDTO> itemReferences = event.getTaskItemReferences();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.task.referenceitems.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getTask().getId());
        object.setType("task");
        as.setObject(object);
        as.setData(getTaskReferenceItemsAddedJson(itemReferences));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void taskReferenceItemDeleted(TaskEvents.TaskReferenceItemDeletedEvent event) {
        PLMItem itemReference = event.getTaskItemReference();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.task.referenceitems.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getTask().getId());
        object.setType("task");
        as.setObject(object);
        as.setData(getTaskReferenceItemDeletedJson(itemReference));

        activityStreamService.create(as);
    }


    @Override
    public String getConverterKey() {
        return "plm.task";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            PLMTask plmTask = taskRepository.findOne(object.getObject());

            if (plmTask == null) return "";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.task.create":
                    convertedString = getTaskCreatedString(messageString, actor, plmTask);
                    break;
                case "plm.task.update.basicinfo":
                    convertedString = getTaskBasicInfoUpdatedString(messageString, actor, plmTask, as);
                    break;
                case "plm.task.files.add":
                    convertedString = getTaskFilesAddedString(messageString, plmTask, as);
                    break;
                case "plm.task.files.delete":
                    convertedString = getTaskFileDeletedString(messageString, plmTask, as);
                    break;
                case "plm.task.files.folders.add":
                    convertedString = getTaskFoldersAddedString(messageString, plmTask, as);
                    break;
                case "plm.task.files.folders.delete":
                    convertedString = getTaskFoldersDeletedString(messageString, plmTask, as);
                    break;
                case "plm.task.files.version":
                    convertedString = getTaskFilesVersionedString(messageString, plmTask, as);
                    break;
                case "plm.task.files.rename":
                    convertedString = getTaskFileRenamedString(messageString, plmTask, as);
                    break;
                case "plm.task.files.replace":
                    convertedString = getTaskFileRenamedString(messageString, plmTask, as);
                    break;
                case "plm.task.files.lock":
                    convertedString = getTaskFileString(messageString, plmTask, as);
                    break;
                case "plm.task.files.unlock":
                    convertedString = getTaskFileString(messageString, plmTask, as);
                    break;
                case "plm.task.files.download":
                    convertedString = getTaskFileString(messageString, plmTask, as);
                    break;
                case "plm.task.workflow.start":
                    convertedString = getTaskWorkflowStartString(messageString, plmTask, as);
                    break;
                case "plm.task.workflow.finish":
                    convertedString = getTaskWorkflowFinishString(messageString, plmTask, as);
                    break;
                case "plm.task.workflow.promote":
                    convertedString = getTaskWorkflowPromoteDemoteString(messageString, plmTask, as);
                    break;
                case "plm.task.workflow.demote":
                    convertedString = getTaskWorkflowPromoteDemoteString(messageString, plmTask, as);
                    break;
                case "plm.task.workflow.hold":
                    convertedString = getTaskWorkflowHoldUnholdString(messageString, plmTask, as);
                    break;
                case "plm.task.workflow.unhold":
                    convertedString = getTaskWorkflowHoldUnholdString(messageString, plmTask, as);
                    break;
                case "plm.task.workflow.add":
                    convertedString = getTaskWorkflowAddedString(messageString, plmTask, as);
                    break;
                case "plm.task.workflow.change":
                    convertedString = getTaskWorkflowChangeString(messageString, plmTask, as);
                    break;
                case "plm.task.deliverables.add":
                    convertedString = getTaskDeliverablesAddedString(messageString, plmTask, as);
                    break;
                case "plm.task.deliverables.delete":
                    convertedString = getTaskMemberDeletedString(messageString, plmTask, as);
                    break;
                case "plm.task.deliverables.finish":
                    convertedString = getTaskDeliverablesFinishString(messageString, plmTask, as);
                    break;
                case "plm.task.referenceitems.add":
                    convertedString = getTaskReferenceItemsAddedString(messageString, plmTask, as);
                    break;
                case "plm.task.referenceitems.delete":
                    convertedString = getTaskMemberDeletedString(messageString, plmTask, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getTaskCreatedString(String messageString, Person actor, PLMTask activity) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), activity.getName());
    }

    private String getTaskBasicInfoUpdatedString(String messageString, Person actor, PLMTask activity, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), activity.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.task.update.basicinfo.property");

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

    public String getTaskFilesAddedJson(List<PLMTaskFile> files) {
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


    public String getTaskDeliverablesAddedJson(List<PLMItem> activityDeliverables) {
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

    public String getTaskDeliverableDeletedJson(PLMItem deliverable) {
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

    private String getTaskReferenceItemsAddedJson(List<ASNewMemberDTO> activityItemReferences) {
        String json = null;

        try {
            json = objectMapper.writeValueAsString(activityItemReferences);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getTaskFoldersAddedJson(PLMTaskFile file) {
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

    private String getTaskFoldersDeletedJson(PLMFile file) {
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

    private String getTaskFilesAddedString(String messageString, PLMTask activity, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), activity.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.task.files.add.file");

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

    private String getTaskFoldersAddedString(String messageString, PLMTask activity, ActivityStream as) {

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

    private String getTaskFoldersDeletedString(String messageString, PLMTask activity, ActivityStream as) {

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


    private String getTaskFileDeletedString(String messageString, PLMTask activity, ActivityStream as) {

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

    public String getTaskFilesVersionedJson(List<PLMTaskFile> files) {
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

    private String getTaskFilesVersionedString(String messageString, PLMTask activity, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), activity.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.task.files.version.file");

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

    private String getTaskFileRenamedString(String messageString, PLMTask activity, ActivityStream as) {
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

    private String getTaskFileString(String messageString, PLMTask activity, ActivityStream as) {
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

    private String getTaskWorkflowStartString(String messageString, PLMTask activity, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                activity.getName());
    }

    private String getTaskWorkflowFinishString(String messageString, PLMTask activity, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                activity.getName());
    }

    private String getTaskWorkflowPromoteDemoteString(String messageString, PLMTask activity, ActivityStream as) {
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

    private String getTaskWorkflowHoldUnholdString(String messageString, PLMTask activity, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus plmWorkflowActivity = workflowStatusRepository.findOne(source.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflowActivity.getName()),
                highlightValue(plmWorkflow.getName()),
                activity.getName());
    }

    public String getTaskWorkflowAddedString(String messageString, PLMTask activity, ActivityStream as) {
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

    public String getTaskWorkflowChangeString(String messageString, PLMTask activity, ActivityStream as) {
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


    private String getTaskMemberDeletedString(String messageString, PLMTask activity, ActivityStream as) {

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


    private String getTaskReferenceItemsAddedString(String messageString, PLMTask activity, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), activity.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.task.referenceitems.add");

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

    private String getTaskReferenceItemDeletedJson(PLMItem itemReference) {
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

    public String getTaskBasicInfoUpdatedJson(PLMTask oldActivity, PLMTask newActivity) {
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

        oldValue = oldActivity.getPercentComplete().toString();
        newValue = newActivity.getPercentComplete().toString();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("percentComplete", oldValue, newValue));
        }

        Integer oldValue1 = oldActivity.getAssignedTo();
        Integer newValue1 = newActivity.getAssignedTo();
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("AssignedTo", personRepository.findOne(oldValue1).getFullName(), personRepository.findOne(newValue1).getFullName()));
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

    private String getTaskDeliverablesAddedString(String messageString, PLMTask task, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), task.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.task.deliverables.add");

        String json = as.getData();
        try {
            List<ASNewMemberDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewMemberDTO>>() {
            });
            files.forEach(f -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(f.getName()), task.getName()));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }



    private String getTaskDeliverablesFinishString(String messageString, PLMTask task, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), task.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.task.deliverables.finish");

        String json = as.getData();
        try {
            List<ASNewMemberDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewMemberDTO>>() {
            });
            files.forEach(f -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(f.getName()), task.getName()));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

}
