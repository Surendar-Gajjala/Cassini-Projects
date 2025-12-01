package com.cassinisys.erp.model.core;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import com.cassinisys.erp.converters.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by reddy on 7/18/15.
 */
@Entity
@Table(name = "ERP_OBJECTATTRIBUTE")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "CORE")
public class ERPObjectAttribute implements Serializable {

	@ApiObjectField(required = true)
	private ObjectAttributeId id;
	@ApiObjectField
	private String stringValue;
	@ApiObjectField
	private Integer integerValue;
	@ApiObjectField
	private Double doubleValue;
	@ApiObjectField
	private Date dateValue;
	@ApiObjectField
	private String singleListValue;
	@ApiObjectField
	private String multiListValue;
	@ApiObjectField
	private boolean booleanValue;

	@EmbeddedId
	public ObjectAttributeId getId() {
		return id;
	}

	public void setId(ObjectAttributeId id) {
		this.id = id;
	}

	@Column(name = "STRING_VALUE")
	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	@Column(name = "INTEGER_VALUE")
	public Integer getIntegerValue() {
		return integerValue;
	}

	public void setIntegerValue(Integer integerValue) {
		this.integerValue = integerValue;
	}

	@Column(name = "DOUBLE_VALUE")
	public Double getDoubleValue() {
		return doubleValue;
	}

	public void setDoubleValue(Double doubleValue) {
		this.doubleValue = doubleValue;
	}

	@Column(name = "DATE_VALUE")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = CustomDateSerializer.class)
	public Date getDateValue() {
		return dateValue;
	}

	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
	}

	@Column(name = "SINGLELIST_VALUE")
	public String getSingleListValue() {
		return singleListValue;
	}

	public void setSingleListValue(String singleListValue) {
		this.singleListValue = singleListValue;
	}

	@Column(name = "MULTILIST_VALUE")
	public String getMultiListValue() {
		return multiListValue;
	}

	public void setMultiListValue(String multiListValue) {
		this.multiListValue = multiListValue;
	}

	@Column(name = "BOOLEAN_VALUE")
	public boolean isBooleanValue() {
		return booleanValue;
	}

	public void setBooleanValue(boolean booleanValue) {
		this.booleanValue = booleanValue;
	}

	@Embeddable
	class ObjectAttributeId implements Serializable {
		private Integer objectId;
		private Integer attributeDef;

		@Column(name = "OBJECT_ID")
		public Integer getObjectId() {
			return objectId;
		}

		public void setObjectId(Integer objectId) {
			this.objectId = objectId;
		}

		@Column(name = "ATTRIBUTEDEF")
		public Integer getAttributeDef() {
			return attributeDef;
		}

		public void setAttributeDef(Integer attributeDef) {
			this.attributeDef = attributeDef;
		}

	}

}
