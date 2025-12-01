package com.cassinisys.plm.model.mes;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "MES_MBOMREVISION")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class MESMBOMRevision extends CassiniObject {

    @Column(name = "MASTER")
    private Integer master;

    @Column(name = "ITEM_REVISION")
    private Integer itemRevision;

    @Column(name = "REVISION")
    private String revision;

    @OneToOne
    @JoinColumn(name = "LIFECYCLE_PHASE")
    private PLMLifeCyclePhase lifeCyclePhase;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RELEASED_DATE", nullable = false)
    private Date releasedDate;

    @Column(name = "WORKFLOW")
    private Integer workflow;
    @Column(name = "RELEASED")
    private Boolean released = Boolean.FALSE;
    @Column(name = "REJECTED")
    private Boolean rejected = Boolean.FALSE;


    @Transient
    private String oldRevision;
    @Transient
    private Boolean pendingMco = Boolean.FALSE;
    @Transient
    private Boolean workflowStarted = false;
    @Transient
    private List<MESMBOMRevisionStatusHistory> statusHistory = new ArrayList<>();
    @Transient
    private Integer mcoId;
    @Transient
    private String mcoNumber;
    @Transient
    private String mcoTitle;
    @Transient
    private String mcoDescription;

    public MESMBOMRevision() {
        super.setObjectType(MESEnumObject.MBOMREVISION);
    }

}
