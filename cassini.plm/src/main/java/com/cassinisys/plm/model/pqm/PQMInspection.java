package com.cassinisys.plm.model.pqm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.wf.WorkflowStatusType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PQM_INSPECTION")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PQMInspection extends CassiniObject {

    @Column(name = "INSPECTION_NUMBER")
    private String inspectionNumber;

    @Column(name = "INSPECTION_PLAN")
    private Integer inspectionPlan;

    @Column(name = "ASSIGNED_TO")
    private Integer assignedTo;

    @Column(name = "WORKFLOW")
    private Integer workflow;

    @Column(name = "STATUS")
    private String status;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.wf.WorkflowStatusType")})
    @Column(name = "STATUS_TYPE")
    private WorkflowStatusType statusType = WorkflowStatusType.UNDEFINED;

    @Column(name = "DEVIATION_SUMMARY")
    private String deviationSummary;

    @Column(name = "NOTES")
    private String notes;

    @Column(name = "IS_RELEASED")
    private Boolean released = Boolean.FALSE;

    @Transient
    private Boolean startWorkflow = Boolean.FALSE;
    @Transient
    private String createdPerson;
    @Transient
    private String modifiedPerson;
    @Transient
    private String assginedTo;
    @Transient
    private String inspectionPlanName;

    public PQMInspection() {
        super.setObjectType(PLMObjectType.INSPECTION);
    }

    @Transient
    private String workflowSettingStatus = "NONE";
    @Transient
    private String workflowSettingStatusType = "UNDEFINED";


}
