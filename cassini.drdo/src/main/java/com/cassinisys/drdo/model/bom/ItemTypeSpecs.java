package com.cassinisys.drdo.model.bom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by subramanyam reddy on 17-10-2018.
 */
@Entity
@Table(name = "ITEMTYPE_SPECS")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "DRDO")
public class ItemTypeSpecs implements Serializable {

    @Id
    @ApiObjectField(required = true)
    @SequenceGenerator(name = "ITEMTYPE_SPECS_ID_GEN", sequenceName = "ITEMTYPE_SPECS_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ITEMTYPE_SPECS_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "ITEMTYPE")
    private Integer itemType;

    @Column(name = "SPEC_NAME")
    private String specName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }
}
