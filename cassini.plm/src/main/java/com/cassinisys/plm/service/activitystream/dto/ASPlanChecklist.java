package com.cassinisys.plm.service.activitystream.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ASPlanChecklist implements Serializable {
    private String title;
    private String procedure;
    private String summary;
}
