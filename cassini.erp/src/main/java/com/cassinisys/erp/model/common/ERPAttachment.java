package com.cassinisys.erp.model.common;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import com.cassinisys.erp.converters.CustomDateSerializer;
import com.cassinisys.erp.model.core.ObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "ERP_ATTACHMENT")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "COMMON")
public class ERPAttachment implements Serializable {

	private static final long serialVersionUID = 1L;
	@ApiObjectField(required = true)
	private Integer id;
	@ApiObjectField(required = true)
	private ObjectType objectType;
	@ApiObjectField(required = true)
	private Integer objectId;
	@ApiObjectField(required = true)
	private String name;
	@ApiObjectField(required = true)
	private Integer size;
	@ApiObjectField(required = true)
	private Date addedOn;
	@ApiObjectField(required = true)
	private Integer addedBy;

	@Id
	@SequenceGenerator(name = "ATTACHMENT_ID_GEN", sequenceName = "ATTACHMENT_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ATTACHMENT_ID_GEN")
	@Column(name = "ATTACHMENT_ID", nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
	public Integer getObjectId() {
		return objectId;
	}

	public void setObjectId(Integer objectId) {
		this.objectId = objectId;
	}

	@Column(name = "NAME", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "SIZE", nullable = false)
	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	@JsonSerialize(using = CustomDateSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ADDED_ON", nullable = false)
	public Date getAddedOn() {
		return addedOn;
	}

	public void setAddedOn(Date addedOn) {
		this.addedOn = addedOn;
	}

	@Column(name = "ADDED_BY", nullable = false)
	public Integer getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(Integer addedBy) {
		this.addedBy = addedBy;
	}

}
