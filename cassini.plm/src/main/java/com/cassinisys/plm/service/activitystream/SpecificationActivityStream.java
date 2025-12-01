package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.SpecificationEvents;
import com.cassinisys.plm.model.pgc.*;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.repo.pgc.PGCObjectTypeAttributeRepository;
import com.cassinisys.plm.repo.pgc.PGCSpecificationRepository;
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

/**
 * Created by GSR Cassini on 30-11-2020.
 */
@Component
public class SpecificationActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PGCObjectTypeAttributeRepository pgcTypeAttributeRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PGCSpecificationRepository specificationRepository;

    @Async
    @EventListener
    public void specificationCreated(SpecificationEvents.SpecificationCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSpecification().getId());
        object.setType("specification");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pgc.pgcspecification.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void specificationBasicInfoUpdated(SpecificationEvents.SpecificationBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PGCSpecification oldPlan = event.getOldSpecification();
        PGCSpecification newPlan = event.getSpecification();

        object.setObject(newPlan.getId());
        object.setType("specification");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pgc.pgcspecification.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getSpecificationBasicInfoUpdatedJson(oldPlan, newPlan));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void specificationAttributesUpdated(SpecificationEvents.SpecificationAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PGCObject specification = event.getSpecification();

        PGCObjectAttribute oldAtt = event.getOldAttribute();
        PGCObjectAttribute newAtt = event.getNewAttribute();

        object.setObject(specification.getId());
        object.setType("specification");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pgc.pgcspecification.update.attributes");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getSpecificationAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void specificationCommentAdded(SpecificationEvents.SpecificationCommentAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pgc.pgcspecification.comment");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSpecification().getId());
        object.setType("specification");
        as.setObject(object);
        as.setData(event.getComment().getComment());

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void specSubstancePartAddedEvent(SpecificationEvents.SubstanceAddEvent event) {
        PGCSubstance asNewMfrPartDTO = event.getSubstance();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pgc.pgcspecification.substance.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSpecification().getId());
        object.setType("specification");
        as.setObject(object);
        as.setData(getSubstanceAddedJson(asNewMfrPartDTO, event.getSpecification()));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void specSubstanceDeleted(SpecificationEvents.SubstanceDeletedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pgc.pgcspecification.substance.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSpecification().getId());
        object.setType("specification");
        as.setObject(object);
        ASSpecSubstanceDelete asMfrPartDelete = new ASSpecSubstanceDelete(event.getSpecification().getName(),
                event.getSubstance().getNumber());
        try {
            as.setData(objectMapper.writeValueAsString(asMfrPartDelete));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Override
    public String getConverterKey() {
        return "plm.pgc.pgcspecification";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            PGCSpecification specification = specificationRepository.findOne(object.getObject());

            if (specification == null) return "";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.pgc.pgcspecification.create":
                    convertedString = getSpecificationCreatedString(messageString, actor, specification);
                    break;
                case "plm.pgc.pgcspecification.update.basicinfo":
                    convertedString = getSpecificationBasicInfoUpdatedString(messageString, actor, specification, as);
                    break;
                case "plm.pgc.pgcspecification.update.attributes":
                    convertedString = getSpecificationAttributeUpdatedString(messageString, actor, specification, as);
                    break;
                case "plm.pgc.pgcspecification.files.add":
                    convertedString = getSpecificationFilesAddedString(messageString, specification, as);
                    break;
                case "plm.pgc.pgcspecification.files.delete":
                    convertedString = getSpecificationFileDeletedString(messageString, specification, as);
                    break;
                case "plm.pgc.pgcspecification.files.folders.add":
                    convertedString = getSpecificationFoldersAddedString(messageString, specification, as);
                    break;
                case "plm.pgc.pgcspecification.files.folders.delete":
                    convertedString = getSpecificationFoldersDeletedString(messageString, specification, as);
                    break;
                case "plm.pgc.pgcspecification.files.version":
                    convertedString = getSpecificationFilesVersionedString(messageString, specification, as);
                    break;
                case "plm.pgc.pgcspecification.files.rename":
                    convertedString = getSpecificationFileRenamedString(messageString, specification, as);
                    break;
                case "plm.pgc.pgcspecification.files.replace":
                    convertedString = getSpecificationFileRenamedString(messageString, specification, as);
                    break;
                case "plm.pgc.pgcspecification.files.lock":
                    convertedString = getSpecificationFileString(messageString, specification, as);
                    break;
                case "plm.pgc.pgcspecification.files.unlock":
                    convertedString = getSpecificationFileString(messageString, specification, as);
                    break;
                case "plm.pgc.pgcspecification.files.download":
                    convertedString = getSpecificationFileString(messageString, specification, as);
                    break;
                case "plm.pgc.pgcspecification.comment":
                    convertedString = getSpecificationCommentString(messageString, specification, as);
                    break;
                case "plm.pgc.pgcspecification.substance.add":
                    convertedString = getSubstanceAddedString(messageString, specification, as);
                    break;
                case "plm.pgc.pgcspecification.substance.delete":
                    convertedString = getSubstanceDeletedString(messageString, specification, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getSpecificationCreatedString(String messageString, Person actor, PGCSpecification specification) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), specification.getNumber());
    }

    private String getSpecificationRevisionCreatedString(String messageString, Person actor, PGCSpecification specification) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), specification.getNumber());
    }

    private String getSpecificationBasicInfoUpdatedString(String messageString, Person actor, PGCSpecification specification, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), specification.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.pgc.pgcspecification.update.basicinfo.property");

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

    private String getSpecificationBasicInfoUpdatedJson(PGCSpecification oldSubstance, PGCSpecification specification) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldSubstance.getName();
        String newValue = specification.getName();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        oldValue = oldSubstance.getDescription();
        newValue = specification.getDescription();
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


    private String getSpecificationAttributesUpdatedJson(PGCObjectAttribute oldAttribute, PGCObjectAttribute newAttribute) {
        List<ASAttributeChangeDTO> changes = new ArrayList<>();

        String oldValue = "";
        if (oldAttribute != null) {
            oldValue = oldAttribute.getValueAsString();
        }
        String newValue = newAttribute.getValueAsString();

        PGCObjectTypeAttribute attDef = pgcTypeAttributeRepository.findOne(newAttribute.getId().getAttributeDef());
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

    private String getSpecificationAttributeUpdatedString(String messageString, Person actor, PGCSpecification specification, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), specification.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.pgc.pgcspecification.update.attributes.attribute");

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

    private String getSpecificationFilesAddedJson(List<PLMFile> files) {
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

    private String getSpecificationFoldersAddedJson(PLMFile file) {
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

    private String getSpecificationFoldersDeletedJson(PLMFile file) {
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

    private String getSpecificationFilesAddedString(String messageString, PGCSpecification specification, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), specification.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.pgc.pgcspecification.files.add.file");

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

    private String getSpecificationFoldersAddedString(String messageString, PGCSpecification specification, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), specification.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getSpecificationFoldersDeletedString(String messageString, PGCSpecification specification, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), specification.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getSpecificationFileDeletedString(String messageString, PGCSpecification specification, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), specification.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getSpecificationFilesVersionedJson(List<PLMFile> files) {
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

    private String getSpecificationFilesVersionedString(String messageString, PGCSpecification specification, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), specification.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.pgc.pgcspecification.files.version.file");

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

    private String getSpecificationFileRenamedString(String messageString, PGCSpecification specification, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    specification.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getSpecificationFileString(String messageString, PGCSpecification specification, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    specification.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }


    private String getSpecificationCommentString(String messageString, PGCSpecification specification, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                specification.getNumber());

        String s = highlightValue("Comment: ");
        s += as.getData();
        s = addMarginToMessage(s);

        message += s;

        return message;
    }

    private String getSubstanceAddedJson(PGCSubstance substance, PGCSpecification specification) {
        List<ASNewMfrPartDTO> asNewMfrPartDTOs = new ArrayList<>();
        ASNewMfrPartDTO asNewMfrPartDTO = new ASNewMfrPartDTO(substance.getId(), substance.getNumber(), substance.getName(), specification.getName());
        asNewMfrPartDTOs.add(asNewMfrPartDTO);
        String json = null;
        try {
            json = objectMapper.writeValueAsString(asNewMfrPartDTOs);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getSubstanceAddedString(String messageString, PGCSpecification mfr, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), mfr.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.pgc.pgcspecification.substance.add.substance");

        String json = as.getData();
        try {
            List<ASNewMfrPartDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewMfrPartDTO>>() {
            });
            files.forEach(f -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(f.getPartNumber()), highlightValue(f.getPartName())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }


    private String getSubstanceDeletedString(String messageString, PGCSpecification manufacturer, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            ASMfrPartDelete partDelete = objectMapper.readValue(json, new TypeReference<ASMfrPartDelete>() {
            });
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(partDelete.getPartNumber()), highlightValue(manufacturer.getName()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

}
