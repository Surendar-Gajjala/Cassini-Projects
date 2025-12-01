package com.cassinisys.drdo.model.dto;

import com.cassinisys.drdo.model.bom.BomInstance;
import com.cassinisys.drdo.model.bom.BomItem;
import com.cassinisys.drdo.model.bom.ItemInstance;
import com.cassinisys.drdo.model.transactions.GatePass;
import com.cassinisys.drdo.model.transactions.Inward;
import com.cassinisys.drdo.model.transactions.IssueItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subra on 17-05-2019.
 */
public class ItemReportDto {

    private BomItem bomItem;

    private List<GatePass> gatePasses = new ArrayList<>();

    private List<Inward> inwards = new ArrayList<>();

    private List<BomInstance> missiles = new ArrayList<>();

    private List<ItemInstance> itemInstances = new ArrayList<>();

    private List<IssueItem> issueItems = new ArrayList<>();

    public BomItem getBomItem() {
        return bomItem;
    }

    public void setBomItem(BomItem bomItem) {
        this.bomItem = bomItem;
    }

    public List<GatePass> getGatePasses() {
        return gatePasses;
    }

    public void setGatePasses(List<GatePass> gatePasses) {
        this.gatePasses = gatePasses;
    }

    public List<Inward> getInwards() {
        return inwards;
    }

    public void setInwards(List<Inward> inwards) {
        this.inwards = inwards;
    }

    public List<BomInstance> getMissiles() {
        return missiles;
    }

    public void setMissiles(List<BomInstance> missiles) {
        this.missiles = missiles;
    }

    public List<ItemInstance> getItemInstances() {
        return itemInstances;
    }

    public void setItemInstances(List<ItemInstance> itemInstances) {
        this.itemInstances = itemInstances;
    }

    public List<IssueItem> getIssueItems() {
        return issueItems;
    }

    public void setIssueItems(List<IssueItem> issueItems) {
        this.issueItems = issueItems;
    }
}
