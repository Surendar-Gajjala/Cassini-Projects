package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.PPAPEvents;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.pqm.PQMPPAP;
import com.cassinisys.plm.model.pqm.PQMPPAPAttribute;
import com.cassinisys.plm.model.pqm.PQMPPAPChecklist;
import com.cassinisys.plm.model.pqm.PQMQualityTypeAttribute;
import com.cassinisys.plm.repo.plm.LifeCyclePhaseRepository;
import com.cassinisys.plm.repo.pqm.PPAPChecklistRepository;
import com.cassinisys.plm.repo.pqm.PPAPRepository;
import com.cassinisys.plm.repo.pqm.QualityTypeAttributeRepository;
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
public class PPAPActivityStream extends BaseActivityStream implements ActivityStreamConverter {

    @Autowired
    private CommonActivityStream commonActivityStream;

    @Autowired
    private PPAPRepository ppapRepository;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private QualityTypeAttributeRepository qualityTypeAttributeRepository;
    @Autowired
    private PPAPChecklistRepository ppapChecklistRepository;
    @Autowired
    private LifeCyclePhaseRepository lifeCyclePhaseRepository;

    @Async
    @EventListener
    public void ppapCreated(PPAPEvents.PPAPCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getPpap().getId());
        object.setType("ppap");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.ppap.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }


    @Async
    @EventListener
    public void ppapBasicInfoUpdated(PPAPEvents.PPAPBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PQMPPAP oldPpap = event.getOldPpap();
        PQMPPAP ppap = event.getNewPpap();

        object.setObject(ppap.getId());
        object.setType("ppap");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.ppap.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getPPAPBasicInfoUpdatedJson(oldPpap, ppap));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }


    @Async
    @EventListener
    public void ppapAttributesUpdated(PPAPEvents.PPAPAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PQMPPAP pqmppap = event.getPpap();

        PQMPPAPAttribute oldAtt = event.getOldAttribute();
        PQMPPAPAttribute newAtt = event.getNewAttribute();

        object.setObject(pqmppap.getId());
        object.setType("ppap");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.ppap.update.attributes");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getPPAPAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void ppapFilesAdded(PPAPEvents.PPAPFilesAddedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.ppap.checklist.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getPpap());
        object.setType("ppap");
        as.setObject(object);
        as.setData(getPPAPFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void ppapFoldersAdded(PPAPEvents.PPAPFoldersAddedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.ppap.checklist.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getPpap());
        object.setType("ppap");
        as.setObject(object);
        as.setData(getPPAPFoldersAddedJson(files));

        activityStreamService.create(as);

    }

    @Async
    @EventListener
    public void ppapFoldersDeleted(PPAPEvents.PPAPFoldersDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.ppap.checklist.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getPpap().getId());
        object.setType("ppap");
        as.setObject(object);
        as.setData(getPPAPFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void ppapFileDeleted(PPAPEvents.PPAPFileDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.ppap.checklist.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getPpap().getId());
        object.setType("ppap");
        as.setObject(object);
        as.setData(getPPAPFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void ppapFilesVersioned(PPAPEvents.PPAPFilesVersionedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.ppap.checklist.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getPpap());
        object.setType("ppap");
        as.setObject(object);
        as.setData(getPPAPFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void ppapFileRenamed(PPAPEvents.PPAPFileRenamedEvent event) {
        PLMFile oldFile = event.getOldFile();
        PLMFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals("Rename")) as.setActivity("plm.quality.ppap.checklist.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.quality.ppap.checklist.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getPpap().getId());
        object.setType("ppap");
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
    public void ppapFileLocked(PPAPEvents.PPAPFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.ppap.checklist.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getPpap().getId());
        object.setType("ppap");
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
    public void ppapFileUnlocked(PPAPEvents.PPAPFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.ppap.checklist.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getPpap().getId());
        object.setType("ppap");
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
    public void ppapFileDownloaded(PPAPEvents.PPAPFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.ppap.checklist.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getPpap().getId());
        object.setType("ppap");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getFile().getId(), event.getFile().getName(), FileUtils.byteCountToDisplaySize(event.getFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }


    @Override
    public String getConverterKey() {
        return "plm.quality.ppap";
    }


    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            PQMPPAP ppap = ppapRepository.findOne(object.getObject());

            if (ppap == null) return "";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.quality.ppap.create":
                    convertedString = getPPAPCreatedString(messageString, actor, ppap);
                    break;
                case "plm.quality.ppap.update.basicinfo":
                    convertedString = getPPAPBasicInfoUpdatedString(messageString, actor, ppap, as);
                    break;
                case "plm.quality.ppap.update.attributes":
                    convertedString = getPPAPAttributeUpdatedString(messageString, actor, ppap, as);
                    break;
                case "plm.quality.ppap.checklist.add":
                    convertedString = getPPAPFilesAddedString(messageString, ppap, as);
                    break;
                case "plm.quality.ppap.checklist.delete":
                    convertedString = getPPAPFileDeletedString(messageString, ppap, as);
                    break;
                case "plm.quality.ppap.checklist.folders.add":
                    convertedString = getPPAPFoldersAddedString(messageString, ppap, as);
                    break;
                case "plm.quality.ppap.checklist.folders.delete":
                    convertedString = getPPAPFoldersDeletedString(messageString, ppap, as);
                    break;
                case "plm.quality.ppap.checklist.version":
                    convertedString = getPPAPFilesVersionedString(messageString, ppap, as);
                    break;
                case "plm.quality.ppap.checklist.rename":
                    convertedString = getPPAPFileRenamedString(messageString, ppap, as);
                    break;
                case "plm.quality.ppap.checklist.replace":
                    convertedString = getPPAPFileRenamedString(messageString, ppap, as);
                    break;
                case "plm.quality.ppap.checklist.lock":
                    convertedString = getPPAPFileString(messageString, ppap, as);
                    break;
                case "plm.quality.ppap.checklist.unlock":
                    convertedString = getPPAPFileString(messageString, ppap, as);
                    break;
                case "plm.quality.ppap.checklist.download":
                    convertedString = getPPAPFileString(messageString, ppap, as);
                    break;
                case "plm.quality.ppap.checklist.reviewer.add":
                    convertedString = getPPAPChecklistReviewerAddedString(messageString, ppap, as);
                    break;
                case "plm.quality.ppap.checklist.reviewer.change":
                    convertedString = getPPAPChecklistReviewerUpdateString(messageString, ppap, as);
                    break;
                case "plm.quality.ppap.checklist.reviewer.delete":
                    convertedString = getPPAPChecklistReviewerDeletedString(messageString, ppap, as);
                    break;
                case "plm.quality.ppap.checklist.reviewer.approved":
                    convertedString = getPPAPChecklistReviewerApprovedString(messageString, ppap, as);
                    break;
                case "plm.quality.ppap.checklist.revised":
                    convertedString = getDocumentFileRenamedString(messageString, as);
                    break;
                case "plm.quality.ppap.checklist.promote":
                    convertedString = getManufacturerPartReportPromoteDemoteString(messageString, ppap, as);
                    break;
                case "plm.quality.ppap.checklist.demote":
                    convertedString = getManufacturerPartReportPromoteDemoteString(messageString, ppap, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }


    private String getPPAPCreatedString(String messageString, Person actor, PQMPPAP ppap) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), ppap.getNumber());
    }


    private String getPPAPBasicInfoUpdatedString(String messageString, Person actor, PQMPPAP ppap, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), ppap.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.quality.ppap.update.basicinfo.property");

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


    private String getPPAPAttributeUpdatedString(String messageString, Person actor, PQMPPAP pqmppap, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), pqmppap.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.quality.ppap.update.attributes.attribute");
        String propertyUpdateString = activityStreamResourceBundle.getString("plm.quality.ppap.update.attributes.attribute").substring(0, 21);

        String json = as.getData();
        try {
            List<ASAttributeChangeDTO> propChanges = objectMapper.readValue(json, new TypeReference<List<ASAttributeChangeDTO>>() {
            });
            propChanges.forEach(p -> {
                String s = null;
                if (p.getAttribute().equals("MULTIPLELIST") || p.getAttribute().equals("OBJECT") || p.getAttribute().equals("IMAGE") || p.getAttribute().equals("ATTACHMENT")) {
                    s = addMarginToMessage(MessageFormat.format(propertyUpdateString, highlightValue(p.getAttribute().toLowerCase())));
                    sb.append(s);
                } else {
                    s = addMarginToMessage(MessageFormat.format(updateString,
                            highlightValue(p.getAttribute()),
                            highlightValue(p.getOldValue()),
                            highlightValue(p.getNewValue())));
                    sb.append(s);
                }
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getPPAPAttributesUpdatedJson(PQMPPAPAttribute oldAttribute, PQMPPAPAttribute newAttribute) {
        PQMQualityTypeAttribute attDef = qualityTypeAttributeRepository.findOne(newAttribute.getId().getAttributeDef());
        List<ASAttributeChangeDTO> changes = commonActivityStream.getAttributeUpdateJsonData(oldAttribute, newAttribute, attDef);

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

    private String getPPAPBasicInfoUpdatedJson(PQMPPAP oldPpap, PQMPPAP ppap) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldPpap.getName();
        String newValue = ppap.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        oldValue = oldPpap.getDescription();
        newValue = ppap.getDescription();
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

    private String getPPAPFilesAddedJson(List<PLMFile> files) {
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

    private String getPPAPFoldersAddedJson(PLMFile file) {
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

    private String getPPAPFoldersDeletedJson(PLMFile file) {
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

    private String getPPAPFilesAddedString(String messageString, PQMPPAP ppap, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), ppap.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.quality.ppap.checklist.add.file");

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

    private String getPPAPFoldersAddedString(String messageString, PQMPPAP ppap, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), ppap.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getPPAPFoldersDeletedString(String messageString, PQMPPAP ppap, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), ppap.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getPPAPFileDeletedString(String messageString, PQMPPAP ppap, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), ppap.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getPPAPFilesVersionedJson(List<PLMFile> files) {
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

    private String getPPAPFilesVersionedString(String messageString, PQMPPAP ppap, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), ppap.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.quality.ppap.checklist.version.file");

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

    private String getPPAPFileRenamedString(String messageString, PQMPPAP ppap, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    ppap.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getPPAPFileString(String messageString, PQMPPAP ppap, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    ppap.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getPPAPChecklistReviewerAddedString(String messageString, PQMPPAP document, ActivityStream as) {

        StringBuffer sb = new StringBuffer();
        String fileString = activityStreamResourceBundle.getString("plm.quality.ppap.checklist.reviewer.add.reviewer");

        String json = as.getData();
        try {
            List<ASNewReviewerDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewReviewerDTO>>() {
            });
            String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()),
                    highlightValue(files.get(0).getName()), highlightValue(document.getName()));
            sb.append(activityString);
            files.forEach(f -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(f.getFullName()), highlightValue(f.getType())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return sb.toString();

    }

    private String getPPAPChecklistReviewerUpdateString(String messageString, PQMPPAP document, ActivityStream as) {
        StringBuffer sb = new StringBuffer();
        String fileString = activityStreamResourceBundle.getString("plm.quality.ppap.checklist.reviewer.change.reviewer");

        String json = as.getData();
        try {
            List<ASNewReviewerDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewReviewerDTO>>() {
            });
            String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()),
                    highlightValue(files.get(0).getName()), highlightValue(document.getName()));
            sb.append(activityString);
            files.forEach(f -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(f.getFullName()), highlightValue(f.getType())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private String getPPAPChecklistReviewerDeletedString(String messageString, PQMPPAP document, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            ASNewReviewerDTO reviewer = objectMapper.readValue(json, new TypeReference<ASNewReviewerDTO>() {
            });
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(reviewer.getType()),
                    highlightValue(reviewer.getFullName()), highlightValue(reviewer.getName()), highlightValue(document.getName()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getPPAPChecklistReviewerApprovedString(String messageString, PQMPPAP document, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            ASNewReviewerDTO reviewer = objectMapper.readValue(json, new TypeReference<ASNewReviewerDTO>() {
            });
            activityString = MessageFormat.format(messageString, highlightValue(reviewer.getFullName()), highlightValue(reviewer.getType()),
                    highlightValue(reviewer.getName()), highlightValue(document.getName()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getDocumentFileRenamedString(String messageString, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            PQMPPAPChecklist oldDocument = ppapChecklistRepository.findOne(asFileReplaceDto.getOldFileId());
            PQMPPAPChecklist newDocument = ppapChecklistRepository.findOne(asFileReplaceDto.getNewFileId());
            if (oldDocument != null && newDocument != null) {
                messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                        highlightValue(asFileReplaceDto.getOldFileName() + " - " + oldDocument.getRevision() + "." + oldDocument.getVersion()),
                        highlightValue(asFileReplaceDto.getNewFileName() + " - " + newDocument.getRevision() + "." + newDocument.getVersion()));
            } else {
                messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                        highlightValue(asFileReplaceDto.getOldFileName()),
                        highlightValue(asFileReplaceDto.getNewFileName()));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }


    private String getManufacturerPartReportPromoteDemoteString(String messageString, PQMPPAP manufacturerPart, ActivityStream as) {
        ActivityStreamObject source = as.getSource();
        ActivityStreamObject target = as.getTarget();
        ASNewFileDTO asNewFileDTO = null;
        PQMPPAPChecklist checklist = null;
        if (as.getData() != null && !as.getData().equals("")) {
            try {
                asNewFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
                });
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        PLMLifeCyclePhase fromLifeCyclePhase = lifeCyclePhaseRepository.findOne(source.getObject());
        PLMLifeCyclePhase toLifeCyclePhase = lifeCyclePhaseRepository.findOne(target.getObject());
        if (asNewFileDTO != null) {
            checklist = ppapChecklistRepository.findOne(asNewFileDTO.getId());
        }
        if (checklist != null) {
            return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(checklist.getName()),
                    highlightValue(checklist.getRevision()),
                    highlightValue(checklist.getVersion().toString()),
                    highlightValue(fromLifeCyclePhase.getPhase()),
                    highlightValue(toLifeCyclePhase.getPhase()));
        } else {
            return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    "", "", "",
                    highlightValue(fromLifeCyclePhase.getPhase()),
                    highlightValue(toLifeCyclePhase.getPhase()));
        }
    }
}
