package com.cassinisys.is.model.pm;
/**
 * Model for ISCustomer
 */

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.platform.model.core.CassiniObject;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * The class is for ISCustomer
 */
@Entity
@Table(name = "IS_CUSTOMER")
@PrimaryKeyJoinColumn(name = "CUSTOMER_ID")
@ApiObject(name = "PM")
public class ISCustomer extends CassiniObject {

    private static final long serialVersionUID = 1L;

    @Column(name = "CUSTOMER_TYPE", nullable = false)
    @ApiObjectField(required = true)
    private Integer customerType;

    @Column(name = "NAME")
    @ApiObjectField(required = true)
    private String name;

    @Column(name = "DESCRIPTION")
    @ApiObjectField
    private String description;

    @Column(name = "OFFICE_PHONE")
    @ApiObjectField
    private String officePhone;

    @Column(name = "OFFICE_FAX")
    @ApiObjectField
    private String officeFax;

    @Column(name = "OFFICE_EMAIL")
    @ApiObjectField
    private String officeEmail;

    @Column(name = "CONTACT_PERSON", nullable = false)
    @ApiObjectField(required = true)
    private Integer contactPerson;

    @Column(name = "ADDRESS", nullable = false)
    private Integer address;

    /**
     * The methods getters and setters are used to get and set values of different classes and datatypes
     */
    public ISCustomer() {
        super(ISObjectType.CUSTOMER);
    }

    public Integer getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Integer customerType) {
        this.customerType = customerType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getOfficeFax() {
        return officeFax;
    }

    public void setOfficeFax(String officeFax) {
        this.officeFax = officeFax;
    }

    public String getOfficeEmail() {
        return officeEmail;
    }

    public void setOfficeEmail(String officeEmail) {
        this.officeEmail = officeEmail;
    }

    public Integer getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(Integer contactPerson) {
        this.contactPerson = contactPerson;
    }

    public Integer getAddress() {
        return address;
    }

    public void setAddress(Integer address) {
        this.address = address;
    }
}
