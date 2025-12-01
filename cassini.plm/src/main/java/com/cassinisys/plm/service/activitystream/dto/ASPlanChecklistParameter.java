package com.cassinisys.plm.service.activitystream.dto;

import com.cassinisys.platform.model.core.DataType;
import com.cassinisys.plm.model.pqm.PQMParamValue;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ASPlanChecklistParameter implements Serializable {
    private Integer id;
    private String name;
    private String passCriteria;
    private DataType dataType;
    private PQMParamValue expectedValue;
    private String checklistName;
}
