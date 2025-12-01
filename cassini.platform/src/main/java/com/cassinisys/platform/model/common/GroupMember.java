package com.cassinisys.platform.model.common;


import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.model.security.Permission;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Anusha on 08-01-2016.
 */

@Entity
@Table(name = "GROUPMEMBER")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupMember implements Serializable {

    @Id
    @SequenceGenerator(name = "GROUPMEMBER_ID_GEN", sequenceName = "GROUPMEMBER_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GROUPMEMBER_ID_GEN")
    @Column(name = "ROWID", nullable = false)
    private Integer rowId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PERSON", nullable = false)
    private Person person;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "GROUP_ID", nullable = false)
    private PersonGroup personGroup;

    @Transient
    private Login login;

    @Transient
    private List<Permission> permissions;

    public Integer getRowId() {
        return rowId;
    }

    public void setRowId(Integer rowId) {
        this.rowId = rowId;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public PersonGroup getPersonGroup() {
        return personGroup;
    }

    public void setPersonGroup(PersonGroup personGroup) {
        this.personGroup = personGroup;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
}
