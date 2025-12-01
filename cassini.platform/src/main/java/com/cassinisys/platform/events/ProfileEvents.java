package com.cassinisys.platform.events;

import com.cassinisys.platform.model.common.Profile;
import lombok.AllArgsConstructor;
import lombok.Data;

public final class ProfileEvents {
    @Data
    @AllArgsConstructor
    public static class ProfileCreatedEvent {
        private Profile profile;
    }

    @Data
    @AllArgsConstructor
    public static class ProfileUpdatedEvent {
        private Profile oldProfile;
        private Profile newProfile;
    }

}