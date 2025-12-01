package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.BOPOperationEvents;
import com.cassinisys.plm.model.mes.*;
import com.cassinisys.plm.model.plm.PLMBom;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.repo.mes.BOPRouteOperationRepository;
import com.cassinisys.plm.repo.mes.MESBOMItemRepository;
import com.cassinisys.plm.repo.mes.MESObjectRepository;
import com.cassinisys.plm.repo.mes.MESObjectTypeRepository;
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
public class BOPOperationActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private BOPRouteOperationRepository bopRouteOperationRepository;
    @Autowired
    private MESBOMItemRepository mesbomItemRepository;
    @Autowired
    private BomRepository bomRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MESObjectRepository mesObjectRepository;
    @Autowired
    private MESObjectTypeRepository mesObjectTypeRepository;

    @Async
    @EventListener
    public void itemBasicInfoUpdated(BOPOperationEvents.BOPOperationBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        MESBOP oldItem = event.getPlmOldBOP();
        MESBOP newItem = event.getBop();
        Integer routeItem = event.getOperation();

        object.setObject(routeItem);
        object.setType("bop");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.bop.route.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getBOPBasicInfoUpdatedJson(oldItem, newItem));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void itemFilesAdded(BOPOperationEvents.BOPOperationFilesAddedEvent event) {
        List<MESBOPOperationFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.bop.route.files.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getOperation());
        object.setType("bop");
        as.setObject(object);
        as.setData(getBOPFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemFileDeleted(BOPOperationEvents.BOPOperationFileDeletedEvent event) {
        MESBOPOperationFile file = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.bop.route.files.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getOperation());
        object.setType("bop");
        as.setObject(object);
        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(file.getId(), file.getName(), FileUtils.byteCountToDisplaySize(file.getSize()));
        String json = "";
        try {
            json = objectMapper.writeValueAsString(asNewFileDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        as.setData(json);
        activityStreamService.create(as);
    }


    @Async
    @EventListener
    public void itemFilesVersioned(BOPOperationEvents.BOPOperationFilesVersionedEvent event) {
        List<MESBOPOperationFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.bop.route.files.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getOperation());
        object.setType("bop");
        as.setObject(object);
        as.setData(getBOPFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemFileRenamed(BOPOperationEvents.BOPOperationFileRenamedEvent event) {
        MESBOPOperationFile oldFile = event.getOldFile();
        MESBOPOperationFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.bop.route.files.rename");
        if (event.getType().equals("Rename")) as.setActivity("plm.mes.bop.route.files.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.mes.bop.route.files.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getOperation());
        object.setType("bop");
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
    public void itemFileLocked(BOPOperationEvents.BOPOperationFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.bop.route.files.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getOperation());
        object.setType("bop");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getBopFile().getId(), event.getBopFile().getName(), FileUtils.byteCountToDisplaySize(event.getBopFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemFileUnlocked(BOPOperationEvents.BOPOperationFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.bop.route.files.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getOperation());
        object.setType("bop");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getBopFile().getId(), event.getBopFile().getName(), FileUtils.byteCountToDisplaySize(event.getBopFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemFileDownloaded(BOPOperationEvents.BOPOperationFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.bop.route.files.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getOperation());
        object.setType("bop");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getBopFile().getId(), event.getBopFile().getName(), FileUtils.byteCountToDisplaySize(event.getBopFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void operationPartAdded(BOPOperationEvents.BOPOperationPartsAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.bop.route.parts.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getOperation());
        object.setType("bop");
        as.setObject(object);
        List<ASBomItem> asBomItems = new ArrayList<>();
        event.getOperationParts().forEach(mesbopOperationPart -> {
            MESBOMItem mesbomItem = mesbomItemRepository.findOne(mesbopOperationPart.getMbomItem());
            PLMBom bom = bomRepository.findOne(mesbomItem.getBomItem());
            asBomItems.add(new ASBomItem(bom.getItem().getItemNumber(),
                    mesbopOperationPart.getQuantity(), "", mesbopOperationPart.getNotes()));
        });
        try {
            as.setData(objectMapper.writeValueAsString(asBomItems));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void operationPartDeleted(BOPOperationEvents.BOPOperationPartDeletedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.bop.route.parts.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getOperation());
        object.setType("bop");
        as.setObject(object);
        MESBOMItem mesbomItem = mesbomItemRepository.findOne(event.getOperationPart().getMbomItem());
        PLMBom bom = bomRepository.findOne(mesbomItem.getBomItem());
        ASBomItem asBomItem = new ASBomItem(bom.getItem().getItemNumber(),
                event.getOperationPart().getQuantity(), "", event.getOperationPart().getNotes());
        try {
            as.setData(objectMapper.writeValueAsString(asBomItem));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemBomItemsUpdated(BOPOperationEvents.BOPOperationPartUpdatedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.bop.route.parts.update.basicinfo");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getOperation());
        object.setType("bop");
        as.setObject(object);
        as.setData(getBomItemUpdatedJson(event.getOldOperationPart(), event.getOperationPart()));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void itemBomItemsUpdated(BOPOperationEvents.BOPOperationResourceUpdatedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.bop.route.resources.update.basicinfo");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getOperation());
        object.setType("bop");
        as.setObject(object);
        as.setData(getBopOperationResourceUpdatedJson(event.getOldOperationResource(), event.getOperationResource()));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void operationResourceAdded(BOPOperationEvents.BOPOperationResourcesAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.bop.route.resources.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getOperation());
        object.setType("bop");
        as.setObject(object);
        List<ASBOPOperationResource> asBomItems = new ArrayList<>();
        event.getOperationResources().forEach(operationResource -> {
            MESObject mesObject = mesObjectRepository.findOne(operationResource.getResource());
            MESObjectType mesObjectType = mesObjectTypeRepository.findOne(operationResource.getResourceType());
            asBomItems.add(new ASBOPOperationResource(operationResource.getType(), mesObjectType.getName(), mesObject.getName()));
        });
        try {
            as.setData(objectMapper.writeValueAsString(asBomItems));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void operationResourceDeleted(BOPOperationEvents.BOPOperationResourceDeletedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.bop.route.resources.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getOperation());
        object.setType("bop");
        as.setObject(object);
        MESObject mesObject = mesObjectRepository.findOne(event.getOperationResource().getResource());
        MESObjectType mesObjectType = mesObjectTypeRepository.findOne(event.getOperationResource().getResourceType());
        ASBOPOperationResource operationResource = new ASBOPOperationResource(event.getOperationResource().getType(), mesObjectType.getName(), mesObject.getName());
        try {
            as.setData(objectMapper.writeValueAsString(operationResource));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void planFoldersAdded(BOPOperationEvents.BOPOperationFoldersAddedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.bop.route.files.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getOperation());
        object.setType("bop");
        as.setObject(object);
        as.setData(getBOPFoldersAddOrDeleteJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void planFoldersDeleted(BOPOperationEvents.BOPOperationFoldersDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.bop.route.files.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getOperation());
        object.setType("bop");
        as.setObject(object);
        as.setData(getBOPFoldersAddOrDeleteJson(files));

        activityStreamService.create(as);
    }

    @Override
    public String getConverterKey() {
        return "plm.mes.bop.route";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            MESBOPRouteOperation routeItem = bopRouteOperationRepository.findOne(object.getObject());

            if (routeItem == null) return "";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.mes.bop.update.basicinfo":
                    convertedString = getBOPBasicInfoUpdatedString(messageString, actor, routeItem, as);
                    break;
                case "plm.mes.bop.route.files.add":
                    convertedString = getBOPFilesAddedString(messageString, routeItem, as);
                    break;
                case "plm.mes.bop.route.files.delete":
                    convertedString = getBOPFilesDeletedString(messageString, routeItem, as);
                    break;
                case "plm.mes.bop.route.files.version":
                    convertedString = getBOPFilesVersionedString(messageString, routeItem, as);
                    break;
                case "plm.mes.bop.route.files.rename":
                    convertedString = getBOPFileRenamedString(messageString, routeItem, as);
                    break;
                case "plm.mes.bop.route.files.replace":
                    convertedString = getBOPFileRenamedString(messageString, routeItem, as);
                    break;
                case "plm.mes.bop.route.files.lock":
                    convertedString = getBOPFileString(messageString, routeItem, as);
                    break;
                case "plm.mes.bop.route.files.unlock":
                    convertedString = getBOPFileString(messageString, routeItem, as);
                    break;
                case "plm.mes.bop.route.files.folders.add":
                    convertedString = getBOPFoldersAddedOrDeletedString(messageString, routeItem, as);
                    break;
                case "plm.mes.bop.route.files.folders.delete":
                    convertedString = getBOPFoldersAddedOrDeletedString(messageString, routeItem, as);
                    break;
                case "plm.mes.bop.route.files.download":
                    convertedString = getBOPFileString(messageString, routeItem, as);
                    break;
                case "plm.mes.bop.route.resources.delete":
                    convertedString = getBOPOperationResourceDeletedString(messageString, routeItem, as);
                    break;
                case "plm.mes.bop.route.resources.add":
                    convertedString = getBOPOperationResourceAddedString(messageString, routeItem, as);
                    break;
                case "plm.mes.bop.route.parts.delete":
                    convertedString = getBOPOperationPartDeletedString(messageString, routeItem, as);
                    break;
                case "plm.mes.bop.route.parts.update.basicinfo":
                    convertedString = getBOPOperationPartUpdatedString(messageString, actor, routeItem, as);
                    break;
                case "plm.mes.bop.route.resources.update.basicinfo":
                    convertedString = getBOPOperationResourceUpdatedString(messageString, actor, routeItem, as);
                    break;
                case "plm.mes.bop.route.parts.add":
                    convertedString = getBOPOperationPartAddedString(messageString, routeItem, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getBomItemUpdatedJson(MESBOPOperationPart oldPart, MESBOPOperationPart part) {
        List<ASBomItemUpdate> changes = new ArrayList<>();
        MESBOPRouteOperation routeItem = bopRouteOperationRepository.findOne(part.getBopOperation());
        String oldValue = oldPart.getQuantity().toString();
        String newValue = part.getQuantity().toString();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASBomItemUpdate(routeItem.getSequenceNumber(), "Quantity", oldValue, newValue));
        }

        oldValue = oldPart.getNotes();
        newValue = part.getNotes();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!oldValue.equals(newValue)) {
            changes.add(new ASBomItemUpdate(routeItem.getSequenceNumber(), "Notes", oldValue, newValue));
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

    private String getBopOperationResourceUpdatedJson(MESBOPOperationResource oldPart, MESBOPOperationResource part) {
        List<ASBomItemUpdate> changes = new ArrayList<>();
        MESBOPRouteOperation routeItem = bopRouteOperationRepository.findOne(part.getBopOperation());

        String oldValue = oldPart.getNotes();
        String newValue = part.getNotes();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!oldValue.equals(newValue)) {
            changes.add(new ASBomItemUpdate(routeItem.getSequenceNumber(), "Notes", oldValue, newValue));
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

    private String getBOPBasicInfoUpdatedJson(MESBOP plmOldItem, MESBOP bop) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = plmOldItem.getName();
        String newValue = bop.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        oldValue = plmOldItem.getDescription();
        newValue = bop.getDescription();
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
        if (changes.size() > 0) {
            try {
                json = objectMapper.writeValueAsString(changes);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        return json;
    }

    private String getBOPBasicInfoUpdatedString(String messageString, Person actor, MESBOPRouteOperation routeItem, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), highlightValue(routeItem.getSequenceNumber()));

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mes.bop.update.basicinfo.property");

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

    private String getBOPFilesAddedJson(List<MESBOPOperationFile> files) {
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

    private String getBOPFilesAddedString(String messageString, MESBOPRouteOperation routeItem, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(routeItem.getSequenceNumber()));

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mes.bop.route.files.add.file");

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

    private String getBOPFilesDeletedString(String messageString, MESBOPRouteOperation routeItem, ActivityStream as) {
        String activityString = "";

        String json = as.getData();
        try {
            ASNewFileDTO asNewFileDTO = objectMapper.readValue(json, new TypeReference<ASNewFileDTO>() {
            });
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(asNewFileDTO.getName()),
                    highlightValue(routeItem.getSequenceNumber()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return activityString;
    }


    private String getBOPFilesVersionedJson(List<MESBOPOperationFile> files) {
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

    private String getBOPFilesVersionedString(String messageString, MESBOPRouteOperation routeItem, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), highlightValue(routeItem.getSequenceNumber()));

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mes.bop.route.files.version.file");

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

    private String getBOPFileRenamedString(String messageString, MESBOPRouteOperation routeItem, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    highlightValue(routeItem.getSequenceNumber()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getBOPFileString(String messageString, MESBOPRouteOperation routeItem, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    highlightValue(routeItem.getSequenceNumber()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getBOPOperationPartAddedString(String messageString, MESBOPRouteOperation routeItem, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(routeItem.getSequenceNumber()));
        StringBuffer sb = new StringBuffer();
        sb.append(message);

        String itemString = activityStreamResourceBundle.getString("plm.mes.bop.route.parts.add.part");

        try {
            List<ASBomItem> items = objectMapper.readValue(as.getData(), new TypeReference<List<ASBomItem>>() {
            });
            for (ASBomItem part : items) {
                String s = addMarginToMessage(MessageFormat.format(itemString, highlightValue(part.getNumber()),
                        part.getQuantity()));
                sb.append(s);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getBOPOperationResourceAddedString(String messageString, MESBOPRouteOperation routeItem, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(routeItem.getSequenceNumber()));
        StringBuffer sb = new StringBuffer();
        sb.append(message);

        String itemString = activityStreamResourceBundle.getString("plm.mes.bop.route.resources.add.resource");

        try {
            List<ASBOPOperationResource> operationResources = objectMapper.readValue(as.getData(), new TypeReference<List<ASBOPOperationResource>>() {
            });
            for (ASBOPOperationResource operationResource : operationResources) {
                String s = addMarginToMessage(MessageFormat.format(itemString, highlightValue(operationResource.getResourceType() + " - " + operationResource.getNumber())));
                sb.append(s);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getBOPOperationPartUpdatedString(String messageString, Person actor, MESBOPRouteOperation routeItem, ActivityStream as) {
        String updateString = activityStreamResourceBundle.getString("plm.mes.bop.route.parts.update.basicinfo.property");
        StringBuffer sb = new StringBuffer();
        String json = as.getData();
        try {
            List<ASBomItemUpdate> propChanges = objectMapper.readValue(json, new TypeReference<List<ASBomItemUpdate>>() {
            });
            String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), highlightValue(propChanges.get(0).getNumber()), highlightValue(routeItem.getSequenceNumber()));

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

    private String getBOPOperationResourceUpdatedString(String messageString, Person actor, MESBOPRouteOperation routeItem, ActivityStream as) {
        String updateString = activityStreamResourceBundle.getString("plm.mes.bop.route.resources.update.basicinfo.property");
        StringBuffer sb = new StringBuffer();
        String json = as.getData();
        try {
            List<ASBomItemUpdate> propChanges = objectMapper.readValue(json, new TypeReference<List<ASBomItemUpdate>>() {
            });
            String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), highlightValue(propChanges.get(0).getNumber()), highlightValue(routeItem.getSequenceNumber()));

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

    private String getBOPOperationPartDeletedString(String messageString, MESBOPRouteOperation routeItem, ActivityStream as) {
        String message = "";
        try {
            ASBomItem part = objectMapper.readValue(as.getData(), new TypeReference<ASBomItem>() {
            });
            message = MessageFormat.format(messageString, as.getActor().getFullName().trim(), highlightValue(part.getNumber()),
                    highlightValue(routeItem.getSequenceNumber()));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        return message;
    }

    private String getBOPOperationResourceDeletedString(String messageString, MESBOPRouteOperation routeItem, ActivityStream as) {
        String message = "";
        try {
            ASBOPOperationResource operationResource = objectMapper.readValue(as.getData(), new TypeReference<ASBOPOperationResource>() {
            });
            message = MessageFormat.format(messageString, as.getActor().getFullName().trim(), highlightValue(operationResource.getResourceType() + " - " + operationResource.getNumber()),
                    highlightValue(routeItem.getSequenceNumber()));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        return message;
    }

    private String getBOPFoldersAddOrDeleteJson(PLMFile file) {
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


    private String getBOPFoldersAddedOrDeletedString(String messageString, MESBOPRouteOperation routeItem, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), highlightValue(routeItem.getSequenceNumber()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }
}
