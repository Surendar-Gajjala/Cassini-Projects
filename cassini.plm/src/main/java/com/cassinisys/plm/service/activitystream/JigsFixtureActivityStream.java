package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.JigsFixtureEvents;
import com.cassinisys.plm.model.mes.MESJigsFixture;
import com.cassinisys.plm.model.mes.MESManufacturerData;
import com.cassinisys.plm.model.mes.MESObjectAttribute;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.pqm.PQMQualityTypeAttribute;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.repo.mes.MESJigsFixtureRepository;
import com.cassinisys.plm.repo.pqm.QualityTypeAttributeRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowDefinitionRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowStatusRepository;
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
public class JigsFixtureActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PLMWorkflowRepository workFlowRepository;
    @Autowired
    private PLMWorkflowStatusRepository workflowStatusRepository;
    @Autowired
    private QualityTypeAttributeRepository qualityTypeAttributeRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository workflowDefinitionRepository;
    @Autowired
    private MESJigsFixtureRepository jigsFixtureRepository;
    @Autowired
    private MachineActivityStream machineActivityStream;

    @Async
    @EventListener
    public void jigsFixtureCreated(JigsFixtureEvents.JigFixtureCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getJigsFixture().getId());
        object.setType("jigfixture");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.jigfixture.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void jigsFixtureBasicInfoUpdated(JigsFixtureEvents.JigFixtureBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        MESJigsFixture oldPlan = event.getOldJigFixture();
        MESJigsFixture newPlan = event.getJigsFixture();

        object.setObject(newPlan.getId());
        object.setType("jigfixture");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.jigfixture.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getJigsFixtureBasicInfoUpdatedJson(oldPlan, newPlan));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void jigsFixtureAttributesUpdated(JigsFixtureEvents.JigFixtureAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        MESJigsFixture jigsFixture = event.getJigsFixture();

        MESObjectAttribute oldAtt = event.getOldAttribute();
        MESObjectAttribute newAtt = event.getNewAttribute();

        object.setObject(jigsFixture.getId());
        object.setType("jigfixture");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.jigfixture.update.attributes");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getJigsFixtureAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void jigsFixtureFilesAdded(JigsFixtureEvents.JigFixtureFilesAddedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.jigfixture.files.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getJigsFixture());
        object.setType("jigfixture");
        as.setObject(object);
        as.setData(getJigsFixtureFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void jigsFixtureFoldersAdded(JigsFixtureEvents.JigFixtureFoldersAddedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.jigfixture.files.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getJigsFixture());
        object.setType("jigfixture");
        as.setObject(object);
        as.setData(getJigsFixtureFoldersAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void jigsFixtureFoldersDeleted(JigsFixtureEvents.JigFixtureFoldersDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.jigfixture.files.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getJigsFixture());
        object.setType("jigfixture");
        as.setObject(object);
        as.setData(getJigsFixtureFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void jigsFixtureFileDeleted(JigsFixtureEvents.JigFixtureFileDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.jigfixture.files.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getJigsFixture());
        object.setType("jigfixture");
        as.setObject(object);
        as.setData(getJigsFixtureFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void jigsFixtureFilesVersioned(JigsFixtureEvents.JigFixtureFilesVersionedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.jigfixture.files.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getJigsFixture());
        object.setType("jigfixture");
        as.setObject(object);
        as.setData(getJigsFixtureFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void jigsFixtureFileRenamed(JigsFixtureEvents.JigFixtureFileRenamedEvent event) {
        PLMFile oldFile = event.getOldFile();
        PLMFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals("Rename")) as.setActivity("plm.mes.jigfixture.files.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.mes.jigfixture.files.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getJigsFixture());
        object.setType("jigfixture");
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
    public void jigsFixtureFileLocked(JigsFixtureEvents.JigFixtureFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.jigfixture.files.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getJigsFixture());
        object.setType("jigfixture");
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
    public void jigsFixtureFileUnlocked(JigsFixtureEvents.JigFixtureFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.jigfixture.files.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getJigsFixture());
        object.setType("jigfixture");
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
    public void jigsFixtureFileDownloaded(JigsFixtureEvents.JigFixtureFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.jigfixture.files.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getJigsFixture());
        object.setType("jigfixture");
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
    public void jigsFixtureCommentAdded(JigsFixtureEvents.JigFixtureCommentAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.jigfixture.comment");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getJigsFixture().getId());
        object.setType("jigfixture");
        as.setObject(object);
        as.setData(event.getComment().getComment());

        activityStreamService.create(as);
    }

    @Override
    public String getConverterKey() {
        return "plm.mes.jigfixture";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            MESJigsFixture jigsFixture = jigsFixtureRepository.findOne(object.getObject());

            if (jigsFixture == null) return "";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.mes.jigfixture.create":
                    convertedString = getJigsFixtureCreatedString(messageString, actor, jigsFixture);
                    break;
                case "plm.mes.jigfixture.update.basicinfo":
                    convertedString = getJigsFixtureBasicInfoUpdatedString(messageString, actor, jigsFixture, as);
                    break;
                case "plm.mes.jigfixture.update.attributes":
                    convertedString = getJigsFixtureAttributeUpdatedString(messageString, actor, jigsFixture, as);
                    break;
                case "plm.mes.jigfixture.files.add":
                    convertedString = getJigsFixtureFilesAddedString(messageString, jigsFixture, as);
                    break;
                case "plm.mes.jigfixture.files.delete":
                    convertedString = getJigsFixtureFileDeletedString(messageString, jigsFixture, as);
                    break;
                case "plm.mes.jigfixture.files.folders.add":
                    convertedString = getJigsFixtureFoldersAddedString(messageString, jigsFixture, as);
                    break;
                case "plm.mes.jigfixture.files.folders.delete":
                    convertedString = getJigsFixtureFoldersDeletedString(messageString, jigsFixture, as);
                    break;
                case "plm.mes.jigfixture.files.version":
                    convertedString = getJigsFixtureFilesVersionedString(messageString, jigsFixture, as);
                    break;
                case "plm.mes.jigfixture.files.rename":
                    convertedString = getJigsFixtureFileRenamedString(messageString, jigsFixture, as);
                    break;
                case "plm.mes.jigfixture.files.replace":
                    convertedString = getJigsFixtureFileRenamedString(messageString, jigsFixture, as);
                    break;
                case "plm.mes.jigfixture.files.lock":
                    convertedString = getJigsFixtureFileString(messageString, jigsFixture, as);
                    break;
                case "plm.mes.jigfixture.files.unlock":
                    convertedString = getJigsFixtureFileString(messageString, jigsFixture, as);
                    break;
                case "plm.mes.jigfixture.files.download":
                    convertedString = getJigsFixtureFileString(messageString, jigsFixture, as);
                    break;
                case "plm.mes.jigfixture.workflow.start":
                    convertedString = getJigsFixtureWorkflowStartString(messageString, jigsFixture, as);
                    break;
                case "plm.mes.jigfixture.workflow.finish":
                    convertedString = getJigsFixtureWorkflowFinishString(messageString, jigsFixture, as);
                    break;
                case "plm.mes.jigfixture.workflow.promote":
                    convertedString = getJigsFixtureWorkflowPromoteDemoteString(messageString, jigsFixture, as);
                    break;
                case "plm.mes.jigfixture.workflow.demote":
                    convertedString = getJigsFixtureWorkflowPromoteDemoteString(messageString, jigsFixture, as);
                    break;
                case "plm.mes.jigfixture.workflow.hold":
                    convertedString = getJigsFixtureWorkflowHoldUnholdString(messageString, jigsFixture, as);
                    break;
                case "plm.mes.jigfixture.workflow.unhold":
                    convertedString = getJigsFixtureWorkflowHoldUnholdString(messageString, jigsFixture, as);
                    break;
                case "plm.mes.jigfixture.workflow.add":
                    convertedString = getJigsFixtureWorkflowAddedString(messageString, jigsFixture, as);
                    break;
                case "plm.mes.jigfixture.workflow.change":
                    convertedString = getJigsFixtureWorkflowChangeString(messageString, jigsFixture, as);
                    break;
                case "plm.mes.jigfixture.comment":
                    convertedString = getJigsFixtureCommentString(messageString, jigsFixture, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getJigsFixtureCreatedString(String messageString, Person actor, MESJigsFixture jigsFixture) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), jigsFixture.getJigType().toString().toLowerCase(), jigsFixture.getNumber());
    }

    private String getJigsFixtureBasicInfoUpdatedString(String messageString, Person actor, MESJigsFixture jigsFixture, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), jigsFixture.getJigType().toString().toLowerCase(), jigsFixture.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mes.jigfixture.update.basicinfo.property");

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

    private String getJigsFixtureBasicInfoUpdatedJson(MESJigsFixture oldJigsFixture, MESJigsFixture jigsFixture) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldJigsFixture.getName();
        String newValue = jigsFixture.getName();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        oldValue = oldJigsFixture.getDescription();
        newValue = jigsFixture.getDescription();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue, newValue));
        }

        oldValue = oldJigsFixture.getActive().toString();
        newValue = jigsFixture.getActive().toString();

        if (!newValue.equals(oldValue)) {
            String newValuee;
            String oldValuee;
            if (newValue == "true") {
                newValuee = "ACTIVE";
            } else {
                newValuee = "INACTIVE";
            }
            if (oldValue == "true") {
                oldValuee = "ACTIVE";
            } else {
                oldValuee = "INACTIVE";
            }
            changes.add(new ASPropertyChangeDTO("Status", oldValuee, newValuee));
        }

        oldValue = oldJigsFixture.getRequiresMaintenance().toString();
        newValue = jigsFixture.getRequiresMaintenance().toString();

        if (!newValue.equals(oldValue)) {
            String newValuee;
            String oldValuee;
            if (newValue == "true") {
                newValuee = "Yes";
            } else {
                newValuee = "No";
            }
            if (oldValue == "true") {
                oldValuee = "Yes";
            } else {
                oldValuee = "No";
            }
            changes.add(new ASPropertyChangeDTO("RequiresMaintenance", oldValuee, newValuee));
        }

        MESManufacturerData oldMESMfr = oldJigsFixture.getManufacturerData();
        MESManufacturerData newMesMfr = jigsFixture.getManufacturerData();
        ASPropertyChangeDTO data = machineActivityStream.MESMfrBasicInfoUpdated(oldMESMfr, newMesMfr);
        if(data != null){
        changes.add(data);
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


    private String getJigsFixtureAttributesUpdatedJson(MESObjectAttribute oldAttribute, MESObjectAttribute newAttribute) {
        List<ASAttributeChangeDTO> changes = new ArrayList<>();

        String oldValue = "";
        if (oldAttribute != null) {
            oldValue = oldAttribute.getValueAsString();
        }
        String newValue = newAttribute.getValueAsString();

        PQMQualityTypeAttribute attDef = qualityTypeAttributeRepository.findOne(newAttribute.getId().getAttributeDef());
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

    private String getJigsFixtureAttributeUpdatedString(String messageString, Person actor, MESJigsFixture jigsFixture, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), jigsFixture.getJigType().toString().toLowerCase(), jigsFixture.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mes.jigfixture.update.attributes.attribute");

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

    private String getJigsFixtureFilesAddedJson(List<PLMFile> files) {
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

    private String getJigsFixtureFoldersAddedJson(PLMFile file) {
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

    private String getJigsFixtureFoldersDeletedJson(PLMFile file) {
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

    private String getJigsFixtureFilesAddedString(String messageString, MESJigsFixture jigsFixture, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), jigsFixture.getJigType().toString().toLowerCase(), jigsFixture.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mes.jigfixture.files.add.file");

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

    private String getJigsFixtureFoldersAddedString(String messageString, MESJigsFixture jigsFixture, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), jigsFixture.getJigType().toString().toLowerCase(), jigsFixture.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getJigsFixtureFoldersDeletedString(String messageString, MESJigsFixture jigsFixture, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), jigsFixture.getJigType().toString().toLowerCase(), jigsFixture.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getJigsFixtureFileDeletedString(String messageString, MESJigsFixture jigsFixture, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), jigsFixture.getJigType().toString().toLowerCase(), jigsFixture.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getJigsFixtureFilesVersionedJson(List<PLMFile> files) {
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

    private String getJigsFixtureFilesVersionedString(String messageString, MESJigsFixture jigsFixture, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), jigsFixture.getJigType().toString().toLowerCase(), jigsFixture.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mes.jigfixture.files.version.file");

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

    private String getJigsFixtureFileRenamedString(String messageString, MESJigsFixture jigsFixture, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    jigsFixture.getJigType().toString().toLowerCase(),
                    jigsFixture.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getJigsFixtureFileString(String messageString, MESJigsFixture jigsFixture, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    jigsFixture.getJigType().toString().toLowerCase(),
                    jigsFixture.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getJigsFixtureWorkflowStartString(String messageString, MESJigsFixture jigsFixture, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                jigsFixture.getJigType().toString().toLowerCase(),
                highlightValue(plmWorkflow.getName()),
                jigsFixture.getNumber());
    }

    private String getJigsFixtureWorkflowFinishString(String messageString, MESJigsFixture jigsFixture, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                jigsFixture.getJigType().toString().toLowerCase(),
                highlightValue(plmWorkflow.getName()),
                jigsFixture.getNumber());
    }

    private String getJigsFixtureWorkflowPromoteDemoteString(String messageString, MESJigsFixture jigsFixture, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();
        ActivityStreamObject target = as.getTarget();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus fromActivity = workflowStatusRepository.findOne(source.getObject());
        PLMWorkflowStatus toActivity = workflowStatusRepository.findOne(target.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                jigsFixture.getJigType().toString().toLowerCase(),
                jigsFixture.getNumber(),
                highlightValue(fromActivity.getName()),
                highlightValue(toActivity.getName()));
    }

    private String getJigsFixtureWorkflowHoldUnholdString(String messageString, MESJigsFixture jigsFixture, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus plmWorkflowActivity = workflowStatusRepository.findOne(source.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflowActivity.getName()),
                highlightValue(plmWorkflow.getName()),
                jigsFixture.getJigType().toString().toLowerCase(),
                jigsFixture.getNumber());
    }

    private String getJigsFixtureWorkflowAddedString(String messageString, MESJigsFixture jigsFixture, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getNewValue()),
                    jigsFixture.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getJigsFixtureWorkflowChangeString(String messageString, MESJigsFixture jigsFixture, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getOldValue()),
                    highlightValue(workflow.getNewValue()),
                    jigsFixture.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }


    private String getJigsFixtureCommentString(String messageString, MESJigsFixture jigsFixture, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                jigsFixture.getNumber());

        String s = highlightValue("Comment: ");
        s += as.getData();
        s = addMarginToMessage(s);

        message += s;

        return message;
    }

}
