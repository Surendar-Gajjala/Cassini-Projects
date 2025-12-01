package com.cassinisys.erp.model.common;

import com.cassinisys.erp.model.core.ERPObject;
import com.cassinisys.erp.model.core.ObjectType;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by reddy on 7/18/15.
 */
@Entity
@Table(name = "ERP_PERSON")
@PrimaryKeyJoinColumn(name = "PERSON_ID")
@ApiObject(group = "COMMON")
public class ERPPerson extends ERPObject implements Serializable {

	@ApiObjectField (required = true)
	private ERPPersonType personType;
	@ApiObjectField
	private String title;
	@ApiObjectField
	private String firstName;
	@ApiObjectField
	private String lastName;
	@ApiObjectField
	private String middleName;
	@ApiObjectField
	private String phoneOffice;
	@ApiObjectField
	private String phoneMobile;
	@ApiObjectField
	private String email;
	@ApiObjectField
	private Set<ERPAddress> addresses;
	@ApiObjectField
	private ERPMobileDevice mobileDevice;
	
	public ERPPerson() {
		super.setObjectType(ObjectType.PERSON);
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="PERSON_TYPE")
	public ERPPersonType getPersonType() {
		return personType;
	}

	public void setPersonType(ERPPersonType personType) {
		this.personType = personType;
	}

	@Column(name = "TITLE")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "FIRST_NAME")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "LAST_NAME")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "MIDDLE_NAME")
	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	@Column(name = "PHONE_OFFICE")
	public String getPhoneOffice() {
		return phoneOffice;
	}

	public void setPhoneOffice(String phoneOffice) {
		this.phoneOffice = phoneOffice;
	}

	@Column(name = "PHONE_MOBILE")
	public String getPhoneMobile() {
		return phoneMobile;
	}

	public void setPhoneMobile(String phoneMobile) {
		this.phoneMobile = phoneMobile;
	}

	@Column(name = "EMAIL")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@ManyToMany(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
	@JoinTable(name = "ERP_PERSONADDRESS", joinColumns = @JoinColumn(name = "PERSON_ID"), inverseJoinColumns = @JoinColumn(name = "ADDRESS_ID"))
	public Set<ERPAddress> getAddresses() {
		return addresses;
	}

	public void setAddresses(Set<ERPAddress> addresses) {
		this.addresses = addresses;
	}

	@OneToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name="MOBILE_DEVICE",insertable=true,
			updatable=true,nullable=true,unique=true)
	public ERPMobileDevice getMobileDevice() {
		return mobileDevice;
	}

	public void setMobileDevice(ERPMobileDevice mobileDevice) {
		this.mobileDevice = mobileDevice;
	}
}
