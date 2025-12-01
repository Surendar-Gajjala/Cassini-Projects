package com.cassinisys.is.model.pm;

import com.cassinisys.platform.model.common.Person;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by CassiniSystems on 14-05-2019.
 */
@Entity
@Table(name = "IS_PROJECTPERSON_ORGANIZATION")
@ApiObject(name = "PM")
public class ISProjectPersonOrganization implements Serializable {

    @Id
    @SequenceGenerator(name = "PROJECTPERSONORGANIZATION_ID_GEN", sequenceName = "PROJECTPERSONORGANIZATION_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROJECTPERSONORGANIZATION_ID_GEN")
    @Column(name = "ROWID", nullable = false)
    @ApiObjectField(required = true)
    private Integer rowId;

    @Column(name = "PROJECT")
    private Integer project;

    @Column(name = "PERSON")
    private Integer person;

    @Column(name = "NODE_NAME")
    private String nodeName;

    @Column(name = "PARENT")
    private Integer parentId;

    @Column(name = "NODE")
    private Integer node;

    @Column(name = "PROJECTROLE")
    private Integer role;

    @Transient
    private Person projectPerson;

    @Transient
    private ISProjectRole projectRole;

    @Transient
    private Integer projectPersonId;

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

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getNode() {
        return node;
    }

    public void setNode(Integer node) {
        this.node = node;
    }

    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }

    public Person getProjectPerson() {
        return projectPerson;
    }

    public void setProjectPerson(Person projectPerson) {
        this.projectPerson = projectPerson;
    }

    public ISProjectRole getProjectRole() {
        return projectRole;
    }

    public void setProjectRole(ISProjectRole projectRole) {
        this.projectRole = projectRole;
    }

    public Integer getProjectPersonId() {
        return projectPersonId;
    }

    public void setProjectPersonId(Integer projectPersonId) {
        this.projectPersonId = projectPersonId;
    }
}
