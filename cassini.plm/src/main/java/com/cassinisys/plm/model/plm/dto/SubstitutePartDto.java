package com.cassinisys.plm.model.plm.dto;

import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import lombok.Data;

/**
 * Created by subramanyam on 15-12-2020.
 */
@Data
public class SubstitutePartDto {
    private Integer id;
    private Integer parent;
    private Integer part;
    private Integer replacementPart;
    private Integer parentRevision;
    private Integer replacementPartRevision;

    private String parentItemNumber;
    private String parentItemName;
    private String parentItemRevision;
    private String replacementPartNumber;
    private String replacementPartName;
    private String replacementPartType;
    private String replacementPartDescription;
    private PLMLifeCyclePhase lifeCyclePhase;
    private String replacementRevision;


}
