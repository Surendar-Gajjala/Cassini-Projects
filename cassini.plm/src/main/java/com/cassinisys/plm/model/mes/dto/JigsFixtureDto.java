package com.cassinisys.plm.model.mes.dto;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
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
public class JigsFixtureDto {
    private Integer id;
    private String name;
    private String type;
    private String number;
    private String description;
    private String createdBy;
    private String modifiedBy;
    private Boolean active = Boolean.TRUE;
    private Boolean requiresMaintenance = Boolean.TRUE;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    private Boolean hasImage = false;
    private String objectType;
    private String subType;
    private List<FileDto> jigsFixtureFiles = new LinkedList<>();
}
