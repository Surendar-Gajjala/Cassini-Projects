package com.cassinisys.erp.model.crm;

import com.cassinisys.erp.model.core.ERPObject;
import com.cassinisys.erp.model.core.ObjectType;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by reddy on 10/28/15.
 */
@Entity
@Table(name = "ERP_VEHICLE")
@PrimaryKeyJoinColumn(name = "VEHICLE_ID")
@ApiObject(group = "CRM")
public class ERPVehicle  extends ERPObject {
    @ApiObjectField (required = true)
    private String number;

    @ApiObjectField
    private String description;

    public ERPVehicle() {
        super.setObjectType(ObjectType.VEHICLE);
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
