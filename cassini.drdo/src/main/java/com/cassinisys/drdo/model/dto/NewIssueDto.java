package com.cassinisys.drdo.model.dto;

import com.cassinisys.drdo.model.transactions.RequestItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subra on 12-12-2018.
 */
public class NewIssueDto {

    private RequestItem requestItem;

    private Double allocateQty = 0.0;

    private List<IssueItemDto> issuedItemDto = new ArrayList<>();

    public RequestItem getRequestItem() {
        return requestItem;
    }

    public void setRequestItem(RequestItem requestItem) {
        this.requestItem = requestItem;
    }

    public List<IssueItemDto> getIssuedItemDto() {
        return issuedItemDto;
    }

    public void setIssuedItemDto(List<IssueItemDto> issuedItemDto) {
        this.issuedItemDto = issuedItemDto;
    }

    public Double getAllocateQty() {
        return allocateQty;
    }

    public void setAllocateQty(Double allocateQty) {
        this.allocateQty = allocateQty;
    }
}
