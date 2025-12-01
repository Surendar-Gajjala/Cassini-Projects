package com.cassinisys.plm.service.activitystream.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by subramanyam on 24-07-2020.
 */
@Data
@AllArgsConstructor
public class ASProblemItemUpdate {
    private String itemNumber;
    private String property;
    private String oldValue;
    private String newValue;
}
