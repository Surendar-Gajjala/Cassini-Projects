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
@Table(name = "PLM_MANUFACTURERPARTTYPEATTRIBUTE")
@PrimaryKeyJoinColumn(name = "ATTRIBUTE_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = false)
public class PLMManufacturerPartTypeAttribute extends ObjectTypeAttribute {

    @Column(name = "MFRPART_TYPE")
    private Integer mfrPartType;

    @Column(name = "SEQ")
    private Integer seq;

    public PLMManufacturerPartTypeAttribute() {
    }


}
