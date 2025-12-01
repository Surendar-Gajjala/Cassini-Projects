package com.cassinisys.plm.model.pgc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by GSR.
 */
@Entity
@Table(name = "PGC_SPECIFICATION")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class PGCSpecification extends PGCObject {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE")
    private PGCSpecificationType type;

    @Column(name = "ACTIVE")
    private Boolean active;

    @Transient
    private String createPerson;

    @Transient
    private String modifiedPerson;
    @Transient
    private Boolean usedSpecification = Boolean.FALSE;

    public PGCSpecification() {
        super.setObjectType(PGCEnumObject.PGCSPECIFICATION);
    }

}

