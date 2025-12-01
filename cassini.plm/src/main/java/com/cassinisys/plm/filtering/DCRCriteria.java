package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by CassiniSystems on 09-06-2020.
 */
public class DCRCriteria extends Criteria {

	Integer crType;
	String crNumber;
	String descriptionofChange;
	String searchQuery;
	String title;
	Integer type;
	String status;
	String urgency;
	Integer changeAnalyst;
	Integer requestedBy;
	Integer originator;
	String changeReasonType;

	public Integer getCrType() {
		return crType;
	}

	public void setCrType(Integer crType) {
		this.crType = crType;
	}

	public String getCrNumber() {
		return crNumber;
	}

	public void setCrNumber(String crNumber) {
		this.crNumber = crNumber;
	}

	public String getDescriptionofChange() {
		return descriptionofChange;
	}

	public void setDescriptionofChange(String descriptionofChange) {
		this.descriptionofChange = descriptionofChange;
	}

	public String getSearchQuery() {
		return searchQuery;
	}

	public void setSearchQuery(String searchQuery) {
		this.searchQuery = searchQuery;
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

	public Integer getChangeAnalyst() {
        return changeAnalyst;
    }

    public void setChangeAnalyst(Integer changeAnalyst) {
        this.changeAnalyst = changeAnalyst;
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
}
