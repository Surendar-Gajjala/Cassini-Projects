package com.cassinisys.is.model.workflow;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Rajabrahmachary on 19-05-2017.
 */
@Entity
@Table(name = "IS_WORKFLOWSTATUSASSIGNMENT")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "PLM")
public class ISWorkFlowStatusAssignment implements Serializable {

    @Id
    @SequenceGenerator(name = "WORKFLOWSTATUSASSIGNMENT_ID_GEN", sequenceName = "WORKFLOWSTATUSASSIGNMENT_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WORKFLOWSTATUSASSIGNMENT_ID_GEN")
    @Column(name = "ID", nullable = false)
    @ApiObjectField(required = true)
    private Integer id;

    @Column(name = "STATUS", nullable = false)
    @ApiObjectField(required = true)
    private Integer status;

    @Column(name = "ASSIGNMENT_TYPE", nullable = false)
    @ApiObjectField(required = true)
    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.is.model.workflow.WorkflowAssigementType")})
    private WorkflowAssigementType assignmentType;

    @Column(name = "PERSON", nullable = false)
    @ApiObjectField(required = true)
    private Integer person;

    @ApiObjectField(required = true)
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_NOTIFIED_ON")
    private Date lastNotifiedOn;

    @ApiObjectField(required = true)
    @Column(name = "COMMENTS")
    private String comments;

    public Date getLastNotifiedOn() {
        return lastNotifiedOn;
    }

    public void setLastNotifiedOn(Date lastNotifiedOn) {
        this.lastNotifiedOn = lastNotifiedOn;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public WorkflowAssigementType getAssignmentType() {
        return assignmentType;
    }

    public void setAssignmentType(WorkflowAssigementType assignmentType) {
        this.assignmentType = assignmentType;
    }

    public Integer getPerson() {
        return person;
    }

    public void setPerson(Integer person) {
        this.person = person;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String note) {
        this.comments = note;
    }

}
