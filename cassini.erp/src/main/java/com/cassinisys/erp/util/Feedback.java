package com.cassinisys.erp.util;

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

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }


}
