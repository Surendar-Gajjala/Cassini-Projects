package com.cassinisys.plm.model.plm.dto;

import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.plm.ReplacementType;
import lombok.Data;

/**
 * Created by subramanyam on 15-12-2020.
 */
@Data
public class AlternatePartDto {
    private Integer id;
    private Integer part;
    private Integer replacementPart;
    private Integer replacementPartRevision;
    private String replacementPartNumber;
    private String replacementPartName;
    private String replacementPartType;
    private String replacementPartDescription;
    private PLMLifeCyclePhase lifeCyclePhase;
    private String replacementRevision;
    private ReplacementType direction;

}
