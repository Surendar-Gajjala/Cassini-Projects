package com.cassinisys.plm.model.cm.dto;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.wf.WorkflowStatusType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by CassiniSystems on 10-06-2020.
 */
@Data
public class DCRDto {

    Integer tagsCount = 0;
    private Integer id;
    private String crType;
    private String crNumber;
    private String type;
    private String title;
    private String descriptionOfChange;
    private String reasonForChange;
    private String proposedChanges;
    private String urgency;
    private String originator;
    private String requestedBy;
    private String changeReasonType;
    private Integer dcodcr;
    private Boolean isImplemented = Boolean.FALSE;
    private String Workflow;
    private PLMLifeCyclePhase lifeCyclePhase;
    private String status;
    private String changeAnalyst;
    private Boolean isApproved = Boolean.FALSE;
    private String rejectionReason;
    private String notes;
    private Boolean onHold = false;
    private String objectType;
    private String subType;
    private Boolean startWorkflow = Boolean.FALSE;
    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.wf.WorkflowStatusType")})
    private WorkflowStatusType statusType = WorkflowStatusType.UNDEFINED;
    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestedDate;
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date approvedDate;
    private String workflowSettingStatus = "NONE";
    private String workflowSettingStatusType = "UNDEFINED";
    private Boolean finishWorkflow = false;
    private Boolean cancelWorkflow = false;
}
