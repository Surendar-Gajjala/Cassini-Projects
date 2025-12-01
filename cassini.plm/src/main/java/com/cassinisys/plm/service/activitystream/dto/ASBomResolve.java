package com.cassinisys.plm.service.activitystream.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by subramanyam on 28-07-2020.
 */
@Data
@AllArgsConstructor
public class ASBomResolve {
    private String oldName;
    private String oldRevision;
    private String oldLifecycle;
    private String newName;
    private String newRevision;
    private String newLifecycle;
}
