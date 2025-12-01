package com.cassinisys.plm.event;

import com.cassinisys.plm.model.mes.MESInstrument;
import com.cassinisys.plm.model.mes.MESObjectAttribute;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Lenovo on 29-10-2020.
 */
public final class InstrumentEvents {
    @Data
    @AllArgsConstructor
    public static class InstrumentCreatedEvent {
        private MESInstrument instrument;
    }

    @Data
    @AllArgsConstructor
    public static class InstrumentBasicInfoUpdatedEvent {
        private MESInstrument oldInstrument;
        private MESInstrument newInstrument;
    }

    @Data
    @AllArgsConstructor
    public static class InstrumentAttributesUpdatedEvent {
        private MESInstrument instrument;
        private MESObjectAttribute oldAttribute;
        private MESObjectAttribute newAttribute;
    }
}
