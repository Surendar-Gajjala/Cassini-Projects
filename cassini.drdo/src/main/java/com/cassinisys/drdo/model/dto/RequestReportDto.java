package com.cassinisys.drdo.model.dto;

import com.cassinisys.drdo.model.transactions.RequestItem;
import com.cassinisys.drdo.model.transactions.RequestStatusHistory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam reddy on 21-11-2018.
 */
public class RequestReportDto {

    private List<RequestItem> requestItems = new ArrayList<>();

    private List<IssueDetailsDto> issuedItems = new ArrayList<>();

    private List<RequestStatusHistory> statusHistories = new ArrayList<>();

    public List<RequestItem> getRequestItems() {
        return requestItems;
    }

    public void setRequestItems(List<RequestItem> requestItems) {
        this.requestItems = requestItems;
    }

    public List<IssueDetailsDto> getIssuedItems() {
        return issuedItems;
    }

    public void setIssuedItems(List<IssueDetailsDto> issuedItems) {
        this.issuedItems = issuedItems;
    }

    public List<RequestStatusHistory> getStatusHistories() {
        return statusHistories;
    }

    public void setStatusHistories(List<RequestStatusHistory> statusHistories) {
        this.statusHistories = statusHistories;
    }
}
