package com.cassinisys.plm.model.mes;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.wf.WorkflowStatusType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Lenovo on 26-10-2020.
 */

@Entity
@Table(name = "MES_PRODUCTION_ORDER")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class MESProductionOrder extends MESObject {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE")
    private MESProductionOrderType type;

    @Column(name = "PLANT")
    private Integer plant;

    @Transient
    private String plantName;

    @Column(name = "WORKFLOW")
    private Integer workflow;

    @Column(name = "ASSIGNED_TO")
    private Integer assignedTo;

    @Column(name = "SHIFT")
    private Integer shift;

    @Column(name = "STATUS", nullable = true)
    private String status = "NONE";

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PLANNED_STARTDATE")
    private Date plannedStartDate;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PLANNED_FINISHDATE")
    private Date plannedFinishDate;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.wf.WorkflowStatusType")})
    @Column(name = "STATUS_TYPE")
    private WorkflowStatusType statusType = WorkflowStatusType.UNDEFINED;

    @OneToOne
    @JoinColumn(name = "LIFECYCLE_PHASE")
    private PLMLifeCyclePhase lifeCyclePhase;

    @Column(name = "APPROVED")
    private Boolean approved = Boolean.FALSE;
    @Column(name = "REJECTED")
    private Boolean rejected = Boolean.FALSE;
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "APPROVED_DATE")
    private Date approvedDate;

    @Transient
    private Integer workflowDefinition;

    @Transient
    private String shiftName;

    public MESProductionOrder() {
        super.setObjectType(MESEnumObject.PRODUCTIONORDER);
    }
}
