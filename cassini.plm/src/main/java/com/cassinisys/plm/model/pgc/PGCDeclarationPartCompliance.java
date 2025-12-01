package com.cassinisys.plm.model.pgc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by GSR on 25-11-2020.
 */
@Entity
@Table(name = "PGC_DECLARATION_PART_COMPLIANCE")
@JsonIgnoreProperties(ignoreUnknown = true)
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class PGCDeclarationPartCompliance implements Serializable {

    @Id
    @SequenceGenerator(name = "DECLARATIONPARTCOMPLIANCE_ID_GEN", sequenceName = "DECLARATIONPARTCOMPLIANCE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DECLARATIONPARTCOMPLIANCE_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "DECLARATION_PART")
    private Integer declarationPart;

    @Column(name = "DECLARATION_SPEC")
    private Integer declarationSpec;

    @Column(name = "COMPLIANT")
    private Boolean compliant = Boolean.FALSE;


}

