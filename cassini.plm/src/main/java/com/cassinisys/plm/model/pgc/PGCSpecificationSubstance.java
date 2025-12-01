package com.cassinisys.plm.model.pgc;

import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by GSR on 25-11-2020.
 */
@Entity
@Table(name = "PGC_SPECIFICATION_SUBSTANCE")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PGCSpecificationSubstance implements Serializable {

    @Id
    @SequenceGenerator(name = "SPECIFICATIONSUBSTANCE_ID_GEN", sequenceName = "SPECIFICATIONSUBSTANCE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SPECIFICATIONSUBSTANCE_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "SPECIFICATION")
    private Integer specification;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SUBSTANCE", nullable = false)
    private PGCSubstance substance;

    @Column(name = "THRESHOLD_PPM")
    private Integer thresholdPpm = 0;

    @Column(name = "THRESHOLD_MASS")
    private Double thresholdMass = 0.0;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE_OF_INCLUSION")
    private Date dateOfInclusion;

    @Column(name = "REASON_FOR_INCLUSION")
    private String reasonForInclusion;

    @Column(name = "UOM")
    private Integer uom;

    @Transient
    private String unitSymbol;
    @Transient
    private String unitName;
}

