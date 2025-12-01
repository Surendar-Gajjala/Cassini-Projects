package com.cassinisys.erp.model.common;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "ERP_COUNTRY")
@PrimaryKeyJoinColumn(name = "COUNTRY_ID")
@ApiObject(group = "COMMON")
public class ERPCountry extends ERPObject implements Serializable {

	@ApiObjectField(required = true)
	private String name;
	@ApiObjectField
	private String shortName;

	public ERPCountry() {
		super.setObjectType(ObjectType.COUNTRY);
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
