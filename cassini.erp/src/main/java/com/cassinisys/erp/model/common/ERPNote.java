package com.cassinisys.erp.model.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import com.cassinisys.erp.model.core.ERPObject;
import com.cassinisys.erp.model.core.ObjectType;

@Entity
@Table(name = "ERP_NOTE")
@PrimaryKeyJoinColumn(name = "NOTE_ID")
@ApiObject(group = "COMMON")
public class ERPNote extends ERPObject {

	private static final long serialVersionUID = 1L;
	@ApiObjectField(required = true)
	private ObjectType objectType;
	@ApiObjectField(required = true)
	private Integer object;
	@ApiObjectField(required = true)
	private String title;
	@ApiObjectField
	private String details;

	public ERPNote() {
		super.setObjectType(ObjectType.NOTE);
	}

	@Type(type = "com.cassinisys.erp.converters.EnumUserType", parameters = { @org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.erp.model.core.ObjectType") })
	@Column(name = "OBJECT_TYPE", nullable = false)
	public ObjectType getObjectType() {
		return objectType;
	}

	public void setObjectType(ObjectType objectType) {
		this.objectType = objectType;
	}

	@Column(name = "OBJECT_ID", nullable = false)
	public Integer getObject() {
		return object;
	}

	public void setObject(Integer object) {
		this.object = object;
	}

	@Column(name = "TITLE", nullable = false)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "DETAILS")
	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

}
