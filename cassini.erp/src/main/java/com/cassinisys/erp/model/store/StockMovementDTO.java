package com.cassinisys.erp.model.store;

import java.util.List;

public class StockMovementDTO {

    private List<ERPStockReceiveItem> topStockReceiveds;

    private List<ERPStockIssueItem> topStockIssueds;

    private Integer receivedQty = 0;

    private Integer issuedQty = 0;

    public List<ERPStockReceiveItem> getTopStockReceiveds() {
        return topStockReceiveds;
    }

    public void setTopStockReceiveds(List<ERPStockReceiveItem> topStockReceiveds) {
        this.topStockReceiveds = topStockReceiveds;
    }

    public List<ERPStockIssueItem> getTopStockIssueds() {
        return topStockIssueds;
    }

    public void setTopStockIssueds(List<ERPStockIssueItem> topStockIssueds) {
        this.topStockIssueds = topStockIssueds;
    }

    public Integer getReceivedQty() {
        return receivedQty;
    }

    public void setReceivedQty(Integer receivedQty) {
        this.receivedQty = receivedQty;
    }

    public Integer getIssuedQty() {
        return issuedQty;
    }

    public void setIssuedQty(Integer issuedQty) {
        this.issuedQty = issuedQty;
    }
}
