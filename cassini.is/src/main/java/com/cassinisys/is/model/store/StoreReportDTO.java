package com.cassinisys.is.model.store;

import com.cassinisys.platform.model.common.Header;

import java.io.Serializable;

/**
 * Created by Nageshreddy on 25-09-2018.
 */
public class StoreReportDTO implements Serializable {

    private Integer serialNo;
    @Header("Item No")
    private String itemNumber;
    private String item;
    private String units;
    @Header("Opening Qty")
    private Double openingStock;
    @Header("Received Qty")
    private Double received;
    @Header("Issued Qty")
    private Double issued;
    @Header("Returned Qty")
    private Double returned;
    @Header("Closing Qty")
    private Double closingStock;

    public StoreReportDTO(String itemNumber, String item, String units, Double openingStock, Double received, Double issued, Double returned, Double closingStock) {
        this.itemNumber = itemNumber;
        this.item = item;
        this.units = units;
        this.openingStock = openingStock;
        this.received = received;
        this.issued = issued;
        this.returned = returned;
        this.closingStock = closingStock;
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

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public Double getOpeningStock() {
        return openingStock;
    }

    public void setOpeningStock(Double openingStock) {
        this.openingStock = openingStock;
    }

    public Double getReceived() {
        return received;
    }

    public void setReceived(Double received) {
        this.received = received;
    }

    public Double getIssued() {
        return issued;
    }

    public void setIssued(Double issued) {
        this.issued = issued;
    }

    public Double getReturned() {
        return returned;
    }

    public void setReturned(Double returned) {
        this.returned = returned;
    }

    public Double getClosingStock() {
        return closingStock;
    }

    public void setClosingStock(Double closingStock) {
        this.closingStock = closingStock;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }
}
