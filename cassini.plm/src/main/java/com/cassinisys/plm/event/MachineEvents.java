package com.cassinisys.plm.event;

import com.cassinisys.plm.model.mes.MESMachine;
import com.cassinisys.plm.model.mes.MESObjectAttribute;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Hello on 10/30/2020.
 */
public final class MachineEvents {
    @Data
    @AllArgsConstructor
    public static class MachineCreatedEvent {
        private MESMachine machine;
    }

    @Data
    @AllArgsConstructor
    public static class MachineBasicInfoUpdatedEvent {
        private MESMachine oldMachine;
        private MESMachine newMachine;
    }

    @Data
    @AllArgsConstructor
    public static class MachineAttributesUpdatedEvent {
        private MESMachine machine;
        private MESObjectAttribute oldAttribute;
        private MESObjectAttribute newAttribute;
    }
}
