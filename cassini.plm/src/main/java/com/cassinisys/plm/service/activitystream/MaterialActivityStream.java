package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.MaterialEvents;
import com.cassinisys.plm.model.mes.MESMaterial;
import com.cassinisys.plm.model.mes.MESObjectAttribute;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.pqm.PQMQualityTypeAttribute;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.repo.mes.MESMaterialRepository;
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
public class MaterialActivityStream extends BaseActivityStream implements ActivityStreamConverter {
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
    private MESMaterialRepository materialRepository;

    @Async
    @EventListener
    public void materialCreated(MaterialEvents.MaterialCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMaterial().getId());
        object.setType("material");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.material.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void materialBasicInfoUpdated(MaterialEvents.MaterialBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        MESMaterial oldPlan = event.getOldMaterial();
        MESMaterial newPlan = event.getMaterial();

        object.setObject(newPlan.getId());
        object.setType("material");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.material.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getMaterialBasicInfoUpdatedJson(oldPlan, newPlan));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void materialAttributesUpdated(MaterialEvents.MaterialAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        MESMaterial material = event.getMaterial();

        MESObjectAttribute oldAtt = event.getOldAttribute();
        MESObjectAttribute newAtt = event.getNewAttribute();

        object.setObject(material.getId());
        object.setType("material");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.material.update.attributes");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getMaterialAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void materialFilesAdded(MaterialEvents.MaterialFilesAddedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.material.files.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMaterial());
        object.setType("material");
        as.setObject(object);
        as.setData(getMaterialFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void materialFoldersAdded(MaterialEvents.MaterialFoldersAddedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.material.files.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMaterial());
        object.setType("material");
        as.setObject(object);
        as.setData(getMaterialFoldersAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void materialFoldersDeleted(MaterialEvents.MaterialFoldersDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.material.files.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMaterial());
        object.setType("material");
        as.setObject(object);
        as.setData(getMaterialFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void materialFileDeleted(MaterialEvents.MaterialFileDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.material.files.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMaterial());
        object.setType("material");
        as.setObject(object);
        as.setData(getMaterialFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void materialFilesVersioned(MaterialEvents.MaterialFilesVersionedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.material.files.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMaterial());
        object.setType("material");
        as.setObject(object);
        as.setData(getMaterialFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void materialFileRenamed(MaterialEvents.MaterialFileRenamedEvent event) {
        PLMFile oldFile = event.getOldFile();
        PLMFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals("Rename")) as.setActivity("plm.mes.material.files.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.mes.material.files.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMaterial());
        object.setType("material");
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
    public void materialFileLocked(MaterialEvents.MaterialFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.material.files.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMaterial());
        object.setType("material");
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
    public void materialFileUnlocked(MaterialEvents.MaterialFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.material.files.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMaterial());
        object.setType("material");
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
    public void materialFileDownloaded(MaterialEvents.MaterialFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.material.files.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMaterial());
        object.setType("material");
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
    public void materialCommentAdded(MaterialEvents.MaterialCommentAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.material.comment");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMaterial().getId());
        object.setType("material");
        as.setObject(object);
        as.setData(event.getComment().getComment());

        activityStreamService.create(as);
    }

    @Override
    public String getConverterKey() {
        return "plm.mes.material";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            MESMaterial material = materialRepository.findOne(object.getObject());

            if (material == null) return "";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.mes.material.create":
                    convertedString = getMaterialCreatedString(messageString, actor, material);
                    break;
                case "plm.mes.material.update.basicinfo":
                    convertedString = getMaterialBasicInfoUpdatedString(messageString, actor, material, as);
                    break;
                case "plm.mes.material.update.attributes":
                    convertedString = getMaterialAttributeUpdatedString(messageString, actor, material, as);
                    break;
                case "plm.mes.material.files.add":
                    convertedString = getMaterialFilesAddedString(messageString, material, as);
                    break;
                case "plm.mes.material.files.delete":
                    convertedString = getMaterialFileDeletedString(messageString, material, as);
                    break;
                case "plm.mes.material.files.folders.add":
                    convertedString = getMaterialFoldersAddedString(messageString, material, as);
                    break;
                case "plm.mes.material.files.folders.delete":
                    convertedString = getMaterialFoldersDeletedString(messageString, material, as);
                    break;
                case "plm.mes.material.files.version":
                    convertedString = getMaterialFilesVersionedString(messageString, material, as);
                    break;
                case "plm.mes.material.files.rename":
                    convertedString = getMaterialFileRenamedString(messageString, material, as);
                    break;
                case "plm.mes.material.files.replace":
                    convertedString = getMaterialFileRenamedString(messageString, material, as);
                    break;
                case "plm.mes.material.files.lock":
                    convertedString = getMaterialFileString(messageString, material, as);
                    break;
                case "plm.mes.material.files.unlock":
                    convertedString = getMaterialFileString(messageString, material, as);
                    break;
                case "plm.mes.material.files.download":
                    convertedString = getMaterialFileString(messageString, material, as);
                    break;
                case "plm.mes.material.workflow.start":
                    convertedString = getMaterialWorkflowStartString(messageString, material, as);
                    break;
                case "plm.mes.material.workflow.finish":
                    convertedString = getMaterialWorkflowFinishString(messageString, material, as);
                    break;
                case "plm.mes.material.workflow.promote":
                    convertedString = getMaterialWorkflowPromoteDemoteString(messageString, material, as);
                    break;
                case "plm.mes.material.workflow.demote":
                    convertedString = getMaterialWorkflowPromoteDemoteString(messageString, material, as);
                    break;
                case "plm.mes.material.workflow.hold":
                    convertedString = getMaterialWorkflowHoldUnholdString(messageString, material, as);
                    break;
                case "plm.mes.material.workflow.unhold":
                    convertedString = getMaterialWorkflowHoldUnholdString(messageString, material, as);
                    break;
                case "plm.mes.material.workflow.add":
                    convertedString = getMaterialWorkflowAddedString(messageString, material, as);
                    break;
                case "plm.mes.material.workflow.change":
                    convertedString = getMaterialWorkflowChangeString(messageString, material, as);
                    break;
                case "plm.mes.material.comment":
                    convertedString = getMaterialCommentString(messageString, material, as);
                    break;
                case "plm.mes.material.relatedItems.add":
                    convertedString = getMaterialRelatedItemsAddedString(messageString, material, as);
                    break;
                case "plm.mes.material.relatedItems.delete":
                    convertedString = getMaterialRelatedItemsDeletedString(messageString, material, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getMaterialCreatedString(String messageString, Person actor, MESMaterial material) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), material.getNumber());
    }

    private String getMaterialRevisionCreatedString(String messageString, Person actor, MESMaterial material) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), material.getNumber());
    }

    private String getMaterialBasicInfoUpdatedString(String messageString, Person actor, MESMaterial material, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), material.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mes.material.update.basicinfo.property");

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

    private String getMaterialBasicInfoUpdatedJson(MESMaterial oldMaterial, MESMaterial material) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldMaterial.getName();
        String newValue = material.getName();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        oldValue = oldMaterial.getDescription();
        newValue = material.getDescription();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue, newValue));
        }

