package com.cassinisys.plm.model.dto;

import lombok.Data;

import javax.persistence.Transient;

@Data
public class WorkflowStatusDTO {
    @Transient
    private Boolean startWorkflow = Boolean.FALSE;
    @Transient
    private String workflowSettingStatus = "NONE";
    @Transient
    private String workflowSettingStatusType = "UNDEFINED";
    @Transient
    private Boolean onHold = false;
    @Transient
    private Boolean finishWorkflow = Boolean.FALSE;
    @Transient
    private Boolean cancelWorkflow = Boolean.FALSE;

}
