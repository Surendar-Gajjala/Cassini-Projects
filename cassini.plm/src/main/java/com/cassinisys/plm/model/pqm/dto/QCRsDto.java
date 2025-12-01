package com.cassinisys.plm.model.pqm.dto;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.plm.model.pqm.QCRFor;
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
public class QCRsDto {
    Integer tagsCount = 0;
    private Integer id;
    private String qcrNumber;
    private String qcrType;
    private QCRFor qcrFor;
    private String title;
    private String description;
    private String qualityAdministrator;
    private String status;
    private WorkflowStatusType statusType;
    private Integer workflow;
    private String wfName;
    private String createdBy;
    private String modifiedBy;
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
    private Boolean finishWorkflow = false;
    private Boolean cancelWorkflow = false;
}
