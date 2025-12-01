package com.cassinisys.platform.model.security;

import com.cassinisys.platform.model.common.Person;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * Created by lakshmi on 10/18/2016.
 */

@Entity
@Table(name = "LOGIN_SECURITY_PERMISSION")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper=false)
public class LoginSecurityPermission {

    @Id
    @SequenceGenerator(name = "LOGINSECURITYPERMISSION_ID_GEN", sequenceName = "LOGINSECURITYPERMISSION_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOGINSECURITYPERMISSION_ID_GEN")
    @Column(name = "PERMISSION_ID")
    private Integer permissionId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PERSON", nullable = false)
    private Person person;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "OBJECT_TYPE")
    private String objectType;

    @Column(name = "SUB_TYPE")
    private String subType;

    @Column(name = "PRIVILEGE")
    private String privilege;

    @Column(name = "ATTRIBUTE_GROUP")
    private String attributeGroup;

    @Column(name = "ATTRIBUTE")
    private String attribute;

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
}