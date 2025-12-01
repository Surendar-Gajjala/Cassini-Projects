package com.cassinisys.plm.model.mes.dto;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.plm.model.mes.BOPPlanTypeEnum;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by smukka on 27-07-2022.
 */
@Data
public class BOPRouteDto {
    private Integer id;
    private Integer bop;
    private Integer operation;
    private Integer phantom;
    private Integer parent;
    private BOPPlanTypeEnum type = BOPPlanTypeEnum.OPERATION;
    private Integer setupTime;
    private Integer cycleTime;
    private String sequenceNumber;

    private String name;
    private String number;
    private String typeName;
    private String description;
    private Integer count = 0;
    private Integer level = 0;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    private String createdByName;
    private String modifiedByName;
    private List<BOPRouteDto> children = new ArrayList<>();

    private Integer resourceCount = 0;
    private Integer partCount = 0;
    private List<BOPResourceDto> resources = new ArrayList<>();

}
