package com.cassinisys.pdm.model;

import com.cassinisys.platform.model.core.ObjectAttribute;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;

/**
 * Created by subramanyamreddy on 20-Jan-17.
 */
@Entity
@Table(name = "PDM_FILEATTRIBUTE")
@PrimaryKeyJoinColumns({@PrimaryKeyJoinColumn(name = "FILE", referencedColumnName = "OBJECT_ID"),
                        @PrimaryKeyJoinColumn(name = "ATTRIBUTE", referencedColumnName = "ATTRIBUTEDEF")})
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "PDM")
public class PDMFileAttribute extends ObjectAttribute{

    public PDMFileAttribute() {
    }
}
