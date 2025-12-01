package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.MeterEvents;
import com.cassinisys.plm.model.mro.MROMeter;
import com.cassinisys.plm.model.mro.MROObject;
import com.cassinisys.plm.model.mro.MROObjectAttribute;
import com.cassinisys.plm.model.mro.MROObjectTypeAttribute;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.repo.mro.MROMeterRepository;
import com.cassinisys.plm.repo.mro.MROObjectTypeAttributeRepository;
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
 * Created by Lenovo on 18-11-2020.
 */
@Component
public class MeterActivityStream extends BaseActivityStream implements ActivityStreamConverter {


    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MROMeterRepository mroMeterRepository;
    @Autowired
    private MROObjectTypeAttributeRepository mroObjectTypeAttributeRepository;
    @Autowired
    private PersonRepository personRepository;

    @Async
    @EventListener
    public void metersCreated(MeterEvents.MeterCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMeter().getId());
        object.setType("mrometer");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mrometer.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void meterBasicInfoUpdated(MeterEvents.MeterBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        MROMeter oldMeter = event.getOldMeter();
        MROMeter newMeter = event.getNewMeter();

        object.setObject(newMeter.getId());
        object.setType("mrometer");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mrometer.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getMeterBasicInfoUpdatedJson(oldMeter, newMeter));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }


    @Async
    @EventListener
    public void meterAttributesUpdated(MeterEvents.MeterAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        MROObject sparePart = event.getMeter();

        MROObjectAttribute oldAtt = event.getOldAttribute();
        MROObjectAttribute newAtt = event.getNewAttribute();
        object.setObject(sparePart.getId());
        ActivityStream as = new ActivityStream();
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getMeterAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void meterFilesAdded(MeterEvents.MeterFilesAddedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mrometer.files.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMeter());
        object.setType("mrometer");
        as.setObject(object);
        as.setData(getMeterFilesAddedJson(files));

        activityStreamService.create(as);
    }

    private String getMeterAttributesUpdatedJson(MROObjectAttribute oldAttribute, MROObjectAttribute newAttribute) {
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

    private String getMeterAttributeUpdatedString(String mrosageString, Person actor, MROMeter sparePart, ActivityStream as) {
        String activityString = MessageFormat.format(mrosageString, actor.getFullName().trim(), sparePart.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mro.mrometer.update.attributes.attribute");

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

    @Async
    @EventListener
    public void meterFoldersAdded(MeterEvents.MeterFoldersAddedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mrometer.files.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMeter());
        object.setType("mrometer");
        as.setObject(object);
        as.setData(getMeterFoldersAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void meterFoldersDeleted(MeterEvents.MeterFoldersDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mrometer.files.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMeter());
        object.setType("mrometer");
        as.setObject(object);
        as.setData(getMeterFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void meterFileDeleted(MeterEvents.MeterFileDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mrometer.files.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMeter());
        object.setType("mrometer");
        as.setObject(object);
        as.setData(getMeterFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void meterFilesVersioned(MeterEvents.MeterFilesVersionedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mrometer.files.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMeter());
        object.setType("mrometer");
        as.setObject(object);
        as.setData(getMeterFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void meterFileRenamed(MeterEvents.MeterFileRenamedEvent event) {
        PLMFile oldFile = event.getOldFile();
        PLMFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals("Rename")) as.setActivity("plm.mro.mrometer.files.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.mro.mrometer.files.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMeter());
        object.setType("mrometer");
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
    public void meterFileLocked(MeterEvents.MeterFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mrometer.files.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMeter());
        object.setType("mrometer");
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
    public void meterFileUnlocked(MeterEvents.MeterFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mrometer.files.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMeter());
        object.setType("mrometer");
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
    public void meterFileDownloaded(MeterEvents.MeterFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mrometer.files.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMeter());
        object.setType("mrometer");
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
    public void meterCommentAdded(MeterEvents.MeterCommentAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mrometer.comment");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMeter().getId());
        object.setType("mrometer");
        as.setObject(object);
        as.setData(event.getComment().getComment());

        activityStreamService.create(as);
    }


    @Override
    public String getConverterKey() {
        return "plm.mro.mrometer";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            MROMeter meter = mroMeterRepository.findOne(object.getObject());
            String meterType = "mrometer";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.mro.mrometer.create":
                    convertedString = getMeterCreatedString(messageString, actor, meter);
                    break;
                case "plm.mro.mrometer.update.basicinfo":
                    convertedString = getMeterBasicInfoUpdatedString(messageString, actor, meter, as);
                    break;
                case "plm.mro.mrometer.update.attributes":
                    convertedString = getMeterAttributeUpdatedString(messageString, actor, meter, as);
                    break;
                case "plm.mro.mrometer.files.add":
                    convertedString = getMeterFilesAddedString(messageString, meter, as);
                    break;
                case "plm.mro.mrometer.files.delete":
                    convertedString = getMeterFileDeletedString(messageString, meter, as);
                    break;
                case "plm.mro.mrometer.files.folders.add":
                    convertedString = getMeterFoldersAddedString(messageString, meter, as);
                    break;
                case "plm.mro.mrometer.files.folders.delete":
                    convertedString = getMeterFoldersDeletedString(messageString, meter, as);
                    break;
                case "plm.mro.mrometer.files.version":
                    convertedString = getMeterFilesVersionedString(messageString, meter, as);
                    break;
                case "plm.mro.mrometer.files.rename":
                    convertedString = getMeterFileRenamedString(messageString, meter, as);
                    break;
                case "plm.mro.mrometer.files.replace":
                    convertedString = getMeterFileRenamedString(messageString, meter, as);
                    break;
                case "plm.mro.mrometer.files.lock":
                    convertedString = getMeterFileString(messageString, meter, as);
                    break;
                case "plm.mro.mrometer.files.unlock":
                    convertedString = getMeterFileString(messageString, meter, as);
                    break;
                case "plm.mro.mrometer.files.download":
                    convertedString = getMeterFileString(messageString, meter, as);
                    break;
                case "plm.mro.mrometer.comment":
                    convertedString = getMeterCommentString(messageString, meter, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getMeterCreatedString(String messageString, Person actor, MROMeter meter) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), meter.getName());
    }


    private String getMeterBasicInfoUpdatedString(String messageString, Person actor, MROMeter meter, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), meter.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mro.mrometer.update.basicinfo.property");

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

    private String getMeterBasicInfoUpdatedJson(MROMeter oldMeter, MROMeter newMeter) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldMeter.getName();
        String newValue = newMeter.getName();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        oldValue = oldMeter.getDescription();
        newValue = newMeter.getDescription();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue, newValue));
        }

        oldValue = oldMeter.getMeterType().toString();
        newValue = newMeter.getMeterType().toString();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("MeterType", oldValue, newValue));
        }
        oldValue = oldMeter.getMeterReadingType().toString();
        newValue = newMeter.getMeterReadingType().toString();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("MeterReadingType", oldValue, newValue));
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

    private String getMeterFilesAddedJson(List<PLMFile> files) {
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

    private String getMeterFoldersAddedJson(PLMFile file) {
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

    private String getMeterFoldersDeletedJson(PLMFile file) {
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

    private String getMeterFilesAddedString(String messageString, MROMeter meter, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), meter.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mro.mrometer.files.add.file");

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

    private String getMeterFoldersAddedString(String messageString, MROMeter meter, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), meter.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getMeterFoldersDeletedString(String messageString, MROMeter meter, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), meter.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getMeterFileDeletedString(String messageString, MROMeter meter, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), meter.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getMeterFilesVersionedJson(List<PLMFile> files) {
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

    private String getMeterFilesVersionedString(String messageString, MROMeter meter, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), meter.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mro.mrometer.files.version.file");

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

    private String getMeterFileRenamedString(String messageString, MROMeter meter, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    meter.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getMeterFileString(String messageString, MROMeter meter, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    meter.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getMeterCommentString(String messageString, MROMeter meter, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                meter.getName());

        String s = highlightValue("Comment: ");
        s += as.getData();
        s = addMarginToMessage(s);

        message += s;

        return message;
    }
}
