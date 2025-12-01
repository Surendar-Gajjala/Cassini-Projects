package com.cassinisys.drdo.model.dto;

import com.cassinisys.drdo.model.bom.*;
import com.cassinisys.drdo.model.transactions.Inward;
import com.cassinisys.drdo.model.transactions.Issue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subra on 13-12-2018.
 */
public class UpnDetailsDto {

    private List<ItemInstanceStatusHistory> statusHistories = new ArrayList<>();

    private List<LotInstance> lotInstances = new ArrayList<>();

    private List<LotInstanceHistory> lotHistories = new ArrayList<>();

    private ItemInstance itemInstance;

    private String certificateNumber;

    private BomItem bomItem;

    private List<Inward> inwards = new ArrayList<>();

    private String storageLocation;

    private Bom system;

    private BomInstance missile;

    private BomGroup section;

    private BomGroup subSystem;

    private BomGroup unit;

    private Double availableLotQuantity = 0.0;

    private Issue issue;

    public List<ItemInstanceStatusHistory> getStatusHistories() {
        return statusHistories;
    }

    public void setStatusHistories(List<ItemInstanceStatusHistory> statusHistories) {
        this.statusHistories = statusHistories;
    }

    public BomItem getBomItem() {
        return bomItem;
    }

    public void setBomItem(BomItem bomItem) {
        this.bomItem = bomItem;
    }

    public List<Inward> getInwards() {
        return inwards;
    }

    public void setInwards(List<Inward> inwards) {
        this.inwards = inwards;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public Bom getSystem() {
        return system;
    }

    public void setSystem(Bom system) {
        this.system = system;
    }

    public BomInstance getMissile() {
        return missile;
    }

    public void setMissile(BomInstance missile) {
        this.missile = missile;
    }

    public BomGroup getSection() {
        return section;
    }

    public void setSection(BomGroup section) {
        this.section = section;
    }

    public BomGroup getSubSystem() {
        return subSystem;
    }

    public void setSubSystem(BomGroup subSystem) {
        this.subSystem = subSystem;
    }

    public BomGroup getUnit() {
        return unit;
    }

    public void setUnit(BomGroup unit) {
        this.unit = unit;
    }

    public ItemInstance getItemInstance() {
        return itemInstance;
    }

    public void setItemInstance(ItemInstance itemInstance) {
        this.itemInstance = itemInstance;
    }

    public List<LotInstanceHistory> getLotHistories() {
        return lotHistories;
    }

    public void setLotHistories(List<LotInstanceHistory> lotHistories) {
        this.lotHistories = lotHistories;
    }

    public List<LotInstance> getLotInstances() {
        return lotInstances;
    }

    public void setLotInstances(List<LotInstance> lotInstances) {
        this.lotInstances = lotInstances;
    }

    public Double getAvailableLotQuantity() {
        return availableLotQuantity;
    }

    public void setAvailableLotQuantity(Double availableLotQuantity) {
        this.availableLotQuantity = availableLotQuantity;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }
}
