package com.cassinisys.plm.model.mobile;

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
 * Created by subramanyam on 02-12-2020.
 */
@Data
public class DCRDetails {
    private Integer id;
    private String impactAnalysis;
    private String type;
    private String crNumber;
    private String title;
    private String descriptionOfChange;
    private String reasonForChange;
    private String proposedChanges;
    private String urgency;
    private String originator;
    private String requestedBy;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestedDate;

    private String modifiedBy;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    private WorkflowStatusType statusType;
    private String status;
    private String changeAnalyst;
    private String notes;
    private String changeReasonType;
    private String changeType;
    private Integer workflow;
}
