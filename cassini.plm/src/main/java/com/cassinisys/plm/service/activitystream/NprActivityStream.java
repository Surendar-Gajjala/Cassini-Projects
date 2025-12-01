package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.NprEvents;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMNpr;
import com.cassinisys.plm.model.plm.PLMNprFile;
import com.cassinisys.plm.model.plm.PLMNprItem;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.repo.plm.NprItemRepository;
import com.cassinisys.plm.repo.plm.NprRepository;
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
public class NprActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private NprRepository nprRepository;
    @Autowired
    private PLMWorkflowRepository workFlowRepository;
    @Autowired
    private PLMWorkflowStatusRepository workflowStatusRepository;
    @Autowired
    private CommonActivityStream commonActivityStream;
    @Autowired
    private NprItemRepository nprItemRepository;

    @Async
    @EventListener
    public void nprCreated(NprEvents.NprCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNpr().getId());
        object.setType("npr");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.npr.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void nprBasicInfoUpdated(NprEvents.NprBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PLMNpr oldnpr = event.getOldNpr();
        PLMNpr newnpr = event.getNpr();

        object.setObject(newnpr.getId());
        object.setType("npr");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.npr.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getNprBasicInfoUpdatedJson(oldnpr, newnpr));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void nprFilesAdded(NprEvents.NprFilesAddedEvent event) {
        List<PLMNprFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.npr.files.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNpr().getId());
        object.setType("npr");
        as.setObject(object);
        as.setData(getNprFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void nprFoldersAdded(NprEvents.NprFoldersAddedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.npr.files.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNpr().getId());
        object.setType("npr");
        as.setObject(object);
        as.setData(getNprFoldersAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void nprFoldersDeleted(NprEvents.NprFoldersDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.npr.files.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNpr().getId());
        object.setType("npr");
        as.setObject(object);
        as.setData(getNprFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void nprFileDeleted(NprEvents.NprFileDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.npr.files.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNpr().getId());
        object.setType("npr");
        as.setObject(object);
        as.setData(getNprFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void nprFilesVersioned(NprEvents.NprFilesVersionedEvent event) {
        List<PLMNprFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.npr.files.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNpr().getId());
        object.setType("npr");
        as.setObject(object);
        as.setData(getNprFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void nprFileRenamed(NprEvents.NprFileRenamedEvent event) {
        PLMFile oldFile = event.getOldFile();
        PLMFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals("Rename")) as.setActivity("plm.npr.files.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.npr.files.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNpr().getId());
        object.setType("npr");
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
    public void nprFileLocked(NprEvents.NprFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.npr.files.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNpr().getId());
        object.setType("npr");
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
    public void nprFileUnlocked(NprEvents.NprFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.npr.files.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNpr().getId());
        object.setType("npr");
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
    public void nprFileDownloaded(NprEvents.NprFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.npr.files.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNpr().getId());
        object.setType("npr");
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
    public void nprCommentAdded(NprEvents.NprCommentAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.npr.comment");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNpr().getId());
        object.setType("npr");
        as.setObject(object);
        as.setData(event.getComment().getComment());

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void nprWorkflowStarted(NprEvents.NprWorkflowStartedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.npr.workflow.start");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNpr().getId());
        object.setType("npr");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void nprWorkflowFinished(NprEvents.NprWorkflowFinishedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.npr.workflow.finish");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNpr().getId());
        object.setType("npr");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void nprWorkflowPromoted(NprEvents.NprWorkflowPromotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.npr.workflow.promote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNpr().getId());
        object.setType("npr");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        ActivityStreamObject source = new ActivityStreamObject();
        source.setObject(event.getFromActivity().getId());
        source.setType("workflowactivity");
        as.setSource(source);

        ActivityStreamObject target = new ActivityStreamObject();
        target.setObject(event.getToActivity().getId());
        target.setType("workflowactivity");
        as.setTarget(target);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void nprWorkflowDemoted(NprEvents.NprWorkflowDemotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.npr.workflow.demote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNpr().getId());
        object.setType("npr");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        ActivityStreamObject source = new ActivityStreamObject();
        source.setObject(event.getFromActivity().getId());
        source.setType("workflowactivity");
        as.setSource(source);

        ActivityStreamObject target = new ActivityStreamObject();
        target.setObject(event.getToActivity().getId());
        target.setType("workflowactivity");
        as.setTarget(target);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void nprWorkflowHold(NprEvents.NprWorkflowHoldEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.npr.workflow.hold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNpr().getId());
        object.setType("npr");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        ActivityStreamObject source = new ActivityStreamObject();
        source.setObject(event.getWorkflowActivity().getId());
        source.setType("workflowstatus");
        as.setSource(source);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void nprWorkflowUnhold(NprEvents.NprWorkflowUnholdEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.npr.workflow.unhold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNpr().getId());
        object.setType("npr");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        ActivityStreamObject source = new ActivityStreamObject();
        source.setObject(event.getWorkflowActivity().getId());
        source.setType("workflowstatus");
        as.setSource(source);

        activityStreamService.create(as);
    }


    @Override
    public String getConverterKey() {
        return "plm.npr";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            PLMNpr npr = nprRepository.findOne(object.getObject());

            if (npr == null) return "";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.npr.create":
                    convertedString = getNprCreatedString(messageString, actor, npr);
                    break;
                case "plm.npr.update.basicinfo":
                    convertedString = getNprBasicInfoUpdatedString(messageString, actor, npr, as);
                    break;
                case "plm.npr.files.add":
                    convertedString = getNprFilesAddedString(messageString, npr, as);
                    break;
                case "plm.npr.files.delete":
                    convertedString = getNprFileDeletedString(messageString, npr, as);
                    break;
                case "plm.npr.files.folders.add":
                    convertedString = getNprFoldersAddedString(messageString, npr, as);
                    break;
                case "plm.npr.files.folders.delete":
                    convertedString = getNprFoldersDeletedString(messageString, npr, as);
                    break;
                case "plm.npr.files.version":
                    convertedString = getNprFilesVersionedString(messageString, npr, as);
                    break;
                case "plm.npr.files.rename":
                    convertedString = getNprFileRenamedString(messageString, npr, as);
                    break;
                case "plm.npr.files.replace":
                    convertedString = getNprFileRenamedString(messageString, npr, as);
                    break;
                case "plm.npr.files.lock":
                    convertedString = getNprFileString(messageString, npr, as);
                    break;
                case "plm.npr.files.unlock":
                    convertedString = getNprFileString(messageString, npr, as);
                    break;
                case "plm.npr.files.download":
                    convertedString = getNprFileString(messageString, npr, as);
                    break;
                case "plm.npr.comment":
                    convertedString = getNprCommentString(messageString, npr, as);
                    break;
                case "plm.npr.workflow.start":
                    convertedString = getNprWorkflowStartString(messageString, npr, as);
                    break;
                case "plm.npr.workflow.finish":
                    convertedString = getNprWorkflowFinishString(messageString, npr, as);
                    break;
                case "plm.npr.workflow.promote":
                    convertedString = getNprWorkflowPromoteDemoteString(messageString, npr, as);
                    break;
                case "plm.npr.workflow.demote":
                    convertedString = getNprWorkflowPromoteDemoteString(messageString, npr, as);
                    break;
                case "plm.npr.workflow.hold":
                    convertedString = getNprWorkflowHoldUnholdString(messageString, npr, as);
                    break;
                case "plm.npr.workflow.unhold":
                    convertedString = getNprWorkflowHoldUnholdString(messageString, npr, as);
                    break;
                case "plm.npr.workflow.add":
                    convertedString = getNprWorkflowAddedString(messageString, npr, as);
                    break;
                case "plm.npr.workflow.change":
                    convertedString = getNprWorkflowChangeString(messageString, npr, as);
                    break;
                case "plm.npr.requestedItem.add":
                    convertedString = getNprRequestedItemAddedString(messageString, npr, as);
                    break;
                case "plm.npr.requestedItem.delete":
                    convertedString = getNPRRelatedItemDeletedString(messageString, npr, as);
                    break;
                case "plm.npr.requestedItem.update":
                    convertedString = getNPRRequestedItemUpdatedString(messageString,npr, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getNprCreatedString(String messageString, Person actor, PLMNpr npr) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), npr.getNumber());
    }

    private String getNprBasicInfoUpdatedString(String messageString, Person actor, PLMNpr npr, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), npr.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.npr.update.basicinfo.property");

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

    private String getNprFilesAddedJson(List<PLMNprFile> files) {
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

    private String getNprFoldersAddedJson(PLMFile file) {
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

    private String getNprFoldersDeletedJson(PLMFile file) {
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

    private String getNprFilesAddedString(String messageString, PLMNpr npr, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), npr.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.npr.files.add.file");

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

    private String getNprFoldersAddedString(String messageString, PLMNpr npr, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), npr.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getNprFoldersDeletedString(String messageString, PLMNpr npr, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), npr.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getNprFileDeletedString(String messageString, PLMNpr npr, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), npr.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getNprFilesVersionedJson(List<PLMNprFile> files) {
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

    private String getNprFilesVersionedString(String messageString, PLMNpr npr, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), npr.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.npr.files.version.file");

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

    private String getNprFileRenamedString(String messageString, PLMNpr npr, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    npr.getDescription());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getNprFileString(String messageString, PLMNpr npr, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    npr.getDescription());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getNprCommentString(String messageString, PLMNpr npr, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                npr.getDescription());

        String s = highlightValue("Comment: ");
        s += as.getData();
        s = addMarginToMessage(s);

        message += s;

        return message;
    }

    private String getNprBasicInfoUpdatedJson(PLMNpr oldnpr, PLMNpr npr) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = null;
        String newValue = null;
        oldValue = oldnpr.getDescription();
        newValue = npr.getDescription();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue, newValue));
        }

        oldValue = oldnpr.getNotes();
        newValue = npr.getNotes();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Notes", oldValue, newValue));
        }

        oldValue = oldnpr.getReasonForRequest();
        newValue = npr.getReasonForRequest();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("ReasonForRequest", oldValue, newValue));
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

    private String getNprWorkflowStartString(String messageString, PLMNpr npr, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                npr.getNumber());
    }

    private String getNprWorkflowFinishString(String messageString, PLMNpr npr, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()), npr.getNumber());
    }

    private String getNprWorkflowPromoteDemoteString(String messageString, PLMNpr npr, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();
        ActivityStreamObject target = as.getTarget();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus fromActivity = workflowStatusRepository.findOne(source.getObject());
        PLMWorkflowStatus toActivity = workflowStatusRepository.findOne(target.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                npr.getNumber(),
                highlightValue(fromActivity.getName()),
                highlightValue(toActivity.getName()));
    }

    private String getNprWorkflowHoldUnholdString(String messageString, PLMNpr npr, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus plmWorkflowActivity = workflowStatusRepository.findOne(source.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflowActivity.getName()),
                highlightValue(plmWorkflow.getName()),
                npr.getNumber());
    }

    private String getNprWorkflowAddedString(String messageString, PLMNpr npr, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getNewValue()),
                    npr.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getNprWorkflowChangeString(String messageString, PLMNpr npr, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getOldValue()),
                    highlightValue(workflow.getNewValue()),
                    npr.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }


