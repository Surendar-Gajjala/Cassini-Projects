package com.cassinisys.plm.model.plm;

import com.cassinisys.platform.model.core.ObjectAttribute;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;

@Entity
@Table(name = "PLM_ITEMATTRIBUTE")
@PrimaryKeyJoinColumns({
        @PrimaryKeyJoinColumn(name = "ITEM",
                referencedColumnName = "OBJECT_ID"),
        @PrimaryKeyJoinColumn(name = "ATTRIBUTE",
                referencedColumnName = "ATTRIBUTEDEF")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMItemAttribute extends ObjectAttribute {

    public PLMItemAttribute() {
    }

    public PLMItemAttribute copy() {
        PLMItemAttribute copy = new PLMItemAttribute();
        copy.setId(this.getId());
        copy.setStringValue(this.getStringValue());
        copy.setIntegerValue(this.getIntegerValue());
        copy.setDoubleValue(this.getDoubleValue());
        copy.setDateValue(this.getDateValue());
        copy.setBooleanValue(this.getBooleanValue());
        copy.setListValue(this.getListValue());
        copy.setMListValue(this.getMListValue());
        return copy;
    }

}
