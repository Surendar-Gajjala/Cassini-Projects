package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.MachineEvents;
import com.cassinisys.plm.model.mes.*;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.repo.mes.MESMachineRepository;
import com.cassinisys.plm.repo.mes.MESObjectTypeAttributeRepository;
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

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Hello on 10/30/2020.
 */
@Component
public class MachineActivityStream extends BaseActivityStream implements ActivityStreamConverter {


    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MESMachineRepository mesMachineRepository;
    @Autowired
    private MESObjectTypeAttributeRepository mesObjectTypeAttributeRepository;
    @Autowired
    private PersonRepository personRepository;

    @Async
    @EventListener
    public void machinesCreated(MachineEvents.MachineCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMachine().getId());
        object.setType("machine");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.machine.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void machineBasicInfoUpdated(MachineEvents.MachineBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        MESMachine oldMachine = event.getOldMachine();
        MESMachine newMachine = event.getNewMachine();

        object.setObject(newMachine.getId());
        object.setType("machine");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.machine.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getMachineBasicInfoUpdatedJson(oldMachine, newMachine));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void machineAttributesUpdated(MachineEvents.MachineAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        MESMachine machine = event.getMachine();

        MESObjectAttribute oldAtt = event.getOldAttribute();
        MESObjectAttribute newAtt = event.getNewAttribute();

        object.setObject(machine.getId());
        object.setType("machine");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.machine.update.attributes");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getMachineAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }


    @Override
    public String getConverterKey() {
        return "plm.mes.machine";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            MESMachine machine = mesMachineRepository.findOne(object.getObject());
            String machineType = "";
            if (machine == null) return "";

            if (machine.getObjectType().equals(ObjectType.valueOf(MESEnumObject.MACHINE.toString()))) {
                machineType = "machine";
            }

            String activity = as.getActivity();
            switch (activity) {
                case "plm.mes.machine.create":
                    convertedString = getMachineCreatedString(messageString, actor, machine);
                    break;
                case "plm.mes.machine.update.basicinfo":
                    convertedString = getMachineBasicInfoUpdatedString(messageString, actor, machine, machineType, as);
                    break;
                case "plm.mes.machine.update.attributes":
                    convertedString = getMachineAttributeUpdatedString(messageString, actor, machine, as);
                    break;
                case "plm.mes.machine.files.add":
                    convertedString = getPlantFilesAddedString(messageString, machine, as);
                    break;
                case "plm.mes.machine.files.delete":
                    convertedString = getPlantFileDeletedString(messageString, machine, as);
                    break;
                case "plm.mes.machine.files.folders.add":
                    convertedString = getPlantFoldersAddedString(messageString, machine, as);
                    break;
                case "plm.mes.machine.files.folders.delete":
                    convertedString = getPlantFoldersDeletedString(messageString, machine, as);
                    break;
                case "plm.mes.machine.files.version":
                    convertedString = getPlantFilesVersionedString(messageString, machine, as);
                    break;
                case "plm.mes.machine.files.rename":
                    convertedString = getPlantFileRenamedString(messageString, machine, as);
                    break;
                case "plm.mes.machine.files.replace":
                    convertedString = getPlantFileRenamedString(messageString, machine, as);
                    break;
                case "plm.mes.machine.files.lock":
                    convertedString = getPlantFileString(messageString, machine, as);
                    break;
                case "plm.mes.machine.files.unlock":
                    convertedString = getPlantFileString(messageString, machine, as);
                    break;
                case "plm.mes.machine.files.download":
                    convertedString = getPlantFileString(messageString, machine, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getMachineCreatedString(String messageString, Person actor, MESMachine machine) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), machine.getNumber());
    }


    private String getMachineBasicInfoUpdatedString(String messageString, Person actor, MESMachine machine, String type, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), type, machine.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mes.machine.update.basicinfo.property");

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

    private String getMachineBasicInfoUpdatedJson(MESMachine oldMachine, MESMachine newMachine) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldMachine.getName();
        String newValue = newMachine.getName();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }


