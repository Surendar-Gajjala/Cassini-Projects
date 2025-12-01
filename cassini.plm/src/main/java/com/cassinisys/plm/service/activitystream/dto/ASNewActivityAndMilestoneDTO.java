package com.cassinisys.plm.service.activitystream.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ASNewActivityAndMilestoneDTO implements Serializable {
    private String phase;
    private String name;
}
