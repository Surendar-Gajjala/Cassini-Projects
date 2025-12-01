package com.cassinisys.tm.model;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by CassiniSystems on 01-08-2016.
 */
@Entity
@Table(name ="BED_ASSIGNEDTO")
@ApiObject(name = "TM")
public class TMBedAssignedTo implements Serializable {

    @Id
    @SequenceGenerator(name = "ROW_ID_GEN", sequenceName = "ROW_ID_SEQ",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator ="ROW_ID_GEN" )
    @Column(name = "ROWID", nullable = false)
    @ApiObjectField(required = true)
    private Integer rowId;

    @Column(name = "BED", nullable = false)
    @ApiObjectField(required = true)
    private Integer bed;

    @Column(name = "PERSON", nullable = false)
    @ApiObjectField(required = true)
    private Integer person;

    public Integer getRowId() {
        return rowId;
    }

    public void setRowId(Integer rowId) {
        this.rowId = rowId;
    }

    public Integer getbed() {
        return bed;
    }

    public void setbed(Integer bed) {
        this.bed = bed;
    }

    public Integer getPerson() {
        return person;
    }

    public void setPerson(Integer person) {
        this.person = person;
    }
}
