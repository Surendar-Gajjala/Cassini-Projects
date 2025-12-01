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
@Table(name = "PQM_NCR")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PQMNCR extends CassiniObject {


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "NCR_TYPE")
    private PQMNCRType ncrType;

    @Column(name = "NCR_NUMBER")
    private String ncrNumber;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "REPORTED_BY")
    private Integer reportedBy;

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

    @Column(name = "IS_RELEASED")
    private Boolean released = Boolean.FALSE;

    @Column(name = "IS_IMPLEMENTED")
    private Boolean isImplemented = Boolean.FALSE;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "IMPLEMENTED_DATE")
    private Date implementedDate;

    @Column(name = "INSPECTION")
    private Integer inspection;

    @Transient
    private Boolean startWorkflow = Boolean.FALSE;
    @Transient
    private String printAnalyst;
    @Transient
    private String type;
    @Transient
    private String inspectionName;
    @Transient
    private String createdPerson;
    @Transient
    private String modifiedPerson;
    @Transient
    private String reportedByPerson;

    public PQMNCR() {
        super.setObjectType(PLMObjectType.NCR);
    }

    @Transient
    private String workflowSettingStatus = "NONE";
    @Transient
    private String workflowSettingStatusType = "UNDEFINED";


}
