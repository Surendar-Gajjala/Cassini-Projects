package com.cassinisys.plm.service.activitystream.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by subramanyam on 28-07-2020.
 */
@Data
@AllArgsConstructor
public class ASCAPAAudit {
    private Integer id;
    private String rootCause;
    private String auditResult;
    private String auditNotes;
}
