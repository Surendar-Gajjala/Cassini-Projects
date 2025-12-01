package com.cassinisys.is.model.procm;
/**
 * Model for ISMaterialItemAttribute
 */

import com.cassinisys.platform.model.core.ObjectAttribute;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;

@Entity
@Table(name = "IS_MATERIALITEMATTRIBUTE")
@PrimaryKeyJoinColumns({
        @PrimaryKeyJoinColumn(name = "ITEM",
                referencedColumnName = "OBJECT_ID"),
        @PrimaryKeyJoinColumn(name = "ATTRIBUTE",
                referencedColumnName = "ATTRIBUTEDEF")
})
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "PROCM")
public class ISMaterialItemAttribute extends ObjectAttribute {

    public ISMaterialItemAttribute() {
    }

    /**
     * The Method is for ISMaterialItemAttribute
     */
    public ISMaterialItemAttribute copy() {
        ISMaterialItemAttribute copy = new ISMaterialItemAttribute();
        copy.setId(this.getId());
        copy.setStringValue(this.getStringValue());
        copy.setIntegerValue(this.getIntegerValue());
        copy.setDoubleValue(this.getDoubleValue());
        copy.setDateValue(this.getDateValue());
        copy.setBooleanValue(this.getBooleanValue());
        /*copy.setListValue(this.getListValue());*/
        return copy;
    }

}
