package com.cassinisys.is.model.tm;

import com.cassinisys.is.model.pm.ISProject;
import com.cassinisys.is.model.pm.ISProjectSite;
import com.cassinisys.is.model.pm.ISWbs;
import com.cassinisys.platform.model.common.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subra on 24-09-2018.
 */
public class AssignedToTaskDto {

    private ISProjectTask task;

    private ISProject project;

    private ISProjectSite site;

    private ISWbs wbs;

    private Person assignedBy;

    private Person assignedTo;

    private Double unitsCompleted = 0.0;

    private List<ISTaskCompletionHistory> taskCompletionHistories = new ArrayList<>();

    public ISProjectTask getTask() {
        return task;
    }

    public void setTask(ISProjectTask task) {
        this.task = task;
    }

    public ISProject getProject() {
        return project;
    }

    public void setProject(ISProject project) {
        this.project = project;
    }

    public ISProjectSite getSite() {
        return site;
    }

    public void setSite(ISProjectSite site) {
        this.site = site;
    }

    public ISWbs getWbs() {
        return wbs;
    }

    public void setWbs(ISWbs wbs) {
        this.wbs = wbs;
    }

    public Person getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(Person assignedBy) {
        this.assignedBy = assignedBy;
    }

    public Person getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Person assignedTo) {
        this.assignedTo = assignedTo;
    }

    public List<ISTaskCompletionHistory> getTaskCompletionHistories() {
        return taskCompletionHistories;
    }

    public void setTaskCompletionHistories(List<ISTaskCompletionHistory> taskCompletionHistories) {
        this.taskCompletionHistories = taskCompletionHistories;
    }

    public Double getUnitsCompleted() {
        return unitsCompleted;
    }

    public void setUnitsCompleted(Double unitsCompleted) {
        this.unitsCompleted = unitsCompleted;
    }
}
