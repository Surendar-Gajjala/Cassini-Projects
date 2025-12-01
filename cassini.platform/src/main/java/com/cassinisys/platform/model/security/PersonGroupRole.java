package com.cassinisys.platform.model.security;

import com.cassinisys.platform.model.common.PersonGroup;
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
@Table(name = "PERSONGROUPROLE")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonGroupRole implements Serializable {

    @Id
    @SequenceGenerator(name = "PERSONGROUPROLE_ID_GEN", sequenceName = "PERSONGROUPROLE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PERSONGROUPROLE_ID_GEN")
    @Column(name = "ROWID", nullable = false)
    private Integer rowId;


    @Column(name = "GROUP_ID", nullable = false)
    private Integer groupId;

    @Column(name = "ROLE", nullable = false)
    private Integer roleId;

    @Transient
    private PersonGroup personGroup;


}
