package com.cassinisys.platform.model.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "SECURITY_PERMISSION")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = false)
public class SecurityPermission {

    @Id
    @Column(name = "PERMISSION_ID")
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "OBJECT_TYPE")
    private String objectType;

    @Column(name = "SUB_TYPE")
    private String subType;

    @Column(name = "SUB_TYPE_ID")
    private Integer subTypeId;

    @Column(name = "PRIVILEGE")
    private String privilege;

    @Column(name = "ATTRIBUTE")
    private String attribute;

    @Column(name = "ATTRIBUTE_GROUP")
    private String attributeGroup;

    @Column(name = "CRITERIA")
    private String criteria;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.platform.model.security.PrivilegeType")})
    @Column(name = "PRIVILEGE_TYPE", nullable = true)
    private PrivilegeType privilegeType = PrivilegeType.GRANTED;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.platform.model.security.ModuleType")})
    @Column(name = "MODULE", nullable = true)
    private ModuleType module = ModuleType.ALL;

    @Transient
    private String person;

    @Transient
    private Boolean usedPermission = Boolean.FALSE;

    @Transient
    private Boolean isExternal = Boolean.FALSE;

}
