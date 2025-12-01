package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.InstrumentEvents;
import com.cassinisys.plm.model.mes.*;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.repo.mes.MESInstrumentRepository;
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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 29-10-2020.
 */
@Component
public class InstrumentActivityStream extends BaseActivityStream implements ActivityStreamConverter {


    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MESInstrumentRepository mesInstrumentRepository;
    @Autowired
    private MESObjectTypeAttributeRepository mesObjectTypeAttributeRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private MachineActivityStream machineActivityStream;

    @Async
    @EventListener
    public void instrumentsCreated(InstrumentEvents.InstrumentCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getInstrument().getId());
        object.setType("instrument");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.instrument.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void instrumentBasicInfoUpdated(InstrumentEvents.InstrumentBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        MESInstrument oldInstrument = event.getOldInstrument();
        MESInstrument newInstrument = event.getNewInstrument();

        object.setObject(newInstrument.getId());
        object.setType("instrument");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.instrument.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getInstrumentBasicInfoUpdatedJson(oldInstrument, newInstrument));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void instrumentAttributesUpdated(InstrumentEvents.InstrumentAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        MESInstrument instrument = event.getInstrument();

        MESObjectAttribute oldAtt = event.getOldAttribute();
        MESObjectAttribute newAtt = event.getNewAttribute();

        object.setObject(instrument.getId());
        object.setType("instrument");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.instrument.update.attributes");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getInstrumentAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }


    @Override
    public String getConverterKey() {
        return "plm.mes.instrument";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            MESInstrument instrument = mesInstrumentRepository.findOne(object.getObject());
            String instrumentType = "";
            if (instrument == null) return "";

            if (instrument.getObjectType().equals(ObjectType.valueOf(MESEnumObject.INSTRUMENT.toString()))) {
                instrumentType = "instrument";
            }

            String activity = as.getActivity();
            switch (activity) {
                case "plm.mes.instrument.create":
                    convertedString = getInstrumentCreatedString(messageString, actor, instrument);
                    break;
                case "plm.mes.instrument.update.basicinfo":
                    convertedString = getInstrumentBasicInfoUpdatedString(messageString, actor, instrument, instrumentType, as);
                    break;
                case "plm.mes.instrument.update.attributes":
                    convertedString = getInstrumentAttributeUpdatedString(messageString, actor, instrument, as);
                    break;
                case "plm.mes.instrument.files.add":
                    convertedString = getPlantFilesAddedString(messageString, instrument, as);
                    break;
                case "plm.mes.instrument.files.delete":
                    convertedString = getPlantFileDeletedString(messageString, instrument, as);
                    break;
                case "plm.mes.instrument.files.folders.add":
                    convertedString = getPlantFoldersAddedString(messageString, instrument, as);
                    break;
                case "plm.mes.instrument.files.folders.delete":
                    convertedString = getPlantFoldersDeletedString(messageString, instrument, as);
                    break;
                case "plm.mes.instrument.files.version":
                    convertedString = getPlantFilesVersionedString(messageString, instrument, as);
                    break;
                case "plm.mes.instrument.files.rename":
                    convertedString = getPlantFileRenamedString(messageString, instrument, as);
                    break;
                case "plm.mes.instrument.files.replace":
                    convertedString = getPlantFileRenamedString(messageString, instrument, as);
                    break;
                case "plm.mes.instrument.files.lock":
                    convertedString = getPlantFileString(messageString, instrument, as);
                    break;
                case "plm.mes.instrument.files.unlock":
                    convertedString = getPlantFileString(messageString, instrument, as);
                    break;
                case "plm.mes.instrument.files.download":
                    convertedString = getPlantFileString(messageString, instrument, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getInstrumentCreatedString(String messageString, Person actor, MESInstrument instrument) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), instrument.getNumber());
    }


    private String getInstrumentBasicInfoUpdatedString(String messageString, Person actor, MESInstrument instrument, String type, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), type, instrument.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mes.instrument.update.basicinfo.property");

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

    private String getInstrumentBasicInfoUpdatedJson(MESInstrument oldInstrument, MESInstrument newInstrument) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldInstrument.getName();
        String newValue = newInstrument.getName();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        oldValue = oldInstrument.getDescription();
        newValue = newInstrument.getDescription();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue, newValue));
        }

        oldValue = oldInstrument.getActive().toString();
        newValue = newInstrument.getActive().toString();

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

        oldValue = oldInstrument.getRequiresMaintenance().toString();
        newValue = newInstrument.getRequiresMaintenance().toString();

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

        MESManufacturerData oldMESMfr = oldInstrument.getManufacturerData();
        MESManufacturerData newMesMfr = newInstrument.getManufacturerData();
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


    private String getInstrumentAttributesUpdatedJson(MESObjectAttribute oldAttribute, MESObjectAttribute newAttribute) {
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

    private String getInstrumentAttributeUpdatedString(String messageString, Person actor, MESInstrument instrument, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), instrument.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mes.instrument.update.attributes.attribute");

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

    private String getPlantFilesAddedString(String messageString, MESInstrument instrument, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), instrument.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mes.instrument.files.add.file");

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

    private String getPlantFoldersAddedString(String messageString, MESInstrument instrument, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), instrument.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getPlantFoldersDeletedString(String messageString, MESInstrument instrument, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), instrument.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getPlantFileDeletedString(String messageString, MESInstrument instrument, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), instrument.getNumber());
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

    private String getPlantFilesVersionedString(String messageString, MESInstrument instrument, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), instrument.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mes.instrument.files.version.file");

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

    private String getPlantFileRenamedString(String messageString, MESInstrument instrument, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    instrument.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getPlantFileString(String messageString, MESInstrument instrument, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    instrument.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }


}
