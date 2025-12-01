package com.cassinisys.plm.model.pgc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by GSR.
 */
@Entity
@Table(name = "PGC_SUBSTANCE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class PGCSubstance extends PGCObject {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE")
    private PGCSubstanceType type;

    @Column(name = "CAS_NUMBER")
    private String casNumber;

    @Column(name = "CA_NUMBER")
    private String caNumber;

    @Transient
    private String createPerson;
    @Transient
    private String modifiedPerson;
    @Transient
    private Boolean usedSubstance = Boolean.FALSE;

    public PGCSubstance() {
        super.setObjectType(PGCEnumObject.PGCSUBSTANCE);
    }

}
