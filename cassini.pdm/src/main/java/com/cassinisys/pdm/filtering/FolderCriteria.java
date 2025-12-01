package com.cassinisys.pdm.filtering;

import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by subramanyamreddy on 27-Jan-17.
 */
public class FolderCriteria extends Criteria {

	String name;
	String Description;
	Integer vault;
	private boolean freeTextSearch = false;
	private String searchQuery;

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

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public Integer getVault() {
		return vault;
	}

	public void setVault(Integer vault) {
		this.vault = vault;
	}
}
