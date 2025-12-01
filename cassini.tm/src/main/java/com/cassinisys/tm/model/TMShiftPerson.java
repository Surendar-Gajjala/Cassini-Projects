package com.cassinisys.tm.model;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Rajabrahmachary on 21-07-2016.
 */
@Entity
@Table(name = "SHIFT_PERSON")
@ApiObject(name = "TM")
public class TMShiftPerson implements Serializable {

    @Id
    @SequenceGenerator(name = "SHIFTPERSON_ID_GEN", sequenceName = "SHIFTPERSON_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SHIFTPERSON_ID_GEN")
    @Column(name = "ROWID", nullable = false)
    @ApiObjectField(required = true)
    private Integer rowId;

    @Column(name = "SHIFT", nullable = false)
    @ApiObjectField(required = true)
    private Integer shift;

    @Column(name = "PERSON", nullable = false)
    @ApiObjectField(required = true)
    private Integer person;


    public Integer getRowId() {
        return rowId;
    }

    public void setRowId(Integer rowId) {
        this.rowId = rowId;
    }

    public Integer getPerson() {
        return person;
    }

    public void setPerson(Integer person) {
        this.person = person;
    }

    public Integer getShift() {
        return shift;
    }

    public void setShift(Integer shift) {
        this.shift = shift;
    }
}
