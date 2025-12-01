package com.cassinisys.platform.model.security;

import com.cassinisys.platform.config.RoleEntityListener;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Entity
@Data
@Table(name = "ROLE")
@JsonIgnoreProperties(ignoreUnknown = true)
@EntityListeners(RoleEntityListener.class)
public class Role implements Serializable {
    @Id
    @SequenceGenerator(name = "ROLE_ID_GEN", sequenceName = "ROLE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROLE_ID_GEN")
    @Column(name = "ROLE_ID")
    private Integer id;
    
    @Column(name = "NAME")
    private String name;
    
    
    @Column(name = "DESCRIPTION")
    private String description;

    @Transient
    private List<PersonRole> personRoles;

    @Transient
    private List<PersonGroupRole> personGroupRoles;

    @Transient
    private List<Permission> permissions;

}
