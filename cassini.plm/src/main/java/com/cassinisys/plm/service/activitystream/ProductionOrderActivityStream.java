package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.PlantEvents;
import com.cassinisys.plm.event.ProductionOrderEvents;
import com.cassinisys.plm.model.mes.MESEnumObject;
import com.cassinisys.plm.model.mes.MESObjectAttribute;
import com.cassinisys.plm.model.mes.MESObjectTypeAttribute;
import com.cassinisys.plm.model.mes.MESPlant;
import com.cassinisys.plm.model.mes.MESProductionOrder;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.repo.mes.MESObjectRepository;
import com.cassinisys.plm.repo.mes.MESObjectTypeAttributeRepository;
import com.cassinisys.plm.repo.mes.MESPlantRepository;
import com.cassinisys.plm.repo.mes.ProductionOrderRepository;
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
public class ProductionOrderActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductionOrderRepository mesProductionOrderRepo;
    @Autowired
    private MESPlantRepository plantRepo;

    @Async
    @EventListener
    public void ProductionOrderCreated(ProductionOrderEvents.ProductionOrderCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProductionOrder().getId());
        object.setType("productionorder");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.productionorder.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void ProductionOrderBasicInfoUpdated(ProductionOrderEvents.ProductionOrderBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        MESProductionOrder oldPlan = event.getOldProductionOrder();
        MESProductionOrder newPlan = event.getProductionOrder();

        object.setObject(newPlan.getId());
        object.setType("productionorder");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.productionorder.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getProductionOrderBasicInfoUpdatedJson(oldPlan, newPlan));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void productionOrderFilesAdded(ProductionOrderEvents.ProductionOrderFilesAddedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.productionorder.files.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProductionOrder());
        object.setType("productionorder");
        as.setObject(object);
        as.setData(getProductionOrderFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void productionOrderFoldersAdded(ProductionOrderEvents.ProductionOrderFoldersAddedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.productionorder.files.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProductionOrder());
        object.setType("productionorder");
        as.setObject(object);
        as.setData(getproductionOrderFoldersAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void productionOrderFoldersDeleted(ProductionOrderEvents.ProductionOrderFoldersDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.productionorder.files.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProductionOrder());
        object.setType("productionorder");
        as.setObject(object);
        as.setData(getproductionOrderFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void productionOrderFileDeleted(ProductionOrderEvents.ProductionOrderFileDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.productionorder.files.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProductionOrder());
        object.setType("productionorder");
        as.setObject(object);
        as.setData(getproductionOrderFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void productionOrderFilesVersioned(ProductionOrderEvents.ProductionOrderFilesVersionedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.productionorder.files.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProductionOrder());
        object.setType("productionorder");
        as.setObject(object);
        as.setData(getproductionOrderFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void productionOrderFileRenamed(ProductionOrderEvents.ProductionOrderFileRenamedEvent event) {
        PLMFile oldFile = event.getOldFile();
        PLMFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals("Rename")) as.setActivity("plm.mes.productionOrder.files.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.mes.productionOrder.files.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProductionOrder());
        object.setType("productionorder");
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
    public void productionOrderFileLocked(ProductionOrderEvents.ProductionOrderFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.productionorder.files.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProductionOrder());
        object.setType("productionorder");
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
    public void productionOrderFileUnlocked(ProductionOrderEvents.ProductionOrderFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.productionorder.files.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProductionOrder());
        object.setType("productionorder");
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
    public void productionOrderFileDownloaded(ProductionOrderEvents.ProductionOrderFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.productionorder.files.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProductionOrder());
        object.setType("productionorder");
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
    public void productionOrderCommentAdded(ProductionOrderEvents.ProductionOrderCommentAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.productionorder.comment");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProductionOrder().getId());
        object.setType("productionorder");
        as.setObject(object);
        as.setData(event.getComment().getComment());

        activityStreamService.create(as);
    }


    @Override
    public String getConverterKey() {
        return "plm.mes.productionorder";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            MESProductionOrder productionOrder = mesProductionOrderRepo.findOne(object.getObject());
            String productionOrderType = "";
            if (productionOrder == null) return "";

            if (productionOrder.getObjectType().equals(ObjectType.valueOf(MESEnumObject.PRODUCTIONORDER.toString()))) {
                productionOrderType = "productionorder";
            }

            String activity = as.getActivity();
            switch (activity) {
                case "plm.mes.productionorder.create":
                    convertedString = getProductionOrderCreatedString(messageString, actor, productionOrder);
                    break;
                case "plm.mes.productionorder.update.basicinfo":
                    convertedString = getProductionOrderBasicInfoUpdatedString(messageString, actor, productionOrder, productionOrderType, as);
                    break;
                case "plm.mes.productionorder.files.add":
                    convertedString = getproductionOrderFilesAddedString(messageString, productionOrder, as);
                    break;
                case "plm.mes.productionorder.files.delete":
                    convertedString = getproductionOrderFileDeletedString(messageString, productionOrder, as);
                    break;
                case "plm.mes.productionorder.files.folders.add":
                    convertedString = getproductionOrderFoldersAddedString(messageString, productionOrder, as);
                    break;
                case "plm.mes.productionorder.files.folders.delete":
                    convertedString = getproductionOrderFoldersDeletedString(messageString, productionOrder, as);
                    break;
                case "plm.mes.productionorder.files.version":
                    convertedString = getproductionOrderFilesVersionedString(messageString, productionOrder, as);
                    break;
                case "plm.mes.productionorder.files.rename":
                    convertedString = getproductionOrderFileRenamedString(messageString, productionOrder, as);
                    break;
                case "plm.mes.productionorder.files.replace":
                    convertedString = getproductionOrderFileRenamedString(messageString, productionOrder, as);
                    break;
                case "plm.mes.productionorder.files.lock":
                    convertedString = getproductionOrderFileString(messageString, productionOrder, as);
                    break;
                case "plm.mes.productionorder.files.unlock":
                    convertedString = getproductionOrderFileString(messageString, productionOrder, as);
                    break;
                case "plm.mes.productionorder.files.download":
                    convertedString = getproductionOrderFileString(messageString, productionOrder, as);
                    break;
                case "plm.mes.productionorder.comment":
                    convertedString = getproductionOrderCommentString(messageString, productionOrder, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getProductionOrderCreatedString(String messageString, Person actor, MESProductionOrder productionOrder) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), productionOrder.getNumber());
    }


    private String getProductionOrderBasicInfoUpdatedString(String messageString, Person actor, MESProductionOrder productionOrder, String type, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), type, productionOrder.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mes.productionorder.update.basicinfo.property");

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

    private String getProductionOrderBasicInfoUpdatedJson(MESProductionOrder oldProductionOrder, MESProductionOrder newProductionOrder) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldProductionOrder.getName();
        String newValue = newProductionOrder.getName();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }
        oldValue = oldProductionOrder.getDescription();
        newValue = newProductionOrder.getDescription();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue, newValue));
        }

        Integer oldId = oldProductionOrder.getPlant();
        Integer newId = newProductionOrder.getPlant();

        oldValue = plantRepo.findOne(oldId).getName();
        newValue  = plantRepo.findOne(newId).getName();

        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("PlantName", oldValue, newValue));
        }

        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
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
    
    private String getProductionOrderFilesAddedJson(List<PLMFile> files) {
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

    private String getproductionOrderFoldersAddedJson(PLMFile file) {
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

    private String getproductionOrderFoldersDeletedJson(PLMFile file) {
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

    private String getproductionOrderFilesAddedString(String messageString, MESProductionOrder productionOrder, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), productionOrder.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mes.productionorder.files.add.file");

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

    private String getproductionOrderFoldersAddedString(String messageString, MESProductionOrder productionOrder, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), productionOrder.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getproductionOrderFoldersDeletedString(String messageString, MESProductionOrder productionOrder, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), productionOrder.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getproductionOrderFileDeletedString(String messageString, MESProductionOrder productionOrder, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), productionOrder.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getproductionOrderFilesVersionedJson(List<PLMFile> files) {
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

    private String getproductionOrderFilesVersionedString(String messageString, MESProductionOrder productionOrder, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), productionOrder.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mes.productionorder.files.version.file");

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

    private String getproductionOrderFileRenamedString(String messageString, MESProductionOrder productionOrder, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    productionOrder.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getproductionOrderFileString(String messageString, MESProductionOrder productionOrder, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    productionOrder.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getproductionOrderCommentString(String messageString, MESProductionOrder productionOrder, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
        productionOrder.getNumber());

        String s = highlightValue("Comment: ");
        s += as.getData();
        s = addMarginToMessage(s);

        message += s;

        return message;
    }

}
