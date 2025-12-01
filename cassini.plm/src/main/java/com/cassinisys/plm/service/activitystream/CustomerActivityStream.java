package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.CustomerEvents;
import com.cassinisys.plm.model.pqm.PQMCustomer;
import com.cassinisys.plm.model.pqm.PQMCustomerFile;
import com.cassinisys.plm.repo.pqm.PQMCustomerRepository;
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
public class CustomerActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PQMCustomerRepository pqmCustomerRepository;
    @Autowired
    private PersonRepository personRepository;

    @Async
    @EventListener
    public void customerCreated(CustomerEvents.CustomerCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getCustomer().getId());
        object.setType("customer");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.customer.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void customerBasicInfoUpdated(CustomerEvents.CustomerBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PQMCustomer oldCustomer = event.getOldCustomer();
        PQMCustomer newCustomer = event.getNewCustomer();

        object.setObject(newCustomer.getId());
        object.setType("customer");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.customer.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getCustomerBasicInfoUpdatedJson(oldCustomer, newCustomer));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void customerFilesAdded(CustomerEvents.CustomerFilesAddedEvent event) {
        List<PQMCustomerFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.customer.files.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getCustomer());
        object.setType("customer");
        as.setObject(object);
        as.setData(getCustomerFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void customerFoldersAdded(CustomerEvents.CustomerFoldersAddedEvent event) {
        PQMCustomerFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.customer.files.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getCustomer());
        object.setType("customer");
        as.setObject(object);
        as.setData(getCustomerFoldersAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void customerFoldersDeleted(CustomerEvents.CustomerFoldersDeletedEvent event) {
        PQMCustomerFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.customer.files.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getCustomer());
        object.setType("customer");
        as.setObject(object);
        as.setData(getCustomerFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void customerFileDeleted(CustomerEvents.CustomerFileDeletedEvent event) {
        PQMCustomerFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.customer.files.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getCustomer());
        object.setType("customer");
        as.setObject(object);
        as.setData(getCustomerFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void customerFilesVersioned(CustomerEvents.CustomerFilesVersionedEvent event) {
        List<PQMCustomerFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.customer.files.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getCustomer());
        object.setType("customer");
        as.setObject(object);
        as.setData(getCustomerFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void customerFileRenamed(CustomerEvents.CustomerFileRenamedEvent event) {
        PQMCustomerFile oldFile = event.getOldFile();
        PQMCustomerFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals("Rename")) as.setActivity("plm.customer.files.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.customer.files.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getCustomer());
        object.setType("customer");
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
    public void customerFileLocked(CustomerEvents.CustomerFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.customer.files.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getCustomer());
        object.setType("customer");
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
    public void customerFileUnlocked(CustomerEvents.CustomerFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.customer.files.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getCustomer());
        object.setType("customer");
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
    public void customerFileDownloaded(CustomerEvents.CustomerFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.customer.files.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getCustomer());
        object.setType("customer");
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
    public void customerCommentAdded(CustomerEvents.CustomerCommentAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.customer.comment");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getCustomer().getId());
        object.setType("customer");
        as.setObject(object);
        as.setData(event.getComment().getComment());

        activityStreamService.create(as);
    }


    @Override
    public String getConverterKey() {
        return "plm.customer";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            PQMCustomer customer = pqmCustomerRepository.findOne(object.getObject());
            String customerType = "customer";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.customer.create":
                    convertedString = getCustomerCreatedString(messageString, actor, customer);
                    break;
                case "plm.customer.update.basicinfo":
                    convertedString = getCustomerBasicInfoUpdatedString(messageString, actor, customer, as);
                    break;
                case "plm.customer.files.add":
                    convertedString = getCustomerFilesAddedString(messageString, customer, as);
                    break;
                case "plm.customer.files.delete":
                    convertedString = getCustomerFileDeletedString(messageString, customer, as);
                    break;
                case "plm.customer.files.folders.add":
                    convertedString = getCustomerFoldersAddedString(messageString, customer, as);
                    break;
                case "plm.customer.files.folders.delete":
                    convertedString = getCustomerFoldersDeletedString(messageString, customer, as);
                    break;
                case "plm.customer.files.version":
                    convertedString = getCustomerFilesVersionedString(messageString, customer, as);
                    break;
                case "plm.customer.files.rename":
                    convertedString = getCustomerFileRenamedString(messageString, customer, as);
                    break;
                case "plm.customer.files.replace":
                    convertedString = getCustomerFileRenamedString(messageString, customer, as);
                    break;
                case "plm.customer.files.lock":
                    convertedString = getCustomerFileString(messageString, customer, as);
                    break;
                case "plm.customer.files.unlock":
                    convertedString = getCustomerFileString(messageString, customer, as);
                    break;
                case "plm.customer.files.download":
                    convertedString = getCustomerFileString(messageString, customer, as);
                    break;
                case "plm.customer.comment":
                    convertedString = getCustomerCommentString(messageString, customer, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getCustomerCreatedString(String messageString, Person actor, PQMCustomer customer) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), customer.getName());
    }


    private String getCustomerBasicInfoUpdatedString(String messageString, Person actor, PQMCustomer customer, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), customer.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.customer.update.basicinfo.property");

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

    private String getCustomerBasicInfoUpdatedJson(PQMCustomer oldCustomer, PQMCustomer newCustomer) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldCustomer.getName();
        String newValue = newCustomer.getName();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
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

    private String getCustomerFilesAddedJson(List<PQMCustomerFile> files) {
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

    private String getCustomerFoldersAddedJson(PQMCustomerFile file) {
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

    private String getCustomerFoldersDeletedJson(PQMCustomerFile file) {
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

    private String getCustomerFilesAddedString(String messageString, PQMCustomer customer, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), customer.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.customer.files.add.file");

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

    private String getCustomerFoldersAddedString(String messageString, PQMCustomer customer, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), customer.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getCustomerFoldersDeletedString(String messageString, PQMCustomer customer, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), customer.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getCustomerFileDeletedString(String messageString, PQMCustomer customer, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), customer.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getCustomerFilesVersionedJson(List<PQMCustomerFile> files) {
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

    private String getCustomerFilesVersionedString(String messageString, PQMCustomer customer, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), customer.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.customer.files.version.file");

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

    private String getCustomerFileRenamedString(String messageString, PQMCustomer customer, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    customer.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getCustomerFileString(String messageString, PQMCustomer customer, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    customer.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getCustomerCommentString(String messageString, PQMCustomer customer, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                customer.getName());

        String s = highlightValue("Comment: ");
        s += as.getData();
        s = addMarginToMessage(s);

        message += s;

        return message;
    }

}
