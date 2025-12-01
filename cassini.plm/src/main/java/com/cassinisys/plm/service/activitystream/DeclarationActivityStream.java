package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.MeasurementUnit;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.core.MeasurementUnitRepository;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.DeclarationEvents;
import com.cassinisys.plm.model.pgc.*;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.repo.pgc.PGCDeclarationRepository;
import com.cassinisys.plm.repo.pgc.PGCObjectTypeAttributeRepository;
import com.cassinisys.plm.repo.pgc.PGCSubstanceRepository;
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
public class DeclarationActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PGCObjectTypeAttributeRepository pgcTypeAttributeRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PGCDeclarationRepository declarationRepository;
    @Autowired
    private PGCSubstanceRepository pgcSubstanceRepository;
    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;

    @Async
    @EventListener
    public void declarationCreated(DeclarationEvents.DeclarationCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDeclaration().getId());
        object.setType("declaration");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pgc.pgcdeclaration.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void declarationBasicInfoUpdated(DeclarationEvents.DeclarationBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PGCDeclaration oldPlan = event.getOldDeclaration();
        PGCDeclaration newPlan = event.getDeclaration();

        object.setObject(newPlan.getId());
        object.setType("declaration");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pgc.pgcdeclaration.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getDeclarationBasicInfoUpdatedJson(oldPlan, newPlan));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void declarationAttributesUpdated(DeclarationEvents.DeclarationAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PGCObject declaration = event.getDeclaration();

        PGCObjectAttribute oldAtt = event.getOldAttribute();
        PGCObjectAttribute newAtt = event.getNewAttribute();

        object.setObject(declaration.getId());
        object.setType("declaration");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pgc.pgcdeclaration.update.attributes");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getDeclarationAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void declarationCommentAdded(DeclarationEvents.DeclarationCommentAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pgc.pgcdeclaration.comment");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDeclaration().getId());
        object.setType("declaration");
        as.setObject(object);
        as.setData(event.getComment().getComment());

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void declarationPartAddedEvent(DeclarationEvents.DeclarationPartAddEvent event) {

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pgc.pgcdeclaration.part.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDeclaration().getId());
        object.setType("declaration");
        as.setObject(object);
        as.setData(getPartAddedJson(event.getParts(), event.getDeclaration()));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void declarationPartDeleted(DeclarationEvents.DeclarationPartDeletedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pgc.pgcdeclaration.part.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDeclaration().getId());
        object.setType("declaration");
        as.setObject(object);
        ASMfrPartDelete asMfrPartDelete = new ASMfrPartDelete(event.getDeclaration().getName(),
                event.getDeclarationPart().getPart().getPartNumber());
        try {
            as.setData(objectMapper.writeValueAsString(asMfrPartDelete));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void declarationSpecificationAddedEvent(DeclarationEvents.DeclarationSpecificationAddEvent event) {

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pgc.pgcdeclaration.specifications.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDeclaration());
        object.setType("declaration");
        as.setObject(object);
        as.setData(getSpecificationAddedJson(event.getDeclarationSpecifications(), event.getDeclaration()));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void declarationSpecificationDeleted(DeclarationEvents.DeclarationSpecificationDeletedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pgc.pgcdeclaration.specifications.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDeclaration());
        object.setType("declaration");
        as.setObject(object);
        ASDeclarationSpecification declarationSpecification = new ASDeclarationSpecification(event.getDeclarationSpecification().getSpecification().getName(), event.getDeclarationSpecification().getSpecification().getNumber());
        try {
            as.setData(objectMapper.writeValueAsString(declarationSpecification));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void declarationPartSubstanceAddedEvent(DeclarationEvents.DeclarationPartSubstanceAddEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pgc.pgcdeclaration.part.substance.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDeclaration());
        object.setType("declaration");
        as.setObject(object);
        List<ASSubstance> asSubstances = new ArrayList<>();
        event.getBosItems().forEach(bosItem -> {
            PGCSubstance substance = pgcSubstanceRepository.findOne(bosItem.getSubstance());
            MeasurementUnit measurementUnit = measurementUnitRepository.findOne(bosItem.getUom());
            asSubstances.add(new ASSubstance(event.getDeclarationPart().getPart().getPartNumber(),
                    substance.getCasNumber(), substance.getName(), bosItem.getMass(), measurementUnit.getName()));
        });
        try {
            as.setData(objectMapper.writeValueAsString(asSubstances));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void declarationPartSubstanceDeleted(DeclarationEvents.DeclarationPartSubstanceDeletedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pgc.pgcdeclaration.part.substance.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDeclaration());
        object.setType("declaration");
        as.setObject(object);
        PGCSubstance substance = pgcSubstanceRepository.findOne(event.getBosItem().getSubstance());
        MeasurementUnit measurementUnit = measurementUnitRepository.findOne(event.getBosItem().getUom());
        ASSubstance asSubstance = new ASSubstance(event.getDeclarationPart().getPart().getPartNumber(),
                substance.getCasNumber(), substance.getName(), event.getBosItem().getMass(), measurementUnit.getName());
        try {
            as.setData(objectMapper.writeValueAsString(asSubstance));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void declarationPartSubstanceUpdated(DeclarationEvents.DeclarationPartSubstanceUpdatedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.pgc.pgcdeclaration.part.substance.update");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getDeclaration());
        object.setType("declaration");
        as.setObject(object);

        String data = getPartSubstanceUpdatedJson(event.getDeclarationPart(), event.getOldBosItem(), event.getBosItem());
        if (data != null) {
            as.setData(data);
            activityStreamService.create(as);
        }
    }

    @Override
    public String getConverterKey() {
        return "plm.pgc.pgcdeclaration";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            PGCDeclaration declaration = declarationRepository.findOne(object.getObject());

            if (declaration == null) return "";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.pgc.pgcdeclaration.create":
                    convertedString = getDeclarationCreatedString(messageString, actor, declaration);
                    break;
                case "plm.pgc.pgcdeclaration.update.basicinfo":
                    convertedString = getDeclarationBasicInfoUpdatedString(messageString, actor, declaration, as);
                    break;
                case "plm.pgc.pgcdeclaration.update.attributes":
                    convertedString = getDeclarationAttributeUpdatedString(messageString, actor, declaration, as);
                    break;
                case "plm.pgc.pgcdeclaration.files.add":
                    convertedString = getDeclarationFilesAddedString(messageString, declaration, as);
                    break;
                case "plm.pgc.pgcdeclaration.files.delete":
                    convertedString = getDeclarationFileDeletedString(messageString, declaration, as);
                    break;
                case "plm.pgc.pgcdeclaration.files.folders.add":
                    convertedString = getDeclarationFoldersAddedString(messageString, declaration, as);
                    break;
                case "plm.pgc.pgcdeclaration.files.folders.delete":
                    convertedString = getDeclarationFoldersDeletedString(messageString, declaration, as);
                    break;
                case "plm.pgc.pgcdeclaration.files.version":
                    convertedString = getDeclarationFilesVersionedString(messageString, declaration, as);
                    break;
                case "plm.pgc.pgcdeclaration.files.rename":
                    convertedString = getDeclarationFileRenamedString(messageString, declaration, as);
                    break;
                case "plm.pgc.pgcdeclaration.files.replace":
                    convertedString = getDeclarationFileRenamedString(messageString, declaration, as);
                    break;
                case "plm.pgc.pgcdeclaration.files.lock":
                    convertedString = getDeclarationFileString(messageString, declaration, as);
                    break;
                case "plm.pgc.pgcdeclaration.files.unlock":
                    convertedString = getDeclarationFileString(messageString, declaration, as);
                    break;
                case "plm.pgc.pgcdeclaration.files.download":
                    convertedString = getDeclarationFileString(messageString, declaration, as);
                    break;
                case "plm.pgc.pgcdeclaration.comment":
                    convertedString = getDeclarationCommentString(messageString, declaration, as);
                    break;
                case "plm.pgc.pgcdeclaration.part.add":
                    convertedString = getPartAddedString(messageString, declaration, as);
                    break;
                case "plm.pgc.pgcdeclaration.part.delete":
                    convertedString = getPartDeletedString(messageString, declaration, as);
                    break;
                case "plm.pgc.pgcdeclaration.specifications.add":
                    convertedString = getSpecificationAddedString(messageString, declaration, as);
                    break;
                case "plm.pgc.pgcdeclaration.specifications.delete":
                    convertedString = getSpecificationDeletedString(messageString, declaration, as);
                    break;
                case "plm.pgc.pgcdeclaration.part.substance.add":
                    convertedString = getPartSubstanceAddedString(messageString, actor, declaration, as);
                    break;
                case "plm.pgc.pgcdeclaration.part.substance.update":
                    convertedString = getPartSubstanceUpdatedString(messageString, actor, declaration, as);
                    break;
                case "plm.pgc.pgcdeclaration.part.substance.delete":
                    convertedString = getPartSubstanceDeletedString(messageString, actor, declaration, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getDeclarationCreatedString(String messageString, Person actor, PGCDeclaration declaration) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), declaration.getNumber());
    }

    private String getDeclarationRevisionCreatedString(String messageString, Person actor, PGCDeclaration declaration) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), declaration.getNumber());
    }

    private String getDeclarationBasicInfoUpdatedString(String messageString, Person actor, PGCDeclaration declaration, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), declaration.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.pgc.pgcdeclaration.update.basicinfo.property");

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

    private String getDeclarationBasicInfoUpdatedJson(PGCDeclaration oldSubstance, PGCDeclaration declaration) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldSubstance.getName();
        String newValue = declaration.getName();
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
        newValue = declaration.getDescription();
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


    private String getDeclarationAttributesUpdatedJson(PGCObjectAttribute oldAttribute, PGCObjectAttribute newAttribute) {
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

    private String getDeclarationAttributeUpdatedString(String messageString, Person actor, PGCDeclaration declaration, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), declaration.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.pgc.pgcdeclaration.update.attributes.attribute");

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

    private String getDeclarationFilesAddedJson(List<PLMFile> files) {
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

    private String getDeclarationFoldersAddedJson(PLMFile file) {
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

    private String getDeclarationFoldersDeletedJson(PLMFile file) {
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

    private String getDeclarationFilesAddedString(String messageString, PGCDeclaration declaration, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), declaration.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.pgc.pgcdeclaration.files.add.file");

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

    private String getDeclarationFoldersAddedString(String messageString, PGCDeclaration declaration, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), declaration.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getDeclarationFoldersDeletedString(String messageString, PGCDeclaration declaration, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), declaration.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getDeclarationFileDeletedString(String messageString, PGCDeclaration declaration, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), declaration.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getDeclarationFilesVersionedJson(List<PLMFile> files) {
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

    private String getDeclarationFilesVersionedString(String messageString, PGCDeclaration declaration, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), declaration.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.pgc.pgcdeclaration.files.version.file");

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

    private String getDeclarationFileRenamedString(String messageString, PGCDeclaration declaration, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    declaration.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getDeclarationFileString(String messageString, PGCDeclaration declaration, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    declaration.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }


    private String getDeclarationCommentString(String messageString, PGCDeclaration declaration, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                declaration.getNumber());

        String s = highlightValue("Comment: ");
        s += as.getData();
        s = addMarginToMessage(s);

        message += s;

        return message;
    }

    private String getPartAddedJson(List<PGCDeclarationPart> parts, PGCDeclaration declaration) {
        List<ASNewMfrPartDTO> asNewMfrPartDTOs = new ArrayList<>();
        parts.forEach(part -> {
            ASNewMfrPartDTO asNewMfrPartDTO = new ASNewMfrPartDTO(part.getId(), part.getPart().getPartNumber(), part.getPart().getPartName(), declaration.getName());
            asNewMfrPartDTOs.add(asNewMfrPartDTO);
        });
        String json = null;
        try {
            json = objectMapper.writeValueAsString(asNewMfrPartDTOs);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getSpecificationAddedJson(List<PGCDeclarationSpecification> declarationSpecifications, Integer declaration) {
        List<ASDeclarationSpecification> asDeclarationSpecifications = new ArrayList<>();
        declarationSpecifications.forEach(declarationSpecification -> {
            ASDeclarationSpecification asDeclarationSpecification = new ASDeclarationSpecification(declarationSpecification.getSpecification().getName(), declarationSpecification.getSpecification().getNumber());
            asDeclarationSpecifications.add(asDeclarationSpecification);
        });
        String json = null;
        try {
            json = objectMapper.writeValueAsString(asDeclarationSpecifications);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getPartAddedString(String messageString, PGCDeclaration mfr, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), mfr.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.pgc.pgcdeclaration.part.add.part");

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

    private String getSpecificationAddedString(String messageString, PGCDeclaration mfr, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), mfr.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.pgc.pgcdeclaration.specifications.add.specification");

        String json = as.getData();
        try {
            List<ASDeclarationSpecification> declarationSpecifications = objectMapper.readValue(json, new TypeReference<List<ASDeclarationSpecification>>() {
            });
            declarationSpecifications.forEach(f -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(f.getNumber()), highlightValue(f.getName())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }


    private String getPartDeletedString(String messageString, PGCDeclaration declaration, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            ASMfrPartDelete partDelete = objectMapper.readValue(json, new TypeReference<ASMfrPartDelete>() {
            });
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(partDelete.getPartNumber()), highlightValue(declaration.getName()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getSpecificationDeletedString(String messageString, PGCDeclaration declaration, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            ASDeclarationSpecification declarationSpecification = objectMapper.readValue(json, new TypeReference<ASDeclarationSpecification>() {
            });
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(declarationSpecification.getNumber() + " - " + declarationSpecification.getName()), highlightValue(declaration.getName()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getPartSubstanceUpdatedJson(PGCDeclarationPart declarationPart, PGCBosItem oldBosItem, PGCBosItem bosItem) {
        List<ASSubstancePropertyChangeDTO> changes = new ArrayList<>();
        PGCSubstance substance = pgcSubstanceRepository.findOne(oldBosItem.getSubstance());
        String oldValue = oldBosItem.getMass().toString();
        String newValue = bosItem.getMass().toString();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASSubstancePropertyChangeDTO("Mass", substance.getName(), declarationPart.getPart().getPartNumber(), oldValue, newValue));
        }

        Integer oldUom = oldBosItem.getUom();
        Integer uom = bosItem.getUom();
        if (oldUom == null) {
            oldUom = 0;
        }
        if (uom == null) {
            uom = 0;
        }
        if (!oldUom.equals(uom)) {
            MeasurementUnit oldUnit = measurementUnitRepository.findOne(oldUom);
            MeasurementUnit unit = measurementUnitRepository.findOne(uom);
            changes.add(new ASSubstancePropertyChangeDTO("Measurement Unit", substance.getName(), declarationPart.getPart().getPartNumber(), oldUnit.getName(), unit.getName()));
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


    private String getPartSubstanceAddedString(String messageString, Person actor, PGCDeclaration declaration, ActivityStream as) {
        String activityString = null;

        StringBuffer sb = new StringBuffer();

        String updateString = activityStreamResourceBundle.getString("plm.pgc.pgcdeclaration.part.substance.add.property");

        String json = as.getData();
        try {
            List<ASSubstance> propChanges = objectMapper.readValue(json, new TypeReference<List<ASSubstance>>() {
            });
            activityString = MessageFormat.format(messageString, actor.getFullName().trim(), propChanges.get(0).getPartNumber(), declaration.getNumber());
            sb.append(activityString);
            propChanges.forEach(p -> {
                String s = addMarginToMessage(MessageFormat.format(updateString,
                        highlightValue(p.getCasNumber()),
                        highlightValue(p.getName()),
                        highlightValue(p.getMass().toString()),
                        highlightValue(p.getUnitName())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getPartSubstanceUpdatedString(String messageString, Person actor, PGCDeclaration declaration, ActivityStream as) {
        String activityString = null;

        StringBuffer sb = new StringBuffer();

        String updateString = activityStreamResourceBundle.getString("plm.pgc.pgcdeclaration.part.substance.update.property");

        String json = as.getData();
        try {
            List<ASSubstancePropertyChangeDTO> propChanges = objectMapper.readValue(json, new TypeReference<List<ASSubstancePropertyChangeDTO>>() {
            });
            activityString = MessageFormat.format(messageString, actor.getFullName().trim(), propChanges.get(0).getName(), propChanges.get(0).getPartNumber(), declaration.getNumber());
            sb.append(activityString);
            propChanges.forEach(change -> {
                String s = addMarginToMessage(MessageFormat.format(updateString,
                        highlightValue(change.getProperty()),
                        highlightValue(change.getOldValue()),
                        highlightValue(change.getNewValue())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getPartSubstanceDeletedString(String messageString, Person actor, PGCDeclaration declaration, ActivityStream as) {
        String activityString = null;
        String json = as.getData();
        try {
            ASSubstance propChanges = objectMapper.readValue(json, new TypeReference<ASSubstance>() {
            });
            activityString = MessageFormat.format(messageString, actor.getFullName().trim(), propChanges.getName() + " - " + propChanges.getCasNumber(), propChanges.getPartNumber(), declaration.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return activityString;
    }

}
