package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.DocumentEvents;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.repo.plm.FileRepository;
import com.cassinisys.plm.repo.plm.LifeCyclePhaseRepository;
import com.cassinisys.plm.repo.pm.PLMDocumentRepository;
import com.cassinisys.plm.service.activitystream.dto.ASFileReplaceDto;
import com.cassinisys.plm.service.activitystream.dto.ASNewFileDTO;
import com.cassinisys.plm.service.activitystream.dto.ASNewReviewerDTO;
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
public class DocumentActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PLMDocumentRepository plmDocumentRepository;
    @Autowired
    private LifeCyclePhaseRepository lifeCyclePhaseRepository;
    @Autowired
    private FileRepository fileRepository;

    @Async
    @EventListener
    public void documentFilesAdded(DocumentEvents.DocumentFilesAddedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.documents.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(0);
        object.setType("documents");
        as.setObject(object);
        as.setData(getDocumentFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void documentFoldersAdded(DocumentEvents.DocumentFoldersAddedEvent event) {
        PLMDocument files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.documents.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(0);
        object.setType("documents");
        as.setObject(object);
        as.setData(getDocumentFoldersAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void documentFoldersDeleted(DocumentEvents.DocumentFoldersDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.documents.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(0);
        object.setType("documents");
        as.setObject(object);
        as.setData(getDocumentFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void documentFileDeleted(DocumentEvents.DocumentFileDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.documents.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(0);
        object.setType("documents");
        as.setObject(object);
        as.setData(getDocumentFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void documentFilesVersioned(DocumentEvents.DocumentFilesVersionedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.documents.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(0);
        object.setType("documents");
        as.setObject(object);
        as.setData(getDocumentFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void documentFileRenamed(DocumentEvents.DocumentFileRenamedEvent event) {
        PLMFile oldFile = event.getOldFile();
        PLMFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals("Rename")) as.setActivity("plm.documents.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.documents.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(0);
        object.setType("documents");
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
    public void documentFileLocked(DocumentEvents.DocumentFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.documents.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(0);
        object.setType("documents");
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
    public void documentFileUnlocked(DocumentEvents.DocumentFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.documents.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(0);
        object.setType("documents");
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
    public void documentFileDownloaded(DocumentEvents.DocumentFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.documents.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(0);
        object.setType("documents");
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
    public void documentLifeCyclePromoted(DocumentEvents.DocumentPromotedEvent event) {
        ActivityStream as = new ActivityStream();
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getId());
        as.setObject(object);

        ActivityStreamObject source = new ActivityStreamObject();
        source.setObject(event.getFromLifeCyclePhase().getId());
        as.setSource(source);

        ActivityStreamObject target = new ActivityStreamObject();
        target.setObject(event.getToLifeCyclePhase().getId());
        as.setTarget(target);
        PLMFile file = fileRepository.findOne(event.getFile());
        if (event.getType().name().equals(PLMObjectType.DOCUMENT.name())) {
            as.setActivity("plm.documents.lifeCyclePhase.promote");
            as.setConverter(getConverterKey());
            object.setType("documents");
            source.setType("documents");
            target.setType("documents");
        } else if (event.getType().name().equals(PLMObjectType.MFRPARTINSPECTIONREPORT.name())) {
            as.setActivity("plm.oem.manufacturerPart.inspectionreport.promote");
            as.setConverter("plm.oem.manufacturerPart");

            object.setType("manufacturerPart");
            source.setType("manufacturerPart");
            target.setType("manufacturerPart");
        } else if (event.getType().name().equals(PLMObjectType.PPAPCHECKLIST.name())) {
            as.setActivity("plm.quality.ppap.checklist.promote");
            as.setConverter("plm.quality.ppap");
            object.setType("ppap");
            source.setType("ppap");
            target.setType("ppap");
        }
        try {
            as.setData(objectMapper.writeValueAsString(new ASNewFileDTO(event.getFile(), file.getName(), "")));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void documentLifeCyclePromoted(DocumentEvents.DocumentRevisedEvent event) {
        ActivityStream as = new ActivityStream();
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getId());
        if (event.getType().name().equals(PLMObjectType.DOCUMENT.name())) {
            as.setActivity("plm.documents.revised");
            as.setConverter(getConverterKey());
            object.setType("documents");
        } else if (event.getType().name().equals(PLMObjectType.MFRPARTINSPECTIONREPORT.name())) {
            as.setActivity("plm.oem.manufacturerPart.inspectionreport.revised");
            as.setConverter("plm.oem.manufacturerPart");
            object.setType("manufacturerPart");
        } else if (event.getType().name().equals(PLMObjectType.PPAPCHECKLIST.name())) {
            as.setActivity("plm.quality.ppap.checklist.revised");
            as.setConverter("plm.quality.ppap");
            object.setType("ppap");
        }

        as.setObject(object);
        ASFileReplaceDto asFileReplaceDto = new ASFileReplaceDto(event.getOldDocument().getId(), event.getOldDocument().getName(), event.getDocument().getId(), event.getDocument().getName());
        try {
            as.setData(objectMapper.writeValueAsString(asFileReplaceDto));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemLifeCycleDemoted(DocumentEvents.DocumentDemotedEvent event) {
        ActivityStream as = new ActivityStream();
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getId());
        as.setObject(object);

        ActivityStreamObject source = new ActivityStreamObject();
        source.setObject(event.getFromLifeCyclePhase().getId());
        as.setSource(source);

        ActivityStreamObject target = new ActivityStreamObject();
        target.setObject(event.getToLifeCyclePhase().getId());
        as.setTarget(target);
        PLMFile file = fileRepository.findOne(event.getFile());
        if (event.getType().name().equals(PLMObjectType.DOCUMENT.name())) {
            as.setActivity("plm.documents.lifeCyclePhase.demote");
            as.setConverter(getConverterKey());
            object.setType("documents");
            source.setType("documents");
            target.setType("documents");
        } else if (event.getType().name().equals(PLMObjectType.MFRPARTINSPECTIONREPORT.name())) {
            as.setActivity("plm.oem.manufacturerPart.inspectionreport.demote");
            as.setConverter("plm.oem.manufacturerPart");

            object.setType("manufacturerPart");
            source.setType("manufacturerPart");
            target.setType("manufacturerPart");
        } else if (event.getType().name().equals(PLMObjectType.PPAPCHECKLIST.name())) {
            as.setActivity("plm.quality.ppap.checklist.demote");
            as.setConverter("plm.quality.ppap");
            object.setType("ppap");
            source.setType("ppap");
            target.setType("ppap");
        }
        try {
            as.setData(objectMapper.writeValueAsString(new ASNewFileDTO(event.getFile(), file.getName(), "")));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        activityStreamService.create(as);
    }


    @Async
    @EventListener
    public void requirementDocumentReviewerAdded(DocumentEvents.DocumentReviewerAddedEvent event) {
        ActivityStream as = new ActivityStream();
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getId());
        as.setObject(object);
        if (event.getType().name().equals(PLMObjectType.DOCUMENT.name())) {
            as.setActivity("plm.documents.reviewer.add");
            as.setConverter(getConverterKey());
            object.setType("documents");
        } else if (event.getType().name().equals(PLMObjectType.MFRPARTINSPECTIONREPORT.name())) {
            as.setActivity("plm.oem.manufacturerPart.inspectionreport.reviewer.add");
            as.setConverter("plm.oem.manufacturerPart");
            object.setType("manufacturerPart");
        } else if (event.getType().name().equals(PLMObjectType.PPAPCHECKLIST.name())) {
            as.setActivity("plm.quality.ppap.checklist.reviewer.add");
            as.setConverter("plm.quality.ppap");
            object.setType("ppap");
        }

        as.setData(getReqReviewerAddedJson(event.getDocument(), event.getReviewer()));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void requirementDocumentReviewerUpdate(DocumentEvents.DocumentReviewerUpdateEvent event) {
        ActivityStream as = new ActivityStream();
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getId());
        as.setObject(object);

        if (event.getType().name().equals(PLMObjectType.DOCUMENT.name())) {
            as.setActivity("plm.documents.reviewer.change");
            as.setConverter(getConverterKey());
            object.setType("documents");
        } else if (event.getType().name().equals(PLMObjectType.MFRPARTINSPECTIONREPORT.name())) {
            as.setActivity("plm.oem.manufacturerPart.inspectionreport.reviewer.change");
            as.setConverter("plm.oem.manufacturerPart");
            object.setType("manufacturerPart");
        } else if (event.getType().name().equals(PLMObjectType.PPAPCHECKLIST.name())) {
            as.setActivity("plm.quality.ppap.checklist.reviewer.change");
            as.setConverter("plm.quality.ppap");
            object.setType("ppap");
        }

        as.setData(getReqReviewerUpdateJson(event.getDocument(), event.getReviewer()));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void requirementDocumentReviewerDeleted(DocumentEvents.DocumentReviewerDeletedEvent event) {
        ActivityStream as = new ActivityStream();

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getId());
        as.setObject(object);
        if (event.getType().name().equals(PLMObjectType.DOCUMENT.name())) {
            as.setActivity("plm.documents.reviewer.delete");
            as.setConverter(getConverterKey());
            object.setType("documents");
        } else if (event.getType().name().equals(PLMObjectType.MFRPARTINSPECTIONREPORT.name())) {
            as.setActivity("plm.oem.manufacturerPart.inspectionreport.reviewer.delete");
            as.setConverter("plm.oem.manufacturerPart");
            object.setType("manufacturerPart");
        } else if (event.getType().name().equals(PLMObjectType.PPAPCHECKLIST.name())) {
            as.setActivity("plm.quality.ppap.checklist.reviewer.delete");
            as.setConverter("plm.quality.ppap");
            object.setType("ppap");
        }

        Person person = personRepository.findOne(event.getReviewer().getReviewer());
        try {
            as.setData(objectMapper.writeValueAsString(new ASNewReviewerDTO(event.getReviewer().getId(), person.getFullName(), "reviewer", event.getDocument().getName())));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void requirementReviewerApproved(DocumentEvents.DocumentReviewerSubmittedEvent event) {
        ActivityStream as = new ActivityStream();

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getId());
        as.setObject(object);

        if (event.getType().name().equals(PLMObjectType.DOCUMENT.name())) {
            as.setActivity("plm.documents.reviewer.approved");
            as.setConverter(getConverterKey());
            object.setType("documents");
        } else if (event.getType().name().equals(PLMObjectType.MFRPARTINSPECTIONREPORT.name())) {
            as.setActivity("plm.oem.manufacturerPart.inspectionreport.reviewer.approved");
            as.setConverter("plm.oem.manufacturerPart");
            object.setType("manufacturerPart");
        } else if (event.getType().name().equals(PLMObjectType.PPAPCHECKLIST.name())) {
            as.setActivity("plm.quality.ppap.checklist.reviewer.approved");
            as.setConverter("plm.quality.ppap");
            object.setType("ppap");
        }
        Person person = personRepository.findOne(event.getReviewer().getReviewer());
        String type = null;
        if (event.getReviewer().getStatus().equals(DocumentApprovalStatus.APPROVED)) {
            type = "approved";
        } else if (event.getReviewer().getStatus().equals(DocumentApprovalStatus.REJECTED)) {
            type = "rejected";
        } else if (event.getReviewer().getStatus().equals(DocumentApprovalStatus.REVIEWED)) {
            type = "reviewed";
        }
        try {
            as.setData(objectMapper.writeValueAsString(new ASNewReviewerDTO(event.getReviewer().getId(), person.getFullName(), type, event.getDocument().getName())));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        activityStreamService.create(as);
    }


    @Override
    public String getConverterKey() {
        return "plm.documents";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            String inspectionType = "";
            String activity = as.getActivity();
            PLMDocument document = plmDocumentRepository.findOne(object.getObject());

            if (!object.getObject().equals(0) && document == null) return "";

            switch (activity) {
                case "plm.documents.add":
                    convertedString = getDocumentFilesAddedString(messageString, as);
                    break;
                case "plm.documents.delete":
                    convertedString = getDocumentFileDeletedString(messageString, as);
                    break;
                case "plm.documents.folders.add":
                    convertedString = getDocumentFoldersAddedString(messageString, as);
                    break;
                case "plm.documents.folders.delete":
                    convertedString = getDocumentFoldersDeletedString(messageString, as);
                    break;
                case "plm.documents.version":
                    convertedString = getDocumentFilesVersionedString(messageString, as);
                    break;
                case "plm.documents.rename":
                    convertedString = getDocumentFileRenamedString(messageString, as);
                    break;
                case "plm.documents.replace":
                    convertedString = getDocumentFileRenamedString(messageString, as);
                    break;
                case "plm.documents.revised":
                    convertedString = getDocumentFileRenamedString(messageString, as);
                    break;
                case "plm.documents.lock":
                    convertedString = getDocumentFileString(messageString, as);
                    break;
                case "plm.documents.unlock":
                    convertedString = getDocumentFileString(messageString, as);
                    break;
                case "plm.documents.download":
                    convertedString = getDocumentFileString(messageString, as);
                    break;
                case "plm.documents.reviewer.add":
                    convertedString = getReqReviewerAddedString(messageString, document, as);
                    break;
                case "plm.documents.reviewer.change":
                    convertedString = getReqReviewerUpdateString(messageString, document, as);
                    break;
                case "plm.documents.reviewer.delete":
                    convertedString = getReqReviewerDeletedString(messageString, document, as);
                    break;
                case "plm.documents.reviewer.approved":
                    convertedString = getReqReviewerApprovedString(messageString, document, as);
                    break;
                case "plm.documents.lifeCyclePhase.promote":
                    convertedString = getRequirementDocumentPromoteDemoteString(messageString, document, as);
                    break;
                case "plm.documents.lifeCyclePhase.demote":
                    convertedString = getRequirementDocumentPromoteDemoteString(messageString, document, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getDocumentFilesAddedJson(List<PLMFile> files) {
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

    private String getDocumentFoldersAddedJson(PLMFile file) {
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

    private String getDocumentFoldersDeletedJson(PLMFile file) {
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

    private String getDocumentFilesAddedString(String messageString, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()));

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.documents.add.file");

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

    private String getDocumentFoldersAddedString(String messageString, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getDocumentFoldersDeletedString(String messageString, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getDocumentFileDeletedString(String messageString, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getDocumentFilesVersionedJson(List<PLMFile> files) {
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

    private String getDocumentFilesVersionedString(String messageString, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.documents.version.file");

        String json = as.getData();
        try {
            List<ASVersionedFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASVersionedFileDTO>>() {
            });
            files.forEach(f -> {
                PLMDocument document = plmDocumentRepository.findOne(f.getId());
                if (document != null) {
                    String s = addMarginToMessage(MessageFormat.format(fileString,
                            highlightValue(f.getName()),
                            highlightValue("" + document.getRevision() + "." + f.getOldVersion()),
                            highlightValue("" + document.getRevision() + "." + f.getNewVersion())));
                    sb.append(s);
                } else {
                    String s = addMarginToMessage(MessageFormat.format(fileString,
                            highlightValue(f.getName()),
                            highlightValue("" + f.getOldVersion()),
                            highlightValue("" + f.getNewVersion())));
                    sb.append(s);
                }
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getDocumentFileRenamedString(String messageString, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            PLMDocument oldDocument = plmDocumentRepository.findOne(asFileReplaceDto.getOldFileId());
            PLMDocument newDocument = plmDocumentRepository.findOne(asFileReplaceDto.getNewFileId());
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

    private String getDocumentFileString(String messageString, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }


    private String getReqReviewerAddedJson(PLMFile document, PLMDocumentReviewer reviewer) {
        List<ASNewReviewerDTO> asNewReviewerDTOs = new ArrayList<>();
        String type = "reviewer";
        Person person = personRepository.findOne(reviewer.getReviewer());
        ASNewReviewerDTO asNewReviewerDTO = new ASNewReviewerDTO(person.getId(), person.getFullName(), type, document.getName());
        asNewReviewerDTOs.add(asNewReviewerDTO);
        String json = null;
        try {
            json = objectMapper.writeValueAsString(asNewReviewerDTOs);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getReqReviewerUpdateJson(PLMFile document, PLMDocumentReviewer reviewer) {
        List<ASNewReviewerDTO> asNewReviewerDTOs = new ArrayList<>();
        String type = null;
        if (!reviewer.getApprover()) {
            type = "reviewer";
        } else {
            type = "approver";
        }
        Person person = personRepository.findOne(reviewer.getReviewer());
        ASNewReviewerDTO asNewReviewerDTO = new ASNewReviewerDTO(person.getId(), person.getFullName(), type, document.getName());
        asNewReviewerDTOs.add(asNewReviewerDTO);
        String json = null;
        try {
            json = objectMapper.writeValueAsString(asNewReviewerDTOs);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getReqReviewerAddedString(String messageString, PLMDocument document, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()),
                highlightValue(document.getName() + " - " + document.getRevision() + "." + document.getVersion()));

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.documents.reviewer.add.reviewer");

        String json = as.getData();
        try {
            List<ASNewReviewerDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewReviewerDTO>>() {
            });
            files.forEach(f -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(f.getFullName()), highlightValue(f.getType())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return sb.toString();

    }

    private String getReqReviewerUpdateString(String messageString, PLMDocument document, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()),
                highlightValue(document.getName() + " - " + document.getRevision() + "." + document.getVersion()));

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.documents.reviewer.change.reviewer");

        String json = as.getData();
        try {
            List<ASNewReviewerDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewReviewerDTO>>() {
            });
            files.forEach(f -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(f.getFullName()), highlightValue(f.getType())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private String getReqReviewerDeletedString(String messageString, PLMDocument document, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            ASNewReviewerDTO reviewer = objectMapper.readValue(json, new TypeReference<ASNewReviewerDTO>() {
            });
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(reviewer.getType()),
                    highlightValue(reviewer.getFullName()), highlightValue(document.getName() + " - " + document.getRevision() + "." + document.getVersion()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getReqReviewerApprovedString(String messageString, PLMDocument document, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            ASNewReviewerDTO reviewer = objectMapper.readValue(json, new TypeReference<ASNewReviewerDTO>() {
            });
            activityString = MessageFormat.format(messageString, highlightValue(reviewer.getFullName()), highlightValue(reviewer.getType()),
                    highlightValue(document.getName() + " - " + document.getRevision() + "." + document.getVersion()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getRequirementDocumentPromoteDemoteString(String messageString, PLMDocument document, ActivityStream as) {
        ActivityStreamObject source = as.getSource();
        ActivityStreamObject target = as.getTarget();

        PLMLifeCyclePhase fromLifeCyclePhase = lifeCyclePhaseRepository.findOne(source.getObject());
        PLMLifeCyclePhase toLifeCyclePhase = lifeCyclePhaseRepository.findOne(target.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(document.getName()),
                highlightValue(document.getRevision()),
                highlightValue(document.getVersion().toString()),
                highlightValue(fromLifeCyclePhase.getPhase()),
                highlightValue(toLifeCyclePhase.getPhase()));
    }

}
