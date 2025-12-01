package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.OperationEvents;
import com.cassinisys.plm.model.mes.*;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.repo.mes.MESObjectTypeAttributeRepository;
import com.cassinisys.plm.repo.mes.MESOperationRepository;
import com.cassinisys.plm.service.activitystream.dto.ASAttributeChangeDTO;
import com.cassinisys.plm.service.activitystream.dto.ASFileReplaceDto;
import com.cassinisys.plm.service.activitystream.dto.ASNewFileDTO;
import com.cassinisys.plm.service.activitystream.dto.ASVersionedFileDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 29-10-2020.
 */
@Component
public class OperationActivityStream extends BaseActivityStream implements ActivityStreamConverter {


    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MESOperationRepository mesOperationRepository;
    @Autowired
    private MESObjectTypeAttributeRepository mesObjectTypeAttributeRepository;
    @Autowired
    private PersonRepository personRepository;

    @Async
    @EventListener
    public void operationsCreated(OperationEvents.OperationCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getOperation().getId());
        object.setType("operation");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.operation.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void operationBasicInfoUpdated(OperationEvents.OperationBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        MESOperation oldOperation = event.getOldOperation();
        MESOperation newOperation = event.getNewOperation();

        object.setObject(newOperation.getId());
        object.setType("operation");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.operation.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getOperationBasicInfoUpdatedJson(oldOperation, newOperation));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void operationAttributesUpdated(OperationEvents.OperationAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        MESOperation operation = event.getOperation();

        MESObjectAttribute oldAtt = event.getOldAttribute();
        MESObjectAttribute newAtt = event.getNewAttribute();

        object.setObject(operation.getId());
        object.setType("operation");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.operation.update.attributes");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getOperationAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    
 @Async
 @EventListener
 public void operationsResourceCreated(OperationEvents.OperationResourceCreatedEvent event) {
     ActivityStreamObject object = new ActivityStreamObject();
     object.setObject(event.getOperation().getId());
     object.setType("operation");

     ActivityStream as = new ActivityStream();
     as.setActivity("plm.mes.operation.resource.create");
     as.setConverter(getConverterKey());
     as.setObject(object);
     activityStreamService.create(as);
 }

 @Async
 @EventListener
 public void operationsResourceDeleted(OperationEvents.OperationResourceDeletedEvent event) {
     ActivityStreamObject object = new ActivityStreamObject();
      object.setObject(event.getOperation().getId());
     object.setType("operation");

     ActivityStream as = new ActivityStream();
     as.setActivity("plm.mes.operation.resource.delete");
     as.setConverter(getConverterKey());
     as.setObject(object);
     activityStreamService.create(as);
 }

 @Async
 @EventListener
 public void operationResourceUpdated(OperationEvents.OperationResourceUpdatedEvent event) {
     ActivityStreamObject object = new ActivityStreamObject();

     MESOperationResources oldOperation = event.getOldResource();
     MESOperationResources newOperation = event.getNewResource();

     object.setObject(newOperation.getOperation());
     object.setType("operation");

     ActivityStream as = new ActivityStream();
     as.setActivity("plm.mes.operation.resource.update");
     as.setObject(object);
     as.setConverter(getConverterKey());
     as.setData(getOperationResourceUpdatedJson(oldOperation, newOperation));
     if (as.getData() != null) {
         activityStreamService.create(as);
     }
 }



    @Override
    public String getConverterKey() {
        return "plm.mes.operation";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            MESOperation operation = mesOperationRepository.findOne(object.getObject());
            String operationType = "";
            if (operation == null) return "";

            if (operation.getObjectType().equals(ObjectType.valueOf(MESEnumObject.OPERATION.toString()))) {
                operationType = "operation";
              }     

            String activity = as.getActivity();
            switch (activity) {
                case "plm.mes.operation.create":
                    convertedString = getOperationCreatedString(messageString, actor, operation);
                    break;
                case "plm.mes.operation.update.basicinfo":
                    convertedString = getOperationBasicInfoUpdatedString(messageString, actor, operation, operationType, as);
                    break;
                case "plm.mes.operation.update.attributes":
                    convertedString = getOperationAttributeUpdatedString(messageString, actor, operation, as);
                    break;
                case "plm.mes.operation.files.add":
                    convertedString = getOperationFilesAddedString(messageString, operation, as);
                    break;
                case "plm.mes.operation.files.delete":
                    convertedString = getOperationFileDeletedString(messageString, operation, as);
                    break;
                case "plm.mes.operation.files.folders.add":
                    convertedString = getOperationFoldersAddedString(messageString, operation, as);
                    break;
                case "plm.mes.operation.files.folders.delete":
                    convertedString = getOperationFoldersDeletedString(messageString, operation, as);
                    break;
                case "plm.mes.operation.files.version":
                    convertedString = getOperationFilesVersionedString(messageString, operation, as);
                    break;
                case "plm.mes.operation.files.rename":
                    convertedString = getOperationFileRenamedString(messageString, operation, as);
                    break;
                case "plm.mes.operation.files.replace":
                    convertedString = getOperationFileRenamedString(messageString, operation, as);
                    break;
                case "plm.mes.operation.files.lock":
                    convertedString = getOperationFileString(messageString, operation, as);
                    break;
                case "plm.mes.operation.files.unlock":
                    convertedString = getOperationFileString(messageString, operation, as);
                    break;
                case "plm.mes.operation.files.download":
                    convertedString = getOperationFileString(messageString, operation, as);
                    break;
                case "plm.mes.operation.resource.create":
                    convertedString = getOperationResourceCreatedString(messageString, actor, operation);
                    break;
                case "plm.mes.operation.resource.delete":
                    convertedString = getOperationResourceDeletedString(messageString, actor, operation);
                    break;

               case "plm.mes.operation.resource.update":
                    convertedString = getOperationResourceUpdatedString(messageString, actor, operation, operationType, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getOperationCreatedString(String messageString, Person actor, MESOperation operation) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), operation.getNumber());
    }

    private String getOperationResourceCreatedString(String messageString, Person actor, MESOperation operation) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), operation.getNumber());
    }

    private String getOperationResourceDeletedString(String messageString, Person actor, MESOperation operation) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), operation.getNumber());
    }



    private String getOperationResourceUpdatedString(String messageString, Person actor, MESOperation operation, String type, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), type, operation.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mes.operation.resource.update.property");

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



    private String getOperationBasicInfoUpdatedString(String messageString, Person actor, MESOperation operation, String type, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), type, operation.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mes.operation.update.basicinfo.property");

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

    private String getOperationBasicInfoUpdatedJson(MESOperation oldOperation, MESOperation newOperation) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldOperation.getName();
        String newValue = newOperation.getName();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        oldValue = oldOperation.getDescription();
        newValue = newOperation.getDescription();
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


    private String getOperationAttributesUpdatedJson(MESObjectAttribute oldAttribute, MESObjectAttribute newAttribute) {
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

    private String getOperationAttributeUpdatedString(String messageString, Person actor, MESOperation operation, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), operation.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("mes.manufacturing.operation.update.attributes.attribute");

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

    private String getOperationFilesAddedString(String messageString, MESOperation operation, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), operation.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mes.operation.files.add.file");

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

    private String getOperationFoldersAddedString(String messageString, MESOperation operation, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), operation.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getOperationFoldersDeletedString(String messageString, MESOperation operation, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), operation.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getOperationFileDeletedString(String messageString, MESOperation operation, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), operation.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getOperationFilesVersionedJson(List<PLMFile> files) {
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

    private String getOperationFilesVersionedString(String messageString, MESOperation operation, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), operation.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mes.operation.files.version.file");

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

    private String getOperationFileRenamedString(String messageString, MESOperation operation, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    operation.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getOperationFileString(String messageString, MESOperation operation, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    operation.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getOperationResourceUpdatedJson(MESOperationResources oldResource, MESOperationResources newResource) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

       String  oldValue = oldResource.getDescription();
       String newValue = newResource.getDescription();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue, newValue));
        }

      String  oldValue1 = oldResource.getQuantity().toString();
      String newValue1 = newResource.getQuantity().toString();
        if (oldValue1 == null) {
            oldValue1 = "" ;
        }
        if (newValue1 == null) {
            newValue1 = ""; 
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Quantity", oldValue1, newValue1));
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


}
