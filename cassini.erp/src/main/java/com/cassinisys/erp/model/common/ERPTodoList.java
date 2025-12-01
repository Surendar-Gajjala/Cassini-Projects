package com.cassinisys.erp.model.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import com.cassinisys.erp.model.core.ERPObject;
import com.cassinisys.erp.model.core.ObjectType;

@Entity
@Table(name = "ERP_TODOLIST")
@PrimaryKeyJoinColumn(name = "TODOLIST_ID")
@ApiObject(group = "COMMON")
public class ERPTodoList extends ERPObject {

	private static final long serialVersionUID = 1L;

	@ApiObjectField(required = true)
	private String name;
	@ApiObjectField
	private String description;
	@ApiObjectField(required = true)
	private Boolean disabled;

	public ERPTodoList() {
		super.setObjectType(ObjectType.TODOLIST);
	}

	@Column(name = "NAME", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "DISABLED", nullable = false)
	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

}
