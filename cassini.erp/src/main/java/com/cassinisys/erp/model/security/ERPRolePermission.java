package com.cassinisys.erp.model.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by reddy on 9/8/15.
 */
@Entity
@Table(name = "ERP_ROLEPERMISSION")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "SECURITY")
public class ERPRolePermission implements Serializable {
    @ApiObjectField(required = true)
    private RolePermissionId id;

    @EmbeddedId
    public RolePermissionId getId() {
        return id;
    }

    public void setId(RolePermissionId id) {
        this.id = id;
    }


}
