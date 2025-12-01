package com.cassinisys.plm.model.mes.dto;

import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by smukka on 06-10-2022.
 */
@Data
public class MBOMInstanceDto {
    private Integer id;
    private Integer productionOrderItem;
    private String number;
    private String serialNumber;
    private String name;
    private String description;
    private String batchNumber;
    private Integer mbomRevision;
    private String status;
    private Boolean released = Boolean.FALSE;
    private Boolean rejected = Boolean.FALSE;
    private String rejectedReason;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date mfgDate;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date rejectedDate;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date finishDate;
    private String objectType;
    private List<MBOMInstanceDto> children = new ArrayList<>();
}
