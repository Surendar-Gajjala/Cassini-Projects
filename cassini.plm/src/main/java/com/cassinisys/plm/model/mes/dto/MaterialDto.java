package com.cassinisys.plm.model.mes.dto;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.plm.model.mes.MESMaterialType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import com.cassinisys.plm.model.plm.dto.FileDto;

/**
 * Created by CassiniSystems on 27-10-2020.
 */
@Data
public class MaterialDto {
    private Integer id;
    private String name;
    private MESMaterialType type;
    private String number;
    private String description;
    private Integer qom;
    private Integer uom;
    private String qomName;
    private String uomName;
    private String createdBy;
    private String modifiedBy;
    private String objectType;
    private String subType;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    private Boolean hasImage = false;
    private List<FileDto> materialFiles = new LinkedList<>();
}
