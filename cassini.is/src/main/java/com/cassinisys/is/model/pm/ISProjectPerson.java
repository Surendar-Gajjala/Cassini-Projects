package com.cassinisys.is.model.pm;
/* Model for ISProjectPerson */

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Anusha on 06-04-2017.
 */
@Entity
@Table(name = "IS_PROJECTPERSON")
@ApiObject(name = "PM")
public class ISProjectPerson implements Serializable {

    @Id
    @SequenceGenerator(name = "PROJECTPERSON_ID_GEN", sequenceName = "PROJECTPERSON_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROJECTPERSON_ID_GEN")
    @Column(name = "ROWID", nullable = false)
    @ApiObjectField(required = true)
    private Integer rowId;

    @Column(name = "PROJECT")
    private Integer project;

    @Column(name = "PERSON")
    private Integer person;

    /**
     * The methods getters and setters are used to get and set values of different classes and datatypes
     */
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

    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }
}

