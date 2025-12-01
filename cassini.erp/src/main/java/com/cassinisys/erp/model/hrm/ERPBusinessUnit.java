package com.cassinisys.erp.model.hrm;

import com.cassinisys.erp.model.core.ERPObject;
import com.cassinisys.erp.model.core.ObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by reddy on 10/1/15.
 */
@Entity
@Table(name = "ERP_BUSINESSUNIT")
@JsonIgnoreProperties(ignoreUnknown = true)
@PrimaryKeyJoinColumn(name = "BUSINESSUNIT_ID")
@ApiObject(group = "HRM")
public class ERPBusinessUnit extends ERPObject {
    @ApiObjectField(required = true)
    private String name;
    @ApiObjectField
    private String description;

    public ERPBusinessUnit() {
        super.setObjectType(ObjectType.BUSINESSUNIT);
    }

    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
