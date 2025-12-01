package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.SubstanceEvents;
import com.cassinisys.plm.model.pgc.PGCObjectAttribute;
import com.cassinisys.plm.model.pgc.PGCObjectTypeAttribute;
import com.cassinisys.plm.model.pgc.PGCSubstance;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.repo.pgc.PGCObjectTypeAttributeRepository;
import com.cassinisys.plm.repo.pgc.PGCSubstanceRepository;
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

/**
 * Created by Suresh Cassini on 30-11-2020.
 */
@Component
public class SubstanceActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PGCObjectTypeAttributeRepository pgcTypeAttributeRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PGCSubstanceRepository substanceRepository;

    @Async
    @EventListener
    public void substanceCreated(SubstanceEvents.SubstanceCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSubstance().getId());
        object.setType("substance");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pgc.pgcsubstance.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void substanceBasicInfoUpdated(SubstanceEvents.SubstanceBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PGCSubstance oldPlan = event.getOldSubstance();
        PGCSubstance newPlan = event.getSubstance();

        object.setObject(newPlan.getId());
        object.setType("substance");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pgc.pgcsubstance.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getSubstanceBasicInfoUpdatedJson(oldPlan, newPlan));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void substanceAttributesUpdated(SubstanceEvents.SubstanceAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();


        PGCObjectAttribute oldAtt = event.getOldAttribute();
        PGCObjectAttribute newAtt = event.getNewAttribute();
        ActivityStream as = new ActivityStream();
        as.setActivity("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase() + ".update.attributes");
        as.setConverter("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase());
        object.setObject(event.getObjectId());
        object.setType(event.getObjectType().toString().toLowerCase());
        as.setObject(object);
        //as.setConverter(getConverterKey());
        as.setData(getSubstanceAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void substanceFilesAdded(SubstanceEvents.SubstanceFilesAddedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase() + ".files.add");
        as.setConverter("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSubstance());
        object.setType(event.getObjectType().toString().toLowerCase());
        as.setObject(object);
        as.setData(getSubstanceFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void substanceFoldersAdded(SubstanceEvents.SubstanceFoldersAddedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase() + ".files.folders.add");
        as.setConverter("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSubstance());
        object.setType(event.getObjectType().toString().toLowerCase());
        as.setObject(object);
        as.setData(getSubstanceFoldersAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void substanceFoldersDeleted(SubstanceEvents.SubstanceFoldersDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase() + ".files.folders.delete");
        as.setConverter("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSubstance());
        object.setType(event.getObjectType().toString().toLowerCase());
        as.setObject(object);
        as.setData(getSubstanceFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void substanceFileDeleted(SubstanceEvents.SubstanceFileDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase() + ".files.delete");
        as.setConverter("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSubstance());
        object.setType(event.getObjectType().toString().toLowerCase());
        as.setObject(object);
        as.setData(getSubstanceFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void substanceFilesVersioned(SubstanceEvents.SubstanceFilesVersionedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase() + ".files.version");
        as.setConverter("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSubstance());
        object.setType(event.getObjectType().toString().toLowerCase());
        as.setObject(object);
        as.setData(getSubstanceFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void substanceFileRenamed(SubstanceEvents.SubstanceFileRenamedEvent event) {
        PLMFile oldFile = event.getOldFile();
        PLMFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals("Rename"))
            as.setActivity("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase() + ".files.rename");
        if (event.getType().equals("Replace"))
            as.setActivity("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase() + ".files.replace");
        as.setConverter("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSubstance());
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
    public void substanceFileLocked(SubstanceEvents.SubstanceFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase() + ".files.lock");
        as.setConverter("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSubstance());
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
    public void substanceFileUnlocked(SubstanceEvents.SubstanceFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase() + ".files.unlock");
        as.setConverter("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSubstance());
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
    public void substanceFileDownloaded(SubstanceEvents.SubstanceFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase() + ".files.download");
        as.setConverter("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSubstance());
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
    public void substanceCommentAdded(SubstanceEvents.SubstanceCommentAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pgc.pgcsubstance.comment");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSubstance().getId());
        object.setType("substance");
        as.setObject(object);
        as.setData(event.getComment().getComment());

        activityStreamService.create(as);
    }

    @Override
    public String getConverterKey() {
        return "plm.pgc.pgcsubstance";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            PGCSubstance substance = substanceRepository.findOne(object.getObject());

            if (substance == null) return "";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.pgc.pgcsubstance.create":
                    convertedString = getSubstanceCreatedString(messageString, actor, substance);
                    break;
                case "plm.pgc.pgcsubstance.update.basicinfo":
                    convertedString = getSubstanceBasicInfoUpdatedString(messageString, actor, substance, as);
                    break;
                case "plm.pgc.pgcsubstance.update.attributes":
                    convertedString = getSubstanceAttributeUpdatedString(messageString, actor, substance, as);
                    break;
                case "plm.pgc.pgcsubstance.files.add":
                    convertedString = getSubstanceFilesAddedString(messageString, substance, as);
                    break;
                case "plm.pgc.pgcsubstance.files.delete":
                    convertedString = getSubstanceFileDeletedString(messageString, substance, as);
                    break;
                case "plm.pgc.pgcsubstance.files.folders.add":
                    convertedString = getSubstanceFoldersAddedString(messageString, substance, as);
                    break;
                case "plm.pgc.pgcsubstance.files.folders.delete":
                    convertedString = getSubstanceFoldersDeletedString(messageString, substance, as);
                    break;
                case "plm.pgc.pgcsubstance.files.version":
                    convertedString = getSubstanceFilesVersionedString(messageString, substance, as);
                    break;
                case "plm.pgc.pgcsubstance.files.rename":
                    convertedString = getSubstanceFileRenamedString(messageString, substance, as);
                    break;
                case "plm.pgc.pgcsubstance.files.replace":
                    convertedString = getSubstanceFileRenamedString(messageString, substance, as);
                    break;
                case "plm.pgc.pgcsubstance.files.lock":
                    convertedString = getSubstanceFileString(messageString, substance, as);
                    break;
                case "plm.pgc.pgcsubstance.files.unlock":
                    convertedString = getSubstanceFileString(messageString, substance, as);
                    break;
                case "plm.pgc.pgcsubstance.files.download":
                    convertedString = getSubstanceFileString(messageString, substance, as);
                    break;
                case "plm.pgc.pgcsubstance.comment":
                    convertedString = getSubstanceCommentString(messageString, substance, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getSubstanceCreatedString(String messageString, Person actor, PGCSubstance substance) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), substance.getNumber());
    }

    private String getSubstanceRevisionCreatedString(String messageString, Person actor, PGCSubstance substance) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), substance.getNumber());
    }

    private String getSubstanceBasicInfoUpdatedString(String messageString, Person actor, PGCSubstance substance, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), substance.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.pgc.pgcsubstance.update.basicinfo.property");

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

    private String getSubstanceBasicInfoUpdatedJson(PGCSubstance oldSubstance, PGCSubstance substance) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldSubstance.getName();
        String newValue = substance.getName();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        oldValue = oldSubstance.getDescription();
        newValue = substance.getDescription();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue, newValue));
        }

        oldValue = oldSubstance.getCasNumber();
        newValue = substance.getCasNumber();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("CAS Number", oldValue, newValue));
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


    private String getSubstanceAttributesUpdatedJson(PGCObjectAttribute oldAttribute, PGCObjectAttribute newAttribute) {
        List<ASAttributeChangeDTO> changes = new ArrayList<>();

        String oldValue = "";
        if (oldAttribute != null) {
            oldValue = oldAttribute.getValueAsString();
        }
        String newValue = newAttribute.getValueAsString();

        PGCObjectTypeAttribute attDef = pgcTypeAttributeRepository.findOne(newAttribute.getId().getAttributeDef());
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

    private String getSubstanceAttributeUpdatedString(String messageString, Person actor, PGCSubstance substance, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), substance.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.pgc.pgcsubstance.update.attributes.attribute");

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

    private String getSubstanceFilesAddedJson(List<PLMFile> files) {
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

    private String getSubstanceFoldersAddedJson(PLMFile file) {
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

    private String getSubstanceFoldersDeletedJson(PLMFile file) {
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

    private String getSubstanceFilesAddedString(String messageString, PGCSubstance substance, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), substance.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.pgc.pgcsubstance.files.add.file");

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

    private String getSubstanceFoldersAddedString(String messageString, PGCSubstance substance, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), substance.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getSubstanceFoldersDeletedString(String messageString, PGCSubstance substance, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), substance.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getSubstanceFileDeletedString(String messageString, PGCSubstance substance, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), substance.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getSubstanceFilesVersionedJson(List<PLMFile> files) {
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

    private String getSubstanceFilesVersionedString(String messageString, PGCSubstance substance, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), substance.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.pgc.pgcsubstance.files.version.file");

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

    private String getSubstanceFileRenamedString(String messageString, PGCSubstance substance, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    substance.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getSubstanceFileString(String messageString, PGCSubstance substance, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    substance.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }


    private String getSubstanceCommentString(String messageString, PGCSubstance substance, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                substance.getNumber());

        String s = highlightValue("Comment: ");
        s += as.getData();
        s = addMarginToMessage(s);

        message += s;

        return message;
    }

}
