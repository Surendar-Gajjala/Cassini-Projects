package com.cassinisys.is.model.im;
/**
 * Model for ISIssue
 */

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.ObjectTypeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;

@Entity
@Table(name = "IS_ISSUE")
@PrimaryKeyJoinColumn(name = "ISSUE_ID")
@ApiObject(name = "IM")
public class ISIssue extends CassiniObject {

    private static final long serialVersionUID = 1L;

    @Column(name = "TARGET_OBJECT_ID", nullable = false)
    @ApiObjectField(required = true)
    private Integer targetObjectId;

    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.platform.model.core.ObjectType")})
    @Column(name = "TARGET_OBJECT_TYPE", nullable = false)
    @JsonDeserialize(using = ObjectTypeDeserializer.class)
    @ApiObjectField(required = true)
    private Enum targetObjectType;

    @Column(name = "TYPE", nullable = true)
    @ApiObjectField(required = true)
    private Integer type;

    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.is.model.im.IssuePriority")})
    @Column(name = "PRIORITY", nullable = false)
    @ApiObjectField(required = true)
    private IssuePriority priority = IssuePriority.LOW;

    @Column(name = "TITLE", nullable = false)
    @ApiObjectField(required = true)
    private String title;

    @Column(name = "DESCRIPTION")
    @ApiObjectField(required = true)
    private String description;

    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.is.model.im.IssueStatus")})
    @Column(name = "STATUS", nullable = false)
    @ApiObjectField(required = true)
    private IssueStatus status = IssueStatus.NEW;

    @Column(name = "ASSIGNED_TO")
    @ApiObjectField
    private Integer assignedTo;

    @Column(name = "TASK")
    @ApiObjectField
    private Integer task;

    @Column(name = "RESOLUTION")
    @ApiObjectField
    private String resolution;

    @Transient
    private Integer media = 0;

    public ISIssue() {
        super(ISObjectType.PROBLEM);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getTargetObjectId() {
        return targetObjectId;
    }

    public void setTargetObjectId(Integer targetObjectId) {
        this.targetObjectId = targetObjectId;
    }

    public Enum getTargetObjectType() {
        return targetObjectType;
    }

    public void setTargetObjectType(Enum targetObjectType) {
        this.targetObjectType = targetObjectType;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public IssuePriority getPriority() {
        return priority;
    }

    public void setPriority(IssuePriority priority) {
        this.priority = priority;
    }

    public IssueStatus getStatus() {
        return status;
    }

    public void setStatus(IssueStatus status) {
        this.status = status;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Integer assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Integer getTask() {
        return task;
    }

    public void setTask(Integer task) {
        this.task = task;
    }

    public Integer getMedia() {
        return media;
    }

    public void setMedia(Integer media) {
        this.media = media;
    }
}
