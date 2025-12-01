package com.cassinisys.is.model.procm.dto;

/**
 * Created by swapna on 25/08/18.
 */
public class ProjectInventoryDTO {

    private Integer projectId;

    private String projectName;

    private Double quantity;

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

}