// -----------------------------------------------------------------------------------------

    @Async
    @EventListener
    public void NPRRequestedItemsAdded(NprEvents.NPRRequestedItemAddedEvent event) {
        List<PLMNprItem> requestedItems = event.getPlmNprItems();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.npr.requestedItem.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNpr().getId());
        object.setType("npr");
        as.setObject(object);
        as.setData(getNPRRequestedItemsAddedJson(requestedItems));

        activityStreamService.create(as);
    }

    private String getNPRRequestedItemsAddedJson(List<PLMNprItem> requestedItems) {
        List<AsNewNPRRequestedItem> asNewNprRequesteditems = new ArrayList<>();
        for (PLMNprItem requestedItem : requestedItems) {
            PLMNprItem plmNprItem = nprItemRepository.findOne(requestedItem.getId());
            AsNewNPRRequestedItem asNewAffectedItem = new AsNewNPRRequestedItem(plmNprItem.getId(),plmNprItem.getItemType().getName(), plmNprItem.getItemName(), plmNprItem.getItemNumber(), plmNprItem.getToRevision(), "");
            asNewNprRequesteditems.add(asNewAffectedItem);
        }
        String json = null;
        try {
            json = objectMapper.writeValueAsString(asNewNprRequesteditems);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getNprRequestedItemAddedString(String messageString, PLMNpr npr, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), npr.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.npr.requestedItem.add.item");

        String json = as.getData();
        try {
            List<AsNewNPRRequestedItem> requestedItems = objectMapper.readValue(json, new TypeReference<List<AsNewNPRRequestedItem>>() {
            });
            requestedItems.forEach(requestedItem -> {
                String s = addMarginToMessage(MessageFormat.format(fileString,highlightValue(requestedItem.getItemNumber()),highlightValue(requestedItem.getItemType()),highlightValue(requestedItem.getItemName())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    @Async
    @EventListener
    public void NPRRequestedItemDeleted(NprEvents.NPRRequestedItemDeletedEvent event) {
        PLMNprItem plmNprItem = event.getPlmNprItem();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.npr.requestedItem.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getNpr().getId());
        object.setType("npr");
        as.setObject(object);
        as.setData(getPLMNPRItemDeletedJson(plmNprItem));

        activityStreamService.create(as);
    }

    private String getPLMNPRItemDeletedJson(PLMNprItem plmNprItem) {
        String json = null;
        PLMNprItem plmNprItem1 = nprItemRepository.findOne(plmNprItem.getId());

        AsNewNPRRequestedItem asNewNPRRequestedItem = new AsNewNPRRequestedItem(plmNprItem1.getId(),plmNprItem1.getItemType().getName(), plmNprItem1.getItemName(), plmNprItem1.getItemNumber(), plmNprItem1.getToRevision(), "");

        List<AsNewNPRRequestedItem> asNewNPRRequestedItems = new ArrayList<>();
        asNewNPRRequestedItems.add(asNewNPRRequestedItem);
        try {
            json = objectMapper.writeValueAsString(asNewNPRRequestedItems);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getNPRRelatedItemDeletedString(String messageString, PLMNpr npr, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<AsNewNPRRequestedItem> asNewNPRRequestedItems = objectMapper.readValue(json, new TypeReference<List<AsNewNPRRequestedItem>>() {
            });
            AsNewNPRRequestedItem asNewNPRRequestedItem = asNewNPRRequestedItems.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(asNewNPRRequestedItem.getItemType()), highlightValue(asNewNPRRequestedItem.getItemNumber()), highlightValue(asNewNPRRequestedItem.getItemName()), npr.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    @Async
    @EventListener
    public void nprRequestedItemUpdated(NprEvents.NPRRequestedItemUpdateEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        object.setObject(event.getNpr().getId());
        object.setType("npr");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.npr.requestedItem.update");
        as.setObject(object);
        as.setConverter(getConverterKey());

        List<ASNPRRequestItemUpdate> changes = new ArrayList<>();
        PLMNprItem item = nprItemRepository.findOne(event.getNewPlmNprItem().getId());
        String oldValue = event.getOldPlmNprItem().getNotes();
        String newValue = event.getNewPlmNprItem().getNotes();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!oldValue.equals(newValue)) {
            changes.add(new ASNPRRequestItemUpdate(item.getItemNumber(), "Notes", oldValue, newValue));
        }
        try {
            if (changes.size() > 0) {
                as.setData(objectMapper.writeValueAsString(changes));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }


    private String getNPRRequestedItemUpdatedString(String messageString, PLMNpr npr, ActivityStream as) {
        StringBuffer sb = new StringBuffer();

        String updateString = activityStreamResourceBundle.getString("plm.npr.requestedItem.update.property");

        String json = as.getData();
        try {
            List<ASNPRRequestItemUpdate> propChanges = objectMapper.readValue(json, new TypeReference<List<ASNPRRequestItemUpdate>>() {
            });
            String activityString = MessageFormat.format(messageString, as.getActor().getFullName(), highlightValue(propChanges.get(0).getItemNumber()),
                    npr.getNumber());
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

}
