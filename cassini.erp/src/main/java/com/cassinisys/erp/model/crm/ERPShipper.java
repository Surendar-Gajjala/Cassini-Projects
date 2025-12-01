package com.cassinisys.erp.model.crm;

import com.cassinisys.erp.model.common.ERPAddress;
import com.cassinisys.erp.model.core.ERPObject;
import com.cassinisys.erp.model.core.ObjectType;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by reddy on 10/13/15.
 */
@Entity
@Table(name = "ERP_SHIPPER")
@PrimaryKeyJoinColumn(name = "SHIPPER_ID")
@ApiObject(group = "CRM")
public class ERPShipper extends ERPObject implements Serializable {
    @ApiObjectField (required = true)
    private String name;
    @ApiObjectField
    private ERPAddress address;
    @ApiObjectField
    private String officePhone;
    @ApiObjectField
    private String officeFax;
    @ApiObjectField
    private String officeEmail;

    public ERPShipper() {
        super();
        setObjectType(ObjectType.SHIPPER);
    }

    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="ADDRESS",nullable = false)
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
