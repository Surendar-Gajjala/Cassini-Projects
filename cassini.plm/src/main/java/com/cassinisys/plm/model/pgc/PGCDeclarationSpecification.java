package com.cassinisys.plm.model.pgc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by GSR on 25-11-2020.
 */
@Entity
@Table(name = "PGC_DECLARATION_SPECIFICATION")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PGCDeclarationSpecification implements Serializable {

    @Id
    @SequenceGenerator(name = "DECLARATIONSPECIFICATION_ID_GEN", sequenceName = "DECLARATIONSPECIFICATION_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DECLARATIONSPECIFICATION_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "DECLARATION")
    private Integer declaration;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SPECIFICATION")
    private PGCSpecification specification;


}

