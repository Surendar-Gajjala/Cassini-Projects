package com.cassinisys.plm.model.pqm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.plm.PLMObjectType;
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

/**
 * Created by subramanyam on 03-06-2020.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PQM_INSPECTION_PLAN_REVISION")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PQMInspectionPlanRevision extends CassiniObject {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PLAN")
    private PQMInspectionPlan plan;

    @Column(name = "REVISION")
    private String revision;

    @OneToOne
    @JoinColumn(name = "LIFECYCLE_PHASE")
    private PLMLifeCyclePhase lifeCyclePhase;

    @Column(name = "WORKFLOW")
    private Integer workflow;

    @Column(name = "STATUS")
    private String status;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.wf.WorkflowStatusType")})
    @Column(name = "STATUS_TYPE")
    private WorkflowStatusType statusType = WorkflowStatusType.UNDEFINED;

    @Column(name = "IS_RELEASED")
    private Boolean released = Boolean.FALSE;

    @Column(name = "IS_REJECTED")
    private Boolean rejected = Boolean.FALSE;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RELEASED_DATE")
    private Date releasedDate;

    @Transient
    private Boolean startWorkflow = Boolean.FALSE;
    @Transient
    private List<PQMInspectionPlanHistory> statusHistory = new ArrayList<>();


    public PQMInspectionPlanRevision() {
        super.setObjectType(PLMObjectType.INSPECTIONPLANREVISION);
    }

    @Transient
    private String workflowSettingStatus = "NONE";
    @Transient
    private String workflowSettingStatusType = "UNDEFINED";


}
