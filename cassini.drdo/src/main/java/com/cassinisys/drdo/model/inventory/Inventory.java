package com.cassinisys.drdo.model.inventory;

import com.cassinisys.drdo.model.bom.ItemRevision;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by subramanyam reddy on 17-10-2018.
 */
@Entity
@Table(name = "INVENTORY")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "DRDO")
public class Inventory implements Serializable {

    @Id
    @ApiObjectField(required = true)
    @SequenceGenerator(name = "INVENTORY_ID_GEN", sequenceName = "INVENTORY_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INVENTORY_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "BOM")
    private Integer bom;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ITEM")
    private ItemRevision item;

    @Column(name = "QTY_ONHAND")
    private Integer quantity = 0;

    @Column(name = "FRACTIONALQTY_ONHAND")
    private Double fractionalQtyOnHand = 0.0;

    @Column(name = "QTY_BUFFERED")
    private Integer qtyBuffered = 0;

    @Column(name = "FRACTIONALQTY_BUFFERED")
    private Double fractionalQtyBuffered = 0.0;

    @Column(name = "QTY_REQUESTED")
    private Integer qtyRequested = 0;

    @Column(name = "FRACTIONALQTY_REQUESTED")
    private Double fractionalQtyRequested = 0.0;

    @Column(name = "QTY_ISSUED")
    private Integer qtyIssued = 0;

    @Column(name = "FRACTIONALQTY_ISSUED")
    private Double fractionalQtyIssued = 0.0;

    @Column(name = "QTY_ALLOCATED")
    private Integer qtyAllocated = 0;

    @Column(name = "FRACTIONALQTY_ALLOCATED")
    private Double fractionalQtyAllocated = 0.0;

    @Column(name = "UNIQUE_CODE")
    private String uniqueCode;

    @ApiObjectField
    @Column(name = "SECTION")
    private Integer section;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBom() {
        return bom;
    }

    public void setBom(Integer bom) {
        this.bom = bom;
    }

    public ItemRevision getItem() {
        return item;
    }

    public void setItem(ItemRevision item) {
        this.item = item;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getFractionalQtyOnHand() {
        return fractionalQtyOnHand;
    }

    public void setFractionalQtyOnHand(Double fractionalQtyOnHand) {
        this.fractionalQtyOnHand = fractionalQtyOnHand;
    }

    public Integer getQtyBuffered() {
        return qtyBuffered;
    }

    public void setQtyBuffered(Integer qtyBuffered) {
        this.qtyBuffered = qtyBuffered;
    }

    public Double getFractionalQtyBuffered() {
        return fractionalQtyBuffered;
    }

    public void setFractionalQtyBuffered(Double fractionalQtyBuffered) {
        this.fractionalQtyBuffered = fractionalQtyBuffered;
    }

    public Integer getQtyRequested() {
        return qtyRequested;
    }

    public void setQtyRequested(Integer qtyRequested) {
        this.qtyRequested = qtyRequested;
    }

    public Double getFractionalQtyRequested() {
        return fractionalQtyRequested;
    }

    public void setFractionalQtyRequested(Double fractionalQtyRequested) {
        this.fractionalQtyRequested = fractionalQtyRequested;
    }

    public Integer getQtyIssued() {
        return qtyIssued;
    }

    public void setQtyIssued(Integer qtyIssued) {
        this.qtyIssued = qtyIssued;
    }

    public Double getFractionalQtyIssued() {
        return fractionalQtyIssued;
    }

    public void setFractionalQtyIssued(Double fractionalQtyIssued) {
        this.fractionalQtyIssued = fractionalQtyIssued;
    }

    public Integer getQtyAllocated() {
        return qtyAllocated;
    }

    public void setQtyAllocated(Integer qtyAllocated) {
        this.qtyAllocated = qtyAllocated;
    }

    public Double getFractionalQtyAllocated() {
        return fractionalQtyAllocated;
    }

    public void setFractionalQtyAllocated(Double fractionalQtyAllocated) {
        this.fractionalQtyAllocated = fractionalQtyAllocated;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public Integer getSection() {
        return section;
    }

    public void setSection(Integer section) {
        this.section = section;
    }
}
