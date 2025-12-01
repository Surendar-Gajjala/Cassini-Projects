package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.plm.model.cm.VarianceEffectivityType;
import com.cassinisys.plm.model.cm.VarianceFor;
import com.cassinisys.plm.model.cm.VarianceType;

/**
 * Created by CassiniSystems on 09-06-2020.
 */
public class VarianceCriteria extends Criteria {

	VarianceType varianceType;
	String varianceNumber;
	String description;
	String searchQuery;
	String title;
	String originator;
	String status;
	VarianceFor varianceFor;
	VarianceEffectivityType effectivityType;

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

	public String getVarianceNumber() {
		return varianceNumber;
	}

	public void setVarianceNumber(String varianceNumber) {
		this.varianceNumber = varianceNumber;
	}

	public VarianceType getVarianceType() {
		return varianceType;
	}

	public void setVarianceType(VarianceType varianceType) {
		this.varianceType = varianceType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOriginator() {
        return originator;
    }

    public void setOriginator(String originator) {
        this.originator = originator;
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public VarianceFor getVarianceFor() {
		return varianceFor;
	}

	public void setVarianceFor(VarianceFor varianceFor) {
		this.varianceFor = varianceFor;
	}

	public VarianceEffectivityType getEffectivityType() {
		return effectivityType;
	}

	public void setEffectivityType(VarianceEffectivityType effectivityType) {
		this.effectivityType = effectivityType;
	}
	
}
