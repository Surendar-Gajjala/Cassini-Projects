package com.cassinisys.plm.model.cm.dto;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.cassinisys.plm.model.cm.RequesterType;
import com.cassinisys.plm.model.wf.WorkflowStatusType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by CassiniSystems on 09-06-2020.
 */
@Data
public class ECRDto {

    Integer tagsCount = 0;
    private Integer id;
    private String crNumber;
    private Integer changeRequest;
    private String type;
    private String title;
    private String descriptionOfChange;
    private String reasonForChange;
    private String proposedChanges;
    private String originator;
    private String requestedBy;
    private String changeReasonType;
    private String createdBy;
    private String changeAnalyst;
    private String status;
    private WorkflowStatusType statusType;
    private String urgency;
    private Boolean isApproved = Boolean.FALSE;
    private Boolean isImplemented = false;
    private Boolean onHold = false;
    private String modifiedBy;
    private String urgencyPrint;
    private String objectType;
    private String subType;
    private Boolean startWorkflow = Boolean.FALSE;
    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestedDate;
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date releasedDate;
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;
    private RequesterType requesterType;
    private String workflowSettingStatus = "NONE";
    private String workflowSettingStatusType = "UNDEFINED";
    private Boolean finishWorkflow = false;
    private Boolean cancelWorkflow = false;
}
