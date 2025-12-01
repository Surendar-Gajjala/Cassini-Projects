package com.cassinisys.platform.model.common;

import com.cassinisys.platform.model.core.ObjectType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "OBJECTGEOLOCATION")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectGeoLocation implements Serializable {
	
    @Id
    @Column(name = "OBJECT_ID")
    private Integer id;
    
    @JsonBackReference
    @MapsId
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "OBJECT_ID", nullable = false)
    private LocationAwareObject object;
    
    @Column(name = "OBJECT_TYPE", nullable = false)
    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = { @org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.platform.model.core.ObjectType") })
    private ObjectType objectType;
    
    @Column(name = "LATITUDE", nullable = false)
    private double latitude;
    
    @Column(name = "LONGITUDE", nullable = false)
    private double longitude;


}
