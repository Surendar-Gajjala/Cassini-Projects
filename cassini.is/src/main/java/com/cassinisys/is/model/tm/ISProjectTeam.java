package com.cassinisys.is.model.tm;
/* Model for  ISProjectTeam */

import com.cassinisys.is.model.pm.ISProject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.security.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "IS_PROJECTTEAM")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "TM")
public class ISProjectTeam implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "ROW_ID_GEN",
            sequenceName = "ROW_ID_SEQ",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "ROW_ID_GEN")
    @Column(name = "ROWID", nullable = false)
    @ApiObjectField(required = true)
    private Integer id;

    @MapsId("projectId")
    @ManyToOne
    @JoinColumn(name = "PROJECT", nullable = false)
    @ApiObjectField(required = true)
    private ISProject project;

    @MapsId("personId")
    @ManyToOne
    @JoinColumn(name = "PERSON", nullable = false)
    @ApiObjectField(required = true)
    private Person person;

    @MapsId("roleId")
    @ManyToOne
    @JoinColumn(name = "ROLE", nullable = false)
    @ApiObjectField(required = true)
    private Role role;

    @Column(name = "REPORTS_TO")
    @ApiObjectField(required = true)
    private Integer reportsTo;

    public ISProjectTeam() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ISProject getProject() {
        return project;
    }

    public void setProject(ISProject project) {
        this.project = project;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Integer getReportsTo() {
        return reportsTo;
    }

    public void setReportsTo(Integer reportsTo) {
        this.reportsTo = reportsTo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ISProjectTeam other = (ISProjectTeam) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
