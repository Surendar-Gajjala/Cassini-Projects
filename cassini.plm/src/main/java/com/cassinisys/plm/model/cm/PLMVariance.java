package com.cassinisys.plm.model.cm;

import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
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
@Table(name = "PLM_VARIANCE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = false)
public class PLMVariance extends PLMChange {

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.cm.VarianceType")})
    @Column(name = "VARIANCE_TYPE", nullable = true)
    private VarianceType varianceType;

    @Column(name = "VARIANCE_NUMBER", nullable = false)
    private String varianceNumber;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "REASON_FOR_VARIANCE")
    private String reasonForVariance;

    @Column(name = "CURRENT_REQUIREMENT")
    private String currentRequirement;

    @Column(name = "REQUIREMENT_DEVIATION")
    private String requirementDeviation;

    @Column(name = "STATUS")
    private String status = "NONE";

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.cm.VarianceFor")})
    @Column(name = "VARIANCE_FOR")
    private VarianceFor varianceFor = VarianceFor.ITEMS;

    @Column(name = "IS_RECURRING")
    private Boolean isRecurring = Boolean.FALSE;

    @Column(name = "ORIGINATOR")
    private Integer originator;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.cm.VarianceEffectivityType")})
    @Column(name = "EFFECTIVITY_TYPE")
    private VarianceEffectivityType effectivityType = VarianceEffectivityType.QUANTITY;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EFFECTIVE_FROM")
    private Date effectiveFrom;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EFFECTIVE_TO")
    private Date effectiveTo;

    @Column(name = "CORRECTIVE_ACTION")
    private String correctiveAction;

    @Column(name = "PREVENTIVE_ACTION")
    private String preventiveAction;

    @Column(name = "NOTES")
    private String notes;

    @Column(name = "WORKFLOW")
    private Integer workflow;

    @Transient
    private Boolean startWorkflow = Boolean.FALSE;

    @Transient
    private String printOriginator;
    @Transient
    private String type;

    @Transient
    private String createdPerson;

    @Transient
    private String modifiedPerson;

    @Transient
    private String waiverAndDeviationFor;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.wf.WorkflowStatusType")})
    @Column(name = "STATUS_TYPE", nullable = false)
    private WorkflowStatusType statusType = WorkflowStatusType.UNDEFINED;

    public PLMVariance() {
        super.setChangeType(ChangeType.VARIANCE);
    }

    @Transient
    private String workflowSettingStatus = "NONE";
    @Transient
    private String workflowSettingStatusType = "UNDEFINED";


}
