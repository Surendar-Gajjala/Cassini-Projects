package com.cassinisys.plm.event;

import javax.annotation.Resource;

import com.cassinisys.plm.model.mes.MESObjectAttribute;
import com.cassinisys.plm.model.mes.MESOperation;
import com.cassinisys.plm.model.mes.MESOperationResources;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Lenovo on 29-10-2020.
 */
public final class OperationEvents {

    @Data
    @AllArgsConstructor
    public static class OperationCreatedEvent {
        private MESOperation operation;
    }
    @Data
    @AllArgsConstructor
    public static class OperationResourceCreatedEvent {
        private MESOperation operation;
        private MESOperationResources resource;
    }

    @Data
    @AllArgsConstructor
    public static class OperationResourceDeletedEvent {
        private MESOperation operation;
        private MESOperationResources resource;
    }

    @Data
    @AllArgsConstructor
    public static class OperationResourceUpdatedEvent {
        private MESOperationResources oldResource;
        private MESOperationResources newResource;
    }

    @Data
    @AllArgsConstructor
    public static class OperationBasicInfoUpdatedEvent {
        private MESOperation oldOperation;
        private MESOperation newOperation;
    }

    @Data
    @AllArgsConstructor
    public static class OperationAttributesUpdatedEvent {
        private MESOperation operation;
        private MESObjectAttribute oldAttribute;
        private MESObjectAttribute newAttribute;
    }

}
