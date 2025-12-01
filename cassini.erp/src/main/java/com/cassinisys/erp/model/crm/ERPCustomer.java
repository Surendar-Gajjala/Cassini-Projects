package com.cassinisys.erp.model.crm;

import com.cassinisys.erp.model.common.ERPAddress;
import com.cassinisys.erp.model.common.ERPPerson;
import com.cassinisys.erp.model.core.ERPLocationAwareObject;
import com.cassinisys.erp.model.core.ObjectType;
import com.cassinisys.erp.model.hrm.ERPBusinessUnit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by reddy on 7/18/15.
 */
@Entity
@Table(name = "ERP_CUSTOMER")
@PrimaryKeyJoinColumn(name = "CUSTOMER_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "CRM")
public class ERPCustomer extends ERPLocationAwareObject {
	@ApiObjectField
	private ERPBusinessUnit businessUnit;
	@ApiObjectField(required = true)
	private ERPCustomerType customerType;
	@ApiObjectField
	private ERPSalesRegion salesRegion;
	@ApiObjectField
	private ERPSalesRep salesRep;
	@ApiObjectField
	private ERPPerson contactPerson;
	@ApiObjectField
	private String name;
	@ApiObjectField
	private String officePhone;
	@ApiObjectField
	private String officeFax;
	@ApiObjectField
	private String officeEmail;
	@ApiObjectField
	private Boolean blacklisted = Boolean.FALSE;
	@ApiObjectField
	private Set<ERPAddress> customerAddresses;


	public ERPCustomer() {
		super.setObjectType(ObjectType.CUSTOMER);
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "BUSINESS_UNIT")
	public ERPBusinessUnit getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(ERPBusinessUnit businessUnit) {
		this.businessUnit = businessUnit;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne
	@JoinColumn(name = "CUSTOMER_TYPE", nullable = false)
	public ERPCustomerType getCustomerType() {
		return customerType;
	}

	public void setCustomerType(ERPCustomerType customerType) {
		this.customerType = customerType;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "REGION", nullable = false)
	public ERPSalesRegion getSalesRegion() {
		return salesRegion;
	}

	public void setSalesRegion(ERPSalesRegion salesRegion) {
		this.salesRegion = salesRegion;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "CONTACT_PERSON")
	public ERPPerson getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(ERPPerson person) {
		this.contactPerson = person;
	}

	@Column(name = "OFFICE_PHONE")
	public String getOfficePhone() {
		return officePhone;
	}

	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}

	@Column(name = "OFFICE_FAX")
	public String getOfficeFax() {
		return officeFax;
	}

	public void setOfficeFax(String officeFax) {
		this.officeFax = officeFax;
	}

	@Column(name = "OFFICE_EMAIL")
	public String getOfficeEmail() {
		return officeEmail;
	}

	public void setOfficeEmail(String officeEmail) {
		this.officeEmail = officeEmail;
	}

	@Column(name = "BLACKLISTED")
	public Boolean isBlacklisted() {
		return blacklisted;
	}

	public void setBlacklisted(Boolean blacklisted) {
		this.blacklisted = blacklisted;
	}

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "ERP_CUSTOMERADDRESS", joinColumns = @JoinColumn(name = "CUSTOMER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ADDRESS_ID"))
	public Set<ERPAddress> getCustomerAddresses() {
		return customerAddresses;
	}

	public void setCustomerAddresses(Set<ERPAddress> customerAddresses) {
		this.customerAddresses = customerAddresses;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "SALESREP", nullable = false)
	public ERPSalesRep getSalesRep() {
		return salesRep;
	}

	public void setSalesRep(ERPSalesRep salesRep) {
		this.salesRep = salesRep;
	}

}
