package com.cassinisys.is.model.procm.dto;

import com.cassinisys.platform.model.common.Header;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Nageshreddy on 26-09-2018.
 */
public class StoresReportDTO implements Serializable {

    @Header("Item No")
    private Integer serialNo;
    private String itemNumber;
    private String item;
    private String unit;
    private List<Double> storesQty;
    private Double totalQty;

    public StoresReportDTO(String itemNumber, String item, String unit, List<Double> storesQty, Double totalQty) {
        this.itemNumber = itemNumber;
        this.item = item;
        this.unit = unit;
        this.storesQty = storesQty;
        this.totalQty = totalQty;
    }

    public Integer getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<Double> getStoresQty() {
        return storesQty;
    }

    public void setStoresQty(List<Double> storesQty) {
        this.storesQty = storesQty;
    }

    public Double getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(Double totalQty) {
        this.totalQty = totalQty;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }
}
