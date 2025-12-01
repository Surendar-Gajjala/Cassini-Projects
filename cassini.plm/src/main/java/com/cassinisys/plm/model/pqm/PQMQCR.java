package com.cassinisys.plm.model.pqm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
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
@Table(name = "PQM_QCR")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PQMQCR extends CassiniObject {

    @Column(name = "QCR_NUMBER")
    private String qcrNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "QCR_TYPE")
    private PQMQCRType qcrType;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.pqm.QCRFor")})
    @Column(name = "QCR_FOR", nullable = true)
    private QCRFor qcrFor = QCRFor.PR;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "WORKFLOW")
    private Integer workflow;

    @Column(name = "QUALITY_ADMINISTRATOR")
    private Integer qualityAdministrator;

    @Column(name = "STATUS")
    private String status = "NONE";

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.wf.WorkflowStatusType")})
    @Column(name = "STATUS_TYPE")
    private WorkflowStatusType statusType = WorkflowStatusType.UNDEFINED;

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
    private String qcrForPrint;
    @Transient
    private String createdPerson;
    @Transient
    private String modifiedPerson;

    public PQMQCR() {
        super.setObjectType(PLMObjectType.QCR);
    }

    @Transient
    private String workflowSettingStatus = "NONE";
    @Transient
    private String workflowSettingStatusType = "UNDEFINED";


}
