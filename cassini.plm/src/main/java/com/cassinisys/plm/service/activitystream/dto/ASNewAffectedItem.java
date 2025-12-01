package com.cassinisys.plm.service.activitystream.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by subramanyam on 22-07-2020.
 */
@Data
@AllArgsConstructor
public class ASNewAffectedItem {
    private Integer id;
    private String itemName;
    private String itemNumber;
    private String revision;
    private String lifecyclePhase;
}
