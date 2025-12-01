package com.cassinisys.erp.model.core;

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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "ERP_LOV")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "CORE")
public class ERPLov implements Serializable {

	@ApiObjectField(required = true)
	private Integer lovId;
	@ApiObjectField
	private String type;
	@ApiObjectField(required = true)
	private String name;
	@ApiObjectField(required = true)
	private String description;
	@ApiObjectField(required = true)
	private String values;

	@Id
	@SequenceGenerator(name = "LOV_ID_GEN", sequenceName = "LOV_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOV_ID_GEN")
	@Column(name = "LOV_ID")
	public Integer getLovId() {
		return lovId;
	}

	public void setLovId(Integer lovId) {
		this.lovId = lovId;
	}

	@Column(name = "TYPE")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "NAME")
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

	@Column(name = "VALUES")
	public String getValues() {
		return values;
	}

	public void setValues(String values) {
		this.values = values;
	}

}
