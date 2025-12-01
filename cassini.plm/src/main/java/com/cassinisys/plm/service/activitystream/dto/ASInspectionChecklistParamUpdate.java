package com.cassinisys.plm.service.activitystream.dto;

import com.cassinisys.plm.model.pqm.ChecklistResult;
import com.cassinisys.plm.model.pqm.PQMParamActualValue;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by subramanyam on 24-07-2020.
 */
@Data
@AllArgsConstructor
public class ASInspectionChecklistParamUpdate {
    private String checklistName;
    private ChecklistResult result;
    private PQMParamActualValue paramActualValue;
}
