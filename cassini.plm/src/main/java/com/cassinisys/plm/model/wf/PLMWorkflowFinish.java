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
@Table(name = "PLM_WORKFLOWFINISH")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName(value = "PLMWorkflowFinish")
public class PLMWorkflowFinish extends PLMWorkflowStatus {
    public PLMWorkflowFinish() {
        super(PLMObjectType.PLMWORKFLOWFINISH);
        super.setType(WorkflowStatusType.FINISH);
    }
}
