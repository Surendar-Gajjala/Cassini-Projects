package com.cassinisys.plm.model.cm;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
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
 * Created by lakshmi on 1/2/2016.
 */

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_ECO")
@PrimaryKeyJoinColumn(name = "ECO_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMECO extends PLMChange {

    @Column(name = "ECO_NUMBER", nullable = false)
    private String ecoNumber;

    @Column(name = "ECO_TYPE")
    private Integer ecoType;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "CHANGE_ANALYST", nullable = false)
    private Integer ecoOwner;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Column(name = "REASON_FOR_CHANGE", nullable = false)
    private String reasonForChange;

    @Column(name = "STATUS", nullable = true)
    private String status = "NONE";

    @Column(name = "IS_RELEASED")
    private Boolean released = Boolean.FALSE;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RELEASED_DATE", nullable = false)
    private Date releasedDate;

    @Column(name = "IS_CANCELLED")
    private Boolean cancelled = Boolean.FALSE;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CANCELLED_DATE", nullable = false)
    private Date cancelledDate;

    @Column(name = "WORKFLOW")
    private Integer workflow;

    @Transient
    private Boolean startWorkflow = Boolean.FALSE;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.wf.WorkflowStatusType")})
    @Column(name = "STATUS_TYPE", nullable = false)
    private WorkflowStatusType statusType = WorkflowStatusType.UNDEFINED;

    @Transient
    private Boolean onHold = false;
    @Transient
    private String fromRev;
    @Transient
    private String toRev;
    @Transient
    private String printWorkFlow;
    @Transient
    private String printAnalyst;
    @Transient
    private String type;
    @Transient
    private String rule;

    @Transient
    private String createdPerson;

    @Transient
    private String modifiedPerson;
    @Transient
    private String workflowSettingStatus = "NONE";
    @Transient
    private String workflowSettingStatusType = "UNDEFINED";
    @Transient
    private Boolean finishWorkflow = false;
    @Transient
    private Boolean cancelWorkflow = false;

    public PLMECO() {
        super.setChangeType(ChangeType.ECO);
    }


}
