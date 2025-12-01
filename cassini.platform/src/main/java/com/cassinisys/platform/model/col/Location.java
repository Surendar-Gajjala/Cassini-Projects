package com.cassinisys.platform.model.col;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by subramanyam reddy on 29-11-2018.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "LOCATION")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Location implements Serializable {

    @Id
    @SequenceGenerator(name = "LOCATION_ID_GEN", sequenceName = "LOCATION_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOCATION_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "LATITUDE")
    private Double latitude;

    @Column(name = "LONGITUDE")
    private Double longitude;

    @Column(name = "UPLOAD_FROM")
    private String uploadFrom;

    @Transient
    private String description;


}
