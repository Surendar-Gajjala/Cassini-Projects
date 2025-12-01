package com.cassinisys.plm.event;

import com.cassinisys.plm.model.plm.PLMObjectType;
import lombok.AllArgsConstructor;
import lombok.Data;

public final class ClassificationEvents {

    @Data
    @AllArgsConstructor
    public static class ClassificationTypeCreatedEvent {
        private PLMObjectType type;
        private Object object;
    }

    @Data
    @AllArgsConstructor
    public static class ClassificationTypeDeletedEvent {
        private PLMObjectType type;
        private Object object;
    }

    @Data
    @AllArgsConstructor
    public static class ClassificationTypeUpdatedEvent {
        private PLMObjectType type;
        private Object oldType;
        private Object newType;
    }

    @Data
    @AllArgsConstructor
    public static class ClassificationTypeAttributeCreatedEvent {
        private PLMObjectType type;
        private Object object;
    }

    @Data
    @AllArgsConstructor
    public static class ClassificationTypeAttributeUpdatedEvent {
        private PLMObjectType type;
        private Object oldValue;
        private Object newValue;
    }

    @Data
    @AllArgsConstructor
    public static class ClassificationTypeAttributeDeletedEvent {
        private PLMObjectType type;
        private Object object;
    }


}
