package com.cassinisys.drdo.model.inventory;

import com.cassinisys.drdo.model.bom.BomGroup;
import com.cassinisys.drdo.model.bom.BomItem;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by subra on 07-10-2018.
 */
@Entity
@Table(name = "STORAGEITEM")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "DRDO")
public class StorageItem implements Serializable {

    @Id
    @ApiObjectField(required = true)
    @SequenceGenerator(name = "STORAGE_ITEM_ID_GEN", sequenceName = "STORAGE_ITEM_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STORAGE_ITEM_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @ApiObjectField
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "STORAGE")
    private Storage storage;

    @ApiObjectField
    @Column(name = "UNIQUE_CODE")
    private String uniqueCode;

    @ApiObjectField
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SECTION")
    private BomGroup section;

    @ApiObjectField
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ITEM")
    private BomItem item;

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

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public BomGroup getSection() {
        return section;
    }

    public void setSection(BomGroup section) {
        this.section = section;
    }

    public BomItem getItem() {
        return item;
    }

    public void setItem(BomItem item) {
        this.item = item;
    }
}
