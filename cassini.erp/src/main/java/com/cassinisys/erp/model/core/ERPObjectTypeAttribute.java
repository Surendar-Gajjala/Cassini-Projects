package com.cassinisys.erp.model.core;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import com.cassinisys.erp.model.common.DataType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by reddy on 7/18/15.
 */

@Entity
@Table(name = "ERP_OBJECTTYPEATTRIBUTE")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "CORE")
public class ERPObjectTypeAttribute implements Serializable {

	@ApiObjectField(required = true)
	private Integer id;
	@ApiObjectField(required = true)
	private DataType dataType;
	@ApiObjectField(required = true)
	private String name;
	@ApiObjectField
	private String description;
	@ApiObjectField
	private boolean required;
	@ApiObjectField(required = true)
	private ObjectType objectType;
	@ApiObjectField(required = true)
	private Set<ERPLov> listOfValues;

	@Id
	@SequenceGenerator(name = "ATTRIBUTE_ID_GEN", sequenceName = "ATTRIBUTE_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ATTRIBUTE_ID_GEN")
	@Column(name = "ATTRIBUTE_ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "DATA_TYPE")
	@Type(type = "com.cassinisys.erp.converters.EnumUserType", parameters = { @org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.erp.model.common.DataType") })
	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
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

	@Column(name = "REQUIRED")
	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	@Column(name = "OBJECT_TYPE", nullable = false)
	@Type(type = "com.cassinisys.erp.converters.EnumUserType", parameters = { @org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.erp.model.core.ObjectType") })
	public ObjectType getObjectType() {
		return objectType;
	}

	public void setObjectType(ObjectType objectType) {
		this.objectType = objectType;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "ERP_ATTRIBUTELOV", joinColumns = @JoinColumn(name = "ATTRIBUTE_ID"), inverseJoinColumns = @JoinColumn(name = "LOV_ID"))
	public Set<ERPLov> getListOfValues() {
		return listOfValues;
	}

	public void setListOfValues(Set<ERPLov> listOfValues) {
		this.listOfValues = listOfValues;
	}

}
