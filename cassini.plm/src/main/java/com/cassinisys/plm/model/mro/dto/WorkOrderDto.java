package com.cassinisys.plm.model.mro.dto;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.plm.model.mro.WorkOrderStatusType;
import com.cassinisys.plm.model.mro.WorkPriorityType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by subramanyam on 19-11-2020.
 */
@Data
public class WorkOrderDto {
    private Integer id;
    private String typeName;
    private String name;
    private String number;
    private String description;
    private String notes;
    private Integer asset;
    private String assetName;
    private Integer request;
    private String requestNumber;
    private Integer plan;
    private String planNumber;
    private String assignedToName;
    private WorkPriorityType priority;
    private WorkOrderStatusType status;
    private String objectType;
    private String modifiedByName;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;
}
