package com.cassinisys.drdo.model.bom;

import com.cassinisys.drdo.model.DRDOObjectType;
import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.List;

/**
 * Created by subra on 07-10-2018.
 */
@Entity
@Table(name = "BOM")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "DRDO")
public class Bom extends CassiniObject {

    @ApiObjectField
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ITEM", nullable = false)
    private ItemRevision item;

    @Transient
    private List<BomInstance> children;

    public Bom() {
        super(DRDOObjectType.BOM);
    }

    public ItemRevision getItem() {
        return item;
    }

    public void setItem(ItemRevision item) {
        this.item = item;
    }

    public List<BomInstance> getChildren() {
        return children;
    }

    public void setChildren(List<BomInstance> children) {
        this.children = children;
    }
}