        oldValue = oldMaterial.getQomObject().getName();
        if (material.getQomObject() != null) {
            newValue = material.getQomObject().getName();
        }
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Measurement", oldValue, newValue));
        }

        if (oldMaterial.getUomObject() != null) {
            oldValue = oldMaterial.getUomObject().getName();
        }
        if (material.getUomObject() != null) {
            newValue = material.getUomObject().getName();
        }
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Measurement unit", oldValue, newValue));
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


    private String getMaterialAttributesUpdatedJson(MESObjectAttribute oldAttribute, MESObjectAttribute newAttribute) {
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

    private String getMaterialAttributeUpdatedString(String messageString, Person actor, MESMaterial material, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), material.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mes.material.update.attributes.attribute");

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

    private String getMaterialFilesAddedJson(List<PLMFile> files) {
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

    private String getMaterialFoldersAddedJson(PLMFile file) {
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

    private String getMaterialFoldersDeletedJson(PLMFile file) {
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

    private String getMaterialFilesAddedString(String messageString, MESMaterial material, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), material.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mes.material.files.add.file");

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

    private String getMaterialFoldersAddedString(String messageString, MESMaterial material, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), material.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getMaterialFoldersDeletedString(String messageString, MESMaterial material, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), material.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getMaterialFileDeletedString(String messageString, MESMaterial material, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), material.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getMaterialFilesVersionedJson(List<PLMFile> files) {
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

    private String getMaterialFilesVersionedString(String messageString, MESMaterial material, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), material.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mes.material.files.version.file");

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

    private String getMaterialFileRenamedString(String messageString, MESMaterial material, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    material.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getMaterialFileString(String messageString, MESMaterial material, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    material.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getMaterialWorkflowStartString(String messageString, MESMaterial material, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                material.getNumber());
    }

    private String getMaterialWorkflowFinishString(String messageString, MESMaterial material, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                material.getNumber());
    }

    private String getMaterialWorkflowPromoteDemoteString(String messageString, MESMaterial material, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();
        ActivityStreamObject target = as.getTarget();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus fromActivity = workflowStatusRepository.findOne(source.getObject());
        PLMWorkflowStatus toActivity = workflowStatusRepository.findOne(target.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                material.getNumber(),
                highlightValue(fromActivity.getName()),
                highlightValue(toActivity.getName()));
    }

    private String getMaterialWorkflowHoldUnholdString(String messageString, MESMaterial material, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus plmWorkflowActivity = workflowStatusRepository.findOne(source.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflowActivity.getName()),
                highlightValue(plmWorkflow.getName()),
                material.getNumber());
    }

    private String getMaterialWorkflowAddedString(String messageString, MESMaterial material, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getNewValue()),
                    material.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getMaterialWorkflowChangeString(String messageString, MESMaterial material, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getOldValue()),
                    highlightValue(workflow.getNewValue()),
                    material.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }


    private String getMaterialCommentString(String messageString, MESMaterial material, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                material.getNumber());

        String s = highlightValue("Comment: ");
        s += as.getData();
        s = addMarginToMessage(s);

        message += s;

        return message;
    }

    private String getMaterialRelatedItemsAddedString(String messageString, MESMaterial material, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), material.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = "";
        if (material.getObjectType().equals(ObjectType.valueOf(PLMObjectType.ITEMINSPECTION.toString()))) {
            fileString = activityStreamResourceBundle.getString("plm.mes.material.relatedItems.add.item");
        } else {
            fileString = activityStreamResourceBundle.getString("plm.mes.material.relatedItems.add.part");
        }

        String json = as.getData();
        try {
            if (material.getObjectType().equals(ObjectType.valueOf(PLMObjectType.ITEMINSPECTION.toString()))) {
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

    private String getMaterialRelatedItemsDeletedString(String messageString, MESMaterial material, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            if (material.getObjectType().equals(ObjectType.valueOf(PLMObjectType.ITEMINSPECTION.toString()))) {
                List<ASNewPRProblemItem> asNewPRProblemItems = objectMapper.readValue(json, new TypeReference<List<ASNewPRProblemItem>>() {
                });
                ASNewPRProblemItem asNewPRProblemItem = asNewPRProblemItems.get(0);
                activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(asNewPRProblemItem.getItemName()),
                        highlightValue(asNewPRProblemItem.getRevision()), highlightValue(asNewPRProblemItem.getLifecyclePhase()), material.getNumber());
            } else {
                List<ASNewNCRProblemItem> asNewPRProblemItems = objectMapper.readValue(json, new TypeReference<List<ASNewNCRProblemItem>>() {
                });
                ASNewNCRProblemItem asNewPRProblemItem = asNewPRProblemItems.get(0);
                activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(asNewPRProblemItem.getPartNumber()),
                        material.getNumber());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }
}
