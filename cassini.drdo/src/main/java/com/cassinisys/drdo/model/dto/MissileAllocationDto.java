package com.cassinisys.drdo.model.dto;

import com.cassinisys.drdo.model.bom.BomInstanceItem;

/**
 * Created by Nageshreddy on 10-12-2018.
 */
public class MissileAllocationDto {

    private BomInstanceItem item;

    private Double allocateQty;

    private Double issuedQty;

    private Double failedQty;

    public Double getAllocateQty() {
        return allocateQty;
    }

    public void setAllocateQty(Double allocateQty) {
        this.allocateQty = allocateQty;
    }

    public Double getIssuedQty() {
        return issuedQty;
    }

    public void setIssuedQty(Double issuedQty) {
        this.issuedQty = issuedQty;
    }

    public Double getFailedQty() {
        return failedQty;
    }

    public void setFailedQty(Double failedQty) {
        this.failedQty = failedQty;
    }

    public BomInstanceItem getItem() {
        return item;
    }

    public void setItem(BomInstanceItem item) {
        this.item = item;
    }

}
