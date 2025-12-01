package com.cassinisys.erp.model.security;

import com.cassinisys.erp.config.RoleEntityListener;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by reddy on 9/8/15.
 */
@Entity
@Table(name = "ERP_ROLE")
@JsonIgnoreProperties(ignoreUnknown = true)
@EntityListeners(RoleEntityListener.class)
@ApiObject(group = "SECURITY")
public class ERPRole implements Serializable {
    @ApiObjectField(required = true)
    private Integer id;
    @ApiObjectField(required = true)
    private String name;
    @ApiObjectField(required = false)
    private String description;
    @ApiObjectField
    private List<ERPPermission> permissions;

    @Id
    @SequenceGenerator(name = "ROLE_ID_GEN", sequenceName = "ROLE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROLE_ID_GEN")
    @Column(name = "ROLE_ID")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Transient
    public List<ERPPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<ERPPermission> permissions) {
        this.permissions = permissions;
    }
}
