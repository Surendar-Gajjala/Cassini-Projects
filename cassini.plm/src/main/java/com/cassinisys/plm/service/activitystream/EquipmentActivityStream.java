package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.EquipmentEvents;
import com.cassinisys.plm.model.mes.MESEquipment;
import com.cassinisys.plm.model.mes.MESManufacturerData;
import com.cassinisys.plm.model.mes.MESObjectAttribute;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.pqm.PQMQualityTypeAttribute;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.repo.mes.MESEquipmentRepository;
import com.cassinisys.plm.repo.pqm.QualityTypeAttributeRepository;
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
public class EquipmentActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PLMWorkflowRepository workFlowRepository;
    @Autowired
    private PLMWorkflowStatusRepository workflowStatusRepository;
    @Autowired
    private QualityTypeAttributeRepository qualityTypeAttributeRepository;
    @Autowired
    private MESEquipmentRepository equipmentRepository;
    @Autowired
    private MachineActivityStream machineActivityStream;

    @Async
    @EventListener
    public void equipmentCreated(EquipmentEvents.EquipmentCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEquipment().getId());
        object.setType("equipment");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.equipment.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void equipmentBasicInfoUpdated(EquipmentEvents.EquipmentBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        MESEquipment oldEquipment = event.getOldEquipment();
        MESEquipment newEquipment = event.getEquipment();

        object.setObject(newEquipment.getId());
        object.setType("equipment");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.equipment.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getEquipmentBasicInfoUpdatedJson(oldEquipment, newEquipment));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void equipmentAttributesUpdated(EquipmentEvents.EquipmentAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        MESEquipment equipment = event.getEquipment();

        MESObjectAttribute oldAtt = event.getOldAttribute();
        MESObjectAttribute newAtt = event.getNewAttribute();

        object.setObject(equipment.getId());
        object.setType("equipment");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.equipment.update.attributes");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getEquipmentAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void equipmentFilesAdded(EquipmentEvents.EquipmentFilesAddedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.equipment.files.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEquipment());
        object.setType("equipment");
        as.setObject(object);
        as.setData(getEquipmentFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void equipmentFoldersAdded(EquipmentEvents.EquipmentFoldersAddedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.equipment.files.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEquipment());
        object.setType("equipment");
        as.setObject(object);
        as.setData(getEquipmentFoldersAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void equipmentFoldersDeleted(EquipmentEvents.EquipmentFoldersDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.equipment.files.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEquipment());
        object.setType("equipment");
        as.setObject(object);
        as.setData(getEquipmentFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void equipmentFileDeleted(EquipmentEvents.EquipmentFileDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.equipment.files.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEquipment());
        object.setType("equipment");
        as.setObject(object);
        as.setData(getEquipmentFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void equipmentFilesVersioned(EquipmentEvents.EquipmentFilesVersionedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.equipment.files.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEquipment());
        object.setType("equipment");
        as.setObject(object);
        as.setData(getEquipmentFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void equipmentFileRenamed(EquipmentEvents.EquipmentFileRenamedEvent event) {
        PLMFile oldFile = event.getOldFile();
        PLMFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals("Rename")) as.setActivity("plm.mes.equipment.files.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.mes.equipment.files.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEquipment());
        object.setType("equipment");
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
    public void equipmentFileLocked(EquipmentEvents.EquipmentFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.equipment.files.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEquipment());
        object.setType("equipment");
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
    public void equipmentFileUnlocked(EquipmentEvents.EquipmentFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.equipment.files.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEquipment());
        object.setType("equipment");
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
    public void equipmentFileDownloaded(EquipmentEvents.EquipmentFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.equipment.files.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEquipment());
        object.setType("equipment");
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
    public void equipmentCommentAdded(EquipmentEvents.EquipmentCommentAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.equipment.comment");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getEquipment().getId());
        object.setType("equipment");
        as.setObject(object);
        as.setData(event.getComment().getComment());

        activityStreamService.create(as);
    }

    @Override
    public String getConverterKey() {
        return "plm.mes.equipment";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            MESEquipment equipment = equipmentRepository.findOne(object.getObject());

            if (equipment == null) return "";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.mes.equipment.create":
                    convertedString = getEquipmentCreatedString(messageString, actor, equipment);
                    break;
                case "plm.mes.equipment.update.basicinfo":
                    convertedString = getEquipmentBasicInfoUpdatedString(messageString, actor, equipment, as);
                    break;
                case "plm.mes.equipment.update.attributes":
                    convertedString = getEquipmentAttributeUpdatedString(messageString, actor, equipment, as);
                    break;
                case "plm.mes.equipment.files.add":
                    convertedString = getEquipmentFilesAddedString(messageString, equipment, as);
                    break;
                case "plm.mes.equipment.files.delete":
                    convertedString = getEquipmentFileDeletedString(messageString, equipment, as);
                    break;
                case "plm.mes.equipment.files.folders.add":
                    convertedString = getEquipmentFoldersAddedString(messageString, equipment, as);
                    break;
                case "plm.mes.equipment.files.folders.delete":
                    convertedString = getEquipmentFoldersDeletedString(messageString, equipment, as);
                    break;
                case "plm.mes.equipment.files.version":
                    convertedString = getEquipmentFilesVersionedString(messageString, equipment, as);
                    break;
                case "plm.mes.equipment.files.rename":
                    convertedString = getEquipmentFileRenamedString(messageString, equipment, as);
                    break;
                case "plm.mes.equipment.files.replace":
                    convertedString = getEquipmentFileRenamedString(messageString, equipment, as);
                    break;
                case "plm.mes.equipment.files.lock":
                    convertedString = getEquipmentFileString(messageString, equipment, as);
                    break;
                case "plm.mes.equipment.files.unlock":
                    convertedString = getEquipmentFileString(messageString, equipment, as);
                    break;
                case "plm.mes.equipment.files.download":
                    convertedString = getEquipmentFileString(messageString, equipment, as);
                    break;
                case "plm.mes.equipment.workflow.start":
                    convertedString = getEquipmentWorkflowStartString(messageString, equipment, as);
                    break;
                case "plm.mes.equipment.workflow.finish":
                    convertedString = getEquipmentWorkflowFinishString(messageString, equipment, as);
                    break;
                case "plm.mes.equipment.workflow.promote":
                    convertedString = getEquipmentWorkflowPromoteDemoteString(messageString, equipment, as);
                    break;
                case "plm.mes.equipment.workflow.demote":
                    convertedString = getEquipmentWorkflowPromoteDemoteString(messageString, equipment, as);
                    break;
                case "plm.mes.equipment.workflow.hold":
                    convertedString = getEquipmentWorkflowHoldUnholdString(messageString, equipment, as);
                    break;
                case "plm.mes.equipment.workflow.unhold":
                    convertedString = getEquipmentWorkflowHoldUnholdString(messageString, equipment, as);
                    break;
                case "plm.mes.equipment.workflow.add":
                    convertedString = getEquipmentWorkflowAddedString(messageString, equipment, as);
                    break;
                case "plm.mes.equipment.workflow.change":
                    convertedString = getEquipmentWorkflowChangeString(messageString, equipment, as);
                    break;
                case "plm.mes.equipment.comment":
                    convertedString = getEquipmentCommentString(messageString, equipment, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getEquipmentCreatedString(String messageString, Person actor, MESEquipment equipment) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), equipment.getNumber());
    }

    private String getEquipmentRevisionCreatedString(String messageString, Person actor, MESEquipment equipment) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), equipment.getNumber());
    }

    private String getEquipmentBasicInfoUpdatedString(String messageString, Person actor, MESEquipment equipment, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), equipment.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mes.equipment.update.basicinfo.property");

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

    private String getEquipmentBasicInfoUpdatedJson(MESEquipment oldEquipment, MESEquipment newEquipment) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldEquipment.getName();
        String newValue = newEquipment.getName();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        oldValue = oldEquipment.getDescription();
        newValue = newEquipment.getDescription();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue, newValue));
        }

        oldValue = oldEquipment.getActive().toString();
        newValue = newEquipment.getActive().toString();

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

        oldValue = oldEquipment.getRequiresMaintenance().toString();
        newValue = newEquipment.getRequiresMaintenance().toString();

        if (!newValue.equals(oldValue)) {
            String newValuee;
            String oldValuee;
            if (newValue == "true") {
                newValuee = "Yes";
            } else {
                newValuee = "No";
            }
            if (oldValue == "true") {
                oldValuee = "Yes";
            } else {
                oldValuee = "No";
            }
            changes.add(new ASPropertyChangeDTO("RequiresMaintenance", oldValuee, newValuee));
        }

        MESManufacturerData oldMESMfr = oldEquipment.getManufacturerData();
        MESManufacturerData newMesMfr = newEquipment.getManufacturerData();
        ASPropertyChangeDTO data = machineActivityStream.MESMfrBasicInfoUpdated(oldMESMfr, newMesMfr);
        if(data != null){
        changes.add(data);
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


    private String getEquipmentAttributesUpdatedJson(MESObjectAttribute oldAttribute, MESObjectAttribute newAttribute) {
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

    private String getEquipmentAttributeUpdatedString(String messageString, Person actor, MESEquipment equipment, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), equipment.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mes.equipment.update.attributes.attribute");

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

    private String getEquipmentFilesAddedJson(List<PLMFile> files) {
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

    private String getEquipmentFoldersAddedJson(PLMFile file) {
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

    private String getEquipmentFoldersDeletedJson(PLMFile file) {
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

    private String getEquipmentFilesAddedString(String messageString, MESEquipment equipment, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), equipment.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mes.equipment.files.add.file");

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

    private String getEquipmentFoldersAddedString(String messageString, MESEquipment equipment, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), equipment.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getEquipmentFoldersDeletedString(String messageString, MESEquipment equipment, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), equipment.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getEquipmentFileDeletedString(String messageString, MESEquipment equipment, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), equipment.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getEquipmentFilesVersionedJson(List<PLMFile> files) {
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

    private String getEquipmentFilesVersionedString(String messageString, MESEquipment equipment, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), equipment.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mes.equipment.files.version.file");

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

    private String getEquipmentFileRenamedString(String messageString, MESEquipment equipment, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    equipment.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getEquipmentFileString(String messageString, MESEquipment equipment, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    equipment.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getEquipmentWorkflowStartString(String messageString, MESEquipment equipment, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                equipment.getNumber());
    }

    private String getEquipmentWorkflowFinishString(String messageString, MESEquipment equipment, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                equipment.getNumber());
    }

    private String getEquipmentWorkflowPromoteDemoteString(String messageString, MESEquipment equipment, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();
        ActivityStreamObject target = as.getTarget();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus fromActivity = workflowStatusRepository.findOne(source.getObject());
        PLMWorkflowStatus toActivity = workflowStatusRepository.findOne(target.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                equipment.getNumber(),
                highlightValue(fromActivity.getName()),
                highlightValue(toActivity.getName()));
    }

    private String getEquipmentWorkflowHoldUnholdString(String messageString, MESEquipment equipment, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus plmWorkflowActivity = workflowStatusRepository.findOne(source.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflowActivity.getName()),
                highlightValue(plmWorkflow.getName()),
                equipment.getNumber());
    }

    private String getEquipmentWorkflowAddedString(String messageString, MESEquipment equipment, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getNewValue()),
                    equipment.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getEquipmentWorkflowChangeString(String messageString, MESEquipment equipment, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getOldValue()),
                    highlightValue(workflow.getNewValue()),
                    equipment.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }


    private String getEquipmentCommentString(String messageString, MESEquipment equipment, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                equipment.getNumber());

        String s = highlightValue("Comment: ");
        s += as.getData();
        s = addMarginToMessage(s);

        message += s;

        return message;
    }
}
