package com.cassinisys.erp.model.store;

import com.cassinisys.erp.model.core.ERPObject;
import com.cassinisys.erp.model.core.ObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "ERP_STORE")
@PrimaryKeyJoinColumn(name = "STORE_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "STORE")
public class ERPStore extends ERPObject {

    private static final long serialVersionUID = 1L;


    @ApiObjectField(required = true)
    private String storeName;

    @ApiObjectField(required = true)
    private String description;

    @ApiObjectField
    private String locationName;

    public ERPStore() {
        super.setObjectType(ObjectType.STORE);
    }

    @Column(name = "STORE_NAME", nullable = false)
    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "LOCATION_NAME")
    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
