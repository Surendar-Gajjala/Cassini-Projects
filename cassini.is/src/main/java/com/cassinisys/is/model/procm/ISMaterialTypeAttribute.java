package com.cassinisys.is.model.procm;

import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "IS_MATERIALTYPEATTRIBUTE")
@PrimaryKeyJoinColumn(name = "ATTRIBUTE_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "IS")
public class ISMaterialTypeAttribute extends ObjectTypeAttribute {

    @Column(name = "ITEM_TYPE")
    @ApiObjectField(required = true)
    private Integer itemType;

    public ISMaterialTypeAttribute() {
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }
}
