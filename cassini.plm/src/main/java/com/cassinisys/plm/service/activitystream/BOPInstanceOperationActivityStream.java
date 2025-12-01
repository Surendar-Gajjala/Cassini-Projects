package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.BOPInstanceOperationEvents;
import com.cassinisys.plm.model.mes.*;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.repo.mes.BOPInstanceRouteOperationRepository;
import com.cassinisys.plm.repo.mes.MESObjectRepository;
import com.cassinisys.plm.repo.mes.MESObjectTypeRepository;
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
public class BOPInstanceOperationActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private BOPInstanceRouteOperationRepository bopInstanceRouteOperationRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Async
    @EventListener
    public void itemFilesAdded(BOPInstanceOperationEvents.BOPOperationFilesAddedEvent event) {
        List<MESBOPInstanceOperationFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.mbominstance.operation.files.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getOperation());
        object.setType("mbominstance");
        as.setObject(object);
        as.setData(getBOPFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemFileDeleted(BOPInstanceOperationEvents.BOPOperationFileDeletedEvent event) {
        MESBOPInstanceOperationFile file = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.mbominstance.operation.files.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getOperation());
        object.setType("mbominstance");
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
    public void itemFilesVersioned(BOPInstanceOperationEvents.BOPOperationFilesVersionedEvent event) {
        List<MESBOPInstanceOperationFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.mbominstance.operation.files.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getOperation());
        object.setType("mbominstance");
        as.setObject(object);
        as.setData(getBOPFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemFileRenamed(BOPInstanceOperationEvents.BOPOperationFileRenamedEvent event) {
        MESBOPInstanceOperationFile oldFile = event.getOldFile();
        MESBOPInstanceOperationFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.mbominstance.operation.files.rename");
        if (event.getType().equals("Rename")) as.setActivity("plm.mes.mbominstance.operation.files.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.mes.mbominstance.operation.files.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getOperation());
        object.setType("mbominstance");
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
    public void itemFileLocked(BOPInstanceOperationEvents.BOPOperationFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.mbominstance.operation.files.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getOperation());
        object.setType("mbominstance");
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
    public void itemFileUnlocked(BOPInstanceOperationEvents.BOPOperationFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.mbominstance.operation.files.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getOperation());
        object.setType("mbominstance");
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
    public void itemFileDownloaded(BOPInstanceOperationEvents.BOPOperationFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.mbominstance.operation.files.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getOperation());
        object.setType("mbominstance");
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
    public void planFoldersAdded(BOPInstanceOperationEvents.BOPOperationFoldersAddedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.mbominstance.operation.files.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getOperation());
        object.setType("mbominstance");
        as.setObject(object);
        as.setData(getBOPFoldersAddOrDeleteJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void planFoldersDeleted(BOPInstanceOperationEvents.BOPOperationFoldersDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.mbominstance.operation.files.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getOperation());
        object.setType("mbominstance");
        as.setObject(object);
        as.setData(getBOPFoldersAddOrDeleteJson(files));

        activityStreamService.create(as);
    }

    @Override
    public String getConverterKey() {
        return "plm.mes.mbominstance.operation";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            MESBOPInstanceRouteOperation routeItem = bopInstanceRouteOperationRepository.findOne(object.getObject());

            if (routeItem == null) return "";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.mes.mbominstance.operation.files.add":
                    convertedString = getBOPFilesAddedString(messageString, routeItem, as);
                    break;
                case "plm.mes.mbominstance.operation.files.delete":
                    convertedString = getBOPFilesDeletedString(messageString, routeItem, as);
                    break;
                case "plm.mes.mbominstance.operation.files.version":
                    convertedString = getBOPFilesVersionedString(messageString, routeItem, as);
                    break;
                case "plm.mes.mbominstance.operation.files.rename":
                    convertedString = getBOPFileRenamedString(messageString, routeItem, as);
                    break;
                case "plm.mes.mbominstance.operation.files.replace":
                    convertedString = getBOPFileRenamedString(messageString, routeItem, as);
                    break;
                case "plm.mes.mbominstance.operation.files.lock":
                    convertedString = getBOPFileString(messageString, routeItem, as);
                    break;
                case "plm.mes.mbominstance.operation.files.unlock":
                    convertedString = getBOPFileString(messageString, routeItem, as);
                    break;
                case "plm.mes.mbominstance.operation.files.folders.add":
                    convertedString = getBOPFoldersAddedOrDeletedString(messageString, routeItem, as);
                    break;
                case "plm.mes.mbominstance.operation.files.folders.delete":
                    convertedString = getBOPFoldersAddedOrDeletedString(messageString, routeItem, as);
                    break;
                case "plm.mes.mbominstance.operation.files.download":
                    convertedString = getBOPFileString(messageString, routeItem, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getBOPFilesAddedJson(List<MESBOPInstanceOperationFile> files) {
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

    private String getBOPFilesAddedString(String messageString, MESBOPInstanceRouteOperation routeItem, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(routeItem.getSequenceNumber()));

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mes.mbominstance.operation.files.add.file");

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

    private String getBOPFilesDeletedString(String messageString, MESBOPInstanceRouteOperation routeItem, ActivityStream as) {
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


    private String getBOPFilesVersionedJson(List<MESBOPInstanceOperationFile> files) {
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

    private String getBOPFilesVersionedString(String messageString, MESBOPInstanceRouteOperation routeItem, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), highlightValue(routeItem.getSequenceNumber()));

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mes.mbominstance.operation.files.version.file");

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

    private String getBOPFileRenamedString(String messageString, MESBOPInstanceRouteOperation routeItem, ActivityStream as) {
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

    private String getBOPFileString(String messageString, MESBOPInstanceRouteOperation routeItem, ActivityStream as) {
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


    private String getBOPFoldersAddedOrDeletedString(String messageString, MESBOPInstanceRouteOperation routeItem, ActivityStream as) {

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
