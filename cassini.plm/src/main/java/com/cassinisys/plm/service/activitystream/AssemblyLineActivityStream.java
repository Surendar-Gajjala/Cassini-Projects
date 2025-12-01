package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.AssemblyLineEvents;
import com.cassinisys.plm.model.mes.*;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.repo.mes.MESAssemblyLineRepository;
import com.cassinisys.plm.repo.mes.MESObjectRepository;
import com.cassinisys.plm.repo.mes.MESObjectTypeAttributeRepository;
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
public class AssemblyLineActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MESAssemblyLineRepository mesAssemblyLineRepo;
    @Autowired
    private MESObjectTypeAttributeRepository mesObjectTypeAttributeRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private MESObjectRepository mesObjectRepository;

    @Async
    @EventListener
    public void assemblyLinesCreated(AssemblyLineEvents.AssemblyLineCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getAssemblyLine().getId());
        object.setType("assemblyLine");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.assemblyline.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void assemblyLineBasicInfoUpdated(AssemblyLineEvents.AssemblyLineBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        MESAssemblyLine oldPlan = event.getOldAssemblyLine();
        MESAssemblyLine newPlan = event.getNewAssemblyLine();

        object.setObject(newPlan.getId());
        object.setType("assemblyLine");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.assemblyline.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getAssemblyLineBasicInfoUpdatedJson(oldPlan, newPlan));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void assemblyLineAttributesUpdated(AssemblyLineEvents.AssemblyLineAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        MESObjectAttribute oldAtt = event.getOldAttribute();
        MESObjectAttribute newAtt = event.getNewAttribute();
        object.setObject(event.getObjectId());
        ActivityStream as = new ActivityStream();
        as.setActivity("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase() + ".update.attributes");
        as.setConverter("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase());
        object.setObject(event.getObjectId());
        object.setType(event.getObjectType().toString().toLowerCase());
        as.setObject(object);
        as.setData(getAssemblyLineAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void assemblyLineFilesAdded(AssemblyLineEvents.AssemblyLineFilesAddedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.assemblyline.files.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getAssemblyLine());
        object.setType("assemblyLine");
        as.setObject(object);
        as.setData(getAssemblyLineFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void assemblyLineFoldersAdded(AssemblyLineEvents.AssemblyLineFoldersAddedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.assemblyline.files.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getAssemblyLine());
        object.setType("assemblyLine");
        as.setObject(object);
        as.setData(getAssemblyLineFoldersAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void assemblyLineFoldersDeleted(AssemblyLineEvents.AssemblyLineFoldersDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.assemblyline.files.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getAssemblyLine());
        object.setType("assemblyLine");
        as.setObject(object);
        as.setData(getAssemblyLineFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void assemblyLineFileDeleted(AssemblyLineEvents.AssemblyLineFileDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.assemblyline.files.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getAssemblyLine());
        object.setType("assemblyLine");
        as.setObject(object);
        as.setData(getAssemblyLineFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void assemblyLineFilesVersioned(AssemblyLineEvents.AssemblyLineFilesVersionedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.assemblyline.files.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getAssemblyLine());
        object.setType("assemblyLine");
        as.setObject(object);
        as.setData(getAssemblyLineFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void assemblyLineFileRenamed(AssemblyLineEvents.AssemblyLineFileRenamedEvent event) {
        PLMFile oldFile = event.getOldFile();
        PLMFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals("Rename")) as.setActivity("plm.mes.assemblyline.files.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.mes.assemblyline.files.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getAssemblyLine());
        object.setType("assemblyLine");
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
    public void assemblyLineFileLocked(AssemblyLineEvents.AssemblyLineFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.assemblyline.files.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getAssemblyLine());
        object.setType("assemblyLine");
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
    public void assemblyLineFileUnlocked(AssemblyLineEvents.AssemblyLineFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.assemblyline.files.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getAssemblyLine());
        object.setType("assemblyLine");
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
    public void assemblyLineFileDownloaded(AssemblyLineEvents.AssemblyLineFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.assemblyline.files.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getAssemblyLine());
        object.setType("assemblyLine");
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
    public void assemblyLineWorkCentersAdded(AssemblyLineEvents.AssemblyLineWorkCentersAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.assemblyline.workCenters.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getAssemblyLine().getId());
        object.setType("assemblyLine");
        as.setObject(object);
        as.setData(getAssemblyLineWorkCntersAddedJson(event.getWorkCenters()));
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void assemblyLineWorkCenterDeleted(AssemblyLineEvents.AssemblyLineWorkCenterDeletedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.assemblyline.workCenters.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getAssemblyLine().getId());
        object.setType("assemblyLine");
        as.setObject(object);
        try {
            as.setData(objectMapper.writeValueAsString(new ASNewAssemblyLineWorkCenter(event.getWorkCenter().getId(), event.getWorkCenter().getNumber(),
                    event.getWorkCenter().getName())));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void assemblyLineCommentAdded(AssemblyLineEvents.AssemblyLineCommentAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.assemblyline.comment");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getAssemblyLine().getId());
        object.setType("assemblyLine");
        as.setObject(object);
        as.setData(event.getComment().getComment());

        activityStreamService.create(as);
    }


    @Override
    public String getConverterKey() {
        return "plm.mes.assemblyline";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            MESAssemblyLine assemblyLine = mesAssemblyLineRepo.findOne(object.getObject());
            String assemblyLineType = "";
            if (assemblyLine == null) return "";

            if (assemblyLine.getObjectType().equals(ObjectType.valueOf(MESEnumObject.ASSEMBLYLINE.toString()))) {
                assemblyLineType = "assemblyLine";
            }

            String activity = as.getActivity();
            switch (activity) {
                case "plm.mes.assemblyline.create":
                    convertedString = getAssemblyLineCreatedString(messageString, actor, assemblyLine);
                    break;
                case "plm.mes.assemblyline.update.basicinfo":
                    convertedString = getAssemblyLineBasicInfoUpdatedString(messageString, actor, assemblyLine, assemblyLineType, as);
                    break;
                case "plm.mes.assemblyline.update.attributes":
                    convertedString = getAssemblyLineAttributeUpdatedString(messageString, actor, assemblyLine, as);
                    break;
                case "plm.mes.assemblyline.files.add":
                    convertedString = getAssemblyLineFilesAddedString(messageString, assemblyLine, as);
                    break;
                case "plm.mes.assemblyline.files.delete":
                    convertedString = getAssemblyLineFileDeletedString(messageString, assemblyLine, as);
                    break;
                case "plm.mes.assemblyline.files.folders.add":
                    convertedString = getAssemblyLineFoldersAddedString(messageString, assemblyLine, as);
                    break;
                case "plm.mes.assemblyline.files.folders.delete":
                    convertedString = getAssemblyLineFoldersDeletedString(messageString, assemblyLine, as);
                    break;
                case "plm.mes.assemblyline.files.version":
                    convertedString = getAssemblyLineFilesVersionedString(messageString, assemblyLine, as);
                    break;
                case "plm.mes.assemblyline.files.rename":
                    convertedString = getAssemblyLineFileRenamedString(messageString, assemblyLine, as);
                    break;
                case "plm.mes.assemblyline.files.replace":
                    convertedString = getAssemblyLineFileRenamedString(messageString, assemblyLine, as);
                    break;
                case "plm.mes.assemblyline.files.lock":
                    convertedString = getAssemblyLineFileString(messageString, assemblyLine, as);
                    break;
                case "plm.mes.assemblyline.files.unlock":
                    convertedString = getAssemblyLineFileString(messageString, assemblyLine, as);
                    break;
                case "plm.mes.assemblyline.files.download":
                    convertedString = getAssemblyLineFileString(messageString, assemblyLine, as);
                    break;
                case "plm.mes.assemblyline.comment":
                    convertedString = getAssemblyLineCommentString(messageString, assemblyLine, as);
                    break;
                case "plm.mes.assemblyline.workCenters.add":
                    convertedString = getAssemblyLineWorkCentersAddedString(messageString, assemblyLine, as);
                    break;
                case "plm.mes.assemblyline.workCenters.delete":
                    convertedString = getAssemblyLineWorkCenterDeletedString(messageString, assemblyLine, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getAssemblyLineCreatedString(String messageString, Person actor, MESAssemblyLine assemblyLine) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), assemblyLine.getNumber());
    }


    private String getAssemblyLineBasicInfoUpdatedString(String messageString, Person actor, MESAssemblyLine assemblyLine, String type, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), type, assemblyLine.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mes.assemblyline.update.basicinfo.property");

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

    private String getAssemblyLineBasicInfoUpdatedJson(MESAssemblyLine oldAssemblyLine, MESAssemblyLine newAssemblyLine) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldAssemblyLine.getName();
        String newValue = newAssemblyLine.getName();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        oldValue = oldAssemblyLine.getDescription();
        newValue = newAssemblyLine.getDescription();
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


    private String getAssemblyLineAttributesUpdatedJson(MESObjectAttribute oldAttribute, MESObjectAttribute newAttribute) {
        List<ASAttributeChangeDTO> changes = new ArrayList<>();

        String oldValue = "";
        if (oldAttribute != null) {
            oldValue = oldAttribute.getValueAsString();
        }
        String newValue = newAttribute.getValueAsString();

        MESObjectTypeAttribute attDef = mesObjectTypeAttributeRepository.findOne(newAttribute.getId().getAttributeDef());
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

    private String getAssemblyLineAttributeUpdatedString(String messageString, Person actor, MESAssemblyLine assemblyLine, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), assemblyLine.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mes.assemblyline.update.attributes.attribute");

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

    private String getAssemblyLineFilesAddedJson(List<PLMFile> files) {
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

    private String getAssemblyLineFoldersAddedJson(PLMFile file) {
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

    private String getAssemblyLineFoldersDeletedJson(PLMFile file) {
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

    private String getAssemblyLineFilesAddedString(String messageString, MESAssemblyLine assemblyLine, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), assemblyLine.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mes.assemblyline.files.add.file");

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

    private String getAssemblyLineFoldersAddedString(String messageString, MESAssemblyLine assemblyLine, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), assemblyLine.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getAssemblyLineFoldersDeletedString(String messageString, MESAssemblyLine assemblyLine, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), assemblyLine.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getAssemblyLineFileDeletedString(String messageString, MESAssemblyLine assemblyLine, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), assemblyLine.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getAssemblyLineFilesVersionedJson(List<PLMFile> files) {
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

    private String getAssemblyLineFilesVersionedString(String messageString, MESAssemblyLine assemblyLine, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), assemblyLine.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mes.assemblyline.files.version.file");

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

    private String getAssemblyLineFileRenamedString(String messageString, MESAssemblyLine assemblyLine, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    assemblyLine.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getAssemblyLineFileString(String messageString, MESAssemblyLine assemblyLine, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    assemblyLine.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getAssemblyLineCommentString(String messageString, MESAssemblyLine assemblyLine, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                assemblyLine.getNumber());

        String s = highlightValue("Comment: ");
        s += as.getData();
        s = addMarginToMessage(s);

        message += s;

        return message;
    }

    private String getAssemblyLineWorkCntersAddedJson(List<MESWorkCenter> workCenters) {
        List<ASNewAssemblyLineWorkCenter> asNewAssemblyLineWorkCenters = new ArrayList<>();
        for (MESWorkCenter workCenter : workCenters) {
            ASNewAssemblyLineWorkCenter asNewAssemblyLineWorkCenter = new ASNewAssemblyLineWorkCenter(workCenter.getId(), workCenter.getName(),
                    workCenter.getNumber());
            asNewAssemblyLineWorkCenters.add(asNewAssemblyLineWorkCenter);
        }
        String json = null;
        try {
            json = objectMapper.writeValueAsString(asNewAssemblyLineWorkCenters);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getAssemblyLineWorkCentersAddedString(String messageString, MESAssemblyLine assemblyLine, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                assemblyLine.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mes.assemblyline.workCenters.add.workCenter");

        String json = as.getData();
        try {
            List<ASNewAssemblyLineWorkCenter> assemblyLineWorkCenters = objectMapper.readValue(json, new TypeReference<List<ASNewAssemblyLineWorkCenter>>() {
            });
            assemblyLineWorkCenters.forEach(workCenter -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(workCenter.getWorkCenterNumber()), highlightValue(workCenter.getWorkCenterName())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getAssemblyLineWorkCenterDeletedString(String messageString, MESAssemblyLine assemblyLine, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            ASNewAssemblyLineWorkCenter workCenter = objectMapper.readValue(json, new TypeReference<ASNewAssemblyLineWorkCenter>() {
            });
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(workCenter.getWorkCenterNumber()),
                    assemblyLine.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

}
