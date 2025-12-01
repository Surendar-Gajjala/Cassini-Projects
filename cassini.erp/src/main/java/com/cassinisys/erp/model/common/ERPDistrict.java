package com.cassinisys.erp.model.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import com.cassinisys.erp.model.core.ERPObject;
import com.cassinisys.erp.model.core.ObjectType;

/**
 * Created by reddy on 8/4/15.
 */
@Entity
@Table(name = "ERP_DISTRICT")
@PrimaryKeyJoinColumn(name = "DISTRICT_ID")
@ApiObject(group = "COMMON")
public class ERPDistrict extends ERPObject {

	@ApiObjectField(required = true)
	private Integer state;
	@ApiObjectField(required = true)
	private String name;

	public ERPDistrict() {
		super.setObjectType(ObjectType.DISTRICT);
	}

	@Column(name = "STATE")
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
