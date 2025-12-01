package com.cassinisys.plm.model.wf;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Rajabrahmachary on 19-05-2017.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_WORKFLOWSTATUSACKNOWLEDGER")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMWorkFlowStatusAcknowledger extends PLMWorkFlowStatusAssignment {

    @Column(name = "ACKNOWLEDGED")
    private boolean acknowledged;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIMESTAMP")
    private Date timeStamp;

    @Column(name = "NOTE")
    private String note;

    public PLMWorkFlowStatusAcknowledger() {
        super.setAssignmentType(WorkflowAssigementType.ACKNOWLEDGER);
    }



}
