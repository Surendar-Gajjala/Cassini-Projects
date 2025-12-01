package com.cassinisys.plm.model.mfr;

import com.cassinisys.platform.model.core.ObjectAttribute;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;

/**
 * Created by subramanyamreddy on 002 2-May -17.
 */
@Entity
@Table(name = "PLM_MANUFACTURERATTRIBUTE")
@PrimaryKeyJoinColumns({
        @PrimaryKeyJoinColumn(name = "MANUFACTURER",
                referencedColumnName = "OBJECT_ID"),
        @PrimaryKeyJoinColumn(name = "ATTRIBUTE",
                referencedColumnName = "ATTRIBUTEDEF")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMManufacturerAttribute extends ObjectAttribute {

    public PLMManufacturerAttribute() {
    }
}
