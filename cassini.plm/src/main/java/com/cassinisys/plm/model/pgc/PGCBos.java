package com.cassinisys.plm.model.pgc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by GSR on 25-11-2020.
 */
@Entity
@Table(name = "PGC_BOS")
@JsonIgnoreProperties(ignoreUnknown = true)
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class PGCBos implements Serializable {

    @Id
    @SequenceGenerator(name = "BOS_ID_GEN", sequenceName = "BOS_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOS_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "PART")
    private Integer part;

}

