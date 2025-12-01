package com.cassinisys.tm.model;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Rajabrahmachary on 05-07-2016.
 */
@Entity
@Table(name = "PROJECT_TASK")
@PrimaryKeyJoinColumn(name = "TASK_ID")
@ApiObject(name = "TM")
public class TMProjectTask extends CassiniObject {

    @Column(name = "PROJECT", nullable = false)
    @ApiObjectField(required = true)
    private Integer project;

    @Column(name = "NAME", nullable = false)
    @ApiObjectField(required = true)
    private String name;

    @Column(name = "DESCRIPTION", nullable = false)
    @ApiObjectField(required = true)
    private String description;

    @Column(name = "SHIFT", nullable = false)
    @ApiObjectField(required = true)
    private Integer shift;

    @Column(name = "LOCATION", nullable = false)
    @ApiObjectField(required = true)
    private String location;

    /*@Column(name = "NOTE", nullable = true)
    private String note;*/

    @Column(name = "pending_reason", nullable = true)
    private Integer pendingReason;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.tm.model.TaskStatus")})
    @Column(name = "STATUS", nullable = false)
    @ApiObjectField(required = true)
    private TaskStatus status;
    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ASSIGNED_DATE", nullable = false)
    @ApiObjectField(required = true)
    private Date assignedDate = new Date();
    @Column(name = "ASSIGNED_TO", nullable = false)
    @ApiObjectField(required = true)
    private Integer assignedTo;
    @Column(name = "VERIFIED_BY", nullable = false)
    @ApiObjectField(required = true)
    private Integer verifiedBy;
    @Column(name = "APPROVED_BY", nullable = false)
    @ApiObjectField(required = true)
    private Integer approvedBy;

    protected TMProjectTask() {
        super(TMObjectType.PROJECTTASK);
    }

    public Integer getPendingReason() {
        return pendingReason;
    }

    public void setPendingReason(Integer pendingReason) {
        this.pendingReason = pendingReason;
    }

    /*public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }*/

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getShift() {
        return shift;
    }

    public void setShift(Integer shift) {
        this.shift = shift;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Date getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(Date assignedDate) {
        this.assignedDate = assignedDate;
    }

    public Integer getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Integer assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Integer getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(Integer verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public Integer getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Integer approvedBy) {
        this.approvedBy = approvedBy;
    }


}
