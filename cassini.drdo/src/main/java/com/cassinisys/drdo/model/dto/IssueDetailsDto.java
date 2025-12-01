package com.cassinisys.drdo.model.dto;

import com.cassinisys.drdo.model.bom.BomItemInstance;
import com.cassinisys.drdo.model.bom.ItemInstance;
import com.cassinisys.drdo.model.bom.LotInstance;
import com.cassinisys.drdo.model.transactions.IssueItem;
import com.cassinisys.drdo.model.transactions.RequestItem;

/**
 * Created by subra on 13-12-2018.
 */
public class IssueDetailsDto {

    private IssueItem issueItem;

    private RequestItem requestItem;

    private BomItemInstance bomItemInstance;

    private ItemInstance itemInstance;

    private LotInstance lotInstance;

    private String reason;

    public IssueItem getIssueItem() {
        return issueItem;
    }

    public void setIssueItem(IssueItem issueItem) {
        this.issueItem = issueItem;
    }

    public RequestItem getRequestItem() {
        return requestItem;
    }

    public void setRequestItem(RequestItem requestItem) {
        this.requestItem = requestItem;
    }

    public BomItemInstance getBomItemInstance() {
        return bomItemInstance;
    }

    public void setBomItemInstance(BomItemInstance bomItemInstance) {
        this.bomItemInstance = bomItemInstance;
    }

    public ItemInstance getItemInstance() {
        return itemInstance;
    }

    public void setItemInstance(ItemInstance itemInstance) {
        this.itemInstance = itemInstance;
    }

    public LotInstance getLotInstance() {
        return lotInstance;
    }

    public void setLotInstance(LotInstance lotInstance) {
        this.lotInstance = lotInstance;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
