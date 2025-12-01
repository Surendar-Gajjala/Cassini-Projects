package com.cassinisys.plm.model.mes;

import com.cassinisys.platform.model.common.Person;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Suresh Cassini on 25-11-2020.
 */
@Entity
@Data
@Table(name = "MES_MANPOWER_CONTACT")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MESManpowerContact implements Serializable {

    @Id
    @SequenceGenerator(name = "ROW_ID_GEN", sequenceName = "ROW_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROW_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "MANPOWER")
    private Integer manpower;

    @Column(name = "CONTACT")
    private Integer contact;


    @Column(name = "ACTIVE")
    private Boolean active = Boolean.TRUE;

    @Transient
    private Person person;

    @Transient
    private Boolean newPerson = Boolean.FALSE;

   
}
