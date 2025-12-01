package com.cassinisys.erp.model.common;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import com.cassinisys.erp.model.core.ERPObject;
import com.cassinisys.erp.model.core.ObjectType;

/**
 * Created by reddy on 7/18/15.
 */
@Entity
@Table(name = "ERP_STATE")
@PrimaryKeyJoinColumn(name = "STATE_ID")
@ApiObject(group = "COMMON")
public class ERPState extends ERPObject implements Serializable {

	@ApiObjectField(required = true)
	private ERPCountry country;
	@ApiObjectField(required = true)
	private String name;
	@ApiObjectField
	private String shortName;

	public ERPState() {
		super.setObjectType(ObjectType.STATE);
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COUNTRY", nullable = false)
	public ERPCountry getCountry() {
		return country;
	}

	public void setCountry(ERPCountry country) {
		this.country = country;
	}

	@Column(name = "NAME", unique = true, nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "SHORT_NAME")
	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

}
