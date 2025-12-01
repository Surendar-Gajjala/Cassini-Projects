package com.cassinisys.erp.model.production;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import com.cassinisys.erp.model.common.ERPAddress;
import com.cassinisys.erp.model.common.ERPPerson;
import com.cassinisys.erp.model.core.ERPObject;
import com.cassinisys.erp.model.core.ObjectType;

/**
 * Created by reddy on 7/22/15.
 */
@Entity
@Table(name = "ERP_SUPPLIER")
@PrimaryKeyJoinColumn(name = "SUPPLIER_ID")
@ApiObject(group = "PRODUCTION")
public class ERPSupplier extends ERPObject {

    @ApiObjectField(required = true)
    private String name;

    @ApiObjectField(required = true)
    private ERPAddress address;

    @ApiObjectField
    private String officePhone;

    @ApiObjectField
    private String officeFax;

    @ApiObjectField
    private String officeEmail;

    @ApiObjectField(required = true)
    private ERPPerson contactPerson;

    public ERPSupplier() {
        super.setObjectType(ObjectType.SUPPLIER);
    }

    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CONTACT_PERSON")
    public ERPPerson getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(ERPPerson contactPerson) {
        this.contactPerson = contactPerson;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ADDRESS")
    public ERPAddress getAddress() {
        return address;
    }

    public void setAddress(ERPAddress address) {
        this.address = address;
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
}
