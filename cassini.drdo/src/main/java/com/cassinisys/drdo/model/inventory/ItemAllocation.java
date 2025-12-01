package com.cassinisys.drdo.model.inventory;

import com.cassinisys.platform.model.core.CassiniObject;

import javax.persistence.*;

/**
 * Created by Nageshreddy on 03-12-2018.
 */
@Entity
@Table(name = "ITEMALLOCATION")
@PrimaryKeyJoinColumn(name = "ID")
public class ItemAllocation extends CassiniObject {

    @Column(name = "BOM")
    private Integer bom;

    @Column(name = "BOMINSTANCE")
    private Integer bomInstance;

    @Column(name = "BOMINSTANCEITEM")
    private Integer bomInstanceItem;

    @Column(name = "ALLOCATE_QTY")
    private Double allocateQty = 0.0;

    @Column(name = "ISSUED_QTY")
    private Double issuedQty = 0.0;

    @Column(name = "ISSUEPROCESS_QTY")
    private Double issueProcessQty = 0.0;

    @Column(name = "FAILED_QTY")
    private Double failedQty = 0.0;

    @Transient
    private Double resetQty;

    @Transient
    private Double currentAllocateQty;

    @Transient
    private Integer itemInstance;

    public Integer getBomInstance() {
        return bomInstance;
    }

    public void setBomInstance(Integer bomInstance) {
        this.bomInstance = bomInstance;
    }

    public Double getAllocateQty() {
        return allocateQty;
    }

    public void setAllocateQty(Double allocateQty) {
        this.allocateQty = allocateQty;
    }

    public Integer getBomInstanceItem() {
        return bomInstanceItem;
    }

    public void setBomInstanceItem(Integer bomInstanceItem) {
        this.bomInstanceItem = bomInstanceItem;
    }

    public Double getIssuedQty() {
        return issuedQty;
    }

    public void setIssuedQty(Double issuedQty) {
        this.issuedQty = issuedQty;
    }

    public Integer getBom() {
        return bom;
    }

    public void setBom(Integer bom) {
        this.bom = bom;
    }

    public Double getFailedQty() {
        return failedQty;
    }

    public void setFailedQty(Double failedQty) {
        this.failedQty = failedQty;
    }

    public Double getResetQty() {
        return resetQty;
    }

    public void setResetQty(Double resetQty) {
        this.resetQty = resetQty;
    }

    public Double getCurrentAllocateQty() {
        return currentAllocateQty;
    }

    public void setCurrentAllocateQty(Double currentAllocateQty) {
        this.currentAllocateQty = currentAllocateQty;
    }

    public Integer getItemInstance() {
        return itemInstance;
    }

    public void setItemInstance(Integer itemInstance) {
        this.itemInstance = itemInstance;
    }

    public Double getIssueProcessQty() {
        return issueProcessQty;
    }

    public void setIssueProcessQty(Double issueProcessQty) {
        this.issueProcessQty = issueProcessQty;
    }
}
