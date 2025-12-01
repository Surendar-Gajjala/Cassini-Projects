package com.cassinisys.drdo.model.inventory;

import com.cassinisys.drdo.model.bom.ItemType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by subra on 07-10-2018.
 */
@Entity
@Table(name = "STORAGEITEMTYPE")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "DRDO")
public class StorageItemType implements Serializable {

    @Id
    @ApiObjectField(required = true)
    @SequenceGenerator(name = "STORAGE_ITEM_TYPE_ID_GEN", sequenceName = "STORAGE_ITEM_TYPE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STORAGE_ITEM_TYPE_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @ApiObjectField
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "STORAGE")
    private Storage storage;

    @ApiObjectField
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ITEM_TYPE")
    private ItemType itemType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }
}
