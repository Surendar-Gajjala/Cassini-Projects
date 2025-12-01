package com.cassinisys.pdm.model;

import com.cassinisys.platform.model.core.ObjectAttribute;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;

/**
 * Created by GSR on 20-01-2017.
 */

@Entity
@Table(name = "PDM_ITEMATTRIBUTE")
@PrimaryKeyJoinColumns({@PrimaryKeyJoinColumn(name = "ITEM", referencedColumnName = "OBJECT_ID"),
		@PrimaryKeyJoinColumn(name = "ATTRIBUTE", referencedColumnName = "ATTRIBUTEDEF")})
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "PDM")
public class PDMItemAttribute extends ObjectAttribute{

	public PDMItemAttribute() {
	}
}
