package com.cassinisys.plm.model.mes;

import com.cassinisys.platform.model.common.Person;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "MES_SHIFT_PERSON")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MESShiftPerson implements Serializable {

    @Id
    @SequenceGenerator(name = "SHIFT_PERSON_ID_GEN", sequenceName = "SHIFT_PERSON_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SHIFT_PERSON_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "SHIFT")
    private Integer shift;

    @Column(name = "PERSON")
    private Integer person;

    @Column(name = "NOTES")
    private String notes;

    @Transient
    private MESManpowerContact manpowerContact;

   
}
