package com.cassinisys.tm.model;

import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by CassiniSystems on 01-08-2016.
 */
@Entity
@Table(name = "SUITE")
@ApiObject(name = "TM")
public class TMAccommodationSuit implements Serializable {

    @Id
    @SequenceGenerator(name = "SUITE_ID_GEN", sequenceName = "SUITE_ID_SEQ",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SUITE_ID_GEN")
    @Column(name = "SUITE_ID", nullable = false)
    @ApiObjectField(required = true)
    private Integer suitId;

    @Column(name = "ACCOMMODATION", nullable = false)
    @ApiObjectField(required = true)
    private Integer accommodation;

    @Column(name = "NAME", nullable = false)
    @ApiObjectField(required = true)
    private String name;

    @Column(name = "DESCRIPTION", nullable = false)
    @ApiObjectField(required = true)
    private String description;

    public Integer getSuitId() {
        return suitId;
    }

    public void setSuitId(Integer suitId) {
        this.suitId = suitId;
    }

    public Integer getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Integer accommodation) {
        this.accommodation = accommodation;
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
