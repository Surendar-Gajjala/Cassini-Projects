package com.cassinisys.erp.model.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by reddy on 9/8/15.
 */
@Embeddable
@JsonIgnoreProperties(ignoreUnknown = true)
public class RolePermissionId implements Serializable {
    @ApiObjectField(required = true)
    private ERPRole role;
    @ApiObjectField(required = true)
    private ERPPermission permission;

    public RolePermissionId() {

    }

    public RolePermissionId(ERPRole role, ERPPermission permission) {
        this.role = role;
        this.permission = permission;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ROLE", nullable = false)
    public ERPRole getRole() {
        return role;
    }

    public void setRole(ERPRole role) {
        this.role = role;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PERMISSION", nullable = false)
    public ERPPermission getPermission() {
        return permission;
    }

    public void setPermission(ERPPermission permission) {
        this.permission = permission;
    }
}