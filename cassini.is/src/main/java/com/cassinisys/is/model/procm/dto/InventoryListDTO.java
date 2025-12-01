package com.cassinisys.is.model.procm.dto;
/**
 * Model for  InventoryListDTO
 */

import java.util.ArrayList;
import java.util.List;

public class InventoryListDTO {

    private String itemNumber;

    private String itemName;

    private Integer storeInvQtyTotal;

    private String description;

    private String itemType;

    private String resourceType;

    private String units;

    private List<StoreInventoryDTO> StoreDetailsList = new ArrayList<>();

    private List<ProjectInventoryDTO> projectInventoryDetailsList = new ArrayList<>();

    private Double projectInvQtyTotal;

    private Double totalQuantity;

    private Boolean first;

    private Boolean last;

    private Long totalPages;

    private Long totalElements;

    private int numberOfElements;

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getStoreInvQtyTotal() {
        return storeInvQtyTotal;
    }

    public void setStoreInvQtyTotal(Integer storeInvQtyTotal) {
        this.storeInvQtyTotal = storeInvQtyTotal;
    }

    public List<StoreInventoryDTO> getStoreDetailsList() {
        return StoreDetailsList;
    }

    public void setStoreDetailsList(List<StoreInventoryDTO> storeDetailsList) {
        StoreDetailsList = storeDetailsList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public StoreInventoryDTO getStoreDTO(Integer storeId) {
        for (StoreInventoryDTO dto : StoreDetailsList) {
            if (dto.getStoreId().equals(storeId)) {
                return dto;
            }
        }
        return null;
    }

    public void addtoStoreDetailsList(StoreInventoryDTO storeInventoryDTO) {
        if (storeInventoryDTO != null) {
            getStoreDetailsList().add(storeInventoryDTO);
        }
    }

    public List<ProjectInventoryDTO> getProjectInventoryDetailsList() {
        return projectInventoryDetailsList;
    }

    public void setProjectInventoryDetailsList(List<ProjectInventoryDTO> projectInventoryDetailsList) {
        this.projectInventoryDetailsList = projectInventoryDetailsList;
    }

    public ProjectInventoryDTO getProjectInventoryDTO(Integer projectID) {
        for (ProjectInventoryDTO dto : projectInventoryDetailsList) {
            if (dto.getProjectId().equals(projectID)) {
                return dto;
            }
        }
        return null;
    }

    public Double getProjectInvQtyTotal() {
        return projectInvQtyTotal;
    }

    public void setProjectInvQtyTotal(Double projectInvQtyTotal) {
        this.projectInvQtyTotal = projectInvQtyTotal;
    }

    public Double getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Double totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Boolean getFirst() {
        return first;
    }

    public void setFirst(Boolean first) {
        this.first = first;
    }

    public Boolean getLast() {
        return last;
    }

    public void setLast(Boolean last) {
        this.last = last;
    }

    public Long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Long totalPages) {
        this.totalPages = totalPages;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }
}
