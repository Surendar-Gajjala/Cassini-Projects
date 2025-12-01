package com.cassinisys.plm.model.pqm.dto;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.plm.model.wf.WorkflowStatusType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by subramanyam on 18-06-2020.
 */
@Data
public class InspectionsDto {

    Integer tagsCount = 0;
    private Integer id;
    private Integer item;
    private Integer material;
    private String inspectionNumber;
    private String inspectionPlan;
    private String assignedTo;
    private Integer workflow;
    private String status;
    private WorkflowStatusType statusType;
    private String deviationSummary;
    private String notes;
    private String createdBy;
    private String modifiedBy;
    private String productName;
    private String materialName;
    private Boolean released = false;
    private String revision;
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;
    private String objectType;
    private String subType;
    private Boolean onHold = false;
    private Boolean startWorkflow = Boolean.FALSE;
    private String workflowSettingStatus = "NONE";
    private String workflowSettingStatusType = "UNDEFINED";
    private Boolean finishWorkflow = Boolean.FALSE;
    private Boolean cancelWorkflow = false;
}
