package com.cassinisys.plm.model.wf.dto;

import com.cassinisys.plm.model.wf.UserTaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class TasksByStatusDTO implements Serializable {
    private UserTaskStatus status;
    private long count;
}
