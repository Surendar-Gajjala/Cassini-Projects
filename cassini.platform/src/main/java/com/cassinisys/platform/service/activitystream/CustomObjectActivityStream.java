package com.cassinisys.platform.service.activitystream;

import com.cassinisys.platform.events.CustomObjectEvents;
import com.cassinisys.platform.events.dto.*;
import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.model.custom.*;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.custom.CustomObjectRepository;
import com.cassinisys.platform.repo.custom.CustomObjectTypeAttributeRepository;
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
public class CustomObjectActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CustomObjectRepository customObjectRepository;
    @Autowired
    private CustomObjectTypeAttributeRepository customObjectTypeAttributeRepository;
    @Autowired
    private PersonRepository personRepository;

    @Async
    @EventListener
    public void customObjectCreated(CustomObjectEvents.CustomObjectCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getCustomObject().getId());
        object.setType("customObject");

        ActivityStream as = new ActivityStream();
        as.setActivity("customObject.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void customObjectBasicInfoUpdated(CustomObjectEvents.CustomObjectBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        CustomObject oldPlan = event.getOldCustomObject();
        CustomObject newPlan = event.getNewCustomObject();

        object.setObject(newPlan.getId());
        object.setType("customObject");

        ActivityStream as = new ActivityStream();
        as.setActivity("customObject.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getCustomObjectBasicInfoUpdatedJson(oldPlan, newPlan));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void customObjectAttributesUpdated(CustomObjectEvents.CustomObjectAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        CustomObjectAttribute oldAtt = event.getOldAttribute();
        CustomObjectAttribute newAtt = event.getNewAttribute();
        object.setObject(event.getObjectId());
        ActivityStream as = new ActivityStream();
        as.setActivity("customObject.update.attributes");
        as.setConverter(getConverterKey());
        object.setObject(event.getObjectId());
        object.setType("customObject");
        as.setObject(object);
        as.setData(getCustomObjectAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void customObjectFilesAdded(CustomObjectEvents.CustomObjectFilesAddedEvent event) {
        List<CustomFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("customObject.files.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getCustomObject());
        object.setType("customObject");
        as.setObject(object);
        as.setData(getCustomObjectFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void customObjectFoldersAdded(CustomObjectEvents.CustomObjectFoldersAddedEvent event) {
        CustomFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("customObject.files.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getCustomObject());
        object.setType("customObject");
        as.setObject(object);
        as.setData(getCustomObjectFoldersAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void customObjectFoldersDeleted(CustomObjectEvents.CustomObjectFoldersDeletedEvent event) {
        CustomFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("customObject.files.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getCustomObject());
        object.setType("customObject");
        as.setObject(object);
        as.setData(getCustomObjectFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void customObjectFileDeleted(CustomObjectEvents.CustomObjectFileDeletedEvent event) {
        CustomFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("customObject.files.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getCustomObject());
        object.setType("customObject");
        as.setObject(object);
        as.setData(getCustomObjectFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void customObjectFilesVersioned(CustomObjectEvents.CustomObjectFilesVersionedEvent event) {
        List<CustomFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("customObject.files.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getCustomObject());
        object.setType("customObject");
        as.setObject(object);
        as.setData(getCustomObjectFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void customObjectFileRenamed(CustomObjectEvents.CustomObjectFileRenamedEvent event) {
        CustomFile oldFile = event.getOldFile();
        CustomFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals("Rename")) as.setActivity("customObject.files.rename");
        if (event.getType().equals("Replace")) as.setActivity("customObject.files.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getCustomObject());
        object.setType("customObject");
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
    public void customObjectFileLocked(CustomObjectEvents.CustomObjectFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("customObject.files.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getCustomObject());
        object.setType("customObject");
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
    public void customObjectFileUnlocked(CustomObjectEvents.CustomObjectFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("customObject.files.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getCustomObject());
        object.setType("customObject");
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
    public void customObjectFileDownloaded(CustomObjectEvents.CustomObjectFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("customObject.files.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getCustomObject());
        object.setType("customObject");
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
    public void customObjectCommentAdded(CustomObjectEvents.CustomObjectCommentAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("customObject.comment");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getCustomObject().getId());
        object.setType("customObject");
        as.setObject(object);
        as.setData(event.getComment().getComment());

        activityStreamService.create(as);
    }


    @Async
    @EventListener
    public void customObjectBomAdded(CustomObjectEvents.CustomObjectBomAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("customObject.bom.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getCustomObject().getId());
        object.setType("customObject");
        as.setObject(object);
        List<ASBomCustomObject> asBomItems = new ArrayList<>();
        asBomItems.add(new ASBomCustomObject(event.getCustomObjectBom().getChild().getNumber(),
                event.getCustomObjectBom().getQuantity(), event.getCustomObjectBom().getNotes()));
        try {
            as.setData(objectMapper.writeValueAsString(asBomItems));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void customObjectBomDeleted(CustomObjectEvents.CustomObjectBomDeletedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("customObject.bom.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getCustomObject().getId());
        object.setType("customObject");
        as.setObject(object);
        ASBomCustomObject asBomItem = new ASBomCustomObject(event.getCustomObjectBom().getChild().getNumber(),
                event.getCustomObjectBom().getQuantity(), event.getCustomObjectBom().getNotes());
        try {
            as.setData(objectMapper.writeValueAsString(asBomItem));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void customObjectBomsAdded(CustomObjectEvents.CustomObjectBomsAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("customObject.bom.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getCustomObject().getId());
        object.setType("customObject");
        as.setObject(object);
        List<ASBomCustomObject> asBomItems = new ArrayList<>();
        event.getCustomObjectBoms().forEach(bom -> {
            asBomItems.add(new ASBomCustomObject(bom.getChild().getNumber(),
                    bom.getQuantity(), bom.getNotes()));
        });
        try {
            as.setData(objectMapper.writeValueAsString(asBomItems));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void customObjectBomsUpdated(CustomObjectEvents.CustomObjectBomUpdatedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("customObject.bom.update");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getCustomObject().getId());
        object.setType("customObject");
        as.setObject(object);
        as.setData(getCustomBomUpdatedJson(event.getOldCustomObjectBom(), event.getCustomObjectBom()));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }


    @Async
    @EventListener
    public void customObjectRelatedAdded(CustomObjectEvents.CustomObjectRelatedAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("customObject.related.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getCustomObject().getId());
        object.setType("customObject");
        as.setObject(object);
        List<ASRelatedObject> asRelatedObjects = new ArrayList<>();
        asRelatedObjects.add(new ASRelatedObject(event.getCustomObjectRelated().getRelated().getNumber(), event.getCustomObjectRelated().getNotes()));
        try {
            as.setData(objectMapper.writeValueAsString(asRelatedObjects));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void customObjectRelatedDeleted(CustomObjectEvents.CustomObjectRelatedDeletedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("customObject.related.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getCustomObject().getId());
        object.setType("customObject");
        as.setObject(object);
        ASRelatedObject asBomItem = new ASRelatedObject(event.getCustomObjectRelated().getRelated().getNumber(),
                event.getCustomObjectRelated().getNotes());
        try {
            as.setData(objectMapper.writeValueAsString(asBomItem));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void customObjectRelatedsAdded(CustomObjectEvents.CustomObjectRelatedsAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("customObject.related.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getCustomObject().getId());
        object.setType("customObject");
        as.setObject(object);
        List<ASRelatedObject> asRelatedObjects = new ArrayList<>();
        event.getCustomObjectRelateds().forEach(related -> {
            asRelatedObjects.add(new ASRelatedObject(related.getRelated().getNumber(),
                    related.getNotes()));
        });
        try {
            as.setData(objectMapper.writeValueAsString(asRelatedObjects));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void customObjectRelatedUpdated(CustomObjectEvents.CustomObjectRelatedUpdatedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("customObject.related.update");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getCustomObject().getId());
        object.setType("customObject");
        as.setObject(object);
        as.setData(getCustomObjectRelatedUpdatedJson(event.getOldCustomObjectRelated(), event.getCustomObjectRelated()));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }


    @Async
    @EventListener
    public void customObjectWorkflowStarted(CustomObjectEvents.CustomObjectWorkflowStartedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("customObject.workflow.start");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getCustomObject().getId());
        object.setType("customObject");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getWorkflowId());
        context.setType("workflow");
        as.setContext(context);


        ASWorkflowStart asBomItem = new ASWorkflowStart(event.getWorkflowId(), event.getWorkflowName());
        try {
            as.setData(objectMapper.writeValueAsString(asBomItem));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void customObjectWorkflowFinished(CustomObjectEvents.CustomObjectWorkflowFinishedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("customObject.workflow.finish");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getCustomObject().getId());
        object.setType("customObject");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getWorkflowId());
        context.setType("workflow");
        as.setContext(context);

        ASWorkflowStart asBomItem = new ASWorkflowStart(event.getWorkflowId(), event.getWorkflowName());
        try {
            as.setData(objectMapper.writeValueAsString(asBomItem));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void customObjectWorkflowPromoted(CustomObjectEvents.CustomObjectWorkflowPromotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("customObject.workflow.promote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getCustomObject().getId());
        object.setType("customObject");
        as.setObject(object);


        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getWorkflowId());
        context.setType("workflow");
        as.setContext(context);

        ActivityStreamObject source = new ActivityStreamObject();
        source.setObject(event.getFromActivityId());
        source.setType("workflowactivity");
        as.setSource(source);

        ActivityStreamObject target = new ActivityStreamObject();
        target.setObject(event.getToActivityId());
        target.setType("workflowactivity");
        as.setTarget(target);

        ASWorkflowPromote promote = new ASWorkflowPromote(event.getWorkflowId(), event.getWorkflowName(), event.getFromActivityName(), event.getToActivityName());
        try {
            as.setData(objectMapper.writeValueAsString(promote));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void customObjectWorkflowDemoted(CustomObjectEvents.CustomObjectWorkflowDemotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("customObject.workflow.demote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getCustomObject().getId());
        object.setType("customObject");
        as.setObject(object);


        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getWorkflowId());
        context.setType("workflow");
        as.setContext(context);

        ActivityStreamObject source = new ActivityStreamObject();
        source.setObject(event.getFromActivityId());
        source.setType("workflowactivity");
        as.setSource(source);

        ActivityStreamObject target = new ActivityStreamObject();
        target.setObject(event.getToActivityId());
        target.setType("workflowactivity");
        as.setTarget(target);

        ASWorkflowPromote promote = new ASWorkflowPromote(event.getWorkflowId(), event.getWorkflowName(), event.getFromActivityName(), event.getToActivityName());
        try {
            as.setData(objectMapper.writeValueAsString(promote));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void customObjectWorkflowHold(CustomObjectEvents.CustomObjectWorkflowHoldEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("customObject.workflow.hold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getCustomObject().getId());
        object.setType("customObject");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getWorkflowId());
        context.setType("workflow");
        as.setContext(context);

        ActivityStreamObject source = new ActivityStreamObject();
        source.setObject(event.getActivityId());
        source.setType("workflowstatus");
        as.setSource(source);

        ASWorkflowHold asWorkflowHold = new ASWorkflowHold(event.getWorkflowId(), event.getWorkflowName(), event.getActivityName());
        try {
            as.setData(objectMapper.writeValueAsString(asWorkflowHold));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void customObjectWorkflowUnhold(CustomObjectEvents.CustomObjectWorkflowUnholdEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("customObject.workflow.unhold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getCustomObject().getId());
        object.setType("customObject");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getWorkflowId());
        context.setType("workflow");
        as.setContext(context);

        ActivityStreamObject source = new ActivityStreamObject();
        source.setObject(event.getActivityId());
        source.setType("workflowstatus");
        as.setSource(source);
        ASWorkflowHold asWorkflowHold = new ASWorkflowHold(event.getWorkflowId(), event.getWorkflowName(), event.getActivityName());
        try {
            as.setData(objectMapper.writeValueAsString(asWorkflowHold));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        activityStreamService.create(as);
    }


    @Override
    public String getConverterKey() {
        return "customObject";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            CustomObject customObject = customObjectRepository.findOne(object.getObject());
            String customObjectType = "";
            if (customObject == null) return "";

            if (customObject.getObjectType().equals(ObjectType.valueOf(ObjectType.CUSTOMOBJECT.toString()))) {
                customObjectType = "customObject";
            }

            String activity = as.getActivity();
            switch (activity) {
                case "customObject.create":
                    convertedString = getCustomObjectCreatedString(messageString, actor, customObject);
                    break;
                case "customObject.update.basicinfo":
                    convertedString = getCustomObjectBasicInfoUpdatedString(messageString, actor, customObject, customObjectType, as);
                    break;
                case "customObject.update.attributes":
                    convertedString = getCustomObjectAttributeUpdatedString(messageString, actor, customObject, as);
                    break;
                case "customObject.files.add":
                    convertedString = getCustomObjectFilesAddedString(messageString, customObject, as);
                    break;
                case "customObject.files.delete":
                    convertedString = getCustomObjectFileDeletedString(messageString, customObject, as);
                    break;
                case "customObject.files.folders.add":
                    convertedString = getCustomObjectFoldersAddedString(messageString, customObject, as);
                    break;
                case "customObject.files.folders.delete":
                    convertedString = getCustomObjectFoldersDeletedString(messageString, customObject, as);
                    break;
                case "customObject.files.version":
                    convertedString = getCustomObjectFilesVersionedString(messageString, customObject, as);
                    break;
                case "customObject.files.rename":
                    convertedString = getCustomObjectFileRenamedString(messageString, customObject, as);
                    break;
                case "customObject.files.replace":
                    convertedString = getCustomObjectFileRenamedString(messageString, customObject, as);
                    break;
                case "customObject.files.lock":
                    convertedString = getCustomObjectFileString(messageString, customObject, as);
                    break;
                case "customObject.files.unlock":
                    convertedString = getCustomObjectFileString(messageString, customObject, as);
                    break;
                case "customObject.files.download":
                    convertedString = getCustomObjectFileString(messageString, customObject, as);
                    break;
                case "customObject.comment":
                    convertedString = getCustomObjectCommentString(messageString, customObject, as);
                    break;
                case "customObject.bom.add":
                    convertedString = getCustomObjectBomAddedString(messageString, customObject, as);
                    break;
                case "customObject.bom.update":
                    convertedString = getBomItemUpdatedString(messageString, actor, customObject, as);
                    break;
                case "customObject.bom.delete":
                    convertedString = getCustomObjectBomDeletedString(messageString, customObject, as);
                    break;
                case "customObject.related.add":
                    convertedString = getCustomObjectRelatedAddedString(messageString, customObject, as);
                    break;
                case "customObject.related.update":
                    convertedString = getCustomObjectRelatedUpdatedString(messageString, actor, customObject, as);
                    break;
                case "customObject.related.delete":
                    convertedString = getCustomObjectRelatedDeletedString(messageString, customObject, as);
                    break;
                case "customObject.workflow.start":
                    convertedString = getCustomObjectWorkflowStartString(messageString, customObject, as);
                    break;
                case "customObject.workflow.finish":
                    convertedString = getCustomObjectWorkflowStartString(messageString, customObject, as);
                    break;
                case "customObject.workflow.promote":
                    convertedString = getCustomObjectWorkflowPromoteDemoteString(messageString, customObject, as);
                    break;
                case "customObject.workflow.demote":
                    convertedString = getCustomObjectWorkflowPromoteDemoteString(messageString, customObject, as);
                    break;
                case "customObject.workflow.hold":
                    convertedString = getCustomObjectWorkflowHoldUnholdString(messageString, customObject, as);
                    break;
                case "customObject.workflow.unhold":
                    convertedString = getCustomObjectWorkflowHoldUnholdString(messageString, customObject, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getCustomObjectCreatedString(String messageString, Person actor, CustomObject customObject) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), customObject.getNumber());
    }


    private String getCustomObjectBasicInfoUpdatedString(String messageString, Person actor, CustomObject customObject, String type, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), type, customObject.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("customObject.update.basicinfo.property");

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

    private String getCustomObjectBasicInfoUpdatedJson(CustomObject oldCustomObject, CustomObject newCustomObject) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldCustomObject.getName();
        String newValue = newCustomObject.getName();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        oldValue = oldCustomObject.getDescription();
        newValue = newCustomObject.getDescription();
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


    private String getCustomObjectAttributesUpdatedJson(CustomObjectAttribute oldAttribute, CustomObjectAttribute newAttribute) {
        List<ASAttributeChangeDTO> changes = new ArrayList<>();

        String oldValue = "";
        if (oldAttribute != null) {
            oldValue = oldAttribute.getValueAsString();
        }
        String newValue = newAttribute.getValueAsString();

        CustomObjectTypeAttribute attDef = customObjectTypeAttributeRepository.findOne(newAttribute.getId().getAttributeDef());
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

    private String getCustomObjectAttributeUpdatedString(String messageString, Person actor, CustomObject customObject, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), customObject.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("customObject.update.attributes.attribute");

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

    private String getCustomObjectFilesAddedJson(List<CustomFile> files) {
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

    private String getCustomObjectFoldersAddedJson(CustomFile file) {
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

    private String getCustomObjectFoldersDeletedJson(CustomFile file) {
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

    private String getCustomObjectFilesAddedString(String messageString, CustomObject customObject, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), customObject.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("customObject.files.add.file");

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

    private String getCustomObjectFoldersAddedString(String messageString, CustomObject customObject, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), customObject.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getCustomObjectFoldersDeletedString(String messageString, CustomObject customObject, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), customObject.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getCustomObjectFileDeletedString(String messageString, CustomObject customObject, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), customObject.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getCustomObjectFilesVersionedJson(List<CustomFile> files) {
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

    private String getCustomObjectFilesVersionedString(String messageString, CustomObject customObject, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), customObject.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("customObject.files.version.file");

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

    private String getCustomObjectFileRenamedString(String messageString, CustomObject customObject, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    customObject.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getCustomObjectFileString(String messageString, CustomObject customObject, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    customObject.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getCustomObjectCommentString(String messageString, CustomObject customObject, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                customObject.getNumber());

        String s = highlightValue("Comment: ");
        s += as.getData();
        s = addMarginToMessage(s);

        message += s;

        return message;
    }

    private String getCustomObjectBomAddedString(String messageString, CustomObject customObject, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                customObject.getNumber());
        StringBuffer sb = new StringBuffer();
        sb.append(message);

        String itemString = activityStreamResourceBundle.getString("customObject.bom.customBom");

        String json = as.getData();
        try {
            List<ASBomCustomObject> items = objectMapper.readValue(as.getData(), new TypeReference<List<ASBomCustomObject>>() {
            });
            for (ASBomCustomObject item : items) {
                String s = addMarginToMessage(MessageFormat.format(itemString, highlightValue(item.getNumber()),
                        item.getQuantity(), item.getNotes()));
                sb.append(s);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getBomItemUpdatedString(String messageString, Person actor, CustomObject object, ActivityStream as) {
        String updateString = activityStreamResourceBundle.getString("customObject.bom.update.property");
        StringBuffer sb = new StringBuffer();
        String json = as.getData();
        try {
            List<ASBomCustomObjectUpdate> propChanges = objectMapper.readValue(json, new TypeReference<List<ASBomCustomObjectUpdate>>() {
            });
            String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), highlightValue(object.getNumber()),
                    highlightValue(propChanges.get(0).getNumber()));

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

    private String getCustomObjectBomDeletedString(String messageString, CustomObject object, ActivityStream as) {
        String message = "";
        try {
            ASBomCustomObject item = objectMapper.readValue(as.getData(), new TypeReference<ASBomCustomObject>() {
            });
            message = MessageFormat.format(messageString, as.getActor().getFullName().trim(), highlightValue(item.getNumber()),
                    object.getNumber());

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        return message;
    }

    private String getCustomBomUpdatedJson(CustomObjectBom customObjectBom, CustomObjectBom objectBom) {
        List<ASBomCustomObjectUpdate> changes = new ArrayList<>();

        String oldValue = customObjectBom.getQuantity().toString();
        String newValue = objectBom.getQuantity().toString();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASBomCustomObjectUpdate(objectBom.getChild().getNumber(), "Quantity", oldValue, newValue));
        }

        oldValue = customObjectBom.getNotes();
        newValue = objectBom.getNotes();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!oldValue.equals(newValue)) {
            changes.add(new ASBomCustomObjectUpdate(objectBom.getChild().getNumber(), "Notes", oldValue, newValue));
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


    private String getCustomObjectRelatedAddedString(String messageString, CustomObject customObject, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                customObject.getNumber());
        StringBuffer sb = new StringBuffer();
        sb.append(message);

        String itemString = activityStreamResourceBundle.getString("customObject.related.relatedObject");

        String json = as.getData();
        try {
            List<ASRelatedObject> items = objectMapper.readValue(as.getData(), new TypeReference<List<ASRelatedObject>>() {
            });
            for (ASRelatedObject item : items) {
                String s = addMarginToMessage(MessageFormat.format(itemString, highlightValue(item.getNumber()),
                        item.getNotes()));
                sb.append(s);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getCustomObjectRelatedUpdatedString(String messageString, Person actor, CustomObject object, ActivityStream as) {
        String updateString = activityStreamResourceBundle.getString("customObject.related.update.property");
        StringBuffer sb = new StringBuffer();
        String json = as.getData();
        try {
            List<ASRelatedObjectUpdate> propChanges = objectMapper.readValue(json, new TypeReference<List<ASRelatedObjectUpdate>>() {
            });
            String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), highlightValue(object.getNumber()),
                    highlightValue(propChanges.get(0).getNumber()));

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

    private String getCustomObjectRelatedDeletedString(String messageString, CustomObject object, ActivityStream as) {
        String message = "";
        try {
            ASBomCustomObject item = objectMapper.readValue(as.getData(), new TypeReference<ASBomCustomObject>() {
            });
            message = MessageFormat.format(messageString, as.getActor().getFullName().trim(), highlightValue(item.getNumber()),
                    object.getNumber());

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        return message;
    }

    private String getCustomObjectRelatedUpdatedJson(CustomObjectRelated oldRelatedObject, CustomObjectRelated objectRelated) {
        List<ASBomCustomObjectUpdate> changes = new ArrayList<>();

        String oldValue = oldRelatedObject.getNotes();
        String newValue = objectRelated.getNotes();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!oldValue.equals(newValue)) {
            changes.add(new ASBomCustomObjectUpdate(objectRelated.getRelated().getNumber(), "Notes", oldValue, newValue));
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


    private String getCustomObjectWorkflowStartString(String messageString, CustomObject object, ActivityStream as) {
        String message = "";
        try {
            ASWorkflowStart workflow = objectMapper.readValue(as.getData(), new TypeReference<ASWorkflowStart>() {
            });
            message = MessageFormat.format(messageString, as.getActor().getFullName().trim(), highlightValue(workflow.getWorkflowName()),
                    object.getNumber());

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        return message;
    }

    

    private String getCustomObjectWorkflowPromoteDemoteString(String messageString, CustomObject object, ActivityStream as) {

        String message = "";
        try {
            ASWorkflowPromote workflow = objectMapper.readValue(as.getData(), new TypeReference<ASWorkflowPromote>() {
            });
            message = MessageFormat.format(messageString, as.getActor().getFullName().trim(), highlightValue(workflow.getWorkflowName()),
                    object.getNumber(), highlightValue(workflow.getFromActivityName()), highlightValue(workflow.getToActivityName()));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return message;

    }

    private String getCustomObjectWorkflowHoldUnholdString(String messageString, CustomObject object, ActivityStream as) {

        String message = "";
        try {
            ASWorkflowHold workflow = objectMapper.readValue(as.getData(), new TypeReference<ASWorkflowHold>() {
            });
            message = MessageFormat.format(messageString, as.getActor().getFullName().trim(), highlightValue(workflow.getActivityName()), highlightValue(workflow.getWorkflowName()),
                    object.getNumber());

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return message;
    }

}
