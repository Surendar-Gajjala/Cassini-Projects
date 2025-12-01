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
@Table(name = "PLM_WORKFLOWDEFINITIONTERMINATE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName(value = "PLMWorkflowDefinitionTerminate")
public class PLMWorkflowDefinitionTerminate extends PLMWorkflowDefinitionStatus {
    public PLMWorkflowDefinitionTerminate() {
        super(PLMObjectType.PLMWORKFLOWDEFINITIONTERMINATE);
        super.setType(WorkflowStatusType.TERMINATE);
    }
}
