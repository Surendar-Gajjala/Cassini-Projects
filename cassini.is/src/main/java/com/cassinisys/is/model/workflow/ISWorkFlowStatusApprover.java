package com.cassinisys.is.model.workflow;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Rajabrahmachary on 19-05-2017.
 */

@Entity
@Table(name = "IS_WORKFLOWSTATUSAPPROVER")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "PLM")
public class ISWorkFlowStatusApprover extends ISWorkFlowStatusAssignment {

    @ApiObjectField(required = true)
    @Column(name = "VOTE")
    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.is.model.workflow.ApproverVote")})
    private ApproverVote vote;

    @ApiObjectField(required = true)
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIMESTAMP")
    private Date timeStamp;

    public ISWorkFlowStatusApprover() {
        super.setAssignmentType(WorkflowAssigementType.APPROVER);
    }

    public ApproverVote getVote() {
        return vote;
    }

    public void setVote(ApproverVote vote) {
        this.vote = vote;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
