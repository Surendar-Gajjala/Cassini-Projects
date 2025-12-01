package com.cassinisys.platform.filtering;

import com.cassinisys.platform.model.security.ModuleType;
import com.cassinisys.platform.model.security.PrivilegeType;

/**
 * Created by CassiniSystems on 09-06-2020.
 */
public class SecurityPermissionCriteria extends Criteria {

	Integer id;
	String name;
	String description;
	String objectType;
	String subType;
	String privilege;
	String attribute;
	String attributeGroup;
	String criteria;
	PrivilegeType privilegeType;
	ModuleType module;
	private String searchQuery = "";

	public String getSearchQuery() {
		return searchQuery;
	}

	public void setSearchQuery(String searchQuery) {
		this.searchQuery = searchQuery;
	}

	public String getName() {
		return name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public String getPrivilege() {
		return privilege;
	}

	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}

	public String getAttributeGroup() {
		return attributeGroup;
	}

	public void setAttributeGroup(String attributeGroup) {
		this.attributeGroup = attributeGroup;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public PrivilegeType getPrivilegeType() {
		return privilegeType;
	}

	public void setPrivilegeType(PrivilegeType privilegeType) {
		this.privilegeType = privilegeType;
	}

	public ModuleType getModule() {
		return module;
	}

	public void setModule(ModuleType module) {
		this.module = module;
	}
}
