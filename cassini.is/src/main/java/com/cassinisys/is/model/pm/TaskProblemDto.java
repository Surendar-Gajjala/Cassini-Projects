package com.cassinisys.is.model.pm;

import com.cassinisys.is.model.im.IssuePriority;
import com.cassinisys.is.model.im.IssueStatus;

/**
 * Created by subramanyam on 25-02-2020.
 */
public class TaskProblemDto {

    private Integer id;

    private String title;

    private String assignedTo;

    private String reportedBy;

    private IssuePriority priority;

    private IssueStatus status;

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

    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
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
}