        oldValue = oldMachine.getDescription();
        newValue = newMachine.getDescription();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue, newValue));
        }

        oldValue = oldMachine.getActive().toString();
        newValue = newMachine.getActive().toString();

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

        oldValue = oldMachine.getRequiresMaintenance().toString();
        newValue = newMachine.getRequiresMaintenance().toString();

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

        MESManufacturerData oldMESMfr = oldMachine.getManufacturerData();
        MESManufacturerData newMesMfr = newMachine.getManufacturerData();
        ASPropertyChangeDTO data = MESMfrBasicInfoUpdated(oldMESMfr, newMesMfr);
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


    private String getMachineAttributesUpdatedJson(MESObjectAttribute oldAttribute, MESObjectAttribute newAttribute) {
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

    private String getMachineAttributeUpdatedString(String messageString, Person actor, MESMachine machine, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), machine.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mes.machine.update.attributes.attribute");

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

    private String getPlantFilesAddedString(String messageString, MESMachine machine, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), machine.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mes.machine.files.add.file");

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

    private String getPlantFoldersAddedString(String messageString, MESMachine machine, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), machine.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getPlantFoldersDeletedString(String messageString, MESMachine machine, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), machine.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getPlantFileDeletedString(String messageString, MESMachine machine, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), machine.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getPlantFilesVersionedJson(List<PLMFile> files) {
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

    private String getPlantFilesVersionedString(String messageString, MESMachine machine, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), machine.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mes.machine.files.version.file");

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

    private String getPlantFileRenamedString(String messageString, MESMachine machine, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    machine.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getPlantFileString(String messageString, MESMachine machine, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    machine.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }



public ASPropertyChangeDTO MESMfrBasicInfoUpdated(MESManufacturerData oldMfr,MESManufacturerData newMfr){

    String oldMfrNameValue = oldMfr.getMfrName();
    String newMfrNameValue = newMfr.getMfrName();
    if (oldMfrNameValue == null) {
        oldMfrNameValue = "";
    }
    if (newMfrNameValue == null) {
        newMfrNameValue = "";
    }
    if (!newMfrNameValue.equals(oldMfrNameValue)) {
       return  new ASPropertyChangeDTO("MFR. Name", oldMfrNameValue, newMfrNameValue);
    }


    String oldMfrDescriptionValue = oldMfr.getMfrDescription();
    String newMfrDescriptionValue = newMfr.getMfrDescription();
    if (oldMfrDescriptionValue == null) {
        oldMfrDescriptionValue = "";
    }
    if (newMfrDescriptionValue == null) {
        newMfrDescriptionValue = "";
    }
    if (!newMfrDescriptionValue.equals(oldMfrDescriptionValue)) {
        return new ASPropertyChangeDTO("MFR. Description", oldMfrDescriptionValue, newMfrDescriptionValue);
    }


    String oldMfrModelNumberValue = oldMfr.getMfrModelNumber();
    String newMfrModelNumberValue = newMfr.getMfrModelNumber();
    if (oldMfrModelNumberValue == null) {
        oldMfrModelNumberValue = "";
    }
    if (newMfrModelNumberValue == null) {
        newMfrModelNumberValue = "";
    }
    if (!newMfrModelNumberValue.equals(oldMfrModelNumberValue)) {
        return new ASPropertyChangeDTO("MFR. Model Number", oldMfrModelNumberValue, newMfrModelNumberValue);
    }


    String oldMfrPartNumberValue = oldMfr.getMfrPartNumber();
    String newMfrPartNumberValue = newMfr.getMfrPartNumber();
    if (oldMfrPartNumberValue == null) {
        oldMfrPartNumberValue = "";
    }
    if (newMfrPartNumberValue == null) {
        newMfrPartNumberValue = "";
    }
    if (!newMfrPartNumberValue.equals(oldMfrPartNumberValue)) {
        return new ASPropertyChangeDTO("MFR. Part Number", oldMfrPartNumberValue, newMfrPartNumberValue);
    }

    String oldMfrSerialNumberValue = oldMfr.getMfrSerialNumber();
    String newMfrSerialNumberValue = newMfr.getMfrSerialNumber();
    if (oldMfrSerialNumberValue == null) {
        oldMfrSerialNumberValue = "";
    }
    if (newMfrSerialNumberValue == null) {
        newMfrSerialNumberValue = "";
    }
    if (!newMfrSerialNumberValue.equals(oldMfrSerialNumberValue)) {
        return new ASPropertyChangeDTO("MFR. Serial Number", oldMfrSerialNumberValue, newMfrSerialNumberValue);
    }



    String oldMfrLotNumberValue = oldMfr.getMfrLotNumber();
    String newMfrLotNumberValue = newMfr.getMfrLotNumber();
    if (oldMfrLotNumberValue == null) {
        oldMfrLotNumberValue = "";
    }
    if (newMfrLotNumberValue == null) {
        newMfrLotNumberValue = "";
    }
    if (!newMfrLotNumberValue.equals(oldMfrLotNumberValue)) {
        return new ASPropertyChangeDTO("MFR. Lot Number", oldMfrLotNumberValue, newMfrLotNumberValue);
    }
    

    Date oldMfrDateValue = oldMfr.getMfrDate();
    Date newMfrDateValue = newMfr.getMfrDate();
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    if (oldMfrDateValue == null && newMfrDateValue != null) {
        String newDateValue = dateFormat.format(newMfrDateValue);
        return new ASPropertyChangeDTO("MFR. Date", "", newDateValue);
    } else if (oldMfrDateValue != null && newMfrDateValue != null) {
        if (!oldMfrDateValue.equals(newMfrDateValue)) {
            String oldDateValue = dateFormat.format(oldMfrDateValue);
            String newDateValue = dateFormat.format(newMfrDateValue);
            return new ASPropertyChangeDTO("MFR. Date", oldDateValue, newDateValue);
        }
    } else if (oldMfrDateValue != null) {
        String oldDateValue = dateFormat.format(oldMfrDateValue);
        return new ASPropertyChangeDTO("MFR. Date", oldDateValue, "");
    }
    

    return null;
}
}