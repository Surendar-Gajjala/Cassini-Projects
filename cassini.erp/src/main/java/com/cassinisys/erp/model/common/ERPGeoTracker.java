package com.cassinisys.erp.model.common;

import com.cassinisys.erp.converters.CustomDateDeserializer;
import com.cassinisys.erp.converters.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by reddy on 10/10/15.
 */

@Entity
@Table(name = "ERP_GEOTRACKER")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "Common")
public class ERPGeoTracker implements Serializable {
    @ApiObjectField(required = true)
    private Integer id;
    @ApiObjectField(required = true)
    private Integer employee;
    @ApiObjectField(required = true)
    private double latitude;
    @ApiObjectField(required = true)
    private double longitude;
    @ApiObjectField(required = true)
    private Date timestamp;


    @Id
    @SequenceGenerator(name = "ROW_ID_GEN", sequenceName = "ROW_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROW_ID_GEN")
    @Column(name = "ROWID", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "EMPLOYEE_ID", nullable = false)
    public Integer getEmployee() {
        return employee;
    }

    public void setEmployee(Integer employee) {
        this.employee = employee;
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

    @Column(name = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
