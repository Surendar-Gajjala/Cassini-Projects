package com.cassinisys.plm.model.dto;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.plm.SharePermission;
import com.cassinisys.plm.model.plm.ShareType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by CassiniSystems on 23-09-2020.
 */
@Data
public class SharedMfrPartObjectDto {

    private Integer id;
    private String name;
    private String number;
    private String description;
    private String mfr;
    private String manager;
    private Integer mfrId;
    private ShareType shareType;
    private SharePermission permission;
    private String sharedBy;
    private PLMLifeCyclePhase lifeCyclePhase;
    private String type;
    private String sharedTo;
    private String supplier;
    private String status;
    private String owner;
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date sharedOn;
}
