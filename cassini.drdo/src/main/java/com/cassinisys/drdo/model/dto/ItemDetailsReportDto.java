package com.cassinisys.drdo.model.dto;

import com.cassinisys.drdo.model.bom.BomItem;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by subramanyam reddy on 29-10-2019.
 */
public class ItemDetailsReportDto {

    private BomItem item;

    private Map<Integer, ItemReportQuantitiesDto> listMap = new HashMap();

    private Double stock = 0.0;

    private Double totalShortage = 0.0;

    public BomItem getItem() {
        return item;
    }

    public void setItem(BomItem item) {
        this.item = item;
    }

    public Map<Integer, ItemReportQuantitiesDto> getListMap() {
        return listMap;
    }

    public void setListMap(Map<Integer, ItemReportQuantitiesDto> listMap) {
        this.listMap = listMap;
    }

    public Double getStock() {
        return stock;
    }

    public void setStock(Double stock) {
        this.stock = stock;
    }

    public Double getTotalShortage() {
        return totalShortage;
    }

    public void setTotalShortage(Double totalShortage) {
        this.totalShortage = totalShortage;
    }
}
