package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.AssetEvents;
import com.cassinisys.plm.model.mro.MROAsset;
import com.cassinisys.plm.model.mro.MROAssetSparePart;
import com.cassinisys.plm.model.mro.MROObjectAttribute;
import com.cassinisys.plm.model.mro.MROObjectTypeAttribute;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.repo.mro.MROAssetRepository;
import com.cassinisys.plm.repo.mro.MROObjectRepository;
import com.cassinisys.plm.repo.mro.MROObjectTypeAttributeRepository;
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
public class AssetActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MROAssetRepository mroAssetRepositoy;
    @Autowired
    private MROObjectTypeAttributeRepository mroObjectTypeAttributeRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private MROObjectRepository mroObjectRepository;

    @Async
    @EventListener
    public void assetsCreated(AssetEvents.AssetCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getAsset().getId());
        object.setType("mroasset");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroasset.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void assetBasicInfoUpdated(AssetEvents.AssetBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        MROAsset oldAsset = event.getOldAsset();
        MROAsset newAsset = event.getNewAsset();

        object.setObject(newAsset.getId());
        object.setType("mroasset");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroasset.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getAssetBasicInfoUpdatedJson(oldAsset, newAsset));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void AssetAttributesUpdated(AssetEvents.AssetAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        ActivityStream as = new ActivityStream();
        MROObjectAttribute oldAtt = event.getOldAttribute();
        MROObjectAttribute newAtt = event.getNewAttribute();
        as.setActivity("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase() + ".update.attributes");
        as.setConverter("plm." + event.getParentType() + "." + event.getObjectType().toString().toLowerCase());
        object.setObject(event.getObjectId());
        object.setType(event.getObjectType().toString().toLowerCase());
        as.setObject(object);
        as.setData(getAssetAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }


    @Async
    @EventListener
    public void assetSparePartCreated(AssetEvents.AssetSparePartCreatedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroasset.spareparts.create");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getAsset());
        object.setType("mroasset");
        as.setObject(object);
        as.setData(getAssetSparePartCreatedJson(event.getAssetSpareParts()));

        activityStreamService.create(as);
    }


    @Async
    @EventListener
    public void assetSparePartDeleted(AssetEvents.AssetSparePartDeletedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroasset.spareparts.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getAsset());
        object.setType("mroasset");
        as.setObject(object);
        as.setData(getAssetSparePartDeletedJson(event.getAssetSparePart()));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void assetFilesAdded(AssetEvents.AssetFilesAddedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroasset.files.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getAsset());
        object.setType("mroasset");
        as.setObject(object);
        as.setData(getAssetFilesAddedJson(files));

        activityStreamService.create(as);
    }


    private String getAssetSparePartCreatedJson(List<MROAssetSparePart> spareParts) {
        List<ASSparePartDTO> changes = new ArrayList<>();

        for (MROAssetSparePart sparePart : spareParts) {
            changes.add(new ASSparePartDTO(sparePart.getSparePart().getName(),null));
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

    private String getAssetSparePartDeletedJson(MROAssetSparePart sparePart) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        changes.add(new ASPropertyChangeDTO("Name", sparePart.getSparePart().getName(), null));
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


    private String getAssetAttributesUpdatedJson(MROObjectAttribute oldAttribute, MROObjectAttribute newAttribute) {
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

    private String getAssetAttributeUpdatedString(String mrosageString, Person actor, MROAsset sparePart, ActivityStream as) {
        String activityString = MessageFormat.format(mrosageString, actor.getFullName().trim(), sparePart.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mro.mroasset.update.attributes.attribute");

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
    public void assetFoldersAdded(AssetEvents.AssetFoldersAddedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroasset.files.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getAsset());
        object.setType("mroasset");
        as.setObject(object);
        as.setData(getAssetFoldersAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void assetFoldersDeleted(AssetEvents.AssetFoldersDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroasset.files.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getAsset());
        object.setType("mroasset");
        as.setObject(object);
        as.setData(getAssetFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void assetFileDeleted(AssetEvents.AssetFileDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroasset.files.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getAsset());
        object.setType("mroasset");
        as.setObject(object);
        as.setData(getAssetFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void assetFilesVersioned(AssetEvents.AssetFilesVersionedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroasset.files.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getAsset());
        object.setType("mroasset");
        as.setObject(object);
        as.setData(getAssetFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void assetFileRenamed(AssetEvents.AssetFileRenamedEvent event) {
        PLMFile oldFile = event.getOldFile();
        PLMFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals("Rename")) as.setActivity("plm.mro.mroasset.files.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.mro.mroasset.files.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getAsset());
        object.setType("mroasset");
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
    public void assetFileLocked(AssetEvents.AssetFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroasset.files.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getAsset());
        object.setType("mroasset");
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
    public void assetFileUnlocked(AssetEvents.AssetFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroasset.files.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getAsset());
        object.setType("mroasset");
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
    public void assetFileDownloaded(AssetEvents.AssetFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroasset.files.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getAsset());
        object.setType("mroasset");
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
    public void assetCommentAdded(AssetEvents.AssetCommentAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroasset.comment");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getAsset().getId());
        object.setType("mroasset");
        as.setObject(object);
        as.setData(event.getComment().getComment());

        activityStreamService.create(as);
    }


    @Override
    public String getConverterKey() {
        return "plm.mro.mroasset";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            MROAsset asset = mroAssetRepositoy.findOne(object.getObject());
            String assetType = "mroasset";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.mro.mroasset.create":
                    convertedString = getAssetCreatedString(messageString, actor, asset);
                    break;
                case "plm.mro.mroasset.update.basicinfo":
                    convertedString = getAssetBasicInfoUpdatedString(messageString, actor, asset, as);
                    break;
                case "plm.mro.mroasset.update.attributes":
                    convertedString = getAssetAttributeUpdatedString(messageString, actor, asset, as);
                    break;
                case "plm.mro.mroasset.spareparts.create":
                    convertedString = getAssetSparePartCreatedString(messageString, actor, asset, as);
                    break;
                case "plm.mro.mroasset.spareparts.delete":
                    convertedString = getAssetSparePartDeletedString(messageString, actor, asset, as);
                    break;
                case "plm.mro.mroasset.files.add":
                    convertedString = getAssetFilesAddedString(messageString, asset, as);
                    break;
                case "plm.mro.mroasset.files.delete":
                    convertedString = getAssetFileDeletedString(messageString, asset, as);
                    break;
                case "plm.mro.mroasset.files.folders.add":
                    convertedString = getAssetFoldersAddedString(messageString, asset, as);
                    break;
                case "plm.mro.mroasset.files.folders.delete":
                    convertedString = getAssetFoldersDeletedString(messageString, asset, as);
                    break;
                case "plm.mro.mroasset.files.version":
                    convertedString = getAssetFilesVersionedString(messageString, asset, as);
                    break;
                case "plm.mro.mroasset.files.rename":
                    convertedString = getAssetFileRenamedString(messageString, asset, as);
                    break;
                case "plm.mro.mroasset.files.replace":
                    convertedString = getAssetFileRenamedString(messageString, asset, as);
                    break;
                case "plm.mro.mroasset.files.lock":
                    convertedString = getAssetFileString(messageString, asset, as);
                    break;
                case "plm.mro.mroasset.files.unlock":
                    convertedString = getAssetFileString(messageString, asset, as);
                    break;
                case "plm.mro.mroasset.files.download":
                    convertedString = getAssetFileString(messageString, asset, as);
                    break;
                case "plm.mro.mroasset.comment":
                    convertedString = getAssetCommentString(messageString, asset, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getAssetCreatedString(String messageString, Person actor, MROAsset asset) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), asset.getName());
    }


    private String getAssetBasicInfoUpdatedString(String messageString, Person actor, MROAsset asset, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), asset.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mro.mroasset.update.basicinfo.property");

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

    private String getAssetBasicInfoUpdatedJson(MROAsset oldAsset, MROAsset newAsset) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldAsset.getName();
        String newValue = newAsset.getName();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        oldValue = oldAsset.getDescription();
        newValue = newAsset.getDescription();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue, newValue));
        }


        oldValue = oldAsset.getMetered().toString();
        newValue = newAsset.getMetered().toString();

        if (!newValue.equals(oldValue)) {
            String newValuee;
            String oldValuee;
            if (newValue == "true") {
                newValuee = "yes";
            } else {
                newValuee = "No";
            }
            if (oldValue == "true") {
                oldValuee = "yes";
            } else {
                oldValuee = "No";
            }
            changes.add(new ASPropertyChangeDTO("Meter", oldValuee, newValuee));
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

    private String getAssetFilesAddedJson(List<PLMFile> files) {
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

    private String getAssetFoldersAddedJson(PLMFile file) {
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

    private String getAssetFoldersDeletedJson(PLMFile file) {
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

    private String getAssetFilesAddedString(String messageString, MROAsset asset, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), asset.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mro.mroasset.files.add.file");

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

    private String getAssetFoldersAddedString(String messageString, MROAsset asset, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), asset.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getAssetFoldersDeletedString(String messageString, MROAsset asset, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), asset.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getAssetFileDeletedString(String messageString, MROAsset asset, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), asset.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getAssetFilesVersionedJson(List<PLMFile> files) {
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

    private String getAssetFilesVersionedString(String messageString, MROAsset asset, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), asset.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mro.mroasset.files.version.file");

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

    private String getAssetFileRenamedString(String messageString, MROAsset asset, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    asset.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getAssetFileString(String messageString, MROAsset asset, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    asset.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getAssetCommentString(String messageString, MROAsset asset, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                asset.getName());

        String s = highlightValue("Comment: ");
        s += as.getData();
        s = addMarginToMessage(s);

        message += s;

        return message;
    }


    private String getAssetSparePartCreatedString(String messageString, Person actor, MROAsset asset, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), asset.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mro.mroasset.spareparts.create.add");
        String json = as.getData();
        try {
            List<ASSparePartDTO> propChanges = objectMapper.readValue(json, new TypeReference<List<ASSparePartDTO>>() {
            });
            propChanges.forEach(p -> {
                String s = addMarginToMessage(MessageFormat.format(updateString,
                        highlightValue(p.getName())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getAssetSparePartDeletedString(String messageString, Person actor, MROAsset asset, ActivityStream as) {
        String json = as.getData();
        String activityString = "";
        try {
            List<ASPropertyChangeDTO> propChanges = objectMapper.readValue(json, new TypeReference<List<ASPropertyChangeDTO>>() {
            });
            activityString = MessageFormat.format(messageString, actor.getFullName().trim(), highlightValue(propChanges.get(0).getOldValue()), asset.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return activityString;
    }

}
