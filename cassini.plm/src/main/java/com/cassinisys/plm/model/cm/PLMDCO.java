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
 * Created by subramanyam on 03-06-2020.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_DCO")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMDCO extends PLMChange {

    @Column(name = "DCO_TYPE")
    private Integer dcoType;

    @Column(name = "DCO_NUMBER")
    private String dcoNumber;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "REASON_FOR_CHANGE")
    private String reasonForChange;

    @Column(name = "CHANGE_ANALYST")
    private Integer changeAnalyst;

    @Column(name = "STATUS")
    private String status = "NONE";

    @Column(name = "IS_RELEASED")
    private Boolean isReleased = Boolean.FALSE;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RELEASED_DATE")
    private Date releasedDate;

    @Column(name = "WORKFLOW")
    private Integer workflow;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.wf.WorkflowStatusType")})
    @Column(name = "STATUS_TYPE", nullable = false)
    private WorkflowStatusType statusType = WorkflowStatusType.UNDEFINED;

    @Transient
    private Integer workflowDfId;

    @Transient
    private Boolean startWorkflow = Boolean.FALSE;

    @Transient
    private String printWorkFlow;
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
    private String urgencyPrint;
    @Transient
    private String rule;
    @Transient
    private String workflowSettingStatus = "NONE";
    @Transient
    private String workflowSettingStatusType = "UNDEFINED";

    public PLMDCO() {
        super.setChangeType(ChangeType.DCO);
    }


}
