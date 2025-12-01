package com.cassinisys.plm.model.mes;

import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by CassiniSystems on 17-09-2020.
 */
@Entity
@Table(name = "MES_OBJECT_TYPE_ATTRIBUTE")
@PrimaryKeyJoinColumn(name = "ATTRIBUTE_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = false)
public class MESObjectTypeAttribute extends ObjectTypeAttribute {

    @Column(name = "TYPE")
    private Integer type;

    @Column(name = "SEQ")
    private Integer seq;

    public MESObjectTypeAttribute() {
    }

}
