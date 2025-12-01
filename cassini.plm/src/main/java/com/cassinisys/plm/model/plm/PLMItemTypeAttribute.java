package com.cassinisys.plm.model.plm;

import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_ITEMTYPEATTRIBUTE")
@PrimaryKeyJoinColumn(name = "ATTRIBUTE_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMItemTypeAttribute extends ObjectTypeAttribute {

    @Transient
    Map<String, PLMItemTypeAttribute> lovsForList = new LinkedHashMap<>();
    @Column(name = "ITEM_TYPE")
    private Integer itemType;
    @Column(name = "REVISION_SPECIFIC", nullable = false)
    private Boolean revisionSpecific;
    @Column(name = "CHANGE_CONTROLLED", nullable = false)
    private Boolean changeControlled;
    @Column(name = "SEQ")
    private Integer seq;
    @Column(name = "CONFIGURABLE", nullable = false)
    private Boolean configurable = Boolean.FALSE;
    @Column(name = "ALLOW_EDIT_AFTER_RELEASE", nullable = false)
    private Boolean allowEditAfterRelease = Boolean.FALSE;

    @Transient
    private String value;

    @Transient
    private String[] configurableAttr;

    public PLMItemTypeAttribute() {
    }


}
