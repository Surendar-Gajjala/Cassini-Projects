package com.cassinisys.drdo.model.procurement;

import com.cassinisys.drdo.model.DRDOObjectType;
import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by subramanyam reddy on 10-12-2018.
 */
@Entity
@Table(name = "MANUFACTURER")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "DRDO")
public class Manufacturer extends CassiniObject {

    @ApiObjectField
    @Column(name = "NAME")
    private String name;

    @ApiObjectField
    @Column(name = "DESCRIPTION")
    private String description;

    @ApiObjectField
    @Column(name = "MFR_CODE")
    private String mfrCode;

    @ApiObjectField
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @ApiObjectField
    @Column(name = "EMAIL")
    private String email;

    public Manufacturer() {
        super(DRDOObjectType.MANUFACTURER);
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

    public String getMfrCode() {
        return mfrCode;
    }

    public void setMfrCode(String mfrCode) {
        this.mfrCode = mfrCode;
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
}
