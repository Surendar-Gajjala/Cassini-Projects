package com.cassinisys.plm.model.mro.dto;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
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
public class MaintenancePlanDto {
    private Integer id;
    private String name;
    private String number;
    private String description;
    private Integer asset;
    private String assetName;
    private String resourceName;
    private String resourceType;
    private String objectType;
    private String modifiedByName;
    private String subType;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;
}
