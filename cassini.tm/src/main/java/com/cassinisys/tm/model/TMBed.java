package com.cassinisys.tm.model;

import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "BED")
public class TMBed implements Serializable {

    @Id
    @SequenceGenerator(name = "BED_ID_GEN", sequenceName = "BED_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BED_ID_GEN")
    @Column(name = "BED_ID", nullable = false)
    @ApiObjectField(required = true)
    private  Integer bedId;

    @Column(name = "SUITE", nullable = false)
    private  Integer suite;

    @Column(name = "NAME", nullable = false)
    private  String  name;

    @Column(name = "PERSON", nullable = false)
    private  Integer  assignedTo;

    public Integer getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Integer assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Integer getBedId() {
        return bedId;
    }

    public void setBedId(Integer bedId) {
        this.bedId = bedId;
    }

    public Integer getSuite() {
        return suite;
    }

    public void setSuite(Integer suite) {
        this.suite = suite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
