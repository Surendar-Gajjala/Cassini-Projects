package com.cassinisys.plm.service.activitystream.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ASProgramProjectDTO implements Serializable {
    private Integer id;
    private String name;
    private String type;
    private String groupName;
}
