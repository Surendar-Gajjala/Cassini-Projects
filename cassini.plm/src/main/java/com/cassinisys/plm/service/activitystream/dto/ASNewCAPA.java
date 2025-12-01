package com.cassinisys.plm.service.activitystream.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by subramanyam on 22-07-2020.
 */
@Data
@AllArgsConstructor
public class ASNewCAPA {
    private Integer id;
    private String rootCause;
    private String preventiveAction;
    private String correctiveAction;
}
