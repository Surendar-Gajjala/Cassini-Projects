package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.SparePartsEvents;
import com.cassinisys.plm.model.mro.*;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.repo.mro.MROObjectTypeAttributeRepository;
import com.cassinisys.plm.repo.mro.MROSparePartRepository;
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
public class SparePartActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MROSparePartRepository sparePartRepository;
    @Autowired
    private MROObjectTypeAttributeRepository mroObjectTypeAttributeRepository;
    @Autowired
    private PersonRepository personRepository;

    @Async
    @EventListener
    public void sparePartsCreated(SparePartsEvents.SparePartCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSparePart().getId());

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mrosparepart.create");
        object.setType("mrosparepart");

        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void sparePartBasicInfoUpdated(SparePartsEvents.SparePartBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        MROSparePart oldSparePart = event.getOldSparePart();
        MROSparePart newSparePart = event.getNewSparePart();

        object.setObject(newSparePart.getId());
        object.setType("mrosparepart");
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mrosparepart.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getSparePartBasicInfoUpdatedJson(oldSparePart, newSparePart));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void sparePartAttributesUpdated(SparePartsEvents.SparePartAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        MROObject sparePart = event.getSparePart();

        MROObjectAttribute oldAtt = event.getOldAttribute();
        MROObjectAttribute newAtt = event.getNewAttribute();
        object.setObject(sparePart.getId());
        ActivityStream as = new ActivityStream();
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getSparePartAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void sparePartFilesAdded(SparePartsEvents.SparePartFilesAddedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mrosparepart.files.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setType("mrosparepart");
        object.setObject(event.getSparePart());
        as.setObject(object);
        as.setData(getSparePartFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void sparePartFoldersAdded(SparePartsEvents.SparePartFoldersAddedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.sparePart.files.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setType("mrosparepart");
        object.setObject(event.getSparePart());
        as.setObject(object);
        as.setData(getSparePartFoldersAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void sparePartFoldersDeleted(SparePartsEvents.SparePartFoldersDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mrosparepart.files.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setType("mrosparepart");
        object.setObject(event.getSparePart());
        as.setObject(object);
        as.setData(getSparePartFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void sparePartFileDeleted(SparePartsEvents.SparePartFileDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.sparePart.files.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setType("mrosparepart");
        object.setObject(event.getSparePart());
        as.setObject(object);
        as.setData(getSparePartFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void sparePartFilesVersioned(SparePartsEvents.SparePartFilesVersionedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mrosparepart.files.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setType("mrosparepart");
        object.setObject(event.getSparePart());
        as.setObject(object);
        as.setData(getSparePartFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void sparePartFileRenamed(SparePartsEvents.SparePartFileRenamedEvent event) {
        PLMFile oldFile = event.getOldFile();
        PLMFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals("Rename")) as.setActivity("plm.mro.sparePart.files.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.mro.sparePart.files.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setType("mrosparepart");
        object.setObject(event.getSparePart());
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
    public void sparePartFileLocked(SparePartsEvents.SparePartFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mrosparepart.files.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSparePart());
        object.setType("mrosparepart");
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
    public void sparePartFileUnlocked(SparePartsEvents.SparePartFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mrosparepart.files.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSparePart());
        object.setType("mrosparepart");
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
    public void sparePartFileDownloaded(SparePartsEvents.SparePartFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mrosparepart.files.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setType("mrosparepart");
        object.setObject(event.getSparePart());
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
    public void sparePartCommentAdded(SparePartsEvents.SparePartCommentAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mrosparepart.comment");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setType("mrosparepart");
        object.setObject(event.getSparePart().getId());
        as.setObject(object);
        as.setData(event.getComment().getComment());

        activityStreamService.create(as);
    }


    @Override
    public String getConverterKey() {
        return "plm.mro.mrosparepart";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String mrosageString = activityStreamResourceBundle.getString(as.getActivity());

        if (mrosageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            MROSparePart sparePart = sparePartRepository.findOne(object.getObject());
            String sparePartType = "";
            if (sparePart == null) return "";

            if (sparePart.getObjectType().equals(ObjectType.valueOf(MROEnumObject.MROSPAREPART.toString()))) {
                sparePartType = "mrosparepart";
            }

            String activity = as.getActivity();
            switch (activity) {
                case "plm.mro.mrosparepart.create":
                    convertedString = getSparePartCreatedString(mrosageString, actor, sparePart);
                    break;
                case "plm.mro.mrosparepart.update.basicinfo":
                    convertedString = getSparePartBasicInfoUpdatedString(mrosageString, actor, sparePart, sparePartType, as);
                    break;
                case "plm.mro.mrosparepart.update.attributes":
                    convertedString = getSparePartAttributeUpdatedString(mrosageString, actor, sparePart, as);
                    break;
                case "plm.mro.mrosparepart.files.add":
                    convertedString = getSparePartFilesAddedString(mrosageString, sparePart, as);
                    break;
                case "plm.mro.mrosparepart.files.delete":
                    convertedString = getSparePartFileDeletedString(mrosageString, sparePart, as);
                    break;
                case "plm.mro.mrosparepart.files.folders.add":
                    convertedString = getSparePartFoldersAddedString(mrosageString, sparePart, as);
                    break;
                case "plm.mro.mrosparepart.files.folders.delete":
                    convertedString = getSparePartFoldersDeletedString(mrosageString, sparePart, as);
                    break;
                case "plm.mro.mrosparepart.files.version":
                    convertedString = getSparePartFilesVersionedString(mrosageString, sparePart, as);
                    break;
                case "plm.mro.mrosparepart.files.rename":
                    convertedString = getSparePartFileRenamedString(mrosageString, sparePart, as);
                    break;
                case "plm.mro.mrosparepart.files.replace":
                    convertedString = getSparePartFileRenamedString(mrosageString, sparePart, as);
                    break;
                case "plm.mro.mrosparepart.files.lock":
                    convertedString = getSparePartFileString(mrosageString, sparePart, as);
                    break;
                case "plm.mro.mrosparepart.files.unlock":
                    convertedString = getSparePartFileString(mrosageString, sparePart, as);
                    break;
                case "plm.mro.mrosparepart.files.download":
                    convertedString = getSparePartFileString(mrosageString, sparePart, as);
                    break;
                case "plm.mro.mrosparepart.comment":
                    convertedString = getSparePartCommentString(mrosageString, sparePart, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getSparePartCreatedString(String mrosageString, Person actor, MROSparePart sparePart) {
        return MessageFormat.format(mrosageString, actor.getFullName().trim(), sparePart.getNumber());
    }


    private String getSparePartBasicInfoUpdatedString(String mrosageString, Person actor, MROSparePart sparePart, String type, ActivityStream as) {
        String activityString = MessageFormat.format(mrosageString, actor.getFullName().trim(), type, sparePart.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mro.mrosparepart.update.basicinfo.property");

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

    private String getSparePartBasicInfoUpdatedJson(MROSparePart oldSparePart, MROSparePart newSparePart) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldSparePart.getName();
        String newValue = newSparePart.getName();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        oldValue = oldSparePart.getDescription();
        newValue = newSparePart.getDescription();
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


    private String getSparePartAttributesUpdatedJson(MROObjectAttribute oldAttribute, MROObjectAttribute newAttribute) {
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

    private String getSparePartAttributeUpdatedString(String mrosageString, Person actor, MROSparePart sparePart, ActivityStream as) {
        String activityString = MessageFormat.format(mrosageString, actor.getFullName().trim(), sparePart.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mro.mrosparepart.update.attributes.attribute");

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

    private String getSparePartFilesAddedJson(List<PLMFile> files) {
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

    private String getSparePartFoldersAddedJson(PLMFile file) {
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

    private String getSparePartFoldersDeletedJson(PLMFile file) {
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

    private String getSparePartFilesAddedString(String mrosageString, MROSparePart sparePart, ActivityStream as) {
        String activityString = MessageFormat.format(mrosageString, highlightValue(as.getActor().getFullName().trim()), sparePart.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mro.mrosparepart.files.add.file");

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

    private String getSparePartFoldersAddedString(String mrosageString, MROSparePart sparePart, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(mrosageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), sparePart.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getSparePartFoldersDeletedString(String mrosageString, MROSparePart sparePart, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(mrosageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), sparePart.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getSparePartFileDeletedString(String mrosageString, MROSparePart sparePart, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(mrosageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), sparePart.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getSparePartFilesVersionedJson(List<PLMFile> files) {
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

    private String getSparePartFilesVersionedString(String mrosageString, MROSparePart sparePart, ActivityStream as) {
        String activityString = MessageFormat.format(mrosageString, as.getActor().getFullName().trim(), sparePart.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mro.mrosparepart.files.version.file");

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

    private String getSparePartFileRenamedString(String mrosageString, MROSparePart sparePart, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            mrosageString = MessageFormat.format(mrosageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    sparePart.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return mrosageString;
    }

    private String getSparePartFileString(String mrosageString, MROSparePart sparePart, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            mrosageString = MessageFormat.format(mrosageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    sparePart.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return mrosageString;
    }

    private String getSparePartCommentString(String mrosageString, MROSparePart sparePart, ActivityStream as) {
        String mrosage = MessageFormat.format(mrosageString, as.getActor().getFullName().trim(),
                sparePart.getNumber());

        String s = highlightValue("Comment: ");
        s += as.getData();
        s = addMarginToMessage(s);

        mrosage += s;

        return mrosage;
    }

}
