package com.cassinisys.platform.model.common;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Nageshreddy on 28-01-2021.
 */
@Entity
@Table(name = "GROUP_PROFILE")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupProfile implements Serializable {

    @Id
    @SequenceGenerator(name = "GROUPPROFILE_ID_GEN", sequenceName = "GROUPPROFILE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GROUPPROFILE_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "PROFILE", nullable = false)
    private Profile profile;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "PERSON_GROUP", nullable = false)
    private PersonGroup personGroup;

    public GroupProfile() {
    }

    public GroupProfile(Profile profile, PersonGroup personGroup) {
        this.profile = profile;
        this.personGroup = personGroup;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public PersonGroup getPersonGroup() {
        return personGroup;
    }

    public void setPersonGroup(PersonGroup personGroup) {
        this.personGroup = personGroup;
    }
}
