package com.cassinisys.is.model.procm.dto;
/**
 * Model for  RfqRecipientsTO
 */

import java.util.List;

public class RfqRecipientsTO {

    private List<Integer> suppliers;

    public RfqRecipientsTO() {
    }

    public List<Integer> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(List<Integer> suppliers) {
        this.suppliers = suppliers;
    }

}
