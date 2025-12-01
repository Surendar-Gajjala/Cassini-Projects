package com.cassinisys.plm.model.pm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by swapna on 12/31/17.
 */

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_PROJECTMEMBER")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMProjectMember implements Serializable {

    @Id
    @SequenceGenerator(name = "PROJECTMEMBER_ID_GEN", sequenceName = "PROJECTMEMBER_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROJECTMEMBER_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column
    private Integer project;

    @Column
    private Integer person;

    @Column
    private String role;

    public PLMProjectMember() {
    }

}
