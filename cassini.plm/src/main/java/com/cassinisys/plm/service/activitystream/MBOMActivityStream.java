package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.MBOMEvents;
import com.cassinisys.plm.model.mes.*;
import com.cassinisys.plm.model.plm.PLMBom;
import com.cassinisys.plm.repo.mes.*;
import com.cassinisys.plm.repo.plm.BomRepository;
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
public class MBOMActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MESMBOMRepository mesMbomRepo;
    @Autowired
    private MESBOMItemRepository mesBomRepo;
    @Autowired
    private MESObjectTypeAttributeRepository mesObjectTypeAttributeRepository;
    @Autowired
    private MESObjectRepository mesObjectRepository;
    @Autowired
    private MESMBOMRevisionRepository mbomRevisionRepository;
    @Autowired
    private BomRepository bomRepository;

    @Async
    @EventListener
    public void mbomsCreated(MBOMEvents.MbomCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMbomRevision().getId());
        object.setType("mbom");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.mbom.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void mbomBasicInfoUpdated(MBOMEvents.MbomBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        MESMBOM oldMbom = event.getOldMbom();
        MESMBOM newMbom = event.getNewMbom();


        MESMBOMRevision mbomRevision = event.getMbomRevision();
        object.setObject(mbomRevision.getId());
        object.setType("mbom");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.mbom.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getMbomBasicInfoUpdatedJson(oldMbom, newMbom));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void mbomAttributesUpdated(MBOMEvents.MbomAttributesUpdatedEvent event) {
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
        as.setData(getMbomAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void mbomFilesAdded(MBOMEvents.MbomFilesAddedEvent event) {
        List<MESMBOMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.mbom.files.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMbomRevision().getId());
        object.setType("mbom");
        as.setObject(object);
        as.setData(getMbomFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void MbomFoldersAdded(MBOMEvents.MbomFoldersAddedEvent event) {
        MESMBOMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.mbom.files.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMbomRevision().getId());
        object.setType("mbom");
        as.setObject(object);
        as.setData(getMbomFoldersAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void mbomFoldersDeleted(MBOMEvents.MbomFoldersDeletedEvent event) {
        MESMBOMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.mbom.files.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMbomRevision().getId());
        object.setType("mbom");
        as.setObject(object);
        as.setData(getMbomFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void mbomFileDeleted(MBOMEvents.MbomFileDeletedEvent event) {
        MESMBOMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.mbom.files.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMbomRevision().getId());
        object.setType("mbom");
        as.setObject(object);
        as.setData(getMbomFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void mbomFilesVersioned(MBOMEvents.MbomFilesVersionedEvent event) {
        List<MESMBOMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.mbom.files.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMbomRevision().getId());
        object.setType("mbom");
        as.setObject(object);
        as.setData(getMbomFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void mbomFileRenamed(MBOMEvents.MbomFileRenamedEvent event) {
        MESMBOMFile oldFile = event.getOldFile();
        MESMBOMFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals("Rename")) as.setActivity("plm.mes.mbom.files.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.mes.mbom.files.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMbomRevision().getId());
        object.setType("mbom");
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
    public void mbomFileDownloaded(MBOMEvents.MbomFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.mbom.files.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMbomRevision().getId());
        object.setType("mbom");
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
    public void mbomCommentAdded(MBOMEvents.MbomCommentAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.mbom.comment");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMbomRevision().getId());
        object.setType("mbom");
        as.setObject(object);
        as.setData(event.getComment().getComment());

        activityStreamService.create(as);
    }


    @Async
    @EventListener
    public void mbomItemsCreated(MBOMEvents.MbomItemsCreatedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.mbom.bom.create");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMbomRevision().getId());
        object.setType("mbom");
        as.setObject(object);
        List<ASMbomItem> asMbomItems = new ArrayList<>();
        event.getMesMbomItems().forEach(bom -> {
            if (bom.getType().equals(MESBomItemType.NORMAL)) {
                PLMBom itemBom = bomRepository.findOne(bom.getBomItem());
                asMbomItems.add(new ASMbomItem(itemBom.getItem().getItemNumber(), itemBom.getItem().getItemName(), bom.getQuantity(), bom.getType().name()));
            } else {
                asMbomItems.add(new ASMbomItem(bom.getPhantomNumber(), bom.getPhantomName(), null, bom.getType().name()));
            }
        });
        try {
            as.setData(objectMapper.writeValueAsString(asMbomItems));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }


    @Async
    @EventListener
    public void mbomItemUpdated(MBOMEvents.MbomItemUpdatedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.mbom.bom.update");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMbomRevision().getId());
        object.setType("item");
        as.setObject(object);
        as.setData(getMbomItemUpdatedJson(event.getOldMesMbomItem(), event.getNewMesMbomItem()));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void mbomItemDeleted(MBOMEvents.MbomItemDeletedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.mbom.bom.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMbomRevision().getId());
        object.setType("mbom");
        as.setObject(object);
        ASMbomItem asMbomItem = null;
        if (event.getMbomItem().getType().equals(MESBomItemType.NORMAL)) {
            PLMBom itemMbom = bomRepository.findOne(event.getMbomItem().getBomItem());
            asMbomItem = new ASMbomItem(itemMbom.getItem().getItemNumber(), itemMbom.getItem().getItemName(),
                    event.getMbomItem().getQuantity(), event.getMbomItem().getType().name());
        } else {
            asMbomItem = new ASMbomItem(event.getMbomItem().getPhantomNumber(), event.getMbomItem().getPhantomName(),
                    event.getMbomItem().getQuantity(), event.getMbomItem().getType().name());
        }
        try {
            as.setData(objectMapper.writeValueAsString(asMbomItem));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Override
    public String getConverterKey() {
        return "plm.mes.mbom";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            MESMBOMRevision mbomRevision = mbomRevisionRepository.findOne(object.getObject());
            if (mbomRevision == null) return "";
            MESMBOM mbom = mesMbomRepo.findOne(mbomRevision.getMaster());

            if (mbom == null) return "";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.mes.mbom.create":
                    convertedString = getMbomCreatedString(messageString, actor, mbom);
                    break;
                case "plm.mes.mbom.bom.create":
                    convertedString = getMbomItemsCreatedString(messageString, mbom, mbomRevision, as);
                    break;
                case "plm.mes.mbom.bom.update":
                    convertedString = getMbomItemUpdatedString(messageString, actor, mbom, mbomRevision, as);
                    break;
                case "plm.mes.mbom.bom.delete":
                    convertedString = getMbomItemDeletedString(messageString, mbom, mbomRevision, as);
                    break;
                case "plm.mes.mbom.update.basicinfo":
                    convertedString = getMbomBasicInfoUpdatedString(messageString, actor, mbom, mbomRevision, as);
                    break;
                case "plm.mes.mbom.update.attributes":
                    convertedString = getMbomAttributeUpdatedString(messageString, actor, mbom, mbomRevision, as);
                    break;
                case "plm.mes.mbom.files.add":
                    convertedString = getMbomFilesAddedString(messageString, mbom, mbomRevision, as);
                    break;
                case "plm.mes.mbom.files.delete":
                    convertedString = getMbomFileDeletedString(messageString, mbom, mbomRevision, as);
                    break;
                case "plm.mes.mbom.files.folders.add":
                    convertedString = getMbomFoldersAddedString(messageString, mbom, mbomRevision, as);
                    break;
                case "plm.mes.mbom.files.folders.delete":
                    convertedString = getMbomFoldersDeletedString(messageString, mbom, mbomRevision, as);
                    break;
                case "plm.mes.mbom.files.version":
                    convertedString = getMbomFilesVersionedString(messageString, mbom, mbomRevision, as);
                    break;
                case "plm.mes.mbom.files.rename":
                    convertedString = getMbomFileRenamedString(messageString, mbom, mbomRevision, as);
                    break;
                case "plm.mes.mbom.files.replace":
                    convertedString = getMbomFileRenamedString(messageString, mbom, mbomRevision, as);
                    break;
                case "plm.mes.mbom.files.download":
                    convertedString = getMbomFileString(messageString, mbom, mbomRevision, as);
                    break;
                case "plm.mes.mbom.comment":
                    convertedString = getMbomCommentString(messageString, mbom, mbomRevision, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getMbomCreatedString(String messageString, Person actor, MESMBOM mbom) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), highlightValue(mbom.getNumber() + " - " + mbom.getName()));
    }

    private String getMbomItemsCreatedString(String messageString, MESMBOM mesMbom, MESMBOMRevision mesMbomRevision, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(mesMbom.getNumber() + " - " + mesMbom.getName()),
                highlightValue(mesMbomRevision.getRevision()));
        StringBuffer sb = new StringBuffer();
        sb.append(message);
        try {
            List<ASMbomItem> items = objectMapper.readValue(as.getData(), new TypeReference<List<ASMbomItem>>() {
            });
            for (ASMbomItem item : items) {
                if (item.getType().equals(MESBomItemType.NORMAL.name())) {
                    String itemString = activityStreamResourceBundle.getString("plm.mes.mbom.bom.create.item");
                    String s = addMarginToMessage(MessageFormat.format(itemString, highlightValue(item.getNumber() + " - " + item.getName()),
                            item.getQuantity()));
                    sb.append(s);
                } else {
                    String itemString = activityStreamResourceBundle.getString("plm.mes.mbom.bom.create.phantom");
                    String s = addMarginToMessage(MessageFormat.format(itemString, highlightValue(item.getNumber() + " - " + item.getName())));
                    sb.append(s);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getMbomItemDeletedString(String messageString, MESMBOM mesMbom, MESMBOMRevision mesMbomRevision, ActivityStream as) {
        String message = "";
        try {
            ASMbomItem item = objectMapper.readValue(as.getData(), new TypeReference<ASMbomItem>() {
            });
            message = MessageFormat.format(messageString, as.getActor().getFullName().trim(), highlightValue(item.getNumber()),
                    highlightValue(mesMbom.getNumber() + " - " + mesMbom.getName()), highlightValue(mesMbomRevision.getRevision()));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        return message;
    }

    private String getMbomItemUpdatedString(String messageString, Person actor, MESMBOM mbom, MESMBOMRevision mbomRevision, ActivityStream as) {
        String updateString = activityStreamResourceBundle.getString("plm.mes.mbom.bom.update.property");
        StringBuffer sb = new StringBuffer();
        String json = as.getData();
        try {
            List<ASMbomItemUpdate> propChanges = objectMapper.readValue(json, new TypeReference<List<ASMbomItemUpdate>>() {
            });
            String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), highlightValue(propChanges.get(0).getNumber()),
                    highlightValue(mbom.getNumber() + " - " + mbom.getName()), highlightValue(mbomRevision.getRevision()));

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

    private String getMbomBasicInfoUpdatedString(String messageString, Person actor, MESMBOM mbom, MESMBOMRevision mbomRevision, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), highlightValue(mbom.getNumber() + " - " + mbom.getName()), highlightValue(mbomRevision.getRevision()));

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mes.mbom.update.basicinfo.property");

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

    private String getMbomBasicInfoUpdatedJson(MESMBOM oldMbom, MESMBOM newMbom) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldMbom.getName();
        String newValue = newMbom.getName();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        oldValue = oldMbom.getDescription();
        newValue = newMbom.getDescription();
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

    private String getMbomItemUpdatedJson(MESBOMItem oldMbomItem, MESBOMItem newMbomItem) {
        List<ASMbomItemUpdate> changes = new ArrayList<>();

        String oldValue = oldMbomItem.getQuantity().toString();
        String newValue = newMbomItem.getQuantity().toString();
        if (!newValue.equals(oldValue)) {
            PLMBom itemMbom = bomRepository.findOne(newMbomItem.getBomItem());
            changes.add(new ASMbomItemUpdate(itemMbom.getItem().getItemNumber(), "Quantity", oldValue, newValue));
        }

        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
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


    private String getMbomAttributesUpdatedJson(MESObjectAttribute oldAttribute, MESObjectAttribute newAttribute) {
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

    private String getMbomAttributeUpdatedString(String messageString, Person actor, MESMBOM mbom, MESMBOMRevision mbomRevision, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), mbom.getNumber(),
                mbomRevision.getRevision(), mbomRevision.getLifeCyclePhase().getPhase());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mes.mbom.update.attributes.attribute");

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

    private String getMbomFilesAddedJson(List<MESMBOMFile> files) {
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

    private String getMbomFoldersAddedJson(MESMBOMFile file) {
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

    private String getMbomFoldersDeletedJson(MESMBOMFile file) {
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

    private String getMbomFilesAddedString(String messageString, MESMBOM mbom, MESMBOMRevision mbomRevision, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), mbom.getNumber(),
                mbomRevision.getRevision(), mbomRevision.getLifeCyclePhase().getPhase());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mes.mbom.files.add.file");

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

    private String getMbomFoldersAddedString(String messageString, MESMBOM mbom, MESMBOMRevision mbomRevision, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), mbom.getNumber(),
                    mbomRevision.getRevision(), mbomRevision.getLifeCyclePhase().getPhase());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getMbomFoldersDeletedString(String messageString, MESMBOM mbom, MESMBOMRevision mbomRevision, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), mbom.getNumber(),
                    mbomRevision.getRevision(), mbomRevision.getLifeCyclePhase().getPhase());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getMbomFileDeletedString(String messageString, MESMBOM mbom, MESMBOMRevision mbomRevision, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), mbom.getNumber(),
                    mbomRevision.getRevision(), mbomRevision.getLifeCyclePhase().getPhase());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getMbomFilesVersionedJson(List<MESMBOMFile> files) {
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

    private String getMbomFilesVersionedString(String messageString, MESMBOM mbom, MESMBOMRevision mbomRevision, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), mbom.getNumber(),
                mbomRevision.getRevision(), mbomRevision.getLifeCyclePhase().getPhase());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mes.mbom.files.version.file");

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

    private String getMbomFileRenamedString(String messageString, MESMBOM mbom, MESMBOMRevision mbomRevision, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    mbom.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getMbomFileString(String messageString, MESMBOM mbom, MESMBOMRevision mbomRevision, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    mbom.getNumber(), mbomRevision.getRevision(), mbomRevision.getLifeCyclePhase().getPhase());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getMbomCommentString(String messageString, MESMBOM mbom, MESMBOMRevision mbomRevision, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                mbom.getNumber(), mbomRevision.getRevision(), mbomRevision.getLifeCyclePhase().getPhase());

        String s = highlightValue("Comment: ");
        s += as.getData();
        s = addMarginToMessage(s);

        message += s;

        return message;
    }

}
