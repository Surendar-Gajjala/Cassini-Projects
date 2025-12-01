package com.cassinisys.plm.model.cm.dto;

import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class MCOProductAffectedItemDto {

    private Integer id;
    private Integer mco;
    private Integer item;
    private Integer toItem;
    private String fromRevision;
    private String toRevision;
    private String notes;

    private String number;
    private String description;
    private String name;
    private String type;
    private String itemName;
    private String itemRevision;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date effectiveDate;

    private PLMLifeCyclePhase fromLifecyclePhase;
    private PLMLifeCyclePhase toLifecyclePhase;
    private PLMLifeCyclePhase oldToLifecyclePhase;
    private List<PLMLifeCyclePhase> normalLifecyclePhases = new ArrayList<>();
    private List<PLMLifeCyclePhase> releasedLifecyclePhases = new ArrayList<>();
    private List<PLMLifeCyclePhase> rejectedLifecyclePhases = new ArrayList<>();


}
