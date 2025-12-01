package com.cassinisys.platform.model.common;

import com.cassinisys.platform.config.GroupEntityListener;
import com.cassinisys.platform.model.security.Permission;
import com.cassinisys.platform.model.security.SecurityPermission;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "PERSONGROUP")
@JsonIgnoreProperties(ignoreUnknown = true)
@EntityListeners(GroupEntityListener.class)
public class PersonGroup implements Serializable {

    @Id
    @SequenceGenerator(name = "PERSONGROUP_ID_GEN", sequenceName = "PERSONGROUP_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PERSONGROUP_ID_GEN")
    @Column(name = "GROUP_ID", nullable = false)
    private Integer groupId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "personGroup", cascade = CascadeType.ALL)
    private Set<GroupMember> groupMember;

    @Column(name = "IS_ACTIVE")
    private boolean isActive = true;

    @Column(name = "EXTERNAL")
    private boolean external = false;

    @Column(name = "PARENT")
    private Integer parent;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "PROFILE")
    private Profile profile;

    @Transient
    private List<PersonGroup> groupChildren;

    @Transient
    private Integer level = 0;

    @Transient
    private List<Permission> permissions;
    @Transient
    private List<SecurityPermission> groupSecurityPermissions = new ArrayList<>();
    @Transient
    private List<GroupMember> groupMembers = new ArrayList<>();

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<GroupMember> getGroupMember() {
        return groupMember;
    }

    public void setGroupMember(Set<GroupMember> groupMember) {
        this.groupMember = groupMember;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isExternal() {
        return external;
    }

    public void setExternal(boolean external) {
        this.external = external;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public List<PersonGroup> getGroupChildren() {
        return groupChildren;
    }

    public void setGroupChildren(List<PersonGroup> groupChildren) {
        this.groupChildren = groupChildren;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<GroupMember> getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(List<GroupMember> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public List<SecurityPermission> getGroupSecurityPermissions() {
        return groupSecurityPermissions;
    }

    public void setGroupSecurityPermissions(List<SecurityPermission> groupSecurityPermissions) {
        this.groupSecurityPermissions = groupSecurityPermissions;
    }
}
