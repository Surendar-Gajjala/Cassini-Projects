package com.cassinisys.plm.model.pqm.dto;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.plm.model.pqm.ReporterType;
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
public class ProblemReportsDto {
    Integer tagsCount = 0;
    private Integer id;
    private String prNumber;
    private String prType;
    private String problem;
    private String product;
    private String revision;
    private String description;
    private String stepsToReproduce;
    private ReporterType reporterType;
    private String reportedBy;
    private Boolean isImplemented = false;
    private Boolean released = false;
    private String otherReported;
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.DATE)
    private Date reportedDate;
    private String qualityAnalyst;
    private String status;
    private WorkflowStatusType statusType;
    private Integer workflow;
    private String failureType;
    private String severity;
    private String disposition;
    private String createdBy;
    private String modifiedBy;
    private Boolean onHold = false;
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;
    private Integer ecr;
    private Integer problemReport;
    private String objectType;
    private Boolean startWorkflow = Boolean.FALSE;
    private String workflowSettingStatus = "NONE";
    private String workflowSettingStatusType = "UNDEFINED";
    private Boolean finishWorkflow = false;
    private Boolean cancelWorkflow = false;
}
