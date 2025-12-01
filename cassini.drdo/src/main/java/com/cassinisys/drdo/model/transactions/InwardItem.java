package com.cassinisys.drdo.model.transactions;

import com.cassinisys.drdo.model.DRDOObjectType;
import com.cassinisys.drdo.model.bom.BomGroup;
import com.cassinisys.drdo.model.bom.BomItem;
import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam reddy on 15-10-2018.
 */
@Entity
@Table(name = "INWARDITEM")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "DRDO")
public class InwardItem extends CassiniObject {

    @ApiObjectField
    @Column(name = "INWARD")
    private Integer inward;

    @ApiObjectField
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BOM_ITEM")
    private BomItem bomItem;

    @ApiObjectField
    @Column(name = "QUANTITY")
    private Integer quantity;

    @ApiObjectField
    @Column(name = "FRACTIONAL_QUANTITY")
    private Double fractionalQuantity;

    @ApiObjectField
    @Column(name = "INSTANCES_CREATED")
    private Boolean instancesCreated = Boolean.FALSE;

    @ApiObjectField
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SECTION")
    private BomGroup section;

    @Transient
    private Boolean accepted = Boolean.FALSE;

    @Transient
    private Boolean pAccepted = Boolean.FALSE;

    @Transient
    private List<InwardItemInstance> instances = new ArrayList<>();


    public InwardItem() {
        super(DRDOObjectType.INWARDITEM);
    }

    public BomItem getBomItem() {
        return bomItem;
    }

    public void setBomItem(BomItem bomItem) {
        this.bomItem = bomItem;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getFractionalQuantity() {
        return fractionalQuantity;
    }

    public void setFractionalQuantity(Double fractionalQuantity) {
        this.fractionalQuantity = fractionalQuantity;
    }

    public Integer getInward() {
        return inward;
    }

    public void setInward(Integer inward) {
        this.inward = inward;
    }

    public Boolean getInstancesCreated() {
        return instancesCreated;
    }

    public void setInstancesCreated(Boolean instancesCreated) {
        this.instancesCreated = instancesCreated;
    }

    public List<InwardItemInstance> getInstances() {
        return instances;
    }

    public void setInstances(List<InwardItemInstance> instances) {
        this.instances = instances;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public Boolean getpAccepted() {
        return pAccepted;
    }

    public void setpAccepted(Boolean pAccepted) {
        this.pAccepted = pAccepted;
    }

    public BomGroup getSection() {
        return section;
    }

    public void setSection(BomGroup section) {
        this.section = section;
    }
}
