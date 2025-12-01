package com.cassinisys.platform.service.activitystream;

import com.cassinisys.platform.events.ProfileEvents;
import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.common.Profile;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.common.ProfileRepository;
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

@Component
public class ProfileActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private PersonRepository personRepository;

    @Async
    @EventListener
    public void profileCreated(ProfileEvents.ProfileCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProfile().getId());
        object.setType("profile");
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.profile.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void profileUpdated(ProfileEvents.ProfileUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        Profile oldProfile = event.getOldProfile();
        Profile newProfile = event.getNewProfile();

        object.setObject(newProfile.getId());
        object.setType("profile");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.profile.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getProfileBasicInfoUpdatedJson(oldProfile, newProfile));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }


    @Override
    public String getConverterKey() {
        return "plm.profile";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            Profile profile = profileRepository.findOne(object.getObject());
            String type = "profile";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.profile.create":
                    convertedString = getProfileCreatedString(messageString, actor, profile);
                    break;
                case "plm.profile.update.basicinfo":
                    convertedString = getProfileBasicInfoUpdatedString(messageString, actor, profile, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getProfileCreatedString(String messageString, Person actor, Profile profile) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), profile.getName());
    }


    private String getProfileBasicInfoUpdatedString(String messageString, Person actor, Profile profile, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), profile.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.profile.update.basicinfo.property");

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

    private String getProfileBasicInfoUpdatedJson(Profile oldProfile, Profile newProfile) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldProfile.getName();
        String newValue = newProfile.getName();
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


}
