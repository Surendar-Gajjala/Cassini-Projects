package com.cassinisys.tm.model;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by CassiniSystems on 01-08-2016.
 */
@Entity
@Table(name = "DEPARTMENT_PERSON")
@ApiObject(name = "TM")
public class TMDepartmentPerson implements Serializable {

    @Id
    @SequenceGenerator(name = "DEPARTMENTPERSON_ID_GEN", sequenceName = "DEPARTMENTPERSON_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEPARTMENTPERSON_ID_GEN")
    private Integer rowId;

    @Column(name = "DEPARTMENT", nullable = false)
    @ApiObjectField(required = true)
     private  Integer department;

    @Column(name = "PERSON", nullable = false)
    @ApiObjectField(required = true)
    private  Integer person;

    public Integer getRowId() {
        return rowId;
    }

    public void setRowId(Integer rowId) {
        this.rowId = rowId;
    }

    public Integer getDepartment() {
        return department;
    }

    public void setDepartment(Integer department) {
        this.department = department;
    }

    public Integer getPerson() {
        return person;
    }

    public void setPerson(Integer person) {
        this.person = person;
    }
}
