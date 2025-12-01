package com.cassinisys.tm.model;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by CassiniSystems on 01-08-2016.
 */
@Entity
@Table(name ="SUITE")
@ApiObject(name = "TM")
public class TMSuit implements Serializable {

    @Id
    @SequenceGenerator(name = "SUITE_ID_GEN", sequenceName = "SUITE_ID_SEQ",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator ="SUITE_ID_GEN" )
    @Column(name = "ROWID", nullable = false)
    @ApiObjectField(required = true)
    private Integer rowId;

    @Column(name = "SUITE", nullable = false)
    @ApiObjectField(required = true)
    private Integer suite;

    @Column(name = "PERSON", nullable = false)
    @ApiObjectField(required = true)
    private Integer person;

    public Integer getRowId() {
        return rowId;
    }

    public void setRowId(Integer rowId) {
        this.rowId = rowId;
    }

    public Integer getSuite() {
        return suite;
    }

    public void setSuite(Integer suite) {
        this.suite = suite;
    }

    public Integer getPerson() {
        return person;
    }

    public void setPerson(Integer person) {
        this.person = person;
    }
}
