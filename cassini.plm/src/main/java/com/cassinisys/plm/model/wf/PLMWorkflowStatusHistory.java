package com.cassinisys.plm.model.wf;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by reddy on 5/30/17.
 */
@Entity
@Data
@Table(name = "PLM_WORKFLOWSTATUSHISTORY")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMWorkflowStatusHistory implements Serializable {
    @Id
    @SequenceGenerator(name = "WORKFLOWSTATUSASSIGNMENT_ID_GEN", sequenceName = "WORKFLOWSTATUSASSIGNMENT_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WORKFLOWSTATUSASSIGNMENT_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "WORKFLOW")
    private Integer workflow;

    @Column(name = "STATUS")
    private Integer status;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIMESTAMP")
    private Date timestamp;

    @Column(name = "HOLD")
    private Boolean hold = Boolean.FALSE;

    @Column(name = "UNHOLD")
    private Boolean unhold = Boolean.FALSE;

    @Column(name = "DEMOTED")
    private Boolean demoted = Boolean.FALSE;

    @Column(name = "ASSIGNMENTS")
    private String assignments = "[]";

    @Column(name = "NOTES")
    private String notes;

    @Transient
    private List<PLMWorkFlowStatusAssignment> workflowStatusAssignments = new ArrayList<>();

    @Transient
    private List<PLMWorkflowStatusActionHistory> statusActionHistory = new ArrayList<>();

    @Transient
    private List<PLMWorkFlowStatusApprover> statusApprovers = new ArrayList<>();

    @Transient
    private List<PLMWorkFlowStatusAcknowledger> statusAcknowledgers = new ArrayList<>();

    @Transient
    private PLMWorkflowStatus statusObject;


}
