package com.cassinisys.plm.event;

import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Created by Nageshreddy on 28-10-2020.
 */
public class PushNotificationEvents {

    @Data
    @AllArgsConstructor
    public static class CreateItemNotification {
        private PLMItem item;
    }


    @Data
    @AllArgsConstructor
    public static class UpdateItemNotification {
        private PLMItem item;
        private String message;
        private List<String> tokens;
    }

    @Data
    @AllArgsConstructor
    public static class WorkflowUpdateNotification {
        private PLMWorkflow workflow;
        private String message;
        private List<String> tokens;
    }


    @Data
    @AllArgsConstructor
    public static class UserTaskNotification {
        private String objectName;
        private String message;
        private List<String> tokens;
    }


}
