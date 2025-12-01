package com.cassinisys.is.model.workflow;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by reddy on 5/30/17.
 */
@Entity
@Table(name = "IS_WORKFLOWSTATUSACTIONHISTORY")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "PLM")
public class ISWorkflowStatusActionHistory implements Serializable {
    @Id
    @SequenceGenerator(name = "WORKFLOWSTATUSASSIGNMENT_ID_GEN", sequenceName = "WORKFLOWSTATUSASSIGNMENT_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WORKFLOWSTATUSASSIGNMENT_ID_GEN")
    @Column(name = "ID", nullable = false)
    @ApiObjectField(required = true)
    private Integer id;

    @ApiObjectField
    @Column(name = "WORKFLOW")
    private Integer workflow;

    @ApiObjectField
    @Column(name = "STATUS")
    private Integer status;

    @ApiObjectField
    @Column(name = "ASSIGNMENT")
    private Integer assignment;

    @ApiObjectField
    @Column(name = "ACTION")
    private String action;

    @ApiObjectField
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIMESTAMP")
    private Date timestamp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWorkflow() {
        return workflow;
    }

    public void setWorkflow(Integer workflow) {
        this.workflow = workflow;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAssignment() {
        return assignment;
    }

    public void setAssignment(Integer assignment) {
        this.assignment = assignment;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
