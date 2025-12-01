package com.cassinisys.plm.model.mes.dto;

import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;

import lombok.Data;

@Data
public class MBOMRevisionDto {

    private Integer id;
    private Integer master;
    private String revision;
    private PLMLifeCyclePhase lifeCyclePhase;
    
}
