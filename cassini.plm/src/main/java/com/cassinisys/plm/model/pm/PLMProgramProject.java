package com.cassinisys.plm.model.pm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by smukka on 15-06-2022.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_PROGRAM_PROJECT")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMProgramProject implements Serializable {
    @Id
    @SequenceGenerator(name = "ROW_ID_GEN", sequenceName = "ROW_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROW_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "PROGRAM")
    private Integer program;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PARENT")
    private Integer parent;

    @Column(name = "PROJECT")
    private Integer project;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.pm.ProgramProjectType")})
    @Column(name = "TYPE")
    private ProgramProjectType type = ProgramProjectType.GROUP;
}
