package com.cassinisys.plm.model.cm.dto;

import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam on 26-06-2021.
 */
@Data
public class AffectedItemDto {
    private Integer id;
    private Integer item;
    private Integer toItem;
    private String itemName;
    private String itemNumber;
    private String itemTypeName;
    private String description;
    private String fromRevision;
    private String toRevision;
    private PLMLifeCyclePhase fromLifecyclePhase;
    private PLMLifeCyclePhase toLifecyclePhase;
    private PLMLifeCyclePhase oldToLifecyclePhase;
    private List<PLMLifeCyclePhase> normalLifecyclePhases = new ArrayList<>();
    private List<PLMLifeCyclePhase> releasedLifecyclePhases = new ArrayList<>();
    private List<PLMLifeCyclePhase> rejectedLifecyclePhases = new ArrayList<>();
}
