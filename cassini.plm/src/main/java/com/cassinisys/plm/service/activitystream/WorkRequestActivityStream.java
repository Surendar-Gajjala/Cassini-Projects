package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.WorkRequestEvents;
import com.cassinisys.plm.model.mro.*;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.repo.mro.MROObjectTypeAttributeRepository;
import com.cassinisys.plm.repo.mro.MROWorkRequestRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowDefinitionRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowStatusRepository;
import com.cassinisys.plm.service.activitystream.dto.ASAttributeChangeDTO;
import com.cassinisys.plm.service.activitystream.dto.ASFileReplaceDto;
import com.cassinisys.plm.service.activitystream.dto.ASNewFileDTO;
import com.cassinisys.plm.service.activitystream.dto.ASVersionedFileDTO;
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
public class WorkRequestActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MROWorkRequestRepository workRequestRepository;
    @Autowired
    private MROObjectTypeAttributeRepository mroObjectTypeAttributeRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PLMWorkflowRepository workFlowRepository;
    @Autowired
    private PLMWorkflowStatusRepository workflowStatusRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository workflowDefinitionRepository;

    @Async
    @EventListener
    public void workRequestsCreated(WorkRequestEvents.WorkRequestCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getWorkRequest().getId());

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroworkrequest.create");
        object.setType("mroworkrequest");

        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void workRequestBasicInfoUpdated(WorkRequestEvents.WorkRequestBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        MROWorkRequest oldWorkRequest = event.getOldWorkRequest();
        MROWorkRequest newWorkRequest = event.getNewWorkRequest();

        object.setObject(newWorkRequest.getId());
        object.setType("mroworkrequest");
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroworkrequest.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getWorkRequestBasicInfoUpdatedJson(oldWorkRequest, newWorkRequest));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void workRequestAttributesUpdated(WorkRequestEvents.WorkRequestAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        MROObject workRequest = event.getWorkRequest();

        MROObjectAttribute oldAtt = event.getOldAttribute();
        MROObjectAttribute newAtt = event.getNewAttribute();

        object.setObject(workRequest.getId());

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroworkrequest.update.attributes");
        object.setType("mroworkrequest");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getWorkRequestAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void workRequestFilesAdded(WorkRequestEvents.WorkRequestFilesAddedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroworkrequest.files.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setType("mroworkrequest");
        object.setObject(event.getWorkRequest());
        as.setObject(object);
        as.setData(getWorkRequestFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void workRequestFoldersAdded(WorkRequestEvents.WorkRequestFoldersAddedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.workRequest.files.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setType("mroworkrequest");
        object.setObject(event.getWorkRequest());
        as.setObject(object);
        as.setData(getWorkRequestFoldersAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void workRequestFoldersDeleted(WorkRequestEvents.WorkRequestFoldersDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroworkrequest.files.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setType("mroworkrequest");
        object.setObject(event.getWorkRequest());
        as.setObject(object);
        as.setData(getWorkRequestFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void workRequestFileDeleted(WorkRequestEvents.WorkRequestFileDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroworkrequest.files.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setType("mroworkrequest");
        object.setObject(event.getWorkRequest());
        as.setObject(object);
        as.setData(getWorkRequestFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void workRequestFilesVersioned(WorkRequestEvents.WorkRequestFilesVersionedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroworkrequest.files.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setType("mroworkrequest");
        object.setObject(event.getWorkRequest());
        as.setObject(object);
        as.setData(getWorkRequestFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void workRequestFileRenamed(WorkRequestEvents.WorkRequestFileRenamedEvent event) {
        PLMFile oldFile = event.getOldFile();
        PLMFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals("Rename")) as.setActivity("plm.mro.mroworkrequest.files.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.mro.mroworkrequest.files.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setType("mroworkrequest");
        object.setObject(event.getWorkRequest());
        object.setType("mroworkrequest");
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
    public void workRequestFileLocked(WorkRequestEvents.WorkRequestFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroworkrequest.files.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getWorkRequest());
        object.setType("mroworkrequest");
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
    public void workRequestFileUnlocked(WorkRequestEvents.WorkRequestFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroworkrequest.files.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getWorkRequest());
        object.setType("mroworkrequest");
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
    public void workRequestFileDownloaded(WorkRequestEvents.WorkRequestFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroworkrequest.files.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setType("mroworkrequest");
        object.setObject(event.getWorkRequest());
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
    public void workRequestCommentAdded(WorkRequestEvents.WorkRequestCommentAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroworkrequest.comment");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setType("mroworkrequest");
        object.setObject(event.getWorkRequest().getId());
        object.setType("workRequest");
        as.setObject(object);
        as.setData(event.getComment().getComment());

        activityStreamService.create(as);
    }


    @Async
    @EventListener
    public void workRequestWorkflowStarted(WorkRequestEvents.WorkRequestWorkflowStartedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroworkrequest.workflow.start");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getWorkRequest().getId());
        object.setType("mroworkrequest");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void workRequestWorkflowFinished(WorkRequestEvents.WorkRequestWorkflowFinishedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroworkrequest.workflow.finish");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getWorkRequest().getId());
        object.setType("mroworkrequest");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void workRequestWorkflowPromoted(WorkRequestEvents.WorkRequestWorkflowPromotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroworkrequest.workflow.promote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getWorkRequest().getId());
        object.setType("mroworkrequest");
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
    public void workRequestWorkflowDemoted(WorkRequestEvents.WorkRequestWorkflowDemotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroworkrequest.workflow.demote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getWorkRequest().getId());
        object.setType("mroworkrequest");
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
    public void workRequestWorkflowHold(WorkRequestEvents.WorkRequestWorkflowHoldEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroworkrequest.workflow.hold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getWorkRequest().getId());
        object.setType("mroworkrequest");
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
    public void workRequestWorkflowUnhold(WorkRequestEvents.WorkRequestWorkflowUnholdEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroworkrequest.workflow.unhold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getWorkRequest().getId());
        object.setType("mroworkrequest");
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
    public void workRequestWorkflowChange(WorkRequestEvents.WorkRequestWorkflowChangeEvent event) {
        ActivityStream as = new ActivityStream();
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getWorkRequest().getId());
        object.setType("mroworkrequest");
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
            as.setActivity("plm.mro.mroworkrequest.workflow.change");
        } else {
            as.setActivity("plm.mro.mroworkrequest.workflow.add");
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


    @Override
    public String getConverterKey() {
        return "plm.mro.mroworkrequest";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            MROWorkRequest workRequest = workRequestRepository.findOne(object.getObject());
            String workRequestType = "";
            if (workRequest == null) return "";

            if (workRequest.getObjectType().equals(ObjectType.valueOf(MROEnumObject.MROSPAREPART.toString()))) {
                workRequestType = "mroworkrequest";
            }

            String activity = as.getActivity();
            switch (activity) {
                case "plm.mro.mroworkrequest.create":
                    convertedString = getWorkRequestCreatedString(messageString, actor, workRequest);
                    break;
                case "plm.mro.mroworkrequest.update.basicinfo":
                    convertedString = getWorkRequestBasicInfoUpdatedString(messageString, actor, workRequest, workRequestType, as);
                    break;
                case "plm.mro.mroworkrequest.update.attributes":
                    convertedString = getWorkRequestAttributeUpdatedString(messageString, actor, workRequest, as);
                    break;
                case "plm.mro.mroworkrequest.files.add":
                    convertedString = getWorkRequestFilesAddedString(messageString, workRequest, as);
                    break;
                case "plm.mro.mroworkrequest.files.delete":
                    convertedString = getWorkRequestFileDeletedString(messageString, workRequest, as);
                    break;
                case "plm.mro.mroworkrequest.files.folders.add":
                    convertedString = getWorkRequestFoldersAddedString(messageString, workRequest, as);
                    break;
                case "plm.mro.mroworkrequest.files.folders.delete":
                    convertedString = getWorkRequestFoldersDeletedString(messageString, workRequest, as);
                    break;
                case "plm.mro.mroworkrequest.files.version":
                    convertedString = getWorkRequestFilesVersionedString(messageString, workRequest, as);
                    break;
                case "plm.mro.mroworkrequest.files.rename":
                    convertedString = getWorkRequestFileRenamedString(messageString, workRequest, as);
                    break;
                case "plm.mro.mroworkrequest.files.replace":
                    convertedString = getWorkRequestFileRenamedString(messageString, workRequest, as);
                    break;
                case "plm.mro.mroworkrequest.files.lock":
                    convertedString = getWorkRequestFileString(messageString, workRequest, as);
                    break;
                case "plm.mro.mroworkrequest.files.unlock":
                    convertedString = getWorkRequestFileString(messageString, workRequest, as);
                    break;
                case "plm.mro.mroworkrequest.files.download":
                    convertedString = getWorkRequestFileString(messageString, workRequest, as);
                    break;
                case "plm.mro.mroworkrequest.comment":
                    convertedString = getWorkRequestCommentString(messageString, workRequest, as);
                    break;
                case "plm.mro.mroworkrequest.workflow.start":
                    convertedString = getWorkRequestWorkflowStartString(messageString, workRequest, as);
                    break;
                case "plm.mro.mroworkrequest.workflow.finish":
                    convertedString = getWorkRequestWorkflowFinishString(messageString, workRequest, as);
                    break;
                case "plm.mro.mroworkrequest.workflow.promote":
                    convertedString = getWorkRequestWorkflowPromoteDemoteString(messageString, workRequest, as);
                    break;
                case "plm.mro.mroworkrequest.workflow.demote":
                    convertedString = getWorkRequestWorkflowPromoteDemoteString(messageString, workRequest, as);
                    break;
                case "plm.mro.mroworkrequest.workflow.hold":
                    convertedString = getWorkRequestWorkflowHoldUnholdString(messageString, workRequest, as);
                    break;
                case "plm.mro.mroworkrequest.workflow.unhold":
                    convertedString = getWorkRequestWorkflowHoldUnholdString(messageString, workRequest, as);
                    break;
                case "plm.mro.mroworkrequest.workflow.add":
                    convertedString = getWorkRequestWorkflowAddedString(messageString, workRequest, as);
                    break;
                case "plm.mro.mroworkrequest.workflow.change":
                    convertedString = getWorkRequestWorkflowChangeString(messageString, workRequest, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getWorkRequestCreatedString(String messageString, Person actor, MROWorkRequest workRequest) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), workRequest.getNumber());
    }


    private String getWorkRequestBasicInfoUpdatedString(String messageString, Person actor, MROWorkRequest workRequest, String type, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), type, workRequest.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mro.mroworkrequest.update.basicinfo.property");

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

    private String getWorkRequestBasicInfoUpdatedJson(MROWorkRequest oldWorkRequest, MROWorkRequest newWorkRequest) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldWorkRequest.getName();
        String newValue = newWorkRequest.getName();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        oldValue = oldWorkRequest.getDescription();
        newValue = newWorkRequest.getDescription();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue, newValue));
        }


        Integer oldValue1 = oldWorkRequest.getRequestor();
        Integer newValue1 = newWorkRequest.getRequestor();
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Requestor", personRepository.findOne(oldValue1).getFullName(), personRepository.findOne(newValue1).getFullName()));
        }

        oldValue = oldWorkRequest.getNotes();
        newValue = newWorkRequest.getNotes();
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


    private String getWorkRequestAttributesUpdatedJson(MROObjectAttribute oldAttribute, MROObjectAttribute newAttribute) {
        List<ASAttributeChangeDTO> changes = new ArrayList<>();

        String oldValue = "";
        if (oldAttribute != null) {
            oldValue = oldAttribute.getValueAsString();
        }
        String newValue = newAttribute.getValueAsString();

        MROObjectTypeAttribute attDef = mroObjectTypeAttributeRepository.findOne(newAttribute.getId().getAttributeDef());
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

    private String getWorkRequestAttributeUpdatedString(String messageString, Person actor, MROWorkRequest workRequest, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), workRequest.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mro.mroworkrequest.update.attributes.attribute");

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

    private String getWorkRequestFilesAddedJson(List<PLMFile> files) {
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

    private String getWorkRequestFoldersAddedJson(PLMFile file) {
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

    private String getWorkRequestFoldersDeletedJson(PLMFile file) {
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

    private String getWorkRequestFilesAddedString(String messageString, MROWorkRequest workRequest, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), workRequest.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mro.mroworkrequest.files.add.file");

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

    private String getWorkRequestFoldersAddedString(String messageString, MROWorkRequest workRequest, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), workRequest.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getWorkRequestFoldersDeletedString(String messageString, MROWorkRequest workRequest, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), workRequest.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getWorkRequestFileDeletedString(String messageString, MROWorkRequest workRequest, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), workRequest.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getWorkRequestFilesVersionedJson(List<PLMFile> files) {
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

    private String getWorkRequestFilesVersionedString(String messageString, MROWorkRequest workRequest, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), workRequest.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mro.mroworkrequest.files.version.file");

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

    private String getWorkRequestFileRenamedString(String messageString, MROWorkRequest workRequest, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    workRequest.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getWorkRequestFileString(String messageString, MROWorkRequest workRequest, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    workRequest.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getWorkRequestCommentString(String messageString, MROWorkRequest workRequest, ActivityStream as) {
        String mrosage = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                workRequest.getNumber());

        String s = highlightValue("Comment: ");
        s += as.getData();
        s = addMarginToMessage(s);

        mrosage += s;

        return mrosage;
    }

    private String getWorkRequestWorkflowStartString(String messageString, MROWorkRequest workRequest, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                workRequest.getName());
    }

    private String getWorkRequestWorkflowFinishString(String messageString, MROWorkRequest workRequest, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                workRequest.getName());
    }

    private String getWorkRequestWorkflowPromoteDemoteString(String messageString, MROWorkRequest workRequest, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();
        ActivityStreamObject target = as.getTarget();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus fromActivity = workflowStatusRepository.findOne(source.getObject());
        PLMWorkflowStatus toActivity = workflowStatusRepository.findOne(target.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                workRequest.getName(),
                highlightValue(fromActivity.getName()),
                highlightValue(toActivity.getName()));
    }

    private String getWorkRequestWorkflowHoldUnholdString(String messageString, MROWorkRequest workRequest, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus plmWorkflowActivity = workflowStatusRepository.findOne(source.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflowActivity.getName()),
                highlightValue(plmWorkflow.getName()),
                workRequest.getName());
    }

    private String getWorkRequestWorkflowAddedString(String messageString, MROWorkRequest workRequest, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getNewValue()),
                    workRequest.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getWorkRequestWorkflowChangeString(String messageString, MROWorkRequest workRequest, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getOldValue()),
                    highlightValue(workflow.getNewValue()),
                    workRequest.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }
}
