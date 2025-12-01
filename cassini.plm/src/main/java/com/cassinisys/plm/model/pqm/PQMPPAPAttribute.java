package com.cassinisys.plm.model.pqm;

import com.cassinisys.platform.model.core.ObjectAttribute;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;

@Entity
@Table(name = "PQM_PPAP_ATTRIBUTE")
@PrimaryKeyJoinColumns({
        @PrimaryKeyJoinColumn(name = "PPAP",
                referencedColumnName = "OBJECT_ID"),
        @PrimaryKeyJoinColumn(name = "ATTRIBUTE",
                referencedColumnName = "ATTRIBUTEDEF")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class PQMPPAPAttribute extends ObjectAttribute {

    public PQMPPAPAttribute() {
    }
}
