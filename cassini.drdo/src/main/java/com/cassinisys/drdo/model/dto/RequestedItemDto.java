package com.cassinisys.drdo.model.dto;

import com.cassinisys.drdo.model.bom.BomGroup;
import com.cassinisys.drdo.model.transactions.RequestItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subra on 19-02-2019.
 */
public class RequestedItemDto {

    private BomGroup section;

    private List<RequestItem> requestItems = new ArrayList<>();

    public BomGroup getSection() {
        return section;
    }

    public void setSection(BomGroup section) {
        this.section = section;
    }

    public List<RequestItem> getRequestItems() {
        return requestItems;
    }

    public void setRequestItems(List<RequestItem> requestItems) {
        this.requestItems = requestItems;
    }
}
