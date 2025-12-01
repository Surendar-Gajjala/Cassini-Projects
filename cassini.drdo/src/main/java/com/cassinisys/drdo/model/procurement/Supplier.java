package com.cassinisys.drdo.model.procurement;

import com.cassinisys.platform.model.common.Address;
import com.cassinisys.platform.model.common.State;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;

/**
 * Created by subramanyam reddy on 10-12-2018.
 */
@Entity
@Table(name = "SUPPLIER")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "DRDO")
public class Supplier extends CassiniObject {

    @ApiObjectField
    @Column(name = "SUPPLIER_NAME")
    private String supplierName;

    @ApiObjectField
    @Column(name = "SUPPLIER_CODE")
    private String supplierCode;

    @ApiObjectField
    @Column(name = "DESCRIPTION")
    private String description;

    @ApiObjectField
    @Column(name = "CONTACT_PERSON")
    private String contactPerson;

    @ApiObjectField
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @ApiObjectField
    @Column(name = "EMAIL")
    private String email;

    @ApiObjectField
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ADDRESS")
    private Address address;

    @Transient
    private State state;

    public Supplier() {
        super(ObjectType.SUPPLIER);
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
