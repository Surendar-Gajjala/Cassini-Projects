package com.cassinisys.drdo.model.dto;

/**
 * Created by subramanyam reddy on 27-11-2018.
 */
public class NotificationDto {

    private Integer inwardItems = 0;
    private Integer inwards = 0;
    private Integer requests = 0;
    private Integer issues = 0;
    private Integer returns = 0;
    private Integer failures = 0;
    private Integer dispatches = 0;
    private Integer gatePasses = 0;
    private Integer failureProcesses = 0;
    private Integer expiryItems = 0;
    private Integer toExpire = 0;

    private Integer readMessages = 0;

    public Integer getInwards() {
        return inwards;
    }

    public void setInwards(Integer inwards) {
        this.inwards = inwards;
    }

    public Integer getRequests() {
        return requests;
    }

    public void setRequests(Integer requests) {
        this.requests = requests;
    }

    public Integer getIssues() {
        return issues;
    }

    public void setIssues(Integer issues) {
        this.issues = issues;
    }

    public Integer getReturns() {
        return returns;
    }

    public void setReturns(Integer returns) {
        this.returns = returns;
    }

    public Integer getFailures() {
        return failures;
    }

    public void setFailures(Integer failures) {
        this.failures = failures;
    }

    public Integer getDispatches() {
        return dispatches;
    }

    public void setDispatches(Integer dispatches) {
        this.dispatches = dispatches;
    }

    public Integer getGatePasses() {
        return gatePasses;
    }

    public void setGatePasses(Integer gatePasses) {
        this.gatePasses = gatePasses;
    }

    public Integer getInwardItems() {
        return inwardItems;
    }

    public void setInwardItems(Integer inwardItems) {
        this.inwardItems = inwardItems;
    }

    public Integer getFailureProcesses() {
        return failureProcesses;
    }

    public void setFailureProcesses(Integer failureProcesses) {
        this.failureProcesses = failureProcesses;
    }

    public Integer getExpiryItems() {
        return expiryItems;
    }

    public void setExpiryItems(Integer expiryItems) {
        this.expiryItems = expiryItems;
    }

    public Integer getReadMessages() {
        return readMessages;
    }

    public void setReadMessages(Integer readMessages) {
        this.readMessages = readMessages;
    }

    public Integer getToExpire() {
        return toExpire;
    }

    public void setToExpire(Integer toExpire) {
        this.toExpire = toExpire;
    }
}
