package com.cassinisys.erp.model.crm;

import com.cassinisys.erp.model.common.ERPCountry;
import com.cassinisys.erp.model.common.ERPState;
import com.cassinisys.erp.model.core.ERPObject;
import com.cassinisys.erp.model.core.ObjectType;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ERP_SALESREGION")
@PrimaryKeyJoinColumn(name = "REGION_ID")
@ApiObject(group = "CRM")
public class ERPSalesRegion extends ERPObject implements Serializable {

	@ApiObjectField(required = true)
	private ERPCountry country;
	@ApiObjectField(required = true)
	private ERPState state;
	@ApiObjectField(required = true)
	private String name;
	@ApiObjectField(required = true)
	private String district;


	public ERPSalesRegion() {
		super.setObjectType(ObjectType.SALESREGION);
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COUNTRY_ID", nullable = false)
	public ERPCountry getCountry() {
		return country;
	}

	public void setCountry(ERPCountry country) {
		this.country = country;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "STATE_ID", nullable = false)
	public ERPState getState() {
		return state;
	}

	public void setState(ERPState state) {
		this.state = state;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DISTRICT")
	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

}
