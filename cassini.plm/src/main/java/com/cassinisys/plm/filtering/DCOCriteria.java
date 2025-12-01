package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by CassiniSystems on 09-06-2020.
 */
public class DCOCriteria extends Criteria {

	Integer dcoType;
	String dcoNumber;
	String description;
	String searchQuery;
	String title;
	Integer type;
	Integer changeAnalyst;
    String status;
	String urgency;
	Integer originator;
	Integer requestedBy;
	String changeReasonType;
	Integer crType;



	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSearchQuery() {
		return searchQuery;
	}

	public void setSearchQuery(String searchQuery) {
		this.searchQuery = searchQuery;
	}

	public String getDcoNumber() {
		return dcoNumber;
	}

	public void setDcoNumber(String dcoNumber) {
		this.dcoNumber = dcoNumber;
	}

	public Integer getDcoType() {
		return dcoType;
	}

	public void setDcoType(Integer dcoType) {
		this.dcoType = dcoType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getChangeAnalyst() {
        return changeAnalyst;
    }

    public void setChangeAnalyst(Integer changeAnalyst) {
        this.changeAnalyst = changeAnalyst;
    }
	
	public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

	public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

	public Integer getOriginator() {
        return originator;
    }

    public void setOriginator(Integer originator) {
        this.originator = originator;
    }

	public Integer getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(Integer requestedBy) {
        this.requestedBy = requestedBy;
    }

	public String getChangeReasonType() {
        return changeReasonType;
    }

    public void setChangeReasonType(String changeReasonType) {
        this.changeReasonType = changeReasonType;
    }

	public Integer getCrType() {
		return crType;
	}

	public void setCrType(Integer crType) {
		this.crType = crType;
	}
}
