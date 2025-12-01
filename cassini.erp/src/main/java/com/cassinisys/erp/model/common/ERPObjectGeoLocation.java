package com.cassinisys.erp.model.common;

import com.cassinisys.erp.model.core.ERPLocationAwareObject;
import com.cassinisys.erp.model.core.ObjectType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by reddy on 10/31/15.
 */
@Entity
@Table(name = "ERP_OBJECTGEOLOCATION")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "CORE")
public class ERPObjectGeoLocation implements Serializable {
    @ApiObjectField(required = true)
    private Integer id;
    @ApiObjectField(required = true)
    private ERPLocationAwareObject object;
    @ApiObjectField(required = true)
    private ObjectType objectType;
    @ApiObjectField(required = true)
    private double latitude;
    @ApiObjectField(required = true)
    private double longitude;

    @Id
    @Column(name = "OBJECT_ID")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonBackReference
    @MapsId
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "OBJECT_ID", nullable = false)
    public ERPLocationAwareObject getObject() {
        return object;
    }

    public void setObject(ERPLocationAwareObject object) {
        this.object = object;
    }

    @Column(name = "OBJECT_TYPE", nullable = false)
    @Type(type = "com.cassinisys.erp.converters.EnumUserType",
            parameters = { @org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.erp.model.core.ObjectType") })
    public ObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(ObjectType objectType) {
        this.objectType = objectType;
    }

    @Column(name = "LATITUDE", nullable = false)
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Column(name = "LONGITUDE", nullable = false)
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
