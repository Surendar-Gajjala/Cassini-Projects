package com.cassinisys.is.model.workflow;

import com.cassinisys.is.model.core.ISObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by reddy on 5/22/17.
 */
@Entity
@Table(name = "IS_WORKFLOWFINISH")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "IS")
public class ISWorkflowFinish extends ISWorkflowStatus {
    public ISWorkflowFinish() {
        super(ISObjectType.ISWORKFLOWFINISH);
    }
}
