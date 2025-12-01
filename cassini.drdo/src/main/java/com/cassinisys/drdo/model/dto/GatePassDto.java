package com.cassinisys.drdo.model.dto;

import com.cassinisys.drdo.model.transactions.GatePass;

/**
 * Created by subramanyam reddy on 20-09-2019.
 */
public class GatePassDto {

    private GatePass gatePass;

    private Double quantity = 0.0;

    public GatePass getGatePass() {
        return gatePass;
    }

    public void setGatePass(GatePass gatePass) {
        this.gatePass = gatePass;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
}
