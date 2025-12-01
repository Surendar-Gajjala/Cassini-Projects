package com.cassinisys.is.model.tm;

import java.io.Serializable;

/**
 * Created by swapna on 09/05/19.
 */
public class TaskStatusCountDTO implements Serializable {

    private Integer newTasks;

    private Integer inProgressTasks;

    private Integer assignedTasks;

    private Integer finishedTasks;

    public Integer getNewTasks() {
        return newTasks;
    }

    public void setNewTasks(Integer newTasks) {
        this.newTasks = newTasks;
    }

    public Integer getInProgressTasks() {
        return inProgressTasks;
    }

    public void setInProgressTasks(Integer inProgressTasks) {
        this.inProgressTasks = inProgressTasks;
    }

    public Integer getAssignedTasks() {
        return assignedTasks;
    }

    public void setAssignedTasks(Integer assignedTasks) {
        this.assignedTasks = assignedTasks;
    }

    public Integer getFinishedTasks() {
        return finishedTasks;
    }

    public void setFinishedTasks(Integer finishedTasks) {
        this.finishedTasks = finishedTasks;
    }
}
