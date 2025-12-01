package com.cassinisys.drdo.model.dto;

import com.cassinisys.drdo.model.transactions.InwardItemInstance;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam reddy on 19-11-2018.
 */
public class InwardReportDto {

    private List<InwardItemDto> inwardItems = new ArrayList<>();

    private List<InwardItemInstance> onHoldInstances = new ArrayList<>();

    private List<InwardItemInstance> returnInstances = new ArrayList<>();

    private List<InwardItemInstance> failedInstances = new ArrayList<>();
    private List<InwardItemInstance> acceptedInstances = new ArrayList<>();

    public List<InwardItemInstance> getOnHoldInstances() {
        return onHoldInstances;
    }

    public void setOnHoldInstances(List<InwardItemInstance> onHoldInstances) {
        this.onHoldInstances = onHoldInstances;
    }

    public void setOnHoldIntances(List<InwardItemInstance> onHoldInstances) {
        this.onHoldInstances = onHoldInstances;
    }

    public List<InwardItemInstance> getReturnInstances() {
        return returnInstances;
    }

    public void setReturnInstances(List<InwardItemInstance> returnInstances) {
        this.returnInstances = returnInstances;
    }

    public List<InwardItemInstance> getFailedInstances() {
        return failedInstances;
    }

    public void setFailedInstances(List<InwardItemInstance> failedInstances) {
        this.failedInstances = failedInstances;
    }

    public void setFailedIntances(List<InwardItemInstance> failedInstances) {
        this.failedInstances = failedInstances;
    }

    public List<InwardItemDto> getInwardItems() {

        return inwardItems;
    }

    public void setInwardItems(List<InwardItemDto> inwardItems) {
        this.inwardItems = inwardItems;
    }

    public List<InwardItemInstance> getAcceptedInstances() {
        return acceptedInstances;
    }

    public void setAcceptedInstances(List<InwardItemInstance> acceptedInstances) {
        this.acceptedInstances = acceptedInstances;
    }
}
