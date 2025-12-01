package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.events.CommonEvents;
import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.plm.event.PushNotificationEvents;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.service.PushNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Nageshreddy on 31-10-2020.
 */

@Component
public class PushNotificationEventListener {

    @Autowired
    private PushNotificationService pushNotificationService;

    @Autowired
    private PersonRepository personRepository;

    @Async
    @EventListener
    public void createItemNotification(PushNotificationEvents.CreateItemNotification event) {
        PLMItem item = event.getItem();
        pushNotificationService.sendPushNotification(item.getItemName() + " created", new String[0], "itemDetails");
    }


    @Async
    @EventListener
    public void itemUpdateNotification(PushNotificationEvents.UpdateItemNotification event) {
        PLMItem item = event.getItem();
        String[] strarray = new String[event.getTokens().size()];
        event.getTokens().toArray(strarray);
        pushNotificationService.sendPushNotification(event.getMessage(), strarray, "itemDetails");
    }


    @Async
    @EventListener
    public void workflowUpdateNotification(PushNotificationEvents.WorkflowUpdateNotification event) {
        PLMWorkflow workflow = event.getWorkflow();
        String[] strarray = new String[event.getTokens().size()];
        event.getTokens().toArray(strarray);
        pushNotificationService.sendPushNotification(event.getMessage(), strarray, "workflowDetails");
    }

    @Async
    @EventListener
    public void requirementUpdateNotification(PushNotificationEvents.UserTaskNotification event) {
        String[] strarray = new String[event.getTokens().size()];
        event.getTokens().toArray(strarray);
        pushNotificationService.sendPushNotification(event.getMessage(), strarray, event.getObjectName());
    }

    @Async
    @EventListener
    public void convertionNotification(CommonEvents.SendPushNotificationOnCommentAdd event) {
        Comment comment = event.getComment();
        List<Person> personList = personRepository.findAll();
        List<String> tokens = new LinkedList<>();
        for (Person person : personList) {
            if (comment.getCommentedBy().getId() != person.getId() &&
                    person.getMobileDevice() != null && person.getMobileDevice().getDeviceId() != null) {
                tokens.add(person.getMobileDevice().getDeviceId());
            }
        }

        String[] strarray = new String[tokens.size()];
        tokens.toArray(strarray);
        pushNotificationService.sendPushNotification(comment.getComment(), strarray, "itemDetails");
    }


}
