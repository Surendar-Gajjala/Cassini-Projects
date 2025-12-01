package com.cassinisys.is.model.procm.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Model for  StoreInventoryDTO
 */

public class StoreInventoryDTO {
    private Integer storeId;

    private String storeName;

    private Integer quantity;

    private Double totalQuantity;

    private List<ProjectInventoryDTO> projectStoreInvDetailsList = new ArrayList<>();

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Double totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public List<ProjectInventoryDTO> getProjectStoreInvDetailsList() {
        return projectStoreInvDetailsList;
    }

    public void setProjectStoreInvDetailsList(List<ProjectInventoryDTO> projectStoreInvDetailsList) {
        this.projectStoreInvDetailsList = projectStoreInvDetailsList;
    }
}
