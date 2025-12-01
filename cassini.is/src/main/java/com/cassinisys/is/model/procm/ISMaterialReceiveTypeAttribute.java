package com.cassinisys.is.model.procm;

import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by swapna on 20/06/19.
 */
@Entity
@Table(name = "IS_MATERIALRECEIVETYPEATTRIBUTE")
@PrimaryKeyJoinColumn(name = "ATTRIBUTE_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "IS")
public class ISMaterialReceiveTypeAttribute extends ObjectTypeAttribute {

    @Column(name = "ITEM_TYPE")
    @ApiObjectField(required = true)
    private Integer itemType;

    public ISMaterialReceiveTypeAttribute() {
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }
}
