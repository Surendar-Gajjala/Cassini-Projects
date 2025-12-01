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
@Table(name = "PLM_WORKFLOWDEFINITIONFINISH")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName(value = "PLMWorkflowDefinitionFinish")
public class PLMWorkflowDefinitionFinish extends PLMWorkflowDefinitionStatus {
    public PLMWorkflowDefinitionFinish() {
        super(PLMObjectType.PLMWORKFLOWDEFINITIONFINISH);
        super.setType(WorkflowStatusType.FINISH);
    }
}
