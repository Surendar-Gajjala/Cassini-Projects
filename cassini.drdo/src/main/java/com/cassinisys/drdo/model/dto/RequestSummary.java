package com.cassinisys.drdo.model.dto;

import com.cassinisys.drdo.model.transactions.Request;

/**
 * Created by subra on 12-08-2019.
 */
public class RequestSummary {

    private Request request;

    private String itemName;

    private String type;

    private Double requestedQty = 0.0;

    private Double acceptedQty = 0.0;

    private Double approvedQty = 0.0;

    private Double rejectedQty = 0.0;

    private Double issuedQty = 0.0;

    private Double qcApprovedQty = 0.0;

    private Double receivedQty = 0.0;

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Double getRequestedQty() {
        return requestedQty;
    }

    public void setRequestedQty(Double requestedQty) {
        this.requestedQty = requestedQty;
    }

    public Double getAcceptedQty() {
        return acceptedQty;
    }

    public void setAcceptedQty(Double acceptedQty) {
        this.acceptedQty = acceptedQty;
    }

    public Double getApprovedQty() {
        return approvedQty;
    }

    public void setApprovedQty(Double approvedQty) {
        this.approvedQty = approvedQty;
    }

    public Double getIssuedQty() {
        return issuedQty;
    }

    public void setIssuedQty(Double issuedQty) {
        this.issuedQty = issuedQty;
    }

    public Double getQcApprovedQty() {
        return qcApprovedQty;
    }

    public void setQcApprovedQty(Double qcApprovedQty) {
        this.qcApprovedQty = qcApprovedQty;
    }

    public Double getReceivedQty() {
        return receivedQty;
    }

    public void setReceivedQty(Double receivedQty) {
        this.receivedQty = receivedQty;
    }

    public Double getRejectedQty() {
        return rejectedQty;
    }

    public void setRejectedQty(Double rejectedQty) {
        this.rejectedQty = rejectedQty;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
