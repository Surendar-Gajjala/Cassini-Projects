package com.cassinisys.plm.model.pqm.dto;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.wf.WorkflowStatusType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by subramanyam on 05-06-2020.
 */
@Data
public class InspectionPlansDto {
    Integer tagsCount = 0;
    private Integer id;
    private String name;
    private Integer latestRevision;
    private String number;
    private String description;
    private String type;
    private String notes;
    private String createdBy;
    private String modifiedBy;
    private String productName;
    private String materialName;
    private String status;
    private WorkflowStatusType statusType;
    private String objectType;
    private Boolean onHold = false;
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;
    private String revision;
    private PLMLifeCyclePhase lifeCyclePhase;
    private Boolean released = false;
    private Boolean rejected = false;
    private Integer workflow;
    private Integer product;
    private Integer material;
    private Boolean startWorkflow = Boolean.FALSE;
    private String workflowSettingStatus = "NONE";
    private String workflowSettingStatusType = "UNDEFINED";
    private Boolean finishWorkflow = false;
    private Boolean cancelWorkflow = false;
}
