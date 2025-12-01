package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.WorkCenterEvents;
import com.cassinisys.plm.model.mes.MESObjectAttribute;
import com.cassinisys.plm.model.mes.MESWorkCenter;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.pqm.PQMQualityTypeAttribute;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.repo.mes.MESWorkCenterRepository;
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
public class WorkCenterActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PLMWorkflowRepository workFlowRepository;
    @Autowired
    private PLMWorkflowStatusRepository workflowStatusRepository;
    @Autowired
    private QualityTypeAttributeRepository qualityTypeAttributeRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository workflowDefinitionRepository;
    @Autowired
    private MESWorkCenterRepository workCenterRepository;

    @Async
    @EventListener
    public void workCenterCreated(WorkCenterEvents.WorkCenterCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getWorkCenter().getId());
        object.setType("workcenter");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.workcenter.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void workCenterBasicInfoUpdated(WorkCenterEvents.WorkCenterBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        MESWorkCenter oldPlan = event.getOldWorkCenter();
        MESWorkCenter newPlan = event.getWorkCenter();

        object.setObject(newPlan.getId());
        object.setType("workcenter");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.workcenter.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getWorkCenterBasicInfoUpdatedJson(oldPlan, newPlan));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void workCenterAttributesUpdated(WorkCenterEvents.WorkCenterAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        MESWorkCenter workCenter = event.getWorkCenter();

        MESObjectAttribute oldAtt = event.getOldAttribute();
        MESObjectAttribute newAtt = event.getNewAttribute();

        object.setObject(workCenter.getId());
        object.setType("workcenter");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.workcenter.update.attributes");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getWorkCenterAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void workCenterFilesAdded(WorkCenterEvents.WorkCenterFilesAddedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase() + ".files.add");
        as.setConverter("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getWorkCenter());
        object.setType(event.getObjectType().toString().toLowerCase());
        as.setObject(object);
        as.setData(getWorkCenterFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void workCenterFoldersAdded(WorkCenterEvents.WorkCenterFoldersAddedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase() + ".files.folders.add");
        as.setConverter("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getWorkCenter());
        object.setType(event.getObjectType().toString().toLowerCase());
        as.setObject(object);
        as.setData(getWorkCenterFoldersAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void workCenterFoldersDeleted(WorkCenterEvents.WorkCenterFoldersDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase() + ".files.folders.delete");
        as.setConverter("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getWorkCenter());
        object.setType(event.getObjectType().toString().toLowerCase());
        as.setObject(object);
        as.setData(getWorkCenterFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void workCenterFileDeleted(WorkCenterEvents.WorkCenterFileDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase() + ".files.delete");
        as.setConverter("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getWorkCenter());
        object.setType(event.getObjectType().toString().toLowerCase());
        as.setObject(object);
        as.setData(getWorkCenterFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void workCenterFilesVersioned(WorkCenterEvents.WorkCenterFilesVersionedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase() + ".files.version");
        as.setConverter("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getWorkCenter());
        object.setType(event.getObjectType().toString().toLowerCase());
        as.setObject(object);
        as.setData(getWorkCenterFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void workCenterFileRenamed(WorkCenterEvents.WorkCenterFileRenamedEvent event) {
        PLMFile oldFile = event.getOldFile();
        PLMFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals("Rename"))
            as.setActivity("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase() + ".files.rename");
        if (event.getType().equals("Replace"))
            as.setActivity("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase() + ".files.replace");
        as.setConverter("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getWorkCenter());
        object.setType(event.getObjectType().toString().toLowerCase());
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
    public void workCenterFileLocked(WorkCenterEvents.WorkCenterFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase() + ".files.lock");
        as.setConverter("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getWorkCenter());
        object.setType(event.getObjectType().toString().toLowerCase());
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
    public void workCenterFileUnlocked(WorkCenterEvents.WorkCenterFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase() + ".files.unlock");
        as.setConverter("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getWorkCenter());
        object.setType(event.getObjectType().toString().toLowerCase());
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
    public void workCenterFileDownloaded(WorkCenterEvents.WorkCenterFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase() + ".files.download");
        as.setConverter("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getWorkCenter());
        object.setType(event.getObjectType().toString().toLowerCase());
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
    public void workCenterCommentAdded(WorkCenterEvents.WorkCenterCommentAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.workcenter.comment");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getWorkCenter().getId());
        object.setType("workcenter");
        as.setObject(object);
        as.setData(event.getComment().getComment());

        activityStreamService.create(as);
    }

    @Override
    public String getConverterKey() {
        return "plm.mes.workcenter";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            MESWorkCenter workCenter = workCenterRepository.findOne(object.getObject());

            if (workCenter == null) return "";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.mes.workcenter.create":
                    convertedString = getWorkCenterCreatedString(messageString, actor, workCenter);
                    break;
                case "plm.mes.workcenter.update.basicinfo":
                    convertedString = getWorkCenterBasicInfoUpdatedString(messageString, actor, workCenter, as);
                    break;
                case "plm.mes.workcenter.update.attributes":
                    convertedString = getWorkCenterAttributeUpdatedString(messageString, actor, workCenter, as);
                    break;
                case "plm.mes.workcenter.files.add":
                    convertedString = getWorkCenterFilesAddedString(messageString, workCenter, as);
                    break;
                case "plm.mes.workcenter.files.delete":
                    convertedString = getWorkCenterFileDeletedString(messageString, workCenter, as);
                    break;
                case "plm.mes.workcenter.files.folders.add":
                    convertedString = getWorkCenterFoldersAddedString(messageString, workCenter, as);
                    break;
                case "plm.mes.workcenter.files.folders.delete":
                    convertedString = getWorkCenterFoldersDeletedString(messageString, workCenter, as);
                    break;
                case "plm.mes.workcenter.files.version":
                    convertedString = getWorkCenterFilesVersionedString(messageString, workCenter, as);
                    break;
                case "plm.mes.workcenter.files.rename":
                    convertedString = getWorkCenterFileRenamedString(messageString, workCenter, as);
                    break;
                case "plm.mes.workcenter.files.replace":
                    convertedString = getWorkCenterFileRenamedString(messageString, workCenter, as);
                    break;
                case "plm.mes.workcenter.files.lock":
                    convertedString = getWorkCenterFileString(messageString, workCenter, as);
                    break;
                case "plm.mes.workcenter.files.unlock":
                    convertedString = getWorkCenterFileString(messageString, workCenter, as);
                    break;
                case "plm.mes.workcenter.files.download":
                    convertedString = getWorkCenterFileString(messageString, workCenter, as);
                    break;
                case "plm.mes.workcenter.workflow.start":
                    convertedString = getWorkCenterWorkflowStartString(messageString, workCenter, as);
                    break;
                case "plm.mes.workcenter.workflow.finish":
                    convertedString = getWorkCenterWorkflowFinishString(messageString, workCenter, as);
                    break;
                case "plm.mes.workcenter.workflow.promote":
                    convertedString = getWorkCenterWorkflowPromoteDemoteString(messageString, workCenter, as);
                    break;
                case "plm.mes.workcenter.workflow.demote":
                    convertedString = getWorkCenterWorkflowPromoteDemoteString(messageString, workCenter, as);
                    break;
                case "plm.mes.workcenter.workflow.hold":
                    convertedString = getWorkCenterWorkflowHoldUnholdString(messageString, workCenter, as);
                    break;
                case "plm.mes.workcenter.workflow.unhold":
                    convertedString = getWorkCenterWorkflowHoldUnholdString(messageString, workCenter, as);
                    break;
                case "plm.mes.workcenter.workflow.add":
                    convertedString = getWorkCenterWorkflowAddedString(messageString, workCenter, as);
                    break;
                case "plm.mes.workcenter.workflow.change":
                    convertedString = getWorkCenterWorkflowChangeString(messageString, workCenter, as);
                    break;
                case "plm.mes.workcenter.comment":
                    convertedString = getWorkCenterCommentString(messageString, workCenter, as);
                    break;
                case "plm.mes.workcenter.relatedItems.add":
                    convertedString = getWorkCenterRelatedItemsAddedString(messageString, workCenter, as);
                    break;
                case "plm.mes.workcenter.relatedItems.delete":
                    convertedString = getWorkCenterRelatedItemsDeletedString(messageString, workCenter, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getWorkCenterCreatedString(String messageString, Person actor, MESWorkCenter workCenter) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), workCenter.getNumber());
    }

    private String getWorkCenterRevisionCreatedString(String messageString, Person actor, MESWorkCenter workCenter) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), workCenter.getNumber());
    }

    private String getWorkCenterBasicInfoUpdatedString(String messageString, Person actor, MESWorkCenter workCenter, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), workCenter.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mes.workcenter.update.basicinfo.property");

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

    private String getWorkCenterBasicInfoUpdatedJson(MESWorkCenter oldWorkCenter, MESWorkCenter workCenter) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldWorkCenter.getName();
        String newValue = workCenter.getName();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        oldValue = oldWorkCenter.getDescription();
        newValue = workCenter.getDescription();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue, newValue));
        }

        oldValue = oldWorkCenter.getLocation();
        newValue = workCenter.getLocation();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Location", oldValue, newValue));
        }

        oldValue = oldWorkCenter.getActive().toString();
        newValue = workCenter.getActive().toString();

        if (!newValue.equals(oldValue)) {
            String newValuee;
            String oldValuee;
            if (newValue == "true") {
                newValuee = "ACTIVE";
            } else {
                newValuee = "INACTIVE";
            }
            if (oldValue == "true") {
                oldValuee = "ACTIVE";
            } else {
                oldValuee = "INACTIVE";
            }
            changes.add(new ASPropertyChangeDTO("Status", oldValuee, newValuee));
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


    private String getWorkCenterAttributesUpdatedJson(MESObjectAttribute oldAttribute, MESObjectAttribute newAttribute) {
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

    private String getWorkCenterAttributeUpdatedString(String messageString, Person actor, MESWorkCenter workCenter, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), workCenter.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mes.workcenter.update.attributes.attribute");

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

    private String getWorkCenterFilesAddedJson(List<PLMFile> files) {
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

    private String getWorkCenterFoldersAddedJson(PLMFile file) {
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

    private String getWorkCenterFoldersDeletedJson(PLMFile file) {
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

    private String getWorkCenterFilesAddedString(String messageString, MESWorkCenter workCenter, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), workCenter.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mes.workcenter.files.add.file");

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

    private String getWorkCenterFoldersAddedString(String messageString, MESWorkCenter workCenter, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), workCenter.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getWorkCenterFoldersDeletedString(String messageString, MESWorkCenter workCenter, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), workCenter.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getWorkCenterFileDeletedString(String messageString, MESWorkCenter workCenter, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), workCenter.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getWorkCenterFilesVersionedJson(List<PLMFile> files) {
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

    private String getWorkCenterFilesVersionedString(String messageString, MESWorkCenter workCenter, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), workCenter.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mes.workcenter.files.version.file");

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

    private String getWorkCenterFileRenamedString(String messageString, MESWorkCenter workCenter, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    workCenter.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getWorkCenterFileString(String messageString, MESWorkCenter workCenter, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    workCenter.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getWorkCenterWorkflowStartString(String messageString, MESWorkCenter workCenter, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                workCenter.getNumber());
    }

    private String getWorkCenterWorkflowFinishString(String messageString, MESWorkCenter workCenter, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                workCenter.getNumber());
    }

    private String getWorkCenterWorkflowPromoteDemoteString(String messageString, MESWorkCenter workCenter, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();
        ActivityStreamObject target = as.getTarget();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus fromActivity = workflowStatusRepository.findOne(source.getObject());
        PLMWorkflowStatus toActivity = workflowStatusRepository.findOne(target.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                workCenter.getNumber(),
                highlightValue(fromActivity.getName()),
                highlightValue(toActivity.getName()));
    }

    private String getWorkCenterWorkflowHoldUnholdString(String messageString, MESWorkCenter workCenter, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus plmWorkflowActivity = workflowStatusRepository.findOne(source.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflowActivity.getName()),
                highlightValue(plmWorkflow.getName()),
                workCenter.getNumber());
    }

    private String getWorkCenterWorkflowAddedString(String messageString, MESWorkCenter workCenter, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getNewValue()),
                    workCenter.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getWorkCenterWorkflowChangeString(String messageString, MESWorkCenter workCenter, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getOldValue()),
                    highlightValue(workflow.getNewValue()),
                    workCenter.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }


    private String getWorkCenterCommentString(String messageString, MESWorkCenter workCenter, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                workCenter.getNumber());

        String s = highlightValue("Comment: ");
        s += as.getData();
        s = addMarginToMessage(s);

        message += s;

        return message;
    }

    private String getWorkCenterRelatedItemsAddedString(String messageString, MESWorkCenter workCenter, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), workCenter.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = "";
        if (workCenter.getObjectType().equals(ObjectType.valueOf(PLMObjectType.ITEMINSPECTION.toString()))) {
            fileString = activityStreamResourceBundle.getString("plm.mes.workcenter.relatedItems.add.item");
        } else {
            fileString = activityStreamResourceBundle.getString("plm.mes.workcenter.relatedItems.add.part");
        }

        String json = as.getData();
        try {
            if (workCenter.getObjectType().equals(ObjectType.valueOf(PLMObjectType.ITEMINSPECTION.toString()))) {
                List<ASNewPRProblemItem> itemList = objectMapper.readValue(json, new TypeReference<List<ASNewPRProblemItem>>() {
                });
                for (ASNewPRProblemItem item : itemList) {
                    String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(item.getItemName()), highlightValue(item.getRevision()),
                            highlightValue(item.getLifecyclePhase())));
                    sb.append(s);
                }
            } else {
                List<ASNewNCRProblemItem> partList = objectMapper.readValue(json, new TypeReference<List<ASNewNCRProblemItem>>() {
                });
                for (ASNewNCRProblemItem part : partList) {
                    String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(part.getPartName()), highlightValue(part.getPartNumber()),
                            highlightValue(part.getPartType())));
                    sb.append(s);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getWorkCenterRelatedItemsDeletedString(String messageString, MESWorkCenter workCenter, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            if (workCenter.getObjectType().equals(ObjectType.valueOf(PLMObjectType.ITEMINSPECTION.toString()))) {
                List<ASNewPRProblemItem> asNewPRProblemItems = objectMapper.readValue(json, new TypeReference<List<ASNewPRProblemItem>>() {
                });
                ASNewPRProblemItem asNewPRProblemItem = asNewPRProblemItems.get(0);
                activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(asNewPRProblemItem.getItemName()),
                        highlightValue(asNewPRProblemItem.getRevision()), highlightValue(asNewPRProblemItem.getLifecyclePhase()), workCenter.getNumber());
            } else {
                List<ASNewNCRProblemItem> asNewPRProblemItems = objectMapper.readValue(json, new TypeReference<List<ASNewNCRProblemItem>>() {
                });
                ASNewNCRProblemItem asNewPRProblemItem = asNewPRProblemItems.get(0);
                activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(asNewPRProblemItem.getPartNumber()),
                        workCenter.getNumber());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }
}
