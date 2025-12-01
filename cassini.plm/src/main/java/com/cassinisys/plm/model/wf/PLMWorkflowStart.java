package com.cassinisys.plm.model.wf;

import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by reddy on 5/22/17.
 */
@Entity
@Table(name = "PLM_WORKFLOWSTART")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName(value = "PLMWorkflowStart")
public class PLMWorkflowStart extends PLMWorkflowStatus {
    public PLMWorkflowStart() {
        super(PLMObjectType.PLMWORKFLOWSTART);
        super.setType(WorkflowStatusType.START);
        super.setFlag(WorkflowStatusFlag.INPROCESS);
    }
}
