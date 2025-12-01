package com.cassinisys.drdo.model.dto;

import com.cassinisys.drdo.model.bom.BomItem;
import com.cassinisys.drdo.model.bom.ItemRevision;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam reddy on 19-11-2018.
 */
public class BomInwardReportDto {

    private ItemRevision itemRevision;

    private Integer totalInward = 0;

    private Double totalFractionalInward = 0.0;

    private BomItem bomItem;

    private List<GatePassDto> gatePasses = new ArrayList<>();

    public ItemRevision getItemRevision() {
        return itemRevision;
    }

    public void setItemRevision(ItemRevision itemRevision) {
        this.itemRevision = itemRevision;
    }

    public Integer getTotalInward() {
        return totalInward;
    }

    public void setTotalInward(Integer totalInward) {
        this.totalInward = totalInward;
    }

    public Double getTotalFractionalInward() {
        return totalFractionalInward;
    }

    public void setTotalFractionalInward(Double totalFractionalInward) {
        this.totalFractionalInward = totalFractionalInward;
    }

    public List<GatePassDto> getGatePasses() {
        return gatePasses;
    }

    public void setGatePasses(List<GatePassDto> gatePasses) {
        this.gatePasses = gatePasses;
    }

    public BomItem getBomItem() {
        return bomItem;
    }

    public void setBomItem(BomItem bomItem) {
        this.bomItem = bomItem;
    }
}
