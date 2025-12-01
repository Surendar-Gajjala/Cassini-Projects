package com.cassinisys.platform.events;

import com.cassinisys.platform.model.core.CassiniObject;
import lombok.AllArgsConstructor;
import lombok.Data;

public final class CassiniObjectEvents {

    @Data
    @AllArgsConstructor
    public static class CassiniObjectCreatedEvent {
        private CassiniObject object;
    }

    @Data
    @AllArgsConstructor
    public static class CassiniObjectUpdatedEvent {
        private CassiniObject object;
    }

    @Data
    @AllArgsConstructor
    public static class CassiniObjectDeletedEvent {
        private CassiniObject object;
    }
}
