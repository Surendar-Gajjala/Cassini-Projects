package com.cassinisys.is.model.pm;
/* Model for ISProjectRole */

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.platform.model.core.CassiniObject;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "IS_PROJECTROLE")
@PrimaryKeyJoinColumn(name = "ROWID")
@ApiObject(name = "PM")
public class ISProjectRole extends CassiniObject {

    @Column(name = "ROLE", nullable = false)
    @ApiObjectField(required = true)
    private String role;

    @Column(name = "DESCRIPTION")
    @ApiObjectField(required = true)
    private String description;

    @Column(name = "PROJECT", nullable = false)
    @ApiObjectField(required = true)
    private Integer project;

    public ISProjectRole() {
        super(ISObjectType.ROLE);
    }

    /**
     * The methods getters and setters are used to get and set values of different classes and datatypes
     */
    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
