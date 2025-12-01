package com.cassinisys.plm.model.pgc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by GSR on 25-11-2020.
 */
@Entity
@Table(name = "PGC_ITEM_SPECIFICATION")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PGCItemSpecification implements Serializable {

    @Id
    @SequenceGenerator(name = "ITEMSPECIFICATION_ID_GEN", sequenceName = "ITEMSPECIFICATION_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ITEMSPECIFICATION_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "ITEM")
    private Integer item;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SPECIFICATION")
    private PGCSpecification specification;
}

