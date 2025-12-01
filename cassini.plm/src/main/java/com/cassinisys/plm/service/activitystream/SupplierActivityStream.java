package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.SupplierEvents;
import com.cassinisys.plm.model.mfr.*;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.repo.mfr.SupplierRepository;
import com.cassinisys.plm.repo.mfr.SupplierTypeAttributeRepository;
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
public class SupplierActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private SupplierTypeAttributeRepository supplierTypeAttributeRepository;
    @Autowired
    private PersonRepository personRepository;

    @Async
    @EventListener
    public void suppliersCreated(SupplierEvents.SupplierCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSupplier().getId());
        object.setType("supplier");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.supplier.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void supplierBasicInfoUpdated(SupplierEvents.SupplierBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PLMSupplier oldSupplier = event.getOldSupplier();
        PLMSupplier newSupplier = event.getNewSupplier();

        object.setObject(newSupplier.getId());
        object.setType("supplier");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.supplier.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getSupplierBasicInfoUpdatedJson(oldSupplier, newSupplier));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void supplierPartAddedEvent(SupplierEvents.SupplierPartAddEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.supplier.parts.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSupplier().getId());
        object.setType("supplier");
        as.setObject(object);
        as.setData(getPartAddedJson(event.getParts(), event.getSupplier()));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void SupplierPartUpdated(SupplierEvents.SupplierPartUpdatedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.supplier.parts.update");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSupplier());
        object.setType("supplier");
        as.setObject(object);
        as.setData(getSupplierPartUpdatedJson(event.getOldSupplierPart(), event.getSupplierPart()));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void supplierPartDeleted(SupplierEvents.SupplierPartDeletedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.supplier.parts.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSupplier().getId());
        object.setType("supplier");
        as.setObject(object);
        ASMfrPartDelete asMfrPartDelete = new ASMfrPartDelete(event.getSupplier().getName(),
                event.getManufacturerPart().getPartNumber());
        try {
            as.setData(objectMapper.writeValueAsString(asMfrPartDelete));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void supplierContactAddedEvent(SupplierEvents.SupplierContactCreatedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.supplier.contact.create");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSupplier().getId());
        object.setType("supplier");
        as.setObject(object);
        as.setData(getContactAddedJson(event.getPerson(), event.getSupplier()));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void supplierContactDeleted(SupplierEvents.SupplierContactDeletedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.supplier.contact.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSupplier().getId());
        object.setType("supplier");
        as.setObject(object);
        ASContactDelete asMfrPartDelete = new ASContactDelete(event.getSupplier().getName(),
                event.getPerson().getFullName());
        try {
            as.setData(objectMapper.writeValueAsString(asMfrPartDelete));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    public String getSupplierPartUpdatedJson(PLMSupplierPart oldWorkOrderPart, PLMSupplierPart workOrderPart) {
        List<ASMROPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldWorkOrderPart.getPartNumber();
        String newValue = workOrderPart.getPartNumber();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASMROPropertyChangeDTO("Part Number", workOrderPart.getManufacturerPart().getPartName(),
                    oldValue, newValue));
        }

        String json = "";
        try {
            if (changes.size() > 0) {
                json = objectMapper.writeValueAsString(changes);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getSupplierPartUpdatedString(String messageString, Person actor, PLMSupplier workOrder,
            ActivityStream as) {
        String activityString = "";

        StringBuffer sb = new StringBuffer();

        String updateString = activityStreamResourceBundle.getString("plm.oem.supplier.parts.update.property");

        String json = as.getData();
        try {
            List<ASMROPropertyChangeDTO> propChanges = objectMapper.readValue(json,
                    new TypeReference<List<ASMROPropertyChangeDTO>>() {
                    });
            activityString = MessageFormat.format(messageString, actor.getFullName().trim(),
                    highlightValue(propChanges.get(0).getName()), highlightValue(workOrder.getName()));
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

    public String getContactAddedJson(Person person, PLMSupplier supplier) {
        ASContactToSupplier asNewMfrPartDTO = new ASContactToSupplier(supplier.getName(), person.getFullName());
        String json = "";
        try {
            json = objectMapper.writeValueAsString(asNewMfrPartDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getContactAddedString(String messageString, PLMSupplier supplier, ActivityStream as) {
        String activityString = "";
        String json = as.getData();
        try {
            ASContactToSupplier supplierContact = objectMapper.readValue(json,
                    new TypeReference<ASContactToSupplier>() {
                    });
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()),
                    highlightValue(supplier.getName()), highlightValue(supplierContact.getContact()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return activityString;
    }

    private String getContactDeletedString(String messageString, PLMSupplier manufacturer, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            ASContactDelete partDelete = objectMapper.readValue(json, new TypeReference<ASContactDelete>() {
            });
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()),
                    highlightValue(partDelete.getContact()), highlightValue(manufacturer.getName()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    public String getPartAddedJson(List<PLMManufacturerPart> parts, PLMSupplier supplier) {
        List<ASNewMfrPartDTO> asNewMfrPartDTOs = new ArrayList<>();
        for (PLMManufacturerPart part : parts) {
            ASNewMfrPartDTO asNewMfrPartDTO = new ASNewMfrPartDTO(part.getId(), part.getPartNumber(),
                    part.getPartName(), supplier.getName());
            asNewMfrPartDTOs.add(asNewMfrPartDTO);
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(asNewMfrPartDTOs);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getPartAddedString(String messageString, PLMSupplier mfr, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()),
                mfr.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.oem.supplier.parts.add");

        String json = as.getData();
        try {
            List<ASNewMfrPartDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewMfrPartDTO>>() {
            });
            files.forEach(f -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(f.getPartNumber()),
                        highlightValue(f.getPartName())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getPartDeletedString(String messageString, PLMSupplier manufacturer, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            ASMfrPartDelete partDelete = objectMapper.readValue(json, new TypeReference<ASMfrPartDelete>() {
            });
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()),
                    highlightValue(partDelete.getPartNumber()), highlightValue(manufacturer.getName()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    @Async
    @EventListener
    public void supplierAttributesUpdated(SupplierEvents.SupplierAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PLMSupplier supplier = event.getSupplier();

        PLMSupplierAttribute oldAtt = event.getOldAttribute();
        PLMSupplierAttribute newAtt = event.getNewAttribute();

        object.setObject(supplier.getId());
        object.setType("supplier");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.supplier.update.attributes");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getSupplierAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void supplierFilesAdded(SupplierEvents.SupplierFilesAddedEvent event) {
        List<PLMSupplierFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.supplier.files.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSupplier());
        object.setType("supplier");
        as.setObject(object);
        as.setData(getSupplierFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void supplierFoldersAdded(SupplierEvents.SupplierFoldersAddedEvent event) {
        PLMSupplierFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.supplier.files.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSupplier());
        object.setType("supplier");
        as.setObject(object);
        as.setData(getSupplierFoldersAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void supplierFoldersDeleted(SupplierEvents.SupplierFoldersDeletedEvent event) {
        PLMSupplierFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.supplier.files.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSupplier());
        object.setType("supplier");
        as.setObject(object);
        as.setData(getSupplierFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void supplierFileDeleted(SupplierEvents.SupplierFileDeletedEvent event) {
        PLMSupplierFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.supplier.files.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSupplier());
        object.setType("supplier");
        as.setObject(object);
        as.setData(getSupplierFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void supplierFilesVersioned(SupplierEvents.SupplierFilesVersionedEvent event) {
        List<PLMSupplierFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.supplier.files.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSupplier());
        object.setType("supplier");
        as.setObject(object);
        as.setData(getSupplierFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void supplierFileRenamed(SupplierEvents.SupplierFileRenamedEvent event) {
        PLMSupplierFile oldFile = event.getOldFile();
        PLMSupplierFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals("Rename"))
            as.setActivity("plm.oem.supplier.files.rename");
        if (event.getType().equals("Replace"))
            as.setActivity("plm.oem.supplier.files.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSupplier());
        object.setType("supplier");
        as.setObject(object);

        ASFileReplaceDto asFileReplaceDto = new ASFileReplaceDto(oldFile.getId(), oldFile.getName(), newFile.getId(),
                newFile.getName());
        try {
            as.setData(objectMapper.writeValueAsString(asFileReplaceDto));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void supplierFileLocked(SupplierEvents.SupplierFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.supplier.files.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSupplier());
        object.setType("supplier");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getFile().getId(), event.getFile().getName(),
                FileUtils.byteCountToDisplaySize(event.getFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void supplierFileUnlocked(SupplierEvents.SupplierFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.supplier.files.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSupplier());
        object.setType("supplier");
        as.setObject(object);
        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getFile().getId(), event.getFile().getName(),
                FileUtils.byteCountToDisplaySize(event.getFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void supplierFileDownloaded(SupplierEvents.SupplierFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.supplier.files.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSupplier());
        object.setType("supplier");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getFile().getId(), event.getFile().getName(),
                FileUtils.byteCountToDisplaySize(event.getFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void supplierCommentAdded(SupplierEvents.SupplierCommentAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.supplier.comment");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSupplier().getId());
        object.setType("supplier");
        as.setObject(object);
        as.setData(event.getComment().getComment());

        activityStreamService.create(as);
    }

    @Override
    public String getConverterKey() {
        return "plm.oem.supplier";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            PLMSupplier supplier = supplierRepository.findOne(object.getObject());
            String supplierType = "";
            if (supplier == null)
                return "";

            if (supplier.getObjectType().equals(ObjectType.valueOf(PLMObjectType.MFRSUPPLIER.toString()))) {
                supplierType = "supplier";
            }

            String activity = as.getActivity();
            switch (activity) {
                case "plm.oem.supplier.create":
                    convertedString = getSupplierCreatedString(messageString, actor, supplier);
                    break;
                case "plm.oem.supplier.update.basicinfo":
                    convertedString = getSupplierBasicInfoUpdatedString(messageString, actor, supplier, supplierType,
                            as);
                    break;
                case "plm.oem.supplier.update.attributes":
                    convertedString = getSupplierAttributeUpdatedString(messageString, actor, supplier, as);
                    break;
                case "plm.oem.supplier.files.add":
                    convertedString = getSupplierFilesAddedString(messageString, supplier, as);
                    break;
                case "plm.oem.supplier.files.delete":
                    convertedString = getSupplierFileDeletedString(messageString, supplier, as);
                    break;
                case "plm.oem.supplier.files.folders.add":
                    convertedString = getSupplierFoldersAddedString(messageString, supplier, as);
                    break;
                case "plm.oem.supplier.files.folders.delete":
                    convertedString = getSupplierFoldersDeletedString(messageString, supplier, as);
                    break;
                case "plm.oem.supplier.files.version":
                    convertedString = getSupplierFilesVersionedString(messageString, supplier, as);
                    break;
                case "plm.oem.supplier.files.rename":
                    convertedString = getSupplierFileRenamedString(messageString, supplier, as);
                    break;
                case "plm.oem.supplier.files.replace":
                    convertedString = getSupplierFileRenamedString(messageString, supplier, as);
                    break;
                case "plm.oem.supplier.files.lock":
                    convertedString = getSupplierFileString(messageString, supplier, as);
                    break;
                case "plm.oem.supplier.files.unlock":
                    convertedString = getSupplierFileString(messageString, supplier, as);
                    break;
                case "plm.oem.supplier.files.download":
                    convertedString = getSupplierFileString(messageString, supplier, as);
                    break;
                case "plm.oem.supplier.contact.create":
                    convertedString = getContactAddedString(messageString, supplier, as);
                    break;
                case "plm.oem.supplier.contact.delete":
                    convertedString = getContactDeletedString(messageString, supplier, as);
                    break;
                case "plm.oem.supplier.parts.add":
                    convertedString = getPartAddedString(messageString, supplier, as);
                    break;
                case "plm.oem.supplier.parts.update":
                    convertedString = getSupplierPartUpdatedString(messageString, actor, supplier, as);
                    break;
                case "plm.oem.supplier.parts.delete":
                    convertedString = getPartDeletedString(messageString, supplier, as);
                    break;
                case "plm.oem.supplier.comment":
                    convertedString = getSupplierCommentString(messageString, supplier, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getSupplierCreatedString(String messageString, Person actor, PLMSupplier supplier) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), supplier.getName());
    }

    private String getSupplierBasicInfoUpdatedString(String messageString, Person actor, PLMSupplier supplier,
            String type, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), type,
                supplier.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.oem.supplier.update.basicinfo.property");

        String json = as.getData();
        try {
            List<ASPropertyChangeDTO> propChanges = objectMapper.readValue(json,
                    new TypeReference<List<ASPropertyChangeDTO>>() {
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

    public String getSupplierBasicInfoUpdatedJson(PLMSupplier oldSuppliert, PLMSupplier newSuppliert) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldSuppliert.getName();
        String newValue = newSuppliert.getName();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        oldValue = oldSuppliert.getDescription();
        newValue = newSuppliert.getDescription();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue, newValue));
        }

        oldValue = oldSuppliert.getAddress();
        newValue = newSuppliert.getAddress();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Address", oldValue, newValue));
        }

        oldValue = oldSuppliert.getCity();
        newValue = newSuppliert.getCity();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("City", oldValue, newValue));
        }

        oldValue = oldSuppliert.getCountry();
        newValue = newSuppliert.getCountry();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Country", oldValue, newValue));
        }

        oldValue = oldSuppliert.getPostalCode();
        newValue = newSuppliert.getPostalCode();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Postal Code", oldValue, newValue));
        }

        oldValue = oldSuppliert.getPhoneNumber();
        newValue = newSuppliert.getPhoneNumber();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Phone Number", oldValue, newValue));
        }

        oldValue = oldSuppliert.getMobileNumber();
        newValue = newSuppliert.getMobileNumber();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Mobile Number", oldValue, newValue));
        }

        oldValue = oldSuppliert.getFaxNumber();
        newValue = newSuppliert.getFaxNumber();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Fax Number", oldValue, newValue));
        }

        oldValue = oldSuppliert.getEmail();
        newValue = newSuppliert.getEmail();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("E-mail", oldValue, newValue));
        }

        oldValue = oldSuppliert.getWebSite();
        newValue = newSuppliert.getWebSite();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("WebSite", oldValue, newValue));
        }

        String json = "";
        try {
            if (changes.size() > 0) {
                json = objectMapper.writeValueAsString(changes);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getSupplierBasicInfoUpdatedJson(PLMSupplierContact oldSuppliert, PLMSupplierContact newSuppliert) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();
        String newValue = "";
        String oldValue = "";
        Person newPerson = personRepository.findOne(newSuppliert.getContact());
        Person oldPerson = personRepository.findOne(oldSuppliert.getContact());
        newValue = newPerson.getFirstName();
        oldValue = oldPerson.getFirstName();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Contact Person", oldValue, newValue));
        }

        oldValue = oldSuppliert.getRole();
        newValue = newSuppliert.getRole();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Role", oldValue, newValue));
        }

        if (oldSuppliert.getActive() != newSuppliert.getActive()) {
            if (oldSuppliert.getActive() == true) {
                oldValue = "Active";
            } else {
                oldValue = "InActive";
            }
            if (newSuppliert.getActive() == true) {
                newValue = "Active";
            } else {
                newValue = "InActive";
            }
            changes.add(new ASPropertyChangeDTO("Active", oldValue, newValue));
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

    private String getSupplierAttributesUpdatedJson(PLMSupplierAttribute oldAttribute,
            PLMSupplierAttribute newAttribute) {
        List<ASAttributeChangeDTO> changes = new ArrayList<>();

        String oldValue = "";
        if (oldAttribute != null) {
            oldValue = oldAttribute.getValueAsString();
        }
        String newValue = newAttribute.getValueAsString();

        PLMSupplierTypeAttribute attDef = supplierTypeAttributeRepository
                .findOne(newAttribute.getId().getAttributeDef());
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

    private String getSupplierAttributeUpdatedString(String messageString, Person actor, PLMSupplier supplier,
            ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), supplier.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.oem.supplier.update.attributes.attribute");

        String json = as.getData();
        try {
            List<ASAttributeChangeDTO> propChanges = objectMapper.readValue(json,
                    new TypeReference<List<ASAttributeChangeDTO>>() {
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

    public String getSupplierFilesAddedJson(List<PLMSupplierFile> files) {
        String json = null;

        List<ASNewFileDTO> ASNewFileDtos = new ArrayList<>();
        files.forEach(f -> ASNewFileDtos
                .add(new ASNewFileDTO(f.getId(), f.getName(), FileUtils.byteCountToDisplaySize(f.getSize()))));

        try {
            json = objectMapper.writeValueAsString(ASNewFileDtos);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getSupplierFoldersAddedJson(PLMSupplierFile file) {
        String json = null;

        List<ASNewFileDTO> ASNewFileDtos = new ArrayList<>();
        ASNewFileDtos
                .add(new ASNewFileDTO(file.getId(), file.getName(), FileUtils.byteCountToDisplaySize(file.getSize())));
        try {
            json = objectMapper.writeValueAsString(ASNewFileDtos);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getSupplierFoldersDeletedJson(PLMSupplierFile file) {
        String json = null;

        List<ASNewFileDTO> ASNewFileDtos = new ArrayList<>();
        ASNewFileDtos
                .add(new ASNewFileDTO(file.getId(), file.getName(), FileUtils.byteCountToDisplaySize(file.getSize())));
        try {
            json = objectMapper.writeValueAsString(ASNewFileDtos);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getSupplierFilesAddedString(String messageString, PLMSupplier supplier, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()),
                supplier.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.oem.supplier.files.add.file");

        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            files.forEach(f -> {
                String s = addMarginToMessage(
                        MessageFormat.format(fileString, highlightValue(f.getName()), f.getSize()));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getSupplierFoldersAddedString(String messageString, PLMSupplier supplier, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()),
                    highlightValue(fileDTO.getName()), supplier.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getSupplierFoldersDeletedString(String messageString, PLMSupplier supplier, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()),
                    highlightValue(fileDTO.getName()), supplier.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getSupplierFileDeletedString(String messageString, PLMSupplier supplier, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()),
                    highlightValue(fileDTO.getName()), supplier.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getSupplierFilesVersionedJson(List<PLMSupplierFile> files) {
        String json = null;

        List<ASVersionedFileDTO> ASVersionedFileDtos = new ArrayList<>();
        files.forEach(f -> ASVersionedFileDtos
                .add(new ASVersionedFileDTO(f.getId(), f.getName(), f.getVersion() - 1, f.getVersion())));

        try {
            json = objectMapper.writeValueAsString(ASVersionedFileDtos);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getSupplierFilesVersionedString(String messageString, PLMSupplier supplier, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                supplier.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.oem.supplier.files.version.file");

        String json = as.getData();
        try {
            List<ASVersionedFileDTO> files = objectMapper.readValue(json,
                    new TypeReference<List<ASVersionedFileDTO>>() {
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

    private String getSupplierFileRenamedString(String messageString, PLMSupplier supplier, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(),
                    new TypeReference<ASFileReplaceDto>() {
                    });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    supplier.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getSupplierFileString(String messageString, PLMSupplier supplier, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    supplier.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getSupplierCommentString(String messageString, PLMSupplier supplier, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                supplier.getName());

        String s = highlightValue("Comment: ");
        s += as.getData();
        s = addMarginToMessage(s);

        message += s;

        return message;
    }

}
