package com.cassinisys.plm.model.mes.dto;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.plm.dto.FileDto;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by smukka on 11-05-2022.
 */
@Data
public class MBOMDto {
    private Integer id;
    private Integer item;
    private String name;
    private String number;
    private String description;
    private String typeName;
    private String revision;
    private PLMLifeCyclePhase lifeCyclePhase;
    private Integer latestRevision;
    private Integer latestReleasedRevision;

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

    private String itemName;
    private String itemNumber;
    private String itemRevision;
    private Integer itemRevisionId;

    private String toRevision;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date releasedDate;

    private Boolean released = Boolean.FALSE;
    private Boolean rejected = Boolean.FALSE;
    private Boolean pendingMco = Boolean.FALSE;
    private String mcoNumber;
    private Integer mco;

    private List<MBOMRevisionDto> mbomRevisions = new LinkedList<>();

    private List<FileDto> mbomFiles = new LinkedList<>();
    private String objectType = "MBOM";
    private Integer tagsCount = 0;

}
