package com.cassinisys.tm.model;

import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by CassiniSystems on 01-08-2016.
 */
@Entity
@Table(name = "ACCOMMODATION")
public class TMAccommodation implements Serializable {

    @Id
    @SequenceGenerator(name = "ACCOMMODATION_ID_GEN", sequenceName = "ACCOMMODATION_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACCOMMODATION_ID_GEN")
    @Column(name = "ACCOM_ID", nullable = false)
    @ApiObjectField(required = true)
    private Integer id;

    @Column(name = "NAME", nullable = false)
    @ApiObjectField(required = true)
    private String name;

    @Column(name = "DESCRIPTION")
    @ApiObjectField(required = true)
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}
