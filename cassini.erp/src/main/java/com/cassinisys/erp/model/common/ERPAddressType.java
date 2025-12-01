package com.cassinisys.erp.model.common;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

@Entity
@Table(name = "ERP_ADDRESSTYPE")
@ApiObject(group = "COMMON")
public class ERPAddressType implements Serializable {

	@ApiObjectField(required = true)
	private Integer typeId;
	@ApiObjectField(required = true)
	private String name;
	@ApiObjectField
	private String description;

	@Id
	@SequenceGenerator(name = "TYPE_ID_GEN", sequenceName = "ADDRESSTYPE_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TYPE_ID_GEN")
	@Column(name = "TYPE_ID")
	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	@Column(name = "NAME", unique = true, nullable = false)
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

}
