package com.cassinisys.drdo.model.dto;

import com.cassinisys.drdo.model.bom.BomInstanceItem;
import com.cassinisys.drdo.model.bom.ItemInstance;
import com.cassinisys.drdo.model.bom.LotInstance;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam reddy on 16-10-2019.
 */
public class ReceiveItemDto {

    private BomInstanceItem item;

    private Double issuedQuantity = 0.0;

    private List<String> certificateNumbers = new ArrayList<>();

    private List<ItemInstance> itemInstances = new ArrayList<>();

    private List<LotInstance> lotInstances = new ArrayList<>();

    public BomInstanceItem getItem() {
        return item;
    }

    public void setItem(BomInstanceItem item) {
        this.item = item;
    }

    public Double getIssuedQuantity() {
        return issuedQuantity;
    }

    public void setIssuedQuantity(Double issuedQuantity) {
        this.issuedQuantity = issuedQuantity;
    }

    public List<ItemInstance> getItemInstances() {
        return itemInstances;
    }

    public void setItemInstances(List<ItemInstance> itemInstances) {
        this.itemInstances = itemInstances;
    }

    public List<LotInstance> getLotInstances() {
        return lotInstances;
    }

    public void setLotInstances(List<LotInstance> lotInstances) {
        this.lotInstances = lotInstances;
    }

    public List<String> getCertificateNumbers() {
        return certificateNumbers;
    }

    public void setCertificateNumbers(List<String> certificateNumbers) {
        this.certificateNumbers = certificateNumbers;
    }
}
