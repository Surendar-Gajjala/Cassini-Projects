package com.cassinisys.drdo.model.dto;

import com.cassinisys.drdo.model.transactions.InwardItem;

/**
 * Created by subra on 19-11-2018.
 */
public class InwardItemDto {

    private InwardItem inwardItem;

    private Integer stock = 0;

    private Double fractionalStock = 0.0;

    private Integer onHold = 0;

    private Double fractionalOnHold = 0.0;

    private Integer returned = 0;

    private Double fractionalReturned = 0.0;

    private Integer failed = 0;

    private Double fractionalFailed = 0.0;

    private Integer acceptedQty = 0;

    private Double fractionalAcceptedQty = 0.0;

    public InwardItem getInwardItem() {
        return inwardItem;
    }

    public void setInwardItem(InwardItem inwardItem) {
        this.inwardItem = inwardItem;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Double getFractionalStock() {
        return fractionalStock;
    }

    public void setFractionalStock(Double fractionalStock) {
        this.fractionalStock = fractionalStock;
    }

    public Integer getOnHold() {
        return onHold;
    }

    public void setOnHold(Integer onHold) {
        this.onHold = onHold;
    }

    public Double getFractionalOnHold() {
        return fractionalOnHold;
    }

    public void setFractionalOnHold(Double fractionalOnHold) {
        this.fractionalOnHold = fractionalOnHold;
    }

    public Integer getReturned() {
        return returned;
    }

    public void setReturned(Integer returned) {
        this.returned = returned;
    }

    public Double getFractionalReturned() {
        return fractionalReturned;
    }

    public void setFractionalReturned(Double fractionalReturned) {
        this.fractionalReturned = fractionalReturned;
    }

    public Integer getFailed() {
        return failed;
    }

    public void setFailed(Integer failed) {
        this.failed = failed;
    }

    public Double getFractionalFailed() {
        return fractionalFailed;
    }

    public void setFractionalFailed(Double fractionalFailed) {
        this.fractionalFailed = fractionalFailed;
    }

    public Integer getAcceptedQty() {
        return acceptedQty;
    }

    public void setAcceptedQty(Integer acceptedQty) {
        this.acceptedQty = acceptedQty;
    }

    public Double getFractionalAcceptedQty() {
        return fractionalAcceptedQty;
    }

    public void setFractionalAcceptedQty(Double fractionalAcceptedQty) {
        this.fractionalAcceptedQty = fractionalAcceptedQty;
    }
}
