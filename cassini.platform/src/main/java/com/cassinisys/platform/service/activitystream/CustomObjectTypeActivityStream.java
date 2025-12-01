package com.cassinisys.platform.service.activitystream;

import com.cassinisys.platform.events.CustomObjectEvents;
import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.model.custom.CustomObjectType;
import com.cassinisys.platform.model.custom.CustomObjectTypeAttribute;
import com.cassinisys.platform.repo.custom.CustomObjectTypeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CustomObjectTypeActivityStream extends BaseActivityStream implements ActivityStreamConverter {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CustomObjectTypeRepository customObjectTypeRepository;


    @Async
    @EventListener
    public void customObjectTypeCreated(CustomObjectEvents.CustomObjectTypeCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        ActivityStream as = new ActivityStream();
        if (event.getType().equals(ObjectType.CUSTOMOBJECTTYPE)) {
            CustomObjectType customObjectType = (CustomObjectType) event.getObject();
            object.setObject(customObjectType.getId());
            as.setActivity("classification.customObjectType.create");
            object.setType("customObjectType");
        }

        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void customObjectTypeDeleted(CustomObjectEvents.CustomObjectTypeDeletedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        ActivityStream as = new ActivityStream();
        if (event.getType().equals(ObjectType.CUSTOMOBJECTTYPE)) {
            CustomObjectType customObjectType = (CustomObjectType) event.getObject();
            object.setObject(customObjectType.getParentType());
            as.setData(customObjectType.getName());
            as.setActivity("classification.customObjectType.delete");
            object.setType("customObjectType");
        }

        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void customObjectTypeUpdated(CustomObjectEvents.CustomObjectTypeUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals(ObjectType.CUSTOMOBJECTTYPE)) {
            CustomObjectType oldType = (CustomObjectType) event.getOldType();
            CustomObjectType newType = (CustomObjectType) event.getNewType();
            object.setObject(newType.getId());
            object.setType("customObjectType");
            as.setActivity("classification.customObjectType.update");
            as.setData(getCustomObjectTypeUpdatedJson(oldType, newType));
        }

        as.setObject(object);
        as.setConverter(getConverterKey());
        if (!as.getData().equals("[]")) activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void customObjectTypeAttributeCreated(CustomObjectEvents.CustomObjectTypeAttributeCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        ActivityStream as = new ActivityStream();
        if (event.getType().equals(ObjectType.CUSTOMOBJECTTYPE)) {
            CustomObjectTypeAttribute customObjectType = (CustomObjectTypeAttribute) event.getObject();
            object.setObject(customObjectType.getCustomObjectType());
            as.setData(customObjectType.getName());
            as.setActivity("classification.customObjectType.attribute.add");
            object.setType("customObjectType");
        }

        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void customObjectTypeAttributeDeleted(CustomObjectEvents.CustomObjectTypeAttributeDeletedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        ActivityStream as = new ActivityStream();
        if (event.getType().equals(ObjectType.CUSTOMOBJECTTYPE)) {
            CustomObjectTypeAttribute customObjectType = (CustomObjectTypeAttribute) event.getObject();
            object.setObject(customObjectType.getCustomObjectType());
            as.setData(customObjectType.getName());
            as.setActivity("classification.customObjectType.attribute.delete");
            object.setType("customObjectType");
        }

        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void customObjectTypeAttributeUpdated(CustomObjectEvents.CustomObjectTypeAttributeUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals(ObjectType.CUSTOMOBJECTTYPE)) {
            CustomObjectTypeAttribute oldType = (CustomObjectTypeAttribute) event.getOldValue();
            CustomObjectTypeAttribute newType = (CustomObjectTypeAttribute) event.getNewValue();
            object.setObject(newType.getCustomObjectType());
            object.setType("customObjectType");
            as.setActivity("classification.customObjectType.attribute.update");
            as.setData(getCustomObjectTypeAttributeUpdatedJson(oldType, newType));
        }

        as.setObject(object);
        as.setConverter(getConverterKey());
        if (!as.getData().equals("[]")) activityStreamService.create(as);
    }


    @Override
    public String getConverterKey() {
        return "classification";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            String name = null;
            if (object.getType().equals("customObjectType"))
                name = customObjectTypeRepository.findOne(object.getObject()).getName();

            String activity = as.getActivity();
            switch (activity) {
                case "classification.customObjectType.create":
                    convertedString = getTypeCreatedString(messageString, actor, name);
                    break;
                case "classification.customObjectType.update":
                    convertedString = getTypeUpdatedString(messageString, actor, name, as);
                    break;
                case "classification.customObjectType.delete":
                    convertedString = getTypeDeleteString(messageString, actor, name, as);
                    break;
                case "classification.customObjectType.attribute.add":
                    convertedString = getTypeAttributeCreatedString(messageString, actor, as);
                    break;
                case "classification.customObjectType.attribute.delete":
                    convertedString = getTypeAttributeDeletedString(messageString, actor, name, as);
                    break;
                case "classification.customObjectType.attribute.update":
                    convertedString = getTypeAttributeUpdatedString(messageString, actor, name, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getTypeCreatedString(String messageString, Person actor, String name) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), name);
    }

    private String getTypeDeleteString(String messageString, Person actor, String name, ActivityStream as) {
        String json = as.getData();
        return MessageFormat.format(messageString, actor.getFullName().trim(), json, name);
    }

    private String getTypeUpdatedString(String messageString, Person actor, String name, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), name);

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("classification.customObjectType.update.property");
        String displayTabString = activityStreamResourceBundle.getString("classification.customObjectType.update.property").substring(0, 20);

        String json = as.getData();
        try {
            List<ASPropertyChangeDTO> propChanges = objectMapper.readValue(json, new TypeReference<List<ASPropertyChangeDTO>>() {
            });
            propChanges.forEach(p -> {
                String s = null;
                if (p.getProperty().equals("Display Tabs")) {
                    s = addMarginToMessage(MessageFormat.format(displayTabString, highlightValue(p.getProperty())));
                    sb.append(s);
                } else {
                    s = addMarginToMessage(MessageFormat.format(updateString,
                            highlightValue(p.getProperty()),
                            highlightValue(p.getOldValue()),
                            highlightValue(p.getNewValue())));
                    sb.append(s);
                }
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getTypeAttributeUpdatedString(String messageString, Person actor, String name, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), name);

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("classification.customObjectType.attribute.update.property");

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

    private String getTypeAttributeCreatedString(String messageString, Person actor, ActivityStream as) {
        String name = as.getData();
        return MessageFormat.format(messageString, actor.getFullName().trim(), name);
    }

    private String getTypeAttributeDeletedString(String messageString, Person actor, String name, ActivityStream as) {
        String data = as.getData();
        return MessageFormat.format(messageString, actor.getFullName().trim(), data, name);
    }

    private String getCustomObjectTypeUpdatedJson(CustomObjectType oldType, CustomObjectType newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldValue2 = oldType.getNumberSource().getName();
        String newValue2 = newType.getNumberSource().getName();
        if (oldValue2 == null) {
            oldValue2 = "";
        }
        if (newValue2 == null) {
            newValue2 = "";
        }
        if (!newValue2.equals(oldValue2)) {
            changes.add(new ASPropertyChangeDTO("Number Source", oldValue2, newValue2));
        }

        String oldValue3 = oldType.getRevisionSequence().getName();
        String newValue3 = newType.getRevisionSequence().getName();
        if (oldValue3 == null) {
            oldValue3 = "";
        }
        if (newValue3 == null) {
            newValue3 = "";
        }
        if (!newValue3.equals(oldValue3)) {
            changes.add(new ASPropertyChangeDTO("Revision sequence", oldValue3, newValue3));
        }

        String oldValue4 = oldType.getLifecycle().getName();
        String newValue4 = newType.getLifecycle().getName();
        if (oldValue4 == null) {
            oldValue4 = "";
        }
        if (newValue4 == null) {
            newValue4 = "";
        }
        if (!newValue4.equals(oldValue4)) {
            changes.add(new ASPropertyChangeDTO("Life cycle", oldValue4, newValue4));
        }

        String[] oldValue7 = oldType.getTabs();
        String[] newValue7 = newType.getTabs();
        if (!Arrays.equals(oldValue7, newValue7)) {
            changes.add(new ASPropertyChangeDTO("Display Tabs", null, null));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }
    

    private String getCustomObjectTypeAttributeUpdatedJson(CustomObjectTypeAttribute oldType, CustomObjectTypeAttribute newType) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldType.getName();
        String newValue = newType.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        String oldValue1 = oldType.getDescription();
        String newValue1 = newType.getDescription();
        if (oldValue1 == null) {
            oldValue1 = "";
        }
        if (newValue1 == null) {
            newValue1 = "";
        }
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue1, newValue1));
        }

        String oldGroupName = oldType.getAttributeGroup();
        String newGroupName = newType.getAttributeGroup();
        if (oldGroupName == null) {
            oldGroupName = "";
        }
        if (newGroupName == null) {
            newGroupName = "";
        }
        if (!newGroupName.equals(oldGroupName)) {
            changes.add(new ASPropertyChangeDTO("Group Name", oldGroupName, newGroupName));
        }

        String oldValue2 = oldType.getDataType().name();
        String newValue2 = newType.getDataType().name();
        if (!newValue2.equals(oldValue2)) {
            changes.add(new ASPropertyChangeDTO("Data type", oldValue2, newValue2));
        }

        if (oldType.getMeasurement() != null && newType.getMeasurement() != null) {
            String oldValue3 = oldType.getMeasurement().getName();
            String newValue3 = newType.getMeasurement().getName();
            if (!newValue3.equals(oldValue3)) {
                changes.add(new ASPropertyChangeDTO("Measurements", oldValue3, newValue3));
            }
        }

        String oldValue4 = oldType.getDefaultTextValue();
        String newValue4 = newType.getDefaultTextValue();
        if (oldValue4 == null) {
            oldValue4 = "";
        }
        if (newValue4 == null) {
            newValue4 = "";
        }
        if (!newValue4.equals(oldValue4)) {
            changes.add(new ASPropertyChangeDTO("Default text", oldValue4, newValue4));
        }

        String oldValue5 = oldType.getDefaultListValue();
        String newValue5 = newType.getDefaultListValue();
        if (oldValue5 == null) {
            oldValue5 = "";
        }
        if (newValue5 == null) {
            newValue5 = "";
        }
        if (!newValue5.equals(oldValue5)) {
            changes.add(new ASPropertyChangeDTO("Default list", oldValue5, newValue5));
        }

        Boolean oldValue11 = oldType.isListMultiple();
        Boolean newValue11 = newType.isListMultiple();
        if (newValue11 != oldValue11) {
            changes.add(new ASPropertyChangeDTO("Multiple list", oldValue11.toString(), newValue11.toString()));
        }

        
        Boolean oldValue7 = oldType.getRevisionSpecific();
        Boolean newValue7 = newType.getRevisionSpecific();
        if (newValue7 != oldValue7) {
            changes.add(new ASPropertyChangeDTO("Revision specific", oldValue7.toString(), newValue7.toString()));
        }
        
        Boolean oldValue9 = oldType.isRequired();
        Boolean newValue9 = newType.isRequired();
        if (newValue9 != oldValue9) {
            changes.add(new ASPropertyChangeDTO("Is required", oldValue9.toString(), newValue9.toString()));
        }

        Boolean oldValue10 = oldType.isVisible();
        Boolean newValue10 = newType.isVisible();
        if (newValue10 != oldValue10) {
            changes.add(new ASPropertyChangeDTO("Is visible", oldValue10.toString(), newValue10.toString()));
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }
    
}
