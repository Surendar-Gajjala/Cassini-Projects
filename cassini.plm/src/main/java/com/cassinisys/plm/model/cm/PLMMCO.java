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
 * Created by subramanyamreddy on 023 23-Jun -17.
 */
@Entity
@Table(name = "PLM_MCO")
@PrimaryKeyJoinColumn(name = "MCO_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class PLMMCO extends PLMChange {

    @Column(name = "MCO_NUMBER", nullable = false)
    private String mcoNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MCO_TYPE")
    private PLMMCOType mcoType;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "CHANGE_ANALYST", nullable = false)
    private Integer changeAnalyst;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Column(name = "REASON_FOR_CHANGE", nullable = false)
    private String reasonForChange;

    @Column(name = "STATUS", nullable = true)
    private String status = "NONE";

    @Column(name = "IS_RELEASED")
    private Boolean released = Boolean.FALSE;

    @Column(name = "REJECTED")
    private Boolean rejected = Boolean.FALSE;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RELEASED_DATE", nullable = false)
    private Date releasedDate;

    @Column(name = "WORKFLOW")
    private Integer workflow;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.wf.WorkflowStatusType")})
    @Column(name = "STATUS_TYPE")
    private WorkflowStatusType statusType = WorkflowStatusType.UNDEFINED;

    @Column(name = "QCR")
    private Integer qcr;

    @Transient
    private Boolean startWorkflow = Boolean.FALSE;
    @Transient
    private Boolean onHold = false;
    @Transient
    private String printAnalyst;
    @Transient
    private String type;
    @Transient
    private String createdPerson;
    @Transient
    private String originatorPrint;
    @Transient
    private String requestedByPrint;
    @Transient
    private String modifiedPersonPrint;
    @Transient
    private String qcrPrint;
    @Transient
    private String fromRev;
    @Transient
    private String toRev;
    @Transient
    private String workflowSettingStatus = "NONE";
    @Transient
    private String workflowSettingStatusType = "UNDEFINED";
    @Transient
    private Boolean finishWorkflow = false;
    @Transient
    private Boolean cancelWorkflow = false;

    public PLMMCO() {
        super.setChangeType(ChangeType.MCO);
    }

}
