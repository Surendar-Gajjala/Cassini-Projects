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
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_MANUFACTURERTYPEATTRIBUTE")
@PrimaryKeyJoinColumn(name = "ATTRIBUTE_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMManufacturerTypeAttribute extends ObjectTypeAttribute {

    @Column(name = "MFR_TYPE")
    private Integer mfrType;

    @Column(name = "SEQ")
    private Integer seq;

    public PLMManufacturerTypeAttribute() {
    }

}
