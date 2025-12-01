package com.cassinisys.erp.model.common;

import com.cassinisys.erp.model.core.ERPObject;
import com.cassinisys.erp.model.core.ObjectType;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by reddy on 7/18/15.
 */
@Entity
@Table(name = "ERP_ADDRESS")
@PrimaryKeyJoinColumn(name = "ADDRESS_ID")
@ApiObject(group = "COMMON")
public class ERPAddress extends ERPObject implements Serializable {

	@ApiObjectField(required = true)
	private ERPAddressType addressType;
	@ApiObjectField(required = true)
	private String addressText;
	@ApiObjectField
	private String city;
	@ApiObjectField(required = true)
	private ERPState state;
	@ApiObjectField(required = true)
	private String district;
	@ApiObjectField(required = true)
	private ERPCountry country;
	@ApiObjectField(required = true)
	private String pincode;

	public ERPAddress() {
		super.setObjectType(ObjectType.ADDRESS);
	}

	@ManyToOne
	@JoinColumn(name = "ADDRESS_TYPE", nullable = false)
	public ERPAddressType getAddressType() {
		return addressType;
	}

	public void setAddressType(ERPAddressType addressType) {
		this.addressType = addressType;
	}

	@Column(name = "ADDRESS_TEXT")
	public String getAddressText() {
		return addressText;
	}

	public void setAddressText(String addressText) {
		this.addressText = addressText;
	}

	@Column(name = "CITY")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "DISTRICT")
	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "STATE", nullable = false)
	public ERPState getState() {
		return state;
	}

	public void setState(ERPState state) {
		this.state = state;
	}

	@ManyToOne
	@JoinColumn(name = "COUNTRY", nullable = false)
	public ERPCountry getCountry() {
		return country;
	}

	public void setCountry(ERPCountry country) {
		this.country = country;
	}

	@Column(name = "PINCODE")
	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
}
