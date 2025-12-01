package com.cassinisys.plm.model.rm;

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
@Table(name = "RM_OBJECTTYPEATTRIBUTE")
@PrimaryKeyJoinColumn(name = "ATTRIBUTE_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RmObjectTypeAttribute extends ObjectTypeAttribute {

    @Column(name = "RM_OBJECTTYPE")
    private Integer rmObjectType;

    @Column(name = "REVISION_SPECIFIC", nullable = false)
    private Boolean revisionSpecific;

    @Column(name = "CHANGE_CONTROLLED", nullable = false)
    private Boolean changeControlled;

    @Column(name = "SEQ")
    private Integer seq;

    public RmObjectTypeAttribute() {
    }


}