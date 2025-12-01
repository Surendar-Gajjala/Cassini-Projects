package com.cassinisys.plm.model.wf.dto;

import com.cassinisys.plm.model.wf.PLMWorkFlowStatusAssignment;
import com.cassinisys.plm.model.wf.UserTaskStatus;
import lombok.Data;

@Data
public class UserTaskDto {
    private String number;
    private String taskType;
    private String objectType;
    private String name;
    private Integer objectId;
    private Integer parentObjectId;
    private Integer workflow;
    private Integer currentStatus;
    private Integer workflowStatus;
    private PLMWorkFlowStatusAssignment assignment;
    private UserTaskStatus status = UserTaskStatus.PENDING;
    private Integer source;
    private Integer context;
}
