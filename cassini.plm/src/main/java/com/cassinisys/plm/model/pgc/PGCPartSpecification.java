package com.cassinisys.plm.model.pgc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by GSR on 25-11-2020.
 */
@Entity
@Table(name = "PGC_PART_SPECIFICATION")
@JsonIgnoreProperties(ignoreUnknown = true)
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class PGCPartSpecification implements Serializable {

    @Id
    @SequenceGenerator(name = "PARTSPECIFICATION_ID_GEN", sequenceName = "PARTSPECIFICATION_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PARTSPECIFICATION_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "PART")
    private Integer part;

    @Column(name = "SPECIFICATION")
    private Integer specification;

    @Column(name = "COMPLIANT")
    private Boolean compliant;
}

