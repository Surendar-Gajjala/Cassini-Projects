package com.cassinisys.is.model.tm;

/**
 * Created by subramanyam on 24-02-2020.
 */
public class ProjectTaskDto {

    private ISProjectTask task;

    private String assignedTo;

    private String phoneNumber;

    private String inspectedBy;

    private String inspectedByPhone;

    public ISProjectTask getTask() {
        return task;
    }

    public void setTask(ISProjectTask task) {
        this.task = task;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getInspectedBy() {
        return inspectedBy;
    }

    public void setInspectedBy(String inspectedBy) {
        this.inspectedBy = inspectedBy;
    }

    public String getInspectedByPhone() {
        return inspectedByPhone;
    }

    public void setInspectedByPhone(String inspectedByPhone) {
        this.inspectedByPhone = inspectedByPhone;
    }
}
