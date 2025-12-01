package com.cassinisys.drdo.model.dto;

import com.cassinisys.drdo.model.bom.BomInstanceItem;
import com.cassinisys.drdo.model.transactions.Request;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam on 15-10-2019.
 */
public class RequestedItemsDto {

    private BomInstanceItem item;

    private List<Request> requests = new ArrayList<>();

    public BomInstanceItem getItem() {
        return item;
    }

    public void setItem(BomInstanceItem item) {
        this.item = item;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }
}
