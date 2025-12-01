package com.cassinisys.platform.model.security;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.Login;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by lakshmi on 16-01-2017.
 */
@Entity
@Data
@Table(name = "PERSONROLE")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonRole implements Serializable {

    @Id
    @SequenceGenerator(name = "PERSONROLE_ID_GEN", sequenceName = "PERSONROLE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PERSONROLE_ID_GEN")
    @Column(name = "ROWID", nullable = false)
    private Integer rowId;

    @Column(name = "PERSON", nullable = false)
    private Integer personId;

    @Column(name = "ROLE", nullable = false)
    private Integer roleId;

    @Transient
    private Login login;

    @Transient
    private Person person;

}
