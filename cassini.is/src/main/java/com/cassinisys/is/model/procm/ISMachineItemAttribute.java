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
@Table(name = "IS_MACHINEITEMATTRIBUTE")
@PrimaryKeyJoinColumns({
        @PrimaryKeyJoinColumn(name = "ITEM",
                referencedColumnName = "OBJECT_ID"),
        @PrimaryKeyJoinColumn(name = "ATTRIBUTE",
                referencedColumnName = "ATTRIBUTEDEF")
})
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "PROCM")
public class ISMachineItemAttribute extends ObjectAttribute {

    public ISMachineItemAttribute() {
    }

    /**
     * The Method is for ISMaterialItemAttribute
     */
    public ISMachineItemAttribute copy() {
        ISMachineItemAttribute copy = new ISMachineItemAttribute();
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
