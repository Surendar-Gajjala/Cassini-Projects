package com.cassinisys.plm.model.plm.dto;

import com.cassinisys.plm.model.plm.PLMLifeCycle;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by smukka on 04-08-2022.
 */
@Data
@JsonIgnoreProperties
public class LifecycleDto {
    private PLMLifeCycle lifecycle;
    private PLMLifeCyclePhase lifecyclePhase;
    private PLMLifeCyclePhase oldLifecyclePhase;
}
