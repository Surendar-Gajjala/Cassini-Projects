package com.cassinisys.plm.model.rm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "RM_OBJECT")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RmObject extends CassiniObject {

    @Column(name = "OBJECT_NUMBER")
    private String objectNumber;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "IS_RELEASED")
    private Boolean released = Boolean.FALSE;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RELEASED_DATE", nullable = false)
    private Date releasedDate;

    @Column(name = "LATEST_REVISION")
    private Integer latestRevision;

    @Column(name = "LATEST_RELEASED_REVISION")
    private Integer latestReleasedRevision;

    @Column(name = "LATEST")
    private Boolean latest = Boolean.FALSE;

    public RmObject(PLMObjectType type) {
        super(type);
    }

    public RmObject() {
    }


}
