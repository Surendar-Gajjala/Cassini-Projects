package com.cassinisys.plm.model.wf.dto;

import com.cassinisys.plm.model.plm.PLMLifeCycle;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by subramanyam on 03-07-2021.
 */
@Data
@JsonIgnoreProperties
public class DefinitionEventDto {
    private PLMLifeCycle lifecycle;
    private PLMLifeCyclePhase lifecyclePhase;
}
