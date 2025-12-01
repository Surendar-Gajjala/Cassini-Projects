package com.cassinisys.drdo.model.bom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by subramanyam reddy on 07-10-2018.
 */

@Entity
@Table(name = "BOMITEMINSTANCE")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "DRDO")
public class BomItemInstance implements Serializable {

    @Id
    @ApiObjectField(required = true)
    @SequenceGenerator(name = "BOM_ITEM_INSTANCE_ID_GEN", sequenceName = "BOM_ITEM_INSTANCE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOM_ITEM_INSTANCE_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @ApiObjectField
    @Column(name = "BOMINSTANCEITEM")
    private Integer bomInstanceItem;

    @ApiObjectField
    @Column(name = "ITEMINSTANCE")
    private Integer itemInstance;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBomInstanceItem() {
        return bomInstanceItem;
    }

    public void setBomInstanceItem(Integer bomInstanceItem) {
        this.bomInstanceItem = bomInstanceItem;
    }

    public Integer getItemInstance() {
        return itemInstance;
    }

    public void setItemInstance(Integer itemInstance) {
        this.itemInstance = itemInstance;
    }
}
