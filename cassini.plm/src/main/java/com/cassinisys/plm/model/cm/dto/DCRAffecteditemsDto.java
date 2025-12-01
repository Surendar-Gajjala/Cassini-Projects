package com.cassinisys.plm.model.cm.dto;

import com.cassinisys.plm.model.cm.PLMDCR;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CassiniSystems on 12-06-2020.
 */
@Data
public class DCRAffecteditemsDto {

    private Integer id;

    private Integer item;

    private Integer toItem;

    private Integer dcr;

    private String notes;

    private String itemNumber;

    private String itemName;

    private String itemType;
    private String phase;

    private String revision;

    private PLMLifeCyclePhase lifeCyclePhase;

    private Integer dco;


    private String fromRevision;
    private String description;
    private String toRevision;
    private String fromPhase;
    private String toPhase;


    private PLMLifeCyclePhase fromLifeCycle;

    private PLMLifeCyclePhase toLifeCycle;

    private List<PLMDCR> plmdcrs = new ArrayList<>();



}
