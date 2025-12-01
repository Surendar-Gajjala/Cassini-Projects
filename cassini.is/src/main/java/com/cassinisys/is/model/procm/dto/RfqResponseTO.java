package com.cassinisys.is.model.procm.dto;
/**
 * Model for  RfqResponseTO
 */

import com.cassinisys.is.model.procm.ISRfqResponseItem;

import java.util.ArrayList;
import java.util.List;

public class RfqResponseTO {
    private Integer supplierId;

    private List<ISRfqResponseItem> items;

    public RfqResponseTO() {
        items = new ArrayList<ISRfqResponseItem>();
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public List<ISRfqResponseItem> getItems() {
        return items;
    }

    public void setItems(List<ISRfqResponseItem> items) {
        this.items = items;
    }
}
