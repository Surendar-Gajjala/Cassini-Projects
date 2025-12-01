package com.cassinisys.plm.service.activitystream.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ASNewTaskDTO implements Serializable {
    private String phase;
    private String activity;
    private String task;
}
