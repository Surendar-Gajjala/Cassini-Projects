package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.ManpowerEvents;
import com.cassinisys.plm.model.mes.MESManpower;
import com.cassinisys.plm.model.mes.MESManpowerContact;
import com.cassinisys.plm.model.mes.MESObjectAttribute;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.pqm.PQMQualityTypeAttribute;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.repo.mes.MESManpowerRepository;
import com.cassinisys.plm.repo.pqm.QualityTypeAttributeRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowDefinitionRepository;
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
public class ManpowerActivityStream extends BaseActivityStream implements ActivityStreamConverter {
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
    private MESManpowerRepository manpowerRepository;

    @Async
    @EventListener
    public void manpowerCreated(ManpowerEvents.ManpowerCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getManpower().getId());
        object.setType("manpower");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.manpower.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void manpowerPersonCreated(ManpowerEvents.ManpowerPersonCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getManpower().getId());
        object.setType("manpower");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.manpower.persons.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    
    @Async
    @EventListener
    public void manpowerPersonDeleted(ManpowerEvents.ManpowerPersonDeletedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getManpower().getId());
        object.setType("manpower");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.manpower.persons.delete");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    
    @Async
    @EventListener
    public void manpowerPersonUpdated(ManpowerEvents.ManpowerPersonUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        MESManpowerContact oldPlan = event.getOldContact();
        MESManpowerContact newPlan = event.getNewContact();

        object.setObject(newPlan.getManpower());
        object.setType("manpower");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.manpower.persons.update");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getManpowerPersonUpdatedJson(oldPlan, newPlan));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }


    @Async
    @EventListener
    public void manpowerBasicInfoUpdated(ManpowerEvents.ManpowerBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        MESManpower oldPlan = event.getOldManpower();
        MESManpower newPlan = event.getManpower();

        object.setObject(newPlan.getId());
        object.setType("manpower");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.manpower.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getManpowerBasicInfoUpdatedJson(oldPlan, newPlan));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void manpowerAttributesUpdated(ManpowerEvents.ManpowerAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        MESManpower manpower = event.getManpower();

        MESObjectAttribute oldAtt = event.getOldAttribute();
        MESObjectAttribute newAtt = event.getNewAttribute();

        object.setObject(manpower.getId());
        object.setType("manpower");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.manpower.update.attributes");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getManpowerAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void manpowerFilesAdded(ManpowerEvents.ManpowerFilesAddedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.manpower.files.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getManpower());
        object.setType("manpower");
        as.setObject(object);
        as.setData(getManpowerFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void manpowerFoldersAdded(ManpowerEvents.ManpowerFoldersAddedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.manpower.files.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getManpower());
        object.setType("manpower");
        as.setObject(object);
        as.setData(getManpowerFoldersAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void manpowerFoldersDeleted(ManpowerEvents.ManpowerFoldersDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.manpower.files.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getManpower());
        object.setType("manpower");
        as.setObject(object);
        as.setData(getManpowerFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void manpowerFileDeleted(ManpowerEvents.ManpowerFileDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.manpower.files.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getManpower());
        object.setType("manpower");
        as.setObject(object);
        as.setData(getManpowerFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void manpowerFilesVersioned(ManpowerEvents.ManpowerFilesVersionedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.manpower.files.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getManpower());
        object.setType("manpower");
        as.setObject(object);
        as.setData(getManpowerFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void manpowerFileRenamed(ManpowerEvents.ManpowerFileRenamedEvent event) {
        PLMFile oldFile = event.getOldFile();
        PLMFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals("Rename")) as.setActivity("plm.mes.manpower.files.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.mes.manpower.files.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getManpower());
        object.setType("manpower");
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
    public void manpowerFileLocked(ManpowerEvents.ManpowerFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.manpower.files.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getManpower());
        object.setType("manpower");
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
    public void manpowerFileUnlocked(ManpowerEvents.ManpowerFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.manpower.files.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getManpower());
        object.setType("manpower");
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
    public void manpowerFileDownloaded(ManpowerEvents.ManpowerFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.manpower.files.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getManpower());
        object.setType("manpower");
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
    public void manpowerCommentAdded(ManpowerEvents.ManpowerCommentAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.manpower.comment");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getManpower().getId());
        object.setType("manpower");
        as.setObject(object);
        as.setData(event.getComment().getComment());

        activityStreamService.create(as);
    }

    @Override
    public String getConverterKey() {
        return "plm.mes.manpower";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            MESManpower manpower = manpowerRepository.findOne(object.getObject());

            if (manpower == null) return "";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.mes.manpower.create":
                    convertedString = getManpowerCreatedString(messageString, actor, manpower);
                    break;
                case "plm.mes.manpower.persons.create":
                    convertedString = getManpowerPersonCreatedString(messageString, actor, manpower);
                    break;
                case "plm.mes.manpower.persons.delete":
                    convertedString = getManpowerPersonDeletedString(messageString, actor, manpower);
                    break;
                case "plm.mes.manpower.update.basicinfo":
                    convertedString = getManpowerBasicInfoUpdatedString(messageString, actor, manpower, as);
                    break;
               case "plm.mes.manpower.persons.update":
                    convertedString = getManpowerPersonUpdatedString(messageString, actor, manpower, as);
                    break;
                case "plm.mes.manpower.update.attributes":
                    convertedString = getManpowerAttributeUpdatedString(messageString, actor, manpower, as);
                    break;
                case "plm.mes.manpower.files.add":
                    convertedString = getManpowerFilesAddedString(messageString, manpower, as);
                    break;
                case "plm.mes.manpower.files.delete":
                    convertedString = getManpowerFileDeletedString(messageString, manpower, as);
                    break;
                case "plm.mes.manpower.files.folders.add":
                    convertedString = getManpowerFoldersAddedString(messageString, manpower, as);
                    break;
                case "plm.mes.manpower.files.folders.delete":
                    convertedString = getManpowerFoldersDeletedString(messageString, manpower, as);
                    break;
                case "plm.mes.manpower.files.version":
                    convertedString = getManpowerFilesVersionedString(messageString, manpower, as);
                    break;
                case "plm.mes.manpower.files.rename":
                    convertedString = getManpowerFileRenamedString(messageString, manpower, as);
                    break;
                case "plm.mes.manpower.files.replace":
                    convertedString = getManpowerFileRenamedString(messageString, manpower, as);
                    break;
                case "plm.mes.manpower.files.lock":
                    convertedString = getManpowerFileString(messageString, manpower, as);
                    break;
                case "plm.mes.manpower.files.unlock":
                    convertedString = getManpowerFileString(messageString, manpower, as);
                    break;
                case "plm.mes.manpower.files.download":
                    convertedString = getManpowerFileString(messageString, manpower, as);
                    break;
                case "plm.mes.manpower.workflow.start":
                    convertedString = getManpowerWorkflowStartString(messageString, manpower, as);
                    break;
                case "plm.mes.manpower.workflow.finish":
                    convertedString = getManpowerWorkflowFinishString(messageString, manpower, as);
                    break;
                case "plm.mes.manpower.workflow.promote":
                    convertedString = getManpowerWorkflowPromoteDemoteString(messageString, manpower, as);
                    break;
                case "plm.mes.manpower.workflow.demote":
                    convertedString = getManpowerWorkflowPromoteDemoteString(messageString, manpower, as);
                    break;
                case "plm.mes.manpower.workflow.hold":
                    convertedString = getManpowerWorkflowHoldUnholdString(messageString, manpower, as);
                    break;
                case "plm.mes.manpower.workflow.unhold":
                    convertedString = getManpowerWorkflowHoldUnholdString(messageString, manpower, as);
                    break;
                case "plm.mes.manpower.workflow.add":
                    convertedString = getManpowerWorkflowAddedString(messageString, manpower, as);
                    break;
                case "plm.mes.manpower.workflow.change":
                    convertedString = getManpowerWorkflowChangeString(messageString, manpower, as);
                    break;
                case "plm.mes.manpower.comment":
                    convertedString = getManpowerCommentString(messageString, manpower, as);
                    break;
                case "plm.mes.manpower.relatedItems.add":
                    convertedString = getManpowerRelatedItemsAddedString(messageString, manpower, as);
                    break;
                case "plm.mes.manpower.relatedItems.delete":
                    convertedString = getManpowerRelatedItemsDeletedString(messageString, manpower, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getManpowerCreatedString(String messageString, Person actor, MESManpower manpower) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), manpower.getNumber());
    }

    private String getManpowerPersonCreatedString(String messageString, Person actor, MESManpower manpower) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), manpower.getNumber());
    }

    private String getManpowerPersonDeletedString(String messageString, Person actor, MESManpower manpower) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), manpower.getNumber());
    }

    private String getManpowerRevisionCreatedString(String messageString, Person actor, MESManpower manpower) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), manpower.getNumber());
    }

    private String getManpowerBasicInfoUpdatedString(String messageString, Person actor, MESManpower manpower, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), manpower.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mes.manpower.update.basicinfo.property");

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

    private String getManpowerPersonUpdatedString(String messageString, Person actor, MESManpower manpower, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), manpower.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mes.manpower.persons.update.property");

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




    private String getManpowerPersonUpdatedJson(MESManpowerContact oldContact, MESManpowerContact newContact) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldContact.getPerson().getFirstName();
        String newValue = newContact.getPerson().getFirstName();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("FirstName", oldValue, newValue));
        }

        oldValue = oldContact.getPerson().getPhoneMobile();
        newValue = newContact.getPerson().getPhoneMobile();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("PhoneNumber", oldValue, newValue));
        }

        oldValue = oldContact.getPerson().getEmail();
        newValue = newContact.getPerson().getEmail();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Email", oldValue, newValue));
        }

        oldValue = oldContact.getPerson().getLastName(); 
        newValue = newContact.getPerson().getLastName();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("LastName", oldValue, newValue));
        }



        oldValue = oldContact.getActive().toString();
        newValue = newContact.getActive().toString();

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



    private String getManpowerBasicInfoUpdatedJson(MESManpower oldManpower, MESManpower manpower) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldManpower.getName();
        String newValue = manpower.getName();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        oldValue = oldManpower.getDescription();
        newValue = manpower.getDescription();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue, newValue));
        }

  /*      oldValue = oldManpower.getActive().toString();
        newValue = manpower.getActive().toString();

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
        }*/

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


    private String getManpowerAttributesUpdatedJson(MESObjectAttribute oldAttribute, MESObjectAttribute newAttribute) {
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

    private String getManpowerAttributeUpdatedString(String messageString, Person actor, MESManpower manpower, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), manpower.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mes.manpower.update.attributes.attribute");

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

    private String getManpowerFilesAddedJson(List<PLMFile> files) {
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

    private String getManpowerFoldersAddedJson(PLMFile file) {
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

    private String getManpowerFoldersDeletedJson(PLMFile file) {
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

    private String getManpowerFilesAddedString(String messageString, MESManpower manpower, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), manpower.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mes.manpower.files.add.file");

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

    private String getManpowerFoldersAddedString(String messageString, MESManpower manpower, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), manpower.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getManpowerFoldersDeletedString(String messageString, MESManpower manpower, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), manpower.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getManpowerFileDeletedString(String messageString, MESManpower manpower, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), manpower.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getManpowerFilesVersionedJson(List<PLMFile> files) {
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

    private String getManpowerFilesVersionedString(String messageString, MESManpower manpower, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), manpower.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mes.manpower.files.version.file");

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

    private String getManpowerFileRenamedString(String messageString, MESManpower manpower, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    manpower.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getManpowerFileString(String messageString, MESManpower manpower, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    manpower.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getManpowerWorkflowStartString(String messageString, MESManpower manpower, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                manpower.getNumber());
    }

    private String getManpowerWorkflowFinishString(String messageString, MESManpower manpower, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                manpower.getNumber());
    }

    private String getManpowerWorkflowPromoteDemoteString(String messageString, MESManpower manpower, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();
        ActivityStreamObject target = as.getTarget();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus fromActivity = workflowStatusRepository.findOne(source.getObject());
        PLMWorkflowStatus toActivity = workflowStatusRepository.findOne(target.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                manpower.getNumber(),
                highlightValue(fromActivity.getName()),
                highlightValue(toActivity.getName()));
    }

    private String getManpowerWorkflowHoldUnholdString(String messageString, MESManpower manpower, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus plmWorkflowActivity = workflowStatusRepository.findOne(source.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflowActivity.getName()),
                highlightValue(plmWorkflow.getName()),
                manpower.getNumber());
    }

    private String getManpowerWorkflowAddedString(String messageString, MESManpower manpower, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getNewValue()),
                    manpower.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getManpowerWorkflowChangeString(String messageString, MESManpower manpower, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getOldValue()),
                    highlightValue(workflow.getNewValue()),
                    manpower.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }


    private String getManpowerCommentString(String messageString, MESManpower manpower, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                manpower.getNumber());

        String s = highlightValue("Comment: ");
        s += as.getData();
        s = addMarginToMessage(s);

        message += s;

        return message;
    }

    private String getManpowerRelatedItemsAddedString(String messageString, MESManpower manpower, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), manpower.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = "";
        if (manpower.getObjectType().equals(ObjectType.valueOf(PLMObjectType.ITEMINSPECTION.toString()))) {
            fileString = activityStreamResourceBundle.getString("plm.mes.manpower.relatedItems.add.item");
        } else {
            fileString = activityStreamResourceBundle.getString("plm.mes.manpower.relatedItems.add.part");
        }

        String json = as.getData();
        try {
            if (manpower.getObjectType().equals(ObjectType.valueOf(PLMObjectType.ITEMINSPECTION.toString()))) {
                List<ASNewPRProblemItem> itemList = objectMapper.readValue(json, new TypeReference<List<ASNewPRProblemItem>>() {
                });
                for (ASNewPRProblemItem item : itemList) {
                    String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(item.getItemName()), highlightValue(item.getRevision()),
                            highlightValue(item.getLifecyclePhase())));
                    sb.append(s);
                }
            } else {
                List<ASNewNCRProblemItem> partList = objectMapper.readValue(json, new TypeReference<List<ASNewNCRProblemItem>>() {
                });
                for (ASNewNCRProblemItem part : partList) {
                    String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(part.getPartName()), highlightValue(part.getPartNumber()),
                            highlightValue(part.getPartType())));
                    sb.append(s);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getManpowerRelatedItemsDeletedString(String messageString, MESManpower manpower, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            if (manpower.getObjectType().equals(ObjectType.valueOf(PLMObjectType.ITEMINSPECTION.toString()))) {
                List<ASNewPRProblemItem> asNewPRProblemItems = objectMapper.readValue(json, new TypeReference<List<ASNewPRProblemItem>>() {
                });
                ASNewPRProblemItem asNewPRProblemItem = asNewPRProblemItems.get(0);
                activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(asNewPRProblemItem.getItemName()),
                        highlightValue(asNewPRProblemItem.getRevision()), highlightValue(asNewPRProblemItem.getLifecyclePhase()), manpower.getNumber());
            } else {
                List<ASNewNCRProblemItem> asNewPRProblemItems = objectMapper.readValue(json, new TypeReference<List<ASNewNCRProblemItem>>() {
                });
                ASNewNCRProblemItem asNewPRProblemItem = asNewPRProblemItems.get(0);
                activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(asNewPRProblemItem.getPartNumber()),
                        manpower.getNumber());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }
}
