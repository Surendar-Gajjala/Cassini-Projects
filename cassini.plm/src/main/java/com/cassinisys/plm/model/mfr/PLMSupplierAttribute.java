package com.cassinisys.plm.model.mfr;

import com.cassinisys.platform.model.core.ObjectAttribute;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;

/**
 * Created by Suresh Cassini on 25-11-2020.
 */
@Entity
@Table(name = "PLM_SUPPLIERATTRIBUTE")
@PrimaryKeyJoinColumns({
        @PrimaryKeyJoinColumn(name = "SUPPLIER",
                referencedColumnName = "OBJECT_ID"),
        @PrimaryKeyJoinColumn(name = "ATTRIBUTE",
                referencedColumnName = "ATTRIBUTEDEF")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMSupplierAttribute extends ObjectAttribute {

    public PLMSupplierAttribute() {
    }
}
