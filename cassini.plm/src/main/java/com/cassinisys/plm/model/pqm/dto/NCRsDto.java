package com.cassinisys.plm.model.pqm.dto;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.cassinisys.plm.model.wf.WorkflowStatusType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by subramanyam on 12-06-2020.
 */
@Data
public class NCRsDto {
    Integer tagsCount = 0;
    private Integer id;
    private String ncrNumber;
    private String ncrType;
    private String title;
    private String description;
    private String reportedBy;
    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportedDate;
    private String qualityAnalyst;
    private String status;
    private Integer workflow;
    private String failureType;
    private String severity;
    private String disposition;
    private String createdBy;
    private String modifiedBy;
    private WorkflowStatusType statusType;
    private Boolean isImplemented = false;
    private Boolean released = false;
    private Boolean onHold = false;
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;
    private String objectType;
    private Boolean startWorkflow = Boolean.FALSE;
    private String workflowSettingStatus = "NONE";
    private String workflowSettingStatusType = "UNDEFINED";
    private Boolean finishWorkflow = Boolean.FALSE;
    private Boolean cancelWorkflow = false;
}
