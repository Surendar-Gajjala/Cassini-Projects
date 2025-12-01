package com.cassinisys.plm.model.pqm;

import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PQM_QUALITY_TYPE_ATTRIBUTE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PQMQualityTypeAttribute extends ObjectTypeAttribute {

    @Column(name = "TYPE")
    private Integer type;

    @Column(name = "SEQ")
    private Integer seq;

    @Column(name = "REVISION_SPECIFIC")
    private Boolean revisionSpecific = Boolean.FALSE;

    public PQMQualityTypeAttribute() {
    }

}
