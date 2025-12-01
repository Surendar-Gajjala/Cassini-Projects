package com.cassinisys.plm.model.pgc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by GSR.
 */
@Entity
@Table(name = "PGC_SUBSTANCEGROUP")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class PGCSubstanceGroup extends PGCObject {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE")
    private PGCSubstanceGroupType type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BASE_SUBSTANCE")
    private PGCSubstanceGroup baseSubstance;

    @Column(name = "CONVERSION_FACTOR")
    private Double conversionFactor;

    public PGCSubstanceGroup() {
        super.setObjectType(PGCEnumObject.PGCSUBSTANCEGROUP);
    }

}

