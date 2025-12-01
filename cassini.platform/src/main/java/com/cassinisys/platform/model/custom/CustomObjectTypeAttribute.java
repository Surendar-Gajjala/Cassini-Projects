package com.cassinisys.platform.model.custom;

import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "CUSTOM_OBJECT_TYPE_ATTRIBUTE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomObjectTypeAttribute extends ObjectTypeAttribute {

    @Column(name = "SEQ_NO")
    private Integer seqNo;

    @Column(name = "CUSTOM_OBJECT_TYPE")
    private Integer customObjectType;

    @Column(name = "REVISION_SPECIFIC")
    private Boolean revisionSpecific = Boolean.FALSE;

    @Column(name = "SHOW_IN_TABLE")
    private Boolean showInTable = Boolean.TRUE;

    public CustomObjectTypeAttribute() {
        super.setObjectType(ObjectType.CUSTOMOBJECT);
    }
}
