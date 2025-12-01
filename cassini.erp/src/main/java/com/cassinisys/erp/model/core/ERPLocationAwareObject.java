package com.cassinisys.erp.model.core;

import com.cassinisys.erp.model.common.ERPObjectGeoLocation;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;

/**
 * Created by reddy on 11/1/15.
 */
@Entity
@Table(name = "ERP_LOCATIONAWAREOBJECT")
@PrimaryKeyJoinColumn(name = "OBJECT_ID")
@ApiObject(group = "CORE")
public class ERPLocationAwareObject extends ERPObject {
    @ApiObjectField
    private ERPObjectGeoLocation geoLocation;

    @JsonManagedReference
    @OneToOne(mappedBy = "object", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    public ERPObjectGeoLocation getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(ERPObjectGeoLocation location) {
        this.geoLocation = location;
    }

}
