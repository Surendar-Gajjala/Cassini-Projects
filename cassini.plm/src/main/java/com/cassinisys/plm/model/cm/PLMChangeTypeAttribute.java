package com.cassinisys.plm.model.cm;

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
@Table(name = "PLM_CHANGETYPEATTRIBUTE")
@PrimaryKeyJoinColumn(name = "ATTRIBUTE_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMChangeTypeAttribute extends ObjectTypeAttribute {

    @Column(name = "CHANGE_TYPE")
    private Integer changeType;

    @Column(name = "SEQ")
    private Integer seq;

    public PLMChangeTypeAttribute() {
    }


}
