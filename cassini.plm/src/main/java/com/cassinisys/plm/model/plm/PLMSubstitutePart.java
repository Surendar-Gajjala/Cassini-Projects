package com.cassinisys.plm.model.plm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by subramanyam on 13-12-2020.
 */
@Entity
@Data
@Table(name = "PLM_SUBSTITUTEPART")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMSubstitutePart implements Serializable {

    @Id
    @SequenceGenerator(name = "SUBSTITUTE_PART_ID_GEN", sequenceName = "SUBSTITUTE_PART_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SUBSTITUTE_PART_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "PARENT")
    private Integer parent;

    @Column(name = "PART")
    private Integer part;

    @Column(name = "REPLACEMENT_PART")
    private Integer replacementPart;
}
