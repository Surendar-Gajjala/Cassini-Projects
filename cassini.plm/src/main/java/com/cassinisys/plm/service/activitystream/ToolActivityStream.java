package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.ToolEvents;
import com.cassinisys.plm.model.mes.MESManufacturerData;
import com.cassinisys.plm.model.mes.MESObjectAttribute;
import com.cassinisys.plm.model.mes.MESTool;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.pqm.PQMQualityTypeAttribute;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.repo.mes.MESToolRepository;
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
public class ToolActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PLMWorkflowRepository workFlowRepository;
    @Autowired
    private PLMWorkflowStatusRepository workflowStatusRepository;
    @Autowired
    private QualityTypeAttributeRepository qualityTypeAttributeRepository;
    @Autowired
    private MESToolRepository toolRepository;
    @Autowired
    private MachineActivityStream machineActivityStream;
    

    @Async
    @EventListener
    public void toolCreated(ToolEvents.ToolCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getTool().getId());
        object.setType("tool");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.tool.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void toolBasicInfoUpdated(ToolEvents.ToolBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        MESTool oldPlan = event.getOldTool();
        MESTool newPlan = event.getTool();

        object.setObject(newPlan.getId());
        object.setType("tool");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.tool.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getToolBasicInfoUpdatedJson(oldPlan, newPlan));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void toolAttributesUpdated(ToolEvents.ToolAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        MESTool tool = event.getTool();

        MESObjectAttribute oldAtt = event.getOldAttribute();
        MESObjectAttribute newAtt = event.getNewAttribute();

        object.setObject(tool.getId());
        object.setType("tool");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.tool.update.attributes");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getToolAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void toolFilesAdded(ToolEvents.ToolFilesAddedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.tool.files.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getTool());
        object.setType("tool");
        as.setObject(object);
        as.setData(getToolFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void toolFoldersAdded(ToolEvents.ToolFoldersAddedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.tool.files.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getTool());
        object.setType("tool");
        as.setObject(object);
        as.setData(getToolFoldersAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void toolFoldersDeleted(ToolEvents.ToolFoldersDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.tool.files.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getTool());
        object.setType("tool");
        as.setObject(object);
        as.setData(getToolFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void toolFileDeleted(ToolEvents.ToolFileDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.tool.files.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getTool());
        object.setType("tool");
        as.setObject(object);
        as.setData(getToolFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void toolFilesVersioned(ToolEvents.ToolFilesVersionedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.tool.files.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getTool());
        object.setType("tool");
        as.setObject(object);
        as.setData(getToolFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void toolFileRenamed(ToolEvents.ToolFileRenamedEvent event) {
        PLMFile oldFile = event.getOldFile();
        PLMFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals("Rename")) as.setActivity("plm.mes.tool.files.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.mes.tool.files.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getTool());
        object.setType("tool");
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
    public void toolFileLocked(ToolEvents.ToolFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.tool.files.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getTool());
        object.setType("tool");
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
    public void toolFileUnlocked(ToolEvents.ToolFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.tool.files.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getTool());
        object.setType("tool");
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
    public void toolFileDownloaded(ToolEvents.ToolFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.tool.files.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getTool());
        object.setType("tool");
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
    public void toolCommentAdded(ToolEvents.ToolCommentAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.tool.comment");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getTool().getId());
        object.setType("tool");
        as.setObject(object);
        as.setData(event.getComment().getComment());

        activityStreamService.create(as);
    }

    @Override
    public String getConverterKey() {
        return "plm.mes.tool";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            MESTool tool = toolRepository.findOne(object.getObject());

            if (tool == null) return "";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.mes.tool.create":
                    convertedString = getToolCreatedString(messageString, actor, tool);
                    break;
                case "plm.mes.tool.update.basicinfo":
                    convertedString = getToolBasicInfoUpdatedString(messageString, actor, tool, as);
                    break;
                case "plm.mes.tool.update.attributes":
                    convertedString = getToolAttributeUpdatedString(messageString, actor, tool, as);
                    break;
                case "plm.mes.tool.files.add":
                    convertedString = getToolFilesAddedString(messageString, tool, as);
                    break;
                case "plm.mes.tool.files.delete":
                    convertedString = getToolFileDeletedString(messageString, tool, as);
                    break;
                case "plm.mes.tool.files.folders.add":
                    convertedString = getToolFoldersAddedString(messageString, tool, as);
                    break;
                case "plm.mes.tool.files.folders.delete":
                    convertedString = getToolFoldersDeletedString(messageString, tool, as);
                    break;
                case "plm.mes.tool.files.version":
                    convertedString = getToolFilesVersionedString(messageString, tool, as);
                    break;
                case "plm.mes.tool.files.rename":
                    convertedString = getToolFileRenamedString(messageString, tool, as);
                    break;
                case "plm.mes.tool.files.replace":
                    convertedString = getToolFileRenamedString(messageString, tool, as);
                    break;
                case "plm.mes.tool.files.lock":
                    convertedString = getToolFileString(messageString, tool, as);
                    break;
                case "plm.mes.tool.files.unlock":
                    convertedString = getToolFileString(messageString, tool, as);
                    break;
                case "plm.mes.tool.files.download":
                    convertedString = getToolFileString(messageString, tool, as);
                    break;
                case "plm.mes.tool.workflow.start":
                    convertedString = getToolWorkflowStartString(messageString, tool, as);
                    break;
                case "plm.mes.tool.workflow.finish":
                    convertedString = getToolWorkflowFinishString(messageString, tool, as);
                    break;
                case "plm.mes.tool.workflow.promote":
                    convertedString = getToolWorkflowPromoteDemoteString(messageString, tool, as);
                    break;
                case "plm.mes.tool.workflow.demote":
                    convertedString = getToolWorkflowPromoteDemoteString(messageString, tool, as);
                    break;
                case "plm.mes.tool.workflow.hold":
                    convertedString = getToolWorkflowHoldUnholdString(messageString, tool, as);
                    break;
                case "plm.mes.tool.workflow.unhold":
                    convertedString = getToolWorkflowHoldUnholdString(messageString, tool, as);
                    break;
                case "plm.mes.tool.workflow.add":
                    convertedString = getToolWorkflowAddedString(messageString, tool, as);
                    break;
                case "plm.mes.tool.workflow.change":
                    convertedString = getToolWorkflowChangeString(messageString, tool, as);
                    break;
                case "plm.mes.tool.comment":
                    convertedString = getToolCommentString(messageString, tool, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getToolCreatedString(String messageString, Person actor, MESTool tool) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), tool.getNumber());
    }

    private String getToolRevisionCreatedString(String messageString, Person actor, MESTool tool) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), tool.getNumber());
    }

    private String getToolBasicInfoUpdatedString(String messageString, Person actor, MESTool tool, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), tool.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mes.tool.update.basicinfo.property");

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

    private String getToolBasicInfoUpdatedJson(MESTool oldTool, MESTool newTool) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldTool.getName();
        String newValue = newTool.getName();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        oldValue = oldTool.getDescription();
        newValue = newTool.getDescription();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue, newValue));
        }


        oldValue = oldTool.getActive().toString();
        newValue = newTool.getActive().toString();

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

        oldValue = oldTool.getRequiresMaintenance().toString();
        newValue = newTool.getRequiresMaintenance().toString();

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

        MESManufacturerData oldMESMfr = oldTool.getManufacturerData();
        MESManufacturerData newMesMfr = newTool.getManufacturerData();
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


    private String getToolAttributesUpdatedJson(MESObjectAttribute oldAttribute, MESObjectAttribute newAttribute) {
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

    private String getToolAttributeUpdatedString(String messageString, Person actor, MESTool tool, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), tool.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mes.tool.update.attributes.attribute");

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

    private String getToolFilesAddedJson(List<PLMFile> files) {
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

    private String getToolFoldersAddedJson(PLMFile file) {
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

    private String getToolFoldersDeletedJson(PLMFile file) {
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

    private String getToolFilesAddedString(String messageString, MESTool tool, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), tool.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mes.tool.files.add.file");

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

    private String getToolFoldersAddedString(String messageString, MESTool tool, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), tool.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getToolFoldersDeletedString(String messageString, MESTool tool, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), tool.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getToolFileDeletedString(String messageString, MESTool tool, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), tool.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getToolFilesVersionedJson(List<PLMFile> files) {
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

    private String getToolFilesVersionedString(String messageString, MESTool tool, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), tool.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mes.tool.files.version.file");

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

    private String getToolFileRenamedString(String messageString, MESTool tool, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    tool.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getToolFileString(String messageString, MESTool tool, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    tool.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getToolWorkflowStartString(String messageString, MESTool tool, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                tool.getNumber());
    }

    private String getToolWorkflowFinishString(String messageString, MESTool tool, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                tool.getNumber());
    }

    private String getToolWorkflowPromoteDemoteString(String messageString, MESTool tool, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();
        ActivityStreamObject target = as.getTarget();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus fromActivity = workflowStatusRepository.findOne(source.getObject());
        PLMWorkflowStatus toActivity = workflowStatusRepository.findOne(target.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                tool.getNumber(),
                highlightValue(fromActivity.getName()),
                highlightValue(toActivity.getName()));
    }

    private String getToolWorkflowHoldUnholdString(String messageString, MESTool tool, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus plmWorkflowActivity = workflowStatusRepository.findOne(source.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflowActivity.getName()),
                highlightValue(plmWorkflow.getName()),
                tool.getNumber());
    }

    private String getToolWorkflowAddedString(String messageString, MESTool tool, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getNewValue()),
                    tool.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getToolWorkflowChangeString(String messageString, MESTool tool, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getOldValue()),
                    highlightValue(workflow.getNewValue()),
                    tool.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }


    private String getToolCommentString(String messageString, MESTool tool, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                tool.getNumber());

        String s = highlightValue("Comment: ");
        s += as.getData();
        s = addMarginToMessage(s);

        message += s;

        return message;
    }
}
