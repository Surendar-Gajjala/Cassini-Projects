package com.cassinisys.drdo.model.transactions;

import com.cassinisys.drdo.model.bom.BomItem;
import com.cassinisys.drdo.model.bom.ItemInstance;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by subramanyam reddy on 15-10-2018.
 */
@Entity
@Table(name = "INWARDITEMINSTANCE")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "DRDO")
public class InwardItemInstance implements Serializable {

    @Id
    @ApiObjectField(required = true)
    @SequenceGenerator(name = "INWARD_ITEM_INSTANCE_ID_GEN", sequenceName = "INWARD_ITEM_INSTANCE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INWARD_ITEM_INSTANCE_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @ApiObjectField
    @Column(name = "INWARDITEM", nullable = false)
    private Integer inwardItem;

    @ApiObjectField
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ITEM", nullable = false)
    private ItemInstance item;

    @Column(name = "LATEST")
    private Boolean latest = Boolean.TRUE;

    @Column(name = "HAS_RETURN_STORAGE")
    private Boolean hasReturnStorage = Boolean.FALSE;

    @Transient
    private BomItem bomItem;

    @Transient
    private Inward inward;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getInwardItem() {
        return inwardItem;
    }

    public void setInwardItem(Integer inwardItem) {
        this.inwardItem = inwardItem;
    }

    public ItemInstance getItem() {
        return item;
    }

    public void setItem(ItemInstance item) {
        this.item = item;
    }

    public BomItem getBomItem() {
        return bomItem;
    }

    public void setBomItem(BomItem bomItem) {
        this.bomItem = bomItem;
    }

    public Inward getInward() {
        return inward;
    }

    public void setInward(Inward inward) {
        this.inward = inward;
    }

    public Boolean getLatest() {
        return latest;
    }

    public void setLatest(Boolean latest) {
        this.latest = latest;
    }

    public Boolean getHasReturnStorage() {
        return hasReturnStorage;
    }

    public void setHasReturnStorage(Boolean hasReturnStorage) {
        this.hasReturnStorage = hasReturnStorage;
    }
}
