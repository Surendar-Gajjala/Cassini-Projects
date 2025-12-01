package com.cassinisys.plm.model.pm;

import com.cassinisys.platform.model.common.Person;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by smukka on 02-06-2022.
 */

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_PROGRAM_RESOURCE")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMProgramResource implements Serializable {

    @Id
    @SequenceGenerator(name = "ROW_ID_GEN", sequenceName = "ROW_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROW_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "PROGRAM")
    private Integer program;

    @Column(name = "PERSON")
    private Integer person;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.pm.ProgramResourceType")})
    @Column(name = "TYPE")
    private ProgramResourceType type = ProgramResourceType.PERSON;

    @Column(name = "ROLE")
    private String role;

    @Transient
    private String personName;

    @Transient
    private String email;

    @Transient
    private String phoneMobile;

    public PLMProgramResource() {
    }

}
