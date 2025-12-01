package com.cassinisys.plm.model.mfr;

import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "PLM_SUPPLIERTYPEATTRIBUTE")
@PrimaryKeyJoinColumn(name = "ATTRIBUTE_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = false)
public class PLMSupplierTypeAttribute extends ObjectTypeAttribute {

    @Column(name = "TYPE")
    private Integer type;

    @Column(name = "SEQ")
    private Integer seq;

    public PLMSupplierTypeAttribute() {
    }
}
