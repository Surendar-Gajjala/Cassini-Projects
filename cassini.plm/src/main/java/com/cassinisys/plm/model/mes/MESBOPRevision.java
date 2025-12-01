package com.cassinisys.plm.model.mes;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.wf.WorkflowStatusType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "MES_BOPREVISION")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class MESBOPRevision extends CassiniObject {

    @Column(name = "MASTER")
    private Integer master;

    @Column(name = "MBOM_REVISION")
    private Integer mbomRevision;

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
    @Column(name = "STATUS", nullable = true)
    private String status = "NONE";
    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.wf.WorkflowStatusType")})
    @Column(name = "STATUS_TYPE")
    private WorkflowStatusType statusType = WorkflowStatusType.UNDEFINED;

    @Column(name = "OLD_REVISION")
    private String oldRevision;
    @Transient
    private Boolean workflowStarted = false;
    @Transient
    private Boolean finishWorkflow = Boolean.FALSE;
    @Transient
    private String mbomName;
    @Transient
    private String mbomNumber;
    @Transient
    private String mbomRevisionName;
    @Transient
    private List<MESBOPRevisionStatusHistory> statusHistory = new ArrayList<>();
    @Transient
    private Boolean startWorkflow = Boolean.FALSE;
    @Transient
    private String workflowSettingStatus = "NONE";
    @Transient
    private String workflowSettingStatusType = "UNDEFINED";

    public MESBOPRevision() {
        super.setObjectType(MESEnumObject.BOPREVISION);
    }

}
