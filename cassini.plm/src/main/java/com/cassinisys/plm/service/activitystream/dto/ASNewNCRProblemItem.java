package com.cassinisys.plm.service.activitystream.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by subramanyam on 22-07-2020.
 */
@Data
@AllArgsConstructor
public class ASNewNCRProblemItem {
    private Integer id;
    private String partName;
    private String partNumber;
    private String partType;
}
