package com.cassinisys.is.model.procm;

import com.cassinisys.platform.model.core.ObjectAttribute;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;

/**
 * Created by swapna on 20/06/19.
 */
@Entity
@Table(name = "IS_RECEIVETYPEITEMATTRIBUTE")
@PrimaryKeyJoinColumns({
        @PrimaryKeyJoinColumn(name = "ITEM",
                referencedColumnName = "OBJECT_ID"),
        @PrimaryKeyJoinColumn(name = "ATTRIBUTE",
                referencedColumnName = "ATTRIBUTEDEF")
})
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "PROCM")
public class ISReceiveTypeItemAttribute extends ObjectAttribute {

    public ISReceiveTypeItemAttribute() {
    }

    public ISReceiveTypeItemAttribute copy() {
        ISReceiveTypeItemAttribute copy = new ISReceiveTypeItemAttribute();
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
