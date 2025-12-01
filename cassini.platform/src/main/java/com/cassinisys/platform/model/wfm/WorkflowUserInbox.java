package com.cassinisys.platform.model.wfm;


import com.cassinisys.platform.model.col.UserInbox;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "WORKFLOWUSERINBOX")
@PrimaryKeyJoinColumn(name = "WFMINBOX_ID")
public class WorkflowUserInbox extends UserInbox {

    @Column(name = "WORKFLOW_ID", nullable = false)
    private Integer workflowId;


}
