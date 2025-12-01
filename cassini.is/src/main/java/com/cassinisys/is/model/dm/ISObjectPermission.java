package com.cassinisys.is.model.dm;
/**
 * Model for ISObjectPermission
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;

@Entity
@Table(name = "IS_OBJECTPERMISSION")
@ApiObject(name = "DM")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ISObjectPermission {

    @Id
    @SequenceGenerator(name = "OBJECTPERMISSION_ID_GEN", sequenceName = "OBJECTPERMISSION_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OBJECTPERMISSION_ID_GEN")
    @Column(name = "ROW_ID", nullable = false)
    @ApiObjectField(required = true)
    private Integer rowId;

    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.is.model.dm.ObjectType")})
    @Column(name = "OBJECTTYPE", nullable = false)
    @ApiObjectField(required = true)
    private ObjectType objectType;

    @Column(name = " OBJECT_ID ")
    @ApiObjectField(required = true)
    private Integer objectId;

    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.is.model.dm.PermissionLevel")})
    @Column(name = "PERMISSION_LEVEL")
    @ApiObjectField(required = true)
    private PermissionLevel permissionLevel;

    @Column(name = "PERMISSION_ASSIGNEDTO")
    @ApiObjectField(required = true)
    private Integer permissionAssignedTo;

    @Type(type = "com.cassinisys.platform.util.converter.StringArrayType")
    @Column(name = "ACTION_TYPES")
    @ApiObjectField(required = true)
    private String[] actionTypes;

    /**
     * The methods getters and setters are used to get and set values of different classes and datatypes
     */
    public Integer getRowId() {
        return rowId;
    }

    public void setRowId(Integer rowId) {
        this.rowId = rowId;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(ObjectType objectType) {
        this.objectType = objectType;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public PermissionLevel getPermissionLevel() {
        return permissionLevel;
    }

    public void setPermissionLevel(PermissionLevel permissionLevel) {
        this.permissionLevel = permissionLevel;
    }

    public Integer getPermissionAssignedTo() {
        return permissionAssignedTo;
    }

    public void setPermissionAssignedTo(Integer permissionAssignedTo) {
        this.permissionAssignedTo = permissionAssignedTo;
    }

    public String[] getActionTypes() {
        return actionTypes;
    }

    public void setActionTypes(String[] actionTypes) {
        this.actionTypes = actionTypes;
    }
}
