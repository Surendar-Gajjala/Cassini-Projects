package com.cassinisys.is.model.pm;

import com.cassinisys.is.model.im.IssuePriority;
import com.cassinisys.is.model.im.IssueStatus;

/**
 * Created by subramanyam on 24-02-2020.
 */
public class ProjectProblemDto {

    private Integer id;

    private String title;

    private String assignedTo;

    private String assignedToNumber;

    private String createdByNumber;

    private String createdBy;

    private String type;

    private String description;

    private IssuePriority priority;

    private IssueStatus status;

    private Integer media = 0;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IssuePriority getPriority() {
        return priority;
    }

    public void setPriority(IssuePriority priority) {
        this.priority = priority;
    }

    public String getAssignedToNumber() {
        return assignedToNumber;
    }

    public void setAssignedToNumber(String assignedToNumber) {
        this.assignedToNumber = assignedToNumber;
    }

    public String getCreatedByNumber() {
        return createdByNumber;
    }

    public void setCreatedByNumber(String createdByNumber) {
        this.createdByNumber = createdByNumber;
    }

    public IssueStatus getStatus() {
        return status;
    }

    public void setStatus(IssueStatus status) {
        this.status = status;
    }

    public Integer getMedia() {
        return media;
    }

    public void setMedia(Integer media) {
        this.media = media;
    }
}
