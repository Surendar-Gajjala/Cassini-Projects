package com.cassinisys.plm.model.pqm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.cassinisys.plm.model.plm.PLMObjectType;
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
@Table(name = "PQM_PROBLEM_REPORT")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PQMProblemReport extends CassiniObject {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PR_TYPE")
    private PQMProblemReportType prType;

    @Column(name = "PR_NUMBER")
    private String prNumber;

    @Column(name = "PRODUCT")
    private Integer product;

    @Column(name = "INSPECTION")
    private Integer inspection;

    @Column(name = "PROBLEM")
    private String problem;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "STEPS_TO_REPRODUCE")
    private String stepsToReproduce;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.pqm.ReporterType")})
    @Column(name = "REPORTER_TYPE", nullable = true)
    private ReporterType reporterType = ReporterType.CUSTOMER;

    @Column(name = "REPORTED_BY")
    private Integer reportedBy;

    @Column(name = "OTHER_REPORTED")
    private String otherReported;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REPORTED_DATE")
    private Date reportedDate;

    @Column(name = "QUALITY_ANALYST")
    private Integer qualityAnalyst;

    @Column(name = "STATUS")
    private String status = "NONE";

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.wf.WorkflowStatusType")})
    @Column(name = "STATUS_TYPE")
    private WorkflowStatusType statusType = WorkflowStatusType.UNDEFINED;

    @Column(name = "WORKFLOW")
    private Integer workflow;

    @Column(name = "FAILURE_TYPE")
    private String failureType;

    @Column(name = "SEVERITY")
    private String severity;

    @Column(name = "DISPOSITION")
    private String disposition;

    @Column(name = "NOTES")
    private String notes;

    @Column(name = "IS_RELEASED")
    private Boolean released = Boolean.FALSE;

    @Column(name = "IS_IMPLEMENTED")
    private Boolean isImplemented = Boolean.FALSE;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "IMPLEMENTED_DATE")
    private Date implementedDate;

    @Transient
    private Boolean startWorkflow = Boolean.FALSE;

    @Transient
    private String printWorkFlow;
    @Transient
    private String printAnalyst;
    @Transient
    private String type;
    @Transient
    private String Qfor;
    @Transient
    private String productName;
    @Transient
    private String createdPerson;
    @Transient
    private String modifiedPerson;
    @Transient
    private String reportedByPerson;

    public PQMProblemReport() {
        super.setObjectType(PLMObjectType.PROBLEMREPORT);
    }

    @Transient
    private String workflowSettingStatus = "NONE";
    @Transient
    private String workflowSettingStatusType = "UNDEFINED";


}
