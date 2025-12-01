package com.cassinisys.platform.model.custom;

import com.cassinisys.platform.model.core.ObjectAttribute;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "CUSTOM_OBJECT_ATTRIBUTE")
@PrimaryKeyJoinColumns({
    @PrimaryKeyJoinColumn(name = "OBJECT_ID",
            referencedColumnName = "OBJECT_ID"),
    @PrimaryKeyJoinColumn(name = "ATTRIBUTE_ID",
            referencedColumnName = "ATTRIBUTEDEF")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomObjectAttribute extends ObjectAttribute {
}
