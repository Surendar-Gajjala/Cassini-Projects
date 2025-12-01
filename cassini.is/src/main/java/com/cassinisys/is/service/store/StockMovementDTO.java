package com.cassinisys.is.service.store;

import com.cassinisys.is.model.store.ISStockReceiveItem;
import com.cassinisys.is.model.store.ISTStockIssueItem;
import com.cassinisys.is.service.procm.ItemDTO;

import java.util.List;

/**
 * Created by swapna on 10/07/18.
 */
public class StockMovementDTO {

    private List<ISStockReceiveItem> topStockReceiveds;

    private List<ISTStockIssueItem> topStockIssueds;

    private Double receivedQty = 0.0;

    private Double issuedQty = 0.0;

    private Double returnedQty = 0.0;

    private ItemDTO itemDTO;

    public List<ISStockReceiveItem> getTopStockReceiveds() {
        return topStockReceiveds;
    }

    public void setTopStockReceiveds(List<ISStockReceiveItem> topStockReceiveds) {
        this.topStockReceiveds = topStockReceiveds;
    }

    public List<ISTStockIssueItem> getTopStockIssueds() {
        return topStockIssueds;
    }

    public void setTopStockIssueds(List<ISTStockIssueItem> topStockIssueds) {
        this.topStockIssueds = topStockIssueds;
    }

    public Double getReceivedQty() {
        return receivedQty;
    }

    public void setReceivedQty(Double receivedQty) {
        this.receivedQty = receivedQty;
    }

    public Double getIssuedQty() {
        return issuedQty;
    }

    public void setIssuedQty(Double issuedQty) {
        this.issuedQty = issuedQty;
    }

    public ItemDTO getItemDTO() {
        return itemDTO;
    }

    public void setItemDTO(ItemDTO itemDTO) {
        this.itemDTO = itemDTO;
    }

    public Double getReturnedQty() {
        return returnedQty;
    }

    public void setReturnedQty(Double returnedQty) {
        this.returnedQty = returnedQty;
    }
}
