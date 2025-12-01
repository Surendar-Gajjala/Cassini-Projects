package com.cassinisys.platform.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ASWorkflowPromote implements Serializable {
    private Integer workflowId;
    private String workflowName;
    private String fromActivityName;
    private String toActivityName;
}
