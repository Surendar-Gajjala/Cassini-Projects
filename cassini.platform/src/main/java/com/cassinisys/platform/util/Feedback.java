package com.cassinisys.platform.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Feedback {

    private String summary;
    private String description;
    private Integer priority;
    private Integer number;
    @JsonProperty("custom_fields")
    private CustomFields customerFields;
    @JsonProperty("notification_list")
    private String notificationList;
    @JsonProperty("assigned_to_id")
    private String assignedToId;
    private String id;

    private String createdDate;
    private String updatedDate;
    private String completedDate;

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(String completedDate) {
        this.completedDate = completedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String assignedTo;
    private String status;

    public String getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(String notificationList) {
        this.notificationList = notificationList;
    }

    public String getAssignedToId() {
        return assignedToId;
    }

    public void setAssignedToId(String assignedToId) {
        this.assignedToId = assignedToId;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CustomFields getCustomerFields() {
        return customerFields;
    }

    public void setCustomerFields(CustomFields customerFields) {
        this.customerFields = customerFields;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }
}
