package com.cassinisys.is.model.pm;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by CassiniSystems on 14-05-2019.
 */

@Entity
@Table(name = "IS_PROJECT_PERSONROLE")
@PrimaryKeyJoinColumn(name = "ROWID")
@ApiObject(name = "PM")
public class ISProjectPersonRole implements Serializable {

    @Id
    @SequenceGenerator(name = "PROJECTPERSONROLE_ID_GEN", sequenceName = "PROJECTPERSONROLE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROJECTPERSONROLE_ID_GEN")
    @Column(name = "ROWID", nullable = false)
    @ApiObjectField(required = true)
    private Integer rowId;

    @Column(name = "PERSON")
    @ApiObjectField(required = true)
    private Integer person;

    @Column(name = "PROJECT")
    @ApiObjectField(required = true)
    private Integer project;

    @Column(name = "ROLE")
    @ApiObjectField(required = true)
    private Integer role;

    /**
     * The methods getters and setters are used to get and set values of different classes and datatypes
     */
    public Integer getRowId() {
        return rowId;
    }

    public void setRowId(Integer rowId) {
        this.rowId = rowId;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
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
