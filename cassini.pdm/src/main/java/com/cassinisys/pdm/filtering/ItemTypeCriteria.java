package com.cassinisys.pdm.filtering;

import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by Surendar Reddy on 27-01-2017.
 */
public class ItemTypeCriteria extends Criteria {

	String name;
	Integer parentType;
	private boolean freeTextSearch = false;
	private String searchQuery;

	public Integer getParentType() {
		return parentType;
	}

	public void setParentType(Integer parentType) {
		this.parentType = parentType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public boolean isFreeTextSearch() {
		return freeTextSearch;
	}

	public void setFreeTextSearch(boolean freeTextSearch) {
		this.freeTextSearch = freeTextSearch;
	}

	public String getSearchQuery() {
		return searchQuery;
	}

	public void setSearchQuery(String searchQuery) {
		this.searchQuery = searchQuery;
	}
}
