package com.cassinisys.is.model.tm;
/* Model for  ISProjectTask */

import com.cassinisys.is.model.pm.ISProjectPerson;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "IS_PROJECTTASK")
@PrimaryKeyJoinColumn(name = "TASK_ID")
@ApiObject(name = "TM")
public class ISProjectTask extends ISTask {

    private static final long serialVersionUID = 1L;

    @Column(name = "PROJECT", nullable = false)
    @ApiObjectField(required = true)
    private Integer project;

    @Transient
    private List<ISTaskCompletionHistory> taskCompletionHistories;

    @Transient
    private List<ISProjectTask> projectTasks;

    @Transient
    private List<ISProjectPerson> projectPersons;

    public ISProjectTask() {
    }

    public List<ISProjectPerson> getProjectPersons() {
        return projectPersons;
    }

    public void setProjectPersons(List<ISProjectPerson> projectPersons) {
        this.projectPersons = projectPersons;
    }

    public List<ISProjectTask> getProjectTasks() {
        return projectTasks;
    }

    public void setProjectTasks(List<ISProjectTask> projectTasks) {
        this.projectTasks = projectTasks;
    }

    public List<ISTaskCompletionHistory> getTaskCompletionHistories() {
        return taskCompletionHistories;
    }

    public void setTaskCompletionHistories(List<ISTaskCompletionHistory> taskCompletionHistories) {
        this.taskCompletionHistories = taskCompletionHistories;
    }

    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }

}
